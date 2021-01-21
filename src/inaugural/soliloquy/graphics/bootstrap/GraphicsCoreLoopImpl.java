package inaugural.soliloquy.graphics.bootstrap;

import org.lwjgl.glfw.GLFWMouseButtonCallback;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.rendering.FrameTimer;
import soliloquy.specs.graphics.rendering.StackRenderer;
import soliloquy.specs.graphics.rendering.WindowDisplayMode;
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
    private WindowDisplayMode _currentWindowDisplayMode;
    private int _currentWindowWidth;
    private int _currentWindowHeight;

    public GraphicsCoreLoopImpl(String titlebar,
                                GLFWMouseButtonCallback mouseButtonCallback,
                                FrameTimer frameTimer,
                                WindowManager windowManager,
                                StackRenderer stackRenderer) {
        _titlebar = titlebar;
        _mouseButtonCallback = mouseButtonCallback;
        _frameTimer = frameTimer;
        _windowManager = windowManager;
        _stackRenderer = stackRenderer;

        _currentWindowWidth = _currentWindowHeight = -1;
        _currentWindowDisplayMode = WindowDisplayMode.UNKNOWN;
    }

    @Override
    public void startup(Consumer<Long> callback) throws IllegalArgumentException {
        if (!glfwInit()) {
            throw new RuntimeException("GLFW failed to initialize");
        }

        _window = glfwCreateWindow(200, 200, _titlebar, 0, 0);
        glfwShowWindow(_window);
//        glfwSetWindowSize(_window, 900, 900);
        glfwMakeContextCurrent(_window);

        createCapabilities();

        glClearColor(0, 0, 0, 1);

        glfwSetMouseButtonCallback(_window, _mouseButtonCallback);

        new Thread(() -> callback.accept(_window)).start();

        while(!glfwWindowShouldClose(_window)) {
            if (_frameTimer.shouldExecuteNextFrame()) {

                if (_windowManager.getWindowDisplayMode() != _currentWindowDisplayMode ||
                _windowManager.windowWidth() != _currentWindowWidth ||
                _windowManager.windowHeight() != _currentWindowHeight) {

                    // TODO: Handle cases other than Windowed

                    glfwSetWindowSize(_window, _windowManager.windowWidth(),
                            _windowManager.windowHeight());

                    _currentWindowDisplayMode = _windowManager.getWindowDisplayMode();
                    _currentWindowWidth = _windowManager.windowWidth();
                    _currentWindowHeight = _windowManager.windowHeight();
                }

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
