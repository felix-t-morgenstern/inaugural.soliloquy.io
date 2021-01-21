package inaugural.soliloquy.graphics.test.display;

import inaugural.soliloquy.graphics.api.WindowResolution;
import inaugural.soliloquy.graphics.bootstrap.GraphicsCoreLoopImpl;
import inaugural.soliloquy.graphics.test.fakes.FakeFrameTimer;
import inaugural.soliloquy.graphics.test.fakes.FakeGLFWMouseButtonCallback;
import inaugural.soliloquy.graphics.test.fakes.FakeStackRenderer;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.rendering.FrameTimer;
import soliloquy.specs.graphics.rendering.StackRenderer;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

class GraphicsCoreLoopImplSimpleTest {
    public static void main(String[] args) {
        FrameTimer frameTimer = new FakeFrameTimer();
        StackRenderer stackRenderer = new FakeStackRenderer();

        GraphicsCoreLoop graphicsCoreLoop = new GraphicsCoreLoopImpl(WindowResolution.RES_800x600,
                "New window", new FakeGLFWMouseButtonCallback(), frameTimer, stackRenderer);

        graphicsCoreLoop.startup(GraphicsCoreLoopImplSimpleTest::closeWindowAfterSomeTime);
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
