package inaugural.soliloquy.io.test.display.rendering.windowresolutionmanager;

import inaugural.soliloquy.io.api.WindowResolution;
import inaugural.soliloquy.io.graphics.bootstrap.GraphicsCoreLoopImpl;
import inaugural.soliloquy.io.graphics.rendering.FrameExecutorImpl;
import inaugural.soliloquy.io.graphics.rendering.WindowResolutionManagerImpl;
import inaugural.soliloquy.io.test.testdoubles.fakes.*;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.io.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.io.mouse.MouseListener;
import soliloquy.specs.io.graphics.rendering.FrameExecutor;
import soliloquy.specs.io.graphics.rendering.Mesh;
import soliloquy.specs.io.graphics.rendering.RenderableStack;
import soliloquy.specs.io.graphics.rendering.WindowDisplayMode;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;
import soliloquy.specs.io.graphics.rendering.timing.GlobalClock;

import java.util.Collection;
import java.util.function.Function;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.random.Random.randomLong;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window in fullscreen mode, changing the resolution of the monitor to
 * 3840x2160, for 3000ms.
 * 2. The window will then close
 */
class WindowResolutionManagerImplFullscreenTest {
    private final static float[] MESH_DATA =
            new float[]{0f, 1f, 1f, 1f, 1f, 0f, 1f, 0f, 0f, 0f, 0f, 1f};

    public static void main(String[] args) {
        WindowResolutionManagerImpl windowResolutionManager =
                new WindowResolutionManagerImpl(WindowDisplayMode.FULLSCREEN,
                        WindowResolution.RES_3840x2160);

        long timestamp = randomLong();
        GlobalClock mockGlobalClock = mock(GlobalClock.class);
        when(mockGlobalClock.globalTimestamp()).thenReturn(timestamp);
        FakeFrameTimer frameTimer = new FakeFrameTimer();
        frameTimer.ShouldExecuteNextFrame = true;
        Function<float[], Function<float[], Mesh>> meshFactory = f1 -> f2 -> new FakeMesh();
        @SuppressWarnings("rawtypes") Collection<Renderer> renderersWithMesh = listOf();

        FrameExecutor frameExecutor = new FrameExecutorImpl(mock(RenderableStack.class), new FakeStackRenderer(), 100);

        @SuppressWarnings("rawtypes") Collection<Renderer> renderersWithShader = listOf();
        GraphicsCoreLoop graphicsCoreLoop =
                new GraphicsCoreLoopImpl("My title bar", new FakeGLFWMouseButtonCallback(),
                        frameTimer, 20, windowResolutionManager, mockGlobalClock, frameExecutor,
                        new FakeShaderFactory(), renderersWithShader, "_", meshFactory,
                        renderersWithMesh, MESH_DATA, MESH_DATA, new FakeGraphicsPreloader(),
                        new FakeMouseCursor(), mock(MouseListener.class));

        graphicsCoreLoop.startup(() -> closeAfterSomeTime(graphicsCoreLoop));
    }

    private static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        CheckedExceptionWrapper.sleep(3000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
