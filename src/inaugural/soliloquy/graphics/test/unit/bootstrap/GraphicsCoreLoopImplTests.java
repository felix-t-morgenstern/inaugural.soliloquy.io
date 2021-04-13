package inaugural.soliloquy.graphics.test.unit.bootstrap;

import inaugural.soliloquy.graphics.bootstrap.GraphicsCoreLoopImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.rendering.Mesh;
import soliloquy.specs.graphics.rendering.Renderer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.lwjgl.glfw.GLFW.*;

class GraphicsCoreLoopImplTests {
    private final String TITLEBAR = "My title bar";
    private final GLFWMouseButtonCallback MOUSE_BUTTON_CALLBACK =
            new FakeGLFWMouseButtonCallback();
    private final FakeFrameTimer FRAME_TIMER = new FakeFrameTimer();
    private final int FRAME_TIMER_POLLING_INTERVAL = 20;
    private final FakeWindowResolutionManager WINDOW_RESOLUTION_MANAGER =
            new FakeWindowResolutionManager();
    private final FakeStackRenderer STACK_RENDERER = new FakeStackRenderer();
    private final FakeShaderFactory SHADER_FACTORY = new FakeShaderFactory();
    private final String SHADER_FILE_PREFIX = "shaderFilePrefix";
    private final FakeRenderer RENDERER = new FakeRenderer();
    @SuppressWarnings("rawtypes")
    private final Collection<Renderer> RENDERERS_WITH_SHADER = new ArrayList<Renderer>() {{
        add(RENDERER);
    }};
    private final FakeMesh MESH = new FakeMesh();
    private final Function<float[], Function<float[], Mesh>> MESH_FACTORY = f1 -> f2 -> {
        MESH.Vertices = f1;
        MESH.UvCoordinates = f2;
        return MESH;
    };
    @SuppressWarnings("rawtypes")
    private final Collection<Renderer> RENDERERS_WITH_MESH = new ArrayList<Renderer>() {{
        add(RENDERER);
    }};
    private final float[] MESH_VERTICES = new float[] {0.123f};
    private final float[] MESH_UV_COORDINATES = new float[] {0.456f};
    private final FakeGraphicsPreloader GRAPHICS_PRELOADER = new FakeGraphicsPreloader();

    private GraphicsCoreLoop _graphicsCoreLoop;

    @BeforeEach
    void setUp() {
        WINDOW_RESOLUTION_MANAGER.UpdateWindowSizeAndLocationAction = () -> {
            long windowId = glfwCreateWindow(800, 600, "My titlebar", 0, 0);
            glfwMakeContextCurrent(windowId);
            return windowId;
        };
        WINDOW_RESOLUTION_MANAGER.CallUpdateWindowSizeAndLocationOnlyOnce = true;

        _graphicsCoreLoop = new GraphicsCoreLoopImpl(
                TITLEBAR,
                MOUSE_BUTTON_CALLBACK,
                FRAME_TIMER,
                FRAME_TIMER_POLLING_INTERVAL,
                WINDOW_RESOLUTION_MANAGER,
                STACK_RENDERER,
                SHADER_FACTORY,
                RENDERERS_WITH_SHADER,
                SHADER_FILE_PREFIX,
                MESH_FACTORY,
                RENDERERS_WITH_MESH,
                MESH_VERTICES,
                MESH_UV_COORDINATES,
                GRAPHICS_PRELOADER
        );
    }

    @Test
    void testInvalidConstructorParams() {
        assertThrows(IllegalArgumentException.class, () -> new GraphicsCoreLoopImpl(
                null,
                MOUSE_BUTTON_CALLBACK,
                FRAME_TIMER,
                FRAME_TIMER_POLLING_INTERVAL,
                WINDOW_RESOLUTION_MANAGER,
                STACK_RENDERER,
                SHADER_FACTORY,
                RENDERERS_WITH_SHADER,
                SHADER_FILE_PREFIX,
                MESH_FACTORY,
                RENDERERS_WITH_MESH,
                MESH_VERTICES,
                MESH_UV_COORDINATES,
                GRAPHICS_PRELOADER
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsCoreLoopImpl(
                "",
                MOUSE_BUTTON_CALLBACK,
                FRAME_TIMER,
                FRAME_TIMER_POLLING_INTERVAL,
                WINDOW_RESOLUTION_MANAGER,
                STACK_RENDERER,
                SHADER_FACTORY,
                RENDERERS_WITH_SHADER,
                SHADER_FILE_PREFIX,
                MESH_FACTORY,
                RENDERERS_WITH_MESH,
                MESH_VERTICES,
                MESH_UV_COORDINATES,
                GRAPHICS_PRELOADER
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsCoreLoopImpl(
                TITLEBAR,
                null,
                FRAME_TIMER,
                FRAME_TIMER_POLLING_INTERVAL,
                WINDOW_RESOLUTION_MANAGER,
                STACK_RENDERER,
                SHADER_FACTORY,
                RENDERERS_WITH_SHADER,
                SHADER_FILE_PREFIX,
                MESH_FACTORY,
                RENDERERS_WITH_MESH,
                MESH_VERTICES,
                MESH_UV_COORDINATES,
                GRAPHICS_PRELOADER
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsCoreLoopImpl(
                TITLEBAR,
                MOUSE_BUTTON_CALLBACK,
                null,
                FRAME_TIMER_POLLING_INTERVAL,
                WINDOW_RESOLUTION_MANAGER,
                STACK_RENDERER,
                SHADER_FACTORY,
                RENDERERS_WITH_SHADER,
                SHADER_FILE_PREFIX,
                MESH_FACTORY,
                RENDERERS_WITH_MESH,
                MESH_VERTICES,
                MESH_UV_COORDINATES,
                GRAPHICS_PRELOADER
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsCoreLoopImpl(
                TITLEBAR,
                MOUSE_BUTTON_CALLBACK,
                FRAME_TIMER,
                -1,
                WINDOW_RESOLUTION_MANAGER,
                STACK_RENDERER,
                SHADER_FACTORY,
                RENDERERS_WITH_SHADER,
                SHADER_FILE_PREFIX,
                MESH_FACTORY,
                RENDERERS_WITH_MESH,
                MESH_VERTICES,
                MESH_UV_COORDINATES,
                GRAPHICS_PRELOADER
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsCoreLoopImpl(
                TITLEBAR,
                MOUSE_BUTTON_CALLBACK,
                FRAME_TIMER,
                1000,
                WINDOW_RESOLUTION_MANAGER,
                STACK_RENDERER,
                SHADER_FACTORY,
                RENDERERS_WITH_SHADER,
                SHADER_FILE_PREFIX,
                MESH_FACTORY,
                RENDERERS_WITH_MESH,
                MESH_VERTICES,
                MESH_UV_COORDINATES,
                GRAPHICS_PRELOADER
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsCoreLoopImpl(
                TITLEBAR,
                MOUSE_BUTTON_CALLBACK,
                FRAME_TIMER,
                FRAME_TIMER_POLLING_INTERVAL,
                null,
                STACK_RENDERER,
                SHADER_FACTORY,
                RENDERERS_WITH_SHADER,
                SHADER_FILE_PREFIX,
                MESH_FACTORY,
                RENDERERS_WITH_MESH,
                MESH_VERTICES,
                MESH_UV_COORDINATES,
                GRAPHICS_PRELOADER
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsCoreLoopImpl(
                TITLEBAR,
                MOUSE_BUTTON_CALLBACK,
                FRAME_TIMER,
                FRAME_TIMER_POLLING_INTERVAL,
                WINDOW_RESOLUTION_MANAGER,
                null,
                SHADER_FACTORY,
                RENDERERS_WITH_SHADER,
                SHADER_FILE_PREFIX,
                MESH_FACTORY,
                RENDERERS_WITH_MESH,
                MESH_VERTICES,
                MESH_UV_COORDINATES,
                GRAPHICS_PRELOADER
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsCoreLoopImpl(
                TITLEBAR,
                MOUSE_BUTTON_CALLBACK,
                FRAME_TIMER,
                FRAME_TIMER_POLLING_INTERVAL,
                WINDOW_RESOLUTION_MANAGER,
                STACK_RENDERER,
                null,
                RENDERERS_WITH_SHADER,
                SHADER_FILE_PREFIX,
                MESH_FACTORY,
                RENDERERS_WITH_MESH,
                MESH_VERTICES,
                MESH_UV_COORDINATES,
                GRAPHICS_PRELOADER
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsCoreLoopImpl(
                TITLEBAR,
                MOUSE_BUTTON_CALLBACK,
                FRAME_TIMER,
                FRAME_TIMER_POLLING_INTERVAL,
                WINDOW_RESOLUTION_MANAGER,
                STACK_RENDERER,
                SHADER_FACTORY,
                null,
                SHADER_FILE_PREFIX,
                MESH_FACTORY,
                RENDERERS_WITH_MESH,
                MESH_VERTICES,
                MESH_UV_COORDINATES,
                GRAPHICS_PRELOADER
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsCoreLoopImpl(
                TITLEBAR,
                MOUSE_BUTTON_CALLBACK,
                FRAME_TIMER,
                FRAME_TIMER_POLLING_INTERVAL,
                WINDOW_RESOLUTION_MANAGER,
                STACK_RENDERER,
                SHADER_FACTORY,
                RENDERERS_WITH_SHADER,
                null,
                MESH_FACTORY,
                RENDERERS_WITH_MESH,
                MESH_VERTICES,
                MESH_UV_COORDINATES,
                GRAPHICS_PRELOADER
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsCoreLoopImpl(
                TITLEBAR,
                MOUSE_BUTTON_CALLBACK,
                FRAME_TIMER,
                FRAME_TIMER_POLLING_INTERVAL,
                WINDOW_RESOLUTION_MANAGER,
                STACK_RENDERER,
                SHADER_FACTORY,
                RENDERERS_WITH_SHADER,
                "",
                MESH_FACTORY,
                RENDERERS_WITH_MESH,
                MESH_VERTICES,
                MESH_UV_COORDINATES,
                GRAPHICS_PRELOADER
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsCoreLoopImpl(
                TITLEBAR,
                MOUSE_BUTTON_CALLBACK,
                FRAME_TIMER,
                FRAME_TIMER_POLLING_INTERVAL,
                WINDOW_RESOLUTION_MANAGER,
                STACK_RENDERER,
                SHADER_FACTORY,
                RENDERERS_WITH_SHADER,
                SHADER_FILE_PREFIX,
                null,
                RENDERERS_WITH_MESH,
                MESH_VERTICES,
                MESH_UV_COORDINATES,
                GRAPHICS_PRELOADER
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsCoreLoopImpl(
                TITLEBAR,
                MOUSE_BUTTON_CALLBACK,
                FRAME_TIMER,
                FRAME_TIMER_POLLING_INTERVAL,
                WINDOW_RESOLUTION_MANAGER,
                STACK_RENDERER,
                SHADER_FACTORY,
                RENDERERS_WITH_SHADER,
                SHADER_FILE_PREFIX,
                MESH_FACTORY,
                null,
                MESH_VERTICES,
                MESH_UV_COORDINATES,
                GRAPHICS_PRELOADER
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsCoreLoopImpl(
                TITLEBAR,
                MOUSE_BUTTON_CALLBACK,
                FRAME_TIMER,
                FRAME_TIMER_POLLING_INTERVAL,
                WINDOW_RESOLUTION_MANAGER,
                STACK_RENDERER,
                SHADER_FACTORY,
                RENDERERS_WITH_SHADER,
                SHADER_FILE_PREFIX,
                MESH_FACTORY,
                RENDERERS_WITH_MESH,
                null,
                MESH_UV_COORDINATES,
                GRAPHICS_PRELOADER
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsCoreLoopImpl(
                TITLEBAR,
                MOUSE_BUTTON_CALLBACK,
                FRAME_TIMER,
                FRAME_TIMER_POLLING_INTERVAL,
                WINDOW_RESOLUTION_MANAGER,
                STACK_RENDERER,
                SHADER_FACTORY,
                RENDERERS_WITH_SHADER,
                SHADER_FILE_PREFIX,
                MESH_FACTORY,
                RENDERERS_WITH_MESH,
                MESH_VERTICES,
                null,
                GRAPHICS_PRELOADER
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsCoreLoopImpl(
                TITLEBAR,
                MOUSE_BUTTON_CALLBACK,
                FRAME_TIMER,
                FRAME_TIMER_POLLING_INTERVAL,
                WINDOW_RESOLUTION_MANAGER,
                STACK_RENDERER,
                SHADER_FACTORY,
                RENDERERS_WITH_SHADER,
                SHADER_FILE_PREFIX,
                MESH_FACTORY,
                RENDERERS_WITH_MESH,
                MESH_VERTICES,
                MESH_UV_COORDINATES,
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

    @Test
    void testMeshAndShaderPassedToRenderersAndGraphicsPreloaderCalled() {
        FRAME_TIMER.ShouldExecuteNextFrame = false;

        _graphicsCoreLoop.startup(() -> closeAfterSomeTime(_graphicsCoreLoop));

        assertSame(MESH, RENDERER.Mesh);
        assertSame(SHADER_FACTORY.MostRecentlyCreated, RENDERER.Shader);
        assertTrue(GRAPHICS_PRELOADER.LoadCalled);
    }

    private static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        CheckedExceptionWrapper.sleep(100);

//        while (graphicsCoreLoop.windowId() <= 0)
//        {
//            CheckedExceptionWrapper.Sleep(100);
//        }

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
