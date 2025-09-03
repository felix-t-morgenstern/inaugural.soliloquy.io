package inaugural.soliloquy.io.test.integration.display.rendering.renderers.spriterenderer;

import inaugural.soliloquy.io.test.integration.display.DisplayTest;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeColorShiftStackAggregator;

import static inaugural.soliloquy.io.api.Constants.INTACT_COLOR;
import static soliloquy.specs.io.graphics.renderables.colorshifting.NetColorShifts.netShifts;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 1920x1080 pixels in the middle of the screen for 3000ms
 * with a titlebar reading "My title bar". The window will contain a picture of a shield,
 * centered in the window, taking up half of the width and three-fourths of the height of the
 * window. This sprite will have its colors shifted by 50% of the color wheel, i.e., red (0
 * degrees) will be cyan (180 degrees), yellow-green (60 degrees) will be blue (240 degrees),
 * etc.
 * 2. The window will then close.
 */
public class SpriteRendererColorRotationShiftTest extends SpriteRendererTest {
    public static void main(String[] args) {
        // NB: This should be brought up to 0.5f
        var netColorShifts = netShifts(0, 0, 0, 0, 30.5f);
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
