package inaugural.soliloquy.io.test.unit.graphics.bootstrap.assetfactories;

import inaugural.soliloquy.io.graphics.bootstrap.assetfactories.ImageFactoryImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.io.graphics.assets.Image;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.ImageFactory;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.ImageDefinition;

import static org.junit.jupiter.api.Assertions.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;

public class ImageFactoryImplTests {
    private final String SIMPLE_GRADIENT_RELATIVE_LOCATION =
            "./src/test/resources/images/ui/gradient_200x200.png";
    private final int SIMPLE_GRADIENT_WIDTH = 200;
    private final int SIMPLE_GRADIENT_HEIGHT = 200;
    @SuppressWarnings("FieldCanBeLocal")
    private final float ALPHA_THRESHOLD = 0.5f;

    private ImageFactory imageFactory;

    @BeforeAll
    public static void setUpFixture() {
        if (!glfwInit()) {
            throw new RuntimeException("GLFW failed to initialize");
        }

        var window = glfwCreateWindow(1, 1, "", 0, 0);
        glfwMakeContextCurrent(window);
        createCapabilities();
    }

    @AfterAll
    static void tearDownFixture() {
        glfwTerminate();
    }

    @BeforeEach
    public void setUp() {
        imageFactory = new ImageFactoryImpl(ALPHA_THRESHOLD);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new ImageFactoryImpl(-0.0001f));
        assertThrows(IllegalArgumentException.class, () -> new ImageFactoryImpl(1.0001f));
    }

    @Test
    public void testLoadNonCapturingImage() {
        var image =
                imageFactory.make(new ImageDefinition(SIMPLE_GRADIENT_RELATIVE_LOCATION, false));

        assertNotNull(image);
        assertTrue(image.textureId() > 0);
        assertEquals(SIMPLE_GRADIENT_RELATIVE_LOCATION, image.relativeLocation());
        assertEquals(SIMPLE_GRADIENT_WIDTH, image.width());
        assertEquals(SIMPLE_GRADIENT_HEIGHT, image.height());

        assertFalse(image.supportsMouseEventCapturing());
        assertThrows(UnsupportedOperationException.class,
                () -> image.capturesMouseEventsAtPixel(0, 0));
    }

    @Test
    public void testLoadCapturingImage() {
        var image = imageFactory.make(new ImageDefinition(SIMPLE_GRADIENT_RELATIVE_LOCATION, true));

        assertNotNull(image);
        assertTrue(image.textureId() > 0);
        assertEquals(SIMPLE_GRADIENT_RELATIVE_LOCATION, image.relativeLocation());
        assertEquals(SIMPLE_GRADIENT_WIDTH, image.width());
        assertEquals(SIMPLE_GRADIENT_HEIGHT, image.height());

        assertTrue(image.supportsMouseEventCapturing());
        assertThrows(IllegalArgumentException.class,
                () -> image.capturesMouseEventsAtPixel(-1, 0));
        assertThrows(IllegalArgumentException.class,
                () -> image.capturesMouseEventsAtPixel(0, -1));
        assertThrows(IllegalArgumentException.class,
                () -> image.capturesMouseEventsAtPixel(image.width(), 0));
        assertThrows(IllegalArgumentException.class,
                () -> image.capturesMouseEventsAtPixel(0, image.height()));

        assertTrue(image.capturesMouseEventsAtPixel(99, 0));
        assertFalse(image.capturesMouseEventsAtPixel(100, 0));
        assertTrue(image.capturesMouseEventsAtPixel(99, 199));
        assertFalse(image.capturesMouseEventsAtPixel(100, 199));
    }
}
