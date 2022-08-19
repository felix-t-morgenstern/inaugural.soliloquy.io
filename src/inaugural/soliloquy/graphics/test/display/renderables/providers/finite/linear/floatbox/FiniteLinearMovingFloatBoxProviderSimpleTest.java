package inaugural.soliloquy.graphics.test.display.renderables.providers.finite.linear.floatbox;

import inaugural.soliloquy.graphics.test.display.DisplayTest;
import inaugural.soliloquy.graphics.test.display.renderables.providers.finite.linear.FiniteLinearMovingProviderTest;

import static inaugural.soliloquy.graphics.api.Constants.INTACT_COLOR;

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
        DisplayTest.runTest(windowResolutionManager ->
                        FiniteLinearMovingProviderTest.generateRenderablesAndRenderersWithMeshAndShader(0f,
                                INTACT_COLOR, null, windowResolutionManager),
                FiniteLinearMovingProviderTest::stackRendererAction,
                FiniteLinearMovingFloatBoxProviderTest::graphicsPreloaderLoadAction,
                DisplayTest::closeAfterSomeTime);
    }
}
