package inaugural.soliloquy.graphics.test.display;

import inaugural.soliloquy.graphics.bootstrap.GraphicsCoreLoopImpl;
import inaugural.soliloquy.graphics.test.fakes.FakeFrameTimer;
import inaugural.soliloquy.graphics.test.fakes.FakeGLFWMouseButtonCallback;
import inaugural.soliloquy.graphics.test.fakes.FakeStackRenderer;
import inaugural.soliloquy.graphics.test.fakes.FakeWindowManager;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.rendering.StackRenderer;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSize;

class GraphicsCoreLoopImplSimpleWindowedTest {
    static FakeWindowManager WindowManager;

    public static void main(String[] args) {
        FakeFrameTimer frameTimer = new FakeFrameTimer();
        StackRenderer stackRenderer = new FakeStackRenderer();
        WindowManager = new FakeWindowManager();

        frameTimer.ShouldExecuteNextFrame = true;

        GraphicsCoreLoop graphicsCoreLoop = new GraphicsCoreLoopImpl("New window",
                new FakeGLFWMouseButtonCallback(), frameTimer, WindowManager, stackRenderer);

        graphicsCoreLoop.startup(
                GraphicsCoreLoopImplSimpleWindowedTest::resizeThenCloseAfterSomeTime);
    }

    private static void resizeThenCloseAfterSomeTime(long window) {
        WindowManager.UpdateWindowSizeAndLocationAction = () -> {
            glfwSetWindowSize(window, 800, 600);
        };

        final int msBeforeClose = 2000;

        try {
            Thread.sleep(msBeforeClose);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        glfwSetWindowShouldClose(window, true);
    }
}
