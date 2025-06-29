package inaugural.soliloquy.io.test.display.bootstrap;

import inaugural.soliloquy.io.graphics.bootstrap.GraphicsCoreLoopImpl;
import inaugural.soliloquy.io.graphics.rendering.FrameExecutorImpl;
import inaugural.soliloquy.io.graphics.rendering.GlobalClockImpl;
import inaugural.soliloquy.io.test.testdoubles.fakes.*;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.io.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.io.mouse.MouseListener;
import soliloquy.specs.io.graphics.rendering.Mesh;
import soliloquy.specs.io.graphics.rendering.RenderableStack;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import java.util.Collection;
import java.util.function.Function;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static org.lwjgl.glfw.GLFW.*;
import static org.mockito.Mockito.mock;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 800x600 pixels for 2000ms with a titlebar reading "New
 * Window"
 * 2. The window will then close
 */
class GraphicsCoreLoopImplSimpleTest {
    private final static float[] MESH_DATA =
            new float[]{0f, 1f, 1f, 1f, 1f, 0f, 1f, 0f, 0f, 0f, 0f, 1f};

    static FakeWindowResolutionManager WindowManager;

    public static void main(String[] args) {
        var frameTimer = new FakeFrameTimer();
        var frameExecutor =
                new FrameExecutorImpl(mock(RenderableStack.class), new FakeStackRenderer(), 100);
        @SuppressWarnings("rawtypes") Collection<Renderer> renderersWithShader = listOf();
        WindowManager = new FakeWindowResolutionManager();
        Function<float[], Function<float[], Mesh>> meshFactory = _ -> _ -> new FakeMesh();
        @SuppressWarnings("rawtypes") Collection<Renderer> renderersWithMesh = listOf();


        GraphicsCoreLoop graphicsCoreLoop =
                new GraphicsCoreLoopImpl("New window", new FakeGLFWMouseButtonCallback(),
                        frameTimer, 20, WindowManager, new GlobalClockImpl(), frameExecutor,
                        new FakeShaderFactory(), renderersWithShader, "_", meshFactory,
                        renderersWithMesh, MESH_DATA, MESH_DATA, new FakeGraphicsPreloader(),
                        new FakeMouseCursor(), mock(MouseListener.class));

        WindowManager.CallUpdateWindowSizeAndLocationOnlyOnce = true;
        WindowManager.UpdateWindowSizeAndLocationAction = () -> {
            var windowId = glfwCreateWindow(800, 600, "My titlebar", 0, 0);
            glfwShowWindow(windowId);
            glfwMakeContextCurrent(windowId);
            return windowId;
        };

        graphicsCoreLoop.startup(() -> resizeThenCloseAfterSomeTime(graphicsCoreLoop));
    }

    private static void resizeThenCloseAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        CheckedExceptionWrapper.sleep(2000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
