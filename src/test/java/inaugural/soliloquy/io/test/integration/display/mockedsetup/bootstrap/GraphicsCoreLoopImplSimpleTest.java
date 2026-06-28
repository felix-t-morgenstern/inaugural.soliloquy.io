package inaugural.soliloquy.io.test.integration.display.mockedsetup.bootstrap;

import inaugural.soliloquy.io.bootstrap.CoreLoopImpl;
import inaugural.soliloquy.io.graphics.renderables.ComponentImpl;
import inaugural.soliloquy.io.graphics.rendering.FrameExecutorImpl;
import inaugural.soliloquy.io.graphics.rendering.GlobalClockImpl;
import inaugural.soliloquy.io.mouse.MouseImpl;
import inaugural.soliloquy.io.mouse.MouseListener;
import inaugural.soliloquy.io.test.testdoubles.fakes.*;
import inaugural.soliloquy.tools.exception.CheckedExceptionWrapper;
import org.apache.commons.lang3.function.TriConsumer;
import soliloquy.specs.io.bootstrap.CoreLoop;
import soliloquy.specs.io.bootstrap.assetfactories.AudioLoader;
import soliloquy.specs.io.graphics.rendering.Mesh;
import soliloquy.specs.io.input.keyboard.KeyEventListener;

import java.util.function.BiFunction;
import java.util.function.Consumer;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.collections.Collections.setOf;
import static org.lwjgl.glfw.GLFW.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        var mockTopLevelComponent = mock(ComponentImpl.class);
        when(mockTopLevelComponent.contentsRepresentation()).thenReturn(setOf());
        var frameExecutor = new FrameExecutorImpl(new FakeComponentRenderer(), 100, () -> {});
        frameExecutor.setTopLevelComponent(mockTopLevelComponent);
        WindowManager = new FakeWindowResolutionManager();
        BiFunction<float[], float[], Mesh> meshFactory = (_, _) -> mock(Mesh.class);


        var coreLoop =
                new CoreLoopImpl("New window",
                        frameTimer, 20, WindowManager, new GlobalClockImpl(), frameExecutor,
                        new FakeShaderFactory(), setOf(), "_", meshFactory,
                        setOf(), MESH_DATA, MESH_DATA, new FakeGraphicsPreloader(),
                        mock(AudioLoader.class), setOf(), mapOf(), mapOf(), mapOf(),
                        mock(KeyEventListener.class), mock(Consumer.class), mock(Consumer.class),
                        mock(TriConsumer.class));

        WindowManager.CallUpdateWindowSizeAndLocationOnlyOnce = true;
        WindowManager.UpdateWindowSizeAndLocationAction = () -> {
            var windowId = glfwCreateWindow(800, 600, "My titlebar", 0, 0);
            glfwShowWindow(windowId);
            glfwMakeContextCurrent(windowId);
            return windowId;
        };

        coreLoop.startup(() -> resizeThenCloseAfterSomeTime(coreLoop));
    }

    private static void resizeThenCloseAfterSomeTime(CoreLoop coreLoop) {
        CheckedExceptionWrapper.sleep(2000);

        glfwSetWindowShouldClose(coreLoop.windowId(), true);
    }
}
