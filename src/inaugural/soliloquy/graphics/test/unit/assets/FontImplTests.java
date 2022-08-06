package inaugural.soliloquy.graphics.test.unit.assets;

import inaugural.soliloquy.common.test.fakes.FakeCoordinateFactory;
import inaugural.soliloquy.graphics.assets.FontImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import inaugural.soliloquy.tools.Tools;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.assets.Font;
import soliloquy.specs.graphics.assets.FontStyleInfo;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontDefinition;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontStyleDefinition;
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
    private final static float ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN = 0.123f;
    private final static float ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC = 0.234f;
    private final static float ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD = 0.345f;
    private final static float ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC = 0.456f;
    private final static Map<Character, Float>
            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN = new HashMap<>();
    private final static Map<Character, Float>
            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC = new HashMap<>();
    private final static Map<Character, Float>
            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD = new HashMap<>();
    private final static Map<Character, Float>
            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC = new HashMap<>();
    private final static Map<Character, Float>
            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN = new HashMap<>();
    private final static Map<Character, Float>
            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC = new HashMap<>();
    private final static Map<Character, Float>
            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD = new HashMap<>();
    private final static Map<Character, Float>
            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC = new HashMap<>();
    private final static float ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN = 0.567f;
    private final static float ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC = 0.678f;
    private final static float ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD = 0.789f;
    private final static float ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC = 0.890f;
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
                new FontDefinition(
                        ID,
                        RELATIVE_LOCATION,
                        MAX_LOSSLESS_FONT_SIZE,
                        LEADING_ADJUSTMENT,
                        new FontStyleDefinition(
                                ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                        ),
                        new FontStyleDefinition(
                                ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                        ),
                        new FontStyleDefinition(
                                ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                        ),
                        new FontStyleDefinition(
                                ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                        )
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
                        new FontDefinition(
                                null,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                LEADING_ADJUSTMENT,
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                )
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FontDefinition(
                                "",
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                LEADING_ADJUSTMENT,
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                )
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FontDefinition(
                                ID,
                                null,
                                MAX_LOSSLESS_FONT_SIZE,
                                LEADING_ADJUSTMENT,
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                )
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FontDefinition(
                                ID,
                                "",
                                MAX_LOSSLESS_FONT_SIZE,
                                LEADING_ADJUSTMENT,
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                )
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                0f,
                                LEADING_ADJUSTMENT,
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                )
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                LEADING_ADJUSTMENT,
                                null,
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                )
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                LEADING_ADJUSTMENT,
                                new FontStyleDefinition(
                                        -0.0001f,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                )
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                LEADING_ADJUSTMENT,
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        null,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                )
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                LEADING_ADJUSTMENT,
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                )
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                LEADING_ADJUSTMENT,
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        -0.0001f
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                )
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                LEADING_ADJUSTMENT,
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        1f - LEADING_ADJUSTMENT
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                )
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                LEADING_ADJUSTMENT,
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                null,
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                )
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                LEADING_ADJUSTMENT,
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FontStyleDefinition(
                                        -0.0001f,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                )
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                LEADING_ADJUSTMENT,
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        null,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                )
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                LEADING_ADJUSTMENT,
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                )
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                LEADING_ADJUSTMENT,
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        -0.0001f
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                )
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                LEADING_ADJUSTMENT,
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        1f - LEADING_ADJUSTMENT
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                )
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                LEADING_ADJUSTMENT,
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                null,
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                )
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                LEADING_ADJUSTMENT,
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FontStyleDefinition(
                                        -0.0001f,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                )
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                LEADING_ADJUSTMENT,
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        null,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                )
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                LEADING_ADJUSTMENT,
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                )
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                LEADING_ADJUSTMENT,
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        -0.0001f
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                )
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                LEADING_ADJUSTMENT,
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        1f - LEADING_ADJUSTMENT
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                )
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                LEADING_ADJUSTMENT,
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                null
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                LEADING_ADJUSTMENT,
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FontStyleDefinition(
                                        -0.0001f,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                )
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                LEADING_ADJUSTMENT,
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        null,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                )
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                LEADING_ADJUSTMENT,
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                )
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                LEADING_ADJUSTMENT,
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        -0.0001f
                                )
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                LEADING_ADJUSTMENT,
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        1f - LEADING_ADJUSTMENT
                                )
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                1f,
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                )
                        ),
                        FLOAT_BOX_FACTORY,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                LEADING_ADJUSTMENT,
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                )
                        ),
                        null,
                        COORDINATE_FACTORY
                ));
        assertThrows(IllegalArgumentException.class,
                () -> new FontImpl(
                        new FontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                LEADING_ADJUSTMENT,
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                )
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

        assertEquals(Tools.round(spaceUvCoordinatesPlain.rightX(), 3),
                Tools.round(bangUvCoordinatesPlain.leftX(), 3));
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

        assertEquals(Tools.round(spaceUvCoordinatesItalic.rightX(), 3),
                Tools.round(bangUvCoordinatesItalic.leftX(), 3));
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

        assertEquals(Tools.round(spaceUvCoordinatesBold.rightX(), 3),
                Tools.round(bangUvCoordinatesBold.leftX(), 3));
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

        assertEquals(Tools.round(spaceUvCoordinatesBoldItalic.rightX(), 3),
                Tools.round(bangUvCoordinatesBoldItalic.leftX(), 3));
        assertEquals(0f, bangUvCoordinatesBoldItalic.topY());
        assertTrue(bangUvCoordinatesBoldItalic.rightX() > 0);
    }

    @Test
    void testGlyphwiseAdditionalLeftBoundaryShift() {
        HashMap<Character, Float> glyphwiseAdditionalLeftBoundaryShiftPlain = new HashMap<>();
        HashMap<Character, Float> glyphwiseAdditionalLeftBoundaryShiftItalic = new HashMap<>();
        HashMap<Character, Float> glyphwiseAdditionalLeftBoundaryShiftBold = new HashMap<>();
        HashMap<Character, Float> glyphwiseAdditionalLeftBoundaryShiftBoldItalic = new HashMap<>();

        float plainLeftBoundaryShift = 0.1f;
        float italicLeftBoundaryShift = 0.2f;
        float boldLeftBoundaryShift = 0.3f;
        float boldItalicLeftBoundaryShift = 0.4f;

        glyphwiseAdditionalLeftBoundaryShiftPlain.put('j', plainLeftBoundaryShift);
        glyphwiseAdditionalLeftBoundaryShiftItalic.put('j', italicLeftBoundaryShift);
        glyphwiseAdditionalLeftBoundaryShiftBold.put('j', boldLeftBoundaryShift);
        glyphwiseAdditionalLeftBoundaryShiftBoldItalic.put('j', boldItalicLeftBoundaryShift);

        Font fontWithGlyphwiseAdditionalLeftBoundaryShift = new FontImpl(
                new FontDefinition(
                        ID,
                        RELATIVE_LOCATION,
                        MAX_LOSSLESS_FONT_SIZE,
                        LEADING_ADJUSTMENT,
                        new FontStyleDefinition(
                                ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                glyphwiseAdditionalLeftBoundaryShiftPlain,
                                ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                        ),
                        new FontStyleDefinition(
                                ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                glyphwiseAdditionalLeftBoundaryShiftItalic,
                                ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                        ),
                        new FontStyleDefinition(
                                ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                glyphwiseAdditionalLeftBoundaryShiftBold,
                                ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                        ),
                        new FontStyleDefinition(
                                ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                glyphwiseAdditionalLeftBoundaryShiftBoldItalic,
                                ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                        )
                ),
                FLOAT_BOX_FACTORY,
                COORDINATE_FACTORY
        );

        FloatBox glyphBoxPlain_i = Font.plain().getUvCoordinatesForGlyph('i');
        FloatBox glyphBoxPlain_j = Font.plain().getUvCoordinatesForGlyph('j');
        FloatBox glyphBoxItalic_i = Font.italic().getUvCoordinatesForGlyph('i');
        FloatBox glyphBoxItalic_j = Font.italic().getUvCoordinatesForGlyph('j');
        FloatBox glyphBoxBold_i = Font.bold().getUvCoordinatesForGlyph('i');
        FloatBox glyphBoxBold_j = Font.bold().getUvCoordinatesForGlyph('j');
        FloatBox glyphBoxBoldItalic_i = Font.boldItalic().getUvCoordinatesForGlyph('i');
        FloatBox glyphBoxBoldItalic_j = Font.boldItalic().getUvCoordinatesForGlyph('j');

        FloatBox glyphBoxWithLeftBoundaryShiftPlain_i =
                fontWithGlyphwiseAdditionalLeftBoundaryShift.plain()
                        .getUvCoordinatesForGlyph('i');
        FloatBox glyphBoxWithLeftBoundaryShiftPlain_j =
                fontWithGlyphwiseAdditionalLeftBoundaryShift.plain()
                        .getUvCoordinatesForGlyph('j');
        FloatBox glyphBoxWithLeftBoundaryShiftItalic_i =
                fontWithGlyphwiseAdditionalLeftBoundaryShift.italic()
                        .getUvCoordinatesForGlyph('i');
        FloatBox glyphBoxWithLeftBoundaryShiftItalic_j =
                fontWithGlyphwiseAdditionalLeftBoundaryShift.italic()
                        .getUvCoordinatesForGlyph('j');
        FloatBox glyphBoxWithLeftBoundaryShiftBold_i =
                fontWithGlyphwiseAdditionalLeftBoundaryShift.bold()
                        .getUvCoordinatesForGlyph('i');
        FloatBox glyphBoxWithLeftBoundaryShiftBold_j =
                fontWithGlyphwiseAdditionalLeftBoundaryShift.bold()
                        .getUvCoordinatesForGlyph('j');
        FloatBox glyphBoxWithLeftBoundaryShiftBoldItalic_i =
                fontWithGlyphwiseAdditionalLeftBoundaryShift.boldItalic()
                        .getUvCoordinatesForGlyph('i');
        FloatBox glyphBoxWithLeftBoundaryShiftBoldItalic_j =
                fontWithGlyphwiseAdditionalLeftBoundaryShift.boldItalic()
                        .getUvCoordinatesForGlyph('j');

        assertEquals(
                Tools.round((glyphBoxPlain_i.rightX() -
                        (glyphBoxPlain_i.height() * plainLeftBoundaryShift)), 4),
                Tools.round(glyphBoxWithLeftBoundaryShiftPlain_i.rightX(), 4));
        assertEquals(
                Tools.round((glyphBoxPlain_j.leftX() -
                        (glyphBoxPlain_j.height() * plainLeftBoundaryShift)), 4),
                Tools.round(glyphBoxWithLeftBoundaryShiftPlain_j.leftX(), 4));
        assertEquals(
                Tools.round((glyphBoxItalic_i.rightX() -
                        (glyphBoxItalic_i.height() * italicLeftBoundaryShift)), 4),
                Tools.round(glyphBoxWithLeftBoundaryShiftItalic_i.rightX(), 4));
        assertEquals(
                Tools.round((glyphBoxItalic_j.leftX() -
                        (glyphBoxItalic_j.height() * italicLeftBoundaryShift)), 4),
                Tools.round(glyphBoxWithLeftBoundaryShiftItalic_j.leftX(), 4));
        assertEquals(
                Tools.round((glyphBoxBold_i.rightX() -
                        (glyphBoxBold_i.height() * boldLeftBoundaryShift)), 4),
                Tools.round(glyphBoxWithLeftBoundaryShiftBold_i.rightX(), 4));
        assertEquals(
                Tools.round((glyphBoxBold_j.leftX() -
                        (glyphBoxBold_j.height() * boldLeftBoundaryShift)), 4),
                Tools.round(glyphBoxWithLeftBoundaryShiftBold_j.leftX(), 4));
        assertEquals(
                Tools.round((glyphBoxBoldItalic_i.rightX() -
                        (glyphBoxBoldItalic_i.height() * boldItalicLeftBoundaryShift)), 4),
                Tools.round(glyphBoxWithLeftBoundaryShiftBoldItalic_i.rightX(), 4));
        assertEquals(
                Tools.round((glyphBoxBoldItalic_j.leftX() -
                        (glyphBoxBoldItalic_j.height() * boldItalicLeftBoundaryShift)), 4),
                Tools.round(glyphBoxWithLeftBoundaryShiftBoldItalic_j.leftX(), 4));
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
    void testGlyphwiseAdditionalHorizontalTextureSpacingPlain() {
        assertSame(GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                Font.plain().glyphwiseAdditionalHorizontalTextureSpacing());
    }

    @Test
    void testGlyphwiseAdditionalHorizontalTextureSpacingItalic() {
        assertSame(GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                Font.italic().glyphwiseAdditionalHorizontalTextureSpacing());
    }

    @Test
    void testGlyphwiseAdditionalHorizontalTextureSpacingBold() {
        assertSame(GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                Font.bold().glyphwiseAdditionalHorizontalTextureSpacing());
    }

    @Test
    void testGlyphwiseAdditionalHorizontalTextureSpacingBoldItalic() {
        assertSame(GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                Font.boldItalic().glyphwiseAdditionalHorizontalTextureSpacing());
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
    void testFontStyleInfoGetInterfaceName() {
        assertEquals(FontStyleInfo.class.getCanonicalName(), Font.plain().getInterfaceName());
        assertEquals(FontStyleInfo.class.getCanonicalName(), Font.italic().getInterfaceName());
        assertEquals(FontStyleInfo.class.getCanonicalName(), Font.bold().getInterfaceName());
        assertEquals(FontStyleInfo.class.getCanonicalName(), Font.boldItalic().getInterfaceName());
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(Font.class.getCanonicalName(), Font.getInterfaceName());
    }
}
