package inaugural.soliloquy.graphics.renderables.providers.factories;

import inaugural.soliloquy.graphics.renderables.providers.GlobalLoopingAnimationImpl;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.assets.Animation;
import soliloquy.specs.graphics.renderables.providers.GlobalLoopingAnimation;
import soliloquy.specs.graphics.renderables.providers.factories.GlobalLoopingAnimationFactory;

public class GlobalLoopingAnimationFactoryImpl implements GlobalLoopingAnimationFactory {
    @Override
    public GlobalLoopingAnimation make(EntityUuid uuid, Animation animation,
                                       int periodModuloOffset) throws IllegalArgumentException {
        return new GlobalLoopingAnimationImpl(uuid, animation, periodModuloOffset);
    }

    @Override
    public String getInterfaceName() {
        return GlobalLoopingAnimationFactory.class.getCanonicalName();
    }
}
