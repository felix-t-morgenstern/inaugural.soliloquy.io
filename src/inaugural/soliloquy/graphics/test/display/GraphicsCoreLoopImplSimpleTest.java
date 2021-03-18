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

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 800x600 pixels for 2000ms with a titlebar reading "New
 *    Window"
 * 2. The window will then close
 *
 */
class GraphicsCoreLoopImplSimpleTest {
    private final static float[] MESH_DATA =
            new float[] {0f, 1f, 1f, 1f, 1f, 0f, 1f, 0f, 0f, 0f, 0f, 1f};

    static FakeWindowResolutionManager WindowManager;

    public static void main(String[] args) {
        FakeFrameTimer frameTimer = new FakeFrameTimer();
        StackRenderer stackRenderer = new FakeStackRenderer();
        Collection<Renderer> renderersWithShader = new ArrayList<>();
        WindowManager = new FakeWindowResolutionManager();
        Function<float[], Function<float[],Mesh>> meshFactory = f1 -> f2 -> new FakeMesh();
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
