package inaugural.soliloquy.io.test.unit.graphics.bootstrap.assetfactories;

import inaugural.soliloquy.io.graphics.bootstrap.assetfactories.ImageAssetSetFactory;
import inaugural.soliloquy.io.test.testdoubles.fakes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.io.graphics.assets.*;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.ImageAssetSetAssetDefinition;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.ImageAssetSetDefinition;

import java.util.List;
import java.util.Map;

import static inaugural.soliloquy.tools.collections.Collections.*;
import static inaugural.soliloquy.tools.random.Random.randomString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;
import static soliloquy.specs.io.graphics.assets.ImageAsset.ImageAssetType;
import static soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.ImageAssetSetAssetDefinition.DisplayParam;

@ExtendWith(MockitoExtension.class)
public class ImageAssetSetFactoryTests {
    private final String IMAGE_ASSET_SET_ID = randomString();

    private final FakeImage CAPTURING_IMAGE = new FakeImage(true);
    private final FakeImage NON_CAPTURING_IMAGE = new FakeImage(false);

    private final String SPRITE_1_ID = randomString();
    private final FakeSprite SPRITE_1 = new FakeSprite(SPRITE_1_ID, CAPTURING_IMAGE);

    private final String SPRITE_2_ID = randomString();
    private final FakeSprite SPRITE_2 = new FakeSprite(SPRITE_2_ID, CAPTURING_IMAGE);

    private final String ANIMATION_1_ID = randomString();
    private final FakeAnimation ANIMATION_1 = new FakeAnimation(ANIMATION_1_ID, true);

    private final String ANIMATION_2_ID = randomString();
    private final FakeAnimation ANIMATION_2 = new FakeAnimation(ANIMATION_2_ID, true);

    private final String GLOBAL_LOOPING_ANIMATION_1_ID = randomString();
    private final String GLOBAL_LOOPING_ANIMATION_2_ID = randomString();

    private final Map<String, Sprite> SPRITES = mapOf();
    private final Map<String, Animation> ANIMATIONS = mapOf();
    private final Map<String, GlobalLoopingAnimation> GLOBAL_LOOPING_ANIMATIONS = mapOf();

    private final String STANCE_PARAM_NAME = randomString();
    private final String STANCE_1 = randomString();
    private final String STANCE_2 = randomString();
    private final String STANCE_3 = randomString();
    private final String DIRECTION_PARAM_NAME = randomString();
    private final String DIRECTION1 = randomString();
    private final String DIRECTION2 = randomString();
    private final String DIRECTION3 = randomString();

    private final ImageAssetSetAssetDefinition ASSET_1_DEFINITION =
            new ImageAssetSetAssetDefinition(ImageAssetType.SPRITE, SPRITE_1_ID,
                    new DisplayParam(STANCE_PARAM_NAME, STANCE_1));
    private final ImageAssetSetAssetDefinition ASSET_2_DEFINITION =
            new ImageAssetSetAssetDefinition(ImageAssetType.SPRITE, SPRITE_2_ID,
                    new DisplayParam(DIRECTION_PARAM_NAME, DIRECTION1));
    private final ImageAssetSetAssetDefinition ASSET_3_DEFINITION =
            new ImageAssetSetAssetDefinition(ImageAssetType.ANIMATION, ANIMATION_1_ID,
                    new DisplayParam(STANCE_PARAM_NAME, STANCE_2));
    private final ImageAssetSetAssetDefinition ASSET_4_DEFINITION =
            new ImageAssetSetAssetDefinition(ImageAssetType.ANIMATION, ANIMATION_2_ID,
                    new DisplayParam(DIRECTION_PARAM_NAME, DIRECTION2));
    private final ImageAssetSetAssetDefinition ASSET_5_DEFINITION =
            new ImageAssetSetAssetDefinition(ImageAssetType.GLOBAL_LOOPING_ANIMATION,
                    GLOBAL_LOOPING_ANIMATION_1_ID, new DisplayParam(STANCE_PARAM_NAME, STANCE_3));
    private final ImageAssetSetAssetDefinition ASSET_6_DEFINITION =
            new ImageAssetSetAssetDefinition(ImageAssetType.GLOBAL_LOOPING_ANIMATION,
                    GLOBAL_LOOPING_ANIMATION_2_ID,
                    new DisplayParam(DIRECTION_PARAM_NAME, DIRECTION3));

    private final List<ImageAssetSetAssetDefinition> IMAGE_ASSET_SET_ASSET_DEFINITIONS =
            listOf(ASSET_1_DEFINITION, ASSET_2_DEFINITION, ASSET_3_DEFINITION, ASSET_4_DEFINITION,
                    ASSET_5_DEFINITION, ASSET_6_DEFINITION);
    private final ImageAssetSetDefinition IMAGE_ASSET_SET_DEFINITION =
            new ImageAssetSetDefinition(IMAGE_ASSET_SET_ID, IMAGE_ASSET_SET_ASSET_DEFINITIONS);

    @Mock private GlobalLoopingAnimation mockGlobalLoopingAnimation1;
    @Mock private GlobalLoopingAnimation mockGlobalLoopingAnimation2;

    private AssetFactory<ImageAssetSetDefinition, ImageAssetSet> factory;

    @BeforeEach
    public void setUp() {
        lenient().when(mockGlobalLoopingAnimation1.id()).thenReturn(GLOBAL_LOOPING_ANIMATION_1_ID);
        lenient().when(mockGlobalLoopingAnimation1.supportsMouseEvents()).thenReturn(true);
        lenient().when(mockGlobalLoopingAnimation2.id()).thenReturn(GLOBAL_LOOPING_ANIMATION_2_ID);
        lenient().when(mockGlobalLoopingAnimation2.supportsMouseEvents()).thenReturn(true);

        SPRITES.put(SPRITE_1_ID, SPRITE_1);
        SPRITES.put(SPRITE_2_ID, SPRITE_2);

        ANIMATIONS.put(ANIMATION_1_ID, ANIMATION_1);
        ANIMATIONS.put(ANIMATION_2_ID, ANIMATION_2);

        GLOBAL_LOOPING_ANIMATIONS.put(GLOBAL_LOOPING_ANIMATION_1_ID, mockGlobalLoopingAnimation1);
        GLOBAL_LOOPING_ANIMATIONS.put(GLOBAL_LOOPING_ANIMATION_2_ID, mockGlobalLoopingAnimation2);

        factory = new ImageAssetSetFactory(SPRITES::get, ANIMATIONS::get,
                GLOBAL_LOOPING_ANIMATIONS::get);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetFactory(null, ANIMATIONS::get,
                        GLOBAL_LOOPING_ANIMATIONS::get));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetFactory(SPRITES::get, null,
                        GLOBAL_LOOPING_ANIMATIONS::get));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetFactory(SPRITES::get, ANIMATIONS::get, null));
    }

    @Test
    public void testCreateWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> factory.make(null));

        assertThrows(IllegalArgumentException.class, () -> factory.make(
                new ImageAssetSetDefinition(null, IMAGE_ASSET_SET_ASSET_DEFINITIONS)));

        assertThrows(IllegalArgumentException.class, () -> factory.make(
                new ImageAssetSetDefinition("", IMAGE_ASSET_SET_ASSET_DEFINITIONS)));

        assertThrows(IllegalArgumentException.class,
                () -> factory.make(new ImageAssetSetDefinition(IMAGE_ASSET_SET_ID, null)));

        assertThrows(IllegalArgumentException.class,
                () -> factory.make(new ImageAssetSetDefinition(IMAGE_ASSET_SET_ID, listOf())));

        assertThrows(IllegalArgumentException.class, () -> factory.make(
                new ImageAssetSetDefinition(IMAGE_ASSET_SET_ID,
                        listOf(new ImageAssetSetAssetDefinition(ImageAssetType.SPRITE, null,
                                        new DisplayParam(STANCE_PARAM_NAME, STANCE_1)),
                                ASSET_2_DEFINITION,
                                ASSET_3_DEFINITION, ASSET_4_DEFINITION, ASSET_5_DEFINITION,
                                ASSET_6_DEFINITION))));

        assertThrows(IllegalArgumentException.class, () -> factory.make(
                new ImageAssetSetDefinition(IMAGE_ASSET_SET_ID,
                        listOf(new ImageAssetSetAssetDefinition(ImageAssetType.SPRITE, "",
                                        new DisplayParam(STANCE_PARAM_NAME, STANCE_1)),
                                ASSET_2_DEFINITION,
                                ASSET_3_DEFINITION, ASSET_4_DEFINITION, ASSET_5_DEFINITION,
                                ASSET_6_DEFINITION))));

        assertThrows(IllegalArgumentException.class, () -> factory.make(
                new ImageAssetSetDefinition(IMAGE_ASSET_SET_ID,
                        listOf(new ImageAssetSetAssetDefinition(ImageAssetType.SPRITE,
                                        "InvalidSpriteId", new DisplayParam(STANCE_PARAM_NAME,
                                        STANCE_1)),
                                ASSET_2_DEFINITION, ASSET_3_DEFINITION, ASSET_4_DEFINITION,
                                ASSET_5_DEFINITION, ASSET_6_DEFINITION))));

        assertThrows(IllegalArgumentException.class, () -> factory.make(
                new ImageAssetSetDefinition(IMAGE_ASSET_SET_ID,
                        listOf(ASSET_1_DEFINITION, ASSET_2_DEFINITION,
                                new ImageAssetSetAssetDefinition(ImageAssetType.ANIMATION,
                                        "InvalidAnimationId",
                                        new DisplayParam(STANCE_PARAM_NAME, STANCE_2)),
                                ASSET_4_DEFINITION, ASSET_5_DEFINITION, ASSET_6_DEFINITION))));
    }

    @Test
    public void testCreatedImageAssetSetId() {
        var output = factory.make(IMAGE_ASSET_SET_DEFINITION);

        assertEquals(IMAGE_ASSET_SET_ID, output.id());
    }

    @Test
    public void testCreatedImageAssetSetGetImageAssetWithDisplayParams() {
        var output = factory.make(IMAGE_ASSET_SET_DEFINITION);

        assertSame(SPRITE_1,
                output.getImageAssetWithDisplayParams(mapOf(pairOf(STANCE_PARAM_NAME, STANCE_1))));
        assertSame(SPRITE_2, output.getImageAssetWithDisplayParams(
                mapOf(pairOf(DIRECTION_PARAM_NAME, DIRECTION1))));
        assertSame(ANIMATION_1,
                output.getImageAssetWithDisplayParams(mapOf(pairOf(STANCE_PARAM_NAME, STANCE_2))));
        assertSame(ANIMATION_2, output.getImageAssetWithDisplayParams(
                mapOf(pairOf(DIRECTION_PARAM_NAME, DIRECTION2))));
        assertSame(mockGlobalLoopingAnimation1,
                output.getImageAssetWithDisplayParams(mapOf(pairOf(STANCE_PARAM_NAME, STANCE_3))));
        assertSame(mockGlobalLoopingAnimation2, output.getImageAssetWithDisplayParams(
                mapOf(pairOf(DIRECTION_PARAM_NAME, DIRECTION3))));
    }

    @Test
    public void testCreatedImageAssetSetGetImageAssetWithDisplayParamsWithInvalidArgs() {
        var output = factory.make(IMAGE_ASSET_SET_DEFINITION);

        assertThrows(IllegalArgumentException.class,
                () -> output.getImageAssetWithDisplayParams(null));
        assertThrows(IllegalArgumentException.class,
                () -> output.getImageAssetWithDisplayParams(mapOf()));
    }

    @Test
    public void testCreatedImageAssetSetSupportsMouseEventsCapturing() {
        var output = factory.make(IMAGE_ASSET_SET_DEFINITION);

        assertTrue(output.supportsMouseEventCapturing());

        SPRITE_1.Image = NON_CAPTURING_IMAGE;

        output = factory.make(IMAGE_ASSET_SET_DEFINITION);

        assertFalse(output.supportsMouseEventCapturing());

        SPRITE_1.Image = CAPTURING_IMAGE;

        output = factory.make(IMAGE_ASSET_SET_DEFINITION);

        assertTrue(output.supportsMouseEventCapturing());

        ANIMATION_1.SupportsMouseEventCapturing = false;

        output = factory.make(IMAGE_ASSET_SET_DEFINITION);

        assertFalse(output.supportsMouseEventCapturing());

        ANIMATION_1.SupportsMouseEventCapturing = true;

        when(mockGlobalLoopingAnimation1.supportsMouseEvents()).thenReturn(false);

        output = factory.make(IMAGE_ASSET_SET_DEFINITION);

        assertFalse(output.supportsMouseEventCapturing());
    }
}
