package inaugural.soliloquy.io.test.unit.graphics.rendering;

import inaugural.soliloquy.io.graphics.rendering.RenderingBoundariesImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;

import static inaugural.soliloquy.io.api.Constants.WHOLE_SCREEN;
import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.valueobjects.FloatBox.intersection;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;

@ExtendWith(MockitoExtension.class)
public class RenderingBoundariesImplTests {
    private final FloatBox RENDERING_BOUNDARIES_1 = randomValidFloatBox();
    private final FloatBox RENDERING_BOUNDARIES_2 =
            randomFloatBoxIntersecting(RENDERING_BOUNDARIES_1);

    private RenderingBoundaries renderingBoundaries;

    private FloatBox randomFloatBoxIntersecting(FloatBox f) {
        var leftX = randomFloatInRange(f.LEFT_X, f.RIGHT_X);
        var topY = randomFloatInRange(f.TOP_Y, f.BOTTOM_Y);
        var rightX = randomFloatWithInclusiveFloor(f.RIGHT_X);
        var bottomY = randomFloatWithInclusiveFloor(f.BOTTOM_Y);

        return floatBoxOf(leftX, topY, rightX, bottomY);
    }

    @BeforeEach
    public void setUp() {
        renderingBoundaries = new RenderingBoundariesImpl();
    }

    @Test
    public void testDefaultCurrentBoundaries() {
        assertEquals(WHOLE_SCREEN, renderingBoundaries.currentBoundaries());
    }

    @Test
    public void testPushNewBoundaries() {
        renderingBoundaries.pushNewBoundaries(RENDERING_BOUNDARIES_1);

        var currentBoundaries = renderingBoundaries.currentBoundaries();

        assertEquals(RENDERING_BOUNDARIES_1, currentBoundaries);
    }

    @Test
    public void testPushMultipleBoundariesOnlyPushesIntersection() {
        renderingBoundaries.pushNewBoundaries(RENDERING_BOUNDARIES_1);
        renderingBoundaries.pushNewBoundaries(RENDERING_BOUNDARIES_2);

        var currentBoundaries = renderingBoundaries.currentBoundaries();

        var expectedBoundaries = intersection(RENDERING_BOUNDARIES_1, RENDERING_BOUNDARIES_2);
        assertEquals(expectedBoundaries, currentBoundaries);
    }

    @Test
    public void testPushNewBoundariesWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> renderingBoundaries.pushNewBoundaries(null));
        assertThrows(IllegalArgumentException.class, () -> renderingBoundaries.pushNewBoundaries(floatBoxOf(0f, 0f, -1f, 0f)));
        assertThrows(IllegalArgumentException.class, () -> renderingBoundaries.pushNewBoundaries(floatBoxOf(0f, 0f, 0f, -1f)));
    }

    @Test
    public void testPopMostRecentBoundary() {
        renderingBoundaries.pushNewBoundaries(RENDERING_BOUNDARIES_1);
        renderingBoundaries.pushNewBoundaries(RENDERING_BOUNDARIES_2);

        renderingBoundaries.popMostRecentBoundaries();
        var currentBoundaries = renderingBoundaries.currentBoundaries();

        assertEquals(RENDERING_BOUNDARIES_1, currentBoundaries);
    }

    @Test
    public void testClearAllBoundariesRevertsToDefault() {
        renderingBoundaries.pushNewBoundaries(RENDERING_BOUNDARIES_1);

        renderingBoundaries.clearAllBoundaries();
        var currentBoundaries = renderingBoundaries.currentBoundaries();

        assertEquals(WHOLE_SCREEN, currentBoundaries);
    }
}
