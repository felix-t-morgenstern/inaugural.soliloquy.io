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

class WindowManagerImplDisplayChangeTest {

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
        int ms = 4000;

        System.out.println("Starting at windowed fullscreen...");

        CheckedExceptionWrapper.Sleep(ms);

        System.out.println("Setting to windowed, small res...");

        windowResolutionManager.setWindowDisplayMode(WindowDisplayMode.WINDOWED);
        windowResolutionManager.setDimensions(800, 600);

        CheckedExceptionWrapper.Sleep(ms);

        System.out.println("Setting to fullscreen, med res...");

        windowResolutionManager.setWindowDisplayMode(WindowDisplayMode.FULLSCREEN);
        windowResolutionManager.setDimensions(1920, 1080);

        CheckedExceptionWrapper.Sleep(ms);

        System.out.println("Setting to fullscreen, large res...");

        windowResolutionManager.setWindowDisplayMode(WindowDisplayMode.FULLSCREEN);
        windowResolutionManager.setDimensions(3840, 2160);

        CheckedExceptionWrapper.Sleep(ms);

        System.out.println("Setting to windowed, small res...");

        windowResolutionManager.setWindowDisplayMode(WindowDisplayMode.WINDOWED);
        windowResolutionManager.setDimensions(800, 600);

        CheckedExceptionWrapper.Sleep(ms);

        System.out.println("Setting to fullscreen, large res...");

        windowResolutionManager.setWindowDisplayMode(WindowDisplayMode.FULLSCREEN);
        windowResolutionManager.setDimensions(3840, 2160);

        CheckedExceptionWrapper.Sleep(ms);

        System.out.println("Setting to windowed fullscreen...");

        windowResolutionManager.setWindowDisplayMode(WindowDisplayMode.WINDOWED_FULLSCREEN);

        CheckedExceptionWrapper.Sleep(ms);

        System.out.println("Setting to fullscreen, large res...");

        windowResolutionManager.setWindowDisplayMode(WindowDisplayMode.FULLSCREEN);
        windowResolutionManager.setDimensions(3840, 2160);

        CheckedExceptionWrapper.Sleep(ms);

        System.out.println("Setting to windowed, small res...");

        windowResolutionManager.setWindowDisplayMode(WindowDisplayMode.WINDOWED);
        windowResolutionManager.setDimensions(800, 600);

        CheckedExceptionWrapper.Sleep(ms);

        System.out.println("Setting to windowed fullscreen...");

        windowResolutionManager.setWindowDisplayMode(WindowDisplayMode.WINDOWED_FULLSCREEN);

        CheckedExceptionWrapper.Sleep(ms);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
