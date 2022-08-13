package inaugural.soliloquy.graphics.test.unit.bootstrap;

import inaugural.soliloquy.graphics.api.dto.*;
import inaugural.soliloquy.graphics.bootstrap.GraphicsPreloaderImpl;
import inaugural.soliloquy.graphics.bootstrap.assetfactories.ImageAssetSetFactory;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import inaugural.soliloquy.tools.random.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static inaugural.soliloquy.graphics.api.dto.AssetType.*;
import static inaugural.soliloquy.tools.random.Random.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GraphicsPreloaderImplTests {
    private final int THREAD_POOL_SIZE = 12;

    private HashMap<AssetType, Integer> _assetTypeBatchSizes;

    @Mock private ImageFactory _mockImageFactory;
    @Mock private AssetFactory<FontDefinition, Font> _mockFontFactory;
    @Mock private AssetFactory<SpriteDefinition, Sprite> _mockSpriteFactory;
    @Mock private AssetFactory<AnimationDefinition, Animation> _mockAnimationFactory;
    @Mock private GlobalLoopingAnimationFactory _mockGlobalLoopingAnimationFactory;
    @Mock private AssetFactory<ImageAssetSetDefinition, ImageAssetSet> _mockImageAssetSetFactory;
    @Mock private MouseCursorImageFactory _mockMouseCursorImageFactory;
    @Mock private AnimatedMouseCursorProviderFactory _mockAnimatedMouseCursorProviderFactory;
    @Mock private StaticMouseCursorProviderFactory _mockStaticMouseCursorProviderFactory;

    private AssetDefinitionsDTO _assetDefinitionsDTO;

    private ImageDefinitionDTO[] _imageDefinitionDTOs;
    private SpriteDefinitionDTO[] _spriteDefinitionDTOs;
    private AnimationDefinitionDTO[] _animationDefinitionDTOs;
    private GlobalLoopingAnimationDefinitionDTO[] _globalLoopingAnimationDefinitionDTOs;
    private ImageAssetSetDefinitionDTO[] _imageAssetSetDefinitionDTOs;
    private MouseCursorImageDefinitionDTO[] _mouseCursorImageDefinitionDTOs;
    private AnimatedMouseCursorDefinitionDTO[] _animatedMouseCursorDefinitionDTOs;
    private StaticMouseCursorDefinitionDTO[] _staticMouseCursorDefinitionDTOs;
    private FontDefinitionDTO[] _fontDefinitionDTOs;

    private CopyOnWriteArrayList<Image> _imageFactoryOutputs;
    private CopyOnWriteArrayList<Font> _fontFactoryOutputs;
    private CopyOnWriteArrayList<MouseCursorImageFactory.Output> _mouseCursorImageFactoryOutputs;

    private ArrayList<Sprite> _processedSprites;
    private ArrayList<Animation> _processedAnimations;
    private ArrayList<GlobalLoopingAnimation> _processedGlobalLoopingAnimations;
    private ArrayList<ImageAssetSet> _processedImageAssetSets;
    private ArrayList<AnimatedMouseCursorProvider> _processedAnimatedMouseCursorProviders;
    private ArrayList<StaticMouseCursorProvider> _processedStaticMouseCursors;
    private ArrayList<Font> _processedFonts;

    private CopyOnWriteArrayList<Object> _allDefinitionsProcessedInOrder;

    private int _assetsProcessedThusFar = 0;

    private int _firstImageIndex = -1;
    private int _lastImageIndex = -1;
    private int _firstFontIndex = -1;
    private int _firstSpriteIndex = -1;
    private int _lastSpriteIndex = -1;
    private int _firstAnimationIndex = -1;
    private int _lastAnimationIndex = -1;
    private int _firstGlobalLoopingAnimationIndex = -1;
    private int _lastGlobalLoopingAnimationIndex = -1;
    private int _firstImageAssetSetIndex = -1;
    private int _firstMouseCursorImageIndex = -1;
    private int _lastMouseCursorImageIndex = -1;
    private int _firstAnimatedMouseCursorIndex = -1;
    private int _firstStaticMouseCursorIndex = -1;

    private static final int BATCH_SIZE = 10;
    private static final int NUMBER_OF_IMAGES = Random.randomIntInRange(1000, 2000);
    // NB: I am making sprites and animations _equal_ to mouse cursor images to test whether the
    //     images are correctly stored and then passed onto subsequent preloading tasks
    private static final int NUMBER_OF_SPRITES = NUMBER_OF_IMAGES;
    private static final int NUMBER_OF_ANIMATIONS = NUMBER_OF_IMAGES;
    private static final int NUMBER_OF_GLOBAL_LOOPING_ANIMATIONS = NUMBER_OF_IMAGES;
    private static final int NUMBER_OF_IMAGE_ASSET_SETS = 1;//Random.randomIntInRange(1000,2000);
    private static final int NUMBER_OF_FONTS = Random.randomIntInRange(1000, 2000);
    private static final int NUMBER_OF_MOUSE_CURSOR_IMAGES = Random.randomIntInRange(1000, 2000);
    private static final int NUMBER_OF_ANIMATED_MOUSE_CURSORS = Random.randomIntInRange(1000, 2000);
    // NB: I am making static mouse cursors _equal_ to mouse cursor images to test whether the
    //     mouse cursor images are correctly stored and then passed onto subsequent preloading
    //     tasks
    private static final int NUMBER_OF_STATIC_MOUSE_CURSORS = NUMBER_OF_MOUSE_CURSOR_IMAGES;

    private GraphicsPreloader _graphicsPreloader;

    @BeforeEach
    void setUp() {
        _assetTypeBatchSizes = new HashMap<AssetType, Integer>() {{
            put(IMAGE, BATCH_SIZE);
            put(SPRITE, BATCH_SIZE);
            put(ANIMATION, BATCH_SIZE);
            put(GLOBAL_LOOPING_ANIMATION, BATCH_SIZE);
            put(IMAGE_ASSET_SET, BATCH_SIZE);
            put(FONT, BATCH_SIZE);
            put(MOUSE_CURSOR_IMAGE, BATCH_SIZE);
            put(ANIMATED_MOUSE_CURSOR_PROVIDER, BATCH_SIZE);
            put(STATIC_MOUSE_CURSOR_PROVIDER, BATCH_SIZE);
        }};

        _imageDefinitionDTOs = new ImageDefinitionDTO[NUMBER_OF_IMAGES];
        for (int i = 0; i < NUMBER_OF_IMAGES; i++) {
            _imageDefinitionDTOs[i] = randomImageDefinitionDTO();
        }

        _spriteDefinitionDTOs = new SpriteDefinitionDTO[NUMBER_OF_SPRITES];
        for (int i = 0; i < NUMBER_OF_SPRITES; i++) {
            _spriteDefinitionDTOs[i] = randomSpriteDefinitionDTO(i);
        }

        _animationDefinitionDTOs = new AnimationDefinitionDTO[NUMBER_OF_ANIMATIONS];
        for (int i = 0; i < NUMBER_OF_ANIMATIONS; i++) {
            _animationDefinitionDTOs[i] = randomAnimationDefinitionDTO(i);
        }

        _globalLoopingAnimationDefinitionDTOs =
                new GlobalLoopingAnimationDefinitionDTO[NUMBER_OF_GLOBAL_LOOPING_ANIMATIONS];
        for (int i = 0; i < NUMBER_OF_GLOBAL_LOOPING_ANIMATIONS; i++) {
            _globalLoopingAnimationDefinitionDTOs[i] = randomGlobalLoopingAnimationDefinitionDTO();
        }

        _imageAssetSetDefinitionDTOs = new ImageAssetSetDefinitionDTO[NUMBER_OF_IMAGE_ASSET_SETS];
        for (int i = 0; i < NUMBER_OF_IMAGE_ASSET_SETS; i++) {
            _imageAssetSetDefinitionDTOs[i] = randomImageAssetSetDefinitionDTO();
        }

        _fontDefinitionDTOs = new FontDefinitionDTO[NUMBER_OF_FONTS];
        for (int i = 0; i < NUMBER_OF_FONTS; i++) {
            _fontDefinitionDTOs[i] = randomFontDefinitionDTO();
        }

        _mouseCursorImageDefinitionDTOs =
                new MouseCursorImageDefinitionDTO[NUMBER_OF_MOUSE_CURSOR_IMAGES];
        for (int i = 0; i < NUMBER_OF_MOUSE_CURSOR_IMAGES; i++) {
            _mouseCursorImageDefinitionDTOs[i] = randomMouseCursorImageDefinitionDTO();
        }

        _animatedMouseCursorDefinitionDTOs =
                new AnimatedMouseCursorDefinitionDTO[NUMBER_OF_ANIMATED_MOUSE_CURSORS];
        for (int i = 0; i < NUMBER_OF_ANIMATED_MOUSE_CURSORS; i++) {
            _animatedMouseCursorDefinitionDTOs[i] = randomAnimatedMouseCursorDefinitionDTO();
        }

        _staticMouseCursorDefinitionDTOs =
                new StaticMouseCursorDefinitionDTO[NUMBER_OF_STATIC_MOUSE_CURSORS];
        for (int i = 0; i < NUMBER_OF_STATIC_MOUSE_CURSORS; i++) {
            _staticMouseCursorDefinitionDTOs[i] = randomStaticMouseCursorDefinitionDTO(i);
        }

        _assetDefinitionsDTO = new AssetDefinitionsDTO(
                _imageDefinitionDTOs,
                _fontDefinitionDTOs,
                _spriteDefinitionDTOs,
                _animationDefinitionDTOs,
                _globalLoopingAnimationDefinitionDTOs,
                _imageAssetSetDefinitionDTOs,
                _mouseCursorImageDefinitionDTOs,
                _animatedMouseCursorDefinitionDTOs,
                _staticMouseCursorDefinitionDTOs
        );

        _allDefinitionsProcessedInOrder = new CopyOnWriteArrayList<>();

        _imageFactoryOutputs = new CopyOnWriteArrayList<>();
        _mockImageFactory = mock(ImageFactory.class);
        when(_mockImageFactory.make(any()))
                .thenAnswer((Answer<Image>) invocation -> {
                    updateAssetIndices(
                            () -> _firstImageIndex,
                            i -> _firstImageIndex = i,
                            i -> _lastImageIndex = i
                    );
                    ImageDefinition definition = invocation.getArgument(0);
                    _allDefinitionsProcessedInOrder.add(definition);
                    FakeImage output = new FakeImage(definition.relativeLocation());
                    _imageFactoryOutputs.add(output);
                    return output;
                });

        _fontFactoryOutputs = new CopyOnWriteArrayList<>();
        //noinspection unchecked
        _mockFontFactory = mock(AssetFactory.class);
        when(_mockFontFactory.make(any()))
                .thenAnswer((Answer<Font>) invocation -> {
                    updateAssetIndices(
                            () -> _firstFontIndex,
                            i -> _firstFontIndex = i,
                            i -> {}
                    );
                    FontDefinition definition = invocation.getArgument(0);
                    _allDefinitionsProcessedInOrder.add(definition);
                    FakeFont output = new FakeFont(definition.id());
                    _fontFactoryOutputs.add(output);
                    return output;
                });

        //noinspection unchecked
        _mockSpriteFactory = mock(AssetFactory.class);
        when(_mockSpriteFactory.make(any()))
                .thenAnswer((Answer<Sprite>) invocation -> {
                    updateAssetIndices(
                            () -> _firstSpriteIndex,
                            i -> _firstSpriteIndex = i,
                            i -> _lastSpriteIndex = i
                    );
                    SpriteDefinition definition = invocation.getArgument(0);
                    _allDefinitionsProcessedInOrder.add(definition);
                    return new FakeSprite(definition.id(), definition.image());
                });

        //noinspection unchecked
        _mockAnimationFactory = mock(AssetFactory.class);
        when(_mockAnimationFactory.make(any()))
                .thenAnswer((Answer<Animation>) invocation -> {
                    updateAssetIndices(
                            () -> _firstAnimationIndex,
                            i -> _firstAnimationIndex = i,
                            i -> _lastAnimationIndex = i
                    );
                    AnimationDefinition definition = invocation.getArgument(0);
                    _allDefinitionsProcessedInOrder.add(definition);
                    return new FakeAnimation(definition.id(), definition.frameSnippetDefinitions());
                });

        _mockGlobalLoopingAnimationFactory = mock(GlobalLoopingAnimationFactory.class);
        when(_mockGlobalLoopingAnimationFactory.make(any()))
                .thenAnswer(invocation -> {
                    updateAssetIndices(
                            () -> _firstGlobalLoopingAnimationIndex,
                            i -> _firstGlobalLoopingAnimationIndex = i,
                            i -> _lastGlobalLoopingAnimationIndex = i
                    );
                    GlobalLoopingAnimationDefinition definition = invocation.getArgument(0);
                    _allDefinitionsProcessedInOrder.add(definition);
                    return new FakeGlobalLoopingAnimation(definition.id());
                });

        //noinspection unchecked
        _mockImageAssetSetFactory = mock(AssetFactory.class);
        when(_mockImageAssetSetFactory.make(any()))
                .thenAnswer((Answer<ImageAssetSet>) invocation -> {
                    updateAssetIndices(
                            () -> _firstImageAssetSetIndex,
                            i -> _firstImageAssetSetIndex = i,
                            i -> {}
                    );
                    ImageAssetSetDefinition definition = invocation.getArgument(0);
                    _allDefinitionsProcessedInOrder.add(definition);
                    return new FakeImageAssetSet(definition.id());
                });

        _mouseCursorImageFactoryOutputs = new CopyOnWriteArrayList<>();
        _mockMouseCursorImageFactory = mock(MouseCursorImageFactory.class);
        when(_mockMouseCursorImageFactory.make(any()))
                .thenAnswer(invocation -> {
                    updateAssetIndices(
                            () -> _firstMouseCursorImageIndex,
                            i -> _firstMouseCursorImageIndex = i,
                            i -> _lastMouseCursorImageIndex = i
                    );
                    MouseCursorImageDefinition definition = invocation.getArgument(0);
                    _allDefinitionsProcessedInOrder.add(definition);
                    MouseCursorImageFactory.Output returnValue =
                            new MouseCursorImageFactory.Output(
                                    definition.relativeLocation(),
                                    randomLong()
                            );
                    _mouseCursorImageFactoryOutputs.add(returnValue);
                    return returnValue;
                });

        _mockAnimatedMouseCursorProviderFactory = mock(AnimatedMouseCursorProviderFactory.class);
        when(_mockAnimatedMouseCursorProviderFactory.make(any()))
                .thenAnswer((Answer<AnimatedMouseCursorProvider>) invocation -> {
                    updateAssetIndices(
                            () -> _firstAnimatedMouseCursorIndex,
                            i -> _firstAnimatedMouseCursorIndex = i,
                            i -> {}
                    );
                    AnimatedMouseCursorProviderDefinition definition = invocation.getArgument(0);
                    _allDefinitionsProcessedInOrder.add(definition);
                    return new FakeAnimatedMouseCursorProvider(definition.id());
                });

        _mockStaticMouseCursorProviderFactory = mock(StaticMouseCursorProviderFactory.class);
        when(_mockStaticMouseCursorProviderFactory.make(any()))
                .thenAnswer((Answer<StaticMouseCursorProvider>) invocation -> {
                    updateAssetIndices(
                            () -> _firstStaticMouseCursorIndex,
                            i -> _firstStaticMouseCursorIndex = i,
                            i -> {}
                    );
                    StaticMouseCursorProviderDefinition definition = invocation.getArgument(0);
                    _allDefinitionsProcessedInOrder.add(definition);
                    return new FakeStaticMouseCursorProvider(definition.id(),
                            definition.mouseCursorImageId());
                });

        _processedSprites = new ArrayList<>();
        _processedAnimations = new ArrayList<>();
        _processedGlobalLoopingAnimations = new ArrayList<>();
        _processedImageAssetSets = new ArrayList<>();
        _processedFonts = new ArrayList<>();
        _processedAnimatedMouseCursorProviders = new ArrayList<>();
        _processedStaticMouseCursors = new ArrayList<>();

        _graphicsPreloader = new GraphicsPreloaderImpl(
                _assetDefinitionsDTO,
                THREAD_POOL_SIZE,
                _assetTypeBatchSizes,
                _mockImageFactory,
                _mockFontFactory,
                _mockSpriteFactory,
                _mockAnimationFactory,
                _mockGlobalLoopingAnimationFactory,
                _mockImageAssetSetFactory,
                _mockMouseCursorImageFactory,
                _mockAnimatedMouseCursorProviderFactory,
                _mockStaticMouseCursorProviderFactory,
                _processedSprites::add,
                _processedAnimations::add,
                _processedGlobalLoopingAnimations::add,
                _processedImageAssetSets::add,
                _processedFonts::add,
                _processedAnimatedMouseCursorProviders::add,
                _processedStaticMouseCursors::add
        );
    }

    private synchronized void updateAssetIndices(Supplier<Integer> getFirstIndex,
                                                 Consumer<Integer> updateFirstIndex,
                                                 Consumer<Integer> updateLastIndex) {
        if (getFirstIndex.get() < 0) {
            updateFirstIndex.accept(_assetsProcessedThusFar);
        }
        updateLastIndex.accept(_assetsProcessedThusFar);
        _assetsProcessedThusFar++;
    }

    @Test
    void constructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> new GraphicsPreloaderImpl(
                null,
                THREAD_POOL_SIZE,
                _assetTypeBatchSizes,
                _mockImageFactory,
                _mockFontFactory,
                _mockSpriteFactory,
                _mockAnimationFactory,
                _mockGlobalLoopingAnimationFactory,
                _mockImageAssetSetFactory,
                _mockMouseCursorImageFactory,
                _mockAnimatedMouseCursorProviderFactory,
                _mockStaticMouseCursorProviderFactory,
                _processedSprites::add,
                _processedAnimations::add,
                _processedGlobalLoopingAnimations::add,
                _processedImageAssetSets::add,
                _processedFonts::add,
                _processedAnimatedMouseCursorProviders::add,
                _processedStaticMouseCursors::add
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsPreloaderImpl(
                _assetDefinitionsDTO,
                THREAD_POOL_SIZE,
                null,
                _mockImageFactory,
                _mockFontFactory,
                _mockSpriteFactory,
                _mockAnimationFactory,
                _mockGlobalLoopingAnimationFactory,
                _mockImageAssetSetFactory,
                _mockMouseCursorImageFactory,
                _mockAnimatedMouseCursorProviderFactory,
                _mockStaticMouseCursorProviderFactory,
                _processedSprites::add,
                _processedAnimations::add,
                _processedGlobalLoopingAnimations::add,
                _processedImageAssetSets::add,
                _processedFonts::add,
                _processedAnimatedMouseCursorProviders::add,
                _processedStaticMouseCursors::add
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsPreloaderImpl(
                _assetDefinitionsDTO,
                THREAD_POOL_SIZE,
                _assetTypeBatchSizes,
                null,
                _mockFontFactory,
                _mockSpriteFactory,
                _mockAnimationFactory,
                _mockGlobalLoopingAnimationFactory,
                _mockImageAssetSetFactory,
                _mockMouseCursorImageFactory,
                _mockAnimatedMouseCursorProviderFactory,
                _mockStaticMouseCursorProviderFactory,
                _processedSprites::add,
                _processedAnimations::add,
                _processedGlobalLoopingAnimations::add,
                _processedImageAssetSets::add,
                _processedFonts::add,
                _processedAnimatedMouseCursorProviders::add,
                _processedStaticMouseCursors::add
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsPreloaderImpl(
                _assetDefinitionsDTO,
                THREAD_POOL_SIZE,
                _assetTypeBatchSizes,
                _mockImageFactory,
                null,
                _mockSpriteFactory,
                _mockAnimationFactory,
                _mockGlobalLoopingAnimationFactory,
                _mockImageAssetSetFactory,
                _mockMouseCursorImageFactory,
                _mockAnimatedMouseCursorProviderFactory,
                _mockStaticMouseCursorProviderFactory,
                _processedSprites::add,
                _processedAnimations::add,
                _processedGlobalLoopingAnimations::add,
                _processedImageAssetSets::add,
                _processedFonts::add,
                _processedAnimatedMouseCursorProviders::add,
                _processedStaticMouseCursors::add
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsPreloaderImpl(
                _assetDefinitionsDTO,
                THREAD_POOL_SIZE,
                _assetTypeBatchSizes,
                _mockImageFactory,
                _mockFontFactory,
                null,
                _mockAnimationFactory,
                _mockGlobalLoopingAnimationFactory,
                _mockImageAssetSetFactory,
                _mockMouseCursorImageFactory,
                _mockAnimatedMouseCursorProviderFactory,
                _mockStaticMouseCursorProviderFactory,
                _processedSprites::add,
                _processedAnimations::add,
                _processedGlobalLoopingAnimations::add,
                _processedImageAssetSets::add,
                _processedFonts::add,
                _processedAnimatedMouseCursorProviders::add,
                _processedStaticMouseCursors::add
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsPreloaderImpl(
                _assetDefinitionsDTO,
                THREAD_POOL_SIZE,
                _assetTypeBatchSizes,
                _mockImageFactory,
                _mockFontFactory,
                _mockSpriteFactory,
                null,
                _mockGlobalLoopingAnimationFactory,
                _mockImageAssetSetFactory,
                _mockMouseCursorImageFactory,
                _mockAnimatedMouseCursorProviderFactory,
                _mockStaticMouseCursorProviderFactory,
                _processedSprites::add,
                _processedAnimations::add,
                _processedGlobalLoopingAnimations::add,
                _processedImageAssetSets::add,
                _processedFonts::add,
                _processedAnimatedMouseCursorProviders::add,
                _processedStaticMouseCursors::add
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsPreloaderImpl(
                _assetDefinitionsDTO,
                THREAD_POOL_SIZE,
                _assetTypeBatchSizes,
                _mockImageFactory,
                _mockFontFactory,
                _mockSpriteFactory,
                _mockAnimationFactory,
                null,
                _mockImageAssetSetFactory,
                _mockMouseCursorImageFactory,
                _mockAnimatedMouseCursorProviderFactory,
                _mockStaticMouseCursorProviderFactory,
                _processedSprites::add,
                _processedAnimations::add,
                _processedGlobalLoopingAnimations::add,
                _processedImageAssetSets::add,
                _processedFonts::add,
                _processedAnimatedMouseCursorProviders::add,
                _processedStaticMouseCursors::add
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsPreloaderImpl(
                _assetDefinitionsDTO,
                THREAD_POOL_SIZE,
                _assetTypeBatchSizes,
                _mockImageFactory,
                _mockFontFactory,
                _mockSpriteFactory,
                _mockAnimationFactory,
                _mockGlobalLoopingAnimationFactory,
                null,
                _mockMouseCursorImageFactory,
                _mockAnimatedMouseCursorProviderFactory,
                _mockStaticMouseCursorProviderFactory,
                _processedSprites::add,
                _processedAnimations::add,
                _processedGlobalLoopingAnimations::add,
                _processedImageAssetSets::add,
                _processedFonts::add,
                _processedAnimatedMouseCursorProviders::add,
                _processedStaticMouseCursors::add
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsPreloaderImpl(
                _assetDefinitionsDTO,
                THREAD_POOL_SIZE,
                _assetTypeBatchSizes,
                _mockImageFactory,
                _mockFontFactory,
                _mockSpriteFactory,
                _mockAnimationFactory,
                _mockGlobalLoopingAnimationFactory,
                _mockImageAssetSetFactory,
                null,
                _mockAnimatedMouseCursorProviderFactory,
                _mockStaticMouseCursorProviderFactory,
                _processedSprites::add,
                _processedAnimations::add,
                _processedGlobalLoopingAnimations::add,
                _processedImageAssetSets::add,
                _processedFonts::add,
                _processedAnimatedMouseCursorProviders::add,
                _processedStaticMouseCursors::add
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsPreloaderImpl(
                _assetDefinitionsDTO,
                THREAD_POOL_SIZE,
                _assetTypeBatchSizes,
                _mockImageFactory,
                _mockFontFactory,
                _mockSpriteFactory,
                _mockAnimationFactory,
                _mockGlobalLoopingAnimationFactory,
                _mockImageAssetSetFactory,
                _mockMouseCursorImageFactory,
                null,
                _mockStaticMouseCursorProviderFactory,
                _processedSprites::add,
                _processedAnimations::add,
                _processedGlobalLoopingAnimations::add,
                _processedImageAssetSets::add,
                _processedFonts::add,
                _processedAnimatedMouseCursorProviders::add,
                _processedStaticMouseCursors::add
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsPreloaderImpl(
                _assetDefinitionsDTO,
                THREAD_POOL_SIZE,
                _assetTypeBatchSizes,
                _mockImageFactory,
                _mockFontFactory,
                _mockSpriteFactory,
                _mockAnimationFactory,
                _mockGlobalLoopingAnimationFactory,
                _mockImageAssetSetFactory,
                _mockMouseCursorImageFactory,
                _mockAnimatedMouseCursorProviderFactory,
                null,
                _processedSprites::add,
                _processedAnimations::add,
                _processedGlobalLoopingAnimations::add,
                _processedImageAssetSets::add,
                _processedFonts::add,
                _processedAnimatedMouseCursorProviders::add,
                _processedStaticMouseCursors::add
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsPreloaderImpl(
                _assetDefinitionsDTO,
                THREAD_POOL_SIZE,
                _assetTypeBatchSizes,
                _mockImageFactory,
                _mockFontFactory,
                _mockSpriteFactory,
                _mockAnimationFactory,
                _mockGlobalLoopingAnimationFactory,
                _mockImageAssetSetFactory,
                _mockMouseCursorImageFactory,
                _mockAnimatedMouseCursorProviderFactory,
                _mockStaticMouseCursorProviderFactory,
                null,
                _processedAnimations::add,
                _processedGlobalLoopingAnimations::add,
                _processedImageAssetSets::add,
                _processedFonts::add,
                _processedAnimatedMouseCursorProviders::add,
                _processedStaticMouseCursors::add
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsPreloaderImpl(
                _assetDefinitionsDTO,
                THREAD_POOL_SIZE,
                _assetTypeBatchSizes,
                _mockImageFactory,
                _mockFontFactory,
                _mockSpriteFactory,
                _mockAnimationFactory,
                _mockGlobalLoopingAnimationFactory,
                _mockImageAssetSetFactory,
                _mockMouseCursorImageFactory,
                _mockAnimatedMouseCursorProviderFactory,
                _mockStaticMouseCursorProviderFactory,
                _processedSprites::add,
                null,
                _processedGlobalLoopingAnimations::add,
                _processedImageAssetSets::add,
                _processedFonts::add,
                _processedAnimatedMouseCursorProviders::add,
                _processedStaticMouseCursors::add
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsPreloaderImpl(
                _assetDefinitionsDTO,
                THREAD_POOL_SIZE,
                _assetTypeBatchSizes,
                _mockImageFactory,
                _mockFontFactory,
                _mockSpriteFactory,
                _mockAnimationFactory,
                _mockGlobalLoopingAnimationFactory,
                _mockImageAssetSetFactory,
                _mockMouseCursorImageFactory,
                _mockAnimatedMouseCursorProviderFactory,
                _mockStaticMouseCursorProviderFactory,
                _processedSprites::add,
                _processedAnimations::add,
                null,
                _processedImageAssetSets::add,
                _processedFonts::add,
                _processedAnimatedMouseCursorProviders::add,
                _processedStaticMouseCursors::add
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsPreloaderImpl(
                _assetDefinitionsDTO,
                THREAD_POOL_SIZE,
                _assetTypeBatchSizes,
                _mockImageFactory,
                _mockFontFactory,
                _mockSpriteFactory,
                _mockAnimationFactory,
                _mockGlobalLoopingAnimationFactory,
                _mockImageAssetSetFactory,
                _mockMouseCursorImageFactory,
                _mockAnimatedMouseCursorProviderFactory,
                _mockStaticMouseCursorProviderFactory,
                _processedSprites::add,
                _processedAnimations::add,
                _processedGlobalLoopingAnimations::add,
                null,
                _processedFonts::add,
                _processedAnimatedMouseCursorProviders::add,
                _processedStaticMouseCursors::add
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsPreloaderImpl(
                _assetDefinitionsDTO,
                THREAD_POOL_SIZE,
                _assetTypeBatchSizes,
                _mockImageFactory,
                _mockFontFactory,
                _mockSpriteFactory,
                _mockAnimationFactory,
                _mockGlobalLoopingAnimationFactory,
                _mockImageAssetSetFactory,
                _mockMouseCursorImageFactory,
                _mockAnimatedMouseCursorProviderFactory,
                _mockStaticMouseCursorProviderFactory,
                _processedSprites::add,
                _processedAnimations::add,
                _processedGlobalLoopingAnimations::add,
                _processedImageAssetSets::add,
                null,
                _processedAnimatedMouseCursorProviders::add,
                _processedStaticMouseCursors::add
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsPreloaderImpl(
                _assetDefinitionsDTO,
                THREAD_POOL_SIZE,
                _assetTypeBatchSizes,
                _mockImageFactory,
                _mockFontFactory,
                _mockSpriteFactory,
                _mockAnimationFactory,
                _mockGlobalLoopingAnimationFactory,
                _mockImageAssetSetFactory,
                _mockMouseCursorImageFactory,
                _mockAnimatedMouseCursorProviderFactory,
                _mockStaticMouseCursorProviderFactory,
                _processedSprites::add,
                _processedAnimations::add,
                _processedGlobalLoopingAnimations::add,
                _processedImageAssetSets::add,
                _processedFonts::add,
                null,
                _processedStaticMouseCursors::add
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsPreloaderImpl(
                _assetDefinitionsDTO,
                THREAD_POOL_SIZE,
                _assetTypeBatchSizes,
                _mockImageFactory,
                _mockFontFactory,
                _mockSpriteFactory,
                _mockAnimationFactory,
                _mockGlobalLoopingAnimationFactory,
                _mockImageAssetSetFactory,
                _mockMouseCursorImageFactory,
                _mockAnimatedMouseCursorProviderFactory,
                _mockStaticMouseCursorProviderFactory,
                _processedSprites::add,
                _processedAnimations::add,
                _processedGlobalLoopingAnimations::add,
                _processedImageAssetSets::add,
                _processedFonts::add,
                _processedAnimatedMouseCursorProviders::add,
                null
        ));
    }

    @Test
    void testAllAssetsProcessedProperly() {
        _graphicsPreloader.load();

        assertAssetsProcessed(
                ImageDefinition.class,
                NUMBER_OF_IMAGES,
                _imageFactoryOutputs,
                _imageDefinitionDTOs,
                Image::relativeLocation,
                imageDefinitionDTO -> imageDefinitionDTO.RelativeLocation,
                null
        );

        assertAssetsWithIdProcessed(
                FontDefinition.class,
                NUMBER_OF_FONTS,
                _fontFactoryOutputs,
                _fontDefinitionDTOs,
                fontDefinitionDTO -> fontDefinitionDTO.id,
                null
        );

        assertAssetsWithIdProcessed(
                SpriteDefinition.class,
                NUMBER_OF_SPRITES,
                _processedSprites,
                _spriteDefinitionDTOs,
                spriteDefinitionDTO -> spriteDefinitionDTO.id,
                sprite -> {
                    List<Image> matchingOutput =
                            _imageFactoryOutputs.stream().filter(image ->
                                    image.relativeLocation()
                                            .equals(sprite.image().relativeLocation()))
                                    .collect(Collectors.toList());
                    assertEquals(1, matchingOutput.size());
                }
        );

        assertAssetsWithIdProcessed(
                AnimationDefinition.class,
                NUMBER_OF_ANIMATIONS,
                _processedAnimations,
                _animationDefinitionDTOs,
                animationDefinitionDTO -> animationDefinitionDTO.id,
                animation -> {
                    List<Image> matchingOutput =
                            _imageFactoryOutputs.stream().filter(image ->
                                    image == (animation.snippetAtFrame(0).image()))
                                    .collect(Collectors.toList());
                    assertEquals(1, matchingOutput.size());
                }
        );

        assertAssetsProcessed(
                GlobalLoopingAnimationDefinition.class,
                NUMBER_OF_GLOBAL_LOOPING_ANIMATIONS,
                _processedGlobalLoopingAnimations,
                _globalLoopingAnimationDefinitionDTOs,
                GlobalLoopingAnimation::id,
                globalLoopingAnimationDefinitionDTO -> globalLoopingAnimationDefinitionDTO.id,
                null
        );

        assertAssetsWithIdProcessed(
                ImageAssetSetDefinition.class,
                NUMBER_OF_IMAGE_ASSET_SETS,
                _processedImageAssetSets,
                _imageAssetSetDefinitionDTOs,
                imageAssetSetDefinitionDTO -> imageAssetSetDefinitionDTO.id,
                null
        );

        assertAssetsProcessed(
                MouseCursorImageDefinition.class,
                NUMBER_OF_MOUSE_CURSOR_IMAGES,
                _mouseCursorImageFactoryOutputs,
                _mouseCursorImageDefinitionDTOs,
                MouseCursorImageFactory.Output::relativeLocation,
                mouseCursorImageDefinitionDTO -> mouseCursorImageDefinitionDTO.RelativeLocation,
                null
        );

        assertAssetsWithIdProcessed(
                AnimatedMouseCursorProviderDefinition.class,
                NUMBER_OF_ANIMATED_MOUSE_CURSORS,
                _processedAnimatedMouseCursorProviders,
                _animatedMouseCursorDefinitionDTOs,
                animatedMouseCursorDefinitionDTO -> animatedMouseCursorDefinitionDTO.Id,
                null
        );

        assertAssetsWithIdProcessed(
                StaticMouseCursorProviderDefinition.class,
                NUMBER_OF_STATIC_MOUSE_CURSORS,
                _processedStaticMouseCursors,
                _staticMouseCursorDefinitionDTOs,
                staticMouseCursorDefinitionDTO -> staticMouseCursorDefinitionDTO.Id,
                staticMouseCursorProvider -> {
                    List<MouseCursorImageFactory.Output> matchingOutput =
                            _mouseCursorImageFactoryOutputs.stream().filter(output ->
                                    output.id() == staticMouseCursorProvider.provide(0L))
                                    .collect(Collectors.toList());
                    assertEquals(1, matchingOutput.size());
                }
        );

        assertAssetsWithIdProcessed(
                FontDefinition.class,
                NUMBER_OF_FONTS,
                _processedFonts,
                _fontDefinitionDTOs,
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
        List<Object> definitionsProcessed = _allDefinitionsProcessedInOrder.stream()
                .filter(definitionClass::isInstance)
                .collect(Collectors.toList());
        assertEquals(numberOfAssets, assetsProcessed.size());
        assertEquals(numberOfAssets, definitionsProcessed.size());
        for (TDefinitionDTO definitionDTO : definitionDTOs) {
            assertEquals(1, (int) assetsProcessed.stream().filter(output ->
                    getAssetId.apply(output).equals(getDefinitionDTOId.apply(definitionDTO)))
                    .count());
        }
        if (additionalDefinitionAssertions != null) {
            assetsProcessed.forEach(additionalDefinitionAssertions);
        }
    }

    @Test
    void testAllAssetsLoadedInProperOrder() {
        _graphicsPreloader.load();

        // Ensure that all Images are loaded first and foremost

        assertEquals(0, _firstImageIndex);
        assertTrue(_lastImageIndex < _firstSpriteIndex);
        assertTrue(_lastImageIndex < _firstAnimationIndex);
        assertTrue(_lastImageIndex < _firstGlobalLoopingAnimationIndex);
        assertTrue(_lastImageIndex < _firstImageAssetSetIndex);
        assertTrue(_lastImageIndex < _firstMouseCursorImageIndex);
        assertTrue(_lastImageIndex < _firstAnimatedMouseCursorIndex);
        assertTrue(_lastImageIndex < _firstStaticMouseCursorIndex);
        assertTrue(_lastImageIndex < _firstFontIndex);

        // Ensure that all Animations are loaded prior to any GlobalLoopingAnimations

        assertTrue(_lastAnimationIndex < _firstGlobalLoopingAnimationIndex);

        // Ensure that all Sprites, Animations, and GlobalLoopingAnimations are loaded prior to any
        // ImageAssetSets

        assertTrue(_lastSpriteIndex < _firstImageAssetSetIndex);
        assertTrue(_lastAnimationIndex < _firstImageAssetSetIndex);
        assertTrue(_lastGlobalLoopingAnimationIndex < _firstImageAssetSetIndex);

        // Ensure that all MouseCursorImages are loaded prior to any animated or static mouse
        // cursor providers

        assertTrue(_lastMouseCursorImageIndex < _firstStaticMouseCursorIndex);
        assertTrue(_lastMouseCursorImageIndex < _firstAnimatedMouseCursorIndex);

        // Ensure that all MouseCursorImages are loaded prior to any Fonts

        assertTrue(_lastMouseCursorImageIndex < _firstFontIndex);
    }

    @Test
    void testExceptionThrownInSpawnedThreadPropagatesToMain() {
        ImageAssetSetFactory brokenImageAssetSetFactory = mock(ImageAssetSetFactory.class);
        when(brokenImageAssetSetFactory.make(any()))
                .thenThrow(new IllegalArgumentException("This is the exception"));

        GraphicsPreloader graphicsPreloader = new GraphicsPreloaderImpl(
                _assetDefinitionsDTO,
                THREAD_POOL_SIZE,
                _assetTypeBatchSizes,
                _mockImageFactory,
                _mockFontFactory,
                _mockSpriteFactory,
                _mockAnimationFactory,
                _mockGlobalLoopingAnimationFactory,
                brokenImageAssetSetFactory,
                _mockMouseCursorImageFactory,
                _mockAnimatedMouseCursorProviderFactory,
                _mockStaticMouseCursorProviderFactory,
                _processedSprites::add,
                _processedAnimations::add,
                _processedGlobalLoopingAnimations::add,
                _processedImageAssetSets::add,
                _processedFonts::add,
                _processedAnimatedMouseCursorProviders::add,
                _processedStaticMouseCursors::add
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
        return new SpriteDefinitionDTO(randomString(), _imageDefinitionDTOs[i].RelativeLocation,
                randomIntInRange(0, 100), randomIntInRange(0, 100), randomIntInRange(101, 200),
                randomIntInRange(101, 200));
    }

    private AnimationDefinitionDTO randomAnimationDefinitionDTO(int i) {
        return new AnimationDefinitionDTO(randomString(), randomIntInRange(1, Integer.MAX_VALUE),
                randomAnimationFrameDefinitionDTOs(i));
    }

    private AnimationFrameDefinitionDTO[] randomAnimationFrameDefinitionDTOs(int i) {
        return new AnimationFrameDefinitionDTO[]{
                new AnimationFrameDefinitionDTO(_imageDefinitionDTOs[i].RelativeLocation, 0,
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
        return new ImageAssetSetAssetDefinitionDTO(randomString(), randomString(),
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
                _mouseCursorImageDefinitionDTOs[i].RelativeLocation);
    }

    // NB: This class exists because there is a need to generate random
    //     AnimatedMouseCursorProviders rapidly

    private static class FakeAnimatedMouseCursorProvider implements AnimatedMouseCursorProvider {
        private String _id;

        private FakeAnimatedMouseCursorProvider(String id) {
            _id = id;
        }

        @Override
        public UUID uuid() throws UnsupportedOperationException {
            return null;
        }

        @Override
        public String id() throws IllegalStateException {
            return _id;
        }

        @Override
        public void reset(long l) throws IllegalArgumentException {

        }

        @Override
        public Long provide(long l) throws IllegalArgumentException {
            return null;
        }

        @Override
        public Long getArchetype() {
            return null;
        }

        @Override
        public void reportPause(long l) throws IllegalArgumentException {

        }

        @Override
        public void reportUnpause(long l) throws IllegalArgumentException {

        }

        @Override
        public Long pausedTimestamp() {
            return null;
        }

        @Override
        public Long mostRecentTimestamp() {
            return null;
        }

        @Override
        public String getInterfaceName() {
            return null;
        }
    }
}
