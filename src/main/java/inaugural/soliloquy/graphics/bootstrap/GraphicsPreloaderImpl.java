package inaugural.soliloquy.graphics.bootstrap;

import inaugural.soliloquy.graphics.api.dto.*;
import inaugural.soliloquy.graphics.bootstrap.tasks.*;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.assets.*;
import soliloquy.specs.graphics.bootstrap.GraphicsPreloader;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.ImageFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.MouseCursorImageFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.AnimationDefinition;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontDefinition;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageAssetSetDefinition;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.SpriteDefinition;
import soliloquy.specs.graphics.renderables.providers.AnimatedMouseCursorProvider;
import soliloquy.specs.graphics.renderables.providers.StaticMouseCursorProvider;
import soliloquy.specs.graphics.renderables.providers.factories.AnimatedMouseCursorProviderFactory;
import soliloquy.specs.graphics.renderables.providers.factories.GlobalLoopingAnimationFactory;
import soliloquy.specs.graphics.renderables.providers.factories.StaticMouseCursorProviderFactory;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static inaugural.soliloquy.tools.concurrency.Concurrency.*;

public class GraphicsPreloaderImpl implements GraphicsPreloader {
    private final AssetDefinitionsDTO ASSET_DEFINITIONS_DTO;

    private final ExecutorService EXECUTOR;
    private final Map<AssetType, Integer> ASSET_TYPE_BATCH_SIZES;

    private final ImageFactory IMAGE_FACTORY;
    private final AssetFactory<FontDefinition, Font> FONT_FACTORY;
    private final AssetFactory<SpriteDefinition, Sprite> SPRITE_FACTORY;
    private final AssetFactory<AnimationDefinition, Animation> ANIMATION_FACTORY;
    private final GlobalLoopingAnimationFactory GLOBAL_LOOPING_ANIMATION_FACTORY;
    private final AssetFactory<ImageAssetSetDefinition, ImageAssetSet> IMAGE_ASSET_SET_FACTORY;
    private final MouseCursorImageFactory MOUSE_CURSOR_IMAGE_FACTORY;
    private final AnimatedMouseCursorProviderFactory ANIMATED_MOUSE_CURSOR_PROVIDER_FACTORY;
    private final StaticMouseCursorProviderFactory STATIC_MOUSE_CURSOR_PROVIDER_FACTORY;

    private final Consumer<Sprite> PROCESS_SPRITE;
    private final Consumer<Animation> PROCESS_ANIMATION;
    private final Consumer<GlobalLoopingAnimation> PROCESS_GLOBAL_LOOPING_ANIMATION;
    private final Consumer<ImageAssetSet> PROCESS_IMAGE_ASSET_SET;
    private final Consumer<Font> PROCESS_FONTS;
    private final Consumer<AnimatedMouseCursorProvider> PROCESS_ANIMATED_MOUSE_CURSOR_PROVIDER;
    private final Consumer<StaticMouseCursorProvider> PROCESS_STATIC_MOUSE_CURSOR_PROVIDER;

    private final HashMap<String, Image> IMAGES;
    private final HashMap<String, Animation> ANIMATIONS;
    private final HashMap<String, Long> MOUSE_CURSOR_IMAGES;

    private final LinkedBlockingQueue<SpriteDefinitionDTO> SPRITE_DEFINITIONS_QUEUE;
    private final LinkedBlockingQueue<AnimationDefinitionDTO> ANIMATION_DEFINITIONS_QUEUE;
    private final LinkedBlockingQueue<GlobalLoopingAnimationDefinitionDTO>
            GLOBAL_LOOPING_ANIMATIONS_DEFINITIONS_QUEUE;
    private final LinkedBlockingQueue<ImageAssetSetDefinitionDTO>
            IMAGE_ASSET_SET_DEFINITIONS_QUEUE;
    private final LinkedBlockingQueue<AnimatedMouseCursorDefinitionDTO>
            ANIMATED_MOUSE_CURSOR_DEFINITIONS_QUEUE;
    private final LinkedBlockingQueue<StaticMouseCursorDefinitionDTO>
            STATIC_MOUSE_CURSOR_DEFINITIONS_QUEUE;

    private int spriteBatchesExecuted = 0;
    private int animationBatchesExecuted = 0;
    private int globalLoopingAnimationBatchesExecuted = 0;
    private int imageAssetSetBatchesExecuted = 0;
    private int animatedMouseCursorBatchesExecuted = 0;
    private int staticMouseCursorBatchesExecuted = 0;

    private Throwable innerThrowable;
    private final Supplier<Boolean> TASK_HAS_THROWN_EXCEPTION = () -> innerThrowable != null;

    public GraphicsPreloaderImpl(AssetDefinitionsDTO assetDefinitionsDTO,
                                 int threadPoolSize,
                                 Map<AssetType, Integer> assetTypeBatchSizes,
                                 ImageFactory imageFactory,
                                 AssetFactory<FontDefinition, Font> fontFactory,
                                 AssetFactory<SpriteDefinition, Sprite> spriteFactory,
                                 AssetFactory<AnimationDefinition, Animation> animationFactory,
                                 GlobalLoopingAnimationFactory globalLoopingAnimationFactory,
                                 AssetFactory<ImageAssetSetDefinition, ImageAssetSet>
                                         imageAssetSetFactory,
                                 MouseCursorImageFactory mouseCursorImageFactory,
                                 AnimatedMouseCursorProviderFactory
                                         animatedMouseCursorProviderFactory,
                                 StaticMouseCursorProviderFactory staticMouseCursorProviderFactory,
                                 Consumer<Sprite> processSprite,
                                 Consumer<Animation> processAnimation,
                                 Consumer<GlobalLoopingAnimation> processGlobalLoopingAnimation,
                                 Consumer<ImageAssetSet> processImageAssetSet,
                                 Consumer<Font> processFonts,
                                 Consumer<AnimatedMouseCursorProvider>
                                         processAnimatedMouseCursorProvider,
                                 Consumer<StaticMouseCursorProvider>
                                         processStaticMouseCursorProvider) {
        ASSET_DEFINITIONS_DTO = Check.ifNull(assetDefinitionsDTO, "assetDefinitionsDTO");

        EXECUTOR = Executors.newFixedThreadPool(threadPoolSize);

        ASSET_TYPE_BATCH_SIZES = Check.ifNull(assetTypeBatchSizes, "assetTypeBatchSizes");

        IMAGE_FACTORY = Check.ifNull(imageFactory, "imageFactory");
        FONT_FACTORY = Check.ifNull(fontFactory, "fontFactory");
        SPRITE_FACTORY = Check.ifNull(spriteFactory, "spriteFactory");
        ANIMATION_FACTORY = Check.ifNull(animationFactory, "animationFactory");
        GLOBAL_LOOPING_ANIMATION_FACTORY = Check.ifNull(globalLoopingAnimationFactory,
                "globalLoopingAnimationFactory");
        IMAGE_ASSET_SET_FACTORY = Check.ifNull(imageAssetSetFactory, "imageAssetSetFactory");
        MOUSE_CURSOR_IMAGE_FACTORY =
                Check.ifNull(mouseCursorImageFactory, "mouseCursorImageFactory");
        ANIMATED_MOUSE_CURSOR_PROVIDER_FACTORY = Check.ifNull(animatedMouseCursorProviderFactory,
                "animatedMouseCursorProviderFactory");
        STATIC_MOUSE_CURSOR_PROVIDER_FACTORY =
                Check.ifNull(staticMouseCursorProviderFactory, "staticMouseCursorProviderFactory");

        IMAGES = new HashMap<>();
        ANIMATIONS = new HashMap<>();
        MOUSE_CURSOR_IMAGES = new HashMap<>();

        Check.ifNull(processSprite, "processSprite");
        PROCESS_SPRITE = sprite -> blockedConsumer(sprite, processSprite);
        Check.ifNull(processAnimation, "processAnimation");
        PROCESS_ANIMATION = animation -> {
            ANIMATIONS.put(animation.id(), animation);
            blockedConsumer(animation, processAnimation);
        };
        Check.ifNull(processGlobalLoopingAnimation, "processGlobalLoopingAnimation");
        PROCESS_GLOBAL_LOOPING_ANIMATION = globalLoopingAnimation ->
                blockedConsumer(globalLoopingAnimation, processGlobalLoopingAnimation);
        Check.ifNull(processImageAssetSet, "processImageAssetSet");
        PROCESS_IMAGE_ASSET_SET = imageAssetSet -> blockedConsumer(imageAssetSet,
                processImageAssetSet);
        Check.ifNull(processFonts, "processFonts");
        PROCESS_FONTS = processFonts;
        Check.ifNull(processAnimatedMouseCursorProvider, "processAnimatedMouseCursorProvider");
        PROCESS_ANIMATED_MOUSE_CURSOR_PROVIDER =
                provider -> blockedConsumer(provider, processAnimatedMouseCursorProvider);
        Check.ifNull(processStaticMouseCursorProvider, "processStaticMouseCursorProvider");
        PROCESS_STATIC_MOUSE_CURSOR_PROVIDER =
                provider -> blockedConsumer(provider, processStaticMouseCursorProvider);

        SPRITE_DEFINITIONS_QUEUE = new LinkedBlockingQueue<>(
                Arrays.asList(ASSET_DEFINITIONS_DTO.spriteDefinitionDTOs)
        );
        ANIMATION_DEFINITIONS_QUEUE = new LinkedBlockingQueue<>(
                Arrays.asList(ASSET_DEFINITIONS_DTO.animationDefinitionDTOs)
        );
        GLOBAL_LOOPING_ANIMATIONS_DEFINITIONS_QUEUE = new LinkedBlockingQueue<>(
                Arrays.asList(ASSET_DEFINITIONS_DTO.globalLoopingAnimationDefinitionDTOs)
        );
        IMAGE_ASSET_SET_DEFINITIONS_QUEUE = new LinkedBlockingQueue<>(
                Arrays.asList(ASSET_DEFINITIONS_DTO.imageAssetSetDefinitionDTOs)
        );
        ANIMATED_MOUSE_CURSOR_DEFINITIONS_QUEUE = new LinkedBlockingQueue<>(
                Arrays.asList(ASSET_DEFINITIONS_DTO.animatedMouseCursorDefinitionDTOs)
        );
        STATIC_MOUSE_CURSOR_DEFINITIONS_QUEUE = new LinkedBlockingQueue<>(
                Arrays.asList(ASSET_DEFINITIONS_DTO.staticMouseCursorDefinitionDTOs)
        );
    }

    private synchronized <TToProcess> void blockedConsumer(TToProcess toProcess,
                                                           Consumer<TToProcess> process) {
        process.accept(toProcess);
    }

    @Override
    public void load() {
        // Images, Fonts, and MouseCursorImages require OpenGL to import into the GPU, and all
        // OpenGL calls must be made on the main thread, therefore this class must be called on the
        // main thread,

        // Load Images serially

        loadAllSerially(ASSET_DEFINITIONS_DTO.imageDefinitionDTOs, batch ->
                new ImagePreloaderTask(batch, IMAGE_FACTORY,
                        image -> IMAGES.put(image.relativeLocation(), image)));

        // Load MouseCursorImages serially, while loading other assets parallelly:
        //     * Sprites
        //     * Animations
        //         -> GlobalLoopingAnimations
        //             -> ImageAssetSets

        ArrayList<CompletableFuture<Void>> parallelizableTasks = new ArrayList<>();

        parallelizableTasks.add(
                runTaskAsync(this::loadImageAssets, this::handleThrowable, EXECUTOR));

        loadAllSerially(ASSET_DEFINITIONS_DTO.mouseCursorImageDefinitionDTOs, batch ->
                new MouseCursorImagePreloaderTask(batch, MOUSE_CURSOR_IMAGE_FACTORY, output ->
                        MOUSE_CURSOR_IMAGES.put(output.relativeLocation(), output.id())));

        // Load Fonts serially, while loading
        //     * MouseCursorImages
        //         -> AnimatedMouseCursors
        //         -> StaticMouseCursors
        // As fonts are done loading, continue loading other assets as necessary

        parallelizableTasks.add(loadBatchesParallellyTask(
                ANIMATED_MOUSE_CURSOR_DEFINITIONS_QUEUE,
                ASSET_TYPE_BATCH_SIZES.get(AssetType.ANIMATED_MOUSE_CURSOR_PROVIDER),
                animatedMouseCursorDefinitionDTOs -> new AnimatedMouseCursorPreloaderTask(
                        MOUSE_CURSOR_IMAGES::get,
                        animatedMouseCursorDefinitionDTOs,
                        ANIMATED_MOUSE_CURSOR_PROVIDER_FACTORY,
                        PROCESS_ANIMATED_MOUSE_CURSOR_PROVIDER
                ),
                animatedMouseCursorBatchesExecuted,
                () -> animatedMouseCursorBatchesExecuted++
        ));

        parallelizableTasks.add(loadBatchesParallellyTask(
                STATIC_MOUSE_CURSOR_DEFINITIONS_QUEUE,
                ASSET_TYPE_BATCH_SIZES.get(AssetType.STATIC_MOUSE_CURSOR_PROVIDER),
                staticMouseCursorDefinitionDTOS -> new StaticMouseCursorPreloaderTask(
                        MOUSE_CURSOR_IMAGES::get,
                        staticMouseCursorDefinitionDTOS,
                        STATIC_MOUSE_CURSOR_PROVIDER_FACTORY,
                        PROCESS_STATIC_MOUSE_CURSOR_PROVIDER
                ),
                staticMouseCursorBatchesExecuted,
                () -> staticMouseCursorBatchesExecuted++
        ));

        loadAllSerially(ASSET_DEFINITIONS_DTO.fontDefinitionDTOs, batch ->
                new FontPreloaderTask(batch, FONT_FACTORY, PROCESS_FONTS));

        waitUntilTasksCompleted(parallelizableTasks, TASK_HAS_THROWN_EXCEPTION);

        EXECUTOR.shutdown();

        if (innerThrowable != null) {
            if (innerThrowable instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) innerThrowable;
            }
            if (innerThrowable instanceof IllegalStateException) {
                throw (IllegalStateException) innerThrowable;
            }
            if (innerThrowable instanceof UnsupportedOperationException) {
                throw (UnsupportedOperationException) innerThrowable;
            }
            throw new RuntimeException(innerThrowable.getMessage());
        }
    }

    private void loadImageAssets() {
        ArrayList<CompletableFuture<Void>> loadImageAssetsTasks = new ArrayList<>();

        loadImageAssetsTasks.add(loadBatchesParallellyTask(
                SPRITE_DEFINITIONS_QUEUE,
                ASSET_TYPE_BATCH_SIZES.get(AssetType.SPRITE),
                spriteDefinitionDTOs -> new SpritePreloaderTask(IMAGES::get, spriteDefinitionDTOs,
                        SPRITE_FACTORY, PROCESS_SPRITE),
                spriteBatchesExecuted,
                () -> spriteBatchesExecuted++
        ));

        loadImageAssetsTasks.add(loadBatchesParallellyTask(
                ANIMATION_DEFINITIONS_QUEUE,
                ASSET_TYPE_BATCH_SIZES.get(AssetType.ANIMATION),
                animationDefinitionDTOs -> new AnimationPreloaderTask(IMAGES::get,
                        animationDefinitionDTOs, ANIMATION_FACTORY, PROCESS_ANIMATION),
                animationBatchesExecuted,
                () -> animationBatchesExecuted++
        ));

        waitUntilTasksCompleted(loadImageAssetsTasks, TASK_HAS_THROWN_EXCEPTION);

        CompletableFuture<Void> globalLoopingAnimationsTask = loadBatchesParallellyTask(
                GLOBAL_LOOPING_ANIMATIONS_DEFINITIONS_QUEUE,
                ASSET_TYPE_BATCH_SIZES.get(AssetType.GLOBAL_LOOPING_ANIMATION),
                globalLoopingAnimationDefinitionDTOs -> new GlobalLoopingAnimationPreloaderTask(
                        ANIMATIONS::get, globalLoopingAnimationDefinitionDTOs,
                        GLOBAL_LOOPING_ANIMATION_FACTORY, PROCESS_GLOBAL_LOOPING_ANIMATION
                ),
                globalLoopingAnimationBatchesExecuted,
                () -> globalLoopingAnimationBatchesExecuted++
        );

        waitUntilTaskCompleted(globalLoopingAnimationsTask, TASK_HAS_THROWN_EXCEPTION);

        CompletableFuture<Void> imageAssetSetsTask = loadBatchesParallellyTask(
                IMAGE_ASSET_SET_DEFINITIONS_QUEUE,
                ASSET_TYPE_BATCH_SIZES.get(AssetType.IMAGE_ASSET_SET),
                imageAssetSetDefinitionDTOs -> new ImageAssetSetPreloaderTask(
                        imageAssetSetDefinitionDTOs, IMAGE_ASSET_SET_FACTORY,
                        PROCESS_IMAGE_ASSET_SET
                ),
                imageAssetSetBatchesExecuted,
                () -> imageAssetSetBatchesExecuted++
        );

        waitUntilTaskCompleted(imageAssetSetsTask, TASK_HAS_THROWN_EXCEPTION);
    }

    private <TDefinitionDTO> void loadAllSerially(TDefinitionDTO[] definitionDTOs,
                                                  Function<List<TDefinitionDTO>, Runnable>
                                                          taskFactory) {
        int assetsLoaded = 0;
        int totalAssets = definitionDTOs.length;
        int batchSize = ASSET_TYPE_BATCH_SIZES.get(AssetType.IMAGE);

        while (assetsLoaded < totalAssets) {
            int numberToTake = Math.min(batchSize + assetsLoaded, totalAssets);

            ArrayList<TDefinitionDTO> batch = new ArrayList<>(
                    Arrays.asList(definitionDTOs)
                            .subList(assetsLoaded, numberToTake));

            taskFactory.apply(batch).run();

            assetsLoaded += batchSize;
        }
    }

    private <TDefinitionDTO> CompletableFuture<Void> loadBatchesParallellyTask(
            LinkedBlockingQueue<TDefinitionDTO> definitionDTOs,
            int batchSize,
            Function<List<TDefinitionDTO>, Runnable> taskFactory,
            Object lockObject,
            Runnable increaseCompletedBatchesCount) {
        // NB: This magic works, since int division always rounds down
        int batchTasksToExecute = ((definitionDTOs.size() - 1) / batchSize) + 1;

        ArrayList<CompletableFuture<Void>> tasks = new ArrayList<>();

        return runTaskAsync(() -> {
            for (int i = 0; i < batchTasksToExecute; i++) {
                tasks.add(runTaskAsync(
                        () -> loadBatch(definitionDTOs, batchSize, taskFactory, lockObject,
                                increaseCompletedBatchesCount),
                        this::handleThrowable,
                        EXECUTOR
                ));
            }

            waitUntilTasksCompleted(tasks, batchTasksToExecute, TASK_HAS_THROWN_EXCEPTION);
        }, this::handleThrowable, EXECUTOR);
    }

    private <TDefinitionDTO> void loadBatch(LinkedBlockingQueue<TDefinitionDTO> definitionDTOs,
                                            int batchSize,
                                            Function<List<TDefinitionDTO>, Runnable> taskFactory,
                                            Object lockObject,
                                            Runnable increaseCompletedBatchesCount) {
        ArrayList<TDefinitionDTO> batch = new ArrayList<>();
        definitionDTOs.drainTo(batch, batchSize);
        taskFactory.apply(batch).run();
        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (lockObject) {
            increaseCompletedBatchesCount.run();
        }
    }

    private synchronized void handleThrowable(Throwable e) {
        if (innerThrowable == null) {
            innerThrowable = e.getCause();
        }
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
