package inaugural.soliloquy.graphics.test.unit.bootstrap.tasks;

import inaugural.soliloquy.graphics.bootstrap.tasks.FontPreloaderTask;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.assets.Font;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FontPreloaderTaskTests {
    private final FakeFontFactory FONT_FACTORY = new FakeFontFactory();
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

    @Test
    void testConstructorWithInvalidParams() {
        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        ArrayList<Font> fonts = new ArrayList<>();

        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        null,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        FONT_FACTORY,
                        null,
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                null,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                "",
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                null,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                "",
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                0f,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                null,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        -0.0001f,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        null,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        -0.0001f
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        1f - LEADING_ADJUSTMENT
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                null,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        -0.0001f,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        null,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        -0.0001f
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        1f - LEADING_ADJUSTMENT
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                null,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        -0.0001f,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        null,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        -0.0001f
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        1f - LEADING_ADJUSTMENT
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                null,
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        -0.0001f,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        null,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        -0.0001f
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        1f - LEADING_ADJUSTMENT
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                ),
                                1f),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        null));
    }

    @Test
    void testRun() {
        ArrayList<Font> fonts = new ArrayList<>();

        FakeFontDefinition fontDefinition = new FakeFontDefinition(
                ID,
                RELATIVE_LOCATION,
                MAX_LOSSLESS_FONT_SIZE,
                new FakeFontStyleDefinition(
                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                ),
                new FakeFontStyleDefinition(
                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                ),
                new FakeFontStyleDefinition(
                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                ),
                new FakeFontStyleDefinition(
                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                ),
                LEADING_ADJUSTMENT);

        FontPreloaderTask fontPreloaderTask = new FontPreloaderTask(FONT_FACTORY,
                fontDefinition, fonts::add);

        fontPreloaderTask.run();

        assertEquals(1, FONT_FACTORY.INPUTS.size());
        assertEquals(1, FONT_FACTORY.OUTPUTS.size());
        FontDefinition input = FONT_FACTORY.INPUTS.get(0);
        assertNotNull(input);
        assertSame(fontDefinition, input);
        assertSame(FONT_FACTORY.OUTPUTS.get(0), fonts.get(0));
    }
}
