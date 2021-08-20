package inaugural.soliloquy.graphics.test.display.rendering.renderers.stackrenderer;

import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 1920x1080 pixels in the middle of the screen for 3000ms
 *    with a titlebar reading "My title bar". The window will contain three Sprites: A fiery axe,
 *    an evil sword, and a nature axe. The fiery axe will be in the lower-left, the evil sword will
 *    be in the center, and the nature axe will be in the upper-right. The evil sword will be
 *    displayed above the fiery axe, and the nature axe above the evil sword.
 * 2. The window will then close.
 *
 */
class StackRendererSimpleTest extends StackRendererTest {
    public static void main(String[] args) {
        runTest(StackRendererSimpleTest::closeAfterSomeTime);
    }

    public static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        CheckedExceptionWrapper.sleep(3000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
