package inaugural.soliloquy.io.test.integration.display.mockedsetup.rendering.windowresolutionmanager;

import inaugural.soliloquy.io.api.WindowResolution;
import inaugural.soliloquy.io.graphics.bootstrap.GraphicsCoreLoopImpl;
import inaugural.soliloquy.io.graphics.rendering.FrameExecutorImpl;
import inaugural.soliloquy.io.graphics.rendering.WindowResolutionManagerImpl;
import inaugural.soliloquy.io.mouse.MouseListener;
import inaugural.soliloquy.io.test.testdoubles.fakes.*;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import inaugural.soliloquy.tools.collections.Collections;
import soliloquy.specs.io.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.rendering.WindowDisplayMode;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;
import soliloquy.specs.io.graphics.rendering.timing.GlobalClock;
import soliloquy.specs.io.input.mouse.MouseCursor;

import java.util.function.BiFunction;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.setOf;
import static inaugural.soliloquy.tools.random.Random.randomLong;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window in windowed mode, with a resolution of 1024x768 pixels, for
 * 3000ms. This window will appear in the center of the screen, and will have a titlebar reading
 * "My title bar".
 * 2. The window will then close
 */
class WindowResolutionManagerImplWindowedTest {
    private final static float[] MESH_DATA =
            new float[]{0f, 1f, 1f, 1f, 1f, 0f, 1f, 0f, 0f, 0f, 0f, 1f};

    public static void main(String[] args) {
        var windowManager = new WindowResolutionManagerImpl(WindowDisplayMode.WINDOWED,
                WindowResolution.RES_1024x768);

        var timestamp = randomLong();
        var mockGlobalClock = mock(GlobalClock.class);
        when(mockGlobalClock.globalTimestamp()).thenReturn(timestamp);
        var frameTimer = new FakeFrameTimer();
        frameTimer.ShouldExecuteNextFrame = true;
        //noinspection rawtypes
        var renderersWithMesh = Collections.<Renderer>setOf();

        var mockTopLevelComponent = mock(Component.class);
        when(mockTopLevelComponent.contents()).thenReturn(setOf());
        var frameExecutor = new FrameExecutorImpl(new FakeComponentRenderer(), 100);
        frameExecutor.setTopLevelComponent(mockTopLevelComponent);

        //noinspection rawtypes
        var renderersWithShader = Collections.<Renderer>setOf();
        @SuppressWarnings("unchecked") var graphicsCoreLoop =
                new GraphicsCoreLoopImpl("My title bar", frameTimer, 20, windowManager,
                        mockGlobalClock, frameExecutor, new FakeShaderFactory(),
                        renderersWithShader, "_", mock(BiFunction.class), renderersWithMesh, MESH_DATA,
                        MESH_DATA, new FakeGraphicsPreloader(), mock(MouseCursor.class),
                        mock(MouseListener.class));

        graphicsCoreLoop.startup(() -> closeAfterSomeTime(graphicsCoreLoop));
    }

    private static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        CheckedExceptionWrapper.sleep(2000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
