package inaugural.soliloquy.graphics.test.display.rendering.renderers.stackrenderer;

import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeFloatBox;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 1920x1080 pixels in the middle of the screen for 3000ms
 * with a titlebar reading "My title bar". The window will contain three Sprites: A fiery axe,
 * an evil sword, and a nature axe. The fiery axe will be in the lower-left, the evil sword will
 * be in the center, and the nature axe will be in the upper-right. The evil sword will be
 * displayed above the fiery axe, and the nature axe above the evil sword.
 * 2. The window displayed will be clipped so that only the portions of the window within the
 * left-most and top-most 62.5% of the window are displayed. This will last for 1000ms.
 * 3. The window displayed will be clipped so that only the portions of the window within the
 * right-most and top-most 62.5% of the window are displayed. This will last for 1000ms.
 * 4. The window displayed will be clipped so that only the portions of the window within the
 * right-most and bottom-most 62.5% of the window are displayed. This will last for 1000ms.
 * 5. The window displayed will be clipped so that only the portions of the window within the
 * left-most and bottom-most 62.5% of the window are displayed. This will last for 1000ms.
 * 6. The entirety of the window will be displayed again for 1000ms.
 * 7. The window will then close.
 */
class StackRendererRenderingBoundariesTest extends StackRendererTest {
    public static void main(String[] args) {
        runTest(StackRendererRenderingBoundariesTest::closeAfterSomeTime);
    }

    public static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        CheckedExceptionWrapper.sleep(1000);

        RENDERING_BOUNDARIES.CurrentBoundaries = new FakeFloatBox(0.0f, 0.0f, 0.625f, 0.625f);

        CheckedExceptionWrapper.sleep(1000);

        RENDERING_BOUNDARIES.CurrentBoundaries = new FakeFloatBox(0.375f, 0.0f, 1.0f, 0.625f);

        CheckedExceptionWrapper.sleep(1000);

        RENDERING_BOUNDARIES.CurrentBoundaries = new FakeFloatBox(0.375f, 0.375f, 1.0f, 1.0f);

        CheckedExceptionWrapper.sleep(1000);

        RENDERING_BOUNDARIES.CurrentBoundaries = new FakeFloatBox(0.0f, 0.375f, 0.625f, 1.0f);

        CheckedExceptionWrapper.sleep(1000);

        RENDERING_BOUNDARIES.CurrentBoundaries = new FakeFloatBox(0.0f, 0.0f, 1.0f, 1.0f);

        CheckedExceptionWrapper.sleep(1000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
