package inaugural.soliloquy.graphics.test.unit.bootstrap.assetfactories;

import inaugural.soliloquy.graphics.bootstrap.assetfactories.SpriteFactory;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
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
    private String _id = "spriteId";

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
        SpriteDefinition spriteDefinition = new SpriteDefinition(_id, _image, _leftX, _topY, 
                _rightX, _bottomY);

        Sprite createdSprite = _spriteFactory.make(spriteDefinition);

        assertNotNull(createdSprite);
        assertEquals(Sprite.class.getCanonicalName(), createdSprite.getInterfaceName());
        assertEquals(_id, createdSprite.id());
        assertSame(_image, createdSprite.image());
        assertEquals(_relativeLocation, createdSprite.image().relativeLocation());
        assertEquals(_leftX, createdSprite.leftX());
        assertEquals(_topY, createdSprite.topY());
        assertEquals(_rightX, createdSprite.rightX());
        assertEquals(_bottomY, createdSprite.bottomY());
    }

    @Test
    void testCreateWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _spriteFactory.make(null));

        assertThrows(IllegalArgumentException.class, () -> _spriteFactory.make(
                new SpriteDefinition(null, _image, _leftX, _topY, _rightX, _bottomY)
        ));
        assertThrows(IllegalArgumentException.class, () -> _spriteFactory.make(
                new SpriteDefinition("", _image, _leftX, _topY, _rightX, _bottomY)
        ));

        assertThrows(IllegalArgumentException.class, () -> _spriteFactory.make(
                new SpriteDefinition(_id, null, _leftX, _topY, _rightX, _bottomY)
        ));

        assertThrows(IllegalArgumentException.class, () -> _spriteFactory.make(
                new SpriteDefinition(_id, new FakeImage(null, _imageWidth, _imageHeight), _leftX,
                        _topY, _rightX, _bottomY)
        ));
        assertThrows(IllegalArgumentException.class, () -> _spriteFactory.make(
                new SpriteDefinition(_id, new FakeImage("", _imageWidth, _imageHeight), _leftX,
                        _topY, _rightX, _bottomY)
        ));

        assertThrows(IllegalArgumentException.class, () -> _spriteFactory.make(
                new SpriteDefinition(_id, new FakeImage(_relativeLocation, 0, _imageHeight), 
                        _leftX, _topY, _rightX, _bottomY)
        ));
        assertThrows(IllegalArgumentException.class, () -> _spriteFactory.make(
                new SpriteDefinition(_id, new FakeImage(_relativeLocation, _imageWidth, 0), _leftX,
                        _topY, _rightX, _bottomY)
        ));

        assertThrows(IllegalArgumentException.class, () -> _spriteFactory.make(
                new SpriteDefinition(_id, _image, -1, _topY, _rightX, _bottomY)
        ));
        assertThrows(IllegalArgumentException.class, () -> _spriteFactory.make(
                new SpriteDefinition(_id, _image, _leftX, -1, _rightX, _bottomY)
        ));
        assertThrows(IllegalArgumentException.class, () -> _spriteFactory.make(
                new SpriteDefinition(_id, _image, _leftX, _topY, -1, _bottomY)
        ));
        assertThrows(IllegalArgumentException.class, () -> _spriteFactory.make(
                new SpriteDefinition(_id, _image, _leftX, _topY, _rightX, -1)
        ));

        assertThrows(IllegalArgumentException.class, () -> _spriteFactory.make(
                new SpriteDefinition(_id, _image, _leftX, _topY, _leftX, _bottomY)
        ));
        assertThrows(IllegalArgumentException.class, () -> _spriteFactory.make(
                new SpriteDefinition(_id, _image, _leftX, _topY, _rightX, _topY)
        ));

        assertThrows(IllegalArgumentException.class, () -> _spriteFactory.make(
                new SpriteDefinition(_id, _image, _imageWidth + 1, _topY, _rightX, _bottomY)
        ));
        assertThrows(IllegalArgumentException.class, () -> _spriteFactory.make(
                new SpriteDefinition(_id, _image, _leftX, _imageHeight + 1, _rightX, _bottomY)
        ));
        assertThrows(IllegalArgumentException.class, () -> _spriteFactory.make(
                new SpriteDefinition(_id, _image, _leftX, _topY, _imageWidth + 1, _bottomY)
        ));
        assertThrows(IllegalArgumentException.class, () -> _spriteFactory.make(
                new SpriteDefinition(_id, _image, _leftX, _topY, _rightX, _imageHeight + 1)
        ));
    }
}
