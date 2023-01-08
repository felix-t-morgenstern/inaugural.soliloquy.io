package inaugural.soliloquy.graphics.test.unit.bootstrap.assetfactories;

import inaugural.soliloquy.graphics.bootstrap.assetfactories.ImageAssetSetFactory;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.common.shared.Direction;
import soliloquy.specs.graphics.assets.*;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageAssetSetAssetDefinition;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageAssetSetDefinition;

import java.util.ArrayList;
import java.util.List;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static org.junit.jupiter.api.Assertions.*;
import static soliloquy.specs.common.shared.Direction.*;

class ImageAssetSetFactoryTests {
    private final String IMAGE_ASSET_SET_ID = "imageAssetSetId";

    private final FakeImage CAPTURING_IMAGE = new FakeImage(true);
    private final FakeImage NON_CAPTURING_IMAGE = new FakeImage(false);

    private final String SPRITE_1_ID = "sprite1Id";
    private final FakeSprite SPRITE_1 = new FakeSprite(SPRITE_1_ID, CAPTURING_IMAGE);

    private final String SPRITE_2_ID = "sprite2Id";
    private final FakeSprite SPRITE_2 = new FakeSprite(SPRITE_2_ID, CAPTURING_IMAGE);

    private final String ANIMATION_1_ID = "animation1Id";
    private final FakeAnimation ANIMATION_1 = new FakeAnimation(ANIMATION_1_ID, true);

    private final String ANIMATION_2_ID = "animation2Id";
    private final FakeAnimation ANIMATION_2 = new FakeAnimation(ANIMATION_2_ID, true);

    private final String GLOBAL_LOOPING_ANIMATION_1_ID = "globalLoopingAnimation1Id";
    private final FakeGlobalLoopingAnimation GLOBAL_LOOPING_ANIMATION_1 =
            new FakeGlobalLoopingAnimation(GLOBAL_LOOPING_ANIMATION_1_ID);

    private final String GLOBAL_LOOPING_ANIMATION_2_ID = "globalLoopingAnimation2Id";
    private final FakeGlobalLoopingAnimation GLOBAL_LOOPING_ANIMATION_2 =
            new FakeGlobalLoopingAnimation(GLOBAL_LOOPING_ANIMATION_2_ID);

    private final FakeRegistry<Sprite> SPRITES_REGISTRY = new FakeRegistry<Sprite>();
    private final FakeRegistry<Animation> ANIMATIONS_REGISTRY = new FakeRegistry<Animation>();
    private final FakeRegistry<GlobalLoopingAnimation> GLOBAL_LOOPING_ANIMATIONS_REGISTRY =
            new FakeRegistry<GlobalLoopingAnimation>();

    private final String TYPE1 = "type1";
    private final String TYPE2 = "type2";
    private final String TYPE3 = "type3";
    private final Direction DIRECTION1 = SOUTHWEST;
    private final Direction DIRECTION2 = SOUTH;
    private final Direction DIRECTION3 = WEST;

    private final ImageAssetSetAssetDefinition ASSET_1_DEFINITION =
            new ImageAssetSetAssetDefinition(TYPE1, null,
                    ImageAsset.ImageAssetType.SPRITE, SPRITE_1_ID);
    private final ImageAssetSetAssetDefinition ASSET_2_DEFINITION =
            new ImageAssetSetAssetDefinition("", DIRECTION1.getValue(),
                    ImageAsset.ImageAssetType.SPRITE, SPRITE_2_ID);
    private final ImageAssetSetAssetDefinition ASSET_3_DEFINITION =
            new ImageAssetSetAssetDefinition(TYPE2, null,
                    ImageAsset.ImageAssetType.ANIMATION, ANIMATION_1_ID);
    private final ImageAssetSetAssetDefinition ASSET_4_DEFINITION =
            new ImageAssetSetAssetDefinition("", DIRECTION2.getValue(),
                    ImageAsset.ImageAssetType.ANIMATION, ANIMATION_2_ID);
    private final ImageAssetSetAssetDefinition ASSET_5_DEFINITION =
            new ImageAssetSetAssetDefinition(TYPE3, null,
                    ImageAsset.ImageAssetType.GLOBAL_LOOPING_ANIMATION,
                    GLOBAL_LOOPING_ANIMATION_1_ID);
    private final ImageAssetSetAssetDefinition ASSET_6_DEFINITION =
            new ImageAssetSetAssetDefinition("", DIRECTION3.getValue(),
                    ImageAsset.ImageAssetType.GLOBAL_LOOPING_ANIMATION,
                    GLOBAL_LOOPING_ANIMATION_2_ID);

    private final List<ImageAssetSetAssetDefinition> IMAGE_ASSET_SET_ASSET_DEFINITIONS =
            listOf(ASSET_1_DEFINITION, ASSET_2_DEFINITION, ASSET_3_DEFINITION, ASSET_4_DEFINITION,
                    ASSET_5_DEFINITION, ASSET_6_DEFINITION);
    private final ImageAssetSetDefinition IMAGE_ASSET_SET_DEFINITION =
            new ImageAssetSetDefinition(IMAGE_ASSET_SET_ID, IMAGE_ASSET_SET_ASSET_DEFINITIONS);

    private AssetFactory<ImageAssetSetDefinition, ImageAssetSet> imageAssetSetFactory;

    @BeforeEach
    void setUp() {
        SPRITES_REGISTRY.add(SPRITE_1);
        SPRITES_REGISTRY.add(SPRITE_2);

        ANIMATIONS_REGISTRY.add(ANIMATION_1);
        ANIMATIONS_REGISTRY.add(ANIMATION_2);

        GLOBAL_LOOPING_ANIMATIONS_REGISTRY.add(GLOBAL_LOOPING_ANIMATION_1);
        GLOBAL_LOOPING_ANIMATIONS_REGISTRY.add(GLOBAL_LOOPING_ANIMATION_2);

        imageAssetSetFactory = new ImageAssetSetFactory(SPRITES_REGISTRY, ANIMATIONS_REGISTRY,
                GLOBAL_LOOPING_ANIMATIONS_REGISTRY);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetFactory(null, ANIMATIONS_REGISTRY,
                        GLOBAL_LOOPING_ANIMATIONS_REGISTRY));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetFactory(SPRITES_REGISTRY, null,
                        GLOBAL_LOOPING_ANIMATIONS_REGISTRY));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetFactory(SPRITES_REGISTRY, ANIMATIONS_REGISTRY,
                        null));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(AssetFactory.class.getCanonicalName() + "<" +
                        ImageAssetSetDefinition.class.getCanonicalName() + "," +
                        ImageAssetSet.class.getCanonicalName() + ">",
                imageAssetSetFactory.getInterfaceName());
    }

    @Test
    void testCreateWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> imageAssetSetFactory.make(null));

        assertThrows(IllegalArgumentException.class, () -> imageAssetSetFactory.make(
                new ImageAssetSetDefinition(null, IMAGE_ASSET_SET_ASSET_DEFINITIONS)));

        assertThrows(IllegalArgumentException.class, () -> imageAssetSetFactory.make(
                new ImageAssetSetDefinition("", IMAGE_ASSET_SET_ASSET_DEFINITIONS)));

        assertThrows(IllegalArgumentException.class, () -> imageAssetSetFactory.make(
                new ImageAssetSetDefinition(IMAGE_ASSET_SET_ID, null)
        ));

        assertThrows(IllegalArgumentException.class, () -> imageAssetSetFactory.make(
                new ImageAssetSetDefinition(IMAGE_ASSET_SET_ID, new ArrayList<>())
        ));

        assertThrows(IllegalArgumentException.class, () -> imageAssetSetFactory.make(
                new ImageAssetSetDefinition(IMAGE_ASSET_SET_ID,
                        listOf(
                                new ImageAssetSetAssetDefinition(TYPE1, null,
                                        ImageAsset.ImageAssetType.SPRITE, null),
                        ASSET_2_DEFINITION, ASSET_3_DEFINITION, ASSET_4_DEFINITION,
                        ASSET_5_DEFINITION, ASSET_6_DEFINITION
                )
        )));

        assertThrows(IllegalArgumentException.class, () -> imageAssetSetFactory.make(
                new ImageAssetSetDefinition(IMAGE_ASSET_SET_ID,
                        listOf(
                                new ImageAssetSetAssetDefinition(TYPE1, null,
                                        ImageAsset.ImageAssetType.SPRITE, ""), ASSET_2_DEFINITION,
                        ASSET_3_DEFINITION, ASSET_4_DEFINITION, ASSET_5_DEFINITION,
                        ASSET_6_DEFINITION
                )
        )));

        assertThrows(IllegalArgumentException.class, () -> imageAssetSetFactory.make(
                new ImageAssetSetDefinition(IMAGE_ASSET_SET_ID,
                        listOf(
                                new ImageAssetSetAssetDefinition(TYPE1, null,
                                        ImageAsset.ImageAssetType.SPRITE, "InvalidSpriteId"),
                        ASSET_2_DEFINITION, ASSET_3_DEFINITION, ASSET_4_DEFINITION,
                        ASSET_5_DEFINITION, ASSET_6_DEFINITION
                )
        )));

        assertThrows(IllegalArgumentException.class, () -> imageAssetSetFactory.make(
                new ImageAssetSetDefinition(IMAGE_ASSET_SET_ID,
                        listOf(
                                ASSET_1_DEFINITION, ASSET_2_DEFINITION,
                                new ImageAssetSetAssetDefinition(TYPE2, null,
                                        ImageAsset.ImageAssetType.ANIMATION, "InvalidAnimationId"),
                        ASSET_4_DEFINITION, ASSET_5_DEFINITION, ASSET_6_DEFINITION
                )
        )));

        assertThrows(IllegalArgumentException.class, () -> imageAssetSetFactory.make(
                new ImageAssetSetDefinition(IMAGE_ASSET_SET_ID,
                        listOf(
                                ASSET_1_DEFINITION, ASSET_2_DEFINITION,
                                new ImageAssetSetAssetDefinition(TYPE1, null,
                                        ImageAsset.ImageAssetType.ANIMATION, ANIMATION_1_ID),
                        ASSET_4_DEFINITION, ASSET_5_DEFINITION, ASSET_6_DEFINITION
                )
        )));
    }

    @Test
    void testCreatedImageAssetSetId() {
        var output = imageAssetSetFactory.make(IMAGE_ASSET_SET_DEFINITION);

        assertEquals(IMAGE_ASSET_SET_ID, output.id());
    }

    @Test
    void testCreatedImageAssetSetGetImageAssetForTypeAndDirection() {
        var output = imageAssetSetFactory.make(IMAGE_ASSET_SET_DEFINITION);

        // NB: The invocations here use null, whereas the asset definitions fed to the factory use
        //     empty strings; this is to test that the factory and SpriteSet treat null and empty
        //     strings interchangeably
        assertSame(SPRITE_1, output.getImageAssetForTypeAndDirection(TYPE1, null));
        assertSame(SPRITE_2, output.getImageAssetForTypeAndDirection(null, DIRECTION1));
        assertSame(SPRITE_2, output.getImageAssetForTypeAndDirection("", DIRECTION1));
        assertSame(ANIMATION_1, output.getImageAssetForTypeAndDirection(TYPE2, null));
        assertSame(ANIMATION_2, output.getImageAssetForTypeAndDirection(null, DIRECTION2));
        assertSame(ANIMATION_2, output.getImageAssetForTypeAndDirection("", DIRECTION2));
        assertSame(GLOBAL_LOOPING_ANIMATION_1, output.getImageAssetForTypeAndDirection(TYPE3, null));
        assertSame(GLOBAL_LOOPING_ANIMATION_2, output.getImageAssetForTypeAndDirection("", DIRECTION3));
        assertSame(GLOBAL_LOOPING_ANIMATION_2, output.getImageAssetForTypeAndDirection(null, DIRECTION3));
    }

    @Test
    void testCreatedImageAssetSetSupportsMouseEventsCapturing() {
        var output = imageAssetSetFactory.make(IMAGE_ASSET_SET_DEFINITION);

        assertTrue(output.supportsMouseEventCapturing());

        SPRITE_1.Image = NON_CAPTURING_IMAGE;

        output = imageAssetSetFactory.make(IMAGE_ASSET_SET_DEFINITION);

        assertFalse(output.supportsMouseEventCapturing());

        SPRITE_1.Image = CAPTURING_IMAGE;

        output = imageAssetSetFactory.make(IMAGE_ASSET_SET_DEFINITION);

        assertTrue(output.supportsMouseEventCapturing());

        ANIMATION_1.SupportsMouseEventCapturing = false;

        output = imageAssetSetFactory.make(IMAGE_ASSET_SET_DEFINITION);

        assertFalse(output.supportsMouseEventCapturing());

    }

    @Test
    void testCreatedImageAssetSetGetInterfaceName() {
        var output = imageAssetSetFactory.make(IMAGE_ASSET_SET_DEFINITION);

        assertEquals(ImageAssetSet.class.getCanonicalName(), output.getInterfaceName());
    }
}
