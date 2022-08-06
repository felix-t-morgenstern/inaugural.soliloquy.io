package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.StaticMouseCursorProviderDefinition;
import soliloquy.specs.graphics.renderables.providers.StaticMouseCursorProvider;
import soliloquy.specs.graphics.renderables.providers.factories.StaticMouseCursorProviderFactory;

import java.util.ArrayList;

public class FakeStaticMouseCursorProviderFactory implements StaticMouseCursorProviderFactory {
    public ArrayList<StaticMouseCursorProviderDefinition> Inputs = new ArrayList<>();

    @Override
    public StaticMouseCursorProvider make(StaticMouseCursorProviderDefinition definition)
            throws IllegalArgumentException {
        Inputs.add(definition);
        return new FakeStaticMouseCursorProvider(definition.id(), definition.mouseCursorImageId());
    }
}
