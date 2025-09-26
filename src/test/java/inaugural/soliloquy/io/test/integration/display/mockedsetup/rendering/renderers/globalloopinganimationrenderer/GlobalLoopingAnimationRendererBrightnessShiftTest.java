package inaugural.soliloquy.io.test.integration.display.mockedsetup.rendering.renderers.globalloopinganimationrenderer;

import inaugural.soliloquy.io.test.testdoubles.fakes.FakeColorShiftStackAggregator;

import static soliloquy.specs.io.graphics.renderables.colorshifting.NetColorShifts.netShifts;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 1920x1080 pixels in the middle of the screen for 3000ms
 * with a titlebar reading "My title bar". The window will contain a looping animation of a
 * torch, centered in the screen, which will change its frames every 250ms. The animation will
 * persist for 2250ms. This animation will have its brightness increased by 50%, i.e. every
 * pixel will be 50% closer to white
 * 2. The window will then close.
 */
public class GlobalLoopingAnimationRendererBrightnessShiftTest
        extends GlobalLoopingAnimationRendererTest {
    public static void main(String[] args) {
        // NB: This should be brought up to 0.6666667f
        var netColorShifts = netShifts(0.5f, 0, 0, 0, 0);
        var colorShiftStackAggregator = new FakeColorShiftStackAggregator(netColorShifts);

        runTest(
                windowResolutionManager -> generateRenderablesAndRenderersWithMeshAndShader(
                        windowResolutionManager,
                        colorShiftStackAggregator
                ),
                GlobalLoopingAnimationRendererTest::graphicsPreloaderLoadAction,
                coreLoop -> closeAfterSomeTime(coreLoop, TEST_DURATION_MS)
        );
    }
}
