package inaugural.soliloquy.io.test.display.renderables.providers.finite.linear.floatf;

import inaugural.soliloquy.io.test.display.DisplayTest;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 1920x1080 pixels in the middle of the screen for 3000ms
 * with a titlebar reading "My title bar". The window will contain a picture of a shield,
 * centered in the window, taking up half of the width and three-fourths of the height of the
 * window.
 * 2. At first, there should be no (or next-to-no border); over the 3000ms, a purple border will
 * gradually grow around the shield.
 * 3. The window will then close.
 */
class FiniteLinearMovingFloatProviderSimpleTest extends FiniteLinearMovingFloatProviderTest {
    public static void main(String[] args) {
        runTest(windowResolutionManager ->
                        generateRenderablesAndRenderersWithMeshAndShader(BORDER_THICKNESS,
                                        BORDER_COLOR, null, windowResolutionManager),
                FiniteLinearMovingFloatProviderTest::graphicsPreloaderLoadAction,
                DisplayTest::closeAfterSomeTime);
    }
}
