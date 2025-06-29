package inaugural.soliloquy.io.graphics.renderables.providers.factories;

import inaugural.soliloquy.io.graphics.renderables.providers.GlobalLoopingAnimationImpl;
import soliloquy.specs.io.graphics.assets.GlobalLoopingAnimation;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.GlobalLoopingAnimationDefinition;
import soliloquy.specs.io.graphics.renderables.providers.factories.GlobalLoopingAnimationFactory;

public class GlobalLoopingAnimationFactoryImpl implements GlobalLoopingAnimationFactory {
    @Override
    public GlobalLoopingAnimation make(GlobalLoopingAnimationDefinition definition)
            throws IllegalArgumentException {
        return new GlobalLoopingAnimationImpl(
                definition.id(),
                definition.animation(),
                definition.periodModuloOffset(),
                definition.pauseTimestamp()
        );
    }
}
