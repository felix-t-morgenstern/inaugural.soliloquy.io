package inaugural.soliloquy.graphics.test.unit.rendering;

import inaugural.soliloquy.graphics.api.WindowResolution;
import inaugural.soliloquy.graphics.rendering.WindowResolutionManagerImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lwjgl.opengl.GL;
import soliloquy.specs.graphics.rendering.WindowDisplayMode;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.lwjgl.glfw.GLFW.*;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

public class WindowResolutionManagerImplTests {
    private WindowResolutionManager windowResolutionManager;

    @BeforeAll
    public static void setUpFixture() {
        if (!glfwInit()) {
            throw new RuntimeException("GLFW failed to initialize");
        }

        var window = glfwCreateWindow(1, 1, "", 0, 0);
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
    }

    @AfterAll
    static void tearDownFixture() {
        glfwTerminate();
    }

    @BeforeEach
    public void setUp() {
        windowResolutionManager = new WindowResolutionManagerImpl(WindowDisplayMode.WINDOWED,
                WindowResolution.RES_1920x1080);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new WindowResolutionManagerImpl(
                null, WindowResolution.RES_1920x1080));
        assertThrows(IllegalArgumentException.class, () -> new WindowResolutionManagerImpl(
                WindowDisplayMode.WINDOWED, null));
    }

    @Test
    public void testGetAndSetWindowDisplayMode() {
        windowResolutionManager.setWindowDisplayMode(WindowDisplayMode.FULLSCREEN);

        assertEquals(WindowDisplayMode.FULLSCREEN,
                windowResolutionManager.getWindowDisplayMode());
    }

    @Test
    public void testSetWindowDisplayModeWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> windowResolutionManager.setWindowDisplayMode(null));
        assertThrows(IllegalArgumentException.class,
                () -> windowResolutionManager.setWindowDisplayMode(WindowDisplayMode.UNKNOWN));
    }

    @Test
    public void testUpdateAndGetDimensions() {
        var width = 3840;
        var height = 2160;

        windowResolutionManager.updateDimensions(width, height);

        assertEquals(pairOf(width, height), windowResolutionManager.getWindowDimensions());
    }

    @Test
    public void testWindowWidthToHeightRatio() {
        var width = 3840;
        var height = 2160;

        windowResolutionManager.updateDimensions(width, height);

        assertEquals(width / (float) height, windowResolutionManager.windowWidthToHeightRatio());
    }

    @Test
    public void testSetInvalidDimensions() {
        assertThrows(IllegalArgumentException.class,
                () -> windowResolutionManager.updateDimensions(0, 600));
        assertThrows(IllegalArgumentException.class,
                () -> windowResolutionManager.updateDimensions(800, 0));
        assertThrows(IllegalArgumentException.class,
                () -> windowResolutionManager.updateDimensions(800, 601));
    }

    @Test
    public void testUpdateWindowSizeAndLocationWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> windowResolutionManager.updateWindowSizeAndLocation(0, null));
        assertThrows(IllegalArgumentException.class,
                () -> windowResolutionManager.updateWindowSizeAndLocation(0, ""));
    }

    // NB: updateWindowSizeAndLocation is not currently tested here, since its only effects are
    //     visible in the actual display, hence the display tests (e.g.
    //     WindowManagerImplWindowedTest)
}
