package inaugural.soliloquy.graphics.test.display;

import inaugural.soliloquy.graphics.api.WindowResolution;
import inaugural.soliloquy.graphics.bootstrap.GraphicsCoreLoopImpl;
import inaugural.soliloquy.graphics.rendering.WindowResolutionManagerImpl;
import inaugural.soliloquy.graphics.test.fakes.*;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.rendering.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

class WindowManagerImplDisplayChangeTest {
    private final static float[] MESH_DATA =
            new float[] {0f, 1f, 1f, 1f, 1f, 0f, 1f, 0f, 0f, 0f, 0f, 1f};

    public static void main(String[] args) {
        WindowResolutionManagerImpl windowResolutionManager =
                new WindowResolutionManagerImpl(WindowDisplayMode.WINDOWED_FULLSCREEN,
                        WindowResolution.RES_WINDOWED_FULLSCREEN);

        FakeFrameTimer frameTimer = new FakeFrameTimer();
        frameTimer.ShouldExecuteNextFrame = true;
        frameTimer.setPollingInterval(20);
        Function<float[], Function<float[],Mesh>> meshFactory = f1 -> f2 -> null;
        @SuppressWarnings("rawtypes") Collection<Renderer> renderersWithMesh = new ArrayList<>();

        StackRenderer stackRenderer = new FakeStackRenderer();
        @SuppressWarnings("rawtypes") Collection<Renderer> renderersWithShader = new ArrayList<>();
        GraphicsCoreLoop graphicsCoreLoop = new GraphicsCoreLoopImpl("My title bar",
                new FakeGLFWMouseButtonCallback(), frameTimer, windowResolutionManager,
                stackRenderer, new FakeShaderFactory(), renderersWithShader, "_", meshFactory,
                renderersWithMesh, MESH_DATA, MESH_DATA, new FakeGraphicsPreloader());

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
