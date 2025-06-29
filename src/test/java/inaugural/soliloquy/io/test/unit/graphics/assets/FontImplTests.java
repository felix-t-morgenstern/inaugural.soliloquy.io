package inaugural.soliloquy.io.test.unit.graphics.assets;

import inaugural.soliloquy.io.graphics.assets.FontImpl;
import inaugural.soliloquy.tools.Tools;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.io.graphics.assets.Font;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.FontDefinition;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.FontStyleDefinition;

import java.util.Map;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.GL_MAX_TEXTURE_SIZE;
import static org.lwjgl.opengl.GL11.glGetInteger;

// TODO: This test requires some refactoring.
public class FontImplTests {
    private final static String ID = "FontId";
    private final static String RELATIVE_LOCATION = "./src/test/resources/fonts/Trajan Pro Regular.ttf";
    private final static float MAX_LOSSLESS_FONT_SIZE = 300f;
    private final static float ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN = 0.123f;
    private final static float ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC = 0.234f;
    private final static float ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD = 0.345f;
    private final static float ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC = 0.456f;
    private final static Map<Character, Float>
            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN = mapOf();
    private final static Map<Character, Float>
            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC = mapOf();
    private final static Map<Character, Float>
            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD = mapOf();
    private final static Map<Character, Float>
            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC = mapOf();
    private final static Map<Character, Float>
            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN = mapOf();
    private final static Map<Character, Float>
            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC = mapOf();
    private final static Map<Character, Float>
            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD = mapOf();
    private final static Map<Character, Float>
            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC = mapOf();
    private final static float ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN = 0.567f;
    private final static float ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC = 0.678f;
    private final static float ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD = 0.789f;
    private final static float ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC = 0.890f;
    private final static float LEADING_ADJUSTMENT = 0.090f;
    private final static int ASCII_CHAR_SPACE = 32;
    private final static int ASCII_CHAR_DELETE = 127;
    private final static int NUMBER_EXTENDED_ASCII_CHARS = 256;

    private static Font Font;

    @BeforeAll
    public static void setUpFixture() {
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
                )
        );
    }

    @AfterAll
    public static void tearDownFixture() {
        glfwTerminate();
    }

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testConstructorWithInvalidArgs() {
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
                        )
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
                        )
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
                        )
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
                        )
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
                        )
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
                        )
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
                        )
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
                        )
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
                        )
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
                        )
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
                        )
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
                        )
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
                        )
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
                        )
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
                        )
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
                        )
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
                        )
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
                        )
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
                        )
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
                        )
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
                        )
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
                        )
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
                        )
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
                        )
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
                        )
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
                        )
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
                        )
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
                        )
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
                        )
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
                        )
                ));
    }

    @Test
    public void testId() {
        assertEquals(ID, Font.id());
    }

    @Test
    public void testTextureId() {
        assertNotEquals(0, Font.plain().textureId());
    }

    @Test
    public void testTextureIdItalic() {
        assertNotEquals(0, Font.italic().textureId());
        assertNotEquals(Font.plain().textureId(), Font.italic().textureId());
    }

    @Test
    public void testTextureIdBold() {
        assertNotEquals(0, Font.bold().textureId());
        assertNotEquals(Font.plain().textureId(), Font.bold().textureId());
        assertNotEquals(Font.italic().textureId(), Font.bold().textureId());
    }

    @Test
    public void testTextureIdBoldItalic() {
        assertNotEquals(0, Font.boldItalic().textureId());
        assertNotEquals(Font.plain().textureId(), Font.boldItalic().textureId());
        assertNotEquals(Font.italic().textureId(), Font.boldItalic().textureId());
        assertNotEquals(Font.bold().textureId(), Font.boldItalic().textureId());
    }

    @Test
    public void testGetUvCoordinatesForGlyphPlain() {
        var spaceUvCoordinatesPlain = Font.plain().getUvCoordinatesForGlyph(' ');
        var bangUvCoordinatesPlain = Font.plain().getUvCoordinatesForGlyph('!');

        assertNotNull(spaceUvCoordinatesPlain);
        assertNotNull(bangUvCoordinatesPlain);

        assertEquals(0f, spaceUvCoordinatesPlain.LEFT_X);
        assertEquals(0f, spaceUvCoordinatesPlain.TOP_Y);
        assertTrue(spaceUvCoordinatesPlain.RIGHT_X > 0);

        assertEquals(Tools.round(spaceUvCoordinatesPlain.RIGHT_X, 3),
                Tools.round(bangUvCoordinatesPlain.LEFT_X, 3));
        assertEquals(0f, bangUvCoordinatesPlain.TOP_Y);
        assertTrue(bangUvCoordinatesPlain.RIGHT_X > 0);
    }

    @Test
    public void testGetUvCoordinatesForGlyphItalic() {
        var spaceUvCoordinatesItalic = Font.italic().getUvCoordinatesForGlyph(' ');
        var bangUvCoordinatesItalic = Font.italic().getUvCoordinatesForGlyph('!');

        assertNotNull(spaceUvCoordinatesItalic);
        assertNotNull(bangUvCoordinatesItalic);

        assertEquals(0f, spaceUvCoordinatesItalic.LEFT_X);
        assertEquals(0f, spaceUvCoordinatesItalic.TOP_Y);
        assertTrue(spaceUvCoordinatesItalic.RIGHT_X > 0);

        assertEquals(Tools.round(spaceUvCoordinatesItalic.RIGHT_X, 3),
                Tools.round(bangUvCoordinatesItalic.LEFT_X, 3));
        assertEquals(0f, bangUvCoordinatesItalic.TOP_Y);
        assertTrue(bangUvCoordinatesItalic.RIGHT_X > 0);
    }

    @Test
    public void testGetUvCoordinatesForGlyphBold() {
        var spaceUvCoordinatesBold = Font.bold().getUvCoordinatesForGlyph(' ');
        var bangUvCoordinatesBold = Font.bold().getUvCoordinatesForGlyph('!');

        assertNotNull(spaceUvCoordinatesBold);
        assertNotNull(bangUvCoordinatesBold);

        assertEquals(0f, spaceUvCoordinatesBold.LEFT_X);
        assertEquals(0f, spaceUvCoordinatesBold.TOP_Y);
        assertTrue(spaceUvCoordinatesBold.RIGHT_X > 0);

        assertEquals(Tools.round(spaceUvCoordinatesBold.RIGHT_X, 3),
                Tools.round(bangUvCoordinatesBold.LEFT_X, 3));
        assertEquals(0f, bangUvCoordinatesBold.TOP_Y);
        assertTrue(bangUvCoordinatesBold.RIGHT_X > 0);
    }

    @Test
    public void testGetUvCoordinatesForGlyphBoldItalic() {
        var spaceUvCoordinatesBoldItalic = Font.boldItalic().getUvCoordinatesForGlyph(' ');
        var bangUvCoordinatesBoldItalic = Font.boldItalic().getUvCoordinatesForGlyph('!');

        assertNotNull(spaceUvCoordinatesBoldItalic);
        assertNotNull(bangUvCoordinatesBoldItalic);

        assertEquals(0f, spaceUvCoordinatesBoldItalic.LEFT_X);
        assertEquals(0f, spaceUvCoordinatesBoldItalic.TOP_Y);
        assertTrue(spaceUvCoordinatesBoldItalic.RIGHT_X > 0);

        assertEquals(Tools.round(spaceUvCoordinatesBoldItalic.RIGHT_X, 3),
                Tools.round(bangUvCoordinatesBoldItalic.LEFT_X, 3));
        assertEquals(0f, bangUvCoordinatesBoldItalic.TOP_Y);
        assertTrue(bangUvCoordinatesBoldItalic.RIGHT_X > 0);
    }

    @Test
    public void testGlyphwiseAdditionalLeftBoundaryShift() {
        Map<Character, Float> glyphwiseAdditionalLeftBoundaryShiftPlain = mapOf();
        Map<Character, Float> glyphwiseAdditionalLeftBoundaryShiftItalic = mapOf();
        Map<Character, Float> glyphwiseAdditionalLeftBoundaryShiftBold = mapOf();
        Map<Character, Float> glyphwiseAdditionalLeftBoundaryShiftBoldItalic = mapOf();

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
                )
        );

        var glyphBoxPlain_i = Font.plain().getUvCoordinatesForGlyph('i');
        var glyphBoxPlain_j = Font.plain().getUvCoordinatesForGlyph('j');
        var glyphBoxItalic_i = Font.italic().getUvCoordinatesForGlyph('i');
        var glyphBoxItalic_j = Font.italic().getUvCoordinatesForGlyph('j');
        var glyphBoxBold_i = Font.bold().getUvCoordinatesForGlyph('i');
        var glyphBoxBold_j = Font.bold().getUvCoordinatesForGlyph('j');
        var glyphBoxBoldItalic_i = Font.boldItalic().getUvCoordinatesForGlyph('i');
        var glyphBoxBoldItalic_j = Font.boldItalic().getUvCoordinatesForGlyph('j');

        var glyphBoxWithLeftBoundaryShiftPlain_i =
                fontWithGlyphwiseAdditionalLeftBoundaryShift.plain()
                        .getUvCoordinatesForGlyph('i');
        var glyphBoxWithLeftBoundaryShiftPlain_j =
                fontWithGlyphwiseAdditionalLeftBoundaryShift.plain()
                        .getUvCoordinatesForGlyph('j');
        var glyphBoxWithLeftBoundaryShiftItalic_i =
                fontWithGlyphwiseAdditionalLeftBoundaryShift.italic()
                        .getUvCoordinatesForGlyph('i');
        var glyphBoxWithLeftBoundaryShiftItalic_j =
                fontWithGlyphwiseAdditionalLeftBoundaryShift.italic()
                        .getUvCoordinatesForGlyph('j');
        var glyphBoxWithLeftBoundaryShiftBold_i =
                fontWithGlyphwiseAdditionalLeftBoundaryShift.bold()
                        .getUvCoordinatesForGlyph('i');
        var glyphBoxWithLeftBoundaryShiftBold_j =
                fontWithGlyphwiseAdditionalLeftBoundaryShift.bold()
                        .getUvCoordinatesForGlyph('j');
        var glyphBoxWithLeftBoundaryShiftBoldItalic_i =
                fontWithGlyphwiseAdditionalLeftBoundaryShift.boldItalic()
                        .getUvCoordinatesForGlyph('i');
        var glyphBoxWithLeftBoundaryShiftBoldItalic_j =
                fontWithGlyphwiseAdditionalLeftBoundaryShift.boldItalic()
                        .getUvCoordinatesForGlyph('j');

        assertEquals(
                Tools.round((glyphBoxPlain_i.RIGHT_X -
                        (glyphBoxPlain_i.height() * plainLeftBoundaryShift)), 4),
                Tools.round(glyphBoxWithLeftBoundaryShiftPlain_i.RIGHT_X, 4));
        assertEquals(
                Tools.round((glyphBoxPlain_j.LEFT_X -
                        (glyphBoxPlain_j.height() * plainLeftBoundaryShift)), 4),
                Tools.round(glyphBoxWithLeftBoundaryShiftPlain_j.LEFT_X, 4));
        assertEquals(
                Tools.round((glyphBoxItalic_i.RIGHT_X -
                        (glyphBoxItalic_i.height() * italicLeftBoundaryShift)), 4),
                Tools.round(glyphBoxWithLeftBoundaryShiftItalic_i.RIGHT_X, 4));
        assertEquals(
                Tools.round((glyphBoxItalic_j.LEFT_X -
                        (glyphBoxItalic_j.height() * italicLeftBoundaryShift)), 4),
                Tools.round(glyphBoxWithLeftBoundaryShiftItalic_j.LEFT_X, 4));
        assertEquals(
                Tools.round((glyphBoxBold_i.RIGHT_X -
                        (glyphBoxBold_i.height() * boldLeftBoundaryShift)), 4),
                Tools.round(glyphBoxWithLeftBoundaryShiftBold_i.RIGHT_X, 4));
        assertEquals(
                Tools.round((glyphBoxBold_j.LEFT_X -
                        (glyphBoxBold_j.height() * boldLeftBoundaryShift)), 4),
                Tools.round(glyphBoxWithLeftBoundaryShiftBold_j.LEFT_X, 4));
        assertEquals(
                Tools.round((glyphBoxBoldItalic_i.RIGHT_X -
                        (glyphBoxBoldItalic_i.height() * boldItalicLeftBoundaryShift)), 4),
                Tools.round(glyphBoxWithLeftBoundaryShiftBoldItalic_i.RIGHT_X, 4));
        assertEquals(
                Tools.round((glyphBoxBoldItalic_j.LEFT_X -
                        (glyphBoxBoldItalic_j.height() * boldItalicLeftBoundaryShift)), 4),
                Tools.round(glyphBoxWithLeftBoundaryShiftBoldItalic_j.LEFT_X, 4));
    }

    @Test
    public void testGetUvCoordinatesForGlyphWithInvalidParameters() {
        assertThrows(IllegalArgumentException.class,
                () -> Font.plain().getUvCoordinatesForGlyph((char) (ASCII_CHAR_SPACE - 1)));
        assertThrows(IllegalArgumentException.class,
                () -> Font.plain().getUvCoordinatesForGlyph((char) ASCII_CHAR_DELETE));
        assertThrows(IllegalArgumentException.class,
                () -> Font.plain().getUvCoordinatesForGlyph((char) NUMBER_EXTENDED_ASCII_CHARS));
    }

    @Test
    public void testGetUvCoordinatesForGlyphItalicWithInvalidParameters() {
        assertThrows(IllegalArgumentException.class,
                () -> Font.italic().getUvCoordinatesForGlyph((char) (ASCII_CHAR_SPACE - 1)));
        assertThrows(IllegalArgumentException.class,
                () -> Font.italic().getUvCoordinatesForGlyph((char) ASCII_CHAR_DELETE));
        assertThrows(IllegalArgumentException.class,
                () -> Font.italic().getUvCoordinatesForGlyph((char) NUMBER_EXTENDED_ASCII_CHARS));
    }

    @Test
    public void testGetUvCoordinatesForGlyphBoldWithInvalidParameters() {
        assertThrows(IllegalArgumentException.class,
                () -> Font.bold().getUvCoordinatesForGlyph((char) (ASCII_CHAR_SPACE - 1)));
        assertThrows(IllegalArgumentException.class,
                () -> Font.bold().getUvCoordinatesForGlyph((char) ASCII_CHAR_DELETE));
        assertThrows(IllegalArgumentException.class,
                () -> Font.bold().getUvCoordinatesForGlyph((char) NUMBER_EXTENDED_ASCII_CHARS));
    }

    @Test
    public void testGetUvCoordinatesForGlyphBoldItalicWithInvalidParameters() {
        assertThrows(IllegalArgumentException.class,
                () -> Font.bold().getUvCoordinatesForGlyph((char) (ASCII_CHAR_SPACE - 1)));
        assertThrows(IllegalArgumentException.class,
                () -> Font.bold().getUvCoordinatesForGlyph((char) ASCII_CHAR_DELETE));
        assertThrows(IllegalArgumentException.class,
                () -> Font.bold().getUvCoordinatesForGlyph((char) NUMBER_EXTENDED_ASCII_CHARS));
    }

    @Test
    public void testGlyphwiseAdditionalHorizontalTextureSpacingPlain() {
        assertSame(GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                Font.plain().glyphwiseAdditionalHorizontalTextureSpacing());
    }

    @Test
    public void testGlyphwiseAdditionalHorizontalTextureSpacingItalic() {
        assertSame(GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                Font.italic().glyphwiseAdditionalHorizontalTextureSpacing());
    }

    @Test
    public void testGlyphwiseAdditionalHorizontalTextureSpacingBold() {
        assertSame(GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                Font.bold().glyphwiseAdditionalHorizontalTextureSpacing());
    }

    @Test
    public void testGlyphwiseAdditionalHorizontalTextureSpacingBoldItalic() {
        assertSame(GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                Font.boldItalic().glyphwiseAdditionalHorizontalTextureSpacing());
    }

    @Test
    public void testDimensionsAndWidthToHeightRatios() {
        int maxDimensions = glGetInteger(GL_MAX_TEXTURE_SIZE);

        assertTrue(Font.plain().textureDimensions().X <= maxDimensions);
        assertEquals(Font.plain().textureWidthToHeightRatio(),
                Font.plain().textureDimensions().X /
                        (float) Font.plain().textureDimensions().Y);

        assertTrue(Font.italic().textureDimensions().X <= maxDimensions);
        assertEquals(Font.italic().textureWidthToHeightRatio(),
                Font.italic().textureDimensions().X /
                        (float) Font.italic().textureDimensions().Y);

        assertTrue(Font.bold().textureDimensions().X <= maxDimensions);
        assertEquals(Font.bold().textureWidthToHeightRatio(),
                Font.bold().textureDimensions().X /
                        (float) Font.bold().textureDimensions().Y);

        assertTrue(Font.boldItalic().textureDimensions().X <= maxDimensions);
        assertEquals(Font.boldItalic().textureWidthToHeightRatio(),
                Font.boldItalic().textureDimensions().X /
                        (float) Font.boldItalic().textureDimensions().Y);
    }
}
