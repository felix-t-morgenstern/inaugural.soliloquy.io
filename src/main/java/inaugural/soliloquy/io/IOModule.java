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
import inaugural.soliloquy.io.bootstrap.CoreLoopImpl;
import inaugural.soliloquy.io.bootstrap.GraphicsPreloaderImpl;
import inaugural.soliloquy.io.bootstrap.assetfactories.*;
import inaugural.soliloquy.io.graphics.GraphicsImpl;
import inaugural.soliloquy.io.graphics.assets.FontImpl;
import inaugural.soliloquy.io.graphics.renderables.*;
import inaugural.soliloquy.io.graphics.renderables.colorshifting.ColorShiftStackAggregatorImpl;
import inaugural.soliloquy.io.graphics.renderables.factories.*;
import inaugural.soliloquy.io.graphics.renderables.providers.*;
import inaugural.soliloquy.io.graphics.renderables.providers.factories.*;
import inaugural.soliloquy.io.graphics.rendering.*;
import inaugural.soliloquy.io.graphics.rendering.factories.ShaderFactoryImpl;
import inaugural.soliloquy.io.graphics.rendering.renderers.*;
import inaugural.soliloquy.io.keyboard.KeyEventHandlerImpl;
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
import inaugural.soliloquy.tools.collections.Collections;
import inaugural.soliloquy.tools.module.AbstractModule;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.apache.commons.lang3.function.TriConsumer;
import soliloquy.specs.common.entities.Methods;
import soliloquy.specs.common.persistence.PersistenceHandler;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.gamestate.entities.Setting;
import soliloquy.specs.io.audio.entities.SoundType;
import soliloquy.specs.io.bootstrap.assetfactories.definitions.AnimatedMouseCursorProviderDefinition;
import soliloquy.specs.io.bootstrap.assetfactories.definitions.GlobalLoopingAnimationDefinition;
import soliloquy.specs.io.bootstrap.assetfactories.definitions.StaticMouseCursorProviderDefinition;
import soliloquy.specs.io.graphics.assets.*;
import soliloquy.specs.io.graphics.assets.Font;
import soliloquy.specs.io.graphics.assets.Image;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.Renderable;
import soliloquy.specs.io.graphics.renderables.providers.AnimatedMouseCursorProvider;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.renderables.providers.StaticMouseCursorProvider;
import soliloquy.specs.io.graphics.rendering.WindowDisplayMode;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;
import soliloquy.specs.io.graphics.rendering.timing.FrameRateReporter;

import java.awt.*;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import static inaugural.soliloquy.io.api.Constants.*;
import static inaugural.soliloquy.io.api.Settings.*;
import static inaugural.soliloquy.io.graphics.renderables.ComponentImpl.COMPONENT_PRERENDER_HOOK;
import static inaugural.soliloquy.tools.collections.Collections.*;
import static inaugural.soliloquy.tools.reflection.Reflection.readMethods;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;
import static soliloquy.specs.io.input.mouse.MouseEventHandler.EventType;

public class IOModule extends AbstractModule {
    public IOModule(CommonModule common,
                    @SuppressWarnings("rawtypes") Function<String, Setting> getSetting,
                    Methods methods,
                    Map<String, Consumer<FrameRateReporter.Aggregate>> aggregateOutputs,
                    String initialTitlebar,
                    Map<String, String> idsForFilenames,
                    Map<String, Integer> defaultLoopStopMsById,
                    Map<String, Integer> defaultLoopRestartMsById,
                    AssetDefinitionsDTO assetDefinitionsDTO) {
        // ====
        // Prep
        // ====

        var persistenceHandler = common.provide(PersistenceHandler.class);
        @SuppressWarnings("rawtypes") TypeHandler<Map> mapHandler =
                persistenceHandler.getTypeHandler(Map.class.getCanonicalName());

        // ======
        // Basics
        // ======

        var globalClock = andRegister(new GlobalClockImpl());
        var timestampValidator = new TimestampValidator(null);

        // ========
        // Keyboard
        // ========

        var keyEventHandler = andRegister(new KeyEventHandlerImpl(timestampValidator));
        var keyEventListener = new KeyEventListenerImpl(keyEventHandler);

        // =====
        // Audio
        // =====

        var soundTypes = Collections.<String, SoundType>mapOf();
        var soundsPlaying = andRegister(new SoundsPlayingImpl());
        var soundFactory = andRegister(new SoundFactoryImpl(soundTypes::get, soundsPlaying));
        @SuppressWarnings("unchecked") var audioFiletypes =
                (Set<String>) (getSetting.apply(AUDIO_FILETYPES_ID).getValue());
        var audioLoader = new AudioLoaderImpl(
                s -> soundTypes.put(s.id(), s),
                SoundTypeImpl::new,
                audioFiletypes
        );
        andRegister(new AudioImpl(soundsPlaying, SoundFactoryImpl::new));

        // =========
        // Rendering
        // =========

        var renderingBoundaries = new RenderingBoundariesImpl();

        Map<Class<?>, Renderer<? extends Renderable>> contentRenderers = mapOf();

        var componentRenderer = new ComponentRendererImpl(
                contentRenderers,
                COMPONENT_PRERENDER_HOOK,
                renderingBoundaries,
                timestampValidator
        );

        var periodsPerFrameRateReportAggregate =
                (int) getSetting.apply(PERIODS_PER_FRAME_RATE_REPORT_AGGREGATE_ID).getValue();
        var frameRateReporter = andRegister(
                new FrameRateReporterImpl(periodsPerFrameRateReportAggregate, aggregateOutputs));
        var frameTimer = andRegister(new FrameTimerImpl(globalClock, frameRateReporter));
        var frameTimerPollingInterval =
                (int) getSetting.apply(FRAME_TIMER_POLLING_INTERVAL_ID).getValue();
        var semaphorePermissions =
                (int) getSetting.apply(FRAME_EXECUTOR_SEMAPHORE_PERMISSIONS_ID).getValue();
        var frameExecutor = andRegister(
                new FrameExecutorImpl(componentRenderer, semaphorePermissions,
                        frameTimer::registerFrameExecution));

        var shaderFactory = new ShaderFactoryImpl();
        var shaderFilenamePrefix = (String) getSetting.apply(SHADER_FILENAME_PREFIX_ID).getValue();

        var meshVertices = (float[]) getSetting.apply(MESH_VERTICES_ID).getValue();
        var meshUvCoords = (float[]) getSetting.apply(MESH_UV_COORDS_ID).getValue();

        // ======
        // Assets
        // ======

        var images = new ConcurrentHashMap<String, Image>();
        var sprites = new ConcurrentHashMap<String, Sprite>();
        var animations = new ConcurrentHashMap<String, Animation>();
        var globalLoopingAnimations = new ConcurrentHashMap<String, GlobalLoopingAnimation>();
        var imageAssetSets = new ConcurrentHashMap<String, ImageAssetSet>();
        var fonts = new ConcurrentHashMap<String, Font>();
        var components = new ConcurrentHashMap<UUID, Component>();
        var mouseCursors = new ConcurrentHashMap<String, ProviderAtTime<Long>>();

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
                image -> images.put(image.relativeLocation(), image),
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

        TriConsumer<Integer, EventType, Runnable> subscribeToNextMouseEvent =
                mouseEventHandler::subscribeToNextEvent;
        andRegister(subscribeToNextMouseEvent, SUBSCRIBE_TO_NEXT_MOUSE_EVENT);

        // =========
        // Renderers
        // =========

        var shiftAggregator = new ColorShiftStackAggregatorImpl();

        contentRenderers.put(
                AntialiasedLineSegmentRenderableImpl.class,
                new AntialiasedLineSegmentRenderer(resManager, timestampValidator)
        );
        contentRenderers.put(
                FiniteAnimationRenderableImpl.class,
                new FiniteAnimationRenderer(renderingBoundaries,
                        resManager::windowWidthToHeightRatio, shiftAggregator, timestampValidator)
        );

        contentRenderers.put(
                GlobalLoopingAnimationRenderableImpl.class,
                new GlobalLoopingAnimationRenderer(renderingBoundaries,
                        resManager::windowWidthToHeightRatio, shiftAggregator, timestampValidator)
        );
        contentRenderers.put(
                ImageAssetSetRenderableImpl.class,
                new ImageAssetSetRenderer(renderingBoundaries, resManager::windowWidthToHeightRatio,
                        shiftAggregator, timestampValidator)
        );
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
                new SpriteRenderer(renderingBoundaries, resManager::windowWidthToHeightRatio,
                        shiftAggregator, timestampValidator)
        );
        var defaultFontColor = (Color) getSetting.apply(DEFAULT_FONT_COLOR_ID).getValue();
        contentRenderers.put(
                TextLineRenderableImpl.class,
                andRegister(new TextLineRendererImpl(renderingBoundaries, defaultFontColor,
                                resManager::windowWidthToHeightRatio, timestampValidator),
                        TEXT_LINE_RENDERER)
        );
        contentRenderers.put(
                TriangleRenderableImpl.class,
                new TriangleRenderer(timestampValidator)
        );

        // ===========
        // Renderables
        // ===========

        var antialiasedLineSegmentRenderableFactory =
                andRegister(new AntialiasedLineSegmentRenderableFactoryImpl());
        var componentFactory = andRegister(new ComponentFactoryImpl(
                c -> components.put(c.uuid(), c),
                c -> components.remove(c.uuid()),
                keyEventHandler::addComponent,
                keyEventHandler::removeComponent,
                mouseCapturing::putRenderable,
                mouseCapturing::removeRenderable,
                methods.CONSUMERS::get,
                methods.BICONSUMERS::get
        ));
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
        @SuppressWarnings("unchecked") var finiteLinearMovingProviderFactory = andRegister(
                new FiniteLinearMovingProviderFactoryImpl(mapOf(
                        pairOf(
                                FloatBox.class,
                                uuid -> valuesAtTimestamps -> pausedTimestamp -> timeVal ->
                                        new FiniteLinearMovingFloatBoxProvider(uuid,
                                                valuesAtTimestamps, pausedTimestamp, timeVal)
                        ),
                        pairOf(
                                Float.class,
                                uuid -> valuesAtTimestamps -> pausedTimestamp -> timeVal ->
                                        new FiniteLinearMovingFloatProvider(uuid,
                                                valuesAtTimestamps, pausedTimestamp, timeVal)
                        ),
                        pairOf(
                                Vertex.class,
                                uuid -> valuesAtTimestamps -> pausedTimestamp -> timeVal ->
                                        new FiniteLinearMovingVertexProvider(uuid,
                                                valuesAtTimestamps, pausedTimestamp, timeVal)
                        )
                ),
                        timestampValidator
                )
        );
        @SuppressWarnings("unchecked") var finiteSinusoidMovingProviderFactory = andRegister(
                new FiniteSinusoidMovingProviderFactoryImpl(mapOf(
                        pairOf(
                                FloatBox.class,
                                uuid -> valuesAtTimestamps -> transitionSharpnesses -> pausedTimestamp -> timeVal -> new FiniteSinusoidMovingFloatBoxProvider(
                                        uuid, valuesAtTimestamps, transitionSharpnesses,
                                        pausedTimestamp, timeVal)
                        ),
                        pairOf(
                                Float.class,
                                uuid -> valuesAtTimestamps -> transitionSharpnesses -> pausedTimestamp -> timeVal -> new FiniteSinusoidMovingFloatProvider(
                                        uuid, valuesAtTimestamps, transitionSharpnesses,
                                        pausedTimestamp, timeVal)
                        ),
                        pairOf(
                                Vertex.class,
                                uuid -> valuesAtTimestamps -> transitionSharpnesses -> pausedTimestamp -> timeVal -> new FiniteSinusoidMovingVertexProvider(
                                        uuid, valuesAtTimestamps, transitionSharpnesses,
                                        pausedTimestamp, timeVal)
                        )
                ),
                        timestampValidator
                )
        );
        var functionalProviderFactory = andRegister(
                new FunctionalProviderFactoryImpl(methods.FUNCTIONS::get, methods.CONSUMERS::get,
                        timestampValidator));
        var loopingLinearMovingColorProviderFactory =
                andRegister(new LoopingLinearMovingColorProviderFactoryImpl(timestampValidator));
        @SuppressWarnings({"unused", "unchecked"})
        var loopingLinearMovingProviderFactory =
                andRegister(new LoopingLinearMovingProviderFactoryImpl(mapOf(
                        pairOf(
                                FloatBox.class,
                                uuid -> periodDuration -> periodModuloOffset -> valuesWithinPeriod -> pausedTimestamp -> timeVal ->
                                        new LoopingLinearMovingFloatBoxProvider(uuid,
                                                valuesWithinPeriod,
                                                periodDuration, periodModuloOffset, pausedTimestamp,
                                                timeVal
                                        )
                        ),
                        pairOf(
                                Float.class,
                                uuid -> periodDuration -> periodModuloOffset -> valuesWithinPeriod -> pausedTimestamp -> timeVal ->
                                        new LoopingLinearMovingFloatProvider(
                                                uuid, valuesWithinPeriod, periodDuration,
                                                periodModuloOffset, pausedTimestamp, timeVal
                                        )
                        ),
                        pairOf(
                                Vertex.class,
                                uuid -> periodDuration -> periodModuloOffset -> valuesWithinPeriod -> pausedTimestamp -> timeVal ->
                                        new LoopingLinearMovingVertexProvider(
                                                uuid, valuesWithinPeriod, periodDuration,
                                                periodModuloOffset, pausedTimestamp, timeVal
                                        )
                        )
                ), timestampValidator));
        var progressiveStringProviderFactory =
                andRegister(new ProgressiveStringProviderFactoryImpl(timestampValidator));
        @SuppressWarnings({"rawtypes", "unchecked"}) BiFunction<UUID, Object, ProviderAtTime>
                staticProviderFactory =
                andRegister((uuid, val) -> new StaticProvider(uuid, val, timestampValidator),
                        STATIC_PROVIDER_FACTORY);
        andRegister(staticProviderFactory.apply(NULL_PROVIDER_UUID, null), NULL_PROVIDER);
        andRegister(staticProviderFactory.apply(WHOLE_SCREEN_PROVIDER_UUID, WHOLE_SCREEN),
                WHOLE_SCREEN_PROVIDER);

        // ========
        // Handlers
        // ========

        // Audio

        var soundHandler = new SoundHandler(soundFactory::make);
        var soundsPlayingHandler = new SoundsPlayingHandler(soundHandler, soundsPlaying);

        persistenceHandler.addTypeHandler(SoundImpl.class, soundHandler);
        persistenceHandler.addTypeHandler(SoundsPlayingImpl.class, soundsPlayingHandler);

        // Providers

        var providerHandler = new ProviderHandler();

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

        var finiteSinusoidProviderHandler = andRegister(
                new FiniteSinusoidMovingProviderHandler(persistenceHandler,
                        finiteSinusoidMovingProviderFactory));
        listOf(
                FiniteSinusoidMovingFloatBoxProvider.class,
                FiniteSinusoidMovingFloatProvider.class,
                FiniteSinusoidMovingVertexProvider.class
        ).forEach(c -> providerHandler.add(c.getCanonicalName(), finiteSinusoidProviderHandler));

        providerHandler.add(LoopingLinearMovingColorProviderImpl.class.getCanonicalName(),
                new LoopingLinearMovingColorProviderHandler(
                        loopingLinearMovingColorProviderFactory));

        providerHandler.add(FunctionalProviderImpl.class.getCanonicalName(),
                new FunctionalProviderHandler(mapHandler, functionalProviderFactory));

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

        providerHandler.add(StaticProvider.class.getCanonicalName(),
                new StaticProviderHandler(persistenceHandler, staticProviderFactory,
                        timestampValidator));

        // Shifts

        var shiftHandler = new ColorShiftHandler(providerHandler);

        // Renderables

        persistenceHandler.addTypeHandler(AntialiasedLineSegmentRenderableImpl.class,
                new AntialiasedLineSegmentRenderableHandler(providerHandler,
                        antialiasedLineSegmentRenderableFactory));
        persistenceHandler.addTypeHandler(FiniteAnimationRenderableImpl.class,
                new FiniteAnimationRenderableHandler(animations::get, methods.CONSUMERS::get,
                        providerHandler,
                        shiftHandler, finiteAnimationRenderableFactory));
        persistenceHandler.addTypeHandler(GlobalLoopingAnimationRenderableImpl.class,
                new GlobalLoopingAnimationRenderableHandler(globalLoopingAnimations::get,
                        methods.CONSUMERS::get, providerHandler, shiftHandler,
                        globalLoopingAnimationRenderableFactory));
        persistenceHandler.addTypeHandler(ImageAssetSetRenderableImpl.class,
                new ImageAssetSetRenderableHandler(imageAssetSets::get, methods.CONSUMERS::get,
                        providerHandler, shiftHandler, imageAssetSetRenderableFactory));
        persistenceHandler.addTypeHandler(RasterizedLineSegmentRenderableImpl.class,
                new RasterizedLineSegmentRenderableHandler(providerHandler,
                        rasterizedLineSegmentRenderableFactory));
        persistenceHandler.addTypeHandler(RectangleRenderableImpl.class,
                new RectangleRenderableHandler(methods.CONSUMERS::get, providerHandler,
                        rectangleRenderableFactory));
        persistenceHandler.addTypeHandler(SpriteRenderableImpl.class,
                new SpriteRenderableHandler(sprites::get, methods.CONSUMERS::get, providerHandler,
                        shiftHandler, spriteRenderableFactory));
        persistenceHandler.addTypeHandler(TextLineRenderableImpl.class,
                new TextLineRenderableHandler(fonts::get, providerHandler,
                        textLineRenderableFactory));
        persistenceHandler.addTypeHandler(TriangleRenderableImpl.class,
                new TriangleRenderableHandler(methods.CONSUMERS::get, providerHandler,
                        triangleRenderableFactory));
        persistenceHandler.addTypeHandler(ComponentImpl.class,
                new ComponentHandler(providerHandler, mapHandler, persistenceHandler,
                        methods.CONSUMERS::get,
                        keyEventHandler::getPriority, componentFactory));

        // ========
        // Graphics
        // ========

        andRegister(new GraphicsImpl(
                images::get,
                sprites::get,
                animations::get,
                globalLoopingAnimations::get,
                imageAssetSets::get,
                fonts::get,
                components::get
        ));

        // =========
        // Core Loop
        // =========

        @SuppressWarnings("unchecked") var audioRelDirs =
                (Set<String>) (getSetting.apply(AUDIO_RELATIVE_DIRS_ID).getValue());
        var renderersSet = setOf(contentRenderers.values().toArray(Renderer[]::new));
        andRegister(new CoreLoopImpl(
                initialTitlebar,
                frameTimer,
                frameTimerPollingInterval,
                resManager,
                globalClock,
                frameExecutor,
                shaderFactory,
                renderersSet,
                shaderFilenamePrefix,
                MeshImpl::new,
                renderersSet,
                meshVertices,
                meshUvCoords,
                graphicsPreloader,
                audioLoader,
                audioRelDirs,
                idsForFilenames,
                defaultLoopStopMsById,
                defaultLoopRestartMsById,
                keyEventListener,
                mouseCursor,
                mouseListener
        ));

        methods.concatenate(readMethods(new IOMethods(soundsPlaying, soundFactory)));
    }
}
