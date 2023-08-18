package inaugural.soliloquy.graphics.bootstrap;

import inaugural.soliloquy.graphics.api.Constants;
import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.opengl.GL;
import soliloquy.specs.common.valueobjects.Pair;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.bootstrap.GraphicsPreloader;
import soliloquy.specs.graphics.io.MouseCursor;
import soliloquy.specs.graphics.io.MouseListener;
import soliloquy.specs.graphics.rendering.FrameExecutor;
import soliloquy.specs.graphics.rendering.Mesh;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.graphics.rendering.factories.ShaderFactory;
import soliloquy.specs.graphics.rendering.renderers.Renderer;
import soliloquy.specs.graphics.rendering.timing.FrameTimer;
import soliloquy.specs.graphics.rendering.timing.GlobalClock;

import java.util.Collection;
import java.util.HashMap;
import java.util.function.Function;

import static inaugural.soliloquy.graphics.api.Constants.ALL_SUPPORTED_MOUSE_BUTTONS;
import static inaugural.soliloquy.graphics.api.Constants.MS_PER_SECOND;
import static inaugural.soliloquy.tools.valueobjects.Pair.pairOf;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class GraphicsCoreLoopImpl implements GraphicsCoreLoop {
    private final String TITLEBAR;
    private final GLFWMouseButtonCallback MOUSE_BUTTON_CALLBACK;
    private final FrameTimer FRAME_TIMER;
    private final int FRAME_TIMER_POLLING_INTERVAL;
    private final WindowResolutionManager WINDOW_RESOLUTION_MANAGER;
    private final GlobalClock GLOBAL_CLOCK;
    private final FrameExecutor FRAME_EXECUTOR;
    private final ShaderFactory SHADER_FACTORY;
    @SuppressWarnings("rawtypes")
    private final Collection<Renderer> RENDERERS_WITH_SHADER;
    private final String SHADER_FILENAME_PREFIX;
    private final Function<float[], Function<float[], Mesh>> MESH_FACTORY;
    @SuppressWarnings("rawtypes")
    private final Collection<Renderer> RENDERERS_WITH_MESH;
    private final float[] MESH_VERTICES;
    private final float[] MESH_UV_COORDINATES;
    private final GraphicsPreloader GRAPHICS_PRELOADER;
    private final MouseCursor MOUSE_CURSOR;
    private final MouseListener MOUSE_LISTENER;

    private final HashMap<Integer, Boolean> MOUSE_BUTTON_STATES;

    private long window = Long.MIN_VALUE;
    private Vertex screenMouseLocation;

    public GraphicsCoreLoopImpl(String titlebar,
                                GLFWMouseButtonCallback mouseButtonCallback,
                                FrameTimer frameTimer,
                                int frameTimerPollingInterval,
                                WindowResolutionManager windowResolutionManager,
                                GlobalClock globalClock,
                                FrameExecutor frameExecutor,
                                ShaderFactory shaderFactory,
                                @SuppressWarnings("rawtypes") Collection<Renderer>
                                        renderersWithShader,
                                String shaderFilenamePrefix,
                                Function<float[], Function<float[], Mesh>> meshFactory,
                                @SuppressWarnings("rawtypes") Collection<Renderer>
                                        renderersWithMesh,
                                float[] meshVertices,
                                float[] meshUvCoordinates,
                                GraphicsPreloader graphicsPreloader,
                                MouseCursor mouseCursor,
                                MouseListener mouseListener) {
        TITLEBAR = Check.ifNullOrEmpty(titlebar, "titlebar");
        MOUSE_BUTTON_CALLBACK = Check.ifNull(mouseButtonCallback, "mouseButtonCallback");
        FRAME_TIMER = Check.ifNull(frameTimer, "frameTimer");
        FRAME_TIMER_POLLING_INTERVAL = Check.throwOnLtValue(
                Check.throwOnGteValue(frameTimerPollingInterval, MS_PER_SECOND,
                        "frameTimerPollingInterval"),
                0, "frameTimerPollingInterval"
        );
        WINDOW_RESOLUTION_MANAGER = Check.ifNull(windowResolutionManager,
                "windowResolutionManager");
        GLOBAL_CLOCK = Check.ifNull(globalClock, "globalClock");
        FRAME_EXECUTOR = Check.ifNull(frameExecutor, "frameExecutor");
        SHADER_FACTORY = Check.ifNull(shaderFactory, "shaderFactory");
        RENDERERS_WITH_SHADER = Check.ifNull(renderersWithShader, "renderersWithShader");
        SHADER_FILENAME_PREFIX = Check.ifNullOrEmpty(shaderFilenamePrefix, "shaderFilenamePrefix");
        MESH_FACTORY = Check.ifNull(meshFactory, "meshFactory");
        RENDERERS_WITH_MESH = Check.ifNull(renderersWithMesh, "renderersWithMesh");
        MESH_VERTICES = Check.ifNull(meshVertices, "meshVertices");
        MESH_UV_COORDINATES = Check.ifNull(meshUvCoordinates, "meshUvCoordinates");
        GRAPHICS_PRELOADER = Check.ifNull(graphicsPreloader, "graphicsPreloader");
        MOUSE_CURSOR = Check.ifNull(mouseCursor, "mouseCursor");
        MOUSE_LISTENER = Check.ifNull(mouseListener, "mouseListener");

        MOUSE_BUTTON_STATES = new HashMap<>();
        for (var button : ALL_SUPPORTED_MOUSE_BUTTONS) {
            MOUSE_BUTTON_STATES.put(button, false);
        }
    }

    @Override
    public void startup(Runnable gameThread) throws IllegalArgumentException {
        if (!glfwInit()) {
            throw new RuntimeException("GLFW failed to initialize");
        }

        updateWindow();

        GL.createCapabilities();

        glClearColor(0, 0, 0, 0);

        glEnable(GL_LINE_STIPPLE);
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_FASTEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDepthMask(false);
        glEnable(GL_TEXTURE_2D);

        glfwSetMouseButtonCallback(window, MOUSE_BUTTON_CALLBACK);

        var shader = SHADER_FACTORY.make(SHADER_FILENAME_PREFIX);
        shader.bind();

        RENDERERS_WITH_SHADER.forEach(renderer -> renderer.setShader(shader));

        var mesh = MESH_FACTORY.apply(MESH_VERTICES).apply(MESH_UV_COORDINATES);

        RENDERERS_WITH_MESH.forEach(renderer -> renderer.setMesh(mesh));

        mesh.bind();

        // TODO: Consider test for whether GraphicsPreloader.load was called _before_ the first
        //  invocation of FrameTimer.shouldExecuteNextFrame
        GRAPHICS_PRELOADER.load();

        new Thread(gameThread).start();

        while (!glfwWindowShouldClose(window)) {
            MOUSE_CURSOR.updateCursor(window);

            if (FRAME_TIMER.shouldExecuteNextFrame()) {
                glfwPollEvents();

                updateWindow();

                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

                var frameTimestamp = GLOBAL_CLOCK.globalTimestamp();

                readMouseButtonStates();

                if (screenMouseLocation != null) {
                    MOUSE_LISTENER.registerMousePositionAndButtonStates(screenMouseLocation,
                            MOUSE_BUTTON_STATES, frameTimestamp);
                }

                FRAME_EXECUTOR.execute(frameTimestamp);

                glfwSwapBuffers(window);
            }

            if (FRAME_TIMER_POLLING_INTERVAL > 0) {
                CheckedExceptionWrapper.sleep(FRAME_TIMER_POLLING_INTERVAL);
            }
        }

        glfwTerminate();
    }

    private void updateWindow() {
        var prevWindow = window;
        window = WINDOW_RESOLUTION_MANAGER.updateWindowSizeAndLocation(window, TITLEBAR);
        if (window == 0) {
            throw new IllegalStateException("Failed to create window");
        }
        if (window != prevWindow) {
            updateWindowDimensionsInResolutionManager();
            setNewMouseCallbacks();
        }
    }

    // NB: Getting the screen dimensions for each callback ensures that mouse cursor position
    // will remain accurately depicted when the resolution changes, but may be a target for
    // performance enhancements.
    private void setNewMouseCallbacks() {
        //noinspection resource
        glfwSetCursorPosCallback(window, (window, xPixel, yPixel) -> {
            var windowDimensions = updateWindowDimensionsInResolutionManager();
            var width = windowDimensions.item1();
            var height = windowDimensions.item2();

            var x = (float) xPixel / width;
            var y = (float) yPixel / height;

            screenMouseLocation = Vertex.of(x, y);
        });
    }

    private void readMouseButtonStates() {
        for (var mouseButton : Constants.ALL_SUPPORTED_MOUSE_BUTTONS) {
            MOUSE_BUTTON_STATES.put(mouseButton,
                    glfwGetMouseButton(window, mouseButton) == GLFW_PRESS);
        }
    }

    private Pair<Integer, Integer> updateWindowDimensionsInResolutionManager() {
        var widthBuffer = BufferUtils.createByteBuffer(4);
        var heightBuffer = BufferUtils.createByteBuffer(4);
        glfwGetWindowSize(window, widthBuffer.asIntBuffer(), heightBuffer.asIntBuffer());
        var width = widthBuffer.getInt(0);
        var height = heightBuffer.getInt(0);

        WINDOW_RESOLUTION_MANAGER.updateDimensions(width, height);

        return pairOf(width, height);
    }

    @Override

    public long windowId() throws UnsupportedOperationException {
        return window;
    }

    @Override
    public String getTitlebar() {
        return TITLEBAR;
    }

    @Override
    public String getInterfaceName() {
        return GraphicsCoreLoop.class.getCanonicalName();
    }
}
