package inaugural.soliloquy.graphics.test.unit.bootstrap.assetfactories;

import inaugural.soliloquy.common.test.fakes.FakeCoordinateFactory;
import inaugural.soliloquy.graphics.assets.FontImpl;
import inaugural.soliloquy.graphics.bootstrap.assetfactories.FontFactory;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeFloatBoxFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.assets.Font;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontDefinition;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontStyleDefinition;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;

class FontFactoryTests {
    private final String ID = "FontId";
    private final String RELATIVE_LOCATION = "./res/fonts/Trajan Pro Regular.ttf";
    private final float MAX_LOSSLESS_FONT_SIZE = 12.3f;
    private final float ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN = 0.123f;
    private final float ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC = 0.234f;
    private final float ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD = 0.345f;
    private final float ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC = 0.456f;
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
    private final float ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN = 0.567f;
    private final float ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC = 0.678f;
    private final float ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD = 0.789f;
    private final float ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC = 0.890f;
    private final float LEADING_ADJUSTMENT = 0.090f;
    private final FakeFloatBoxFactory FLOAT_BOX_FACTORY = new FakeFloatBoxFactory();
    private final FakeCoordinateFactory COORDINATE_FACTORY = new FakeCoordinateFactory();

    private AssetFactory<FontDefinition, Font> _fontFactory;

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
        _fontFactory = new FontFactory(FLOAT_BOX_FACTORY, COORDINATE_FACTORY);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new FontFactory(null, COORDINATE_FACTORY));
        assertThrows(IllegalArgumentException.class,
                () -> new FontFactory(FLOAT_BOX_FACTORY, null));
    }

    @Test
    void testMake() {
        FontDefinition fontDefinition = new FontDefinition(
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
                ));

        Font createdFont = _fontFactory.make(fontDefinition);

        assertNotNull(createdFont);
        assertEquals(ID, createdFont.id());
        assertTrue(createdFont instanceof FontImpl);
    }

    @Test
    void testMakeWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _fontFactory.make(
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
                        ))));
        assertThrows(IllegalArgumentException.class, () -> _fontFactory.make(
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
                        ))));
        assertThrows(IllegalArgumentException.class, () -> _fontFactory.make(
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
                        ))));
        assertThrows(IllegalArgumentException.class, () -> _fontFactory.make(
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
                        ))));
        assertThrows(IllegalArgumentException.class, () -> _fontFactory.make(
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
                        ))));
        assertThrows(IllegalArgumentException.class, () -> _fontFactory.make(
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
                        ))));
        assertThrows(IllegalArgumentException.class, () -> _fontFactory.make(
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
                        ))));
        assertThrows(IllegalArgumentException.class, () -> _fontFactory.make(
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
                        ))));
        assertThrows(IllegalArgumentException.class, () -> _fontFactory.make(
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
                        ))));
        assertThrows(IllegalArgumentException.class, () -> _fontFactory.make(
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
                        ))));
        assertThrows(IllegalArgumentException.class, () -> _fontFactory.make(
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
                        ))));
        assertThrows(IllegalArgumentException.class, () -> _fontFactory.make(
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
                        ))));
        assertThrows(IllegalArgumentException.class, () -> _fontFactory.make(
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
                        ))));
        assertThrows(IllegalArgumentException.class, () -> _fontFactory.make(
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
                        ))));
        assertThrows(IllegalArgumentException.class, () -> _fontFactory.make(
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
                        ))));
        assertThrows(IllegalArgumentException.class, () -> _fontFactory.make(
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
                        ))));
        assertThrows(IllegalArgumentException.class, () -> _fontFactory.make(
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
                        ))));
        assertThrows(IllegalArgumentException.class, () -> _fontFactory.make(
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
                        ))));
        assertThrows(IllegalArgumentException.class, () -> _fontFactory.make(
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
                        ))));
        assertThrows(IllegalArgumentException.class, () -> _fontFactory.make(
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
                        ))));
        assertThrows(IllegalArgumentException.class, () -> _fontFactory.make(
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
                        ))));
        assertThrows(IllegalArgumentException.class, () -> _fontFactory.make(
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
                        ))));
        assertThrows(IllegalArgumentException.class, () -> _fontFactory.make(
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
                        ))));
        assertThrows(IllegalArgumentException.class, () -> _fontFactory.make(
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
                        ))));
        assertThrows(IllegalArgumentException.class, () -> _fontFactory.make(
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
                        null)));
        assertThrows(IllegalArgumentException.class, () -> _fontFactory.make(
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
                        ))));
        assertThrows(IllegalArgumentException.class, () -> _fontFactory.make(
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
                        ))));
        assertThrows(IllegalArgumentException.class, () -> _fontFactory.make(
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
                        ))));
        assertThrows(IllegalArgumentException.class, () -> _fontFactory.make(
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
                        ))));
        assertThrows(IllegalArgumentException.class, () -> _fontFactory.make(
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
                        ))));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(AssetFactory.class.getCanonicalName() + "<" +
                        FontDefinition.class.getCanonicalName() + "," + Font.class.getCanonicalName() +
                        ">",
                _fontFactory.getInterfaceName());
    }
}
