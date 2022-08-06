package inaugural.soliloquy.graphics.bootstrap.tasks;

import inaugural.soliloquy.graphics.api.dto.AnimatedMouseCursorDefinitionDTO;
import inaugural.soliloquy.graphics.api.dto.AnimatedMouseCursorFrameDefinitionDTO;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.AnimatedMouseCursorProviderDefinition;
import soliloquy.specs.graphics.renderables.providers.AnimatedMouseCursorProvider;
import soliloquy.specs.graphics.renderables.providers.factories.AnimatedMouseCursorProviderFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public class AnimatedMouseCursorPreloaderTask implements Runnable {
    private final Collection<AnimatedMouseCursorDefinitionDTO> DEFINITION_DTOS;
    private final Function<String, Long> GET_MOUSE_CURSORS_BY_RELATIVE_LOCATION;
    private final AnimatedMouseCursorProviderFactory FACTORY;
    private final Consumer<AnimatedMouseCursorProvider> PROCESS_RESULT;

    /** @noinspection ConstantConditions*/
    public AnimatedMouseCursorPreloaderTask(Function<String, Long>
                                                    getMouseCursorsByRelativeLocation,
                                            Collection<AnimatedMouseCursorDefinitionDTO>
                                             animatedMouseCursorDefinitionDTOs,
                                            AnimatedMouseCursorProviderFactory
                                                 animatedMouseCursorProviderFactory,
                                            Consumer<AnimatedMouseCursorProvider> processResult) {
        GET_MOUSE_CURSORS_BY_RELATIVE_LOCATION = Check.ifNull(getMouseCursorsByRelativeLocation,
                "getMouseCursorsByRelativeLocation");
        // TODO: Deal with the awful clot of logic here by refactoring out validation
        Check.ifNull(animatedMouseCursorDefinitionDTOs, "animatedMouseCursorDefinitionDTOs");
        animatedMouseCursorDefinitionDTOs.forEach(animatedMouseCursorDefinitionDTO -> {
            Check.ifNull(animatedMouseCursorDefinitionDTO,
                    "animatedMouseCursorDefinitionDTO within animatedMouseCursorDefinitionDTOs");
            Check.ifNullOrEmpty(animatedMouseCursorDefinitionDTO.Id,
                    "animatedMouseCursorDefinitionDTO.Id within " +
                            "animatedMouseCursorDefinitionDTOs");
            Check.ifNull(animatedMouseCursorDefinitionDTO.Frames,
                    "animatedMouseCursorDefinitionDTO.Frames within " +
                            "animatedMouseCursorDefinitionDTOs (" +
                            animatedMouseCursorDefinitionDTO.Id + ")");
            if (animatedMouseCursorDefinitionDTO.Frames.length == 0) {
                throw new IllegalArgumentException("AnimatedMouseCursorPreloaderTask: " +
                        "animatedMouseCursorDefinitionDTO.Frames is empty within " +
                        "animatedMouseCursorDefinitionDTOs (" +
                        animatedMouseCursorDefinitionDTO.Id + ")");
            }
            int maxFrameMs = 0;
            boolean frameAt0Ms = false;
            for (AnimatedMouseCursorFrameDefinitionDTO frameDTO :
                    animatedMouseCursorDefinitionDTO.Frames) {
                Check.ifNullOrEmpty(frameDTO.Img,
                        "frame within animatedMouseCursorDefinitionDTOs (" +
                                animatedMouseCursorDefinitionDTO.Id + ")");
                frameAt0Ms = frameAt0Ms || frameDTO.Ms == 0;
                maxFrameMs = Math.max(maxFrameMs, frameDTO.Ms);
            }
            if (!frameAt0Ms) {
                throw new IllegalArgumentException("AnimatedMouseCursorPreloaderTask: " +
                        "animatedMouseCursorDefinitionDTO.Frames has no frame at 0ms within " +
                        "animatedMouseCursorDefinitionDTOs (" + animatedMouseCursorDefinitionDTO.Id + ")");
            }
            Check.throwOnSecondLte(maxFrameMs, animatedMouseCursorDefinitionDTO.Duration,
                    "maximum frame ms", "animatedMouseCursorDefinitionDTO.Duration within " +
                            "animatedMouseCursorDefinitionDTOs (" +
                            animatedMouseCursorDefinitionDTO.Id + ")");
            Check.throwOnSecondLte(animatedMouseCursorDefinitionDTO.Offset, animatedMouseCursorDefinitionDTO.Duration,
                    "animatedMouseCursorDefinitionDTO.Offset",
                    "animatedMouseCursorDefinitionDTO.Duration within " +
                            "animatedMouseCursorDefinitionDTOs (" +
                            animatedMouseCursorDefinitionDTO.Id + ")");
            Check.throwOnLtValue(animatedMouseCursorDefinitionDTO.Offset, 0,
                    "animatedMouseCursorDefinitionDTO.Offset within " +
                            "animatedMouseCursorDefinitionDTOs (" +
                            animatedMouseCursorDefinitionDTO.Id + ")");
            if (animatedMouseCursorDefinitionDTO.Paused != null &&
                    animatedMouseCursorDefinitionDTO.Timestamp == null) {
                throw new IllegalArgumentException("animatedMouseCursorDefinitionDTO.Paused is " +
                        "non-null while animatedMouseCursorDefinitionDTO.Timestamp is null (id = "
                        + animatedMouseCursorDefinitionDTO.Id + ")");
            }
            Check.throwOnSecondLte(animatedMouseCursorDefinitionDTO.Paused, animatedMouseCursorDefinitionDTO.Timestamp,
                    "animatedMouseCursorDefinitionDTO.Paused",
                    "animatedMouseCursorDefinitionDTO.Timestamp within " +
                            "animatedMouseCursorDefinitionDTOs (" +
                            animatedMouseCursorDefinitionDTO.Id + ")");
        });
        DEFINITION_DTOS = animatedMouseCursorDefinitionDTOs;
        FACTORY = Check.ifNull(animatedMouseCursorProviderFactory,
                "animatedMouseCursorProviderFactory");
        PROCESS_RESULT = Check.ifNull(processResult, "processResult");
    }

    @Override
    public void run() {
        DEFINITION_DTOS.forEach(animatedMouseCursorDefinitionDTO -> {
            HashMap<Integer, Long> cursorsAtMs = new HashMap<>();
            for(AnimatedMouseCursorFrameDefinitionDTO frame :
                    animatedMouseCursorDefinitionDTO.Frames) {
                cursorsAtMs.put(frame.Ms, GET_MOUSE_CURSORS_BY_RELATIVE_LOCATION.apply(frame.Img));
            }
            PROCESS_RESULT.accept(FACTORY.make(
                    new AnimatedMouseCursorProviderDefinition(
                            animatedMouseCursorDefinitionDTO.Id,
                            cursorsAtMs,
                            animatedMouseCursorDefinitionDTO.Duration,
                            animatedMouseCursorDefinitionDTO.Offset,
                            animatedMouseCursorDefinitionDTO.Paused,
                            animatedMouseCursorDefinitionDTO.Timestamp
                    )
            ));
        });
    }
}
