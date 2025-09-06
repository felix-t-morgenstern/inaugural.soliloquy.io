package inaugural.soliloquy.io;

import inaugural.soliloquy.common.CommonModule;
import inaugural.soliloquy.io.api.WindowResolution;
import inaugural.soliloquy.io.api.dto.AssetDefinitionsDTO;
import inaugural.soliloquy.io.api.dto.AssetType;
import inaugural.soliloquy.io.audio.AudioImpl;
import inaugural.soliloquy.io.audio.entities.SoundImpl;
import inaugural.soliloquy.io.audio.entities.SoundTypeImpl;
import inaugural.soliloquy.io.audio.entities.SoundsPlayingImpl;
import inaugural.soliloquy.io.audio.factories.SoundFactoryImpl;
import inaugural.soliloquy.io.audio.bootstrap.AudioLoaderImpl;
import inaugural.soliloquy.io.graphics.GraphicsImpl;
import inaugural.soliloquy.io.graphics.assets.FontImpl;
import inaugural.soliloquy.io.graphics.bootstrap.GraphicsCoreLoopImpl;
import inaugural.soliloquy.io.graphics.bootstrap.GraphicsPreloaderImpl;
import inaugural.soliloquy.io.graphics.bootstrap.assetfactories.*;
import inaugural.soliloquy.io.graphics.renderables.*;
import inaugural.soliloquy.io.graphics.renderables.colorshifting.ColorShiftStackAggregatorImpl;
import inaugural.soliloquy.io.graphics.renderables.factories.*;
import inaugural.soliloquy.io.graphics.renderables.providers.*;
import inaugural.soliloquy.io.graphics.renderables.providers.factories.*;
import inaugural.soliloquy.io.graphics.rendering.*;
import inaugural.soliloquy.io.graphics.rendering.factories.ShaderFactoryImpl;
import inaugural.soliloquy.io.graphics.rendering.renderers.*;
import inaugural.soliloquy.io.keyboard.KeyEventListenerImpl;
import inaugural.soliloquy.io.mouse.MouseCursorImpl;
import inaugural.soliloquy.io.mouse.MouseEventCapturingSpatialIndexImpl;
import inaugural.soliloquy.io.mouse.MouseEventHandlerImpl;
import inaugural.soliloquy.io.mouse.MouseListener;
import inaugural.soliloquy.io.persistence.audio.SoundHandler;
import inaugural.soliloquy.io.persistence.audio.SoundsPlayingHandler;
import inaugural.soliloquy.io.persistence.graphics.renderables.*;
import inaugural.soliloquy.io.persistence.graphics.renderables.colorshifting.ColorShiftHandler;
import inaugural.soliloquy.io.persistence.graphics.renderables.providers.*;
import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.collections.Collections;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.int4.dirk.api.Injector;
import org.int4.dirk.di.Injectors;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.persistence.PersistenceHandler;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.game.Module;
import soliloquy.specs.gamestate.entities.Setting;
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
import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

public class IOModule implements Module {
    private final Injector INJECTOR;

    public IOModule(CommonModule common,
                    @SuppressWarnings("rawtypes") Function<String, Setting> getSetting,
                    @SuppressWarnings("rawtypes") Function<String, Action> getAction,
                    Collection<FrameRateReporterAggregateOutput> aggregateOutputs,
                    String initialTitlebar,
                    AssetDefinitionsDTO assetDefinitionsDTO) {
        // ====
        // Prep
        // ====

        INJECTOR = Injectors.manual();

        var persistenceHandler = common.provide(PersistenceHandler.class);

        // ======
        // Basics
        // ======

        var globalClock = andRegister(new GlobalClockImpl());
        var timestampValidator = new TimestampValidator(null);

        // ========
        // Keyboard
        // ========

        andRegister(new KeyEventListenerImpl(timestampValidator));

        // =====
        // Audio
        // =====

        var soundTypes = Collections.<String, SoundType>mapOf();
        var soundsPlaying = andRegister(new SoundsPlayingImpl());
        var soundFactory = andRegister(new SoundFactoryImpl(soundTypes::get, soundsPlaying));
        @SuppressWarnings("unchecked") var audioFiletypes =
                (Set<String>) getSetting.apply(AUDIO_FILETYPES_ID);
        andRegister(new AudioLoaderImpl(
                s -> soundTypes.put(s.id(), s),
                SoundTypeImpl::new,
                audioFiletypes
        ));
        andRegister(new AudioImpl(soundsPlaying, SoundFactoryImpl::new));

        // =========
        // Rendering
        // =========

        var renderingBoundaries = new RenderingBoundariesImpl();

        Map<Class<?>, Renderer<? extends Renderable>> contentRenderers = mapOf();

        var componentRenderer = new ComponentRendererImpl(contentRenderers, renderingBoundaries,
                timestampValidator);

        var periodsPerFrameRateReportAggregate =
                (int) getSetting.apply(PERIODS_PER_FRAME_RATE_REPORT_AGGREGATE_ID).getValue();
        var frameRateReporter = andRegister(
                new FrameRateReporterImpl(periodsPerFrameRateReportAggregate, aggregateOutputs));
        var frameTimer = andRegister(new FrameTimerImpl(globalClock, frameRateReporter));
        var frameTimerPollingInterval =
                (int) getSetting.apply(FRAME_TIMER_POLLING_INTERVAL_ID).getValue();
        var semaphorePermissions =
                (int) getSetting.apply(FRAME_EXECUTOR_SEMAPHORE_PERMISSIONS_ID).getValue();
        var frameExecutor =
                andRegister(new FrameExecutorImpl(componentRenderer, semaphorePermissions));

        var shaderFactory = new ShaderFactoryImpl();
        var shaderFilenamePrefix = (String) getSetting.apply(SHADER_FILENAME_PREFIX_ID).getValue();

        var meshVertices = (float[]) getSetting.apply(MESH_VERTICES_ID).getValue();
        var meshUvCoords = (float[]) getSetting.apply(MESH_UV_COORDS_ID).getValue();

        // ======
        // Assets
        // ======

        var sprites = Collections.<String, Sprite>mapOf();
        var animations = Collections.<String, Animation>mapOf();
        var globalLoopingAnimations = Collections.<String, GlobalLoopingAnimation>mapOf();
        var imageAssetSets = Collections.<String, ImageAssetSet>mapOf();
        var fonts = Collections.<String, Font>mapOf();
        var mouseCursors = Collections.<String, ProviderAtTime<Long>>mapOf();

        // ===================
        // Graphics Preloading
        // ===================

        var alphaThreshold = (float) getSetting.apply(MOUSE_CAPTURE_ALPHA_THRESHOLD_ID).getValue();
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
                (int) getSetting.apply(GRAPHICS_PRELOADER_THREAD_POOL_SIZE_ID).getValue();
        @SuppressWarnings("unchecked") var assetTypeBatchSizes =
                (Map<AssetType, Integer>) getSetting.apply(
                        GRAPHICS_PRELOADER_ASSET_TYPE_BATCH_SIZES_ID).getValue();
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

        // ======
        // Window
        // ======

        var startingWindowDisplayMode =
                (WindowDisplayMode) getSetting.apply(STARTING_WINDOW_DISPLAY_MODE_ID).getValue();
        var startingWindowResolution =
                (WindowResolution) getSetting.apply(STARTING_WINDOW_RESOLUTION_ID).getValue();
        var resManager = andRegister(new WindowResolutionManagerImpl(startingWindowDisplayMode,
                startingWindowResolution));

        // =====
        // Mouse
        // =====

        var mouseCursor = andRegister(new MouseCursorImpl(mouseCursors::get, globalClock));
        var mouseCapturing = new MouseEventCapturingSpatialIndexImpl();
        var mouseEventHandler = new MouseEventHandlerImpl(mouseCapturing);
        var mouseListener = new MouseListener(mouseEventHandler);

        // =========
        // Renderers
        // =========

        var shiftAggregator = new ColorShiftStackAggregatorImpl();
        contentRenderers.put(
                AntialiasedLineSegmentRenderableImpl.class,
                new AntialiasedLineSegmentRenderer(resManager, timestampValidator)
        );
        var finiteAnimationRenderer = new FiniteAnimationRenderer(renderingBoundaries,
                resManager::windowWidthToHeightRatio, shiftAggregator, timestampValidator);
        contentRenderers.put(
                FiniteAnimationRenderer.class,
                finiteAnimationRenderer
        );
        var globalLoopingAnimationRenderer = new GlobalLoopingAnimationRenderer(renderingBoundaries, resManager::windowWidthToHeightRatio, shiftAggregator, timestampValidator);
        contentRenderers.put(
                GlobalLoopingAnimationRenderableImpl.class,
                globalLoopingAnimationRenderer
        );
        var imageAssetSetRenderer = new ImageAssetSetRenderer(renderingBoundaries, resManager::windowWidthToHeightRatio, shiftAggregator, timestampValidator);
        contentRenderers.put(
                ImageAssetSetRenderableImpl.class,
                imageAssetSetRenderer
        );
        contentRenderers.put(
                RasterizedLineSegmentRenderableImpl.class,
                new RasterizedLineSegmentRenderer(timestampValidator)
        );
        var rectangleRenderer = new RectangleRenderer(timestampValidator);
        contentRenderers.put(
                RectangleRenderableImpl.class,
                rectangleRenderer
        );
        var spriteRenderer = new SpriteRenderer(renderingBoundaries, resManager::windowWidthToHeightRatio, shiftAggregator, timestampValidator);
        contentRenderers.put(
                SpriteRenderableImpl.class,
                spriteRenderer
        );
        var defaultFontColor = (Color) getSetting.apply(DEFAULT_FONT_COLOR_ID).getValue();
        var textLineRenderer = new TextLineRenderer(renderingBoundaries, defaultFontColor, resManager::windowWidthToHeightRatio, timestampValidator);
        contentRenderers.put(
                TextLineRenderableImpl.class,
                textLineRenderer
        );
        contentRenderers.put(
                TriangleRenderableImpl.class,
                new TriangleRenderer(timestampValidator)
        );

        @SuppressWarnings("rawtypes") var renderersWithShaderAndMesh = Collections.<Renderer>setOf(
                finiteAnimationRenderer,
                globalLoopingAnimationRenderer,
                imageAssetSetRenderer,
                rectangleRenderer,
                spriteRenderer,
                textLineRenderer
        );

        // ===========
        // Renderables
        // ===========

        var antialiasedLineSegmentRenderableFactory =
                andRegister(new AntialiasedLineSegmentRenderableFactoryImpl());
        andRegister(new ComponentFactoryImpl(mouseCapturing::putRenderable,
                mouseCapturing::removeRenderable));
        var finiteAnimationRenderableFactory = andRegister(
                new FiniteAnimationRenderableFactoryImpl(renderingBoundaries, timestampValidator));
        var globalLoopingAnimationRenderableFactory = andRegister(
                new GlobalLoopingAnimationRenderableFactoryImpl(renderingBoundaries,
                        timestampValidator));
        var imageAssetSetRenderableFactory = andRegister(
                new ImageAssetSetRenderableFactoryImpl(renderingBoundaries, timestampValidator));
        var rasterizedLineSegmentRenderableFactory =
                andRegister(new RasterizedLineSegmentRenderableFactoryImpl());
        var rectangleRenderableFactory = andRegister(
                new RectangleRenderableFactoryImpl(renderingBoundaries, timestampValidator));
        var spriteRenderableFactory = andRegister(
                new SpriteRenderableFactoryImpl(renderingBoundaries, timestampValidator));
        var textLineRenderableFactory = andRegister(new TextLineRenderableFactoryImpl());
        var triangleRenderableFactory = andRegister(
                new TriangleRenderableFactoryImpl(renderingBoundaries, timestampValidator));

        // =========
        // Providers
        // =========

        var finiteLinearMovingColorProviderFactory =
                andRegister(new FiniteLinearMovingColorProviderFactoryImpl(timestampValidator));
        @SuppressWarnings("unchecked") var finiteLinearMovingProviderFactory =
                new FiniteLinearMovingProviderFactoryImpl(mapOf(
                        pairOf(
                                FiniteLinearMovingFloatBoxProvider.class.getCanonicalName(),
                                uuid -> valuesAtTimestamps -> pausedTimestamp -> timeVal ->
                                        new FiniteLinearMovingFloatBoxProvider(uuid,
                                                valuesAtTimestamps, pausedTimestamp, timeVal)
                        ),
                        pairOf(
                                FiniteLinearMovingFloatProvider.class.getCanonicalName(),
                                uuid -> valuesAtTimestamps -> pausedTimestamp -> timeVal ->
                                        new FiniteLinearMovingFloatProvider(uuid,
                                                valuesAtTimestamps, pausedTimestamp, timeVal)
                        ),
                        pairOf(
                                FiniteLinearMovingVertexProvider.class.getCanonicalName(),
                                uuid -> valuesAtTimestamps -> pausedTimestamp -> timeVal ->
                                        new FiniteLinearMovingVertexProvider(uuid,
                                                valuesAtTimestamps, pausedTimestamp, timeVal)
                        )
                ),
                        timestampValidator
                );
        var loopingLinearMovingColorProviderFactory =
                andRegister(new LoopingLinearMovingColorProviderFactoryImpl(timestampValidator));
        @SuppressWarnings({"unused", "unchecked"})
        var loopingLinearMovingProviderFactory =
                andRegister(new LoopingLinearMovingProviderFactoryImpl(mapOf(
                        pairOf(
                                LoopingLinearMovingFloatBoxProvider.class.getCanonicalName(),
                                uuid -> periodDuration -> periodModuloOffset -> valuesWithinPeriod -> pausedTimestamp -> timeVal ->
                                        new LoopingLinearMovingFloatBoxProvider(uuid,
                                                valuesWithinPeriod,
                                                periodDuration, periodModuloOffset, pausedTimestamp,
                                                timeVal
                                        )
                        ),
                        pairOf(
                                LoopingLinearMovingFloatProvider.class.getCanonicalName(),
                                uuid -> periodDuration -> periodModuloOffset -> valuesWithinPeriod -> pausedTimestamp -> timeVal ->
                                        new LoopingLinearMovingFloatProvider(
                                                uuid, valuesWithinPeriod, periodDuration,
                                                periodModuloOffset, pausedTimestamp, timeVal
                                        )
                        ),
                        pairOf(
                                LoopingLinearMovingVertexProvider.class.getCanonicalName(),
                                uuid -> periodDuration -> periodModuloOffset -> valuesWithinPeriod -> pausedTimestamp -> timeVal ->
                                        new LoopingLinearMovingVertexProvider(
                                                uuid, valuesWithinPeriod, periodDuration,
                                                periodModuloOffset, pausedTimestamp, timeVal
                                        )
                        )
                ), timestampValidator));
        var progressiveStringProviderFactory =
                andRegister(new ProgressiveStringProviderFactoryImpl(timestampValidator));
        var staticProviderFactory = andRegister(new StaticProviderFactoryImpl(timestampValidator));

        // ========
        // Handlers
        // ========

        // Audio

        var soundHandler = new SoundHandler(soundFactory::make);
        var soundsPlayingHandler = new SoundsPlayingHandler(soundHandler, soundsPlaying);

        persistenceHandler.addTypeHandler(SoundImpl.class, soundHandler);
        persistenceHandler.addTypeHandler(SoundsPlayingImpl.class, soundsPlayingHandler);

        // Providers

        @SuppressWarnings("rawtypes") var providerSubhandlers =
                Collections.<String, TypeHandler>mapOf();

        var providerHandler = new ProviderHandler(providerSubhandlers);

        providerHandler.add(FiniteLinearMovingColorProviderImpl.class.getCanonicalName(),
                new FiniteLinearMovingColorProviderHandler(finiteLinearMovingColorProviderFactory));

        var finiteLinearProviderHandler = andRegister(
                new FiniteLinearMovingProviderHandler(persistenceHandler,
                        finiteLinearMovingProviderFactory));
        listOf(
                FiniteLinearMovingFloatBoxProvider.class,
                FiniteLinearMovingFloatProvider.class,
                FiniteLinearMovingVertexProvider.class
        ).forEach(c ->
                providerHandler.add(c.getCanonicalName(), finiteLinearProviderHandler));

        providerHandler.add(LoopingLinearMovingColorProviderImpl.class.getCanonicalName(),
                new LoopingLinearMovingColorProviderHandler(
                        loopingLinearMovingColorProviderFactory));

        var loopingLinearProviderHandler =
                new LoopingLinearMovingProviderHandler(persistenceHandler,
                        loopingLinearMovingProviderFactory);
        listOf(
                LoopingLinearMovingFloatBoxProvider.class,
                LoopingLinearMovingFloatProvider.class,
                LoopingLinearMovingVertexProvider.class
        ).forEach(c ->
                providerHandler.add(c.getCanonicalName(), loopingLinearProviderHandler));

        providerHandler.add(ProgressiveStringProvider.class.getCanonicalName(),
                new ProgressiveStringProviderHandler(progressiveStringProviderFactory));

        providerHandler.add(StaticProviderImpl.class.getCanonicalName(),
                new StaticProviderHandler(persistenceHandler, staticProviderFactory,
                        timestampValidator));

        // Shifts

        var shiftHandler = new ColorShiftHandler(providerHandler);

        // Renderables

        persistenceHandler.addTypeHandler(AntialiasedLineSegmentRenderableImpl.class,
                new AntialiasedLineSegmentRenderableHandler(providerHandler,
                        antialiasedLineSegmentRenderableFactory));
        persistenceHandler.addTypeHandler(FiniteAnimationRenderableImpl.class,
                new FiniteAnimationRenderableHandler(animations::get, getAction, providerHandler,
                        shiftHandler, finiteAnimationRenderableFactory));
        persistenceHandler.addTypeHandler(GlobalLoopingAnimationRenderableImpl.class,
                new GlobalLoopingAnimationRenderableHandler(globalLoopingAnimations::get, getAction,
                        providerHandler, shiftHandler, globalLoopingAnimationRenderableFactory));
        persistenceHandler.addTypeHandler(ImageAssetSetRenderableImpl.class,
                new ImageAssetSetRenderableHandler(imageAssetSets::get, getAction, providerHandler,
                        shiftHandler, imageAssetSetRenderableFactory));
        persistenceHandler.addTypeHandler(RasterizedLineSegmentRenderableImpl.class,
                new RasterizedLineSegmentRenderableHandler(providerHandler,
                        rasterizedLineSegmentRenderableFactory));
        persistenceHandler.addTypeHandler(RectangleRenderableImpl.class,
                new RectangleRenderableHandler(getAction, providerHandler,
                        rectangleRenderableFactory));
        persistenceHandler.addTypeHandler(SpriteRenderableImpl.class,
                new SpriteRenderableHandler(sprites::get, getAction, providerHandler, shiftHandler,
                        spriteRenderableFactory));
        persistenceHandler.addTypeHandler(TextLineRenderableImpl.class,
                new TextLineRenderableHandler(fonts::get, providerHandler,
                        textLineRenderableFactory));
        persistenceHandler.addTypeHandler(TriangleRenderableImpl.class,
                new TriangleRenderableHandler(getAction, providerHandler,
                        triangleRenderableFactory));

        // ========
        // Graphics
        // ========

        andRegister(new GraphicsCoreLoopImpl(
                initialTitlebar,
                frameTimer,
                frameTimerPollingInterval,
                resManager,
                globalClock,
                frameExecutor,
                shaderFactory,
                renderersWithShaderAndMesh,
                shaderFilenamePrefix,
                MeshImpl::new,
                renderersWithShaderAndMesh,
                meshVertices,
                meshUvCoords,
                graphicsPreloader,
                mouseCursor,
                mouseListener
        ));

        andRegister(new GraphicsImpl(
                sprites::get,
                animations::get,
                globalLoopingAnimations::get,
                imageAssetSets::get,
                fonts::get
        ));
    }

    @Override
    public <T> T provide(Class<T> clazz) throws IllegalArgumentException {
        return INJECTOR.getInstance(clazz);
    }

    public <T> T provide(String instance) throws IllegalArgumentException {
        Check.ifNullOrEmpty(instance, "instance");
        throw new IllegalArgumentException("No named instances within CommonModule");
    }

    private <T> T andRegister(T registrant) {
        INJECTOR.registerInstance(registrant);

        return registrant;
    }
}
