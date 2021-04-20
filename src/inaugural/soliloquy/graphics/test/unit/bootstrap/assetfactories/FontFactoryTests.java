package inaugural.soliloquy.graphics.test.unit.bootstrap.assetfactories;

import inaugural.soliloquy.graphics.assets.FontImpl;
import inaugural.soliloquy.graphics.bootstrap.assetfactories.FontFactory;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.assets.Font;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontDefinition;

import static org.junit.jupiter.api.Assertions.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;

class FontFactoryTests {
    private final String ID = "FontId";
    private final String RELATIVE_LOCATION = "./res/fonts/Trajan Pro Regular.ttf";
    private final float MAX_LOSSLESS_FONT_SIZE = 12.3f;
    private final float ADDITIONAL_GLYPH_HORIZONTAL_PADDING = 0.123f;
    private final float ADDITIONAL_GLYPH_VERTICAL_PADDING = 0.456f;
    private final float LEADING_ADJUSTMENT = 0.456f;
    private final FakeFloatBoxFactory FLOAT_BOX_FACTORY = new FakeFloatBoxFactory();

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
        _fontFactory = new FontFactory(FLOAT_BOX_FACTORY);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> new FontFactory(null));
    }

    @Test
    void testMake() {
        FakeFontDefinition fontDefinition = new FakeFontDefinition(ID, RELATIVE_LOCATION,
                MAX_LOSSLESS_FONT_SIZE, ADDITIONAL_GLYPH_HORIZONTAL_PADDING, null,
                ADDITIONAL_GLYPH_VERTICAL_PADDING, LEADING_ADJUSTMENT);

        Font createdFont = _fontFactory.make(fontDefinition);

        assertNotNull(createdFont);
        assertEquals(ID, createdFont.id());
        assertTrue(createdFont instanceof FontImpl);
    }

    @Test
    void testMakeWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _fontFactory.make(
                new FakeFontDefinition(null, RELATIVE_LOCATION, MAX_LOSSLESS_FONT_SIZE,
                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING, null,
                        ADDITIONAL_GLYPH_VERTICAL_PADDING, LEADING_ADJUSTMENT)));
        assertThrows(IllegalArgumentException.class, () -> _fontFactory.make(
                new FakeFontDefinition("", RELATIVE_LOCATION, MAX_LOSSLESS_FONT_SIZE,
                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING, null,
                        ADDITIONAL_GLYPH_VERTICAL_PADDING, LEADING_ADJUSTMENT)));
        assertThrows(IllegalArgumentException.class, () -> _fontFactory.make(
                new FakeFontDefinition(ID, null, MAX_LOSSLESS_FONT_SIZE,
                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING, null,
                        ADDITIONAL_GLYPH_VERTICAL_PADDING, LEADING_ADJUSTMENT)));
        assertThrows(IllegalArgumentException.class, () -> _fontFactory.make(
                new FakeFontDefinition(ID, "", MAX_LOSSLESS_FONT_SIZE,
                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING, null,
                        ADDITIONAL_GLYPH_VERTICAL_PADDING, LEADING_ADJUSTMENT)));
        assertThrows(IllegalArgumentException.class, () -> _fontFactory.make(
                new FakeFontDefinition(ID, RELATIVE_LOCATION, 0,
                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING, null,
                        ADDITIONAL_GLYPH_VERTICAL_PADDING, LEADING_ADJUSTMENT)));
        assertThrows(IllegalArgumentException.class, () -> _fontFactory.make(
                new FakeFontDefinition(ID, RELATIVE_LOCATION, MAX_LOSSLESS_FONT_SIZE,
                        -0.0001f, null,
                        ADDITIONAL_GLYPH_VERTICAL_PADDING, LEADING_ADJUSTMENT)));
        assertThrows(IllegalArgumentException.class, () -> _fontFactory.make(
                new FakeFontDefinition(ID, RELATIVE_LOCATION, MAX_LOSSLESS_FONT_SIZE,
                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING, null,
                        -0.0001f, LEADING_ADJUSTMENT)));
        assertThrows(IllegalArgumentException.class, () -> _fontFactory.make(
                new FakeFontDefinition(ID, RELATIVE_LOCATION, MAX_LOSSLESS_FONT_SIZE,
                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING, null,
                        ADDITIONAL_GLYPH_VERTICAL_PADDING, -0.0001f)));
        assertThrows(IllegalArgumentException.class, () -> _fontFactory.make(
                new FakeFontDefinition(ID, RELATIVE_LOCATION, MAX_LOSSLESS_FONT_SIZE,
                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING, null,
                        ADDITIONAL_GLYPH_VERTICAL_PADDING, 1f)));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(AssetFactory.class.getCanonicalName() + "<" +
                FontDefinition.class.getCanonicalName() + "," + Font.class.getCanonicalName() +
                ">",
                _fontFactory.getInterfaceName());
    }
}
