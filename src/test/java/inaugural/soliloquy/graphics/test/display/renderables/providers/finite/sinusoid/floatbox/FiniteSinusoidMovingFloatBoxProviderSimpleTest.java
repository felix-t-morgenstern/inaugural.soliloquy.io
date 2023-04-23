package inaugural.soliloquy.graphics.test.display.renderables.providers.finite.sinusoid.floatbox;

import inaugural.soliloquy.graphics.test.display.DisplayTest;

import static inaugural.soliloquy.graphics.api.Constants.INTACT_COLOR;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 1920x1080 pixels in the middle of the screen for 6000ms
 * with a titlebar reading "My title bar". The window will contain a picture of a shield,
 * located in the upper-left corner, at 50% size.
 * 2. Over the first 3000ms, the shield will move to the center of the window, while growing to 100%
 * size. Over the last 3000ms, it will move to the lower-right corner of the window, while shrinking
 * to 50% of its original size. Over the first 3000ms, it will move slowly at first, then quicker,
 * then more slowly towards the end. Over the last 3000ms, at first, it will move more slowly than
 * it did at the start of the first 3000ms; then, in the middle, it will move more quickly than it
 * had in the middle of the first 3000ms; lastly, it will move more slowly than it had at the end of
 * the first 3000ms.
 * 3. The window will then close.
 */
class FiniteSinusoidMovingFloatBoxProviderSimpleTest
        extends FiniteSinusoidMovingFloatBoxProviderTest {
    public static void main(String[] args) {
        DisplayTest.runTest(windowResolutionManager ->
                        FiniteSinusoidMovingFloatBoxProviderTest
                                .generateRenderablesAndRenderersWithMeshAndShader(0f,
                                        INTACT_COLOR, null,
                                        windowResolutionManager),
                FiniteSinusoidMovingFloatBoxProviderTest::graphicsPreloaderLoadAction,
                g -> DisplayTest.closeAfterSomeTime(g, 6000));
    }
}
