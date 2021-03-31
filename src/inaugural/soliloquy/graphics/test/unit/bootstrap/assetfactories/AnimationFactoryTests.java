package inaugural.soliloquy.graphics.test.unit.bootstrap.assetfactories;

import inaugural.soliloquy.graphics.bootstrap.assetfactories.AnimationFactory;
import inaugural.soliloquy.graphics.test.fakes.FakeAnimationDefinition;
import inaugural.soliloquy.graphics.test.fakes.FakeAnimationFrameSnippet;
import inaugural.soliloquy.graphics.test.fakes.FakeImage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.assets.Animation;
import soliloquy.specs.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.AnimationDefinition;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AnimationFactoryTests {
    private String _image1RelativeLocation = "image1RelativeLocation";
    private int _image1Width = 111;
    private int _image1Height = 222;
    private FakeImage _image1 =
            new FakeImage(_image1RelativeLocation, _image1Width, _image1Height);

    private int _snippet1LeftX = 11;
    private int _snippet1TopY = 22;
    private int _snippet1RightX = 33;
    private int _snippet1BottomY = 44;
    private float _snippet1OffsetX = 0.11f;
    private float _snippet1OffsetY = 0.22f;
    private int _snippet1Ms = 0;

    private FakeAnimationFrameSnippet _animationFrameSnippetDefinition1 =
            new FakeAnimationFrameSnippet(_image1, _snippet1LeftX, _snippet1TopY,
                    _snippet1RightX, _snippet1BottomY, _snippet1OffsetX, _snippet1OffsetY);

    private String _image2RelativeLocation = "image2RelativeLocation";
    private int _image2Width = 333;
    private int _image2Height = 444;
    private FakeImage _image2 =
            new FakeImage(_image2RelativeLocation, _image2Width, _image2Height);

    private int _snippet2LeftX = 55;
    private int _snippet2TopY = 66;
    private int _snippet2RightX = 77;
    private int _snippet2BottomY = 88;
    private float _snippet2OffsetX = 0.33f;
    private float _snippet2OffsetY = 0.44f;
    private int _snippet2Ms = 100;

    private FakeAnimationFrameSnippet _animationFrameSnippetDefinition2 =
            new FakeAnimationFrameSnippet(_image2, _snippet2LeftX, _snippet2TopY,
                    _snippet2RightX, _snippet2BottomY, _snippet2OffsetX, _snippet2OffsetY);

    private String _image3RelativeLocation = "image3RelativeLocation";
    private int _image3Width = 555;
    private int _image3Height = 666;
    private FakeImage _image3 =
            new FakeImage(_image3RelativeLocation, _image3Width, _image3Height);

    private int _snippet3LeftX = 111;
    private int _snippet3TopY = 222;
    private int _snippet3RightX = 333;
    private int _snippet3BottomY = 444;
    private float _snippet3OffsetX = 0.55f;
    private float _snippet3OffsetY = 0.66f;
    private int _snippet3Ms = 300;

    private FakeAnimationFrameSnippet _animationFrameSnippetDefinition3 =
            new FakeAnimationFrameSnippet(_image3, _snippet3LeftX, _snippet3TopY,
                    _snippet3RightX, _snippet3BottomY, _snippet3OffsetX, _snippet3OffsetY);

    private String _id = "animationId";
    private int _animationDurationMs = 400;

    private AssetFactory<AnimationDefinition, Animation> _animationFactory;

    @BeforeEach
    void setUp() {
        _animationFactory = new AnimationFactory();
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(AssetFactory.class.getCanonicalName() + "<" +
                AnimationDefinition.class.getCanonicalName() + "," +
                Animation.class.getCanonicalName() + ">",
                _animationFactory.getInterfaceName());
    }

    @Test
    void testCreate() {
        Map<Integer, AnimationFrameSnippet> animationFrameSnippets = new HashMap<>();
        animationFrameSnippets.put(_snippet1Ms, _animationFrameSnippetDefinition1);
        animationFrameSnippets.put(_snippet2Ms, _animationFrameSnippetDefinition2);
        animationFrameSnippets.put(_snippet3Ms, _animationFrameSnippetDefinition3);

        Animation createdAnimation = _animationFactory.make(
                new FakeAnimationDefinition(_animationDurationMs, _id,
                        animationFrameSnippets));

        assertNotNull(createdAnimation);

        assertEquals(_id, createdAnimation.id());

        assertEquals(_animationDurationMs, createdAnimation.msDuration());

        assertEquals(Animation.class.getCanonicalName(), createdAnimation.getInterfaceName());

        assertThrows(IllegalArgumentException.class, () -> createdAnimation.snippetAtFrame(-1));
        assertThrows(IllegalArgumentException.class,
                () -> createdAnimation.snippetAtFrame(_animationDurationMs + 1));

        AnimationFrameSnippet frame1 = createdAnimation.snippetAtFrame(_snippet2Ms - 1);
        assertNotNull(frame1);
        assertSame(_animationFrameSnippetDefinition1, frame1);

        AnimationFrameSnippet frame2 = createdAnimation.snippetAtFrame(_snippet3Ms - 1);
        assertNotNull(frame2);
        assertSame(_animationFrameSnippetDefinition2, frame2);

        AnimationFrameSnippet frame3 = createdAnimation.snippetAtFrame(_animationDurationMs);
        assertNotNull(frame3);
        assertSame(_animationFrameSnippetDefinition3, frame3);
    }

    @Test
    void testCreateWithInvalidParams() {
        Map<Integer, AnimationFrameSnippet> animationFrameSnippets = new HashMap<>();
        animationFrameSnippets.put(_snippet1Ms, _animationFrameSnippetDefinition1);
        animationFrameSnippets.put(_snippet2Ms, _animationFrameSnippetDefinition2);
        animationFrameSnippets.put(_snippet3Ms, _animationFrameSnippetDefinition3);

        assertThrows(IllegalArgumentException.class, () -> _animationFactory.make(null));



        assertThrows(IllegalArgumentException.class,
                () -> _animationFactory.make(new FakeAnimationDefinition(0, _id,
                        animationFrameSnippets)));



        assertThrows(IllegalArgumentException.class,
                () -> _animationFactory.make(new FakeAnimationDefinition(_animationDurationMs,
                        null, animationFrameSnippets)));
        assertThrows(IllegalArgumentException.class,
                () -> _animationFactory.make(new FakeAnimationDefinition(_animationDurationMs,
                        "", animationFrameSnippets)));



        assertThrows(IllegalArgumentException.class,
                () -> _animationFactory.make(new FakeAnimationDefinition(_animationDurationMs,
                        _id, null)));
        assertThrows(IllegalArgumentException.class,
                () -> _animationFactory.make(new FakeAnimationDefinition(_animationDurationMs,
                        _id, new HashMap<>())));



        animationFrameSnippets.remove(_snippet1Ms);
        animationFrameSnippets.put(-1, _animationFrameSnippetDefinition1);
        assertThrows(IllegalArgumentException.class,
                () -> _animationFactory.make(new FakeAnimationDefinition(_animationDurationMs,
                        _id, animationFrameSnippets)));
        animationFrameSnippets.remove(-1);
        animationFrameSnippets.put(_snippet1Ms, _animationFrameSnippetDefinition1);

        animationFrameSnippets.remove(_snippet1Ms);
        animationFrameSnippets.put(_animationDurationMs, _animationFrameSnippetDefinition1);
        assertThrows(IllegalArgumentException.class,
                () -> _animationFactory.make(new FakeAnimationDefinition(_animationDurationMs,
                        _id, animationFrameSnippets)));
        animationFrameSnippets.remove(_animationDurationMs);
        animationFrameSnippets.put(_snippet1Ms, _animationFrameSnippetDefinition1);

        animationFrameSnippets.remove(_snippet1Ms);
        animationFrameSnippets.put(1, _animationFrameSnippetDefinition1);
        assertThrows(IllegalArgumentException.class,
                () -> _animationFactory.make(new FakeAnimationDefinition(_animationDurationMs,
                        _id, animationFrameSnippets)));
        animationFrameSnippets.remove(1);
        animationFrameSnippets.put(_snippet1Ms, _animationFrameSnippetDefinition1);



        _animationFrameSnippetDefinition1._image = null;
        assertThrows(IllegalArgumentException.class, () -> _animationFactory.make(
                new FakeAnimationDefinition(_animationDurationMs, _id, animationFrameSnippets)
        ));
        _animationFrameSnippetDefinition1._image = _image1;



        _image1._relativeLocation = null;
        assertThrows(IllegalArgumentException.class, () -> _animationFactory.make(
                new FakeAnimationDefinition(_animationDurationMs, _id, animationFrameSnippets)
        ));
        _image1._relativeLocation = _image1RelativeLocation;

        _image1._relativeLocation = "";
        assertThrows(IllegalArgumentException.class, () -> _animationFactory.make(
                new FakeAnimationDefinition(_animationDurationMs, _id, animationFrameSnippets)
        ));
        _image1._relativeLocation = _image1RelativeLocation;



        _image1._width = 0;
        assertThrows(IllegalArgumentException.class, () -> _animationFactory.make(
                new FakeAnimationDefinition(_animationDurationMs, _id, animationFrameSnippets)
        ));
        _image1._width = _image1Width;

        _image1._height = 0;
        assertThrows(IllegalArgumentException.class, () -> _animationFactory.make(
                new FakeAnimationDefinition(_animationDurationMs, _id, animationFrameSnippets)
        ));
        _image1._height = _image1Height;



        _animationFrameSnippetDefinition1._leftX = -1;
        assertThrows(IllegalArgumentException.class, () -> _animationFactory.make(
                new FakeAnimationDefinition(_animationDurationMs, _id, animationFrameSnippets)
        ));
        _animationFrameSnippetDefinition1._leftX = _snippet1LeftX;

        _animationFrameSnippetDefinition1._topY = -1;
        assertThrows(IllegalArgumentException.class, () -> _animationFactory.make(
                new FakeAnimationDefinition(_animationDurationMs, _id, animationFrameSnippets)
        ));
        _animationFrameSnippetDefinition1._topY = _snippet1TopY;

        _animationFrameSnippetDefinition1._rightX = -1;
        assertThrows(IllegalArgumentException.class, () -> _animationFactory.make(
                new FakeAnimationDefinition(_animationDurationMs, _id, animationFrameSnippets)
        ));
        _animationFrameSnippetDefinition1._rightX = _snippet1RightX;

        _animationFrameSnippetDefinition1._bottomY = -1;
        assertThrows(IllegalArgumentException.class, () -> _animationFactory.make(
                new FakeAnimationDefinition(_animationDurationMs, _id, animationFrameSnippets)
        ));
        _animationFrameSnippetDefinition1._bottomY = _snippet1BottomY;



        _animationFrameSnippetDefinition1._rightX = _snippet1LeftX;
        assertThrows(IllegalArgumentException.class, () -> _animationFactory.make(
                new FakeAnimationDefinition(_animationDurationMs, _id, animationFrameSnippets)
        ));
        _animationFrameSnippetDefinition1._rightX = _snippet1RightX;

        _animationFrameSnippetDefinition1._bottomY = _snippet1TopY;
        assertThrows(IllegalArgumentException.class, () -> _animationFactory.make(
                new FakeAnimationDefinition(_animationDurationMs, _id, animationFrameSnippets)
        ));
        _animationFrameSnippetDefinition1._bottomY = _snippet1BottomY;



        _animationFrameSnippetDefinition1._leftX = _image1Width + 1;
        assertThrows(IllegalArgumentException.class, () -> _animationFactory.make(
                new FakeAnimationDefinition(_animationDurationMs, _id, animationFrameSnippets)
        ));
        _animationFrameSnippetDefinition1._leftX = _snippet1LeftX;

        _animationFrameSnippetDefinition1._topY = _image1Height + 1;
        assertThrows(IllegalArgumentException.class, () -> _animationFactory.make(
                new FakeAnimationDefinition(_animationDurationMs, _id, animationFrameSnippets)
        ));
        _animationFrameSnippetDefinition1._topY = _snippet1TopY;

        _animationFrameSnippetDefinition1._rightX = _image1Width + 1;
        assertThrows(IllegalArgumentException.class, () -> _animationFactory.make(
                new FakeAnimationDefinition(_animationDurationMs, _id, animationFrameSnippets)
        ));
        _animationFrameSnippetDefinition1._rightX = _snippet1RightX;

        _animationFrameSnippetDefinition1._bottomY = _image1Height + 1;
        assertThrows(IllegalArgumentException.class, () -> _animationFactory.make(
                new FakeAnimationDefinition(_animationDurationMs, _id, animationFrameSnippets)
        ));
        _animationFrameSnippetDefinition1._bottomY = _snippet1BottomY;
    }
}
