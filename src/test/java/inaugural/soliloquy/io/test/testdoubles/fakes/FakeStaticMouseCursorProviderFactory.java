package inaugural.soliloquy.io.test.testdoubles.fakes;

import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.StaticMouseCursorProviderDefinition;
import soliloquy.specs.io.graphics.renderables.providers.StaticMouseCursorProvider;
import soliloquy.specs.io.graphics.renderables.providers.factories.StaticMouseCursorProviderFactory;

import java.util.List;

import static inaugural.soliloquy.tools.collections.Collections.listOf;


public class FakeStaticMouseCursorProviderFactory implements StaticMouseCursorProviderFactory {
    public List<StaticMouseCursorProviderDefinition> Inputs = listOf();

    @Override
    public StaticMouseCursorProvider make(StaticMouseCursorProviderDefinition definition)
            throws IllegalArgumentException {
        Inputs.add(definition);
        return new FakeStaticMouseCursorProvider(definition.id(), definition.mouseCursorImageId());
    }
}
