package inaugural.soliloquy.graphics.bootstrap;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.opengl.GL;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.bootstrap.GraphicsPreloader;
import soliloquy.specs.graphics.io.MouseCursor;
import soliloquy.specs.graphics.rendering.FrameExecutor;
import soliloquy.specs.graphics.rendering.Mesh;
import soliloquy.specs.graphics.rendering.Shader;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.graphics.rendering.factories.ShaderFactory;
import soliloquy.specs.graphics.rendering.renderers.Renderer;
import soliloquy.specs.graphics.rendering.timing.FrameTimer;

import java.util.Collection;
import java.util.function.Function;

import static inaugural.soliloquy.graphics.api.Constants.MS_PER_SECOND;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class GraphicsCoreLoopImpl implements GraphicsCoreLoop {
    private final String TITLEBAR;
    private final GLFWMouseButtonCallback MOUSE_BUTTON_CALLBACK;
    private final FrameTimer FRAME_TIMER;
    private final int FRAME_TIMER_POLLING_INTERVAL;
    private final WindowResolutionManager WINDOW_RESOLUTION_MANAGER;
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

    private long _window;

    public GraphicsCoreLoopImpl(String titlebar,
                                GLFWMouseButtonCallback mouseButtonCallback,
                                FrameTimer frameTimer,
                                int frameTimerPollingInterval,
                                WindowResolutionManager windowResolutionManager,
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
                                MouseCursor mouseCursor) {
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

        glfwSetMouseButtonCallback(_window, MOUSE_BUTTON_CALLBACK);

        Shader shader = SHADER_FACTORY.make(SHADER_FILENAME_PREFIX);
        shader.bind();

        RENDERERS_WITH_SHADER.forEach(renderer -> renderer.setShader(shader));

        Mesh mesh = MESH_FACTORY.apply(MESH_VERTICES).apply(MESH_UV_COORDINATES);

        RENDERERS_WITH_MESH.forEach(renderer -> renderer.setMesh(mesh));

        mesh.bind();

        // TODO: Consider test for whether GraphicsPreloader.load was called _before_ the first
        //  invocation of FrameTimer.shouldExecuteNextFrame
        GRAPHICS_PRELOADER.load();

        new Thread(gameThread).start();

        while (!glfwWindowShouldClose(_window)) {
            MOUSE_CURSOR.updateCursor(_window);

            if (FRAME_TIMER.shouldExecuteNextFrame()) {
                glfwPollEvents();

                updateWindow();

                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

                FRAME_EXECUTOR.execute();

                glfwSwapBuffers(_window);
            }

            if (FRAME_TIMER_POLLING_INTERVAL > 0) {
                CheckedExceptionWrapper.sleep(FRAME_TIMER_POLLING_INTERVAL);
            }
        }

        glfwTerminate();
    }

    private void updateWindow() {
        _window = WINDOW_RESOLUTION_MANAGER.updateWindowSizeAndLocation(_window, TITLEBAR);
        if (_window == 0) {
            throw new IllegalStateException("Failed to create window");
        }
    }

    @Override
    public long windowId() throws UnsupportedOperationException {
        return _window;
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
