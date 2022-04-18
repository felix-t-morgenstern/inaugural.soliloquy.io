package inaugural.soliloquy.graphics.bootstrap.tasks;

import inaugural.soliloquy.graphics.assets.CanValidateFontDefinitions;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.assets.Font;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontDefinition;

import java.util.Collection;
import java.util.function.Consumer;

public class FontPreloaderTask extends CanValidateFontDefinitions implements Runnable {
    private final AssetFactory<FontDefinition, Font> FACTORY;
    private final Collection<FontDefinition> FONT_DEFINITIONS;
    private final Consumer<Font> PROCESS_RESULT;

    /** @noinspection ConstantConditions*/
    public FontPreloaderTask(Collection<FontDefinition> fontDefinitions,
                             AssetFactory<FontDefinition, Font> factory,
                             Consumer<Font> processResult) {
        FACTORY = Check.ifNull(factory, "factory");
        FONT_DEFINITIONS = Check.ifNull(fontDefinitions, "fontDefinitions");
        // validateFontDefinition is protected, so lambda cannot be used
        //noinspection Convert2MethodRef
        fontDefinitions.forEach(fontDefinition -> validateFontDefinition(fontDefinition));
        PROCESS_RESULT = Check.ifNull(processResult, "processResult");
    }

    @Override
    public void run() {
        FONT_DEFINITIONS.forEach(fontDefinition ->
                PROCESS_RESULT.accept(FACTORY.make(fontDefinition)));
    }
}
