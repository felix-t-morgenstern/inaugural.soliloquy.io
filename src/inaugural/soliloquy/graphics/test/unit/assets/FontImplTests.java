package inaugural.soliloquy.graphics.test.unit.assets;

import inaugural.soliloquy.graphics.assets.FontImpl;
import inaugural.soliloquy.graphics.test.fakes.FakeFloatBoxFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.assets.Font;
import soliloquy.specs.graphics.rendering.FloatBox;

import static org.junit.jupiter.api.Assertions.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;

class FontImplTests {
    private final String ID = "FontId";
    private final String RELATIVE_LOCATION = "./res/fonts/Trajan Pro Regular.ttf";
    private final float MAX_LOSSLESS_FONT_SIZE = 12.3f;
    private final float ADDITIONAL_GLYPH_HORIZONTAL_PADDING = 0.123f;
    private final float ADDITIONAL_GLYPH_VERTICAL_PADDING = 0.123f;
    private final float LEADING_ADJUSTMENT = 0.456f;
    private final int IMAGE_WIDTH = 123;
    private final int IMAGE_HEIGHT = 2340;
    private final FakeFloatBoxFactory FLOAT_BOX_FACTORY = new FakeFloatBoxFactory();
    private final static int ASCII_CHAR_SPACE = 32;
    private final static int ASCII_CHAR_DELETE = 127;
    private final static int NUMBER_EXTENDED_ASCII_CHARS = 256;

    private Font _font;

    @BeforeAll
    static void setUpFixture() {
        if (!glfwInit()) {
            throw new RuntimeException("GLFW failed to initialize");
        }

        long window = glfwCreateWindow(1, 1, "", 0, 0);
        glfwMakeContextCurrent(window);
        createCapabilities();
    }

    @AfterAll
    static void tearDownFixture() {
        glfwTerminate();
    }

    @BeforeEach
    void setUp() {
        _font = new FontImpl(ID, RELATIVE_LOCATION, MAX_LOSSLESS_FONT_SIZE,
                ADDITIONAL_GLYPH_HORIZONTAL_PADDING, ADDITIONAL_GLYPH_VERTICAL_PADDING,
                LEADING_ADJUSTMENT, IMAGE_WIDTH, IMAGE_HEIGHT, FLOAT_BOX_FACTORY);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(null, RELATIVE_LOCATION, MAX_LOSSLESS_FONT_SIZE,
                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING, ADDITIONAL_GLYPH_VERTICAL_PADDING,
                        LEADING_ADJUSTMENT, IMAGE_WIDTH, IMAGE_HEIGHT, FLOAT_BOX_FACTORY));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl("", RELATIVE_LOCATION, MAX_LOSSLESS_FONT_SIZE,
                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING, ADDITIONAL_GLYPH_VERTICAL_PADDING,
                        LEADING_ADJUSTMENT, IMAGE_WIDTH, IMAGE_HEIGHT, FLOAT_BOX_FACTORY));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(ID, null, MAX_LOSSLESS_FONT_SIZE,
                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING, ADDITIONAL_GLYPH_VERTICAL_PADDING,
                        LEADING_ADJUSTMENT, IMAGE_WIDTH, IMAGE_HEIGHT, FLOAT_BOX_FACTORY));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(ID, "", MAX_LOSSLESS_FONT_SIZE,
                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING, ADDITIONAL_GLYPH_VERTICAL_PADDING,
                        LEADING_ADJUSTMENT, IMAGE_WIDTH, IMAGE_HEIGHT, FLOAT_BOX_FACTORY));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(ID, RELATIVE_LOCATION, 0f,
                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING, ADDITIONAL_GLYPH_VERTICAL_PADDING,
                        LEADING_ADJUSTMENT, IMAGE_WIDTH, IMAGE_HEIGHT, FLOAT_BOX_FACTORY));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(ID, RELATIVE_LOCATION, MAX_LOSSLESS_FONT_SIZE,
                        -0.001f, ADDITIONAL_GLYPH_VERTICAL_PADDING,
                        LEADING_ADJUSTMENT, IMAGE_WIDTH, IMAGE_HEIGHT, FLOAT_BOX_FACTORY));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(ID, RELATIVE_LOCATION, MAX_LOSSLESS_FONT_SIZE,
                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING, -0.001f,
                        LEADING_ADJUSTMENT, IMAGE_WIDTH, IMAGE_HEIGHT, FLOAT_BOX_FACTORY));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(ID, RELATIVE_LOCATION, MAX_LOSSLESS_FONT_SIZE,
                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING, ADDITIONAL_GLYPH_VERTICAL_PADDING,
                        -0.001f, IMAGE_WIDTH, IMAGE_HEIGHT, FLOAT_BOX_FACTORY));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(ID, RELATIVE_LOCATION, MAX_LOSSLESS_FONT_SIZE,
                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING, ADDITIONAL_GLYPH_VERTICAL_PADDING,
                        1f, IMAGE_WIDTH, IMAGE_HEIGHT, FLOAT_BOX_FACTORY));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(ID, RELATIVE_LOCATION, MAX_LOSSLESS_FONT_SIZE,
                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING, ADDITIONAL_GLYPH_VERTICAL_PADDING,
                        LEADING_ADJUSTMENT, 0, IMAGE_HEIGHT, FLOAT_BOX_FACTORY));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(ID, RELATIVE_LOCATION, MAX_LOSSLESS_FONT_SIZE,
                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING, ADDITIONAL_GLYPH_VERTICAL_PADDING,
                        LEADING_ADJUSTMENT, IMAGE_WIDTH, 0, FLOAT_BOX_FACTORY));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(ID, RELATIVE_LOCATION, MAX_LOSSLESS_FONT_SIZE,
                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING, ADDITIONAL_GLYPH_VERTICAL_PADDING,
                        LEADING_ADJUSTMENT, IMAGE_WIDTH, IMAGE_HEIGHT, null));
    }

    @Test
    void testId() {
        assertEquals(ID, _font.id());
    }

    @Test
    void testTextureId() {
        assertNotEquals(0, _font.textureId());
    }

    @Test
    void testTextureIdItalic() {
        assertNotEquals(0, _font.textureIdItalic());
        assertNotEquals(_font.textureId(), _font.textureIdItalic());
    }

    @Test
    void testTextureIdBold() {
        assertNotEquals(0, _font.textureIdBold());
        assertNotEquals(_font.textureId(), _font.textureIdBold());
        assertNotEquals(_font.textureIdItalic(), _font.textureIdBold());
    }

    @Test
    void testTextureIdBoldItalic() {
        assertNotEquals(0, _font.textureIdBoldItalic());
        assertNotEquals(_font.textureId(), _font.textureIdBoldItalic());
        assertNotEquals(_font.textureIdItalic(), _font.textureIdBoldItalic());
        assertNotEquals(_font.textureIdBold(), _font.textureIdBoldItalic());
    }

    @Test
    void testGetUvCoordinatesForGlyph() {
        FloatBox spaceUvCoordinates = _font.getUvCoordinatesForGlyph(' ');
        FloatBox bangUvCoordinates = _font.getUvCoordinatesForGlyph('!');

        FloatBox firstCharacterOnSecondLine = null;
        for (int i = ASCII_CHAR_SPACE; i < NUMBER_EXTENDED_ASCII_CHARS; i++) {
            if (i == ASCII_CHAR_DELETE) {
                continue;
            }
            FloatBox glyphUvCoordinates = _font.getUvCoordinatesForGlyph((char)i);
            if (glyphUvCoordinates.topY() > 0f) {
                firstCharacterOnSecondLine = glyphUvCoordinates;
                break;
            }
        }

        assertNotNull(spaceUvCoordinates);
        assertNotNull(bangUvCoordinates);
        assertNotNull(firstCharacterOnSecondLine);

        assertEquals(0f, spaceUvCoordinates.leftX());
        assertEquals(0f, spaceUvCoordinates.topY());
        assertTrue(spaceUvCoordinates.rightX() > 0);

        assertEquals(spaceUvCoordinates.rightX(), bangUvCoordinates.leftX());
        assertEquals(0f, bangUvCoordinates.topY());
        assertTrue(bangUvCoordinates.rightX() > 0);

        assertEquals(0f, firstCharacterOnSecondLine.leftX());
        assertEquals(spaceUvCoordinates.bottomY(), firstCharacterOnSecondLine.topY());
        assertEquals(spaceUvCoordinates.bottomY() * 2, firstCharacterOnSecondLine.bottomY());
    }

    @Test
    void testGetUvCoordinatesForGlyphItalic() {
        FloatBox spaceUvCoordinatesItalic = _font.getUvCoordinatesForGlyphItalic(' ');
        FloatBox bangUvCoordinatesItalic = _font.getUvCoordinatesForGlyphItalic('!');

        FloatBox lastUvCoordinates =
                _font.getUvCoordinatesForGlyph((char)(NUMBER_EXTENDED_ASCII_CHARS - 1));
        FloatBox lastUvCoordinatesItalic =
                _font.getUvCoordinatesForGlyphItalic((char)(NUMBER_EXTENDED_ASCII_CHARS - 1));

        FloatBox firstCharacterOnSecondLine = null;
        for (int i = ASCII_CHAR_SPACE; i < NUMBER_EXTENDED_ASCII_CHARS; i++) {
            if (i == ASCII_CHAR_DELETE) {
                continue;
            }
            FloatBox glyphUvCoordinates = _font.getUvCoordinatesForGlyphItalic((char)i);
            if (glyphUvCoordinates.topY() > 0f) {
                firstCharacterOnSecondLine = glyphUvCoordinates;
                break;
            }
        }

        assertNotNull(spaceUvCoordinatesItalic);
        assertNotNull(bangUvCoordinatesItalic);
        assertNotNull(firstCharacterOnSecondLine);

        assertEquals(0f, spaceUvCoordinatesItalic.leftX());
        assertEquals(0f, spaceUvCoordinatesItalic.topY());
        assertTrue(spaceUvCoordinatesItalic.rightX() > 0);

        assertEquals(spaceUvCoordinatesItalic.rightX(), bangUvCoordinatesItalic.leftX());
        assertEquals(0f, bangUvCoordinatesItalic.topY());
        assertTrue(bangUvCoordinatesItalic.rightX() > 0);

        assertEquals(0f, firstCharacterOnSecondLine.leftX());
        assertEquals(spaceUvCoordinatesItalic.bottomY(), firstCharacterOnSecondLine.topY());
        assertEquals(spaceUvCoordinatesItalic.bottomY() * 2, firstCharacterOnSecondLine.bottomY());

        assertNotEquals(lastUvCoordinates.rightX(), lastUvCoordinatesItalic.rightX());
        assertNotEquals(lastUvCoordinates.bottomY(), lastUvCoordinatesItalic.bottomY());
    }

    @Test
    void testGetUvCoordinatesForGlyphBold() {
        FloatBox spaceUvCoordinatesBold = _font.getUvCoordinatesForGlyphBold(' ');
        FloatBox bangUvCoordinatesBold = _font.getUvCoordinatesForGlyphBold('!');

        FloatBox lastUvCoordinates =
                _font.getUvCoordinatesForGlyph((char)(NUMBER_EXTENDED_ASCII_CHARS - 1));
        FloatBox lastUvCoordinatesItalic =
                _font.getUvCoordinatesForGlyphItalic((char)(NUMBER_EXTENDED_ASCII_CHARS - 1));
        FloatBox lastUvCoordinatesBold =
                _font.getUvCoordinatesForGlyphBold((char)(NUMBER_EXTENDED_ASCII_CHARS - 1));

        FloatBox firstCharacterOnSecondLine = null;
        for (int i = ASCII_CHAR_SPACE; i < NUMBER_EXTENDED_ASCII_CHARS; i++) {
            if (i == ASCII_CHAR_DELETE) {
                continue;
            }
            FloatBox glyphUvCoordinates = _font.getUvCoordinatesForGlyphBold((char)i);
            if (glyphUvCoordinates.topY() > 0f) {
                firstCharacterOnSecondLine = glyphUvCoordinates;
                break;
            }
        }

        assertNotNull(spaceUvCoordinatesBold);
        assertNotNull(bangUvCoordinatesBold);
        assertNotNull(firstCharacterOnSecondLine);

        assertEquals(0f, spaceUvCoordinatesBold.leftX());
        assertEquals(0f, spaceUvCoordinatesBold.topY());
        assertTrue(spaceUvCoordinatesBold.rightX() > 0);

        assertEquals(spaceUvCoordinatesBold.rightX(), bangUvCoordinatesBold.leftX());
        assertEquals(0f, bangUvCoordinatesBold.topY());
        assertTrue(bangUvCoordinatesBold.rightX() > 0);

        assertEquals(0f, firstCharacterOnSecondLine.leftX());
        assertEquals(spaceUvCoordinatesBold.bottomY(), firstCharacterOnSecondLine.topY());
        assertEquals(spaceUvCoordinatesBold.bottomY() * 2, firstCharacterOnSecondLine.bottomY());

        assertNotEquals(lastUvCoordinates.rightX(), lastUvCoordinatesBold.rightX());
        assertNotEquals(lastUvCoordinates.bottomY(), lastUvCoordinatesBold.bottomY());
        // NB: These tests are commented out, because I couldn't find any fonts whose italic widths
        // were different from their bold widths; look to display tests to verify this
        // functionality
//        assertNotEquals(lastUvCoordinatesItalic.rightX(), lastUvCoordinatesBold.rightX());
//        assertNotEquals(lastUvCoordinatesItalic.bottomY(), lastUvCoordinatesBold.bottomY());
    }

    @Test
    void testGetUvCoordinatesForGlyphBoldItalic() {
        FloatBox spaceUvCoordinatesBoldItalic = _font.getUvCoordinatesForGlyphBoldItalic(' ');
        FloatBox bangUvCoordinatesBoldItalic = _font.getUvCoordinatesForGlyphBoldItalic('!');

        FloatBox lastUvCoordinates =
                _font.getUvCoordinatesForGlyph((char)(NUMBER_EXTENDED_ASCII_CHARS - 1));
        FloatBox lastUvCoordinatesItalic =
                _font.getUvCoordinatesForGlyphItalic((char)(NUMBER_EXTENDED_ASCII_CHARS - 1));
        FloatBox lastUvCoordinatesBold =
                _font.getUvCoordinatesForGlyphBold((char)(NUMBER_EXTENDED_ASCII_CHARS - 1));
        FloatBox lastUvCoordinatesBoldItalic =
                _font.getUvCoordinatesForGlyphBoldItalic((char)(NUMBER_EXTENDED_ASCII_CHARS - 1));

        FloatBox firstCharacterOnSecondLine = null;
        for (int i = ASCII_CHAR_SPACE; i < NUMBER_EXTENDED_ASCII_CHARS; i++) {
            if (i == ASCII_CHAR_DELETE) {
                continue;
            }
            FloatBox glyphUvCoordinates = _font.getUvCoordinatesForGlyphBoldItalic((char)i);
            if (glyphUvCoordinates.topY() > 0f) {
                firstCharacterOnSecondLine = glyphUvCoordinates;
                break;
            }
        }

        assertNotNull(spaceUvCoordinatesBoldItalic);
        assertNotNull(bangUvCoordinatesBoldItalic);
        assertNotNull(firstCharacterOnSecondLine);

        assertEquals(0f, spaceUvCoordinatesBoldItalic.leftX());
        assertEquals(0f, spaceUvCoordinatesBoldItalic.topY());
        assertTrue(spaceUvCoordinatesBoldItalic.rightX() > 0);

        assertEquals(spaceUvCoordinatesBoldItalic.rightX(), bangUvCoordinatesBoldItalic.leftX());
        assertEquals(0f, bangUvCoordinatesBoldItalic.topY());
        assertTrue(bangUvCoordinatesBoldItalic.rightX() > 0);

        assertEquals(0f, firstCharacterOnSecondLine.leftX());
        assertEquals(spaceUvCoordinatesBoldItalic.bottomY(), firstCharacterOnSecondLine.topY());
        assertEquals(spaceUvCoordinatesBoldItalic.bottomY() * 2, firstCharacterOnSecondLine.bottomY());

        assertNotEquals(lastUvCoordinates.rightX(), lastUvCoordinatesBoldItalic.rightX());
        assertNotEquals(lastUvCoordinates.bottomY(), lastUvCoordinatesBoldItalic.bottomY());
        // NB: These tests are commented out, because I couldn't find any fonts whose italic widths
        // or bold widths were different from their bold-italic widths; look to display tests to
        // verify this functionality
//        assertNotEquals(lastUvCoordinatesItalic.rightX(), lastUvCoordinatesBoldItalic.rightX());
//        assertNotEquals(lastUvCoordinatesItalic.bottomY(), lastUvCoordinatesBoldItalic.bottomY());
//        assertNotEquals(lastUvCoordinatesBold.rightX(), lastUvCoordinatesBoldItalic.rightX());
//        assertNotEquals(lastUvCoordinatesBold.bottomY(), lastUvCoordinatesBoldItalic.bottomY());
    }

    @Test
    void testGetUvCoordinatesForGlyphWithInvalidParameters() {
        assertThrows(IllegalArgumentException.class,
                () -> _font.getUvCoordinatesForGlyph((char)(ASCII_CHAR_SPACE - 1)));
        assertThrows(IllegalArgumentException.class,
                () -> _font.getUvCoordinatesForGlyph((char)ASCII_CHAR_DELETE));
        assertThrows(IllegalArgumentException.class,
                () -> _font.getUvCoordinatesForGlyph((char)NUMBER_EXTENDED_ASCII_CHARS));
    }

    @Test
    void testGetUvCoordinatesForGlyphItalicWithInvalidParameters() {
        assertThrows(IllegalArgumentException.class,
                () -> _font.getUvCoordinatesForGlyphItalic((char)(ASCII_CHAR_SPACE - 1)));
        assertThrows(IllegalArgumentException.class,
                () -> _font.getUvCoordinatesForGlyphItalic((char)ASCII_CHAR_DELETE));
        assertThrows(IllegalArgumentException.class,
                () -> _font.getUvCoordinatesForGlyphItalic((char)NUMBER_EXTENDED_ASCII_CHARS));
    }

    @Test
    void testGetUvCoordinatesForGlyphBoldWithInvalidParameters() {
        assertThrows(IllegalArgumentException.class,
                () -> _font.getUvCoordinatesForGlyphBold((char)(ASCII_CHAR_SPACE - 1)));
        assertThrows(IllegalArgumentException.class,
                () -> _font.getUvCoordinatesForGlyphBold((char)ASCII_CHAR_DELETE));
        assertThrows(IllegalArgumentException.class,
                () -> _font.getUvCoordinatesForGlyphBold((char)NUMBER_EXTENDED_ASCII_CHARS));
    }

    @Test
    void testGetUvCoordinatesForGlyphBoldItalicWithInvalidParameters() {
        assertThrows(IllegalArgumentException.class,
                () -> _font.getUvCoordinatesForGlyphBold((char)(ASCII_CHAR_SPACE - 1)));
        assertThrows(IllegalArgumentException.class,
                () -> _font.getUvCoordinatesForGlyphBold((char)ASCII_CHAR_DELETE));
        assertThrows(IllegalArgumentException.class,
                () -> _font.getUvCoordinatesForGlyphBold((char)NUMBER_EXTENDED_ASCII_CHARS));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(Font.class.getCanonicalName(), _font.getInterfaceName());
    }
}
