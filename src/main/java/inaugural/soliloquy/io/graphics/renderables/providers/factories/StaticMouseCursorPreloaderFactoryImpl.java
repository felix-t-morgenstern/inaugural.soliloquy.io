package inaugural.soliloquy.io.graphics.renderables.providers.factories;

import inaugural.soliloquy.io.graphics.renderables.providers.StaticMouseCursorProviderImpl;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.StaticMouseCursorProviderDefinition;
import soliloquy.specs.io.graphics.renderables.providers.StaticMouseCursorProvider;
import soliloquy.specs.io.graphics.renderables.providers.factories.StaticMouseCursorProviderFactory;

public class StaticMouseCursorPreloaderFactoryImpl implements StaticMouseCursorProviderFactory {
    @Override
    public StaticMouseCursorProvider make(StaticMouseCursorProviderDefinition definition)
            throws IllegalArgumentException {
        return new StaticMouseCursorProviderImpl(definition.id(), definition.mouseCursorImageId(),
                null);
    }
}
