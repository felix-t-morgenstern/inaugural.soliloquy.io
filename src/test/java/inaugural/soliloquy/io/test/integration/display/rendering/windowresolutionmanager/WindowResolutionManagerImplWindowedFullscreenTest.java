package inaugural.soliloquy.io.test.integration.display.rendering.windowresolutionmanager;

import inaugural.soliloquy.io.api.WindowResolution;
import inaugural.soliloquy.io.graphics.bootstrap.GraphicsCoreLoopImpl;
import inaugural.soliloquy.io.graphics.rendering.FrameExecutorImpl;
import inaugural.soliloquy.io.graphics.rendering.WindowResolutionManagerImpl;
import inaugural.soliloquy.io.mouse.MouseListener;
import inaugural.soliloquy.io.test.testdoubles.fakes.*;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.io.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.io.graphics.rendering.Mesh;
import soliloquy.specs.io.graphics.rendering.WindowDisplayMode;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;
import soliloquy.specs.io.graphics.rendering.timing.GlobalClock;
import soliloquy.specs.io.input.mouse.MouseCursor;
import soliloquy.specs.ui.Component;

import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

import static inaugural.soliloquy.tools.collections.Collections.setOf;
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
        @SuppressWarnings("rawtypes") Set<Renderer> renderersWithMesh = setOf();


        var mockTopLevelComponent = mock(Component.class);
        when(mockTopLevelComponent.contents()).thenReturn(setOf());
        var frameExecutor = new FrameExecutorImpl(new FakeComponentRenderer(), 100);
        frameExecutor.setTopLevelComponent(mockTopLevelComponent);

        @SuppressWarnings("rawtypes") Set<Renderer> renderersWithShader = setOf();
        @SuppressWarnings("unchecked") var graphicsCoreLoop =
                new GraphicsCoreLoopImpl("My title bar", frameTimer, 20, windowResolutionManager,
                        mockGlobalClock, frameExecutor, new FakeShaderFactory(),
                        renderersWithShader, "_", mock(BiFunction.class), renderersWithMesh, MESH_DATA,
                        MESH_DATA, new FakeGraphicsPreloader(), mock(MouseCursor.class),
                        mock(MouseListener.class));

        graphicsCoreLoop.startup(() -> closeAfterSomeTime(graphicsCoreLoop));
    }

    private static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        CheckedExceptionWrapper.sleep(3000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
