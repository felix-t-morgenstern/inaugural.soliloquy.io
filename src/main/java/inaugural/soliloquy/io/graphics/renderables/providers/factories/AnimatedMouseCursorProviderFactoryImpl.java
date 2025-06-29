package inaugural.soliloquy.io.graphics.renderables.providers.factories;

import inaugural.soliloquy.io.graphics.renderables.providers.AnimatedMouseCursorProviderImpl;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.AnimatedMouseCursorProviderDefinition;
import soliloquy.specs.io.graphics.renderables.providers.AnimatedMouseCursorProvider;
import soliloquy.specs.io.graphics.renderables.providers.factories.AnimatedMouseCursorProviderFactory;

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
