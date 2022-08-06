package inaugural.soliloquy.graphics.renderables.providers.factories;

import inaugural.soliloquy.graphics.renderables.providers.StaticMouseCursorProviderImpl;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.StaticMouseCursorProviderDefinition;
import soliloquy.specs.graphics.renderables.providers.StaticMouseCursorProvider;
import soliloquy.specs.graphics.renderables.providers.factories.StaticMouseCursorProviderFactory;

public class StaticMouseCursorPreloaderFactoryImpl implements StaticMouseCursorProviderFactory {
    @Override
    public StaticMouseCursorProvider make(StaticMouseCursorProviderDefinition definition)
            throws IllegalArgumentException {
        return new StaticMouseCursorProviderImpl(definition.id(), definition.mouseCursorImageId(),
                null);
    }
}
