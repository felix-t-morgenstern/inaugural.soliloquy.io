package inaugural.soliloquy.graphics.rendering;

import inaugural.soliloquy.graphics.api.WindowResolution;
import inaugural.soliloquy.tools.Check;
import org.lwjgl.glfw.GLFWVidMode;
import soliloquy.specs.common.valueobjects.Pair;
import soliloquy.specs.graphics.rendering.WindowDisplayMode;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;

import java.util.function.Consumer;
import java.util.function.Function;

import static org.lwjgl.glfw.GLFW.*;

public class WindowResolutionManagerImpl implements WindowResolutionManager {
    private WindowDisplayMode windowDisplayMode;
    private WindowResolution windowResolution;

    private WindowDisplayMode mostRecentlyRenderedWindowDisplayMode;
    private WindowResolution mostRecentlyRenderedWindowResolution;

    private Pair<Integer, Integer> windowDimensions;

    public WindowResolutionManagerImpl(WindowDisplayMode startingWindowDisplayMode,
                                       WindowResolution startingWindowResolution) {
        windowDisplayMode = Check.ifNull(startingWindowDisplayMode, "startingWindowDisplayMode");
        windowResolution = Check.ifNull(startingWindowResolution, "startingWindowResolution");

        if (windowDisplayMode == WindowDisplayMode.WINDOWED_FULLSCREEN &&
                windowResolution != WindowResolution.RES_WINDOWED_FULLSCREEN) {
            throw new IllegalArgumentException("WindowResolutionManagerImpl: If " +
                    "windowDisplayMode is WINDOWED_FULLSCREEN, windowResolution must be " +
                    "RES_WINDOWED_FULLSCREEN");
        }

        mostRecentlyRenderedWindowDisplayMode = WindowDisplayMode.UNKNOWN;
        mostRecentlyRenderedWindowResolution = WindowResolution.RES_INVALID;
    }

    @Override
    public WindowDisplayMode getWindowDisplayMode() {
        return windowDisplayMode;
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
        this.windowDisplayMode = windowDisplayMode;
    }

    @Override
    public void updateDimensions(int width, int height)
            throws IllegalArgumentException, UnsupportedOperationException {
        Check.throwOnLtValue(width, 1, "width");
        Check.throwOnLtValue(height, 1, "height");

        if (windowDisplayMode != WindowDisplayMode.WINDOWED_FULLSCREEN &&
                windowDisplayMode != WindowDisplayMode.FULLSCREEN) {
            WindowResolution windowResolutionFromInputs =
                    WindowResolution.getFromWidthAndHeight(width, height);

            if (windowResolutionFromInputs == WindowResolution.RES_INVALID) {
                throw new IllegalArgumentException(
                        "WindowResolutionManagerImpl.setDimensions: inputs " +
                                "(" + width + "," + height +
                                ") do not correspond to valid resolution");
            }
        }

        windowResolution = WindowResolution.getFromWidthAndHeight(width, height);
        windowDimensions = new Pair<>(width, height);
    }

    @Override
    public Pair<Integer, Integer> getWindowDimensions() throws UnsupportedOperationException {
        return windowDimensions;
    }

    @Override
    public float windowWidthToHeightRatio() {
        int width = windowDimensions.getItem1();
        int height = windowDimensions.getItem2();

        return width / (float) height;
    }

    @Override
    public long updateWindowSizeAndLocation(long windowId, String titlebar) {
        // System.out.println("updateWindowSizeAndLocation");

        Check.ifNullOrEmpty(titlebar, "titlebar");

        if (windowResolution != mostRecentlyRenderedWindowResolution ||
                windowDisplayMode != mostRecentlyRenderedWindowDisplayMode) {
            switch (windowDisplayMode) {
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

            mostRecentlyRenderedWindowResolution = windowResolution;
            mostRecentlyRenderedWindowDisplayMode = windowDisplayMode;
        }

        return windowId;
    }

    private long renderWindowed(long windowId, String titlebar) {
        // TODO: Consider enforcing window to be within boundaries of monitor resolution
        return renderWindowForMode(windowId, WindowDisplayMode.WINDOWED,
                currentWindowId -> glfwSetWindowSize(currentWindowId, windowResolution.WIDTH,
                        windowResolution.HEIGHT),
                monitor -> glfwVidMode -> {

                    glfwWindowHint(GLFW_DECORATED, GLFW_TRUE);

                    long newWindowId = glfwCreateWindow(windowResolution.WIDTH,
                            windowResolution.HEIGHT, titlebar, 0, 0);
                    glfwSetWindowPos(newWindowId,
                            (glfwVidMode.width() - windowResolution.WIDTH) / 2,
                            (glfwVidMode.height() - windowResolution.HEIGHT) / 2);

                    return newWindowId;
                });
    }

    private long renderFullscreen(long windowId, String titlebar) {
        return renderWindowForMode(windowId, WindowDisplayMode.FULLSCREEN,
                currentWindowId -> glfwSetWindowSize(currentWindowId, windowResolution.WIDTH,
                        windowResolution.HEIGHT),
                monitor -> glfwVidMode -> glfwCreateWindow(windowResolution.WIDTH,
                        windowResolution.HEIGHT, titlebar, monitor, 0));
    }

    private long renderWindowedFullscreen(long windowId, String titlebar) {
        return renderWindowForMode(windowId, WindowDisplayMode.WINDOWED_FULLSCREEN,
                currentWindowId -> {}, monitor -> glfwVidMode -> {

                    glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);

                    int windowedFullscreenWidth = glfwVidMode.width();
                    int windowedFullscreenHeight = glfwVidMode.height();

                    long newWindowId = glfwCreateWindow(
                            windowedFullscreenWidth,
                            windowedFullscreenHeight,
                            titlebar,
                            0, 0);
                    glfwSetWindowMonitor(newWindowId, 0,
                            0,
                            0,
                            windowedFullscreenWidth,
                            windowedFullscreenHeight,
                            GLFW_DONT_CARE);
                    return newWindowId;
                });
    }

    private long renderWindowForMode(long windowId,
                                     WindowDisplayMode windowDisplayMode,
                                     Consumer<Long> noDisplayModeChangeAction,
                                     Function<Long, Function<GLFWVidMode, Long>> createNewWindow) {
        if (mostRecentlyRenderedWindowDisplayMode == windowDisplayMode) {
            noDisplayModeChangeAction.accept(windowId);
            return windowId;
        }

        long oldWindowId = windowId;
        long monitor = glfwGetPrimaryMonitor();
        GLFWVidMode glfwVidMode = glfwGetVideoMode(monitor);
        assert glfwVidMode != null;

        windowId = createNewWindow.apply(monitor).apply(glfwVidMode);

        if (mostRecentlyRenderedWindowDisplayMode != WindowDisplayMode.UNKNOWN) {
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
