package inaugural.soliloquy.io.test.display.renderables.providers.looping.linear.floatf;

import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.io.graphics.bootstrap.GraphicsCoreLoop;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 1920x1080 pixels in the middle of the screen for 3000ms
 * with a titlebar reading "My title bar". The window will contain a picture of a shield,
 * centered in the window, taking up half of the width and three-fourths of the height of the
 * window.
 * 2. Over 3000ms, the border around the shield will pulse. The pulsing will stop for 2000ms. Then,
 * the pulsing will continue for another 2000ms.
 * 3. The window will then close.
 */
class LoopingLinearMovingFloatProviderTestWithPausing
        extends LoopingLinearMovingFloatProviderTest {
    public static void main(String[] args) {
        runTest(windowResolutionManager ->
                        generateRenderablesAndRenderersWithMeshAndShader(BORDER_THICKNESS,
                                BORDER_COLOR, null, windowResolutionManager),
                LoopingLinearMovingFloatProviderTest::graphicsPreloaderLoadAction,
                LoopingLinearMovingFloatProviderTestWithPausing::closeAfterSomeTime);
    }

    public static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        CheckedExceptionWrapper.sleep(3000);

        BORDER_THICKNESS_PROVIDER.reportPause(GLOBAL_CLOCK.globalTimestamp());

        CheckedExceptionWrapper.sleep(2000);

        BORDER_THICKNESS_PROVIDER.reportUnpause(GLOBAL_CLOCK.globalTimestamp());

        CheckedExceptionWrapper.sleep(2000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
