package inaugural.soliloquy.io.test.display.renderables.providers.finite.sinusoid.floatbox;

import inaugural.soliloquy.io.test.display.DisplayTest;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.io.graphics.bootstrap.GraphicsCoreLoop;

import static inaugural.soliloquy.io.api.Constants.INTACT_COLOR;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 1920x1080 pixels in the middle of the screen for 3000ms
 * with a titlebar reading "My title bar". The window will contain a picture of a shield,
 * centered in the window, taking up half of the width and three-fourths of the height of the
 * window.
 * 2. At first, there should be no (or next-to-no border). Over the first 4000ms, a purple border
 * will grow around the shield, slowly at first, then quicker, then more slowly towards the end.
 * Over the last 4000ms, the border will recede; at first, it will recede more slowly than it
 * accreted; then, in the middle, it will recede more quickly than it had accreted in the middle;
 * lastly, it will recede more slowly than it had accreted at the end.
 * 3. During the first transition, after 2000ms, the transition will pause for 2000ms.
 * 4. The window will then close.
 */
public class FiniteSinusoidMovingFloatBoxProviderPausingTest
        extends FiniteSinusoidMovingFloatBoxProviderTest {
    public static void main(String[] args) {
        DisplayTest.runTest(windowResolutionManager ->
                        FiniteSinusoidMovingFloatBoxProviderTest
                                .generateRenderablesAndRenderersWithMeshAndShader(0f,
                                        INTACT_COLOR, null,
                                        windowResolutionManager),
                FiniteSinusoidMovingFloatBoxProviderTest::graphicsPreloaderLoadAction,
                FiniteSinusoidMovingFloatBoxProviderPausingTest::closeAfterSomeTime);
    }


    public static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        CheckedExceptionWrapper.sleep(2000);

        FiniteSinusoidMovingFloatBoxProvider.reportPause(GLOBAL_CLOCK.globalTimestamp());

        CheckedExceptionWrapper.sleep(2000);

        FiniteSinusoidMovingFloatBoxProvider.reportUnpause(GLOBAL_CLOCK.globalTimestamp());

        CheckedExceptionWrapper.sleep(4000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
