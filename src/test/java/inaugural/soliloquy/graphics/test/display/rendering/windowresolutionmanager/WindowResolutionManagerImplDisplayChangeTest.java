package inaugural.soliloquy.graphics.test.display.rendering.windowresolutionmanager;

import inaugural.soliloquy.graphics.api.WindowResolution;
import inaugural.soliloquy.graphics.bootstrap.GraphicsCoreLoopImpl;
import inaugural.soliloquy.graphics.rendering.FrameExecutorImpl;
import inaugural.soliloquy.graphics.rendering.WindowResolutionManagerImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.io.MouseListener;
import soliloquy.specs.graphics.rendering.*;
import soliloquy.specs.graphics.rendering.renderers.Renderer;
import soliloquy.specs.graphics.rendering.timing.GlobalClock;

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
 * Broadly, this test goes through a transition between all types of display modes.
 *
 * 1. This test will start by displaying a window in windowed fullscreen mode for 4000ms.
 * 2. The window will switch to windowed mode, with a resolution of 800x600px, for 4000ms.
 * 3. The window will switch to fullscreen mode, changing the screen to have a resolution of
 * 1920x1080px, for 4000ms.
 * 4. The window will switch to fullscreen mode, changing the screen to have a resolution of
 * 3840x2160px, for 4000ms.
 * 5. The window will switch to windowed mode, with a resolution of 800x600px, for 4000ms.
 * 6. The window will switch to fullscreen mode, changing the screen to have a resolution of
 * 3840x2160px, for 4000ms.
 * 7. The window will switch to windowed fullscreen for 4000ms.
 * 8. The window will switch to fullscreen mode, changing the screen to have a resolution of
 * 3840x2160px, for 4000ms.
 * 9. The window will switch to windowed mode, with a resolution of 800x600px, for 4000ms.
 * 10. The window will switch to windowed fullscreen for 4000ms.
 * 11. The window will close.
 */
class WindowResolutionManagerImplDisplayChangeTest {
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
        Function<float[], Function<float[], Mesh>> meshFactory = f1 -> f2 -> null;
        @SuppressWarnings("rawtypes") Collection<Renderer> renderersWithMesh = listOf();

        FrameExecutor frameExecutor =
                new FrameExecutorImpl(mock(RenderableStack.class),
                        new FakeStackRenderer(), 100);

        @SuppressWarnings("rawtypes") Collection<Renderer> renderersWithShader = listOf();
        GraphicsCoreLoop graphicsCoreLoop =
                new GraphicsCoreLoopImpl("My title bar", new FakeGLFWMouseButtonCallback(),
                        frameTimer, 20, windowResolutionManager, mockGlobalClock, frameExecutor,
                        new FakeShaderFactory(), renderersWithShader, "_", meshFactory,
                        renderersWithMesh, MESH_DATA, MESH_DATA, new FakeGraphicsPreloader(),
                        new FakeMouseCursor(), mock(MouseListener.class));

        graphicsCoreLoop.startup(() ->
                closeAfterSomeTime(graphicsCoreLoop, windowResolutionManager));
    }

    private static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop,
                                           WindowResolutionManager windowResolutionManager) {
        var ms = 4000;

        System.out.println("Starting at windowed fullscreen...");

        CheckedExceptionWrapper.sleep(ms);

        System.out.println("Setting to windowed, small res...");

        windowResolutionManager.setWindowDisplayMode(WindowDisplayMode.WINDOWED);
        windowResolutionManager.updateDimensions(800, 600);

        CheckedExceptionWrapper.sleep(ms);

        System.out.println("Setting to fullscreen, med res...");

        windowResolutionManager.setWindowDisplayMode(WindowDisplayMode.FULLSCREEN);
        windowResolutionManager.updateDimensions(1920, 1080);

        CheckedExceptionWrapper.sleep(ms);

        System.out.println("Setting to fullscreen, large res...");

        windowResolutionManager.setWindowDisplayMode(WindowDisplayMode.FULLSCREEN);
        windowResolutionManager.updateDimensions(3840, 2160);

        CheckedExceptionWrapper.sleep(ms);

        System.out.println("Setting to windowed, small res...");

        windowResolutionManager.setWindowDisplayMode(WindowDisplayMode.WINDOWED);
        windowResolutionManager.updateDimensions(800, 600);

        CheckedExceptionWrapper.sleep(ms);

        System.out.println("Setting to fullscreen, large res...");

        windowResolutionManager.setWindowDisplayMode(WindowDisplayMode.FULLSCREEN);
        windowResolutionManager.updateDimensions(3840, 2160);

        CheckedExceptionWrapper.sleep(ms);

        System.out.println("Setting to windowed fullscreen...");

        windowResolutionManager.setWindowDisplayMode(WindowDisplayMode.WINDOWED_FULLSCREEN);

        CheckedExceptionWrapper.sleep(ms);

        System.out.println("Setting to fullscreen, large res...");

        windowResolutionManager.setWindowDisplayMode(WindowDisplayMode.FULLSCREEN);
        windowResolutionManager.updateDimensions(3840, 2160);

        CheckedExceptionWrapper.sleep(ms);

        System.out.println("Setting to windowed, small res...");

        windowResolutionManager.setWindowDisplayMode(WindowDisplayMode.WINDOWED);
        windowResolutionManager.updateDimensions(800, 600);

        CheckedExceptionWrapper.sleep(ms);

        System.out.println("Setting to windowed fullscreen...");

        windowResolutionManager.setWindowDisplayMode(WindowDisplayMode.WINDOWED_FULLSCREEN);

        CheckedExceptionWrapper.sleep(ms);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
