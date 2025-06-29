package inaugural.soliloquy.io.test.display.renderables.providers.looping.linear.floatbox;

import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.io.graphics.bootstrap.GraphicsCoreLoop;

import java.awt.*;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 1920x1080 pixels in the middle of the screen for 8000ms
 * with a titlebar reading "My title bar". The window will contain a picture of a shield,
 * in the top-left corner of the window, taking up one eighth of the width and three-sixteenths
 * of the height of the window.
 * 2. Over 8000ms, the shield will move clockwise from one corner of the screen to another.
 * 3. After the first 5500ms, the movement will pause for 2000ms. Then, the movement will continue
 * for the remaining 2500ms.
 * 4. The window will then close.
 */
class LoopingLinearMovingFloatBoxProviderTestWithPausing
        extends LoopingLinearMovingFloatBoxProviderTest {
    public static void main(String[] args) {
        runTest(windowResolutionManager ->
                        generateRenderablesAndRenderersWithMeshAndShader(0f,
                                Color.WHITE, null, windowResolutionManager),
                LoopingLinearMovingFloatBoxProviderTest::graphicsPreloaderLoadAction,
                LoopingLinearMovingFloatBoxProviderTestWithPausing::closeAfterSomeTime);
    }

    public static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        CheckedExceptionWrapper.sleep(5500);

        RENDERING_DIMENSIONS_PROVIDER.reportPause(GLOBAL_CLOCK.globalTimestamp());

        CheckedExceptionWrapper.sleep(2000);

        RENDERING_DIMENSIONS_PROVIDER.reportUnpause(GLOBAL_CLOCK.globalTimestamp());

        CheckedExceptionWrapper.sleep(2500);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
