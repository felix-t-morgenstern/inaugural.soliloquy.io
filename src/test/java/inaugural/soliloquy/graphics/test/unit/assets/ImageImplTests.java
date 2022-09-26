package inaugural.soliloquy.graphics.test.unit.assets;

import inaugural.soliloquy.graphics.assets.ImageImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.assets.Image;

import static org.junit.jupiter.api.Assertions.*;

class ImageImplTests {
    protected final static int BITS_PER_LONG = 64;

    private final int TEXTURE_ID = 123;
    private final String RELATIVE_LOCATION = "relativeLocation";
    private final int WIDTH = 456;
    private final int HEIGHT = 789;

    private Image _image;
    private Image _imageWithMask;
    private long[][] _capturingMask;

    @BeforeEach
    void setUp() {
        _capturingMask = new long[1][1];
        _capturingMask[0][0] = Long.parseUnsignedLong(
                "1000000000000000000000000000000000000000000000000000000000000000", 2);

        _image = new ImageImpl(TEXTURE_ID, RELATIVE_LOCATION, WIDTH, HEIGHT);
        _imageWithMask = new ImageImpl(TEXTURE_ID, RELATIVE_LOCATION, WIDTH, HEIGHT,
                _capturingMask);
    }

    @Test
    void testConstructorWithInvalidParams() {
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
    void testTextureId() {
        assertEquals(TEXTURE_ID, _image.textureId());
        assertEquals(TEXTURE_ID, _imageWithMask.textureId());
    }

    @Test
    void testRelativeLocation() {
        assertEquals(RELATIVE_LOCATION, _image.relativeLocation());
        assertEquals(RELATIVE_LOCATION, _imageWithMask.relativeLocation());
    }

    @Test
    void testWidth() {
        assertEquals(WIDTH, _image.width());
        assertEquals(WIDTH, _imageWithMask.width());
    }

    @Test
    void testHeight() {
        assertEquals(HEIGHT, _image.height());
        assertEquals(HEIGHT, _imageWithMask.height());
    }

    @Test
    void testSupportsMouseEventCapturing() {
        assertFalse(_image.supportsMouseEventCapturing());
        assertTrue(_imageWithMask.supportsMouseEventCapturing());
    }

    @Test
    void testCapturesMouseEventsAtPixel() {
        assertThrows(UnsupportedOperationException.class,
                () -> _image.capturesMouseEventsAtPixel(0, 0));

        assertTrue(_imageWithMask.capturesMouseEventsAtPixel(0, 0));
        assertFalse(_imageWithMask.capturesMouseEventsAtPixel(1, 0));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(Image.class.getCanonicalName(), _image.getInterfaceName());
    }
}
