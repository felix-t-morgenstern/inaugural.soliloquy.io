package inaugural.soliloquy.graphics.test.display;

import inaugural.soliloquy.graphics.api.WindowResolution;
import inaugural.soliloquy.graphics.bootstrap.GraphicsCoreLoopImpl;
import inaugural.soliloquy.graphics.rendering.WindowResolutionManagerImpl;
import inaugural.soliloquy.graphics.test.fakes.FakeFrameTimer;
import inaugural.soliloquy.graphics.test.fakes.FakeGLFWMouseButtonCallback;
import inaugural.soliloquy.graphics.test.fakes.FakeShaderFactory;
import inaugural.soliloquy.graphics.test.fakes.FakeStackRenderer;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.rendering.StackRenderer;
import soliloquy.specs.graphics.rendering.WindowDisplayMode;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

class WindowManagerImplWindowedFullscreenTest {

    public static void main(String[] args) {
        WindowResolutionManagerImpl windowResolutionManager =
                new WindowResolutionManagerImpl(WindowDisplayMode.WINDOWED_FULLSCREEN,
                        WindowResolution.RES_WINDOWED_FULLSCREEN);

        FakeFrameTimer frameTimer = new FakeFrameTimer();
        frameTimer.ShouldExecuteNextFrame = true;
        frameTimer.setPollingInterval(20);

        StackRenderer stackRenderer = new FakeStackRenderer();
        GraphicsCoreLoop graphicsCoreLoop = new GraphicsCoreLoopImpl("My title bar",
                new FakeGLFWMouseButtonCallback(), frameTimer, windowResolutionManager,
                stackRenderer, new FakeShaderFactory(), "");

        graphicsCoreLoop.startup(() ->
                closeAfterSomeTime(graphicsCoreLoop, windowResolutionManager));
    }

    private static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop,
                                           WindowResolutionManager windowResolutionManager) {
        CheckedExceptionWrapper.Sleep(3000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
