package inaugural.soliloquy.graphics.test.display.rendering.renderers.finiteanimationrenderer;

import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeColorShiftStackAggregator;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeNetColorShifts;

/**
 * Test acceptance criteria:
 *
 * 1. An 800x600 window will open. An explosion will be displayed in the center of the window over
 *    roughly 1250ms. The explosion will be shifted to be green.
 * 2. The window will close.
 *
 */
public class FiniteAnimationRendererColorRotationShiftTest extends FiniteAnimationRendererTest {
    public static void main(String[] args) {
        FakeNetColorShifts netColorShifts = new FakeNetColorShifts();
        // NB: This should be brought up to 0.3333f
        netColorShifts.NetColorRotationShift = 30.3333f;
        FakeColorShiftStackAggregator colorShiftStackAggregator =
                new FakeColorShiftStackAggregator(netColorShifts);

        runTest(
                windowResolutionManager -> FiniteAnimationRendererTest
                        .generateRenderablesAndRenderersWithMeshAndShader(
                                windowResolutionManager,
                                colorShiftStackAggregator),
                timestamp -> FiniteAnimationRenderer.render(FiniteAnimationRenderable, timestamp),
                FiniteAnimationRendererTest::graphicsPreloaderLoadAction,
                graphicsCoreLoop -> closeAfterSomeTime(graphicsCoreLoop, TestDurationMs)
        );
    }
}
