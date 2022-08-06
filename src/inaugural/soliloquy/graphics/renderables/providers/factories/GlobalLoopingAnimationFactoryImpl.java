package inaugural.soliloquy.graphics.renderables.providers.factories;

import inaugural.soliloquy.graphics.renderables.providers.GlobalLoopingAnimationImpl;
import soliloquy.specs.graphics.assets.Animation;
import soliloquy.specs.graphics.assets.GlobalLoopingAnimation;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.GlobalLoopingAnimationDefinition;
import soliloquy.specs.graphics.renderables.providers.factories.GlobalLoopingAnimationFactory;

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

    @Override
    public String getInterfaceName() {
        return GlobalLoopingAnimationFactory.class.getCanonicalName();
    }
}
