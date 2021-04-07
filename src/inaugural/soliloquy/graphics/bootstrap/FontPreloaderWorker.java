package inaugural.soliloquy.graphics.bootstrap;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.assets.Font;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontDefinition;

import java.util.function.Consumer;

public class FontPreloaderWorker implements Runnable {
    private final AssetFactory<FontDefinition, Font> FONT_FACTORY;
    private final String ID;
    private final String RELATIVE_LOCATION;
    private final float MAX_LOSSLESS_FONT_SIZE;
    private final float ADDITIONAL_GLYPH_PADDING;
    private final float LEADING_ADJUSTMENT;
    private final Consumer<Font> ADD_LOADED_FONT;

    public FontPreloaderWorker(AssetFactory<FontDefinition, Font> fontFactory,
                               String id, String relativeLocation, float maxLosslessFontSize,
                               float additionalGlyphPadding, float leadingAdjustment,
                               Consumer<Font> addLoadedFont) {
        FONT_FACTORY = Check.ifNull(fontFactory, "fontFactory");
        ID = Check.ifNullOrEmpty(id, "id");
        RELATIVE_LOCATION = Check.ifNullOrEmpty(relativeLocation, "relativeLocation");
        MAX_LOSSLESS_FONT_SIZE = Check.throwOnLteZero(maxLosslessFontSize, "maxLosslessFontSize");
        ADDITIONAL_GLYPH_PADDING = Check.throwOnLteZero(additionalGlyphPadding,
                "additionalGlyphPadding");
        LEADING_ADJUSTMENT = Check.throwOnLtValue(
                    Check.throwOnGteValue(leadingAdjustment, 1f, "leadingAdjustment"),
                0f, "leadingAdjustment");
        ADD_LOADED_FONT = Check.ifNull(addLoadedFont, "addLoadedFont");
    }

    @Override
    public void run() {
        ADD_LOADED_FONT.accept(FONT_FACTORY.make(new FontDefinition() {
            @Override
            public String relativeLocation() {
                return RELATIVE_LOCATION;
            }

            @Override
            public float maxLosslessFontSize() {
                return MAX_LOSSLESS_FONT_SIZE;
            }

            @Override
            public float additionalGlyphPadding() {
                return ADDITIONAL_GLYPH_PADDING;
            }

            @Override
            public float leadingAdjustment() {
                return LEADING_ADJUSTMENT;
            }

            @Override
            public String id() throws IllegalStateException {
                return ID;
            }

            @Override
            public String getInterfaceName() {
                return FontDefinition.class.getCanonicalName();
            }
        }));
    }
}
