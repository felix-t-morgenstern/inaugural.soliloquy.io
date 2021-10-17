package inaugural.soliloquy.graphics.bootstrap;

import inaugural.soliloquy.graphics.api.dto.GlobalLoopingAnimationDefinitionDTO;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.infrastructure.Registry;
import soliloquy.specs.graphics.assets.Animation;
import soliloquy.specs.graphics.assets.GlobalLoopingAnimation;
import soliloquy.specs.graphics.renderables.providers.factories.GlobalLoopingAnimationFactory;

import java.util.Collection;
import java.util.Map;

public class GlobalLoopingAnimationPreloaderWorker {
    private final Map<String, Animation> ANIMATIONS;
    private final GlobalLoopingAnimationFactory GLOBAL_LOOPING_ANIMATION_FACTORY;
    private final Registry<GlobalLoopingAnimation> GLOBAL_LOOPING_ANIMATIONS;

    public GlobalLoopingAnimationPreloaderWorker(Map<String, Animation> animations,
                                                 GlobalLoopingAnimationFactory
                                                         globalLoopingAnimationFactory,
                                                 Registry<GlobalLoopingAnimation>
                                                         globalLoopingAnimations) {
        ANIMATIONS = Check.ifNull(animations, "animations");
        GLOBAL_LOOPING_ANIMATION_FACTORY = Check.ifNull(globalLoopingAnimationFactory,
                "globalLoopingAnimationFactory");
        GLOBAL_LOOPING_ANIMATIONS = Check.ifNull(globalLoopingAnimations,
                "globalLoopingAnimations");
    }

    public void run(Collection<GlobalLoopingAnimationDefinitionDTO>
                            globalLoopingAnimationDefinitionDTOs) {
        globalLoopingAnimationDefinitionDTOs.forEach(definition -> GLOBAL_LOOPING_ANIMATIONS
                .add(GLOBAL_LOOPING_ANIMATION_FACTORY.make(definition.id,
                        ANIMATIONS.get(definition.animationId), definition.periodModuloOffset,
                        null)));
    }
}
