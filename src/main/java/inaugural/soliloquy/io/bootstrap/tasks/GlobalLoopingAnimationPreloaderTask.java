package inaugural.soliloquy.io.bootstrap.tasks;

import inaugural.soliloquy.io.api.dto.GlobalLoopingAnimationDefinitionDTO;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.io.graphics.assets.Animation;
import soliloquy.specs.io.graphics.assets.GlobalLoopingAnimation;
import soliloquy.specs.io.bootstrap.assetfactories.definitions.GlobalLoopingAnimationDefinition;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

public class GlobalLoopingAnimationPreloaderTask implements Runnable {
    private final Function<String, Animation> GET_ANIMATION;
    private final Function<GlobalLoopingAnimationDefinition, GlobalLoopingAnimation> GLOBAL_LOOPING_ANIMATION_FACTORY;
    private final Collection<GlobalLoopingAnimationDefinitionDTO>
            GLOBAL_LOOPING_ANIMATION_DEFINITION_DTOS;
    private final Consumer<GlobalLoopingAnimation> PROCESS_RESULT;

    /** @noinspection ConstantConditions */
    public GlobalLoopingAnimationPreloaderTask(Function<String, Animation> getAnimation,
                                               Collection<GlobalLoopingAnimationDefinitionDTO>
                                                       globalLoopingAnimationDefinitionDTOs,
                                               Function<GlobalLoopingAnimationDefinition, GlobalLoopingAnimation>
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
            Check.throwOnLtValue(globalLoopingAnimationDefinitionDTO.periodModuloOffset, 0,
                    "globalLoopingAnimationDefinitionDTO.periodModuloOffset within " +
                            "globalLoopingAnimationDefinitionDTOs (" +
                            globalLoopingAnimationDefinitionDTO.id + ")");
        });
        GLOBAL_LOOPING_ANIMATION_DEFINITION_DTOS = globalLoopingAnimationDefinitionDTOs;
        PROCESS_RESULT = Check.ifNull(processResult, "processResult");
    }

    public void run() {
        GLOBAL_LOOPING_ANIMATION_DEFINITION_DTOS.forEach(definition -> PROCESS_RESULT
                .accept(GLOBAL_LOOPING_ANIMATION_FACTORY.apply(
                        new GlobalLoopingAnimationDefinition(
                                definition.id,
                                GET_ANIMATION.apply(definition.animationId),
                                definition.periodModuloOffset,
                                null
                        )
                )));
    }
}
