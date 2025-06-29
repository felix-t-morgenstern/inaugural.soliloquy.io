package inaugural.soliloquy.io.test.display.renderables.providers.looping.linear.color;

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
 * of the height of the window. There will be a colored border around the shield.
 * 2. Each 4000ms, the color of the shield will transition from red to blue, blue to green, green
 * to fuchsia, and from fuchsia back to red again.
 * 3. The border color animation will proceed for 8000ms.
 * 4. The window will then close.
 */
class LoopingMovingColorProviderImplSimpleTest extends LoopingMovingColorProviderImplTest {
    public static void main(String[] args) {
        runTest(windowResolutionManager ->
                        generateRenderablesAndRenderersWithMeshAndShader(BORDER_THICKNESS,
                                Color.WHITE, null, windowResolutionManager),
                LoopingMovingColorProviderImplTest::graphicsPreloaderLoadAction,
                LoopingMovingColorProviderImplSimpleTest::closeAfterSomeTime);
    }

    public static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        BORDER_COLOR_PROVIDER.reset(GLOBAL_CLOCK.globalTimestamp());

        CheckedExceptionWrapper.sleep(8000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
