package inaugural.soliloquy.graphics.test.display.renderables.providers.finite.sinusoid.floatf;

import inaugural.soliloquy.graphics.test.display.DisplayTest;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 1920x1080 pixels in the middle of the screen for 3000ms
 * with a titlebar reading "My title bar". The window will contain a picture of a shield,
 * centered in the window, taking up half of the width and three-fourths of the height of the
 * window.
 * 2. At first, there should be no (or next-to-no border). Over the first 6000ms, a purple border
 * will grow around the shield, slowly at first, then quicker, then more slowly towards the end.
 * Within this first transition, there will be in the pause in the middle, for 2000ms. Over the
 * last 4000ms, the border will recede; at first, it will recede more slowly than it accreted;
 * then, in the middle, it will recede more quickly than it had accreted in the middle; lastly, it
 * will recede more slowly than it had accreted at the end.
 * 3. The window will then close.
 */
class FiniteSinusoidMovingFloatProviderPausingTest extends FiniteSinusoidMovingFloatProviderTest {
    public static void main(String[] args) {
        DisplayTest.runTest(windowResolutionManager ->
                        FiniteSinusoidMovingFloatProviderTest
                                .generateRenderablesAndRenderersWithMeshAndShader(BORDER_THICKNESS,
                                        BORDER_COLOR, null, windowResolutionManager),
                FiniteSinusoidMovingFloatProviderTest::stackRendererAction,
                FiniteSinusoidMovingFloatProviderTest::graphicsPreloaderLoadAction,
                FiniteSinusoidMovingFloatProviderPausingTest::closeAfterSomeTime
        );
    }


    public static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        CheckedExceptionWrapper.sleep(2000);

        FiniteSinusoidMovingFloatProvider.reportPause(GLOBAL_CLOCK.globalTimestamp());

        CheckedExceptionWrapper.sleep(2000);

        FiniteSinusoidMovingFloatProvider.reportUnpause(GLOBAL_CLOCK.globalTimestamp());

        CheckedExceptionWrapper.sleep(6000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
