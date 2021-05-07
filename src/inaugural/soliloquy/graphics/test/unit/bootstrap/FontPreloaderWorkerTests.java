package inaugural.soliloquy.graphics.test.unit.bootstrap;

import inaugural.soliloquy.graphics.bootstrap.FontPreloaderWorker;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.assets.Font;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontDefinition;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FontPreloaderWorkerTests {
    private final FakeFontFactory FONT_FACTORY = new FakeFontFactory();
    private final String ID = "FontId";
    private final String RELATIVE_LOCATION = "./res/fonts/Trajan Pro Regular.ttf";
    private final float MAX_LOSSLESS_FONT_SIZE = 12.3f;
    private final float ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN = 0.123f;
    private final float ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC = 0.234f;
    private final float ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD = 0.345f;
    private final float ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC = 0.456f;
    private final float ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN = 0.567f;
    private final float ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC = 0.678f;
    private final float ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD = 0.789f;
    private final float ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC = 0.890f;
    private final float LEADING_ADJUSTMENT = 0.090f;

    @Test
    void testConstructorWithInvalidParams() {
        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        ArrayList<Font> fonts = new ArrayList<>();

        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderWorker(
                        null,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderWorker(
                        FONT_FACTORY,
                        null,
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderWorker(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                null,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderWorker(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                "",
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderWorker(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                null,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderWorker(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                "",
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderWorker(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                0f,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderWorker(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                null,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderWorker(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        -0.0001f,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderWorker(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        null,
                                        -0.0001f
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderWorker(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        null,
                                        1f - LEADING_ADJUSTMENT
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderWorker(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                null,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderWorker(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        -0.0001f,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderWorker(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        null,
                                        -0.0001f
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderWorker(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        null,
                                        1f - LEADING_ADJUSTMENT
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderWorker(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                null,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderWorker(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        -0.0001f,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderWorker(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        null,
                                        -0.0001f
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderWorker(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        null,
                                        1f - LEADING_ADJUSTMENT
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderWorker(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                null,
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderWorker(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        -0.0001f,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderWorker(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        null,
                                        -0.0001f
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderWorker(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        null,
                                        1f - LEADING_ADJUSTMENT
                                ),
                                LEADING_ADJUSTMENT),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderWorker(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                                ),
                                1f),
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderWorker(
                        FONT_FACTORY,
                        new FakeFontDefinition(
                                ID,
                                RELATIVE_LOCATION,
                                MAX_LOSSLESS_FONT_SIZE,
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                                ),
                                new FakeFontStyleDefinition(
                                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                                        null,
                                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
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
                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_PLAIN,
                        null,
                        ADDITIONAL_GLYPH_VERTICAL_PADDING_PLAIN
                ),
                new FakeFontStyleDefinition(
                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_ITALIC,
                        null,
                        ADDITIONAL_GLYPH_VERTICAL_PADDING_ITALIC
                ),
                new FakeFontStyleDefinition(
                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD,
                        null,
                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD
                ),
                new FakeFontStyleDefinition(
                        ADDITIONAL_GLYPH_HORIZONTAL_PADDING_BOLD_ITALIC,
                        null,
                        ADDITIONAL_GLYPH_VERTICAL_PADDING_BOLD_ITALIC
                ),
                LEADING_ADJUSTMENT);

        FontPreloaderWorker fontPreloaderWorker = new FontPreloaderWorker(FONT_FACTORY,
                fontDefinition, fonts::add);

        fontPreloaderWorker.run();

        assertEquals(1, FONT_FACTORY.INPUTS.size());
        assertEquals(1, FONT_FACTORY.OUTPUTS.size());
        FontDefinition input = FONT_FACTORY.INPUTS.get(0);
        assertNotNull(input);
        assertSame(fontDefinition, input);
        assertSame(FONT_FACTORY.OUTPUTS.get(0), fonts.get(0));
    }
}
