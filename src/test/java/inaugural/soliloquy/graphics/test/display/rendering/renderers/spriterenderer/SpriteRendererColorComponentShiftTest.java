package inaugural.soliloquy.graphics.test.display.rendering.renderers.spriterenderer;

import inaugural.soliloquy.graphics.test.display.DisplayTest;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeColorShiftStackAggregator;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeNetColorShifts;

import static inaugural.soliloquy.graphics.api.Constants.INTACT_COLOR;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 1920x1080 pixels in the middle of the screen for 3000ms
 * with a titlebar reading "My title bar". The window will contain a picture of a shield,
 * centered in the window, taking up half of the width and three-fourths of the height of the
 * window. This sprite will have its brightness decreased by 100% for all pixels; although red
 * will be counter-adjusted back to 100% of its original value, green will be counter-adjusted
 * back to 50% of its original value, and blue will be counter-adjusted back to 25% of its
 * original value.
 * 2. The window will then close.
 */
public class SpriteRendererColorComponentShiftTest extends SpriteRendererTest {
    public static void main(String[] args) {
        FakeNetColorShifts netColorShifts = new FakeNetColorShifts();
        // NB: This should be brought up to 0.5f
        netColorShifts.BrightnessShift = -1.0f;
        netColorShifts.RedIntensityShift = 1.0f;
        netColorShifts.GreenIntensityShift = 0.5f;
        netColorShifts.BlueIntensityShift = 0.25f;
        FakeColorShiftStackAggregator colorShiftStackAggregator =
                new FakeColorShiftStackAggregator(netColorShifts);

        runTest(
                windowResolutionManager -> SpriteRendererTest
                        .generateRenderablesAndRenderersWithMeshAndShader(
                                0f,
                                INTACT_COLOR,
                                colorShiftStackAggregator,
                                windowResolutionManager),
                SpriteRendererTest::graphicsPreloaderLoadAction,
                DisplayTest::closeAfterSomeTime);
    }
}
