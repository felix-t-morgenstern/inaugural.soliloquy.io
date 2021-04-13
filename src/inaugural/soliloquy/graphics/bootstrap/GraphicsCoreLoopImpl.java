package inaugural.soliloquy.graphics.bootstrap;

import inaugural.soliloquy.graphics.rendering.GlobalClockImpl;
import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.opengl.GL;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.bootstrap.GraphicsPreloader;
import soliloquy.specs.graphics.rendering.*;
import soliloquy.specs.graphics.rendering.factories.ShaderFactory;
import soliloquy.specs.graphics.rendering.timing.FrameTimer;
import soliloquy.specs.graphics.rendering.timing.GlobalClock;

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
    private final StackRenderer STACK_RENDERER;
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

    private long _window;

    public GraphicsCoreLoopImpl(String titlebar,
                                GLFWMouseButtonCallback mouseButtonCallback,
                                FrameTimer frameTimer,
                                int frameTimerPollingInterval,
                                WindowResolutionManager windowResolutionManager,
                                StackRenderer stackRenderer,
                                ShaderFactory shaderFactory,
                                @SuppressWarnings("rawtypes") Collection<Renderer>
                                        renderersWithShader,
                                String shaderFilenamePrefix,
                                Function<float[], Function<float[],Mesh>> meshFactory,
                                @SuppressWarnings("rawtypes") Collection<Renderer>
                                        renderersWithMesh,
                                float[] meshVertices,
                                float[] meshUvCoordinates,
                                GraphicsPreloader graphicsPreloader) {
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
        STACK_RENDERER = Check.ifNull(stackRenderer, "stackRenderer");
        SHADER_FACTORY = Check.ifNull(shaderFactory, "shaderFactory");
        RENDERERS_WITH_SHADER = Check.ifNull(renderersWithShader, "renderersWithShader");
        SHADER_FILENAME_PREFIX = Check.ifNullOrEmpty(shaderFilenamePrefix, "shaderFilenamePrefix");
        MESH_FACTORY = Check.ifNull(meshFactory, "meshFactory");
        RENDERERS_WITH_MESH = Check.ifNull(renderersWithMesh, "renderersWithMesh");
        MESH_VERTICES = Check.ifNull(meshVertices, "meshVertices");
        MESH_UV_COORDINATES = Check.ifNull(meshUvCoordinates, "meshUvCoordinates");
        GRAPHICS_PRELOADER = Check.ifNull(graphicsPreloader, "graphicsPreloader");
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

        glfwSetMouseButtonCallback(_window, MOUSE_BUTTON_CALLBACK);

        Shader shader = SHADER_FACTORY.make(SHADER_FILENAME_PREFIX);
        shader.bind();

        RENDERERS_WITH_SHADER.forEach(renderer -> renderer.setShader(shader));

        Mesh mesh = MESH_FACTORY.apply(MESH_VERTICES).apply(MESH_UV_COORDINATES);

        RENDERERS_WITH_MESH.forEach(renderer -> renderer.setMesh(mesh));

        mesh.bind();

        // TODO: Consider test for whether GraphicsPreloader.load was called _before_ the first invocation of FrameTimer.shouldExecuteNextFrame
        GRAPHICS_PRELOADER.load();

        new Thread(gameThread).start();

        // TODO: Get this shit out of here too; this should be handled by the FrameExecutor
        GlobalClock globalClock = new GlobalClockImpl();

        while(!glfwWindowShouldClose(_window)) {
            if (FRAME_TIMER.shouldExecuteNextFrame()) {
                glfwPollEvents();

                updateWindow();

                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

                // TODO: Offset this silliness to the FrameExecutor
                STACK_RENDERER.render(globalClock.globalTimestamp());

                glfwSwapBuffers(_window);
            }

            CheckedExceptionWrapper.sleep(FRAME_TIMER_POLLING_INTERVAL);
        }

        glfwTerminate();
    }

    private void updateWindow() {
        _window = WINDOW_RESOLUTION_MANAGER.updateWindowSizeAndLocation(_window, TITLEBAR);
        // TODO: Ensure via tests that 0 values throw exceptions, both at initial creation, and on updates
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
