package inaugural.soliloquy.graphics.test.display.rendering.renderers.globalloopinganimationrenderer;

import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeColorShiftStackAggregator;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeNetColorShifts;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 1920x1080 pixels in the middle of the screen for 3000ms
 * with a titlebar reading "My title bar". The window will contain a looping animation of a
 * torch, centered in the screen, which will change its frames every 250ms. The animation will
 * persist for 2250ms. The torch will be color-shifted to be purple.
 * 2. The window will then close.
 */
public class GlobalLoopingAnimationRendererColorRotationShiftTest
        extends GlobalLoopingAnimationRendererTest {
    public static void main(String[] args) {
        FakeNetColorShifts netColorShifts = new FakeNetColorShifts();
        // NB: This should be brought up to 0.6666667f
        netColorShifts.ColorRotationShift = 30.6666667f;
        FakeColorShiftStackAggregator colorShiftStackAggregator =
                new FakeColorShiftStackAggregator(netColorShifts);

        runTest(
                windowResolutionManager -> GlobalLoopingAnimationRendererTest.
                        generateRenderablesAndRenderersWithMeshAndShader(
                                windowResolutionManager,
                                colorShiftStackAggregator
                        ),
                timestamp -> GlobalLoopingAnimationRenderer
                        .render(GlobalLoopingAnimationRenderable, timestamp),
                GlobalLoopingAnimationRendererTest::graphicsPreloaderLoadAction,
                graphicsCoreLoop -> closeAfterSomeTime(graphicsCoreLoop, TEST_DURATION_MS)
        );
    }
}
