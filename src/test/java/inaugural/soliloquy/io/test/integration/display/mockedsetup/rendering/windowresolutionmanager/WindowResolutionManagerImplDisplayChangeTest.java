package inaugural.soliloquy.io.test.integration.display.mockedsetup.rendering.windowresolutionmanager;

import inaugural.soliloquy.io.api.WindowResolution;
import inaugural.soliloquy.io.bootstrap.CoreLoopImpl;
import inaugural.soliloquy.io.graphics.rendering.FrameExecutorImpl;
import inaugural.soliloquy.io.graphics.rendering.MeshImpl;
import inaugural.soliloquy.io.graphics.rendering.WindowResolutionManagerImpl;
import inaugural.soliloquy.io.mouse.MouseListener;
import inaugural.soliloquy.io.test.testdoubles.fakes.*;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.io.bootstrap.CoreLoop;
import soliloquy.specs.io.bootstrap.assetfactories.AudioLoader;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.rendering.*;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;
import soliloquy.specs.io.graphics.rendering.timing.GlobalClock;
import soliloquy.specs.io.input.keyboard.KeyEventListener;
import soliloquy.specs.io.input.mouse.MouseCursor;

import java.util.Set;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.collections.Collections.setOf;
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
        @SuppressWarnings("rawtypes") Set<Renderer> renderersWithMesh = setOf();

        var mockTopLevelComponent = mock(Component.class);
        when(mockTopLevelComponent.contentsRepresentation()).thenReturn(setOf());
        var frameExecutor = new FrameExecutorImpl(new FakeComponentRenderer(), 100);
        frameExecutor.setTopLevelComponent(mockTopLevelComponent);

        @SuppressWarnings("rawtypes") Set<Renderer> renderersWithShader = setOf();
        CoreLoop coreLoop =
                new CoreLoopImpl("My title bar",
                        frameTimer, 20, windowResolutionManager, mockGlobalClock, frameExecutor,
                        new FakeShaderFactory(), renderersWithShader, "_", MeshImpl::new,
                        renderersWithMesh, MESH_DATA, MESH_DATA, new FakeGraphicsPreloader(),
                        mock(AudioLoader.class), setOf(), mapOf(), mapOf(), mapOf(),
                        mock(KeyEventListener.class), mock(MouseCursor.class), mock(MouseListener.class));

        coreLoop.startup(() ->
                closeAfterSomeTime(coreLoop, windowResolutionManager));
    }

    private static void closeAfterSomeTime(CoreLoop coreLoop,
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

        glfwSetWindowShouldClose(coreLoop.windowId(), true);
    }
}
