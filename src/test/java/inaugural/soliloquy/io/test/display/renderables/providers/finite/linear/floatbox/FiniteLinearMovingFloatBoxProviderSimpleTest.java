package inaugural.soliloquy.io.test.display.renderables.providers.finite.linear.floatbox;

import inaugural.soliloquy.io.test.display.DisplayTest;

import static inaugural.soliloquy.io.api.Constants.INTACT_COLOR;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 1920x1080 pixels in the middle of the screen for 3000ms
 * with a titlebar reading "My title bar". The window will contain a picture of a shield,
 * located in the upper-left corner, at 50% size.
 * 2. Over the 3000ms, the shield will move to the center of the window, while growing to 100%
 * size.
 * 3. The window will then close.
 */
class FiniteLinearMovingFloatBoxProviderSimpleTest extends FiniteLinearMovingFloatBoxProviderTest {
    public static void main(String[] args) {
        runTest(windowResolutionManager ->
                        generateRenderablesAndRenderersWithMeshAndShader(0f,
                                INTACT_COLOR, null, windowResolutionManager),
                FiniteLinearMovingFloatBoxProviderTest::graphicsPreloaderLoadAction,
                DisplayTest::closeAfterSomeTime);
    }
}
