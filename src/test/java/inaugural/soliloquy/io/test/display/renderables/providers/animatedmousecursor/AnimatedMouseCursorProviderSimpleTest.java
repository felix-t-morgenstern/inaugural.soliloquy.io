package inaugural.soliloquy.io.test.display.renderables.providers.animatedmousecursor;

import inaugural.soliloquy.io.test.display.io.mousecursor.MouseCursorImplTest;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.io.graphics.bootstrap.GraphicsCoreLoop;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 1920x1080 pixels in the middle of the screen for 4000ms
 * with a titlebar reading "My title bar". While the mouse cursor is in the window, it will be
 * of a custom icon that changes color.
 * 2. The window will then close.
 */
class AnimatedMouseCursorProviderSimpleTest extends AnimatedMouseCursorProviderTest {
    public static void main(String[] args) {
        runTest(MouseCursorImplTest::generateRenderablesAndRenderersWithMeshAndShader,
                AnimatedMouseCursorProviderTest::graphicsPreloaderLoadAction,
                AnimatedMouseCursorProviderSimpleTest::actAndCloseAfterSomeTime);
    }

    private static void actAndCloseAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        MouseCursor.setMouseCursor(PROVIDER_ID);

        CheckedExceptionWrapper.sleep(4000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
