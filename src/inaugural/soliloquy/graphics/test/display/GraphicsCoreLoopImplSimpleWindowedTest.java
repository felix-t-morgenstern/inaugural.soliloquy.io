package inaugural.soliloquy.graphics.test.display;

import inaugural.soliloquy.graphics.bootstrap.GraphicsCoreLoopImpl;
import inaugural.soliloquy.graphics.test.fakes.FakeFrameTimer;
import inaugural.soliloquy.graphics.test.fakes.FakeGLFWMouseButtonCallback;
import inaugural.soliloquy.graphics.test.fakes.FakeStackRenderer;
import inaugural.soliloquy.graphics.test.fakes.FakeWindowManager;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.rendering.StackRenderer;
import soliloquy.specs.graphics.rendering.WindowDisplayMode;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

class GraphicsCoreLoopImplSimpleWindowedTest {
    static FakeWindowManager WindowManager;

    public static void main(String[] args) {
        FakeFrameTimer frameTimer = new FakeFrameTimer();
        StackRenderer stackRenderer = new FakeStackRenderer();
        WindowManager = new FakeWindowManager();

        frameTimer.ShouldExecuteNextFrame = true;

        WindowManager.setWindowDisplayMode(WindowDisplayMode.WINDOWED);
        WindowManager.setDimensions(800, 600);

        GraphicsCoreLoop graphicsCoreLoop = new GraphicsCoreLoopImpl("New window",
                new FakeGLFWMouseButtonCallback(), frameTimer, WindowManager, stackRenderer);

        graphicsCoreLoop.startup(GraphicsCoreLoopImplSimpleWindowedTest::closeWindowAfterSomeTime);
    }

    private static void closeWindowAfterSomeTime(long window) {
        final int msBeforeClose = 2000;

        try {
            Thread.sleep(msBeforeClose);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        glfwSetWindowShouldClose(window, true);
    }
}
