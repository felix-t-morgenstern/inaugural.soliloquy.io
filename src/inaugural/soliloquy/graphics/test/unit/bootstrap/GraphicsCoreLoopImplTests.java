package inaugural.soliloquy.graphics.test.unit.bootstrap;

import inaugural.soliloquy.graphics.bootstrap.GraphicsCoreLoopImpl;
import inaugural.soliloquy.graphics.test.fakes.*;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;

import static org.junit.jupiter.api.Assertions.*;
import static org.lwjgl.glfw.GLFW.*;

class GraphicsCoreLoopImplTests {
    private final String TITLEBAR = "My title bar";
    private final GLFWMouseButtonCallback MOUSE_BUTTON_CALLBACK =
            new FakeGLFWMouseButtonCallback();
    private final FakeFrameTimer FRAME_TIMER = new FakeFrameTimer();
    private final FakeWindowResolutionManager WINDOW_RESOLUTION_MANAGER =
            new FakeWindowResolutionManager();
    private final FakeStackRenderer STACK_RENDERER = new FakeStackRenderer();
    private final FakeShaderFactory SHADER_FACTORY = new FakeShaderFactory();
    private final String SHADER_FILE_PREFIX = "shaderFilePrefix";

    private GraphicsCoreLoop _graphicsCoreLoop;

    @BeforeEach
    void setUp() {
        WINDOW_RESOLUTION_MANAGER.UpdateWindowSizeAndLocationAction = () -> {
            long windowId = glfwCreateWindow(800, 600, "My titlebar", 0, 0);
            glfwMakeContextCurrent(windowId);
            return windowId;
        };
        WINDOW_RESOLUTION_MANAGER.CallUpdateWindowSizeAndLocationOnlyOnce = true;

        FRAME_TIMER.setPollingInterval(20);

        _graphicsCoreLoop = new GraphicsCoreLoopImpl(
                TITLEBAR,
                MOUSE_BUTTON_CALLBACK,
                FRAME_TIMER,
                WINDOW_RESOLUTION_MANAGER,
                STACK_RENDERER,
                SHADER_FACTORY,
                SHADER_FILE_PREFIX
        );
    }

    @Test
    void testInvalidConstructorParams() {
        assertThrows(IllegalArgumentException.class, () -> new GraphicsCoreLoopImpl(
                null,
                MOUSE_BUTTON_CALLBACK,
                FRAME_TIMER,
                WINDOW_RESOLUTION_MANAGER,
                STACK_RENDERER,
                SHADER_FACTORY,
                SHADER_FILE_PREFIX
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsCoreLoopImpl(
                "",
                MOUSE_BUTTON_CALLBACK,
                FRAME_TIMER,
                WINDOW_RESOLUTION_MANAGER,
                STACK_RENDERER,
                SHADER_FACTORY,
                SHADER_FILE_PREFIX
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsCoreLoopImpl(
                TITLEBAR,
                null,
                FRAME_TIMER,
                WINDOW_RESOLUTION_MANAGER,
                STACK_RENDERER,
                SHADER_FACTORY,
                SHADER_FILE_PREFIX
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsCoreLoopImpl(
                TITLEBAR,
                MOUSE_BUTTON_CALLBACK,
                null,
                WINDOW_RESOLUTION_MANAGER,
                STACK_RENDERER,
                SHADER_FACTORY,
                SHADER_FILE_PREFIX
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsCoreLoopImpl(
                TITLEBAR,
                MOUSE_BUTTON_CALLBACK,
                FRAME_TIMER,
                null,
                STACK_RENDERER,
                SHADER_FACTORY,
                SHADER_FILE_PREFIX
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsCoreLoopImpl(
                TITLEBAR,
                MOUSE_BUTTON_CALLBACK,
                FRAME_TIMER,
                WINDOW_RESOLUTION_MANAGER,
                null,
                SHADER_FACTORY,
                SHADER_FILE_PREFIX
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsCoreLoopImpl(
                TITLEBAR,
                MOUSE_BUTTON_CALLBACK,
                FRAME_TIMER,
                WINDOW_RESOLUTION_MANAGER,
                STACK_RENDERER,
                null,
                SHADER_FILE_PREFIX
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsCoreLoopImpl(
                TITLEBAR,
                MOUSE_BUTTON_CALLBACK,
                FRAME_TIMER,
                WINDOW_RESOLUTION_MANAGER,
                STACK_RENDERER,
                SHADER_FACTORY,
                null
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsCoreLoopImpl(
                TITLEBAR,
                MOUSE_BUTTON_CALLBACK,
                FRAME_TIMER,
                WINDOW_RESOLUTION_MANAGER,
                STACK_RENDERER,
                SHADER_FACTORY,
                ""
        ));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(GraphicsCoreLoop.class.getCanonicalName(),
                _graphicsCoreLoop.getInterfaceName());
    }

    @Test
    void testGetTitlebar() {
        assertEquals(TITLEBAR, _graphicsCoreLoop.getTitlebar());
    }

    @Test
    void testWhenFrameTimerDoesNotPermitNewFrames() {
        FRAME_TIMER.ShouldExecuteNextFrame = false;

        _graphicsCoreLoop.startup(() -> closeAfterSomeTime(_graphicsCoreLoop));

        // Should be 1, since it is called to create the initial window
        assertEquals(1, WINDOW_RESOLUTION_MANAGER.NumberOfTimesUpdateWindowSizeAndLocationActionCalled);
        assertEquals(0, STACK_RENDERER.NumberOfTimesRenderCalled);
    }

    @Test
    void testWhenFrameTimerPermitsNewFrames() {
        FRAME_TIMER.ShouldExecuteNextFrame = true;

        _graphicsCoreLoop.startup(() -> closeAfterSomeTime(_graphicsCoreLoop));

        // NB: This test is *somewhat* indeterminate, since the polling interval used from
        //     FrameTimer does *not* guarantee polling at *precisely* that rate; instead, it only
        //     specifies the delay between either the last time a frame was rendered, or the last
        //     time FrameRender instructed GraphicsCoreLoop to not render a frame, and the next
        //     time GraphicsCoreLoop asks FrameTimer whether to render the next frame. This test
        //     should no longer be indeterminate if GraphicsCoreLoop is refactored to treat the
        //     polling interval as an actual interval, rather than merely a delay; however, since
        //     the interval should be very small in practice (e.g. 2-5ms), this slight
        //     indeterminacy should not radically affect performance.
        assertTrue(WINDOW_RESOLUTION_MANAGER.NumberOfTimesUpdateWindowSizeAndLocationActionCalled > 1);
        assertTrue(STACK_RENDERER.NumberOfTimesRenderCalled > 1);
    }

    private static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        CheckedExceptionWrapper.Sleep(100);

//        while (graphicsCoreLoop.windowId() <= 0)
//        {
//            CheckedExceptionWrapper.Sleep(100);
//        }

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
