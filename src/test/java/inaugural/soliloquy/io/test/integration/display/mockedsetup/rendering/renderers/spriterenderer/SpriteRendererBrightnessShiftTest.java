package inaugural.soliloquy.io.test.integration.display.mockedsetup.rendering.renderers.spriterenderer;

import inaugural.soliloquy.io.test.integration.display.mockedsetup.DisplayTest;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeColorShiftStackAggregator;

import static inaugural.soliloquy.io.api.Constants.INTACT_COLOR;
import static soliloquy.specs.io.graphics.renderables.colorshifting.NetColorShifts.netShifts;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 1920x1080 pixels in the middle of the screen for 3000ms
 * with a titlebar reading "My title bar". The window will contain a picture of a shield,
 * centered in the window, taking up half of the width and three-fourths of the height of the
 * window. This sprite will have its brightness increased by 50%, i.e. every pixel will be 50%
 * closer to white
 * 2. The window will then close.
 */
public class SpriteRendererBrightnessShiftTest extends SpriteRendererTest {
    public static void main(String[] args) {
        // NB: This should be brought up to 0.5f
        var netColorShifts = netShifts(0.5f, 0, 0, 0, 0);
        FakeColorShiftStackAggregator colorShiftStackAggregator =
                new FakeColorShiftStackAggregator(netColorShifts);

        runTest(
                windowResolutionManager -> generateRenderablesAndRenderersWithMeshAndShader(
                        0f,
                        INTACT_COLOR,
                        colorShiftStackAggregator,
                        windowResolutionManager::windowWidthToHeightRatio),
                SpriteRendererTest::graphicsPreloaderLoadAction,
                DisplayTest::closeAfterSomeTime);
    }
}
