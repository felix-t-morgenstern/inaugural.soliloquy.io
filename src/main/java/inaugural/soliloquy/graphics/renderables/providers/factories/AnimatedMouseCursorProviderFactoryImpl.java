package inaugural.soliloquy.graphics.renderables.providers.factories;

import inaugural.soliloquy.graphics.renderables.providers.AnimatedMouseCursorProviderImpl;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.AnimatedMouseCursorProviderDefinition;
import soliloquy.specs.graphics.renderables.providers.AnimatedMouseCursorProvider;
import soliloquy.specs.graphics.renderables.providers.factories.AnimatedMouseCursorProviderFactory;

public class AnimatedMouseCursorProviderFactoryImpl implements AnimatedMouseCursorProviderFactory {
    @Override
    public AnimatedMouseCursorProvider make(AnimatedMouseCursorProviderDefinition definition)
            throws IllegalArgumentException {
        return new AnimatedMouseCursorProviderImpl(
                definition.id(),
                definition.cursorsAtMs(),
                definition.msDuration(),
                definition.periodModuloOffset(),
                definition.pausedTimestamp(),
                definition.mostRecentTimestamp()
        );
    }
}
