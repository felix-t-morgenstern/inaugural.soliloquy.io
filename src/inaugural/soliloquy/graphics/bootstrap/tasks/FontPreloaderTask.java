package inaugural.soliloquy.graphics.bootstrap.tasks;

import inaugural.soliloquy.graphics.assets.CanValidateFontDefinitions;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.assets.Font;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontDefinition;

import java.util.Collection;
import java.util.function.Consumer;

public class FontPreloaderTask extends CanValidateFontDefinitions implements Runnable {
    private final AssetFactory<FontDefinition, Font> FONT_FACTORY;
    private final Collection<FontDefinition> FONT_DEFINITIONS;
    private final Consumer<Font> ADD_LOADED_FONT;

    /** @noinspection ConstantConditions*/
    public FontPreloaderTask(AssetFactory<FontDefinition, Font> fontFactory,
                             Collection<FontDefinition> fontDefinitions,
                             Consumer<Font> addLoadedFont) {
        FONT_FACTORY = Check.ifNull(fontFactory, "fontFactory");
        FONT_DEFINITIONS = Check.ifNull(fontDefinitions, "fontDefinitions");
        // validateFontDefinition is protected, so lambda cannot be used
        //noinspection Convert2MethodRef
        fontDefinitions.forEach(fontDefinition -> validateFontDefinition(fontDefinition));
        ADD_LOADED_FONT = Check.ifNull(addLoadedFont, "addLoadedFont");
    }

    @Override
    public void run() {
        FONT_DEFINITIONS.forEach(fontDefinition ->
                ADD_LOADED_FONT.accept(FONT_FACTORY.make(fontDefinition)));
    }
}
