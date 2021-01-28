package inaugural.soliloquy.graphics.test.unit;

import inaugural.soliloquy.graphics.bootstrap.GraphicsCoreLoopImpl;
import inaugural.soliloquy.graphics.test.fakes.FakeFrameTimer;
import inaugural.soliloquy.graphics.test.fakes.FakeGLFWMouseButtonCallback;
import inaugural.soliloquy.graphics.test.fakes.FakeStackRenderer;
import inaugural.soliloquy.graphics.test.fakes.FakeWindowResolutionManager;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;

import static org.junit.jupiter.api.Assertions.*;
import static org.lwjgl.glfw.GLFW.*;

class GraphicsCoreLoopImplTests {
    private String _titlebar;
    private GLFWMouseButtonCallback _mouseButtonCallback;
    private FakeFrameTimer _frameTimer;
    private FakeWindowResolutionManager _windowManager;
    private FakeStackRenderer _stackRenderer;

    private GraphicsCoreLoop _graphicsCoreLoop;

    @BeforeEach
    void setUp() {
        _titlebar = "My title bar";
        _mouseButtonCallback = new FakeGLFWMouseButtonCallback();
        _frameTimer = new FakeFrameTimer();
        _windowManager = new FakeWindowResolutionManager();
        _stackRenderer = new FakeStackRenderer();

        _windowManager.UpdateWindowSizeAndLocationAction = () -> {
            long windowId = glfwCreateWindow(800, 600, "My titlebar", 0, 0);
            glfwMakeContextCurrent(windowId);
            return windowId;
        };
        _windowManager.CallUpdateWindowSizeAndLocationOnlyOnce = true;

        _frameTimer.setPollingInterval(20);

        _graphicsCoreLoop = new GraphicsCoreLoopImpl(
                _titlebar,
                _mouseButtonCallback,
                _frameTimer,
                _windowManager,
                _stackRenderer
        );
    }

    @Test
    void testInvalidConstructorParams() {
        assertThrows(IllegalArgumentException.class, () -> new GraphicsCoreLoopImpl(
                null,
                _mouseButtonCallback,
                _frameTimer,
                _windowManager,
                _stackRenderer
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsCoreLoopImpl(
                "",
                _mouseButtonCallback,
                _frameTimer,
                _windowManager,
                _stackRenderer
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsCoreLoopImpl(
                _titlebar,
                null,
                _frameTimer,
                _windowManager,
                _stackRenderer
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsCoreLoopImpl(
                _titlebar,
                _mouseButtonCallback,
                null,
                _windowManager,
                _stackRenderer
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsCoreLoopImpl(
                _titlebar,
                _mouseButtonCallback,
                _frameTimer,
                null,
                _stackRenderer
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsCoreLoopImpl(
                _titlebar,
                _mouseButtonCallback,
                _frameTimer,
                _windowManager,
                null
        ));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(GraphicsCoreLoop.class.getCanonicalName(),
                _graphicsCoreLoop.getInterfaceName());
    }

    @Test
    void testGetTitlebar() {
        assertEquals(_titlebar, _graphicsCoreLoop.getTitlebar());
    }

    @Test
    void testWhenFrameTimerDoesNotPermitNewFrames() {
        _frameTimer.ShouldExecuteNextFrame = false;

        _graphicsCoreLoop.startup(() -> closeAfterSomeTime(_graphicsCoreLoop));

        // Should be 1, since it is called to create the initial window
        assertEquals(1, _windowManager.NumberOfTimesUpdateWindowSizeAndLocationActionCalled);
        assertEquals(0, _stackRenderer.NumberOfTimesRenderCalled);
    }

    @Test
    void testWhenFrameTimerPermitsNewFrames() {
        _frameTimer.ShouldExecuteNextFrame = true;

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
        assertTrue(_windowManager.NumberOfTimesUpdateWindowSizeAndLocationActionCalled > 1);
        assertTrue(_stackRenderer.NumberOfTimesRenderCalled > 1);
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
