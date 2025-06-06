package inaugural.soliloquy.graphics.test.unit.bootstrap;

import inaugural.soliloquy.graphics.api.dto.*;
import inaugural.soliloquy.graphics.bootstrap.GraphicsPreloaderImpl;
import inaugural.soliloquy.graphics.bootstrap.assetfactories.ImageAssetSetFactory;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import inaugural.soliloquy.tools.collections.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import soliloquy.specs.common.shared.HasId;
import soliloquy.specs.graphics.assets.*;
import soliloquy.specs.graphics.bootstrap.GraphicsPreloader;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.ImageFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.MouseCursorImageFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.*;
import soliloquy.specs.graphics.renderables.providers.AnimatedMouseCursorProvider;
import soliloquy.specs.graphics.renderables.providers.StaticMouseCursorProvider;
import soliloquy.specs.graphics.renderables.providers.factories.AnimatedMouseCursorProviderFactory;
import soliloquy.specs.graphics.renderables.providers.factories.GlobalLoopingAnimationFactory;
import soliloquy.specs.graphics.renderables.providers.factories.StaticMouseCursorProviderFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static inaugural.soliloquy.graphics.api.dto.AssetType.*;
import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.random.Random.*;
import static java.awt.GridBagConstraints.SOUTHWEST;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

@ExtendWith(MockitoExtension.class)
public class GraphicsPreloaderImplTests {
    private final int THREAD_POOL_SIZE = 12;

    private Map<AssetType, Integer> assetTypeBatchSizes;

    @Mock private ImageFactory mockImageFactory;
    @Mock private AssetFactory<FontDefinition, Font> mockFontFactory;
    @Mock private AssetFactory<SpriteDefinition, Sprite> mockSpriteFactory;
    @Mock private AssetFactory<AnimationDefinition, Animation> mockAnimationFactory;
    @Mock private GlobalLoopingAnimationFactory mockGlobalLoopingAnimationFactory;
    @Mock private AssetFactory<ImageAssetSetDefinition, ImageAssetSet> mockImageAssetSetFactory;
    @Mock private MouseCursorImageFactory mockMouseCursorImageFactory;
    @Mock private AnimatedMouseCursorProviderFactory mockAnimatedMouseCursorProviderFactory;
    @Mock private StaticMouseCursorProviderFactory mockStaticMouseCursorProviderFactory;

    private AssetDefinitionsDTO assetDefinitionsDTO;

    private ImageDefinitionDTO[] imageDefinitionDTOs;
    private SpriteDefinitionDTO[] spriteDefinitionDTOs;
    private AnimationDefinitionDTO[] animationDefinitionDTOs;
    private GlobalLoopingAnimationDefinitionDTO[] globalLoopingAnimationDefinitionDTOs;
    private ImageAssetSetDefinitionDTO[] imageAssetSetDefinitionDTOs;
    private MouseCursorImageDefinitionDTO[] mouseCursorImageDefinitionDTOs;
    private AnimatedMouseCursorDefinitionDTO[] animatedMouseCursorDefinitionDTOs;
    private StaticMouseCursorDefinitionDTO[] staticMouseCursorDefinitionDTOs;
    private FontDefinitionDTO[] fontDefinitionDTOs;

    private CopyOnWriteArrayList<Image> imageFactoryOutputs;
    private CopyOnWriteArrayList<Font> fontFactoryOutputs;
    private CopyOnWriteArrayList<MouseCursorImageFactory.Output> mouseCursorImageFactoryOutputs;

    private List<Sprite> processedSprites;
    private List<Animation> processedAnimations;
    private List<GlobalLoopingAnimation> processedGlobalLoopingAnimations;
    private List<ImageAssetSet> processedImageAssetSets;
    private List<AnimatedMouseCursorProvider> processedAnimatedMouseCursorProviders;
    private List<StaticMouseCursorProvider> processedStaticMouseCursors;
    private List<Font> processedFonts;

    private CopyOnWriteArrayList<Object> allDefinitionsProcessedInOrder;

    private int assetsProcessedThusFar = 0;

    private int firstImageIndex = -1;
    private int lastImageIndex = -1;
    private int firstFontIndex = -1;
    private int firstSpriteIndex = -1;
    private int lastSpriteIndex = -1;
    private int firstAnimationIndex = -1;
    private int lastAnimationIndex = -1;
    private int firstGlobalLoopingAnimationIndex = -1;
    private int lastGlobalLoopingAnimationIndex = -1;
    private int firstImageAssetSetIndex = -1;
    private int firstMouseCursorImageIndex = -1;
    private int lastMouseCursorImageIndex = -1;
    private int firstAnimatedMouseCursorIndex = -1;
    private int firstStaticMouseCursorIndex = -1;

    private static final int BATCH_SIZE = 10;
    // NB: I am making sprites and animations equal to mouse cursor images to test whether the
    //     images are correctly stored and then passed onto subsequent preloading tasks
    private static final int NUMBER_OF_IMAGES = randomNumberOfAssetsInRange();
    private static final int NUMBER_OF_SPRITES = NUMBER_OF_IMAGES;
    private static final int NUMBER_OF_ANIMATIONS = NUMBER_OF_IMAGES;
    private static final int NUMBER_OF_GLOBAL_LOOPING_ANIMATIONS = NUMBER_OF_IMAGES;
    private static final int NUMBER_OF_IMAGE_ASSET_SETS = randomNumberOfAssetsInRange();
    private static final int NUMBER_OF_FONTS = randomNumberOfAssetsInRange();
    private static final int NUMBER_OF_MOUSE_CURSOR_IMAGES = randomNumberOfAssetsInRange();
    private static final int NUMBER_OF_ANIMATED_MOUSE_CURSORS = randomNumberOfAssetsInRange();
    // NB: I am making static mouse cursors equal to mouse cursor images to test whether the
    //     mouse cursor images are correctly stored and then passed onto subsequent preloading
    //     tasks
    private static final int NUMBER_OF_STATIC_MOUSE_CURSORS = NUMBER_OF_MOUSE_CURSOR_IMAGES;

    private GraphicsPreloader graphicsPreloader;

    private static int randomNumberOfAssetsInRange() {
        return randomIntInRange(10, 50);
    }

    @BeforeEach
    public void setUp() {
        assetTypeBatchSizes = Collections.mapOf(
            pairOf(IMAGE, BATCH_SIZE),
            pairOf(SPRITE, BATCH_SIZE),
            pairOf(ANIMATION, BATCH_SIZE),
            pairOf(GLOBAL_LOOPING_ANIMATION, BATCH_SIZE),
            pairOf(IMAGE_ASSET_SET, BATCH_SIZE),
            pairOf(FONT, BATCH_SIZE),
            pairOf(MOUSE_CURSOR_IMAGE, BATCH_SIZE),
            pairOf(ANIMATED_MOUSE_CURSOR_PROVIDER, BATCH_SIZE),
            pairOf(STATIC_MOUSE_CURSOR_PROVIDER, BATCH_SIZE)
        );

        imageDefinitionDTOs = new ImageDefinitionDTO[NUMBER_OF_IMAGES];
        for (var i = 0; i < NUMBER_OF_IMAGES; i++) {
            imageDefinitionDTOs[i] = randomImageDefinitionDTO();
        }

        spriteDefinitionDTOs = new SpriteDefinitionDTO[NUMBER_OF_SPRITES];
        for (var i = 0; i < NUMBER_OF_SPRITES; i++) {
            spriteDefinitionDTOs[i] = randomSpriteDefinitionDTO(i);
        }

        animationDefinitionDTOs = new AnimationDefinitionDTO[NUMBER_OF_ANIMATIONS];
        for (var i = 0; i < NUMBER_OF_ANIMATIONS; i++) {
            animationDefinitionDTOs[i] = randomAnimationDefinitionDTO(i);
        }

        globalLoopingAnimationDefinitionDTOs =
                new GlobalLoopingAnimationDefinitionDTO[NUMBER_OF_GLOBAL_LOOPING_ANIMATIONS];
        for (var i = 0; i < NUMBER_OF_GLOBAL_LOOPING_ANIMATIONS; i++) {
            globalLoopingAnimationDefinitionDTOs[i] = randomGlobalLoopingAnimationDefinitionDTO();
        }

        imageAssetSetDefinitionDTOs = new ImageAssetSetDefinitionDTO[NUMBER_OF_IMAGE_ASSET_SETS];
        for (var i = 0; i < NUMBER_OF_IMAGE_ASSET_SETS; i++) {
            imageAssetSetDefinitionDTOs[i] = randomImageAssetSetDefinitionDTO();
        }

        fontDefinitionDTOs = new FontDefinitionDTO[NUMBER_OF_FONTS];
        for (var i = 0; i < NUMBER_OF_FONTS; i++) {
            fontDefinitionDTOs[i] = randomFontDefinitionDTO();
        }

        mouseCursorImageDefinitionDTOs =
                new MouseCursorImageDefinitionDTO[NUMBER_OF_MOUSE_CURSOR_IMAGES];
        for (var i = 0; i < NUMBER_OF_MOUSE_CURSOR_IMAGES; i++) {
            mouseCursorImageDefinitionDTOs[i] = randomMouseCursorImageDefinitionDTO();
        }

        animatedMouseCursorDefinitionDTOs =
                new AnimatedMouseCursorDefinitionDTO[NUMBER_OF_ANIMATED_MOUSE_CURSORS];
        for (var i = 0; i < NUMBER_OF_ANIMATED_MOUSE_CURSORS; i++) {
            animatedMouseCursorDefinitionDTOs[i] = randomAnimatedMouseCursorDefinitionDTO();
        }

        staticMouseCursorDefinitionDTOs =
                new StaticMouseCursorDefinitionDTO[NUMBER_OF_STATIC_MOUSE_CURSORS];
        for (var i = 0; i < NUMBER_OF_STATIC_MOUSE_CURSORS; i++) {
            staticMouseCursorDefinitionDTOs[i] = randomStaticMouseCursorDefinitionDTO(i);
        }

        assetDefinitionsDTO = new AssetDefinitionsDTO(
                imageDefinitionDTOs,
                fontDefinitionDTOs,
                spriteDefinitionDTOs,
                animationDefinitionDTOs,
                globalLoopingAnimationDefinitionDTOs,
                imageAssetSetDefinitionDTOs,
                mouseCursorImageDefinitionDTOs,
                animatedMouseCursorDefinitionDTOs,
                staticMouseCursorDefinitionDTOs
        );

        allDefinitionsProcessedInOrder = new CopyOnWriteArrayList<>();

        imageFactoryOutputs = new CopyOnWriteArrayList<>();
        lenient().when(mockImageFactory.make(any()))
                .thenAnswer((Answer<Image>) invocation -> {
                    updateAssetIndices(
                            () -> firstImageIndex,
                            i -> firstImageIndex = i,
                            i -> lastImageIndex = i
                    );
                    ImageDefinition definition = invocation.getArgument(0);
                    allDefinitionsProcessedInOrder.add(definition);
                    FakeImage output = new FakeImage(definition.relativeLocation());
                    imageFactoryOutputs.add(output);
                    return output;
                });

        fontFactoryOutputs = new CopyOnWriteArrayList<>();
        lenient().when(mockFontFactory.make(any()))
                .thenAnswer((Answer<Font>) invocation -> {
                    updateAssetIndices(
                            () -> firstFontIndex,
                            i -> firstFontIndex = i,
                            i -> {}
                    );
                    FontDefinition definition = invocation.getArgument(0);
                    allDefinitionsProcessedInOrder.add(definition);
                    FakeFont output = new FakeFont(definition.id());
                    fontFactoryOutputs.add(output);
                    return output;
                });

        lenient().when(mockSpriteFactory.make(any()))
                .thenAnswer((Answer<Sprite>) invocation -> {
                    updateAssetIndices(
                            () -> firstSpriteIndex,
                            i -> firstSpriteIndex = i,
                            i -> lastSpriteIndex = i
                    );
                    SpriteDefinition definition = invocation.getArgument(0);
                    allDefinitionsProcessedInOrder.add(definition);
                    return new FakeSprite(definition.id(), definition.image());
                });

        lenient().when(mockAnimationFactory.make(any()))
                .thenAnswer((Answer<Animation>) invocation -> {
                    updateAssetIndices(
                            () -> firstAnimationIndex,
                            i -> firstAnimationIndex = i,
                            i -> lastAnimationIndex = i
                    );
                    AnimationDefinition definition = invocation.getArgument(0);
                    allDefinitionsProcessedInOrder.add(definition);
                    return new FakeAnimation(definition.id(), definition.frameSnippetDefinitions());
                });

        lenient().when(mockGlobalLoopingAnimationFactory.make(any()))
                .thenAnswer(invocation -> {
                    updateAssetIndices(
                            () -> firstGlobalLoopingAnimationIndex,
                            i -> firstGlobalLoopingAnimationIndex = i,
                            i -> lastGlobalLoopingAnimationIndex = i
                    );
                    GlobalLoopingAnimationDefinition definition = invocation.getArgument(0);
                    allDefinitionsProcessedInOrder.add(definition);
                    return new FakeGlobalLoopingAnimation(definition.id());
                });

        lenient().when(mockImageAssetSetFactory.make(any()))
                .thenAnswer((Answer<ImageAssetSet>) invocation -> {
                    updateAssetIndices(
                            () -> firstImageAssetSetIndex,
                            i -> firstImageAssetSetIndex = i,
                            i -> {}
                    );
                    ImageAssetSetDefinition definition = invocation.getArgument(0);
                    allDefinitionsProcessedInOrder.add(definition);
                    return new FakeImageAssetSet(definition.id());
                });

        mouseCursorImageFactoryOutputs = new CopyOnWriteArrayList<>();
        lenient().when(mockMouseCursorImageFactory.make(any()))
                .thenAnswer(invocation -> {
                    updateAssetIndices(
                            () -> firstMouseCursorImageIndex,
                            i -> firstMouseCursorImageIndex = i,
                            i -> lastMouseCursorImageIndex = i
                    );
                    MouseCursorImageDefinition definition = invocation.getArgument(0);
                    allDefinitionsProcessedInOrder.add(definition);
                    MouseCursorImageFactory.Output returnValue =
                            new MouseCursorImageFactory.Output(
                                    definition.relativeLocation(),
                                    randomLong()
                            );
                    mouseCursorImageFactoryOutputs.add(returnValue);
                    return returnValue;
                });

        lenient().when(mockAnimatedMouseCursorProviderFactory.make(any()))
                .thenAnswer((Answer<AnimatedMouseCursorProvider>) invocation -> {
                    updateAssetIndices(
                            () -> firstAnimatedMouseCursorIndex,
                            i -> firstAnimatedMouseCursorIndex = i,
                            i -> {}
                    );
                    AnimatedMouseCursorProviderDefinition definition = invocation.getArgument(0);
                    allDefinitionsProcessedInOrder.add(definition);
                    AnimatedMouseCursorProvider animatedMouseCursorProvider =
                            mock(AnimatedMouseCursorProvider.class);
                    lenient().when(animatedMouseCursorProvider.id()).thenReturn(definition.id());
                    return animatedMouseCursorProvider;
                });

        lenient().when(mockStaticMouseCursorProviderFactory.make(any()))
                .thenAnswer((Answer<StaticMouseCursorProvider>) invocation -> {
                    updateAssetIndices(
                            () -> firstStaticMouseCursorIndex,
                            i -> firstStaticMouseCursorIndex = i,
                            i -> {}
                    );
                    StaticMouseCursorProviderDefinition definition = invocation.getArgument(0);
                    allDefinitionsProcessedInOrder.add(definition);
                    return new FakeStaticMouseCursorProvider(definition.id(),
                            definition.mouseCursorImageId());
                });

        processedSprites = listOf();
        processedAnimations = listOf();
        processedGlobalLoopingAnimations = listOf();
        processedImageAssetSets = listOf();
        processedFonts = listOf();
        processedAnimatedMouseCursorProviders = listOf();
        processedStaticMouseCursors = listOf();

        graphicsPreloader = new GraphicsPreloaderImpl(
                assetDefinitionsDTO,
                THREAD_POOL_SIZE,
                assetTypeBatchSizes,
                mockImageFactory,
                mockFontFactory,
                mockSpriteFactory,
                mockAnimationFactory,
                mockGlobalLoopingAnimationFactory,
                mockImageAssetSetFactory,
                mockMouseCursorImageFactory,
                mockAnimatedMouseCursorProviderFactory,
                mockStaticMouseCursorProviderFactory,
                processedSprites::add,
                processedAnimations::add,
                processedGlobalLoopingAnimations::add,
                processedImageAssetSets::add,
                processedFonts::add,
                processedAnimatedMouseCursorProviders::add,
                processedStaticMouseCursors::add
        );
    }

    private synchronized void updateAssetIndices(Supplier<Integer> getFirstIndex,
                                                 Consumer<Integer> updateFirstIndex,
                                                 Consumer<Integer> updateLastIndex) {
        if (getFirstIndex.get() < 0) {
            updateFirstIndex.accept(assetsProcessedThusFar);
        }
        updateLastIndex.accept(assetsProcessedThusFar);
        assetsProcessedThusFar++;
    }

    @Test
    public void constructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new GraphicsPreloaderImpl(
                null,
                THREAD_POOL_SIZE,
                assetTypeBatchSizes,
                mockImageFactory,
                mockFontFactory,
                mockSpriteFactory,
                mockAnimationFactory,
                mockGlobalLoopingAnimationFactory,
                mockImageAssetSetFactory,
                mockMouseCursorImageFactory,
                mockAnimatedMouseCursorProviderFactory,
                mockStaticMouseCursorProviderFactory,
                processedSprites::add,
                processedAnimations::add,
                processedGlobalLoopingAnimations::add,
                processedImageAssetSets::add,
                processedFonts::add,
                processedAnimatedMouseCursorProviders::add,
                processedStaticMouseCursors::add
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsPreloaderImpl(
                assetDefinitionsDTO,
                THREAD_POOL_SIZE,
                null,
                mockImageFactory,
                mockFontFactory,
                mockSpriteFactory,
                mockAnimationFactory,
                mockGlobalLoopingAnimationFactory,
                mockImageAssetSetFactory,
                mockMouseCursorImageFactory,
                mockAnimatedMouseCursorProviderFactory,
                mockStaticMouseCursorProviderFactory,
                processedSprites::add,
                processedAnimations::add,
                processedGlobalLoopingAnimations::add,
                processedImageAssetSets::add,
                processedFonts::add,
                processedAnimatedMouseCursorProviders::add,
                processedStaticMouseCursors::add
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsPreloaderImpl(
                assetDefinitionsDTO,
                THREAD_POOL_SIZE,
                assetTypeBatchSizes,
                null,
                mockFontFactory,
                mockSpriteFactory,
                mockAnimationFactory,
                mockGlobalLoopingAnimationFactory,
                mockImageAssetSetFactory,
                mockMouseCursorImageFactory,
                mockAnimatedMouseCursorProviderFactory,
                mockStaticMouseCursorProviderFactory,
                processedSprites::add,
                processedAnimations::add,
                processedGlobalLoopingAnimations::add,
                processedImageAssetSets::add,
                processedFonts::add,
                processedAnimatedMouseCursorProviders::add,
                processedStaticMouseCursors::add
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsPreloaderImpl(
                assetDefinitionsDTO,
                THREAD_POOL_SIZE,
                assetTypeBatchSizes,
                mockImageFactory,
                null,
                mockSpriteFactory,
                mockAnimationFactory,
                mockGlobalLoopingAnimationFactory,
                mockImageAssetSetFactory,
                mockMouseCursorImageFactory,
                mockAnimatedMouseCursorProviderFactory,
                mockStaticMouseCursorProviderFactory,
                processedSprites::add,
                processedAnimations::add,
                processedGlobalLoopingAnimations::add,
                processedImageAssetSets::add,
                processedFonts::add,
                processedAnimatedMouseCursorProviders::add,
                processedStaticMouseCursors::add
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsPreloaderImpl(
                assetDefinitionsDTO,
                THREAD_POOL_SIZE,
                assetTypeBatchSizes,
                mockImageFactory,
                mockFontFactory,
                null,
                mockAnimationFactory,
                mockGlobalLoopingAnimationFactory,
                mockImageAssetSetFactory,
                mockMouseCursorImageFactory,
                mockAnimatedMouseCursorProviderFactory,
                mockStaticMouseCursorProviderFactory,
                processedSprites::add,
                processedAnimations::add,
                processedGlobalLoopingAnimations::add,
                processedImageAssetSets::add,
                processedFonts::add,
                processedAnimatedMouseCursorProviders::add,
                processedStaticMouseCursors::add
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsPreloaderImpl(
                assetDefinitionsDTO,
                THREAD_POOL_SIZE,
                assetTypeBatchSizes,
                mockImageFactory,
                mockFontFactory,
                mockSpriteFactory,
                null,
                mockGlobalLoopingAnimationFactory,
                mockImageAssetSetFactory,
                mockMouseCursorImageFactory,
                mockAnimatedMouseCursorProviderFactory,
                mockStaticMouseCursorProviderFactory,
                processedSprites::add,
                processedAnimations::add,
                processedGlobalLoopingAnimations::add,
                processedImageAssetSets::add,
                processedFonts::add,
                processedAnimatedMouseCursorProviders::add,
                processedStaticMouseCursors::add
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsPreloaderImpl(
                assetDefinitionsDTO,
                THREAD_POOL_SIZE,
                assetTypeBatchSizes,
                mockImageFactory,
                mockFontFactory,
                mockSpriteFactory,
                mockAnimationFactory,
                null,
                mockImageAssetSetFactory,
                mockMouseCursorImageFactory,
                mockAnimatedMouseCursorProviderFactory,
                mockStaticMouseCursorProviderFactory,
                processedSprites::add,
                processedAnimations::add,
                processedGlobalLoopingAnimations::add,
                processedImageAssetSets::add,
                processedFonts::add,
                processedAnimatedMouseCursorProviders::add,
                processedStaticMouseCursors::add
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsPreloaderImpl(
                assetDefinitionsDTO,
                THREAD_POOL_SIZE,
                assetTypeBatchSizes,
                mockImageFactory,
                mockFontFactory,
                mockSpriteFactory,
                mockAnimationFactory,
                mockGlobalLoopingAnimationFactory,
                null,
                mockMouseCursorImageFactory,
                mockAnimatedMouseCursorProviderFactory,
                mockStaticMouseCursorProviderFactory,
                processedSprites::add,
                processedAnimations::add,
                processedGlobalLoopingAnimations::add,
                processedImageAssetSets::add,
                processedFonts::add,
                processedAnimatedMouseCursorProviders::add,
                processedStaticMouseCursors::add
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsPreloaderImpl(
                assetDefinitionsDTO,
                THREAD_POOL_SIZE,
                assetTypeBatchSizes,
                mockImageFactory,
                mockFontFactory,
                mockSpriteFactory,
                mockAnimationFactory,
                mockGlobalLoopingAnimationFactory,
                mockImageAssetSetFactory,
                null,
                mockAnimatedMouseCursorProviderFactory,
                mockStaticMouseCursorProviderFactory,
                processedSprites::add,
                processedAnimations::add,
                processedGlobalLoopingAnimations::add,
                processedImageAssetSets::add,
                processedFonts::add,
                processedAnimatedMouseCursorProviders::add,
                processedStaticMouseCursors::add
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsPreloaderImpl(
                assetDefinitionsDTO,
                THREAD_POOL_SIZE,
                assetTypeBatchSizes,
                mockImageFactory,
                mockFontFactory,
                mockSpriteFactory,
                mockAnimationFactory,
                mockGlobalLoopingAnimationFactory,
                mockImageAssetSetFactory,
                mockMouseCursorImageFactory,
                null,
                mockStaticMouseCursorProviderFactory,
                processedSprites::add,
                processedAnimations::add,
                processedGlobalLoopingAnimations::add,
                processedImageAssetSets::add,
                processedFonts::add,
                processedAnimatedMouseCursorProviders::add,
                processedStaticMouseCursors::add
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsPreloaderImpl(
                assetDefinitionsDTO,
                THREAD_POOL_SIZE,
                assetTypeBatchSizes,
                mockImageFactory,
                mockFontFactory,
                mockSpriteFactory,
                mockAnimationFactory,
                mockGlobalLoopingAnimationFactory,
                mockImageAssetSetFactory,
                mockMouseCursorImageFactory,
                mockAnimatedMouseCursorProviderFactory,
                null,
                processedSprites::add,
                processedAnimations::add,
                processedGlobalLoopingAnimations::add,
                processedImageAssetSets::add,
                processedFonts::add,
                processedAnimatedMouseCursorProviders::add,
                processedStaticMouseCursors::add
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsPreloaderImpl(
                assetDefinitionsDTO,
                THREAD_POOL_SIZE,
                assetTypeBatchSizes,
                mockImageFactory,
                mockFontFactory,
                mockSpriteFactory,
                mockAnimationFactory,
                mockGlobalLoopingAnimationFactory,
                mockImageAssetSetFactory,
                mockMouseCursorImageFactory,
                mockAnimatedMouseCursorProviderFactory,
                mockStaticMouseCursorProviderFactory,
                null,
                processedAnimations::add,
                processedGlobalLoopingAnimations::add,
                processedImageAssetSets::add,
                processedFonts::add,
                processedAnimatedMouseCursorProviders::add,
                processedStaticMouseCursors::add
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsPreloaderImpl(
                assetDefinitionsDTO,
                THREAD_POOL_SIZE,
                assetTypeBatchSizes,
                mockImageFactory,
                mockFontFactory,
                mockSpriteFactory,
                mockAnimationFactory,
                mockGlobalLoopingAnimationFactory,
                mockImageAssetSetFactory,
                mockMouseCursorImageFactory,
                mockAnimatedMouseCursorProviderFactory,
                mockStaticMouseCursorProviderFactory,
                processedSprites::add,
                null,
                processedGlobalLoopingAnimations::add,
                processedImageAssetSets::add,
                processedFonts::add,
                processedAnimatedMouseCursorProviders::add,
                processedStaticMouseCursors::add
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsPreloaderImpl(
                assetDefinitionsDTO,
                THREAD_POOL_SIZE,
                assetTypeBatchSizes,
                mockImageFactory,
                mockFontFactory,
                mockSpriteFactory,
                mockAnimationFactory,
                mockGlobalLoopingAnimationFactory,
                mockImageAssetSetFactory,
                mockMouseCursorImageFactory,
                mockAnimatedMouseCursorProviderFactory,
                mockStaticMouseCursorProviderFactory,
                processedSprites::add,
                processedAnimations::add,
                null,
                processedImageAssetSets::add,
                processedFonts::add,
                processedAnimatedMouseCursorProviders::add,
                processedStaticMouseCursors::add
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsPreloaderImpl(
                assetDefinitionsDTO,
                THREAD_POOL_SIZE,
                assetTypeBatchSizes,
                mockImageFactory,
                mockFontFactory,
                mockSpriteFactory,
                mockAnimationFactory,
                mockGlobalLoopingAnimationFactory,
                mockImageAssetSetFactory,
                mockMouseCursorImageFactory,
                mockAnimatedMouseCursorProviderFactory,
                mockStaticMouseCursorProviderFactory,
                processedSprites::add,
                processedAnimations::add,
                processedGlobalLoopingAnimations::add,
                null,
                processedFonts::add,
                processedAnimatedMouseCursorProviders::add,
                processedStaticMouseCursors::add
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsPreloaderImpl(
                assetDefinitionsDTO,
                THREAD_POOL_SIZE,
                assetTypeBatchSizes,
                mockImageFactory,
                mockFontFactory,
                mockSpriteFactory,
                mockAnimationFactory,
                mockGlobalLoopingAnimationFactory,
                mockImageAssetSetFactory,
                mockMouseCursorImageFactory,
                mockAnimatedMouseCursorProviderFactory,
                mockStaticMouseCursorProviderFactory,
                processedSprites::add,
                processedAnimations::add,
                processedGlobalLoopingAnimations::add,
                processedImageAssetSets::add,
                null,
                processedAnimatedMouseCursorProviders::add,
                processedStaticMouseCursors::add
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsPreloaderImpl(
                assetDefinitionsDTO,
                THREAD_POOL_SIZE,
                assetTypeBatchSizes,
                mockImageFactory,
                mockFontFactory,
                mockSpriteFactory,
                mockAnimationFactory,
                mockGlobalLoopingAnimationFactory,
                mockImageAssetSetFactory,
                mockMouseCursorImageFactory,
                mockAnimatedMouseCursorProviderFactory,
                mockStaticMouseCursorProviderFactory,
                processedSprites::add,
                processedAnimations::add,
                processedGlobalLoopingAnimations::add,
                processedImageAssetSets::add,
                processedFonts::add,
                null,
                processedStaticMouseCursors::add
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsPreloaderImpl(
                assetDefinitionsDTO,
                THREAD_POOL_SIZE,
                assetTypeBatchSizes,
                mockImageFactory,
                mockFontFactory,
                mockSpriteFactory,
                mockAnimationFactory,
                mockGlobalLoopingAnimationFactory,
                mockImageAssetSetFactory,
                mockMouseCursorImageFactory,
                mockAnimatedMouseCursorProviderFactory,
                mockStaticMouseCursorProviderFactory,
                processedSprites::add,
                processedAnimations::add,
                processedGlobalLoopingAnimations::add,
                processedImageAssetSets::add,
                processedFonts::add,
                processedAnimatedMouseCursorProviders::add,
                null
        ));
    }

    @Test
    public void testAllAssetsProcessedProperly() {
        graphicsPreloader.load();

        assertAssetsProcessed(
                ImageDefinition.class,
                NUMBER_OF_IMAGES,
                imageFactoryOutputs,
                imageDefinitionDTOs,
                Image::relativeLocation,
                imageDefinitionDTO -> imageDefinitionDTO.RelativeLocation,
                null
        );

        assertAssetsWithIdProcessed(
                FontDefinition.class,
                NUMBER_OF_FONTS,
                fontFactoryOutputs,
                fontDefinitionDTOs,
                fontDefinitionDTO -> fontDefinitionDTO.id,
                null
        );

        assertAssetsWithIdProcessed(
                SpriteDefinition.class,
                NUMBER_OF_SPRITES,
                processedSprites,
                spriteDefinitionDTOs,
                spriteDefinitionDTO -> spriteDefinitionDTO.id,
                sprite -> {
                    List<Image> matchingOutput =
                            imageFactoryOutputs.stream().filter(image ->
                                    image.relativeLocation()
                                            .equals(sprite.image().relativeLocation())).toList();
                    assertEquals(1, matchingOutput.size());
                }
        );

        assertAssetsWithIdProcessed(
                AnimationDefinition.class,
                NUMBER_OF_ANIMATIONS,
                processedAnimations,
                animationDefinitionDTOs,
                animationDefinitionDTO -> animationDefinitionDTO.id,
                animation -> {
                    List<Image> matchingOutput =
                            imageFactoryOutputs.stream().filter(image ->
                                    image == (animation.snippetAtFrame(0).image())).toList();
                    assertEquals(1, matchingOutput.size());
                }
        );

        assertAssetsProcessed(
                GlobalLoopingAnimationDefinition.class,
                NUMBER_OF_GLOBAL_LOOPING_ANIMATIONS,
                processedGlobalLoopingAnimations,
                globalLoopingAnimationDefinitionDTOs,
                GlobalLoopingAnimation::id,
                globalLoopingAnimationDefinitionDTO -> globalLoopingAnimationDefinitionDTO.id,
                null
        );

        assertAssetsWithIdProcessed(
                ImageAssetSetDefinition.class,
                NUMBER_OF_IMAGE_ASSET_SETS,
                processedImageAssetSets,
                imageAssetSetDefinitionDTOs,
                imageAssetSetDefinitionDTO -> imageAssetSetDefinitionDTO.id,
                null
        );

        assertAssetsProcessed(
                MouseCursorImageDefinition.class,
                NUMBER_OF_MOUSE_CURSOR_IMAGES,
                mouseCursorImageFactoryOutputs,
                mouseCursorImageDefinitionDTOs,
                MouseCursorImageFactory.Output::relativeLocation,
                mouseCursorImageDefinitionDTO -> mouseCursorImageDefinitionDTO.RelativeLocation,
                null
        );

        assertAssetsWithIdProcessed(
                AnimatedMouseCursorProviderDefinition.class,
                NUMBER_OF_ANIMATED_MOUSE_CURSORS,
                processedAnimatedMouseCursorProviders,
                animatedMouseCursorDefinitionDTOs,
                animatedMouseCursorDefinitionDTO -> animatedMouseCursorDefinitionDTO.Id,
                null
        );

        assertAssetsWithIdProcessed(
                StaticMouseCursorProviderDefinition.class,
                NUMBER_OF_STATIC_MOUSE_CURSORS,
                processedStaticMouseCursors,
                staticMouseCursorDefinitionDTOs,
                staticMouseCursorDefinitionDTO -> staticMouseCursorDefinitionDTO.Id,
                staticMouseCursorProvider -> {
                    List<MouseCursorImageFactory.Output> matchingOutput =
                            mouseCursorImageFactoryOutputs.stream().filter(output ->
                                    output.id() == staticMouseCursorProvider.provide(0L)).toList();
                    assertEquals(1, matchingOutput.size());
                }
        );

        assertAssetsWithIdProcessed(
                FontDefinition.class,
                NUMBER_OF_FONTS,
                processedFonts,
                fontDefinitionDTOs,
                fontDefinitionDTO -> fontDefinitionDTO.id,
                null
        );

        // For StaticMouseCursors, the "value" is the Long generated by Image; test whether the
        // Long values are pulled in order
    }

    private <TDefinitionDTO, TDefinition, TAsset extends HasId> void assertAssetsWithIdProcessed(
            Class<TDefinition> definitionClass,
            int numberOfAssets,
            List<TAsset> assetsProcessed,
            TDefinitionDTO[] definitionDTOs,
            Function<TDefinitionDTO, String> getDefinitionDTOId,
            Consumer<TAsset> additionalDefinitionAssertions) {
        assertAssetsProcessed(
                definitionClass,
                numberOfAssets,
                assetsProcessed,
                definitionDTOs,
                HasId::id,
                getDefinitionDTOId,
                additionalDefinitionAssertions
        );
    }

    private <TDefinitionDTO, TDefinition, TAsset> void assertAssetsProcessed(
            Class<TDefinition> definitionClass,
            int numberOfAssets,
            List<TAsset> assetsProcessed,
            TDefinitionDTO[] definitionDTOs,
            Function<TAsset, String> getAssetId,
            Function<TDefinitionDTO, String> getDefinitionDTOId,
            Consumer<TAsset> additionalDefinitionAssertions) {
        var definitionsProcessed = allDefinitionsProcessedInOrder.stream()
                .filter(definitionClass::isInstance).toList();
        assertEquals(numberOfAssets, assetsProcessed.size());
        assertEquals(numberOfAssets, definitionsProcessed.size());
        for (var definitionDTO : definitionDTOs) {
            assertEquals(1, (int) assetsProcessed.stream().filter(output ->
                            getAssetId.apply(output).equals(getDefinitionDTOId.apply(definitionDTO)))
                    .count());
        }
        if (additionalDefinitionAssertions != null) {
            assetsProcessed.forEach(additionalDefinitionAssertions);
        }
    }

    @Test
    public void testAllAssetsLoadedInProperOrder() {
        graphicsPreloader.load();

        // Ensure that all Images are loaded first and foremost

        assertEquals(0, firstImageIndex);
        assertTrue(lastImageIndex < firstSpriteIndex);
        assertTrue(lastImageIndex < firstAnimationIndex);
        assertTrue(lastImageIndex < firstGlobalLoopingAnimationIndex);
        assertTrue(lastImageIndex < firstImageAssetSetIndex);
        assertTrue(lastImageIndex < firstMouseCursorImageIndex);
        assertTrue(lastImageIndex < firstAnimatedMouseCursorIndex);
        assertTrue(lastImageIndex < firstStaticMouseCursorIndex);
        assertTrue(lastImageIndex < firstFontIndex);

        // Ensure that all Animations are loaded prior to any GlobalLoopingAnimations

        assertTrue(lastAnimationIndex < firstGlobalLoopingAnimationIndex);

        // Ensure that all Sprites, Animations, and GlobalLoopingAnimations are loaded prior to any
        // ImageAssetSets

        assertTrue(lastSpriteIndex < firstImageAssetSetIndex);
        assertTrue(lastAnimationIndex < firstImageAssetSetIndex);
        assertTrue(lastGlobalLoopingAnimationIndex < firstImageAssetSetIndex);

        // Ensure that all MouseCursorImages are loaded prior to any animated or static mouse
        // cursor providers

        assertTrue(lastMouseCursorImageIndex < firstStaticMouseCursorIndex);
        assertTrue(lastMouseCursorImageIndex < firstAnimatedMouseCursorIndex);

        // Ensure that all MouseCursorImages are loaded prior to any Fonts

        assertTrue(lastMouseCursorImageIndex < firstFontIndex);
    }

    @Test
    public void testExceptionThrownInSpawnedThreadPropagatesToMain() {
        var brokenImageAssetSetFactory = mock(ImageAssetSetFactory.class);
        when(brokenImageAssetSetFactory.make(any()))
                .thenThrow(new IllegalArgumentException("This is the exception"));

        GraphicsPreloader graphicsPreloader = new GraphicsPreloaderImpl(
                assetDefinitionsDTO,
                THREAD_POOL_SIZE,
                assetTypeBatchSizes,
                mockImageFactory,
                mockFontFactory,
                mockSpriteFactory,
                mockAnimationFactory,
                mockGlobalLoopingAnimationFactory,
                brokenImageAssetSetFactory,
                mockMouseCursorImageFactory,
                mockAnimatedMouseCursorProviderFactory,
                mockStaticMouseCursorProviderFactory,
                processedSprites::add,
                processedAnimations::add,
                processedGlobalLoopingAnimations::add,
                processedImageAssetSets::add,
                processedFonts::add,
                processedAnimatedMouseCursorProviders::add,
                processedStaticMouseCursors::add
        );

        assertThrows(IllegalArgumentException.class, graphicsPreloader::load);
    }

    private ImageDefinitionDTO randomImageDefinitionDTO() {
        return new ImageDefinitionDTO(randomString(), randomBoolean());
    }

    private FontDefinitionDTO randomFontDefinitionDTO() {
        return new FontDefinitionDTO(randomString(), randomString(),
                randomFloatInRange(0, 0.5f), randomFloatInRange(0, 0.5f),
                randomFontStyleDefinitionDTO(), randomFontStyleDefinitionDTO(),
                randomFontStyleDefinitionDTO(), randomFontStyleDefinitionDTO());
    }

    private FontStyleDefinitionDTO randomFontStyleDefinitionDTO() {
        return new FontStyleDefinitionDTO(randomFloatInRange(0, 0.5f),
                new FontStyleDefinitionGlyphPropertyDTO[0],
                new FontStyleDefinitionGlyphPropertyDTO[0],
                randomFloatInRange(0, 0.5f));
    }

    private SpriteDefinitionDTO randomSpriteDefinitionDTO(int i) {
        return new SpriteDefinitionDTO(randomString(), imageDefinitionDTOs[i].RelativeLocation,
                randomIntInRange(0, 100), randomIntInRange(0, 100), randomIntInRange(101, 200),
                randomIntInRange(101, 200));
    }

    private AnimationDefinitionDTO randomAnimationDefinitionDTO(int i) {
        return new AnimationDefinitionDTO(randomString(), randomIntInRange(1, Integer.MAX_VALUE),
                randomAnimationFrameDefinitionDTOs(i));
    }

    private AnimationFrameDefinitionDTO[] randomAnimationFrameDefinitionDTOs(int i) {
        return new AnimationFrameDefinitionDTO[]{
                new AnimationFrameDefinitionDTO(imageDefinitionDTOs[i].RelativeLocation, 0,
                        randomIntInRange(0, 100), randomIntInRange(0, 100),
                        randomIntInRange(101, 200), randomIntInRange(101, 200),
                        randomFloatInRange(0, 1), randomFloatInRange(0, 1))
        };
    }

    private GlobalLoopingAnimationDefinitionDTO randomGlobalLoopingAnimationDefinitionDTO() {
        return new GlobalLoopingAnimationDefinitionDTO(randomString(), randomString(),
                randomIntWithInclusiveFloor(0));
    }

    private ImageAssetSetDefinitionDTO randomImageAssetSetDefinitionDTO() {
        return new ImageAssetSetDefinitionDTO(randomString(),
                new ImageAssetSetAssetDefinitionDTO[]{randomImageAssetSetAssetDefinitionDTO()});
    }

    private ImageAssetSetAssetDefinitionDTO randomImageAssetSetAssetDefinitionDTO() {
        return new ImageAssetSetAssetDefinitionDTO(randomString(), SOUTHWEST,
                randomIntInRange(1, 3), randomString());
    }

    private MouseCursorImageDefinitionDTO randomMouseCursorImageDefinitionDTO() {
        return new MouseCursorImageDefinitionDTO(randomString(), randomIntWithInclusiveFloor(0),
                randomIntWithInclusiveFloor(0));
    }

    private AnimatedMouseCursorDefinitionDTO randomAnimatedMouseCursorDefinitionDTO() {
        return new AnimatedMouseCursorDefinitionDTO(randomString(),
                new AnimatedMouseCursorFrameDefinitionDTO[]{
                        randomAnimatedMouseCursorFrameDefinitionDTO()
                }, randomIntWithInclusiveFloor(0), 0, randomLongWithInclusiveCeiling(0),
                randomLongWithInclusiveFloor(0));
    }

    private AnimatedMouseCursorFrameDefinitionDTO randomAnimatedMouseCursorFrameDefinitionDTO() {
        return new AnimatedMouseCursorFrameDefinitionDTO(0, randomString());
    }

    private StaticMouseCursorDefinitionDTO randomStaticMouseCursorDefinitionDTO(int i) {
        return new StaticMouseCursorDefinitionDTO(randomString(),
                mouseCursorImageDefinitionDTOs[i].RelativeLocation);
    }
}
