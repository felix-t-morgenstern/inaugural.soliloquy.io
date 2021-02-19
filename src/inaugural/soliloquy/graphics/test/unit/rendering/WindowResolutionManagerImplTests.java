package inaugural.soliloquy.graphics.test.unit.rendering;

import inaugural.soliloquy.graphics.api.WindowResolution;
import inaugural.soliloquy.graphics.rendering.WindowResolutionManagerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.rendering.WindowDisplayMode;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WindowResolutionManagerImplTests {
    private WindowResolutionManagerImpl _windowResolutionManager;

    @BeforeEach
    void setUp() {
        _windowResolutionManager = new WindowResolutionManagerImpl(WindowDisplayMode.WINDOWED,
                WindowResolution.RES_1920x1080);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> new WindowResolutionManagerImpl(
                null, WindowResolution.RES_1920x1080));
        assertThrows(IllegalArgumentException.class, () -> new WindowResolutionManagerImpl(
                WindowDisplayMode.WINDOWED, null));
        assertThrows(IllegalArgumentException.class, () -> new WindowResolutionManagerImpl(
                WindowDisplayMode.WINDOWED_FULLSCREEN, WindowResolution.RES_1920x1080));
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
    void testSetInvalidDimensions() {
        assertThrows(IllegalArgumentException.class,
                () -> _windowResolutionManager.setDimensions(0, 600));
        assertThrows(IllegalArgumentException.class,
                () -> _windowResolutionManager.setDimensions(800, 0));
        assertThrows(IllegalArgumentException.class,
                () -> _windowResolutionManager.setDimensions(800, 601));
    }

    @Test
    void testGetAndSetDimensionsWhileWindowedFullscreen() {
        _windowResolutionManager.setWindowDisplayMode(WindowDisplayMode.WINDOWED_FULLSCREEN);

        assertThrows(UnsupportedOperationException.class, _windowResolutionManager::getWidth);
        assertThrows(UnsupportedOperationException.class, _windowResolutionManager::getHeight);
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
