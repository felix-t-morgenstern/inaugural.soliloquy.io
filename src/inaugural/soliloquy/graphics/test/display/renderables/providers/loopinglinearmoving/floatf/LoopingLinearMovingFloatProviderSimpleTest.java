package inaugural.soliloquy.graphics.test.display.renderables.providers.loopinglinearmoving.floatf;

import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 1920x1080 pixels in the middle of the screen for 3000ms
 *    with a titlebar reading "My title bar". The window will contain a picture of a shield,
 *    centered in the window, taking up half of the width and three-fourths of the height of the
 *    window.
 * 2. Over 4000ms, the border around the shield will pulse.
 * 3. The window will then close.
 *
 */
class LoopingLinearMovingFloatProviderSimpleTest extends LoopingLinearMovingFloatProviderTest {
    public static void main(String[] args) {
        runTest(windowResolutionManager ->
                        generateRenderablesAndRenderersWithMeshAndShader(BORDER_THICKNESS,
                                BORDER_COLOR, null, windowResolutionManager),
                LoopingLinearMovingFloatProviderTest::stackRendererAction,
                LoopingLinearMovingFloatProviderTest::graphicsPreloaderLoadAction,
                LoopingLinearMovingFloatProviderSimpleTest::closeAfterSomeTime);
    }

    public static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        CheckedExceptionWrapper.sleep(4000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
