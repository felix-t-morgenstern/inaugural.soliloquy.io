package inaugural.soliloquy.graphics.test.display.rendering.windowresolutionmanager;

import inaugural.soliloquy.graphics.api.WindowResolution;
import inaugural.soliloquy.graphics.bootstrap.GraphicsCoreLoopImpl;
import inaugural.soliloquy.graphics.rendering.FrameExecutorImpl;
import inaugural.soliloquy.graphics.rendering.WindowResolutionManagerImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.io.MouseListener;
import soliloquy.specs.graphics.rendering.FrameExecutor;
import soliloquy.specs.graphics.rendering.Mesh;
import soliloquy.specs.graphics.rendering.RenderableStack;
import soliloquy.specs.graphics.rendering.WindowDisplayMode;
import soliloquy.specs.graphics.rendering.renderers.Renderer;
import soliloquy.specs.graphics.rendering.timing.GlobalClock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

import static inaugural.soliloquy.tools.generic.Archetypes.generateSimpleArchetype;
import static inaugural.soliloquy.tools.random.Random.randomLong;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window in windowed fullscreen mode, setting the window to take up
 * the entirety of the screen, without changing the screen's resolution. The window will stay up
 * for 3000ms.
 * 2. The window will then close
 */
class WindowResolutionManagerImplWindowedFullscreenTest {
    private final static float[] MESH_DATA =
            new float[]{0f, 1f, 1f, 1f, 1f, 0f, 1f, 0f, 0f, 0f, 0f, 1f};

    public static void main(String[] args) {
        WindowResolutionManagerImpl windowResolutionManager = new WindowResolutionManagerImpl(
                WindowDisplayMode.WINDOWED_FULLSCREEN, WindowResolution.RES_WINDOWED_FULLSCREEN);

        long timestamp = randomLong();
        GlobalClock mockGlobalClock = mock(GlobalClock.class);
        when(mockGlobalClock.globalTimestamp()).thenReturn(timestamp);
        FakeFrameTimer frameTimer = new FakeFrameTimer();
        frameTimer.ShouldExecuteNextFrame = true;
        Function<float[], Function<float[], Mesh>> meshFactory = f1 -> f2 -> new FakeMesh();
        @SuppressWarnings("rawtypes") Collection<Renderer> renderersWithMesh = new ArrayList<>();

        FrameExecutor frameExecutor = new FrameExecutorImpl(generateSimpleArchetype(RenderableStack.class), new FakeStackRenderer(), 100);

        @SuppressWarnings("rawtypes") Collection<Renderer> renderersWithShader = new ArrayList<>();
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
