package inaugural.soliloquy.graphics.test.unit.assets;

import inaugural.soliloquy.common.test.fakes.FakeCoordinateFactory;
import inaugural.soliloquy.graphics.assets.FontImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.assets.Font;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.GL_MAX_TEXTURE_SIZE;
import static org.lwjgl.opengl.GL11.glGetInteger;

class FontImplTests {
    private final static String ID = "FontId";
    private final static String RELATIVE_LOCATION = "./res/fonts/Trajan Pro Regular.ttf";
    private final static float MAX_LOSSLESS_FONT_SIZE = 300f;
    private final static float ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN = 0.123f;
    private final static float ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC = 0.234f;
    private final static float ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD = 0.345f;
    private final static float ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC = 0.456f;
    private final static Map<Character, Float> GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_PLAIN =
            new HashMap<>();
    private final static Map<Character, Float> GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_ITALIC =
            new HashMap<>();
    private final static Map<Character, Float> GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD =
            new HashMap<>();
    private final static Map<Character, Float> GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD_ITALIC =
            new HashMap<>();
    private final static float ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN = 0.567f;
    private final static float ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC = 0.678f;
    private final static float ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD = 0.789f;
    private final static float ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC = 0.890f;
    private final static float LEADING_ADJUSTMENT = 0.090f;
    private final static FakeFloatBoxFactory FLOAT_BOX_FACTORY = new FakeFloatBoxFactory();
    private final static FakeCoordinateFactory COORDINATE_FACTORY = new FakeCoordinateFactory();
    private final static int ASCII_CHAR_SPACE = 32;
    private final static int ASCII_CHAR_DELETE = 127;
    private final static int NUMBER_EXTENDED_ASCII_CHARS = 256;

    private static Font Font;

    @BeforeAll
    static void setUpFixture() {
        if (!glfwInit()) {
            throw new RuntimeException("GLFW failed to initialize");
        }

        long window = glfwCreateWindow(1, 1, "", 0, 0);
        glfwMakeContextCurrent(window);
        createCapabilities();

        Font = new FontImpl(
                new FakeFontDefinition(
                        ID,
                        RELATIVE_LOCATION,
                        MAX_LOSSLESS_FONT_SIZE,
                        new FakeFontStyleDefinition(
                                ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_PLAIN,
                                ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                        ),
                        new FakeFontStyleDefinition(
                                ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_ITALIC,
                                ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                        ),
                        new FakeFontStyleDefinition(
                                ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD,
                                ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                        ),
                        new FakeFontStyleDefinition(
                                ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD_ITALIC,
                                ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                        ),
                        LEADING_ADJUSTMENT
                ),
                FLOAT_BOX_FACTORY,
                COORDINATE_FACTORY
        );
    }

    @AfterAll
    static void tearDownFixture() {
        glfwTerminate();
    }

    @BeforeEach
    void setUp() {
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FakeFontDefinition(
                                null,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FakeFontDefinition(
                                "",
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FakeFontDefinition(
                                ID,
                                null,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FakeFontDefinition(
                                ID,
                                "",
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                0f,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                null,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        -0.0001f,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_PLAIN,
                                        -0.0001f
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_PLAIN,
                                        1f - LEADING_ADJUSTMENT
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                null,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        -0.0001f,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_ITALIC,
                                        -0.0001f
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_ITALIC,
                                        1f - LEADING_ADJUSTMENT
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                null,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        -0.0001f,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD,
                                        -0.0001f
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD,
                                        1f - LEADING_ADJUSTMENT
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                null,
                                LEADING_ADJUSTMENT
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        -0.0001f,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        -0.0001f
                                ),
                                LEADING_ADJUSTMENT
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        1f - LEADING_ADJUSTMENT
                                ),
                                LEADING_ADJUSTMENT
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                                ),
                                1f
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT
                        ),
                        null,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT
                        ),
                        FLOAT_BOX_FACTORY,
                        null
                ));
    }

    @Test
    void testId() {
        assertEquals(ID, Font.id());
    }

    @Test
    void testTextureId() {
        assertNotEquals(0, Font.plain().textureId());
    }

    @Test
    void testTextureIdItalic() {
        assertNotEquals(0, Font.italic().textureId());
        assertNotEquals(Font.plain().textureId(), Font.italic().textureId());
    }

    @Test
    void testTextureIdBold() {
        assertNotEquals(0, Font.bold().textureId());
        assertNotEquals(Font.plain().textureId(), Font.bold().textureId());
        assertNotEquals(Font.italic().textureId(), Font.bold().textureId());
    }

    @Test
    void testTextureIdBoldItalic() {
        assertNotEquals(0, Font.boldItalic().textureId());
        assertNotEquals(Font.plain().textureId(), Font.boldItalic().textureId());
        assertNotEquals(Font.italic().textureId(), Font.boldItalic().textureId());
        assertNotEquals(Font.bold().textureId(), Font.boldItalic().textureId());
    }

    @Test
    void testGetUvCoordinatesForGlyphPlain() {
        FloatBox spaceUvCoordinatesPlain = Font.plain().getUvCoordinatesForGlyph(' ');
        FloatBox bangUvCoordinatesPlain = Font.plain().getUvCoordinatesForGlyph('!');

        assertNotNull(spaceUvCoordinatesPlain);
        assertNotNull(bangUvCoordinatesPlain);

        assertEquals(0f, spaceUvCoordinatesPlain.leftX());
        assertEquals(0f, spaceUvCoordinatesPlain.topY());
        assertTrue(spaceUvCoordinatesPlain.rightX() > 0);

        assertEquals(Math.round(spaceUvCoordinatesPlain.rightX() * 1000f) / 1000f,
                Math.round(bangUvCoordinatesPlain.leftX() * 1000f) / 1000f);
        assertEquals(0f, bangUvCoordinatesPlain.topY());
        assertTrue(bangUvCoordinatesPlain.rightX() > 0);
    }

    @Test
    void testGetUvCoordinatesForGlyphItalic() {
        FloatBox spaceUvCoordinatesItalic = Font.italic().getUvCoordinatesForGlyph(' ');
        FloatBox bangUvCoordinatesItalic = Font.italic().getUvCoordinatesForGlyph('!');

        assertNotNull(spaceUvCoordinatesItalic);
        assertNotNull(bangUvCoordinatesItalic);

        assertEquals(0f, spaceUvCoordinatesItalic.leftX());
        assertEquals(0f, spaceUvCoordinatesItalic.topY());
        assertTrue(spaceUvCoordinatesItalic.rightX() > 0);

        assertEquals(Math.round(spaceUvCoordinatesItalic.rightX() * 1000f) / 1000f,
                Math.round(bangUvCoordinatesItalic.leftX() * 1000f) / 1000f);
        assertEquals(0f, bangUvCoordinatesItalic.topY());
        assertTrue(bangUvCoordinatesItalic.rightX() > 0);
    }

    @Test
    void testGetUvCoordinatesForGlyphBold() {
        FloatBox spaceUvCoordinatesBold = Font.bold().getUvCoordinatesForGlyph(' ');
        FloatBox bangUvCoordinatesBold = Font.bold().getUvCoordinatesForGlyph('!');

        assertNotNull(spaceUvCoordinatesBold);
        assertNotNull(bangUvCoordinatesBold);

        assertEquals(0f, spaceUvCoordinatesBold.leftX());
        assertEquals(0f, spaceUvCoordinatesBold.topY());
        assertTrue(spaceUvCoordinatesBold.rightX() > 0);

        assertEquals(Math.round(spaceUvCoordinatesBold.rightX() * 1000f) / 1000f,
                Math.round(bangUvCoordinatesBold.leftX() * 1000f) / 1000f);
        assertEquals(0f, bangUvCoordinatesBold.topY());
        assertTrue(bangUvCoordinatesBold.rightX() > 0);
    }

    @Test
    void testGetUvCoordinatesForGlyphBoldItalic() {
        FloatBox spaceUvCoordinatesBoldItalic = Font.boldItalic().getUvCoordinatesForGlyph(' ');
        FloatBox bangUvCoordinatesBoldItalic = Font.boldItalic().getUvCoordinatesForGlyph('!');

        assertNotNull(spaceUvCoordinatesBoldItalic);
        assertNotNull(bangUvCoordinatesBoldItalic);

        assertEquals(0f, spaceUvCoordinatesBoldItalic.leftX());
        assertEquals(0f, spaceUvCoordinatesBoldItalic.topY());
        assertTrue(spaceUvCoordinatesBoldItalic.rightX() > 0);

        assertEquals(Math.round(spaceUvCoordinatesBoldItalic.rightX() * 1000f) / 1000f,
                Math.round(bangUvCoordinatesBoldItalic.leftX() * 1000f) / 1000f);
        assertEquals(0f, bangUvCoordinatesBoldItalic.topY());
        assertTrue(bangUvCoordinatesBoldItalic.rightX() > 0);
    }

    @Test
    void testGetUvCoordinatesForGlyphWithInvalidParameters() {
        assertThrows(IllegalArgumentException.class,
                () -> Font.plain().getUvCoordinatesForGlyph((char)(ASCII_CHAR_SPACE - 1)));
        assertThrows(IllegalArgumentException.class,
                () -> Font.plain().getUvCoordinatesForGlyph((char)ASCII_CHAR_DELETE));
        assertThrows(IllegalArgumentException.class,
                () -> Font.plain().getUvCoordinatesForGlyph((char)NUMBER_EXTENDED_ASCII_CHARS));
    }

    @Test
    void testGetUvCoordinatesForGlyphItalicWithInvalidParameters() {
        assertThrows(IllegalArgumentException.class,
                () -> Font.italic().getUvCoordinatesForGlyph((char)(ASCII_CHAR_SPACE - 1)));
        assertThrows(IllegalArgumentException.class,
                () -> Font.italic().getUvCoordinatesForGlyph((char)ASCII_CHAR_DELETE));
        assertThrows(IllegalArgumentException.class,
                () -> Font.italic().getUvCoordinatesForGlyph((char)NUMBER_EXTENDED_ASCII_CHARS));
    }

    @Test
    void testGetUvCoordinatesForGlyphBoldWithInvalidParameters() {
        assertThrows(IllegalArgumentException.class,
                () -> Font.bold().getUvCoordinatesForGlyph((char)(ASCII_CHAR_SPACE - 1)));
        assertThrows(IllegalArgumentException.class,
                () -> Font.bold().getUvCoordinatesForGlyph((char)ASCII_CHAR_DELETE));
        assertThrows(IllegalArgumentException.class,
                () -> Font.bold().getUvCoordinatesForGlyph((char)NUMBER_EXTENDED_ASCII_CHARS));
    }

    @Test
    void testGetUvCoordinatesForGlyphBoldItalicWithInvalidParameters() {
        assertThrows(IllegalArgumentException.class,
                () -> Font.bold().getUvCoordinatesForGlyph((char)(ASCII_CHAR_SPACE - 1)));
        assertThrows(IllegalArgumentException.class,
                () -> Font.bold().getUvCoordinatesForGlyph((char)ASCII_CHAR_DELETE));
        assertThrows(IllegalArgumentException.class,
                () -> Font.bold().getUvCoordinatesForGlyph((char)NUMBER_EXTENDED_ASCII_CHARS));
    }

    @Test
    void testGlyphwiseAdditionalHorizontalPaddingPlain() {
        assertSame(GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_PLAIN,
                Font.plain().glyphwiseAdditionalHorizontalPadding());
    }

    @Test
    void testGlyphwiseAdditionalHorizontalPaddingItalic() {
        assertSame(GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_ITALIC,
                Font.italic().glyphwiseAdditionalHorizontalPadding());
    }

    @Test
    void testGlyphwiseAdditionalHorizontalPaddingBold() {
        assertSame(GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD,
                Font.bold().glyphwiseAdditionalHorizontalPadding());
    }

    @Test
    void testGlyphwiseAdditionalHorizontalPaddingBoldItalic() {
        assertSame(GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING_BOLD_ITALIC,
                Font.boldItalic().glyphwiseAdditionalHorizontalPadding());
    }

    @Test
    void testDimensionsAndWidthToHeightRatios() {
        int maxDimensions = glGetInteger(GL_MAX_TEXTURE_SIZE);

        assertTrue(Font.plain().textureDimensions().getX() <= maxDimensions);
        assertEquals(Font.plain().textureWidthToHeightRatio(),
                Font.plain().textureDimensions().getX() /
                        (float) Font.plain().textureDimensions().getY());

        assertTrue(Font.italic().textureDimensions().getX() <= maxDimensions);
        assertEquals(Font.italic().textureWidthToHeightRatio(),
                Font.italic().textureDimensions().getX() /
                        (float) Font.italic().textureDimensions().getY());

        assertTrue(Font.bold().textureDimensions().getX() <= maxDimensions);
        assertEquals(Font.bold().textureWidthToHeightRatio(),
                Font.bold().textureDimensions().getX() /
                        (float) Font.bold().textureDimensions().getY());

        assertTrue(Font.boldItalic().textureDimensions().getX() <= maxDimensions);
        assertEquals(Font.boldItalic().textureWidthToHeightRatio(),
                Font.boldItalic().textureDimensions().getX() /
                        (float) Font.boldItalic().textureDimensions().getY());
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(Font.class.getCanonicalName(), Font.getInterfaceName());
    }
}
