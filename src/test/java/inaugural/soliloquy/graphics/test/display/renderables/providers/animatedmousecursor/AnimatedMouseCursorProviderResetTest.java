package inaugural.soliloquy.graphics.test.display.renderables.providers.animatedmousecursor;

import inaugural.soliloquy.graphics.test.display.io.mousecursor.MouseCursorImplTest;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 1920x1080 pixels in the middle of the screen
 * with a titlebar reading "My title bar". While the mouse cursor is in the window, it will be
 * of a custom icon that changes color.
 * 2. The mouse cursor will be animated for 3000ms. Then, the mouse cursor will reset to its
 * original color. Then, it will continue being animated for 3000ms.
 * 2. The window will then close.
 */
class AnimatedMouseCursorProviderResetTest extends AnimatedMouseCursorProviderTest {
    public static void main(String[] args) {
        runTest(MouseCursorImplTest::generateRenderablesAndRenderersWithMeshAndShader,
                timestamp -> {},
                AnimatedMouseCursorProviderTest::graphicsPreloaderLoadAction,
                AnimatedMouseCursorProviderResetTest::actAndCloseAfterSomeTime);
    }

    private static void actAndCloseAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        MouseCursor.setMouseCursor(PROVIDER_ID);
        _animatedMouseCursorProvider.reset(GLOBAL_CLOCK.globalTimestamp());

        CheckedExceptionWrapper.sleep(1000);

        _animatedMouseCursorProvider.reset(GLOBAL_CLOCK.globalTimestamp());

        CheckedExceptionWrapper.sleep(3000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
