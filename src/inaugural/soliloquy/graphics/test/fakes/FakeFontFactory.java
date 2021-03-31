package inaugural.soliloquy.graphics.test.fakes;

import soliloquy.specs.graphics.assets.Font;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontDefinition;

import java.util.ArrayList;

public class FakeFontFactory implements AssetFactory<FontDefinition, Font> {
    public final ArrayList<FontDefinition> INPUTS = new ArrayList<>();
    public final ArrayList<Font> OUTPUTS = new ArrayList<>();

    @Override
    public Font make(FontDefinition fontDefinition) throws IllegalArgumentException {
        INPUTS.add(fontDefinition);
        Font createdFont = new FakeFont();
        OUTPUTS.add(createdFont);
        return createdFont;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
