package inaugural.soliloquy.graphics.test.unit.bootstrap;

import inaugural.soliloquy.graphics.bootstrap.FontPreloaderWorker;
import inaugural.soliloquy.graphics.test.fakes.FakeFontFactory;
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
    private final float ADDITIONAL_GLYPH_PADDING = 0.123f;

    @Test
    void testConstructorWithInvalidParams() {
        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        ArrayList<Font> fonts = new ArrayList<>();

        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderWorker(null, ID, RELATIVE_LOCATION,
                        MAX_LOSSLESS_FONT_SIZE, ADDITIONAL_GLYPH_PADDING, fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderWorker(FONT_FACTORY, null, RELATIVE_LOCATION,
                        MAX_LOSSLESS_FONT_SIZE, ADDITIONAL_GLYPH_PADDING, fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderWorker(FONT_FACTORY, "", RELATIVE_LOCATION,
                        MAX_LOSSLESS_FONT_SIZE, ADDITIONAL_GLYPH_PADDING, fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderWorker(FONT_FACTORY, ID, null,
                        MAX_LOSSLESS_FONT_SIZE, ADDITIONAL_GLYPH_PADDING, fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderWorker(FONT_FACTORY, ID, "",
                        MAX_LOSSLESS_FONT_SIZE, ADDITIONAL_GLYPH_PADDING, fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderWorker(FONT_FACTORY, ID, RELATIVE_LOCATION,
                        0f, ADDITIONAL_GLYPH_PADDING, fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderWorker(FONT_FACTORY, ID, RELATIVE_LOCATION,
                        MAX_LOSSLESS_FONT_SIZE, -0.001f, fonts::add));
        assertThrows(IllegalArgumentException.class,
                () -> new FontPreloaderWorker(FONT_FACTORY, ID, RELATIVE_LOCATION,
                        MAX_LOSSLESS_FONT_SIZE, ADDITIONAL_GLYPH_PADDING, null));
    }

    @Test
    void testRun() {
        ArrayList<Font> fonts = new ArrayList<>();

        FontPreloaderWorker fontPreloaderWorker = new FontPreloaderWorker(FONT_FACTORY, ID,
                RELATIVE_LOCATION, MAX_LOSSLESS_FONT_SIZE, ADDITIONAL_GLYPH_PADDING, fonts::add);

        fontPreloaderWorker.run();

        assertEquals(1, FONT_FACTORY.INPUTS.size());
        assertEquals(1, FONT_FACTORY.OUTPUTS.size());
        FontDefinition input = FONT_FACTORY.INPUTS.get(0);
        assertNotNull(input);
        assertEquals(ID, input.id());
        assertEquals(RELATIVE_LOCATION, input.relativeLocation());
        assertEquals(MAX_LOSSLESS_FONT_SIZE, input.maxLosslessFontSize());
        assertEquals(ADDITIONAL_GLYPH_PADDING, input.additionalGlyphPadding());
        assertEquals(FontDefinition.class.getCanonicalName(), input.getInterfaceName());
        assertEquals(1, fonts.size());
        assertSame(FONT_FACTORY.OUTPUTS.get(0), fonts.get(0));
    }
}
