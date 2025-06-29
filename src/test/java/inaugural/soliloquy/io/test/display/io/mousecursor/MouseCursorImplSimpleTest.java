package inaugural.soliloquy.io.test.display.io.mousecursor;

import inaugural.soliloquy.io.api.Constants;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeStaticProvider;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.io.graphics.bootstrap.GraphicsCoreLoop;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 800x600 pixels for 2000ms with a titlebar reading "New
 * Window"
 * 2. The mouse cursor will then change to a hand icon for 2000ms
 * 2. The window will then close
 */
class MouseCursorImplSimpleTest extends MouseCursorImplTest {
    public static void main(String[] args) {
        runTest(MouseCursorImplTest::generateRenderablesAndRenderersWithMeshAndShader,
                MouseCursorImplSimpleTest::graphicsPreloaderLoadAction,
                MouseCursorImplSimpleTest::actAndCloseAfterSomeTime);
    }

    protected static void graphicsPreloaderLoadAction() {
        var standardArrowMouseCursor = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        var standardHandMouseCursor = glfwCreateStandardCursor(GLFW_HAND_CURSOR);

        MouseCursorProviders.put(Constants.STANDARD_ARROW_MOUSE_CURSOR_ID,
                new FakeStaticProvider<>(standardArrowMouseCursor));
        MouseCursorProviders.put(Constants.STANDARD_HAND_CURSOR_ID,
                new FakeStaticProvider<>(standardHandMouseCursor));
    }

    private static void actAndCloseAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        CheckedExceptionWrapper.sleep(2000);

        MouseCursor.setMouseCursor(Constants.STANDARD_HAND_CURSOR_ID);

        CheckedExceptionWrapper.sleep(2000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
