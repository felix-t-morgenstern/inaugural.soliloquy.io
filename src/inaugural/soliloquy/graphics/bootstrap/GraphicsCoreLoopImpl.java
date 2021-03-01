package inaugural.soliloquy.graphics.bootstrap;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.opengl.GL;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.rendering.FrameTimer;
import soliloquy.specs.graphics.rendering.Shader;
import soliloquy.specs.graphics.rendering.StackRenderer;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.graphics.rendering.factories.ShaderFactory;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class GraphicsCoreLoopImpl implements GraphicsCoreLoop {
    private final String TITLEBAR;
    private final GLFWMouseButtonCallback MOUSE_BUTTON_CALLBACK;
    private final FrameTimer FRAME_TIMER;
    private final WindowResolutionManager WINDOW_RESOLUTION_MANAGER;
    private final StackRenderer STACK_RENDERER;
    private final ShaderFactory SHADER_FACTORY;
    private final String SHADER_FILENAME_PREFIX;

    private long _window;

    public GraphicsCoreLoopImpl(String titlebar,
                                GLFWMouseButtonCallback mouseButtonCallback,
                                FrameTimer frameTimer,
                                WindowResolutionManager windowResolutionManager,
                                StackRenderer stackRenderer,
                                ShaderFactory shaderFactory,
                                String shaderFilenamePrefix) {
        TITLEBAR = Check.ifNullOrEmpty(titlebar, "titlebar");
        MOUSE_BUTTON_CALLBACK = Check.ifNull(mouseButtonCallback, "mouseButtonCallback");
        FRAME_TIMER = Check.ifNull(frameTimer, "frameTimer");
        WINDOW_RESOLUTION_MANAGER = Check.ifNull(windowResolutionManager,
                "windowResolutionManager");
        STACK_RENDERER = Check.ifNull(stackRenderer, "stackRenderer");
        SHADER_FACTORY = Check.ifNull(shaderFactory, "shaderFactory");
        SHADER_FILENAME_PREFIX = Check.ifNullOrEmpty(shaderFilenamePrefix, "shaderFilenamePrefix");
    }

    @Override
    public void startup(Runnable gameThread) throws IllegalArgumentException {
        if (!glfwInit()) {
            throw new RuntimeException("GLFW failed to initialize");
        }

        _window = WINDOW_RESOLUTION_MANAGER.updateWindowSizeAndLocation(_window, TITLEBAR);

        // TODO: Ensure via tests that 0 values throw exceptions, both at initial creation, and on updates
        if (_window == 0) {
            throw new IllegalStateException("Failed to create window");
        }

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

        new Thread(gameThread).start();

        while(!glfwWindowShouldClose(_window)) {
            if (FRAME_TIMER.shouldExecuteNextFrame()) {
                glfwPollEvents();

                _window = WINDOW_RESOLUTION_MANAGER.updateWindowSizeAndLocation(_window, TITLEBAR);

                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

                STACK_RENDERER.render();

                glfwSwapBuffers(_window);
            }

            CheckedExceptionWrapper.Sleep(FRAME_TIMER.getPollingInterval());
        }

        glfwTerminate();
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
