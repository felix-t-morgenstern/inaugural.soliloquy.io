package inaugural.soliloquy.graphics.bootstrap;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.opengl.GL;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.rendering.FrameTimer;
import soliloquy.specs.graphics.rendering.StackRenderer;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class GraphicsCoreLoopImpl implements GraphicsCoreLoop {
    private final String _titlebar;
    private final GLFWMouseButtonCallback _mouseButtonCallback;
    private final FrameTimer _frameTimer;
    private final WindowResolutionManager _windowManager;
    private final StackRenderer _stackRenderer;

    private long _window;

    public GraphicsCoreLoopImpl(String titlebar,
                                GLFWMouseButtonCallback mouseButtonCallback,
                                FrameTimer frameTimer,
                                WindowResolutionManager windowResolutionManager,
                                StackRenderer stackRenderer) {
        _titlebar = Check.ifNullOrEmpty(titlebar, "titlebar");
        _mouseButtonCallback = Check.ifNull(mouseButtonCallback, "mouseButtonCallback");
        _frameTimer = Check.ifNull(frameTimer, "frameTimer");
        _windowManager = Check.ifNull(windowResolutionManager, "windowResolutionManager");
        _stackRenderer = Check.ifNull(stackRenderer, "stackRenderer");
    }

    @Override
    public void startup(Runnable gameThread) throws IllegalArgumentException {
        if (!glfwInit()) {
            throw new RuntimeException("GLFW failed to initialize");
        }

        _window = _windowManager.updateWindowSizeAndLocation(_window, _titlebar);

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

        glfwSetMouseButtonCallback(_window, _mouseButtonCallback);

        new Thread(gameThread).start();

        while(!glfwWindowShouldClose(_window)) {
            if (_frameTimer.shouldExecuteNextFrame()) {
                glfwPollEvents();

                _window = _windowManager.updateWindowSizeAndLocation(_window, _titlebar);

                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

                _stackRenderer.render();

                glfwSwapBuffers(_window);
            }

            CheckedExceptionWrapper.Sleep(_frameTimer.getPollingInterval());
        }

        glfwTerminate();
    }

    @Override
    public long windowId() throws UnsupportedOperationException {
        return _window;
    }

    @Override
    public String getTitlebar() {
        return _titlebar;
    }

    @Override
    public String getInterfaceName() {
        return GraphicsCoreLoop.class.getCanonicalName();
    }
}
