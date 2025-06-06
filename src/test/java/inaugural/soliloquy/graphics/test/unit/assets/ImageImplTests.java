package inaugural.soliloquy.graphics.test.unit.assets;

import inaugural.soliloquy.graphics.assets.ImageImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.assets.Image;

import static org.junit.jupiter.api.Assertions.*;

public class ImageImplTests {
    protected final static int BITS_PER_LONG = 64;

    private final int TEXTURE_ID = 123;
    private final String RELATIVE_LOCATION = "relativeLocation";
    private final int WIDTH = 456;
    private final int HEIGHT = 789;

    private Image image;
    private Image imageWithMask;

    @BeforeEach
    public void setUp() {
        var capturingMask = new long[1][1];
        capturingMask[0][0] = Long.parseUnsignedLong(
                "1000000000000000000000000000000000000000000000000000000000000000", 2);

        image = new ImageImpl(TEXTURE_ID, RELATIVE_LOCATION, WIDTH, HEIGHT);
        imageWithMask = new ImageImpl(TEXTURE_ID, RELATIVE_LOCATION, WIDTH, HEIGHT,
                capturingMask);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new ImageImpl(0, RELATIVE_LOCATION, WIDTH, HEIGHT));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageImpl(TEXTURE_ID, null, WIDTH, HEIGHT));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageImpl(TEXTURE_ID, "", WIDTH, HEIGHT));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageImpl(TEXTURE_ID, RELATIVE_LOCATION, 0, HEIGHT));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageImpl(TEXTURE_ID, RELATIVE_LOCATION, WIDTH, 0));

        assertThrows(IllegalArgumentException.class,
                () -> new ImageImpl(0, RELATIVE_LOCATION, WIDTH, HEIGHT, new long[1][1]));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageImpl(TEXTURE_ID, null, WIDTH, HEIGHT, new long[1][1]));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageImpl(TEXTURE_ID, "", WIDTH, HEIGHT, new long[1][1]));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageImpl(TEXTURE_ID, RELATIVE_LOCATION, 0, HEIGHT, new long[1][1]));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageImpl(TEXTURE_ID, RELATIVE_LOCATION, WIDTH, 0, new long[1][1]));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageImpl(TEXTURE_ID, RELATIVE_LOCATION, WIDTH, HEIGHT, null));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageImpl(TEXTURE_ID, RELATIVE_LOCATION, WIDTH, HEIGHT, new long[0][1]));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageImpl(TEXTURE_ID, RELATIVE_LOCATION, WIDTH, HEIGHT, new long[1][0]));
    }

    @Test
    public void testTextureId() {
        assertEquals(TEXTURE_ID, image.textureId());
        assertEquals(TEXTURE_ID, imageWithMask.textureId());
    }

    @Test
    public void testRelativeLocation() {
        assertEquals(RELATIVE_LOCATION, image.relativeLocation());
        assertEquals(RELATIVE_LOCATION, imageWithMask.relativeLocation());
    }

    @Test
    public void testWidth() {
        assertEquals(WIDTH, image.width());
        assertEquals(WIDTH, imageWithMask.width());
    }

    @Test
    public void testHeight() {
        assertEquals(HEIGHT, image.height());
        assertEquals(HEIGHT, imageWithMask.height());
    }

    @Test
    public void testSupportsMouseEventCapturing() {
        assertFalse(image.supportsMouseEventCapturing());
        assertTrue(imageWithMask.supportsMouseEventCapturing());
    }

    @Test
    public void testCapturesMouseEventsAtPixel() {
        assertThrows(UnsupportedOperationException.class,
                () -> image.capturesMouseEventsAtPixel(0, 0));

        assertTrue(imageWithMask.capturesMouseEventsAtPixel(0, 0));
        assertFalse(imageWithMask.capturesMouseEventsAtPixel(1, 0));
    }
}
