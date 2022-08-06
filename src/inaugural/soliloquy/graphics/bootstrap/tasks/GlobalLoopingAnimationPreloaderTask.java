package inaugural.soliloquy.graphics.bootstrap.tasks;

import inaugural.soliloquy.graphics.api.dto.GlobalLoopingAnimationDefinitionDTO;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.assets.Animation;
import soliloquy.specs.graphics.assets.GlobalLoopingAnimation;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.GlobalLoopingAnimationDefinition;
import soliloquy.specs.graphics.renderables.providers.factories.GlobalLoopingAnimationFactory;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

public class GlobalLoopingAnimationPreloaderTask implements Runnable {
    private final Function<String, Animation> GET_ANIMATION;
    private final GlobalLoopingAnimationFactory GLOBAL_LOOPING_ANIMATION_FACTORY;
    private final Collection<GlobalLoopingAnimationDefinitionDTO>
            GLOBAL_LOOPING_ANIMATION_DEFINITION_DTOS;
    private final Consumer<GlobalLoopingAnimation> PROCESS_RESULT;

    /** @noinspection ConstantConditions*/
    public GlobalLoopingAnimationPreloaderTask(Function<String, Animation> getAnimation,
                                               Collection<GlobalLoopingAnimationDefinitionDTO>
                                                       globalLoopingAnimationDefinitionDTOs,
                                               GlobalLoopingAnimationFactory
                                                         globalLoopingAnimationFactory,
                                               Consumer<GlobalLoopingAnimation> processResult) {
        GET_ANIMATION = Check.ifNull(getAnimation, "getAnimation");
        GLOBAL_LOOPING_ANIMATION_FACTORY = Check.ifNull(globalLoopingAnimationFactory,
                "globalLoopingAnimationFactory");
        Check.ifNull(globalLoopingAnimationDefinitionDTOs, "globalLoopingAnimationDefinitionDTOs");
        if (globalLoopingAnimationDefinitionDTOs.isEmpty()) {
            throw new IllegalArgumentException("GlobalLoopingAnimationPreloaderTask: " +
                    "globalLoopingAnimationDefinitionDTOs is empty");
        }
        globalLoopingAnimationDefinitionDTOs.forEach(globalLoopingAnimationDefinitionDTO -> {
            Check.ifNull(globalLoopingAnimationDefinitionDTO,
                    "globalLoopingAnimationDefinitionDTO within " +
                            "globalLoopingAnimationDefinitionDTOs");
            Check.ifNullOrEmpty(globalLoopingAnimationDefinitionDTO.id,
                    "globalLoopingAnimationDefinitionDTO.id within " +
                            "globalLoopingAnimationDefinitionDTOs");
            Check.ifNullOrEmpty(globalLoopingAnimationDefinitionDTO.animationId,
                    "globalLoopingAnimationDefinitionDTO.animationId within " +
                            "globalLoopingAnimationDefinitionDTOs (" +
                            globalLoopingAnimationDefinitionDTO.id + ")");
            Check.throwOnLteZero(globalLoopingAnimationDefinitionDTO.periodModuloOffset,
                    "globalLoopingAnimationDefinitionDTO.periodModuloOffset within " +
                            "globalLoopingAnimationDefinitionDTOs (" +
                            globalLoopingAnimationDefinitionDTO.id + ")");
        });
        GLOBAL_LOOPING_ANIMATION_DEFINITION_DTOS = globalLoopingAnimationDefinitionDTOs;
        PROCESS_RESULT = Check.ifNull(processResult, "processResult");
    }

    public void run() {
        GLOBAL_LOOPING_ANIMATION_DEFINITION_DTOS.forEach(definition -> PROCESS_RESULT
                .accept(GLOBAL_LOOPING_ANIMATION_FACTORY.make(
                        new GlobalLoopingAnimationDefinition(
                                definition.id,
                                GET_ANIMATION.apply(definition.animationId),
                                definition.periodModuloOffset,
                                null
                        )
                )));
    }
}
