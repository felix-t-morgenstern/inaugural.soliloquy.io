package inaugural.soliloquy.io.test.unit.graphics.bootstrap.tasks;

import inaugural.soliloquy.io.api.dto.FontDefinitionDTO;
import inaugural.soliloquy.io.api.dto.FontStyleDefinitionDTO;
import inaugural.soliloquy.io.api.dto.FontStyleDefinitionGlyphPropertyDTO;
import inaugural.soliloquy.io.graphics.bootstrap.tasks.FontPreloaderTask;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeFontFactory;
import inaugural.soliloquy.tools.random.Random;
import org.junit.jupiter.api.Test;
import soliloquy.specs.io.graphics.assets.Font;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.FontDefinition;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.FontStyleDefinition;

import java.util.ArrayList;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static org.junit.jupiter.api.Assertions.*;

class FontPreloaderTaskTests {
    private final FakeFontFactory FONT_FACTORY = new FakeFontFactory();
    private final String ID = "FontId";
    private final String RELATIVE_LOCATION = "./src/test/resources/fonts/Trajan Pro Regular.ttf";
    private final float MAX_LOSSLESS_FONT_SIZE = 12.3f;
    private final float ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN = 0.123f;
    private final float ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC = 0.234f;
    private final float ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD = 0.345f;
    private final float ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC = 0.456f;
    private final static FontStyleDefinitionGlyphPropertyDTO[]
            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN = makeRandomGlyphPropertyDTOs();
    private final static FontStyleDefinitionGlyphPropertyDTO[]
            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC = makeRandomGlyphPropertyDTOs();
    private final static FontStyleDefinitionGlyphPropertyDTO[]
            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD = makeRandomGlyphPropertyDTOs();
    private final static FontStyleDefinitionGlyphPropertyDTO[]
            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC =
            makeRandomGlyphPropertyDTOs();
    private final static FontStyleDefinitionGlyphPropertyDTO[]
            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN = makeRandomGlyphPropertyDTOs();
    private final static FontStyleDefinitionGlyphPropertyDTO[]
            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC = makeRandomGlyphPropertyDTOs();
    private final static FontStyleDefinitionGlyphPropertyDTO[]
            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD = makeRandomGlyphPropertyDTOs();
    private final static FontStyleDefinitionGlyphPropertyDTO[]
            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC = makeRandomGlyphPropertyDTOs();
    private final float ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN = 0.567f;
    private final float ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC = 0.678f;
    private final float ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD = 0.789f;
    private final float ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC = 0.890f;
    private final float LEADING_ADJUSTMENT = 0.090f;

    @Test
    public void testConstructorWithInvalidParams() {
        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        ArrayList<Font> fonts = new ArrayList<>();

        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        listOf(
                            new FontDefinitionDTO(
                                    ID,
                                    RELATIVE_LOCATION,
                                    MAX_LOSSLESS_FONT_SIZE,
                                    LEADING_ADJUSTMENT,
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                    ))
                        ),
                        null,
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        null,
                        FONT_FACTORY,
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        listOf(
                            new FontDefinitionDTO(
                                    null,
                                    RELATIVE_LOCATION,
                                    MAX_LOSSLESS_FONT_SIZE,
                                    LEADING_ADJUSTMENT,
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                    ))
                        ),
                        FONT_FACTORY,
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        listOf(
                            new FontDefinitionDTO(
                                    "",
                                    RELATIVE_LOCATION,
                                    MAX_LOSSLESS_FONT_SIZE,
                                    LEADING_ADJUSTMENT,
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                    ))
                        ),
                        FONT_FACTORY,
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        listOf(
                            new FontDefinitionDTO(
                                    ID,
                                    null,
                                    MAX_LOSSLESS_FONT_SIZE,
                                    LEADING_ADJUSTMENT,
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                    ))
                        ),
                        FONT_FACTORY,
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        listOf(
                            new FontDefinitionDTO(
                                    ID,
                                    "",
                                    MAX_LOSSLESS_FONT_SIZE,
                                    LEADING_ADJUSTMENT,
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                    ))
                        ),
                        FONT_FACTORY,
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        listOf(
                            new FontDefinitionDTO(
                                    ID,
                                    RELATIVE_LOCATION,
                                    0f,
                                    LEADING_ADJUSTMENT,
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                    ))
                        ),
                        FONT_FACTORY,
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        listOf(
                            new FontDefinitionDTO(
                                    ID,
                                    RELATIVE_LOCATION,
                                    MAX_LOSSLESS_FONT_SIZE,
                                    LEADING_ADJUSTMENT,
                                    null,
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                    ))
                        ),
                        FONT_FACTORY,
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        listOf(
                            new FontDefinitionDTO(
                                    ID,
                                    RELATIVE_LOCATION,
                                    MAX_LOSSLESS_FONT_SIZE,
                                    LEADING_ADJUSTMENT,
                                    new FontStyleDefinitionDTO(
                                            -0.0001f,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                    ))
                        ),
                        FONT_FACTORY,
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        listOf(
                            new FontDefinitionDTO(
                                    ID,
                                    RELATIVE_LOCATION,
                                    MAX_LOSSLESS_FONT_SIZE,
                                    LEADING_ADJUSTMENT,
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            null,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                    ))
                        ),
                        FONT_FACTORY,
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        listOf(
                            new FontDefinitionDTO(
                                    ID,
                                    RELATIVE_LOCATION,
                                    MAX_LOSSLESS_FONT_SIZE,
                                    LEADING_ADJUSTMENT,
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            null,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                    ))
                        ),
                        FONT_FACTORY,
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        listOf(
                            new FontDefinitionDTO(
                                    ID,
                                    RELATIVE_LOCATION,
                                    MAX_LOSSLESS_FONT_SIZE,
                                    LEADING_ADJUSTMENT,
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                            -0.0001f
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                    ))
                        ),
                        FONT_FACTORY,
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        listOf(
                            new FontDefinitionDTO(
                                    ID,
                                    RELATIVE_LOCATION,
                                    MAX_LOSSLESS_FONT_SIZE,
                                    LEADING_ADJUSTMENT,
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                            1f - LEADING_ADJUSTMENT
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                    ))
                        ),
                        FONT_FACTORY,
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        listOf(
                            new FontDefinitionDTO(
                                    ID,
                                    RELATIVE_LOCATION,
                                    MAX_LOSSLESS_FONT_SIZE,
                                    LEADING_ADJUSTMENT,
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                    ),
                                    null,
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                    ))
                        ),
                        FONT_FACTORY,
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        listOf(
                            new FontDefinitionDTO(
                                    ID,
                                    RELATIVE_LOCATION,
                                    MAX_LOSSLESS_FONT_SIZE,
                                    LEADING_ADJUSTMENT,
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                    ),
                                    new FontStyleDefinitionDTO(
                                            -0.0001f,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                    ))
                        ),
                        FONT_FACTORY,
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        listOf(
                            new FontDefinitionDTO(
                                    ID,
                                    RELATIVE_LOCATION,
                                    MAX_LOSSLESS_FONT_SIZE,
                                    LEADING_ADJUSTMENT,
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            null,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                    ))
                        ),
                        FONT_FACTORY,
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        listOf(
                            new FontDefinitionDTO(
                                    ID,
                                    RELATIVE_LOCATION,
                                    MAX_LOSSLESS_FONT_SIZE,
                                    LEADING_ADJUSTMENT,
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            null,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                    ))
                        ),
                        FONT_FACTORY,
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        listOf(
                            new FontDefinitionDTO(
                                    ID,
                                    RELATIVE_LOCATION,
                                    MAX_LOSSLESS_FONT_SIZE,
                                    LEADING_ADJUSTMENT,
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                            -0.0001f
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                    ))
                        ),
                        FONT_FACTORY,
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        listOf(
                            new FontDefinitionDTO(
                                    ID,
                                    RELATIVE_LOCATION,
                                    MAX_LOSSLESS_FONT_SIZE,
                                    LEADING_ADJUSTMENT,
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                            1f - LEADING_ADJUSTMENT
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                    ))
                        ),
                        FONT_FACTORY,
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        listOf(
                            new FontDefinitionDTO(
                                    ID,
                                    RELATIVE_LOCATION,
                                    MAX_LOSSLESS_FONT_SIZE,
                                    LEADING_ADJUSTMENT,
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                    ),
                                    null,
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                    ))
                        ),
                        FONT_FACTORY,
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        listOf(
                            new FontDefinitionDTO(
                                    ID,
                                    RELATIVE_LOCATION,
                                    MAX_LOSSLESS_FONT_SIZE,
                                    LEADING_ADJUSTMENT,
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                    ),
                                    new FontStyleDefinitionDTO(
                                            -0.0001f,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                    ))
                        ),
                        FONT_FACTORY,
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        listOf(
                            new FontDefinitionDTO(
                                    ID,
                                    RELATIVE_LOCATION,
                                    MAX_LOSSLESS_FONT_SIZE,
                                    LEADING_ADJUSTMENT,
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            null,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                    ))
                        ),
                        FONT_FACTORY,
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        listOf(
                            new FontDefinitionDTO(
                                    ID,
                                    RELATIVE_LOCATION,
                                    MAX_LOSSLESS_FONT_SIZE,
                                    LEADING_ADJUSTMENT,
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            null,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                    ))
                        ),
                        FONT_FACTORY,
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        listOf(
                            new FontDefinitionDTO(
                                    ID,
                                    RELATIVE_LOCATION,
                                    MAX_LOSSLESS_FONT_SIZE,
                                    LEADING_ADJUSTMENT,
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                            -0.0001f
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                    ))
                        ),
                        FONT_FACTORY,
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        listOf(
                            new FontDefinitionDTO(
                                    ID,
                                    RELATIVE_LOCATION,
                                    MAX_LOSSLESS_FONT_SIZE,
                                    LEADING_ADJUSTMENT,
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                            1f - LEADING_ADJUSTMENT
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                    ))
                        ),
                        FONT_FACTORY,
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        listOf(
                            new FontDefinitionDTO(
                                    ID,
                                    RELATIVE_LOCATION,
                                    MAX_LOSSLESS_FONT_SIZE,
                                    LEADING_ADJUSTMENT,
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                    ),
                                    null)
                        ),
                        FONT_FACTORY,
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        listOf(
                            new FontDefinitionDTO(
                                    ID,
                                    RELATIVE_LOCATION,
                                    MAX_LOSSLESS_FONT_SIZE,
                                    LEADING_ADJUSTMENT,
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                    ),
                                    new FontStyleDefinitionDTO(
                                            -0.0001f,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                    ))
                        ),
                        FONT_FACTORY,
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        listOf(
                            new FontDefinitionDTO(
                                    ID,
                                    RELATIVE_LOCATION,
                                    MAX_LOSSLESS_FONT_SIZE,
                                    LEADING_ADJUSTMENT,
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            null,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                    ))
                        ),
                        FONT_FACTORY,
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        listOf(
                            new FontDefinitionDTO(
                                    ID,
                                    RELATIVE_LOCATION,
                                    MAX_LOSSLESS_FONT_SIZE,
                                    LEADING_ADJUSTMENT,
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            null,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                    ))
                        ),
                        FONT_FACTORY,
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        listOf(
                            new FontDefinitionDTO(
                                    ID,
                                    RELATIVE_LOCATION,
                                    MAX_LOSSLESS_FONT_SIZE,
                                    LEADING_ADJUSTMENT,
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                            -0.0001f
                                    ))
                        ),
                        FONT_FACTORY,
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        listOf(
                            new FontDefinitionDTO(
                                    ID,
                                    RELATIVE_LOCATION,
                                    MAX_LOSSLESS_FONT_SIZE,
                                    LEADING_ADJUSTMENT,
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                            1f - LEADING_ADJUSTMENT
                                    ))
                        ),
                        FONT_FACTORY,
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        listOf(
                            new FontDefinitionDTO(
                                    ID,
                                    RELATIVE_LOCATION,
                                    MAX_LOSSLESS_FONT_SIZE,
                                    1f,
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                    ))
                        ),
                        FONT_FACTORY,
                        fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderTask(
                        listOf(
                            new FontDefinitionDTO(
                                    ID,
                                    RELATIVE_LOCATION,
                                    MAX_LOSSLESS_FONT_SIZE,
                                    LEADING_ADJUSTMENT,
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                                    ),
                                    new FontStyleDefinitionDTO(
                                            ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                                            GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                                            ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                                    ))
                        ),
                        FONT_FACTORY,
                        null));
    }

    @Test
    public void testRun() {
        ArrayList<Font> fonts = new ArrayList<>();

        FontDefinitionDTO fontDefinitionDTO = new FontDefinitionDTO(
                ID,
                RELATIVE_LOCATION,
                MAX_LOSSLESS_FONT_SIZE,
                LEADING_ADJUSTMENT,
                new FontStyleDefinitionDTO(
                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_PLAIN,
                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_PLAIN,
                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_PLAIN
                ),
                new FontStyleDefinitionDTO(
                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_ITALIC,
                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_ITALIC,
                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_ITALIC
                ),
                new FontStyleDefinitionDTO(
                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD,
                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD,
                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD,
                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD
                ),
                new FontStyleDefinitionDTO(
                        ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC,
                        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT_BOLD_ITALIC,
                        ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_BOLD_ITALIC
                ));

        FontPreloaderTask fontPreloaderTask = new FontPreloaderTask(
                listOf(
                    fontDefinitionDTO
                ),
                FONT_FACTORY,
                fonts::add
        );

        fontPreloaderTask.run();

        assertEquals(1, FONT_FACTORY.INPUTS.size());
        assertEquals(1, FONT_FACTORY.OUTPUTS.size());
        FontDefinition input = FONT_FACTORY.INPUTS.getFirst();
        assertNotNull(input);
        assertEquals(ID, input.id());
        assertEquals(RELATIVE_LOCATION, input.relativeLocation());
        assertEquals(MAX_LOSSLESS_FONT_SIZE, input.maxLosslessFontSize());
        assertEquals(LEADING_ADJUSTMENT, input.leadingAdjustment());
        assertStyleDefinitionsEqual(fontDefinitionDTO.plain, input.plain());
        assertStyleDefinitionsEqual(fontDefinitionDTO.italic, input.italic());
        assertStyleDefinitionsEqual(fontDefinitionDTO.bold, input.bold());
        assertStyleDefinitionsEqual(fontDefinitionDTO.boldItalic, input.boldItalic());
        assertSame(FONT_FACTORY.OUTPUTS.getFirst(), fonts.getFirst());
    }

    private void assertStyleDefinitionsEqual(FontStyleDefinitionDTO dto,
                                             FontStyleDefinition definition) {
        assertEquals(dto.additionalGlyphHorizontalTextureSpacing,
                definition.additionalGlyphHorizontalTextureSpacing());

        assertEquals(dto.additionalGlyphVerticalTextureSpacing,
                definition.additionalGlyphVerticalTextureSpacing());
    }

    private static FontStyleDefinitionGlyphPropertyDTO[] makeRandomGlyphPropertyDTOs() {
        return new FontStyleDefinitionGlyphPropertyDTO[]{
                new FontStyleDefinitionGlyphPropertyDTO(Random.randomChar(),
                        Random.randomFloatInRange(0, 1))
        };
    }
}
