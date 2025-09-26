package inaugural.soliloquy.io.bootstrap.tasks;

import inaugural.soliloquy.io.api.dto.AnimationDefinitionDTO;
import inaugural.soliloquy.io.api.dto.AnimationFrameDefinitionDTO;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.io.graphics.assets.Animation;
import soliloquy.specs.io.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.io.graphics.assets.Image;
import soliloquy.specs.io.bootstrap.assetfactories.definitions.AnimationDefinition;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;

public class AnimationPreloaderTask implements Runnable {
    private final Function<String, Image> GET_IMAGE;
    private final Function<AnimationDefinition, Animation> FACTORY;
    private final Collection<AnimationDefinitionDTO> ANIMATION_DEFINITION_DTOS;
    private final Consumer<Animation> PROCESS_RESULT;

    /** @noinspection ConstantConditions */
    public AnimationPreloaderTask(Function<String, Image> getImage,
                                  Collection<AnimationDefinitionDTO> animationDefinitionDTOs,
                                  Function<AnimationDefinition, Animation> factory,
                                  Consumer<Animation> processResult) {
        GET_IMAGE = Check.ifNull(getImage, "getImage");
        FACTORY = Check.ifNull(factory, "factory");

        Check.ifNull(animationDefinitionDTOs, "animationDefinitionDTOs");
        if (animationDefinitionDTOs.isEmpty()) {
            throw new IllegalArgumentException(
                    "AnimationPreloaderTask: animationDefinitionDTOs is empty");
        }
        animationDefinitionDTOs.forEach(animationDefinitionDTO -> {
            Check.ifNull(animationDefinitionDTO,
                    "animationDefinitionDTO within animationDefinitionDTOs");
            Check.ifNullOrEmpty(animationDefinitionDTO.id,
                    "animationDefinitionDTO.id within animationDefinitionDTOs");
            Check.throwOnLteZero(animationDefinitionDTO.msDur,
                    "animationDefinitionDTO.msDur within animationDefinitionDTOs (" +
                            animationDefinitionDTO.id + ")");
            Check.ifNull(animationDefinitionDTO.frames,
                    "animationDefinitionDTO.frames within animationDefinitionDTOs (" +
                            animationDefinitionDTO.id + ")");
            if (animationDefinitionDTO.frames.length == 0) {
                throw new IllegalArgumentException("animationDefinitionDTO.frames within " +
                        "animationDefinitionDTOs (" + animationDefinitionDTO.id + ") is empty");
            }
            var maxFrameMs = 0;
            var frameAt0ms = false;
            for (AnimationFrameDefinitionDTO frameDTO : animationDefinitionDTO.frames) {
                maxFrameMs = Math.max(maxFrameMs, frameDTO.ms);
                frameAt0ms = frameAt0ms || frameDTO.ms == 0;
            }
            if (!frameAt0ms) {
                throw new IllegalArgumentException("AnimationPreloaderTask: " +
                        "animationDefinitionDTO within animationDefinitionDTOs (" +
                        animationDefinitionDTO.id + ") has no frame at 0ms");
            }
            if (animationDefinitionDTO.msDur < maxFrameMs) {
                throw new IllegalArgumentException("AnimationPreloaderTask: " +
                        "animationDefinitionDTO within animationDefinitionDTOs (" +
                        animationDefinitionDTO.id + ") has msDur (" +
                        animationDefinitionDTO.msDur + ") less than max ms of frame (" +
                        maxFrameMs + ")");
            }
        });
        ANIMATION_DEFINITION_DTOS = animationDefinitionDTOs;

        PROCESS_RESULT = Check.ifNull(processResult, "processResult");
    }

    public void run() {
        ANIMATION_DEFINITION_DTOS.forEach(dto ->
                PROCESS_RESULT.accept(FACTORY.apply(makeDefinition(dto))));
    }

    private AnimationDefinition makeDefinition(AnimationDefinitionDTO animationDefinitionDTO) {
        Map<Integer, AnimationFrameSnippet> snippetDefinitions = mapOf();
        for (var frameSnippetDTO : animationDefinitionDTO.frames) {
            snippetDefinitions.put(frameSnippetDTO.ms, new AnimationFrameSnippet() {
                @Override
                public Image image() {
                    return GET_IMAGE.apply(frameSnippetDTO.imgLoc);
                }

                @Override
                public int leftX() {
                    return frameSnippetDTO.leftX;
                }

                @Override
                public int topY() {
                    return frameSnippetDTO.topY;
                }

                @Override
                public int rightX() {
                    return frameSnippetDTO.rightX;
                }

                @Override
                public int bottomY() {
                    return frameSnippetDTO.bottomY;
                }

                @Override
                public float offsetX() {
                    return frameSnippetDTO.offsetX;
                }

                @Override
                public float offsetY() {
                    return frameSnippetDTO.offsetY;
                }
            });
        }

        return new AnimationDefinition(
                animationDefinitionDTO.id,
                animationDefinitionDTO.msDur,
                snippetDefinitions
        );
    }
}
