package inaugural.soliloquy.graphics.test.unit.bootstrap.assetfactories;

import inaugural.soliloquy.graphics.bootstrap.assetfactories.AnimationFactory;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
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
            new FakeImage(_image1RelativeLocation, _image1Width, _image1Height, true);

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
            new FakeImage(_image2RelativeLocation, _image2Width, _image2Height, true);

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
            new FakeImage(_image3RelativeLocation, _image3Width, _image3Height, true);

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

        assertTrue(createdAnimation.supportsMouseEventCapturing());

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



        _image1.SupportsMouseEventCapturing = false;

        Animation createdAnimationNonCapturing = _animationFactory.make(
                new FakeAnimationDefinition(_animationDurationMs, _id, animationFrameSnippets));

        assertFalse(createdAnimationNonCapturing.supportsMouseEventCapturing());
    }

    @Test
    void testSnippetAtFrame() {
        int animationDurationMs = 2000;
        int snippet1Ms = 0;
        int snippet2Ms = 10;
        int snippet3Ms = 30;
        int snippet4Ms = 60;
        int snippet5Ms = 100;
        int snippet6Ms = 150;
        int snippet7Ms = 210;
        int snippet8Ms = 280;
        int snippet9Ms = 360;
        int snippet10Ms = 450;
        int snippet11Ms = 550;
        int snippet12Ms = 660;
        int snippet13Ms = 780;
        int snippet14Ms = 910;
        int snippet15Ms = 1050;
        int snippet16Ms = 1200;
        int snippet17Ms = 1360;
        FakeImage image1 = new FakeImage("image1", 1, 1);
        FakeImage image2 = new FakeImage("image2", 1, 1);
        FakeImage image3 = new FakeImage("image3", 1, 1);
        FakeImage image4 = new FakeImage("image4", 1, 1);
        FakeImage image5 = new FakeImage("image5", 1, 1);
        FakeImage image6 = new FakeImage("image6", 1, 1);
        FakeImage image7 = new FakeImage("image7", 1, 1);
        FakeImage image8 = new FakeImage("image8", 1, 1);
        FakeImage image9 = new FakeImage("image9", 1, 1);
        FakeImage image10 = new FakeImage("image10", 1, 1);
        FakeImage image11 = new FakeImage("image11", 1, 1);
        FakeImage image12 = new FakeImage("image12", 1, 1);
        FakeImage image13 = new FakeImage("image13", 1, 1);
        FakeImage image14 = new FakeImage("image14", 1, 1);
        FakeImage image15 = new FakeImage("image15", 1, 1);
        FakeImage image16 = new FakeImage("image16", 1, 1);
        FakeImage image17 = new FakeImage("image17", 1, 1);
        FakeAnimationFrameSnippet snippet1 = new FakeAnimationFrameSnippet(
                image1, 0, 0, 1, 1, 0f, 0f);
        FakeAnimationFrameSnippet snippet2 = new FakeAnimationFrameSnippet(
                image2, 0, 0, 1, 1, 0f, 0f);
        FakeAnimationFrameSnippet snippet3 = new FakeAnimationFrameSnippet(
                image3, 0, 0, 1, 1, 0f, 0f);
        FakeAnimationFrameSnippet snippet4 = new FakeAnimationFrameSnippet(
                image4, 0, 0, 1, 1, 0f, 0f);
        FakeAnimationFrameSnippet snippet5 = new FakeAnimationFrameSnippet(
                image5, 0, 0, 1, 1, 0f, 0f);
        FakeAnimationFrameSnippet snippet6 = new FakeAnimationFrameSnippet(
                image6, 0, 0, 1, 1, 0f, 0f);
        FakeAnimationFrameSnippet snippet7 = new FakeAnimationFrameSnippet(
                image7, 0, 0, 1, 1, 0f, 0f);
        FakeAnimationFrameSnippet snippet8 = new FakeAnimationFrameSnippet(
                image8, 0, 0, 1, 1, 0f, 0f);
        FakeAnimationFrameSnippet snippet9 = new FakeAnimationFrameSnippet(
                image9, 0, 0, 1, 1, 0f, 0f);
        FakeAnimationFrameSnippet snippet10 = new FakeAnimationFrameSnippet(
                image10, 0, 0, 1, 1, 0f, 0f);
        FakeAnimationFrameSnippet snippet11 = new FakeAnimationFrameSnippet(
                image11, 0, 0, 1, 1, 0f, 0f);
        FakeAnimationFrameSnippet snippet12 = new FakeAnimationFrameSnippet(
                image12, 0, 0, 1, 1, 0f, 0f);
        FakeAnimationFrameSnippet snippet13 = new FakeAnimationFrameSnippet(
                image13, 0, 0, 1, 1, 0f, 0f);
        FakeAnimationFrameSnippet snippet14 = new FakeAnimationFrameSnippet(
                image14, 0, 0, 1, 1, 0f, 0f);
        FakeAnimationFrameSnippet snippet15 = new FakeAnimationFrameSnippet(
                image15, 0, 0, 1, 1, 0f, 0f);
        FakeAnimationFrameSnippet snippet16 = new FakeAnimationFrameSnippet(
                image16, 0, 0, 1, 1, 0f, 0f);
        FakeAnimationFrameSnippet snippet17 = new FakeAnimationFrameSnippet(
                image17, 0, 0, 1, 1, 0f, 0f);

        Map<Integer, AnimationFrameSnippet> animationFrameSnippets = new HashMap<>();
        animationFrameSnippets.put(snippet1Ms, snippet1);
        animationFrameSnippets.put(snippet2Ms, snippet2);
        animationFrameSnippets.put(snippet3Ms, snippet3);
        animationFrameSnippets.put(snippet4Ms, snippet4);
        animationFrameSnippets.put(snippet5Ms, snippet5);
        animationFrameSnippets.put(snippet6Ms, snippet6);
        animationFrameSnippets.put(snippet7Ms, snippet7);
        animationFrameSnippets.put(snippet8Ms, snippet8);
        animationFrameSnippets.put(snippet9Ms, snippet9);
        animationFrameSnippets.put(snippet10Ms, snippet10);
        animationFrameSnippets.put(snippet11Ms, snippet11);
        animationFrameSnippets.put(snippet12Ms, snippet12);
        animationFrameSnippets.put(snippet13Ms, snippet13);
        animationFrameSnippets.put(snippet14Ms, snippet14);
        animationFrameSnippets.put(snippet15Ms, snippet15);
        animationFrameSnippets.put(snippet16Ms, snippet16);
        animationFrameSnippets.put(snippet17Ms, snippet17);


        Animation createdAnimation = _animationFactory.make(
                new FakeAnimationDefinition(animationDurationMs, _id,
                        animationFrameSnippets));

        for (int ms = 0; ms < animationDurationMs; ms++) {
            if (ms < snippet2Ms) {
                assertSame(snippet1, createdAnimation.snippetAtFrame(ms));
            }
            else if (ms < snippet3Ms) {
                assertSame(snippet2, createdAnimation.snippetAtFrame(ms));
            }
            else if (ms < snippet4Ms) {
                assertSame(snippet3, createdAnimation.snippetAtFrame(ms));
            }
            else if (ms < snippet5Ms) {
                assertSame(snippet4, createdAnimation.snippetAtFrame(ms));
            }
            else if (ms < snippet6Ms) {
                assertSame(snippet5, createdAnimation.snippetAtFrame(ms));
            }
            else if (ms < snippet7Ms) {
                assertSame(snippet6, createdAnimation.snippetAtFrame(ms));
            }
            else if (ms < snippet8Ms) {
                assertSame(snippet7, createdAnimation.snippetAtFrame(ms));
            }
            else if (ms < snippet9Ms) {
                assertSame(snippet8, createdAnimation.snippetAtFrame(ms));
            }
            else if (ms < snippet10Ms) {
                assertSame(snippet9, createdAnimation.snippetAtFrame(ms));
            }
            else if (ms < snippet11Ms) {
                assertSame(snippet10, createdAnimation.snippetAtFrame(ms));
            }
            else if (ms < snippet12Ms) {
                assertSame(snippet11, createdAnimation.snippetAtFrame(ms));
            }
            else if (ms < snippet13Ms) {
                assertSame(snippet12, createdAnimation.snippetAtFrame(ms));
            }
            else if (ms < snippet14Ms) {
                assertSame(snippet13, createdAnimation.snippetAtFrame(ms));
            }
            else if (ms < snippet15Ms) {
                assertSame(snippet14, createdAnimation.snippetAtFrame(ms));
            }
            else if (ms < snippet16Ms) {
                assertSame(snippet15, createdAnimation.snippetAtFrame(ms));
            }
            else if (ms < snippet17Ms) {
                assertSame(snippet16, createdAnimation.snippetAtFrame(ms));
            }
            else {
                assertSame(snippet17, createdAnimation.snippetAtFrame(ms));
            }
        }
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



        _animationFrameSnippetDefinition1.Image = null;
        assertThrows(IllegalArgumentException.class, () -> _animationFactory.make(
                new FakeAnimationDefinition(_animationDurationMs, _id, animationFrameSnippets)
        ));
        _animationFrameSnippetDefinition1.Image = _image1;



        _image1.RelativeLocation = null;
        assertThrows(IllegalArgumentException.class, () -> _animationFactory.make(
                new FakeAnimationDefinition(_animationDurationMs, _id, animationFrameSnippets)
        ));
        _image1.RelativeLocation = _image1RelativeLocation;

        _image1.RelativeLocation = "";
        assertThrows(IllegalArgumentException.class, () -> _animationFactory.make(
                new FakeAnimationDefinition(_animationDurationMs, _id, animationFrameSnippets)
        ));
        _image1.RelativeLocation = _image1RelativeLocation;



        _image1.Width = 0;
        assertThrows(IllegalArgumentException.class, () -> _animationFactory.make(
                new FakeAnimationDefinition(_animationDurationMs, _id, animationFrameSnippets)
        ));
        _image1.Width = _image1Width;

        _image1.Height = 0;
        assertThrows(IllegalArgumentException.class, () -> _animationFactory.make(
                new FakeAnimationDefinition(_animationDurationMs, _id, animationFrameSnippets)
        ));
        _image1.Height = _image1Height;



        _animationFrameSnippetDefinition1.LeftX = -1;
        assertThrows(IllegalArgumentException.class, () -> _animationFactory.make(
                new FakeAnimationDefinition(_animationDurationMs, _id, animationFrameSnippets)
        ));
        _animationFrameSnippetDefinition1.LeftX = _snippet1LeftX;

        _animationFrameSnippetDefinition1.TopY = -1;
        assertThrows(IllegalArgumentException.class, () -> _animationFactory.make(
                new FakeAnimationDefinition(_animationDurationMs, _id, animationFrameSnippets)
        ));
        _animationFrameSnippetDefinition1.TopY = _snippet1TopY;

        _animationFrameSnippetDefinition1.RightX = -1;
        assertThrows(IllegalArgumentException.class, () -> _animationFactory.make(
                new FakeAnimationDefinition(_animationDurationMs, _id, animationFrameSnippets)
        ));
        _animationFrameSnippetDefinition1.RightX = _snippet1RightX;

        _animationFrameSnippetDefinition1.BottomY = -1;
        assertThrows(IllegalArgumentException.class, () -> _animationFactory.make(
                new FakeAnimationDefinition(_animationDurationMs, _id, animationFrameSnippets)
        ));
        _animationFrameSnippetDefinition1.BottomY = _snippet1BottomY;



        _animationFrameSnippetDefinition1.RightX = _snippet1LeftX;
        assertThrows(IllegalArgumentException.class, () -> _animationFactory.make(
                new FakeAnimationDefinition(_animationDurationMs, _id, animationFrameSnippets)
        ));
        _animationFrameSnippetDefinition1.RightX = _snippet1RightX;

        _animationFrameSnippetDefinition1.BottomY = _snippet1TopY;
        assertThrows(IllegalArgumentException.class, () -> _animationFactory.make(
                new FakeAnimationDefinition(_animationDurationMs, _id, animationFrameSnippets)
        ));
        _animationFrameSnippetDefinition1.BottomY = _snippet1BottomY;



        _animationFrameSnippetDefinition1.LeftX = _image1Width + 1;
        assertThrows(IllegalArgumentException.class, () -> _animationFactory.make(
                new FakeAnimationDefinition(_animationDurationMs, _id, animationFrameSnippets)
        ));
        _animationFrameSnippetDefinition1.LeftX = _snippet1LeftX;

        _animationFrameSnippetDefinition1.TopY = _image1Height + 1;
        assertThrows(IllegalArgumentException.class, () -> _animationFactory.make(
                new FakeAnimationDefinition(_animationDurationMs, _id, animationFrameSnippets)
        ));
        _animationFrameSnippetDefinition1.TopY = _snippet1TopY;

        _animationFrameSnippetDefinition1.RightX = _image1Width + 1;
        assertThrows(IllegalArgumentException.class, () -> _animationFactory.make(
                new FakeAnimationDefinition(_animationDurationMs, _id, animationFrameSnippets)
        ));
        _animationFrameSnippetDefinition1.RightX = _snippet1RightX;

        _animationFrameSnippetDefinition1.BottomY = _image1Height + 1;
        assertThrows(IllegalArgumentException.class, () -> _animationFactory.make(
                new FakeAnimationDefinition(_animationDurationMs, _id, animationFrameSnippets)
        ));
        _animationFrameSnippetDefinition1.BottomY = _snippet1BottomY;
    }
}
