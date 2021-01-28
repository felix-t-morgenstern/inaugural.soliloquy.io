package inaugural.soliloquy.graphics.test.display;

import inaugural.soliloquy.graphics.bootstrap.GraphicsCoreLoopImpl;
import inaugural.soliloquy.graphics.test.fakes.FakeFrameTimer;
import inaugural.soliloquy.graphics.test.fakes.FakeGLFWMouseButtonCallback;
import inaugural.soliloquy.graphics.test.fakes.FakeStackRenderer;
import inaugural.soliloquy.graphics.test.fakes.FakeWindowResolutionManager;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.rendering.StackRenderer;

import static org.lwjgl.glfw.GLFW.*;

class GraphicsCoreLoopImplSimpleTest {
    static FakeWindowResolutionManager WindowManager;

    public static void main(String[] args) {
        FakeFrameTimer frameTimer = new FakeFrameTimer();
        StackRenderer stackRenderer = new FakeStackRenderer();
        WindowManager = new FakeWindowResolutionManager();

        frameTimer.ShouldExecuteNextFrame = true;
        frameTimer.setPollingInterval(10);

        GraphicsCoreLoop graphicsCoreLoop = new GraphicsCoreLoopImpl("New window",
                new FakeGLFWMouseButtonCallback(), frameTimer, WindowManager, stackRenderer);

        WindowManager.CallUpdateWindowSizeAndLocationOnlyOnce = true;
        WindowManager.UpdateWindowSizeAndLocationAction = () -> {
            long windowId = glfwCreateWindow(800, 600, "My titlebar", 0, 0);
            glfwShowWindow(windowId);
            glfwMakeContextCurrent(windowId);
            return windowId;
        };

        graphicsCoreLoop.startup(() -> resizeThenCloseAfterSomeTime(graphicsCoreLoop));
    }

    private static void resizeThenCloseAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        CheckedExceptionWrapper.Sleep(2000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
