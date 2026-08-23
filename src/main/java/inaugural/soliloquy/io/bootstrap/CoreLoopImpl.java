package inaugural.soliloquy.io.bootstrap;

import inaugural.soliloquy.io.api.Constants;
import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.exception.CheckedExceptionWrapper;
import org.apache.commons.lang3.function.TriConsumer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import soliloquy.specs.common.valueobjects.Pair;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.bootstrap.CoreLoop;
import soliloquy.specs.io.bootstrap.GraphicsPreloader;
import soliloquy.specs.io.bootstrap.assetfactories.AudioLoader;
import soliloquy.specs.io.graphics.rendering.FrameExecutor;
import soliloquy.specs.io.graphics.rendering.Mesh;
import soliloquy.specs.io.graphics.rendering.Shader;
import soliloquy.specs.io.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.io.graphics.rendering.factories.ShaderFactory;
import soliloquy.specs.io.graphics.rendering.timing.FrameTimer;
import soliloquy.specs.io.graphics.rendering.timing.GlobalClock;
import soliloquy.specs.io.input.keyboard.KeyEventListener;

import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import static inaugural.soliloquy.io.api.Constants.ALL_SUPPORTED_MOUSE_BUTTONS;
import static inaugural.soliloquy.io.api.Constants.MS_PER_SECOND;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

public class CoreLoopImpl implements CoreLoop {
    private final FrameTimer FRAME_TIMER;
    private final int FRAME_TIMER_POLLING_INTERVAL;
    private final WindowResolutionManager WINDOW_RESOLUTION_MANAGER;
    private final GlobalClock GLOBAL_CLOCK;
    private final FrameExecutor FRAME_EXECUTOR;
    private final ShaderFactory SHADER_FACTORY;
    private final Set<Consumer<Shader>> SHADER_SUBSCRIBERS;
    private final String SHADER_FILENAME_PREFIX;
    private final BiFunction<float[], float[], Mesh> MESH_FACTORY;
    private final Set<Consumer<Mesh>> MESH_SUBSCRIBERS;
    private final float[] MESH_VERTICES;
    private final float[] MESH_UV_COORDINATES;
    private final GraphicsPreloader GRAPHICS_PRELOADER;
    private final AudioLoader AUDIO_LOADER;
    private final Set<String> AUDIO_REL_DIRS;
    private final Map<String, String> IDS_FOR_FILENAMES;
    private final Map<String, Integer> DEFAULT_LOOP_STOP_MS_BY_ID;
    private final Map<String, Integer> DEFAULT_LOOP_RESTART_MS_BY_ID;
    private final KeyEventListener KEY_EVENT_LISTENER;
    private final Consumer<Long> UPDATE_MOUSE_CURSOR;
    private final Consumer<Vertex> UPDATE_MOST_RECENT_MOUSE_LOC;
    private final TriConsumer<Vertex, Map<Integer, Boolean>, Long>
            DETERMINE_MOUSE_EVENTS_AND_ACT;

    private final Map<Integer, Boolean> MOUSE_BUTTON_STATES;

    private long window = Long.MIN_VALUE;
    private String titlebar;
    private Vertex screenMouseLocation;

    public CoreLoopImpl(
            String titlebar,
            FrameTimer frameTimer,
            int frameTimerPollingInterval,
            WindowResolutionManager windowResolutionManager,
            GlobalClock globalClock,
            FrameExecutor frameExecutor,
            ShaderFactory shaderFactory,
            Set<Consumer<Shader>> shaderSubscribers,
            String shaderFilenamePrefix,
            BiFunction<float[], float[], Mesh> meshFactory,
            Set<Consumer<Mesh>> meshSubscribers,
            float[] meshVertices,
            float[] meshUvCoordinates,
            GraphicsPreloader graphicsPreloader,
            AudioLoader audioLoader,
            Set<String> audioRelDirs,
            Map<String, String> idsForFilenames,
            Map<String, Integer> defaultLoopStopMsById,
            Map<String, Integer> defaultLoopRestartMsById,
            KeyEventListener keyEventListener,
            Consumer<Long> updateMouseCursor,
            Consumer<Vertex> updateMostRecentMouseLoc,
            TriConsumer<Vertex, Map<Integer, Boolean>, Long> determineMouseEventsAndAct) {
        this.titlebar = Check.ifNullOrEmpty(titlebar, "titlebar");
        FRAME_TIMER = Check.ifNull(frameTimer, "frameTimer");
        FRAME_TIMER_POLLING_INTERVAL =
                Check.throwOnGteValue(frameTimerPollingInterval, MS_PER_SECOND,
                        "frameTimerPollingInterval");
        WINDOW_RESOLUTION_MANAGER = Check.ifNull(windowResolutionManager,
                "windowResolutionManager");
        GLOBAL_CLOCK = Check.ifNull(globalClock, "globalClock");
        FRAME_EXECUTOR = Check.ifNull(frameExecutor, "frameExecutor");
        SHADER_FACTORY = Check.ifNull(shaderFactory, "shaderFactory");
        SHADER_SUBSCRIBERS = Check.ifNull(shaderSubscribers, "shaderSubscribers");
        SHADER_FILENAME_PREFIX = Check.ifNullOrEmpty(shaderFilenamePrefix, "shaderFilenamePrefix");
        MESH_FACTORY = Check.ifNull(meshFactory, "meshFactory");
        MESH_SUBSCRIBERS = Check.ifNull(meshSubscribers, "meshSubscribers");
        MESH_VERTICES = Check.ifNull(meshVertices, "meshVertices");
        MESH_UV_COORDINATES = Check.ifNull(meshUvCoordinates, "meshUvCoordinates");
        GRAPHICS_PRELOADER = Check.ifNull(graphicsPreloader, "graphicsPreloader");
        AUDIO_LOADER = Check.ifNull(audioLoader, "audioLoader");
        AUDIO_REL_DIRS = Check.ifNull(audioRelDirs, "audioRelDirs");
        IDS_FOR_FILENAMES = Check.ifNull(idsForFilenames, "idsForFilenames");
        DEFAULT_LOOP_STOP_MS_BY_ID = Check.ifNull(defaultLoopStopMsById, "defaultLoopStopMsById");
        DEFAULT_LOOP_RESTART_MS_BY_ID =
                Check.ifNull(defaultLoopRestartMsById, "defaultLoopRestartMsById");
        KEY_EVENT_LISTENER = Check.ifNull(keyEventListener, "keyEventListener");
        UPDATE_MOUSE_CURSOR = Check.ifNull(updateMouseCursor, "updateMouseCursor");
        UPDATE_MOST_RECENT_MOUSE_LOC =
                Check.ifNull(updateMostRecentMouseLoc, "updateMostRecentMouseLoc");
        DETERMINE_MOUSE_EVENTS_AND_ACT =
                Check.ifNull(determineMouseEventsAndAct, "determineMouseEventsAndAct");

        MOUSE_BUTTON_STATES = mapOf();
        for (var button : ALL_SUPPORTED_MOUSE_BUTTONS) {
            MOUSE_BUTTON_STATES.put(button, false);
        }
    }

    @Override
    public void startup(Runnable game) throws IllegalArgumentException {
        if (!glfwInit()) {
            throw new RuntimeException("GLFW failed to initialize");
        }

        updateWindow();

        glSetup();

        bindShaderAndMesh();

        // TODO: Consider test for whether GraphicsPreloader.load was called _before_ the first
        //  invocation of FrameTimer.shouldExecuteNextFrame
        GRAPHICS_PRELOADER.load();

        AUDIO_REL_DIRS.forEach(relDir -> AUDIO_LOADER.loadFromDirectory(
                relDir,
                IDS_FOR_FILENAMES,
                DEFAULT_LOOP_STOP_MS_BY_ID,
                DEFAULT_LOOP_RESTART_MS_BY_ID
        ));

        new Thread(FRAME_TIMER::start).start();

        var gameThread = new Thread(game);
        gameThread.start();

        while (!glfwWindowShouldClose(window) && gameThread.isAlive()) {
            UPDATE_MOUSE_CURSOR.accept(window);

            if (FRAME_TIMER.shouldExecuteNextFrame()) {
                runFrame();
            }

            if (FRAME_TIMER_POLLING_INTERVAL > 0) {
                CheckedExceptionWrapper.sleep(FRAME_TIMER_POLLING_INTERVAL);
            }
        }

        tearDown();
    }

    private void glSetup() {
        GL.createCapabilities();

        glClearColor(0, 0, 0, 0);

        glEnable(GL_LINE_STIPPLE);
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_FASTEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDepthMask(false);
        glEnable(GL_TEXTURE_2D);

        glOrtho(0d, 1d, 1d, 0d, 0d, 1d);
    }

    private void bindShaderAndMesh() {
        var shader = SHADER_FACTORY.make(SHADER_FILENAME_PREFIX);
        shader.bind();

        SHADER_SUBSCRIBERS.forEach(s -> s.accept(shader));

        var mesh = MESH_FACTORY.apply(MESH_VERTICES, MESH_UV_COORDINATES);
        mesh.bind();

        MESH_SUBSCRIBERS.forEach(s -> s.accept(mesh));
    }

    private void runFrame() {
        glfwPollEvents();

        updateWindow();

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        var frameTimestamp = GLOBAL_CLOCK.globalTimestamp();

        KEY_EVENT_LISTENER.reportKeyEvents(frameTimestamp);
        readMouseButtonStates();

        UPDATE_MOST_RECENT_MOUSE_LOC.accept(screenMouseLocation);
        if (screenMouseLocation != null) {
            DETERMINE_MOUSE_EVENTS_AND_ACT.accept(
                    screenMouseLocation,
                    MOUSE_BUTTON_STATES,
                    frameTimestamp
            );
        }

        FRAME_EXECUTOR.execute(frameTimestamp);

        glfwSwapBuffers(window);
    }

    private void updateWindow() {
        var prevWindow = window;
        window = WINDOW_RESOLUTION_MANAGER.updateWindowSizeAndLocation(window, titlebar);
        glfwMakeContextCurrent(window);
        if (window == 0) {
            throw new IllegalStateException("Failed to create window");
        }
        if (window != prevWindow) {
            updateWindowDimensionsInResolutionManager();
            KEY_EVENT_LISTENER.registerKeyListener(window);
            setNewMouseCallbacks();
        }
    }

    // NB: Getting the screen dimensions for each callback ensures that mouse cursor position
    // will remain accurately depicted when the resolution changes, but may be a target for
    // performance enhancements.

    private void setNewMouseCallbacks() {
        //noinspection resource
        glfwSetCursorPosCallback(window, (_, xPixel, yPixel) -> {
            var windowDimensions = updateWindowDimensionsInResolutionManager();
            var width = windowDimensions.FIRST;
            var height = windowDimensions.SECOND;

            var x = (float) xPixel / width;
            var y = (float) yPixel / height;

            screenMouseLocation = vertexOf(x, y);
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
        return titlebar;
    }

    @Override
    public void setTitlebar(String titlebar) throws IllegalArgumentException {
        this.titlebar = Check.ifNullOrEmpty(titlebar, "titlebar");
    }

    private void tearDown() {
        FRAME_TIMER.stop();

        glfwTerminate();
    }
}
