package inaugural.soliloquy.graphics.test.unit;

import inaugural.soliloquy.graphics.bootstrap.GraphicsCoreLoopImpl;
import inaugural.soliloquy.graphics.test.fakes.FakeFrameTimer;
import inaugural.soliloquy.graphics.test.fakes.FakeGLFWMouseButtonCallback;
import inaugural.soliloquy.graphics.test.fakes.FakeStackRenderer;
import inaugural.soliloquy.graphics.test.fakes.FakeWindowManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;

import static org.junit.jupiter.api.Assertions.*;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

class GraphicsCoreLoopImplTests {
    private String _titlebar;
    private GLFWMouseButtonCallback _mouseButtonCallback;
    private FakeFrameTimer _frameTimer;
    private FakeWindowManager _windowManager;
    private FakeStackRenderer _stackRenderer;

    private GraphicsCoreLoop _graphicsCoreLoop;

    @BeforeEach
    void setUp() {
        _titlebar = "My title bar";
        _mouseButtonCallback = new FakeGLFWMouseButtonCallback();
        _frameTimer = new FakeFrameTimer();
        _windowManager = new FakeWindowManager();
        _stackRenderer = new FakeStackRenderer();

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
    void testStartupWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _graphicsCoreLoop.startup(null));
    }

    @Test
    void testWhenFrameTimerDoesNotPermitNewFrames() {
        _frameTimer.ShouldExecuteNextFrame = false;

        _graphicsCoreLoop.startup(GraphicsCoreLoopImplTests::closeAfterSomeTime);

        assertEquals(0, _windowManager.NumberOfTimesUpdateWindowSizeAndLocationActionCalled);
        assertEquals(0, _stackRenderer.NumberOfTimesRenderCalled);
    }

    @Test
    void testWhenFrameTimerPermitsNewFrames() {
        _frameTimer.ShouldExecuteNextFrame = true;

        _graphicsCoreLoop.startup(GraphicsCoreLoopImplTests::closeAfterSomeTime);

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

    private static void closeAfterSomeTime(long window) {
        final int msBeforeClose = 50;

        try {
            Thread.sleep(msBeforeClose);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        glfwSetWindowShouldClose(window, true);
    }
}
