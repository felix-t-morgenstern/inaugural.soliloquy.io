package inaugural.soliloquy.graphics.test.unit;

import inaugural.soliloquy.graphics.bootstrap.assetfactories.SpriteSetFactory;
import inaugural.soliloquy.graphics.test.fakes.FakeImage;
import inaugural.soliloquy.graphics.test.fakes.FakeSpriteSetDefinition;
import inaugural.soliloquy.graphics.test.fakes.FakeSpriteSetSnippetDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.assets.AssetSnippet;
import soliloquy.specs.graphics.assets.SpriteSet;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.SpriteSetDefinition;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.SpriteSetSnippetDefinition;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SpriteSetFactoryTests {
    private String _image1RelativeLocation = "image1RelativeLocation";
    private int _image1Width = 111;
    private int _image1Height = 222;
    private FakeImage _image1 =
            new FakeImage(_image1RelativeLocation, _image1Width, _image1Height);

    private int _snippet1LeftX = 11;
    private int _snippet1TopY = 22;
    private int _snippet1RightX = 33;
    private int _snippet1BottomY = 44;
    private String _snippet1Type = "snippet1Type";

    private FakeSpriteSetSnippetDefinition _spriteSetSnippetDefinition1 =
            new FakeSpriteSetSnippetDefinition(_image1, _snippet1LeftX, _snippet1TopY,
                    _snippet1RightX, _snippet1BottomY, _snippet1Type, null);

    private String _image2RelativeLocation = "image2RelativeLocation";
    private int _image2Width = 333;
    private int _image2Height = 444;
    private FakeImage _image2 =
            new FakeImage(_image2RelativeLocation, _image2Width, _image2Height);

    private int _snippet2LeftX = 55;
    private int _snippet2TopY = 66;
    private int _snippet2RightX = 77;
    private int _snippet2BottomY = 88;
    private String _snippet2Direction = "snippet2Direction";

    private FakeSpriteSetSnippetDefinition _spriteSetSnippetDefinition2 =
            new FakeSpriteSetSnippetDefinition(_image2, _snippet2LeftX, _snippet2TopY,
                    _snippet2RightX, _snippet2BottomY, null, _snippet2Direction);

    private String _image3RelativeLocation = "image3RelativeLocation";
    private int _image3Width = 555;
    private int _image3Height = 666;
    private FakeImage _image3 =
            new FakeImage(_image3RelativeLocation, _image3Width, _image3Height);

    private int _snippet3LeftX = 111;
    private int _snippet3TopY = 222;
    private int _snippet3RightX = 333;
    private int _snippet3BottomY = 444;

    private FakeSpriteSetSnippetDefinition _spriteSetSnippetDefinition3 =
            new FakeSpriteSetSnippetDefinition(_image3, _snippet3LeftX, _snippet3TopY,
                    _snippet3RightX, _snippet3BottomY, "", null);

    private String _assetId = "assetId";

    private AssetFactory<SpriteSetDefinition, SpriteSet> _spriteSetFactory;

    @BeforeEach
    void setUp() {
        _spriteSetFactory = new SpriteSetFactory();
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(AssetFactory.class.getCanonicalName() + "<" +
                SpriteSetDefinition.class.getCanonicalName() + "," +
                SpriteSet.class.getCanonicalName() + ">",
                _spriteSetFactory.getInterfaceName());
    }

    @Test
    void testCreate() {
        @SuppressWarnings({"unchecked", "rawtypes"}) SpriteSetDefinition spriteSetDefinition =
                new FakeSpriteSetDefinition(new ArrayList() {{
                    add(_spriteSetSnippetDefinition1);
                    add(_spriteSetSnippetDefinition2);
                    add(_spriteSetSnippetDefinition3);
                }}, _assetId);

        SpriteSet createdSpriteSet = _spriteSetFactory.create(spriteSetDefinition);

        assertNotNull(createdSpriteSet);

        assertEquals(_assetId, createdSpriteSet.id());

        assertEquals(SpriteSet.class.getCanonicalName(), createdSpriteSet.getInterfaceName());

        AssetSnippet snippet1 = createdSpriteSet
                .getImageAndBoundariesForTypeAndDirection(_snippet1Type, null);
        assertNotNull(snippet1);
        assertSame(_image1, snippet1.image());
        assertEquals(_snippet1LeftX, snippet1.leftX());
        assertEquals(_snippet1TopY, snippet1.topY());
        assertEquals(_snippet1RightX, snippet1.rightX());
        assertEquals(_snippet1BottomY, snippet1.bottomY());
        assertEquals(AssetSnippet.class.getCanonicalName(), snippet1.getInterfaceName());

        AssetSnippet snippet2 = createdSpriteSet
                .getImageAndBoundariesForTypeAndDirection("", _snippet2Direction);
        assertNotNull(snippet2);
        assertSame(_image2, snippet2.image());
        assertEquals(_snippet2LeftX, snippet2.leftX());
        assertEquals(_snippet2TopY, snippet2.topY());
        assertEquals(_snippet2RightX, snippet2.rightX());
        assertEquals(_snippet2BottomY, snippet2.bottomY());
        assertEquals(AssetSnippet.class.getCanonicalName(), snippet2.getInterfaceName());

        AssetSnippet snippet3 = createdSpriteSet
                .getImageAndBoundariesForTypeAndDirection(null, "");
        assertNotNull(snippet3);
        assertSame(_image3, snippet3.image());
        assertEquals(_snippet3LeftX, snippet3.leftX());
        assertEquals(_snippet3TopY, snippet3.topY());
        assertEquals(_snippet3RightX, snippet3.rightX());
        assertEquals(_snippet3BottomY, snippet3.bottomY());
        assertEquals(AssetSnippet.class.getCanonicalName(), snippet3.getInterfaceName());
    }

    // TODO: Consider breaking out into separate test cases
    @Test
    void testCreateWithInvalidParams() {
        @SuppressWarnings({"unchecked", "rawtypes"})
        List<SpriteSetSnippetDefinition> spriteSetSnippetDefinitions = new ArrayList() {{
            add(_spriteSetSnippetDefinition1);
            add(_spriteSetSnippetDefinition2);
            add(_spriteSetSnippetDefinition3);
        }};

        assertThrows(IllegalArgumentException.class, () -> _spriteSetFactory.create(null));

        assertThrows(IllegalArgumentException.class, () -> _spriteSetFactory.create(
                new FakeSpriteSetDefinition(null, _assetId)
        ));
        assertThrows(IllegalArgumentException.class, () -> _spriteSetFactory.create(
                new FakeSpriteSetDefinition(new ArrayList<>(), _assetId)
        ));

        assertThrows(IllegalArgumentException.class, () -> _spriteSetFactory.create(
                new FakeSpriteSetDefinition(spriteSetSnippetDefinitions, null)
        ));
        assertThrows(IllegalArgumentException.class, () -> _spriteSetFactory.create(
                new FakeSpriteSetDefinition(spriteSetSnippetDefinitions, "")
        ));



        _spriteSetSnippetDefinition1._image = null;
        assertThrows(IllegalArgumentException.class, () -> _spriteSetFactory.create(
                new FakeSpriteSetDefinition(spriteSetSnippetDefinitions, _assetId)
        ));
        _spriteSetSnippetDefinition1._image = _image1;



        _image1._relativeLocation = null;
        assertThrows(IllegalArgumentException.class, () -> _spriteSetFactory.create(
                new FakeSpriteSetDefinition(spriteSetSnippetDefinitions, _assetId)
        ));
        _image1._relativeLocation = _image1RelativeLocation;

        _image1._relativeLocation = "";
        assertThrows(IllegalArgumentException.class, () -> _spriteSetFactory.create(
                new FakeSpriteSetDefinition(spriteSetSnippetDefinitions, _assetId)
        ));
        _image1._relativeLocation = _image1RelativeLocation;



        _image1._width = 0;
        assertThrows(IllegalArgumentException.class, () -> _spriteSetFactory.create(
                new FakeSpriteSetDefinition(spriteSetSnippetDefinitions, _assetId)
        ));
        _image1._width = _image1Width;

        _image1._height = 0;
        assertThrows(IllegalArgumentException.class, () -> _spriteSetFactory.create(
                new FakeSpriteSetDefinition(spriteSetSnippetDefinitions, _assetId)
        ));
        _image1._height = _image1Height;



        _spriteSetSnippetDefinition1._type = null;
        _spriteSetSnippetDefinition1._direction = _snippet2Direction;
        assertThrows(IllegalArgumentException.class, () -> _spriteSetFactory.create(
                new FakeSpriteSetDefinition(spriteSetSnippetDefinitions, _assetId)
        ));
        _spriteSetSnippetDefinition1._type = _snippet1Type;
        _spriteSetSnippetDefinition1._direction = null;



        _spriteSetSnippetDefinition1._leftX = -1;
        assertThrows(IllegalArgumentException.class, () -> _spriteSetFactory.create(
                new FakeSpriteSetDefinition(spriteSetSnippetDefinitions, _assetId)
        ));
        _spriteSetSnippetDefinition1._leftX = _snippet1LeftX;

        _spriteSetSnippetDefinition1._topY = -1;
        assertThrows(IllegalArgumentException.class, () -> _spriteSetFactory.create(
                new FakeSpriteSetDefinition(spriteSetSnippetDefinitions, _assetId)
        ));
        _spriteSetSnippetDefinition1._topY = _snippet1TopY;

        _spriteSetSnippetDefinition1._rightX = -1;
        assertThrows(IllegalArgumentException.class, () -> _spriteSetFactory.create(
                new FakeSpriteSetDefinition(spriteSetSnippetDefinitions, _assetId)
        ));
        _spriteSetSnippetDefinition1._rightX = _snippet1RightX;

        _spriteSetSnippetDefinition1._bottomY = -1;
        assertThrows(IllegalArgumentException.class, () -> _spriteSetFactory.create(
                new FakeSpriteSetDefinition(spriteSetSnippetDefinitions, _assetId)
        ));
        _spriteSetSnippetDefinition1._bottomY = _snippet1BottomY;



        _spriteSetSnippetDefinition1._rightX = _snippet1LeftX;
        assertThrows(IllegalArgumentException.class, () -> _spriteSetFactory.create(
                new FakeSpriteSetDefinition(spriteSetSnippetDefinitions, _assetId)
        ));
        _spriteSetSnippetDefinition1._rightX = _snippet1RightX;

        _spriteSetSnippetDefinition1._bottomY = _snippet1TopY;
        assertThrows(IllegalArgumentException.class, () -> _spriteSetFactory.create(
                new FakeSpriteSetDefinition(spriteSetSnippetDefinitions, _assetId)
        ));
        _spriteSetSnippetDefinition1._bottomY = _snippet1BottomY;



        _spriteSetSnippetDefinition1._leftX = _image1Width + 1;
        assertThrows(IllegalArgumentException.class, () -> _spriteSetFactory.create(
                new FakeSpriteSetDefinition(spriteSetSnippetDefinitions, _assetId)
        ));
        _spriteSetSnippetDefinition1._leftX = _snippet1LeftX;

        _spriteSetSnippetDefinition1._topY = _image1Height + 1;
        assertThrows(IllegalArgumentException.class, () -> _spriteSetFactory.create(
                new FakeSpriteSetDefinition(spriteSetSnippetDefinitions, _assetId)
        ));
        _spriteSetSnippetDefinition1._topY = _snippet1TopY;

        _spriteSetSnippetDefinition1._rightX = _image1Width + 1;
        assertThrows(IllegalArgumentException.class, () -> _spriteSetFactory.create(
                new FakeSpriteSetDefinition(spriteSetSnippetDefinitions, _assetId)
        ));
        _spriteSetSnippetDefinition1._rightX = _snippet1RightX;

        _spriteSetSnippetDefinition1._bottomY = _image1Height + 1;
        assertThrows(IllegalArgumentException.class, () -> _spriteSetFactory.create(
                new FakeSpriteSetDefinition(spriteSetSnippetDefinitions, _assetId)
        ));
        _spriteSetSnippetDefinition1._bottomY = _snippet1BottomY;
    }

    @Test
    void testGetImageAndBoundariesForTypeAndDirection() {
        @SuppressWarnings({"unchecked", "rawtypes"})
        List<SpriteSetSnippetDefinition> spriteSetSnippetDefinitions = new ArrayList() {{
            add(_spriteSetSnippetDefinition1);
            add(_spriteSetSnippetDefinition2);
            add(_spriteSetSnippetDefinition3);
        }};

        SpriteSet spriteSet = _spriteSetFactory.create(
                new FakeSpriteSetDefinition(spriteSetSnippetDefinitions, _assetId));

        AssetSnippet snippet1 =
                spriteSet.getImageAndBoundariesForTypeAndDirection(_snippet1Type, null);
        AssetSnippet snippet1FromEmpty =
                spriteSet.getImageAndBoundariesForTypeAndDirection(_snippet1Type, "");

        assertSame(snippet1, snippet1FromEmpty);

        assertSame(_image1, snippet1.image());
        assertEquals(_snippet1LeftX, snippet1.leftX());
        assertEquals(_snippet1TopY, snippet1.topY());
        assertEquals(_snippet1RightX, snippet1.rightX());
        assertEquals(_snippet1BottomY, snippet1.bottomY());

        AssetSnippet snippet2 =
                spriteSet.getImageAndBoundariesForTypeAndDirection(null, _snippet2Direction);

        assertSame(_image2, snippet2.image());
        assertEquals(_snippet2LeftX, snippet2.leftX());
        assertEquals(_snippet2TopY, snippet2.topY());
        assertEquals(_snippet2RightX, snippet2.rightX());
        assertEquals(_snippet2BottomY, snippet2.bottomY());

        AssetSnippet snippet3 =
                spriteSet.getImageAndBoundariesForTypeAndDirection("", null);
        AssetSnippet snippet3FromReversedParams =
                spriteSet.getImageAndBoundariesForTypeAndDirection(null, "");

        assertSame(snippet3, snippet3FromReversedParams);

        assertSame(_image3, snippet3.image());
        assertEquals(_snippet3LeftX, snippet3.leftX());
        assertEquals(_snippet3TopY, snippet3.topY());
        assertEquals(_snippet3RightX, snippet3.rightX());
        assertEquals(_snippet3BottomY, snippet3.bottomY());
    }

    @Test
    void testId() {
        @SuppressWarnings({"unchecked", "rawtypes"})
        List<SpriteSetSnippetDefinition> spriteSetSnippetDefinitions = new ArrayList() {{
            add(_spriteSetSnippetDefinition1);
            add(_spriteSetSnippetDefinition2);
            add(_spriteSetSnippetDefinition3);
        }};

        SpriteSet spriteSet = _spriteSetFactory.create(
                new FakeSpriteSetDefinition(spriteSetSnippetDefinitions, _assetId));

        assertEquals(_assetId, spriteSet.id());
    }
}
