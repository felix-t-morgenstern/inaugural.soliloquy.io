package inaugural.soliloquy.graphics.test.unit.bootstrap;

import inaugural.soliloquy.graphics.bootstrap.ImagePreloaderWorker;
import org.junit.jupiter.api.*;
import org.lwjgl.opengl.GL;
import soliloquy.specs.graphics.assets.Image;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.lwjgl.glfw.GLFW.*;

class ImagePreloaderWorkerTests {
    private final String SIMPLE_GRADIENT_RELATIVE_LOCATION = "./res/images/ui/gradient.png";

    @BeforeAll
    static void setUp() {
        if (!glfwInit()) {
            throw new RuntimeException("GLFW failed to initialize");
        }

        long window = glfwCreateWindow(1, 1, "", 0, 0);
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
    }

    @AfterAll
    static void tearDown() {
        glfwTerminate();
    }

    @Test
    void testConstructorWithInvalidParams() {
        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        List<Image> images = new ArrayList<>();

        assertThrows(IllegalArgumentException.class,
                () -> new ImagePreloaderWorker(null, images::add));
        assertThrows(IllegalArgumentException.class,
                () -> new ImagePreloaderWorker("", images::add));
        assertThrows(IllegalArgumentException.class,
                () -> new ImagePreloaderWorker(SIMPLE_GRADIENT_RELATIVE_LOCATION, null));

        assertThrows(IllegalArgumentException.class,
                () -> new ImagePreloaderWorker(null, 0f, images::add));
        assertThrows(IllegalArgumentException.class,
                () -> new ImagePreloaderWorker("", 0f, images::add));
        assertThrows(IllegalArgumentException.class,
                () -> new ImagePreloaderWorker(SIMPLE_GRADIENT_RELATIVE_LOCATION, 0f, null));
    }

    @Test
    void testLoadNonCapturingImage() {
        List<Image> images = new ArrayList<>();

        ImagePreloaderWorker imagePreloaderWorker =
                new ImagePreloaderWorker(SIMPLE_GRADIENT_RELATIVE_LOCATION, images::add);
        imagePreloaderWorker.load();

        assertEquals(1, images.size());

        Image image = images.get(0);

        assertEquals(SIMPLE_GRADIENT_RELATIVE_LOCATION, image.relativeLocation());
        assertNotEquals(0, image.textureId());
        assertEquals(20, image.width());
        assertEquals(20, image.height());
        assertEquals(Image.class.getCanonicalName(), image.getInterfaceName());

        assertFalse(image.supportsMouseEventCapturing());
        assertThrows(UnsupportedOperationException.class,
                () -> image.capturesMouseEventsAtPixel(0, 0));
    }

    @Test
    void testLoadCapturingImage() {
        List<Image> images = new ArrayList<>();

        ImagePreloaderWorker imagePreloaderWorker =
                new ImagePreloaderWorker(SIMPLE_GRADIENT_RELATIVE_LOCATION, 0.5f, images::add);
        imagePreloaderWorker.load();

        assertEquals(1, images.size());

        Image image = images.get(0);

        assertEquals(SIMPLE_GRADIENT_RELATIVE_LOCATION, image.relativeLocation());
        assertNotEquals(0, image.textureId());
        assertEquals(20, image.width());
        assertEquals(20, image.height());
        assertEquals(Image.class.getCanonicalName(), image.getInterfaceName());

        assertTrue(image.supportsMouseEventCapturing());
        assertThrows(IllegalArgumentException.class,
                () -> image.capturesMouseEventsAtPixel(-1, 0));
        assertThrows(IllegalArgumentException.class,
                () -> image.capturesMouseEventsAtPixel(0, -1));
        assertThrows(IllegalArgumentException.class,
                () -> image.capturesMouseEventsAtPixel(image.width(), 0));
        assertThrows(IllegalArgumentException.class,
                () -> image.capturesMouseEventsAtPixel(0, image.height()));

        assertTrue(image.capturesMouseEventsAtPixel(9, 0));
        assertFalse(image.capturesMouseEventsAtPixel(10, 0));
        assertTrue(image.capturesMouseEventsAtPixel(9, 19));
        assertFalse(image.capturesMouseEventsAtPixel(10, 19));
    }
}
