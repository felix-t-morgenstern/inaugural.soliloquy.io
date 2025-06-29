package inaugural.soliloquy.io.test.testdoubles.fakes;

import soliloquy.specs.io.graphics.assets.Font;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.FontDefinition;

import java.util.List;

import static inaugural.soliloquy.tools.collections.Collections.listOf;


public class FakeFontFactory implements AssetFactory<FontDefinition, Font> {
    public final List<FontDefinition> INPUTS = listOf();
    public final List<Font> OUTPUTS = listOf();

    @Override
    public Font make(FontDefinition fontDefinition) throws IllegalArgumentException {
        INPUTS.add(fontDefinition);
        Font createdFont = new FakeFont();
        OUTPUTS.add(createdFont);
        return createdFont;
    }
}
