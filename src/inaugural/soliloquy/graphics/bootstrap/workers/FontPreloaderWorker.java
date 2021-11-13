package inaugural.soliloquy.graphics.bootstrap.workers;

import inaugural.soliloquy.graphics.assets.CanValidateFontDefinitions;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.assets.Font;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontDefinition;

import java.util.function.Consumer;

public class FontPreloaderWorker extends CanValidateFontDefinitions implements Runnable {
    private final AssetFactory<FontDefinition, Font> FONT_FACTORY;
    private final FontDefinition FONT_DEFINITION;
    private final Consumer<Font> ADD_LOADED_FONT;

    public FontPreloaderWorker(AssetFactory<FontDefinition, Font> fontFactory,
                               FontDefinition fontDefinition,
                               Consumer<Font> addLoadedFont) {
        FONT_FACTORY = Check.ifNull(fontFactory, "fontFactory");
        FONT_DEFINITION = Check.ifNull(fontDefinition, "fontDefinition");
        validateFontDefinition(fontDefinition);
        ADD_LOADED_FONT = Check.ifNull(addLoadedFont, "addLoadedFont");
    }

    @Override
    public void run() {
        ADD_LOADED_FONT.accept(FONT_FACTORY.make(FONT_DEFINITION));
    }
}
