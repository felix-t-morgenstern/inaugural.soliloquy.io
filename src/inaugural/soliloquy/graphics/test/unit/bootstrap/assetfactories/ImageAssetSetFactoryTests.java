package inaugural.soliloquy.graphics.test.unit.bootstrap.assetfactories;

import inaugural.soliloquy.graphics.bootstrap.assetfactories.ImageAssetSetFactory;
import inaugural.soliloquy.graphics.test.fakes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.assets.Animation;
import soliloquy.specs.graphics.assets.ImageAsset;
import soliloquy.specs.graphics.assets.ImageAssetSet;
import soliloquy.specs.graphics.assets.Sprite;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageAssetSetAssetDefinition;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageAssetSetDefinition;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ImageAssetSetFactoryTests {
    private final String IMAGE_ASSET_SET_ID = "imageAssetSetId";

    private final String SPRITE_1_ID = "sprite1Id";
    private final FakeSprite SPRITE_1 = new FakeSprite(SPRITE_1_ID);

    private final String SPRITE_2_ID = "sprite2Id";
    private final FakeSprite SPRITE_2 = new FakeSprite(SPRITE_2_ID);

    private final String ANIMATION_1_ID = "animation1Id";
    private final FakeAnimation ANIMATION_1 = new FakeAnimation(ANIMATION_1_ID);

    private final String ANIMATION_2_ID = "animation2Id";
    private final FakeAnimation ANIMATION_2 = new FakeAnimation(ANIMATION_2_ID);

    private final FakeRegistry<Sprite> SPRITES_REGISTRY = new FakeRegistry<>();
    private final FakeRegistry<Animation> ANIMATIONS_REGISTRY = new FakeRegistry<>();

    private final String TYPE1 = "type1";
    private final String TYPE2 = "type2";
    private final String DIRECTION1 = "direction1";
    private final String DIRECTION2 = "direction2";

    private final FakeImageAssetSetAssetDefinition ASSET_1_DEFINITION =
            new FakeImageAssetSetAssetDefinition(TYPE1, "",
                    ImageAsset.ImageAssetType.SPRITE, SPRITE_1_ID);
    private final FakeImageAssetSetAssetDefinition ASSET_2_DEFINITION =
            new FakeImageAssetSetAssetDefinition("", DIRECTION1,
                    ImageAsset.ImageAssetType.SPRITE, SPRITE_2_ID);
    private final FakeImageAssetSetAssetDefinition ASSET_3_DEFINITION =
            new FakeImageAssetSetAssetDefinition(TYPE2, "",
                    ImageAsset.ImageAssetType.ANIMATION, ANIMATION_1_ID);
    private final FakeImageAssetSetAssetDefinition ASSET_4_DEFINITION =
            new FakeImageAssetSetAssetDefinition("", DIRECTION2,
                    ImageAsset.ImageAssetType.ANIMATION, ANIMATION_2_ID);

    private final List<ImageAssetSetAssetDefinition> IMAGE_ASSET_SET_ASSET_DEFINITIONS =
            new ArrayList<ImageAssetSetAssetDefinition>() {{
                add(ASSET_1_DEFINITION);
                add(ASSET_2_DEFINITION);
                add(ASSET_3_DEFINITION);
                add(ASSET_4_DEFINITION);
            }};
    private final FakeImageAssetSetDefinition IMAGE_ASSET_SET_DEFINITION =
            new FakeImageAssetSetDefinition(IMAGE_ASSET_SET_ASSET_DEFINITIONS, IMAGE_ASSET_SET_ID);

    private AssetFactory<ImageAssetSetDefinition, ImageAssetSet> _imageAssetSetFactory;

    @BeforeEach
    void setUp() {
        SPRITES_REGISTRY.add(SPRITE_1);
        SPRITES_REGISTRY.add(SPRITE_2);

        ANIMATIONS_REGISTRY.add(ANIMATION_1);
        ANIMATIONS_REGISTRY.add(ANIMATION_2);

        _imageAssetSetFactory = new ImageAssetSetFactory(SPRITES_REGISTRY, ANIMATIONS_REGISTRY);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetFactory(null, ANIMATIONS_REGISTRY));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetFactory(SPRITES_REGISTRY, null));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(AssetFactory.class.getCanonicalName() + "<" +
                ImageAssetSetDefinition.class.getCanonicalName() + "," +
                ImageAssetSet.class.getCanonicalName() + ">",
                _imageAssetSetFactory.getInterfaceName());
    }

    // TODO: Consider breaking out into separate test cases
    @Test
    void testCreateWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetFactory.make(null));

        IMAGE_ASSET_SET_DEFINITION.Id = null;
        assertThrows(IllegalArgumentException.class,
                () -> _imageAssetSetFactory.make(IMAGE_ASSET_SET_DEFINITION));

        IMAGE_ASSET_SET_DEFINITION.Id = "";
        assertThrows(IllegalArgumentException.class,
                () -> _imageAssetSetFactory.make(IMAGE_ASSET_SET_DEFINITION));

        IMAGE_ASSET_SET_DEFINITION.Id = IMAGE_ASSET_SET_ID;

        IMAGE_ASSET_SET_DEFINITION.ImageAssetSetAssetDefinitions = null;
        assertThrows(IllegalArgumentException.class,
                () -> _imageAssetSetFactory.make(IMAGE_ASSET_SET_DEFINITION));

        IMAGE_ASSET_SET_DEFINITION.ImageAssetSetAssetDefinitions = new ArrayList<>();
        assertThrows(IllegalArgumentException.class,
                () -> _imageAssetSetFactory.make(IMAGE_ASSET_SET_DEFINITION));

        IMAGE_ASSET_SET_DEFINITION.ImageAssetSetAssetDefinitions = IMAGE_ASSET_SET_ASSET_DEFINITIONS;

        ASSET_1_DEFINITION.AssetType = ImageAsset.ImageAssetType.UNKNOWN;
        assertThrows(IllegalArgumentException.class,
                () -> _imageAssetSetFactory.make(IMAGE_ASSET_SET_DEFINITION));

        ASSET_1_DEFINITION.AssetType = ImageAsset.ImageAssetType.SPRITE;

        ASSET_1_DEFINITION.AssetId = null;
        assertThrows(IllegalArgumentException.class,
                () -> _imageAssetSetFactory.make(IMAGE_ASSET_SET_DEFINITION));

        ASSET_1_DEFINITION.AssetId = "";
        assertThrows(IllegalArgumentException.class,
                () -> _imageAssetSetFactory.make(IMAGE_ASSET_SET_DEFINITION));

        ASSET_1_DEFINITION.AssetId = "InvalidSpriteId";
        assertThrows(IllegalArgumentException.class,
                () -> _imageAssetSetFactory.make(IMAGE_ASSET_SET_DEFINITION));

        ASSET_1_DEFINITION.AssetId = SPRITE_1_ID;

        ASSET_3_DEFINITION.AssetId = "InvalidAnimationId";
        assertThrows(IllegalArgumentException.class,
                () -> _imageAssetSetFactory.make(IMAGE_ASSET_SET_DEFINITION));

        ASSET_3_DEFINITION.AssetId = ANIMATION_1_ID;

        ASSET_3_DEFINITION.Type = TYPE1;
        assertThrows(IllegalArgumentException.class,
                () -> _imageAssetSetFactory.make(IMAGE_ASSET_SET_DEFINITION));

        ASSET_3_DEFINITION.Type = TYPE2;
    }

    @Test
    void testCreatedSpriteSetId() {
        ImageAssetSet imageAssetSet = _imageAssetSetFactory.make(IMAGE_ASSET_SET_DEFINITION);

        assertEquals(IMAGE_ASSET_SET_ID, imageAssetSet.id());
    }

    @Test
    void testCreatedSpriteSetGetImageAssetForTypeAndDirection() {
        ImageAssetSet imageAssetSet = _imageAssetSetFactory.make(IMAGE_ASSET_SET_DEFINITION);

        // NB: The invocations here use null, whereas the asset definitions fed to the factory use
        //     empty strings; this is to test that the factory and SpriteSet treat null and empty
        //     strings interchangeably
        assertSame(SPRITE_1, imageAssetSet.getImageAssetForTypeAndDirection(TYPE1, null));
        assertSame(SPRITE_2, imageAssetSet.getImageAssetForTypeAndDirection(null, DIRECTION1));
        assertSame(ANIMATION_1, imageAssetSet.getImageAssetForTypeAndDirection(TYPE2, null));
        assertSame(ANIMATION_2, imageAssetSet.getImageAssetForTypeAndDirection(null, DIRECTION2));
    }

    @Test
    void testCreatedSpriteSetGetInterfaceName() {
        ImageAssetSet imageAssetSet = _imageAssetSetFactory.make(IMAGE_ASSET_SET_DEFINITION);

        assertEquals(ImageAssetSet.class.getCanonicalName(), imageAssetSet.getInterfaceName());
    }
}
