package inaugural.soliloquy.io.test.integration.display.mockedsetup.rendering.windowresolutionmanager;

import inaugural.soliloquy.io.api.WindowResolution;
import inaugural.soliloquy.io.bootstrap.CoreLoopImpl;
import inaugural.soliloquy.io.graphics.renderables.ComponentImpl;
import inaugural.soliloquy.io.graphics.rendering.FrameExecutorImpl;
import inaugural.soliloquy.io.graphics.rendering.WindowResolutionManagerImpl;
import inaugural.soliloquy.io.mouse.MouseListener;
import inaugural.soliloquy.io.test.testdoubles.fakes.*;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.io.bootstrap.CoreLoop;
import soliloquy.specs.io.bootstrap.assetfactories.AudioLoader;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.rendering.WindowDisplayMode;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;
import soliloquy.specs.io.graphics.rendering.timing.GlobalClock;
import soliloquy.specs.io.input.keyboard.KeyEventListener;
import soliloquy.specs.io.input.mouse.MouseCursor;

import java.util.Set;
import java.util.function.BiFunction;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.collections.Collections.setOf;
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
        @SuppressWarnings("rawtypes") Set<Renderer> renderersWithMesh = setOf();

        var mockTopLevelComponent = mock(ComponentImpl.class);
        when(mockTopLevelComponent.contentsRepresentation()).thenReturn(setOf());
        var frameExecutor = new FrameExecutorImpl(new FakeComponentRenderer(), 100, () -> {});
        frameExecutor.setTopLevelComponent(mockTopLevelComponent);

        @SuppressWarnings("rawtypes") Set<Renderer> renderersWithShader = setOf();
        @SuppressWarnings("unchecked") var coreLoop =
                new CoreLoopImpl("My title bar", frameTimer, 20, windowResolutionManager,
                        mockGlobalClock, frameExecutor, new FakeShaderFactory(),
                        renderersWithShader, "_", mock(BiFunction.class), renderersWithMesh,
                        MESH_DATA, MESH_DATA, new FakeGraphicsPreloader(), mock(AudioLoader.class),
                        setOf(), mapOf(), mapOf(), mapOf(), mock(KeyEventListener.class),
                        mock(MouseCursor.class), mock(MouseListener.class));

        coreLoop.startup(() -> closeAfterSomeTime(coreLoop));
    }

    private static void closeAfterSomeTime(CoreLoop coreLoop) {
        CheckedExceptionWrapper.sleep(3000);

        glfwSetWindowShouldClose(coreLoop.windowId(), true);
    }
}
