package inaugural.soliloquy.graphics.test.unit.bootstrap.assetfactories;

import inaugural.soliloquy.graphics.bootstrap.assetfactories.AnimationFactory;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeAnimationFrameSnippet;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeImage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.assets.Animation;
import soliloquy.specs.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.AnimationDefinition;

import java.util.Map;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static org.junit.jupiter.api.Assertions.*;

public class AnimationFactoryTests {
    private final String IMAGE_1_RELATIVE_LOC = "image1RelativeLocation";
    private final int IMAGE_1_WIDTH = 111;
    private final int IMAGE_1_HEIGHT = 222;
    private final FakeImage IMAGE_1 =
            new FakeImage(IMAGE_1_RELATIVE_LOC, IMAGE_1_WIDTH, IMAGE_1_HEIGHT, true);

    private final int SNIPPET_1_LEFT_X = 11;
    private final int SNIPPET_1_TOP_Y = 22;
    private final int SNIPPET_1_RIGHT_X = 33;
    private final int SNIPPET_1_BottomY = 44;
    private final float SNIPPET_1_OFFSET_X = 0.11f;
    private final float SNIPPET_1_OFFSET_Y = 0.22f;
    private final int SNIPPET_1_MS = 0;

    private final FakeAnimationFrameSnippet animationFrameSnippetDefinition1 =
            new FakeAnimationFrameSnippet(IMAGE_1, SNIPPET_1_LEFT_X, SNIPPET_1_TOP_Y,
                    SNIPPET_1_RIGHT_X, SNIPPET_1_BottomY, SNIPPET_1_OFFSET_X, SNIPPET_1_OFFSET_Y);

    private final String IMAGE_2_RELATIVE_LOC = "image2RelativeLocation";
    private final int IMAGE_2_WIDTH = 333;
    private final int IMAGE_2_HEIGHT = 444;
    private final FakeImage IMAGE_2 =
            new FakeImage(IMAGE_2_RELATIVE_LOC, IMAGE_2_WIDTH, IMAGE_2_HEIGHT, true);

    private final int SNIPPET_2_LEFT_X = 55;
    private final int SNIPPET_2_TOP_Y = 66;
    private final int SNIPPET_2_RIGHT_X = 77;
    private final int SNIPPET_2_BottomY = 88;
    private final float SNIPPET_2_OFFSET_X = 0.33f;
    private final float SNIPPET_2_OFFSET_Y = 0.44f;
    private final int SNIPPET_2_MS = 100;

    private final FakeAnimationFrameSnippet animationFrameSnippetDefinition2 =
            new FakeAnimationFrameSnippet(IMAGE_2, SNIPPET_2_LEFT_X, SNIPPET_2_TOP_Y,
                    SNIPPET_2_RIGHT_X, SNIPPET_2_BottomY, SNIPPET_2_OFFSET_X, SNIPPET_2_OFFSET_Y);

    private final String IMAGE_3_RELATIVE_LOC = "image3RelativeLocation";
    private final int IMAGE_3_WIDTH = 555;
    private final int IMAGE_3_HEIGHT = 666;
    private final FakeImage IMAGE_3 =
            new FakeImage(IMAGE_3_RELATIVE_LOC, IMAGE_3_WIDTH, IMAGE_3_HEIGHT, true);

    private final int SNIPPET_3_LEFT_X = 111;
    private final int SNIPPET_3_TOP_Y = 222;
    private final int SNIPPET_3_RIGHT_X = 333;
    private final int SNIPPET_3_BottomY = 444;
    private final float SNIPPET_3_OFFSET_X = 0.55f;
    private final float SNIPPET_3_OFFSET_Y = 0.66f;
    private final int SNIPPET_3_MS = 300;

    private final FakeAnimationFrameSnippet animationFrameSnippetDefinition3 =
            new FakeAnimationFrameSnippet(IMAGE_3, SNIPPET_3_LEFT_X, SNIPPET_3_TOP_Y,
                    SNIPPET_3_RIGHT_X, SNIPPET_3_BottomY, SNIPPET_3_OFFSET_X, SNIPPET_3_OFFSET_Y);

    private final String id = "animationId";
    private final int animationDurationMS = 400;

    private AssetFactory<AnimationDefinition, Animation> animationFactory;

    @BeforeEach
    public void setUp() {
        animationFactory = new AnimationFactory();
    }

    @Test
    public void testCreate() {
        Map<Integer, AnimationFrameSnippet> animationFrameSnippets = mapOf();
        animationFrameSnippets.put(SNIPPET_1_MS, animationFrameSnippetDefinition1);
        animationFrameSnippets.put(SNIPPET_2_MS, animationFrameSnippetDefinition2);
        animationFrameSnippets.put(SNIPPET_3_MS, animationFrameSnippetDefinition3);

        Animation createdAnimation = animationFactory.make(
                new AnimationDefinition(id, animationDurationMS, animationFrameSnippets));

        assertNotNull(createdAnimation);

        assertEquals(id, createdAnimation.id());

        assertEquals(animationDurationMS, createdAnimation.msDuration());

        assertTrue(createdAnimation.supportsMouseEventCapturing());

        assertThrows(IllegalArgumentException.class, () -> createdAnimation.snippetAtFrame(-1));
        assertThrows(IllegalArgumentException.class,
                () -> createdAnimation.snippetAtFrame(animationDurationMS + 1));

        AnimationFrameSnippet frame1 = createdAnimation.snippetAtFrame(SNIPPET_2_MS - 1);
        assertNotNull(frame1);
        assertSame(animationFrameSnippetDefinition1, frame1);

        AnimationFrameSnippet frame2 = createdAnimation.snippetAtFrame(SNIPPET_3_MS - 1);
        assertNotNull(frame2);
        assertSame(animationFrameSnippetDefinition2, frame2);

        AnimationFrameSnippet frame3 = createdAnimation.snippetAtFrame(animationDurationMS);
        assertNotNull(frame3);
        assertSame(animationFrameSnippetDefinition3, frame3);



        IMAGE_1.SupportsMouseEventCapturing = false;

        Animation createdAnimationNonCapturing = animationFactory.make(
                new AnimationDefinition(id, animationDurationMS, animationFrameSnippets));

        assertFalse(createdAnimationNonCapturing.supportsMouseEventCapturing());
    }

    @Test
    public void testSnippetAtFrame() {
        int animationDurationMS = 2000;
        int SNIPPET_1_MS = 0;
        int SNIPPET_2_MS = 10;
        int SNIPPET_3_MS = 30;
        int snippet4MS = 60;
        int snippet5MS = 100;
        int snippet6MS = 150;
        int snippet7MS = 210;
        int snippet8MS = 280;
        int snippet9MS = 360;
        int SNIPPET_1_0MS = 450;
        int SNIPPET_1_1MS = 550;
        int SNIPPET_1_2MS = 660;
        int SNIPPET_1_3MS = 780;
        int SNIPPET_1_4MS = 910;
        int SNIPPET_1_5MS = 1050;
        int SNIPPET_1_6MS = 1200;
        int SNIPPET_1_7MS = 1360;
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
        FakeAnimationFrameSnippet SNIPPET_1_ = new FakeAnimationFrameSnippet(
                image1, 0, 0, 1, 1, 0f, 0f);
        FakeAnimationFrameSnippet SNIPPET_2_ = new FakeAnimationFrameSnippet(
                image2, 0, 0, 1, 1, 0f, 0f);
        FakeAnimationFrameSnippet SNIPPET_3_ = new FakeAnimationFrameSnippet(
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
        FakeAnimationFrameSnippet SNIPPET_1_0 = new FakeAnimationFrameSnippet(
                image10, 0, 0, 1, 1, 0f, 0f);
        FakeAnimationFrameSnippet SNIPPET_1_1 = new FakeAnimationFrameSnippet(
                image11, 0, 0, 1, 1, 0f, 0f);
        FakeAnimationFrameSnippet SNIPPET_1_2 = new FakeAnimationFrameSnippet(
                image12, 0, 0, 1, 1, 0f, 0f);
        FakeAnimationFrameSnippet SNIPPET_1_3 = new FakeAnimationFrameSnippet(
                image13, 0, 0, 1, 1, 0f, 0f);
        FakeAnimationFrameSnippet SNIPPET_1_4 = new FakeAnimationFrameSnippet(
                image14, 0, 0, 1, 1, 0f, 0f);
        FakeAnimationFrameSnippet SNIPPET_1_5 = new FakeAnimationFrameSnippet(
                image15, 0, 0, 1, 1, 0f, 0f);
        FakeAnimationFrameSnippet SNIPPET_1_6 = new FakeAnimationFrameSnippet(
                image16, 0, 0, 1, 1, 0f, 0f);
        FakeAnimationFrameSnippet SNIPPET_1_7 = new FakeAnimationFrameSnippet(
                image17, 0, 0, 1, 1, 0f, 0f);

        Map<Integer, AnimationFrameSnippet> animationFrameSnippets = mapOf();
        animationFrameSnippets.put(SNIPPET_1_MS, SNIPPET_1_);
        animationFrameSnippets.put(SNIPPET_2_MS, SNIPPET_2_);
        animationFrameSnippets.put(SNIPPET_3_MS, SNIPPET_3_);
        animationFrameSnippets.put(snippet4MS, snippet4);
        animationFrameSnippets.put(snippet5MS, snippet5);
        animationFrameSnippets.put(snippet6MS, snippet6);
        animationFrameSnippets.put(snippet7MS, snippet7);
        animationFrameSnippets.put(snippet8MS, snippet8);
        animationFrameSnippets.put(snippet9MS, snippet9);
        animationFrameSnippets.put(SNIPPET_1_0MS, SNIPPET_1_0);
        animationFrameSnippets.put(SNIPPET_1_1MS, SNIPPET_1_1);
        animationFrameSnippets.put(SNIPPET_1_2MS, SNIPPET_1_2);
        animationFrameSnippets.put(SNIPPET_1_3MS, SNIPPET_1_3);
        animationFrameSnippets.put(SNIPPET_1_4MS, SNIPPET_1_4);
        animationFrameSnippets.put(SNIPPET_1_5MS, SNIPPET_1_5);
        animationFrameSnippets.put(SNIPPET_1_6MS, SNIPPET_1_6);
        animationFrameSnippets.put(SNIPPET_1_7MS, SNIPPET_1_7);


        Animation createdAnimation = animationFactory.make(
                new AnimationDefinition(id, animationDurationMS, animationFrameSnippets));

        for (var ms = 0; ms < animationDurationMS; ms++) {
            if (ms < SNIPPET_2_MS) {
                assertSame(SNIPPET_1_, createdAnimation.snippetAtFrame(ms));
            }
            else if (ms < SNIPPET_3_MS) {
                assertSame(SNIPPET_2_, createdAnimation.snippetAtFrame(ms));
            }
            else if (ms < snippet4MS) {
                assertSame(SNIPPET_3_, createdAnimation.snippetAtFrame(ms));
            }
            else if (ms < snippet5MS) {
                assertSame(snippet4, createdAnimation.snippetAtFrame(ms));
            }
            else if (ms < snippet6MS) {
                assertSame(snippet5, createdAnimation.snippetAtFrame(ms));
            }
            else if (ms < snippet7MS) {
                assertSame(snippet6, createdAnimation.snippetAtFrame(ms));
            }
            else if (ms < snippet8MS) {
                assertSame(snippet7, createdAnimation.snippetAtFrame(ms));
            }
            else if (ms < snippet9MS) {
                assertSame(snippet8, createdAnimation.snippetAtFrame(ms));
            }
            else if (ms < SNIPPET_1_0MS) {
                assertSame(snippet9, createdAnimation.snippetAtFrame(ms));
            }
            else if (ms < SNIPPET_1_1MS) {
                assertSame(SNIPPET_1_0, createdAnimation.snippetAtFrame(ms));
            }
            else if (ms < SNIPPET_1_2MS) {
                assertSame(SNIPPET_1_1, createdAnimation.snippetAtFrame(ms));
            }
            else if (ms < SNIPPET_1_3MS) {
                assertSame(SNIPPET_1_2, createdAnimation.snippetAtFrame(ms));
            }
            else if (ms < SNIPPET_1_4MS) {
                assertSame(SNIPPET_1_3, createdAnimation.snippetAtFrame(ms));
            }
            else if (ms < SNIPPET_1_5MS) {
                assertSame(SNIPPET_1_4, createdAnimation.snippetAtFrame(ms));
            }
            else if (ms < SNIPPET_1_6MS) {
                assertSame(SNIPPET_1_5, createdAnimation.snippetAtFrame(ms));
            }
            else if (ms < SNIPPET_1_7MS) {
                assertSame(SNIPPET_1_6, createdAnimation.snippetAtFrame(ms));
            }
            else {
                assertSame(SNIPPET_1_7, createdAnimation.snippetAtFrame(ms));
            }
        }
    }

    @Test
    public void testCreateWithInvalidArgs() {
        Map<Integer, AnimationFrameSnippet> animationFrameSnippets = mapOf();
        animationFrameSnippets.put(SNIPPET_1_MS, animationFrameSnippetDefinition1);
        animationFrameSnippets.put(SNIPPET_2_MS, animationFrameSnippetDefinition2);
        animationFrameSnippets.put(SNIPPET_3_MS, animationFrameSnippetDefinition3);

        assertThrows(IllegalArgumentException.class, () -> animationFactory.make(null));



        assertThrows(IllegalArgumentException.class,
                () -> animationFactory.make(new AnimationDefinition(
                        null, animationDurationMS, animationFrameSnippets)));
        assertThrows(IllegalArgumentException.class,
                () -> animationFactory.make(new AnimationDefinition(
                        "", animationDurationMS, animationFrameSnippets)));



        assertThrows(IllegalArgumentException.class,
                () -> animationFactory.make(new AnimationDefinition(id, 0,
                        animationFrameSnippets)));



        assertThrows(IllegalArgumentException.class,
                () -> animationFactory.make(new AnimationDefinition(
                        id, animationDurationMS, null)));
        assertThrows(IllegalArgumentException.class,
                () -> animationFactory.make(new AnimationDefinition(
                        id, animationDurationMS, mapOf())));



        animationFrameSnippets.remove(SNIPPET_1_MS);
        animationFrameSnippets.put(-1, animationFrameSnippetDefinition1);
        assertThrows(IllegalArgumentException.class,
                () -> animationFactory.make(new AnimationDefinition(
                        id, animationDurationMS, animationFrameSnippets)));
        animationFrameSnippets.remove(-1);
        animationFrameSnippets.put(SNIPPET_1_MS, animationFrameSnippetDefinition1);

        animationFrameSnippets.remove(SNIPPET_1_MS);
        animationFrameSnippets.put(animationDurationMS, animationFrameSnippetDefinition1);
        assertThrows(IllegalArgumentException.class,
                () -> animationFactory.make(new AnimationDefinition(
                        id, animationDurationMS, animationFrameSnippets)));
        animationFrameSnippets.remove(animationDurationMS);
        animationFrameSnippets.put(SNIPPET_1_MS, animationFrameSnippetDefinition1);

        animationFrameSnippets.remove(SNIPPET_1_MS);
        animationFrameSnippets.put(1, animationFrameSnippetDefinition1);
        assertThrows(IllegalArgumentException.class,
                () -> animationFactory.make(new AnimationDefinition(
                        id, animationDurationMS, animationFrameSnippets)));
        animationFrameSnippets.remove(1);
        animationFrameSnippets.put(SNIPPET_1_MS, animationFrameSnippetDefinition1);



        animationFrameSnippetDefinition1.Image = null;
        assertThrows(IllegalArgumentException.class, () -> animationFactory.make(
                new AnimationDefinition(id, animationDurationMS, animationFrameSnippets)
        ));
        animationFrameSnippetDefinition1.Image = IMAGE_1;



        IMAGE_1.RelativeLocation = null;
        assertThrows(IllegalArgumentException.class, () -> animationFactory.make(
                new AnimationDefinition(id, animationDurationMS, animationFrameSnippets)
        ));
        IMAGE_1.RelativeLocation = IMAGE_1_RELATIVE_LOC;

        IMAGE_1.RelativeLocation = "";
        assertThrows(IllegalArgumentException.class, () -> animationFactory.make(
                new AnimationDefinition(id, animationDurationMS, animationFrameSnippets)
        ));
        IMAGE_1.RelativeLocation = IMAGE_1_RELATIVE_LOC;



        IMAGE_1.Width = 0;
        assertThrows(IllegalArgumentException.class, () -> animationFactory.make(
                new AnimationDefinition(id, animationDurationMS, animationFrameSnippets)
        ));
        IMAGE_1.Width = IMAGE_1_WIDTH;

        IMAGE_1.Height = 0;
        assertThrows(IllegalArgumentException.class, () -> animationFactory.make(
                new AnimationDefinition(id, animationDurationMS, animationFrameSnippets)
        ));
        IMAGE_1.Height = IMAGE_1_HEIGHT;



        animationFrameSnippetDefinition1.LeftX = -1;
        assertThrows(IllegalArgumentException.class, () -> animationFactory.make(
                new AnimationDefinition(id, animationDurationMS, animationFrameSnippets)
        ));
        animationFrameSnippetDefinition1.LeftX = SNIPPET_1_LEFT_X;

        animationFrameSnippetDefinition1.TopY = -1;
        assertThrows(IllegalArgumentException.class, () -> animationFactory.make(
                new AnimationDefinition(id, animationDurationMS, animationFrameSnippets)
        ));
        animationFrameSnippetDefinition1.TopY = SNIPPET_1_TOP_Y;

        animationFrameSnippetDefinition1.RightX = -1;
        assertThrows(IllegalArgumentException.class, () -> animationFactory.make(
                new AnimationDefinition(id, animationDurationMS, animationFrameSnippets)
        ));
        animationFrameSnippetDefinition1.RightX = SNIPPET_1_RIGHT_X;

        animationFrameSnippetDefinition1.BottomY = -1;
        assertThrows(IllegalArgumentException.class, () -> animationFactory.make(
                new AnimationDefinition(id, animationDurationMS, animationFrameSnippets)
        ));
        animationFrameSnippetDefinition1.BottomY = SNIPPET_1_BottomY;



        animationFrameSnippetDefinition1.RightX = SNIPPET_1_LEFT_X;
        assertThrows(IllegalArgumentException.class, () -> animationFactory.make(
                new AnimationDefinition(id, animationDurationMS, animationFrameSnippets)
        ));
        animationFrameSnippetDefinition1.RightX = SNIPPET_1_RIGHT_X;

        animationFrameSnippetDefinition1.BottomY = SNIPPET_1_TOP_Y;
        assertThrows(IllegalArgumentException.class, () -> animationFactory.make(
                new AnimationDefinition(id, animationDurationMS, animationFrameSnippets)
        ));
        animationFrameSnippetDefinition1.BottomY = SNIPPET_1_BottomY;



        animationFrameSnippetDefinition1.LeftX = IMAGE_1_WIDTH + 1;
        assertThrows(IllegalArgumentException.class, () -> animationFactory.make(
                new AnimationDefinition(id, animationDurationMS, animationFrameSnippets)
        ));
        animationFrameSnippetDefinition1.LeftX = SNIPPET_1_LEFT_X;

        animationFrameSnippetDefinition1.TopY = IMAGE_1_HEIGHT + 1;
        assertThrows(IllegalArgumentException.class, () -> animationFactory.make(
                new AnimationDefinition(id, animationDurationMS, animationFrameSnippets)
        ));
        animationFrameSnippetDefinition1.TopY = SNIPPET_1_TOP_Y;

        animationFrameSnippetDefinition1.RightX = IMAGE_1_WIDTH + 1;
        assertThrows(IllegalArgumentException.class, () -> animationFactory.make(
                new AnimationDefinition(id, animationDurationMS, animationFrameSnippets)
        ));
        animationFrameSnippetDefinition1.RightX = SNIPPET_1_RIGHT_X;

        animationFrameSnippetDefinition1.BottomY = IMAGE_1_HEIGHT + 1;
        assertThrows(IllegalArgumentException.class, () -> animationFactory.make(
                new AnimationDefinition(id, animationDurationMS, animationFrameSnippets)
        ));
        animationFrameSnippetDefinition1.BottomY = SNIPPET_1_BottomY;
    }
}
