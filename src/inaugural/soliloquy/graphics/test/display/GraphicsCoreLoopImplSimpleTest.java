package inaugural.soliloquy.graphics.test.display;

import inaugural.soliloquy.graphics.bootstrap.GraphicsCoreLoopImpl;
import inaugural.soliloquy.graphics.test.fakes.*;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.rendering.Mesh;
import soliloquy.specs.graphics.rendering.Renderer;
import soliloquy.specs.graphics.rendering.StackRenderer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

import static org.lwjgl.glfw.GLFW.*;

class GraphicsCoreLoopImplSimpleTest {
    private final static float[] MESH_DATA =
            new float[] {0f, 1f, 1f, 1f, 1f, 0f, 1f, 0f, 0f, 0f, 0f, 1f};

    static FakeWindowResolutionManager WindowManager;

    public static void main(String[] args) {
        FakeFrameTimer frameTimer = new FakeFrameTimer();
        StackRenderer stackRenderer = new FakeStackRenderer();
        Collection<Renderer> renderersWithShader = new ArrayList<>();
        WindowManager = new FakeWindowResolutionManager();
        Function<float[], Function<float[],Mesh>> meshFactory = f1 -> f2 -> null;
        Collection<Renderer> renderersWithMesh = new ArrayList<>();

        frameTimer.ShouldExecuteNextFrame = true;
        frameTimer.setPollingInterval(10);

        GraphicsCoreLoop graphicsCoreLoop = new GraphicsCoreLoopImpl("New window",
                new FakeGLFWMouseButtonCallback(), frameTimer, WindowManager, stackRenderer,
                new FakeShaderFactory(), renderersWithShader, "_", meshFactory, renderersWithMesh,
                MESH_DATA, MESH_DATA, new FakeGraphicsPreloader());

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
