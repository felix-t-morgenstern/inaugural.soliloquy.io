package inaugural.soliloquy.graphics.bootstrap;

import inaugural.soliloquy.graphics.api.WindowResolution;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.rendering.FrameTimer;
import soliloquy.specs.graphics.rendering.RenderableStack;
import soliloquy.specs.graphics.rendering.StackRenderer;

import java.util.function.Consumer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.glClearColor;

public class GraphicsCoreLoopImpl implements GraphicsCoreLoop {
    private final WindowResolution _startingWindowResolution;
    private final String _titlebar;
    private final GLFWMouseButtonCallback _mouseButtonCallback;
    private final FrameTimer _frameTimer;
    private final StackRenderer _stackRenderer;

    private long _window;

    public GraphicsCoreLoopImpl(WindowResolution startingWindowResolution, String titlebar,
                                GLFWMouseButtonCallback mouseButtonCallback,
                                FrameTimer frameTimer,
                                StackRenderer stackRenderer) {
        _startingWindowResolution = startingWindowResolution;
        _titlebar = titlebar;
        _mouseButtonCallback = mouseButtonCallback;
        _frameTimer = frameTimer;
        _stackRenderer = stackRenderer;
    }

    @Override
    public void startup(Consumer<Long> callback) throws IllegalArgumentException {
        if (!glfwInit()) {
            throw new RuntimeException("GLFW failed to initialize");
        }

        // TODO: Ensure that windowed fullscreen works
        _window = glfwCreateWindow(_startingWindowResolution.WIDTH,
                                   _startingWindowResolution.HEIGHT,
                                   _titlebar,
                                   0,
                                   0);
        glfwShowWindow(_window);
        glfwMakeContextCurrent(_window);

        createCapabilities();
        glClearColor(0, 0, 0, 1);

        glfwSetMouseButtonCallback(_window, _mouseButtonCallback);

        callback.accept(_window);

        while(!glfwWindowShouldClose(_window)) {
            if (_frameTimer.shouldExecuteNextFrame()) {
                _stackRenderer.render();
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
