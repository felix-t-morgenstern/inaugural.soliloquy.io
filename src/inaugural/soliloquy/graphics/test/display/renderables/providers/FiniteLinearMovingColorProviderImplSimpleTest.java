package inaugural.soliloquy.graphics.test.display.renderables.providers;

import inaugural.soliloquy.graphics.test.display.DisplayTest;

import static inaugural.soliloquy.graphics.api.Constants.INTACT_COLOR;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 1920x1080 pixels in the middle of the screen for 3000ms
 *    with a titlebar reading "My title bar". The window will contain a picture of a shield,
 *    centered in the window, taking up half of the width and three-fourths of the height of the
 *    window.
 * 2. At first, there should be no (or next-to-no border); over the first 1000ms, the border will
 *    transition from purple to light blue; after which, it will transition over 2000ms to a dull
 *    gray.
 * 3. The window will then close.
 *
 */
class FiniteLinearMovingColorProviderImplSimpleTest
        extends FiniteLinearMovingColorProviderImplTest {

    public static void main(String[] args) {
        runTest(windowResolutionManager ->
                        generateRenderablesAndRenderersWithMeshAndShader(BORDER_THICKNESS,
                                INTACT_COLOR, null, windowResolutionManager),
                FiniteLinearMovingProviderTest::stackRendererAction,
                FiniteLinearMovingColorProviderImplSimpleTest::graphicsPreloaderLoadAction,
                DisplayTest::closeAfterSomeTime);
    }
}
