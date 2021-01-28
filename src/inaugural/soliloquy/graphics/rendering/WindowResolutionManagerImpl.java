package inaugural.soliloquy.graphics.rendering;

import inaugural.soliloquy.graphics.api.WindowResolution;
import inaugural.soliloquy.tools.Check;
import org.lwjgl.glfw.GLFWVidMode;
import soliloquy.specs.graphics.rendering.WindowDisplayMode;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;

import static org.lwjgl.glfw.GLFW.*;

// TODO: Clean up all the mess in this code
public class WindowResolutionManagerImpl implements WindowResolutionManager {
    private WindowDisplayMode _windowDisplayMode;
    private WindowResolution _windowResolution;

    private WindowDisplayMode _mostRecentlyRenderedWindowDisplayMode;
    private WindowResolution _mostRecentlyRenderedWindowResolution;

    public WindowResolutionManagerImpl(WindowDisplayMode startingWindowDisplayMode,
                                       WindowResolution startingWindowResolution) {
        _windowDisplayMode = Check.ifNull(startingWindowDisplayMode, "startingWindowDisplayMode");
        _windowResolution = Check.ifNull(startingWindowResolution, "startingWindowResolution");

        if (_windowDisplayMode == WindowDisplayMode.WINDOWED_FULLSCREEN &&
                _windowResolution != WindowResolution.RES_WINDOWED_FULLSCREEN) {
            throw new IllegalArgumentException("WindowResolutionManagerImpl: If " +
                    "windowDisplayMode is WINDOWED_FULLSCREEN, windowResolution must be " +
                    "RES_WINDOWED_FULLSCREEN");
        }

        _mostRecentlyRenderedWindowDisplayMode = WindowDisplayMode.UNKNOWN;
        _mostRecentlyRenderedWindowResolution = WindowResolution.RES_INVALID;
    }

    @Override
    public WindowDisplayMode getWindowDisplayMode() {
        return _windowDisplayMode;
    }

    @Override
    public void setWindowDisplayMode(WindowDisplayMode windowDisplayMode)
            throws IllegalArgumentException {
        if (Check.ifNull(windowDisplayMode, "windowDisplayMode") ==
                WindowDisplayMode.UNKNOWN) {
            throw new IllegalArgumentException(
                    "WindowResolutionManagerImpl.setWindowDisplayMode: windowDisplayMode cannot " +
                            "be UNKNOWN");
        }
        _windowDisplayMode = windowDisplayMode;
    }

    @Override
    public void setDimensions(int width, int height)
            throws IllegalArgumentException, UnsupportedOperationException {
        if (_windowDisplayMode == WindowDisplayMode.WINDOWED_FULLSCREEN) {
            throw new UnsupportedOperationException(
                    "WindowResolutionManagerImpl.setDimensions: cannot set dimensions while " +
                            "windowed fullscreen");
        }

        if (width < 1) {
            throw new IllegalArgumentException("WindowResolutionManagerImpl.setDimensions: " +
                    "width (" + width + ") cannot be less than 1");
        }
        if (height < 1) {
            throw new IllegalArgumentException("WindowResolutionManagerImpl.setDimensions: " +
                    "height (" + height + ") cannot be less than 1");
        }

        WindowResolution windowResolutionFromInputs =
                WindowResolution.getFromWidthAndHeight(width, height);

        if (windowResolutionFromInputs == WindowResolution.RES_INVALID) {
            throw new IllegalArgumentException("WindowResolutionManagerImpl.setDimensions: inputs " +
                    "(" + width + "," + height + ") do not correspond to valid resolution");
        }

        _windowResolution = WindowResolution.getFromWidthAndHeight(width, height);
    }

    @Override
    public int getWidth() throws UnsupportedOperationException {
        if (_windowDisplayMode == WindowDisplayMode.WINDOWED_FULLSCREEN) {
            throw new UnsupportedOperationException(
                    "WindowResolutionManagerImpl.getHeight: cannot get width while windowed " +
                            "fullscreen");
        }

        return _windowResolution.WIDTH;
    }

    @Override
    public int getHeight() throws UnsupportedOperationException {
        if (_windowDisplayMode == WindowDisplayMode.WINDOWED_FULLSCREEN) {
            throw new UnsupportedOperationException(
                    "WindowResolutionManagerImpl.getHeight: cannot get height while windowed " +
                            "fullscreen");
        }
        return _windowResolution.HEIGHT;
    }

    @Override
    public long updateWindowSizeAndLocation(long windowId, String titlebar) {
        Check.ifNullOrEmpty(titlebar, "titlebar");

        if (_windowResolution != _mostRecentlyRenderedWindowResolution ||
                _windowDisplayMode != _mostRecentlyRenderedWindowDisplayMode) {
            // Do the actual action here
            switch(_windowDisplayMode) {
                case WINDOWED:
                    windowId = renderWindowed(windowId, titlebar);
                    break;
                case FULLSCREEN:
                    windowId = renderFullscreen(windowId, titlebar);
                    break;
                case WINDOWED_FULLSCREEN:
                    windowId = renderWindowedFullscreen(windowId, titlebar);
                    break;
                default:
                    throw new IllegalStateException(
                            "WindowResolutionManager.updateWindowSizeAndLocation: display mode " +
                                    "cannot be UNKNOWN");
            }

            _mostRecentlyRenderedWindowResolution = _windowResolution;
            _mostRecentlyRenderedWindowDisplayMode = _windowDisplayMode;
        }

        // TODO: Is this call redundant?
        glfwShowWindow(windowId);

        return windowId;
    }

    private long renderWindowed(long windowId, String titlebar) {
        // TODO: Consider enforcing window to be within boundaries of monitor resolution
        if (_mostRecentlyRenderedWindowDisplayMode == WindowDisplayMode.WINDOWED) {
            glfwSetWindowSize(windowId, _windowResolution.WIDTH, _windowResolution.HEIGHT);
            return windowId;
        }

        long oldWindowId = windowId;
        long monitor = glfwGetPrimaryMonitor();
        GLFWVidMode glfwVidMode = glfwGetVideoMode(monitor);
        assert glfwVidMode != null;

        glfwWindowHint(GLFW_DECORATED, GLFW_TRUE);

        windowId = glfwCreateWindow(glfwVidMode.width(), glfwVidMode.height(), titlebar,
                0, 0);
        glfwSetWindowMonitor(windowId, 0,
                (glfwVidMode.width() - _windowResolution.WIDTH) / 2,
                (glfwVidMode.height() - _windowResolution.HEIGHT) / 2,
                _windowResolution.WIDTH,
                _windowResolution.HEIGHT,
                GLFW_DONT_CARE);

        if (_mostRecentlyRenderedWindowDisplayMode != WindowDisplayMode.UNKNOWN) {
            glfwDestroyWindow(oldWindowId);
        }

        glfwMakeContextCurrent(windowId);
        glfwShowWindow(windowId);

        return windowId;
    }

    // TODO: This is looking a lot like renderWindowed; consider merging?
    private long renderFullscreen(long windowId, String titlebar) {
        if (_mostRecentlyRenderedWindowDisplayMode == WindowDisplayMode.FULLSCREEN) {
            glfwSetWindowSize(windowId, _windowResolution.WIDTH, _windowResolution.HEIGHT);
            return windowId;
        }

        long oldWindowId = windowId;
        long monitor = glfwGetPrimaryMonitor();
        GLFWVidMode glfwVidMode = glfwGetVideoMode(monitor);
        assert glfwVidMode != null;

        windowId = glfwCreateWindow(_windowResolution.WIDTH, _windowResolution.HEIGHT, titlebar,
                monitor, 0);

        if (_mostRecentlyRenderedWindowDisplayMode != WindowDisplayMode.UNKNOWN) {
            glfwDestroyWindow(oldWindowId);
        }

        glfwMakeContextCurrent(windowId);
        glfwShowWindow(windowId);

        return windowId;
    }

    // TODO: This is looking a lot like renderWindowed; consider merging?
    private long renderWindowedFullscreen(long windowId, String titlebar) {
        if (_mostRecentlyRenderedWindowDisplayMode == WindowDisplayMode.WINDOWED_FULLSCREEN) {
            return windowId;
        }

        long oldWindowId = windowId;
        long monitor = glfwGetPrimaryMonitor();
        GLFWVidMode glfwVidMode = glfwGetVideoMode(monitor);
        assert glfwVidMode != null;

        glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);

        windowId = glfwCreateWindow(glfwVidMode.width(), glfwVidMode.height(), titlebar,
                0, 0);
        glfwSetWindowMonitor(windowId, 0,
                0,
                0,
                glfwVidMode.width(),
                glfwVidMode.height(),
                GLFW_DONT_CARE);

        if (_mostRecentlyRenderedWindowDisplayMode != WindowDisplayMode.UNKNOWN) {
            glfwDestroyWindow(oldWindowId);
        }

        glfwMakeContextCurrent(windowId);
        glfwShowWindow(windowId);

        return windowId;
    }

    @Override
    public String getInterfaceName() {
        return WindowResolutionManager.class.getCanonicalName();
    }
}
