package inaugural.soliloquy.io;

import inaugural.soliloquy.common.CommonModule;
import inaugural.soliloquy.io.api.WindowResolution;
import inaugural.soliloquy.io.api.dto.AssetDefinitionsDTO;
import inaugural.soliloquy.io.api.dto.AssetType;
import inaugural.soliloquy.io.audio.AudioImpl;
import inaugural.soliloquy.io.audio.entities.SoundTypeImpl;
import inaugural.soliloquy.io.audio.entities.SoundsPlayingImpl;
import inaugural.soliloquy.io.audio.factories.SoundFactoryImpl;
import inaugural.soliloquy.io.audio.infrastructure.AudioLoaderImpl;
import inaugural.soliloquy.io.graphics.GraphicsImpl;
import inaugural.soliloquy.io.graphics.assets.FontImpl;
import inaugural.soliloquy.io.graphics.bootstrap.GraphicsCoreLoopImpl;
import inaugural.soliloquy.io.graphics.bootstrap.GraphicsPreloaderImpl;
import inaugural.soliloquy.io.graphics.bootstrap.assetfactories.*;
import inaugural.soliloquy.io.graphics.renderables.*;
import inaugural.soliloquy.io.graphics.renderables.colorshifting.ColorShiftStackAggregatorImpl;
import inaugural.soliloquy.io.graphics.renderables.providers.AnimatedMouseCursorProviderImpl;
import inaugural.soliloquy.io.graphics.renderables.providers.GlobalLoopingAnimationImpl;
import inaugural.soliloquy.io.graphics.renderables.providers.StaticMouseCursorProviderImpl;
import inaugural.soliloquy.io.graphics.rendering.*;
import inaugural.soliloquy.io.graphics.rendering.factories.ShaderFactoryImpl;
import inaugural.soliloquy.io.graphics.rendering.renderers.*;
import inaugural.soliloquy.io.keyboard.KeyEventListenerImpl;
import inaugural.soliloquy.io.mouse.MouseCursorImpl;
import inaugural.soliloquy.io.mouse.MouseEventCapturingSpatialIndexImpl;
import inaugural.soliloquy.io.mouse.MouseEventHandlerImpl;
import inaugural.soliloquy.io.mouse.MouseListener;
import inaugural.soliloquy.io.persistence.graphics.renderables.providers.ProviderHandler;
import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.collections.Collections;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.int4.dirk.api.Injector;
import org.int4.dirk.di.Injectors;
import soliloquy.specs.common.persistence.PersistenceHandler;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.game.Module;
import soliloquy.specs.io.audio.entities.SoundType;
import soliloquy.specs.io.graphics.assets.*;
import soliloquy.specs.io.graphics.assets.Font;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.AnimatedMouseCursorProviderDefinition;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.GlobalLoopingAnimationDefinition;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.StaticMouseCursorProviderDefinition;
import soliloquy.specs.io.graphics.renderables.Renderable;
import soliloquy.specs.io.graphics.renderables.providers.AnimatedMouseCursorProvider;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.renderables.providers.StaticMouseCursorProvider;
import soliloquy.specs.io.graphics.rendering.WindowDisplayMode;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;
import soliloquy.specs.io.graphics.rendering.timing.FrameRateReporterAggregateOutput;

import java.awt.*;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static inaugural.soliloquy.io.api.Settings.*;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;

public class IOModule implements Module {
    private final Injector INJECTOR;

    public IOModule(CommonModule common,
                    Function<String, Object> getSetting,
                    Collection<FrameRateReporterAggregateOutput> aggregateOutputs,
                    String initialTitlebar,
                    AssetDefinitionsDTO assetDefinitionsDTO) {
        var persistenceHandler = common.provide(PersistenceHandler.class);

        var globalClock = new GlobalClockImpl();
        var timestampValidator = new TimestampValidator(null);

        var keyEventListener = new KeyEventListenerImpl(timestampValidator);

        var soundTypes = Collections.<String, SoundType>mapOf();
        var soundsPlaying = new SoundsPlayingImpl();
        var soundFactory = new SoundFactoryImpl(soundTypes::get, soundsPlaying);
        @SuppressWarnings("unchecked") var audioFiletypes =
                (Set<String>) getSetting.apply(AUDIO_FILETYPES_ID);
        var audioLoader = new AudioLoaderImpl(
                s -> soundTypes.put(s.id(), s),
                SoundTypeImpl::new,
                audioFiletypes
        );
        var audio = new AudioImpl(soundsPlaying, SoundFactoryImpl::new);

        var renderingBoundaries = new RenderingBoundariesImpl();

        Map<Class<?>, Renderer<? extends Renderable>> contentRenderers = mapOf();

        var componentRenderer = new ComponentRendererImpl(contentRenderers, renderingBoundaries,
                timestampValidator);

        var periodsPerFrameRateReportAggregate =
                (int) getSetting.apply(PERIODS_PER_FRAME_RATE_REPORT_AGGREGATE_ID);
        var frameRateReporter =
                new FrameRateReporterImpl(periodsPerFrameRateReportAggregate, aggregateOutputs);
        var frameTimer = new FrameTimerImpl(globalClock, frameRateReporter);
        var frameTimerPollingInterval = (int) getSetting.apply(FRAME_TIMER_POLLING_INTERVAL_ID);
        var semaphorePermissions = (int) getSetting.apply(FRAME_EXECUTOR_SEMAPHORE_PERMISSIONS_ID);
        var frameExecutor = new FrameExecutorImpl(componentRenderer, semaphorePermissions);

        var shaderFactory = new ShaderFactoryImpl();
        @SuppressWarnings("rawtypes") var renderersWithShader = Collections.<Renderer>setOf();
        var shaderFilenamePrefix = (String) getSetting.apply(SHADER_FILENAME_PREFIX_ID);

        @SuppressWarnings("rawtypes") var renderersWithMesh = Collections.<Renderer>setOf();
        var meshVertices = (float[]) getSetting.apply(MESH_VERTICES_ID);
        var meshUvCoords = (float[]) getSetting.apply(MESH_UV_COORDS_ID);

        var sprites = Collections.<String, Sprite>mapOf();
        var animations = Collections.<String, Animation>mapOf();
        var globalLoopingAnimations = Collections.<String, GlobalLoopingAnimation>mapOf();
        var imageAssetSets = Collections.<String, ImageAssetSet>mapOf();
        var fonts = Collections.<String, Font>mapOf();
        var mouseCursors = Collections.<String, ProviderAtTime<Long>>mapOf();

        var alphaThreshold = (float) getSetting.apply(MOUSE_CAPTURE_ALPHA_THRESHOLD_ID);
        var imageFactory = new ImageFactoryImpl(alphaThreshold);
        var spriteFactory = new SpriteFactory();
        var animationFactory = new AnimationFactory();
        Function<GlobalLoopingAnimationDefinition, GlobalLoopingAnimation>
                globalLoopingAnimationFactory =
                definition -> new GlobalLoopingAnimationImpl(definition.ID, definition.ANIMATION,
                        definition.PERIOD_MODULO_OFFSET, definition.PAUSE_TIMESTAMP,
                        timestampValidator);
        var imageAssetSetFactory = new ImageAssetSetFactory(
                sprites::get, animations::get, globalLoopingAnimations::get);
        var mouseCursorImageFactory = new MouseCursorImageFactoryImpl();
        Function<AnimatedMouseCursorProviderDefinition, AnimatedMouseCursorProvider>
                animatedMouseCursorFactory =
                definition -> new AnimatedMouseCursorProviderImpl(definition.id(),
                        definition.cursorsAtMs(), definition.msDuration(),
                        definition.periodModuloOffset(), definition.pausedTimestamp(),
                        timestampValidator);
        Function<StaticMouseCursorProviderDefinition, StaticMouseCursorProvider>
                staticMouseCursorFactory =
                definition -> new StaticMouseCursorProviderImpl(definition.id(),
                        definition.mouseCursorImageId(), null);

        var graphicsPreloaderThreadPoolSize =
                (int) getSetting.apply(GRAPHICS_PRELOADER_THREAD_POOL_SIZE_ID);
        @SuppressWarnings("unchecked") var assetTypeBatchSizes =
                (Map<AssetType, Integer>) getSetting.apply(
                        GRAPHICS_PRELOADER_ASSET_TYPE_BATCH_SIZES_ID);
        var graphicsPreloader = new GraphicsPreloaderImpl(
                assetDefinitionsDTO,
                graphicsPreloaderThreadPoolSize,
                assetTypeBatchSizes,
                imageFactory,
                FontImpl::new,
                spriteFactory,
                animationFactory,
                globalLoopingAnimationFactory,
                imageAssetSetFactory,
                mouseCursorImageFactory,
                animatedMouseCursorFactory,
                staticMouseCursorFactory,
                sprite -> sprites.put(sprite.id(), sprite),
                animation -> animations.put(animation.id(), animation),
                globalLoopingAnimation -> globalLoopingAnimations
                        .put(globalLoopingAnimation.id(), globalLoopingAnimation),
                imageAssetSet -> imageAssetSets.put(imageAssetSet.id(), imageAssetSet),
                font -> fonts.put(font.id(), font),
                animatedCursor -> mouseCursors.put(animatedCursor.id(), animatedCursor),
                staticCursor -> mouseCursors.put(staticCursor.id(), staticCursor)
        );

        var startingWindowDisplayMode =
                (WindowDisplayMode) getSetting.apply(STARTING_WINDOW_DISPLAY_MODE_ID);
        var startingWindowResolution =
                (WindowResolution) getSetting.apply(STARTING_WINDOW_RESOLUTION_ID);
        var resManager = new WindowResolutionManagerImpl(
                startingWindowDisplayMode, startingWindowResolution);
        var mouseCursor = new MouseCursorImpl(mouseCursors::get, globalClock);
        var mouseCapturing = new MouseEventCapturingSpatialIndexImpl();
        var mouseEventHandler = new MouseEventHandlerImpl(mouseCapturing);
        var mouseListener = new MouseListener(mouseEventHandler);

        var colorShiftAggregator = new ColorShiftStackAggregatorImpl();
        contentRenderers.put(
                AntialiasedLineSegmentRenderableImpl.class,
                new AntialiasedLineSegmentRenderer(resManager, timestampValidator)
        );
        contentRenderers.put(
                FiniteAnimationRenderer.class,
                new FiniteAnimationRenderer(renderingBoundaries, colorShiftAggregator,
                        timestampValidator)
        );
        contentRenderers.put(
                GlobalLoopingAnimationRenderableImpl.class,
                new GlobalLoopingAnimationRenderer(renderingBoundaries, colorShiftAggregator,
                        timestampValidator)
        );
        // TODO: Add ImageAssetSetRenderer once completed!!! _Oops._
        contentRenderers.put(
                RasterizedLineSegmentRenderableImpl.class,
                new RasterizedLineSegmentRenderer(timestampValidator)
        );
        contentRenderers.put(
                RectangleRenderableImpl.class,
                new RectangleRenderer(timestampValidator)
        );
        contentRenderers.put(
                SpriteRenderableImpl.class,
                new SpriteRenderer(renderingBoundaries, resManager, colorShiftAggregator,
                        timestampValidator)
        );
        var defaultFontColor = (Color) getSetting.apply(DEFAULT_FONT_COLOR_ID);
        contentRenderers.put(
                TextLineRenderableImpl.class,
                new TextLineRendererImpl(renderingBoundaries, defaultFontColor, resManager,
                        timestampValidator)
        );
        contentRenderers.put(
                TriangleRenderableImpl.class,
                new TriangleRenderer(timestampValidator)
        );

        @SuppressWarnings("rawtypes") var providerSubhandlers =
                Collections.<String, TypeHandler<ProviderAtTime>>mapOf();

        var providerHandler = new ProviderHandler(providerSubhandlers);

        var graphicsLoop = new GraphicsCoreLoopImpl(
                initialTitlebar,
                frameTimer,
                frameTimerPollingInterval,
                resManager,
                globalClock,
                frameExecutor,
                shaderFactory,
                renderersWithShader,
                shaderFilenamePrefix,
                MeshImpl::new,
                renderersWithMesh,
                meshVertices,
                meshUvCoords,
                graphicsPreloader,
                mouseCursor,
                mouseListener
        );

        var graphics = new GraphicsImpl(
                sprites::get,
                animations::get,
                globalLoopingAnimations::get,
                imageAssetSets::get,
                fonts::get
        );

        INJECTOR = Injectors.manual();

        INJECTOR.registerInstance(keyEventListener);
        INJECTOR.registerInstance(soundFactory);
        INJECTOR.registerInstance(audioLoader);
        INJECTOR.registerInstance(audio);
        INJECTOR.registerInstance(mouseCursor);
        INJECTOR.registerInstance(globalClock);
        INJECTOR.registerInstance(frameExecutor);
        INJECTOR.registerInstance(frameRateReporter);
        INJECTOR.registerInstance(resManager);
        INJECTOR.registerInstance(graphicsLoop);
        INJECTOR.registerInstance(graphics);
    }

    @Override
    public <T> T provide(Class<T> clazz) throws IllegalArgumentException {
        return INJECTOR.getInstance(clazz);
    }

    public <T> T provide(String instance) throws IllegalArgumentException {
        Check.ifNullOrEmpty(instance, "instance");
        throw new IllegalArgumentException("No named instances within CommonModule");
    }
}
