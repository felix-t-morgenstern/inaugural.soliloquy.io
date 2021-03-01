package inaugural.soliloquy.graphics.test.unit.bootstrap.assetfactories;

import inaugural.soliloquy.graphics.bootstrap.assetfactories.SpriteFactory;
import inaugural.soliloquy.graphics.test.fakes.FakeImage;
import inaugural.soliloquy.graphics.test.fakes.FakeSpriteDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.assets.Image;
import soliloquy.specs.graphics.assets.Sprite;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.SpriteDefinition;

import static org.junit.jupiter.api.Assertions.*;

class SpriteFactoryTests {
    private String _relativeLocation = "relativeLocation";
    private int _imageWidth = 123;
    private int _imageHeight = 456;
    private Image _image = new FakeImage(_relativeLocation, _imageWidth, _imageHeight);
    private int _leftX = 12;
    private int _topY = 34;
    private int _rightX = 56;
    private int _bottomY = 78;
    private String _assetId = "assetId";

    private AssetFactory<SpriteDefinition, Sprite> _spriteFactory;

    @BeforeEach
    void setUp() {
        _spriteFactory = new SpriteFactory();
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(AssetFactory.class.getCanonicalName() + "<" +
                SpriteDefinition.class.getCanonicalName() + "," + Sprite.class.getCanonicalName() +
                ">",
                _spriteFactory.getInterfaceName());
    }

    @Test
    void testCreate() {
        SpriteDefinition spriteDefinition = new FakeSpriteDefinition(_image, _leftX, _topY, _rightX,
                _bottomY, _assetId);

        Sprite createdSprite = _spriteFactory.make(spriteDefinition);

        assertNotNull(createdSprite);
        assertEquals(Sprite.class.getCanonicalName(), createdSprite.getInterfaceName());
        assertEquals(_assetId, createdSprite.id());
        assertSame(_image, createdSprite.image());
        assertEquals(_relativeLocation, createdSprite.image().relativeLocation());
        assertEquals(_leftX, createdSprite.leftX());
        assertEquals(_topY, createdSprite.topY());
        assertEquals(_rightX, createdSprite.rightX());
        assertEquals(_bottomY, createdSprite.bottomY());
    }

    // TODO: Consider breaking out into separate test cases
    @Test
    void testCreateWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _spriteFactory.make(null));

        assertThrows(IllegalArgumentException.class, () -> _spriteFactory.make(
                new FakeSpriteDefinition(null, _leftX, _topY, _rightX, _bottomY, _assetId)
        ));

        assertThrows(IllegalArgumentException.class, () -> _spriteFactory.make(
                new FakeSpriteDefinition(new FakeImage(null, _imageWidth, _imageHeight), _leftX,
                        _topY, _rightX, _bottomY, _assetId)
        ));
        assertThrows(IllegalArgumentException.class, () -> _spriteFactory.make(
                new FakeSpriteDefinition(new FakeImage("", _imageWidth, _imageHeight), _leftX,
                        _topY, _rightX, _bottomY, _assetId)
        ));

        assertThrows(IllegalArgumentException.class, () -> _spriteFactory.make(
                new FakeSpriteDefinition(new FakeImage(_relativeLocation, 0, _imageHeight), _leftX,
                        _topY, _rightX, _bottomY, _assetId)
        ));
        assertThrows(IllegalArgumentException.class, () -> _spriteFactory.make(
                new FakeSpriteDefinition(new FakeImage(_relativeLocation, _imageWidth, 0), _leftX,
                        _topY, _rightX, _bottomY, _assetId)
        ));

        assertThrows(IllegalArgumentException.class, () -> _spriteFactory.make(
                new FakeSpriteDefinition(_image, -1, _topY, _rightX, _bottomY, _assetId)
        ));
        assertThrows(IllegalArgumentException.class, () -> _spriteFactory.make(
                new FakeSpriteDefinition(_image, _leftX, -1, _rightX, _bottomY, _assetId)
        ));
        assertThrows(IllegalArgumentException.class, () -> _spriteFactory.make(
                new FakeSpriteDefinition(_image, _leftX, _topY, -1, _bottomY, _assetId)
        ));
        assertThrows(IllegalArgumentException.class, () -> _spriteFactory.make(
                new FakeSpriteDefinition(_image, _leftX, _topY, _rightX, -1, _assetId)
        ));

        assertThrows(IllegalArgumentException.class, () -> _spriteFactory.make(
                new FakeSpriteDefinition(_image, _leftX, _topY, _leftX, _bottomY, _assetId)
        ));
        assertThrows(IllegalArgumentException.class, () -> _spriteFactory.make(
                new FakeSpriteDefinition(_image, _leftX, _topY, _rightX, _topY, _assetId)
        ));

        assertThrows(IllegalArgumentException.class, () -> _spriteFactory.make(
                new FakeSpriteDefinition(_image, _imageWidth + 1, _topY, _rightX, _bottomY, _assetId)
        ));
        assertThrows(IllegalArgumentException.class, () -> _spriteFactory.make(
                new FakeSpriteDefinition(_image, _leftX, _imageHeight + 1, _rightX, _bottomY,
                        _assetId)
        ));
        assertThrows(IllegalArgumentException.class, () -> _spriteFactory.make(
                new FakeSpriteDefinition(_image, _leftX, _topY, _imageWidth + 1, _bottomY,
                        _assetId)
        ));
        assertThrows(IllegalArgumentException.class, () -> _spriteFactory.make(
                new FakeSpriteDefinition(_image, _leftX, _topY, _rightX, _imageHeight + 1,
                        _assetId)
        ));

        assertThrows(IllegalArgumentException.class, () -> _spriteFactory.make(
                new FakeSpriteDefinition(_image, _leftX, _topY, _rightX, _bottomY, null)
        ));
        assertThrows(IllegalArgumentException.class, () -> _spriteFactory.make(
                new FakeSpriteDefinition(_image, _leftX, _topY, _rightX, _bottomY, "")
        ));
    }
}
