package inaugural.soliloquy.graphics.bootstrap;

import inaugural.soliloquy.tools.Check;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.rendering.FrameTimer;
import soliloquy.specs.graphics.rendering.StackRenderer;
import soliloquy.specs.graphics.rendering.WindowManager;

import java.util.function.Consumer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.*;

public class GraphicsCoreLoopImpl implements GraphicsCoreLoop {
    private final String _titlebar;
    private final GLFWMouseButtonCallback _mouseButtonCallback;
    private final FrameTimer _frameTimer;
    private final WindowManager _windowManager;
    private final StackRenderer _stackRenderer;

    private long _window;

    public GraphicsCoreLoopImpl(String titlebar,
                                GLFWMouseButtonCallback mouseButtonCallback,
                                FrameTimer frameTimer,
                                WindowManager windowManager,
                                StackRenderer stackRenderer) {
        _titlebar = Check.ifNullOrEmpty(titlebar, "titlebar");
        _mouseButtonCallback = Check.ifNull(mouseButtonCallback, "mouseButtonCallback");
        _frameTimer = Check.ifNull(frameTimer, "frameTimer");
        _windowManager = Check.ifNull(windowManager, "windowManager");
        _stackRenderer = Check.ifNull(stackRenderer, "stackRenderer");
    }

    @Override
    public void startup(Consumer<Long> callback) throws IllegalArgumentException {
        Check.ifNull(callback, "callback");

        if (!glfwInit()) {
            throw new RuntimeException("GLFW failed to initialize");
        }

        long timestamp =

        _window = glfwCreateWindow(1, 1, _titlebar, 0, 0);
        glfwShowWindow(_window);
        glfwMakeContextCurrent(_window);

        createCapabilities();

        glClearColor(0, 0, 0, 1);

        glfwSetMouseButtonCallback(_window, _mouseButtonCallback);

        new Thread(() -> callback.accept(_window)).start();

        while(!glfwWindowShouldClose(_window)) {
            if (_frameTimer.shouldExecuteNextFrame()) {
                _windowManager.updateWindowSizeAndLocation();

                _stackRenderer.render();

                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

                glfwSwapBuffers(_window); // swap the color buffers

                // Poll for window events. The key callback above will only be
                // invoked during this call.
                glfwPollEvents();
            }

            // TODO: Generate RuntimeException wrapper method in Tools
            try {
                Thread.sleep(_frameTimer.getPollingInterval());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // TODO: Test and implement
    @Override
    public String getInterfaceName() {
        return null;
    }
}
