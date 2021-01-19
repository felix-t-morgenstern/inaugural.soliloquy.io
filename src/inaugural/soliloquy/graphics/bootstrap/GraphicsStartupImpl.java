package inaugural.soliloquy.graphics.bootstrap;

import inaugural.soliloquy.graphics.api.WindowResolution;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import soliloquy.specs.graphics.bootstrap.GraphicsStartup;

import java.util.function.Consumer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.glClearColor;

public class GraphicsStartupImpl implements GraphicsStartup {
    private final WindowResolution _startingWindowResolution;
    private final String _titlebar;
    private final GLFWMouseButtonCallback _mouseButtonCallback;

    private long _window;

    public GraphicsStartupImpl(WindowResolution startingWindowResolution, String titlebar,
                               GLFWMouseButtonCallback mouseButtonCallback) {
        _startingWindowResolution = startingWindowResolution;
        _titlebar = titlebar;
        _mouseButtonCallback = mouseButtonCallback;
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
    }

    // TODO: Test and implement
    @Override
    public String getInterfaceName() {
        return null;
    }
}
