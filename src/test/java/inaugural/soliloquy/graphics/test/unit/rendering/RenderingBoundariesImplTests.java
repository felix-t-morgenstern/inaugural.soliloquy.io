package inaugural.soliloquy.graphics.test.unit.rendering;

import inaugural.soliloquy.graphics.rendering.RenderingBoundariesImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.RenderingBoundaries;

import static inaugural.soliloquy.graphics.api.Constants.WHOLE_SCREEN;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RenderingBoundariesImplTests {
    @Mock private FloatBox mockFloatBox1;
    @Mock private FloatBox mockFloatBox2;
    @Mock private FloatBox mockFloatBox3;
    @Mock private FloatBox mockFloatBox1Intersected2;
    @Mock private FloatBox mockFloatBox1Intersected2Intersected3;

    private RenderingBoundaries renderingBoundaries;

    @Before
    public void setUp() {
        when(mockFloatBox1.intersection(any())).thenReturn(mockFloatBox1Intersected2);
        when(mockFloatBox1Intersected2.intersection(any())).thenReturn(
                mockFloatBox1Intersected2Intersected3);

        renderingBoundaries = new RenderingBoundariesImpl();
    }

    @Test
    public void testCurrentBoundariesWithoutAnyInput() {
        var currentBoundaries = renderingBoundaries.currentBoundaries();

        assertEquals(WHOLE_SCREEN, currentBoundaries);
    }

    @Test
    public void testPushOneNewBoundaries() {
        renderingBoundaries.pushNewBoundaries(mockFloatBox1);

        assertSame(mockFloatBox1, renderingBoundaries.currentBoundaries());
    }

    @Test
    public void testPushTwoNewBoundaries() {
        renderingBoundaries.pushNewBoundaries(mockFloatBox1);
        renderingBoundaries.pushNewBoundaries(mockFloatBox2);

        assertSame(mockFloatBox1Intersected2, renderingBoundaries.currentBoundaries());
    }

    @Test
    public void testPushThreeNewBoundaries() {
        renderingBoundaries.pushNewBoundaries(mockFloatBox1);
        renderingBoundaries.pushNewBoundaries(mockFloatBox2);
        renderingBoundaries.pushNewBoundaries(mockFloatBox3);

        assertSame(mockFloatBox1Intersected2Intersected3, renderingBoundaries.currentBoundaries());
    }

    @Test
    public void testPushThreeNewBoundariesThenPopOne() {
        renderingBoundaries.pushNewBoundaries(mockFloatBox1);
        renderingBoundaries.pushNewBoundaries(mockFloatBox2);
        renderingBoundaries.pushNewBoundaries(mockFloatBox3);
        renderingBoundaries.popMostRecentBoundaries();

        assertSame(mockFloatBox1Intersected2, renderingBoundaries.currentBoundaries());
    }

    @Test
    public void testPushThreeNewBoundariesThenPopTwo() {
        renderingBoundaries.pushNewBoundaries(mockFloatBox1);
        renderingBoundaries.pushNewBoundaries(mockFloatBox2);
        renderingBoundaries.pushNewBoundaries(mockFloatBox3);
        renderingBoundaries.popMostRecentBoundaries();
        renderingBoundaries.popMostRecentBoundaries();

        assertSame(mockFloatBox1, renderingBoundaries.currentBoundaries());
    }

    @Test
    public void testPushThreeNewBoundariesThenPopThree() {
        renderingBoundaries.pushNewBoundaries(mockFloatBox1);
        renderingBoundaries.pushNewBoundaries(mockFloatBox2);
        renderingBoundaries.pushNewBoundaries(mockFloatBox3);
        renderingBoundaries.popMostRecentBoundaries();
        renderingBoundaries.popMostRecentBoundaries();
        renderingBoundaries.popMostRecentBoundaries();

        assertEquals(WHOLE_SCREEN, renderingBoundaries.currentBoundaries());
    }

    @Test
    public void testClearAllBoundaries() {
        renderingBoundaries.pushNewBoundaries(mockFloatBox1);
        renderingBoundaries.pushNewBoundaries(mockFloatBox2);
        renderingBoundaries.pushNewBoundaries(mockFloatBox3);
        renderingBoundaries.clearAllBoundaries();

        assertEquals(WHOLE_SCREEN, renderingBoundaries.currentBoundaries());
    }

    @Test
    public void testPushNewBoundariesWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> renderingBoundaries.pushNewBoundaries(null));
    }

    @Test
    public void testGetInterfaceName() {
        assertEquals(RenderingBoundaries.class.getCanonicalName(),
                renderingBoundaries.getInterfaceName());
    }
}
