package inaugural.soliloquy.graphics.test.display.io;

import inaugural.soliloquy.graphics.api.Constants;
import inaugural.soliloquy.graphics.io.MouseCursorImpl;
import inaugural.soliloquy.graphics.test.display.DisplayTest;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 800x600 pixels for 2000ms with a titlebar reading "New
 *    Window"
 * 2. The mouse cursor will then change to a hand icon for 2000ms
 * 2. The window will then close
 *
 */
class MouseCursorImplSimpleTest extends DisplayTest {
    private static HashMap<String, ProviderAtTime<Long>> _mouseCursors;

    public static void main(String[] args) {
        runTest(MouseCursorImplSimpleTest::generateRenderablesAndRenderersWithMeshAndShader,
                timestamp -> {},
                MouseCursorImplSimpleTest::graphicsPreloaderLoadAction,
                MouseCursorImplSimpleTest::resizeThenCloseAfterSomeTime);
    }

    /** @noinspection rawtypes*/
    private static java.util.List<Renderer> generateRenderablesAndRenderersWithMeshAndShader(
            WindowResolutionManager windowResolutionManager) {
        _mouseCursors = new HashMap<>();
        MouseCursor = new MouseCursorImpl(_mouseCursors, new FakeGlobalClock());
        FrameTimer.ShouldExecuteNextFrame = true;

        return new ArrayList<>();
    }

    protected static void graphicsPreloaderLoadAction() {
        long standardArrowMouseCursor = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        long standardHandMouseCursor = glfwCreateStandardCursor(GLFW_HAND_CURSOR);

        _mouseCursors.put(Constants.STANDARD_ARROW_MOUSE_CURSOR_ID,
                new FakeStaticProviderAtTime<>(standardArrowMouseCursor));
        _mouseCursors.put(Constants.STANDARD_HAND_CURSOR_ID,
                new FakeStaticProviderAtTime<>(standardHandMouseCursor));
    }

    private static void resizeThenCloseAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        CheckedExceptionWrapper.sleep(2000);

        MouseCursor.setMouseCursor(Constants.STANDARD_HAND_CURSOR_ID);

        CheckedExceptionWrapper.sleep(2000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
