package inaugural.soliloquy.graphics.test.unit.rendering;

import inaugural.soliloquy.common.test.fakes.FakeCoordinateFactory;
import inaugural.soliloquy.graphics.api.WindowResolution;
import inaugural.soliloquy.graphics.rendering.WindowResolutionManagerImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lwjgl.opengl.GL;
import soliloquy.specs.common.infrastructure.Pair;
import soliloquy.specs.common.valueobjects.Coordinate;
import soliloquy.specs.graphics.rendering.WindowDisplayMode;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.lwjgl.glfw.GLFW.*;

class WindowResolutionManagerImplTests {
    private final FakeCoordinateFactory COORDINATE_FACTORY = new FakeCoordinateFactory();

    private WindowResolutionManagerImpl _windowResolutionManager;

    @BeforeAll
    static void setUpFixture() {
        if (!glfwInit()) {
            throw new RuntimeException("GLFW failed to initialize");
        }

        long window = glfwCreateWindow(1, 1, "", 0, 0);
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
    }

    @AfterAll
    static void tearDownFixture() {
        glfwTerminate();
    }

    @BeforeEach
    void setUp() {
        _windowResolutionManager = new WindowResolutionManagerImpl(WindowDisplayMode.WINDOWED,
                WindowResolution.RES_1920x1080, COORDINATE_FACTORY);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> new WindowResolutionManagerImpl(
                null, WindowResolution.RES_1920x1080, COORDINATE_FACTORY));
        assertThrows(IllegalArgumentException.class, () -> new WindowResolutionManagerImpl(
                WindowDisplayMode.WINDOWED, null, COORDINATE_FACTORY));
        assertThrows(IllegalArgumentException.class, () -> new WindowResolutionManagerImpl(
                WindowDisplayMode.WINDOWED_FULLSCREEN, WindowResolution.RES_1920x1080, null));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(WindowResolutionManager.class.getCanonicalName(),
                _windowResolutionManager.getInterfaceName());
    }

    @Test
    void testGetAndSetWindowDisplayMode() {
        _windowResolutionManager.setWindowDisplayMode(WindowDisplayMode.FULLSCREEN);

        assertEquals(WindowDisplayMode.FULLSCREEN,
                _windowResolutionManager.getWindowDisplayMode());
    }

    @Test
    void testSetWindowDisplayModeWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> _windowResolutionManager.setWindowDisplayMode(null));
        assertThrows(IllegalArgumentException.class,
                () -> _windowResolutionManager.setWindowDisplayMode(WindowDisplayMode.UNKNOWN));
    }

    @Test
    void testSetAndGetDimensions() {
        int width = 3840;
        int height = 2160;

        _windowResolutionManager.setDimensions(width, height);

        assertEquals(width, _windowResolutionManager.getWidth());
        assertEquals(height, _windowResolutionManager.getHeight());
    }

    @Test
    void testRegisterResolutionSubscriber() {
        int width = 3840;
        int height = 2160;

        // NB: Assertions are defined prior to the test action due to limitations on how Java
        //     allows the inside of lambda statements to manipulate objects defined outside of them
        Consumer<Coordinate> subscriber = resolution -> {
            assertEquals(width, resolution.getX());
            assertEquals(height, resolution.getY());
        };

        _windowResolutionManager.registerResolutionSubscriber(subscriber);
        _windowResolutionManager.setDimensions(width, height);
        _windowResolutionManager.updateWindowSizeAndLocation(0, "titlebar");
    }

    @Test
    void testRegisterResolutionSubscriberWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> _windowResolutionManager.registerResolutionSubscriber(null));
    }

    @Test
    void testSetInvalidDimensions() {
        assertThrows(IllegalArgumentException.class,
                () -> _windowResolutionManager.setDimensions(0, 600));
        assertThrows(IllegalArgumentException.class,
                () -> _windowResolutionManager.setDimensions(800, 0));
        assertThrows(IllegalArgumentException.class,
                () -> _windowResolutionManager.setDimensions(800, 601));
    }

    @Test
    void testSetDimensionsWhileWindowedFullscreen() {
        _windowResolutionManager.setWindowDisplayMode(WindowDisplayMode.WINDOWED_FULLSCREEN);

        assertThrows(UnsupportedOperationException.class,
                () -> _windowResolutionManager.setDimensions(800, 600));
    }

    @Test
    void testUpdateWindowSizeAndLocationWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> _windowResolutionManager.updateWindowSizeAndLocation(0, null));
        assertThrows(IllegalArgumentException.class,
                () -> _windowResolutionManager.updateWindowSizeAndLocation(0, ""));
    }

    // NB: updateWindowSizeAndLocation is not currently tested here, since its only effects are
    //     visible in the actual display, hence the display tests (e.g.
    //     WindowManagerImplWindowedTest)
}
