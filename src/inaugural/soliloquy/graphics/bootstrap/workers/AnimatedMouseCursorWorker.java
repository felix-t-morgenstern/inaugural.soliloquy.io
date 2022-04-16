package inaugural.soliloquy.graphics.bootstrap.workers;

import inaugural.soliloquy.graphics.api.dto.AnimatedMouseCursorDefinitionDTO;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.renderables.providers.factories.AnimatedMouseCursorProviderFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public class AnimatedMouseCursorWorker implements Runnable {
    private final Collection<AnimatedMouseCursorDefinitionDTO> ANIMATED_MOUSE_CURSOR_DTOS;
    private final Function<String, Long> GET_MOUSE_CURSORS_BY_RELATIVE_LOCATION;
    private final AnimatedMouseCursorProviderFactory ANIMATED_MOUSE_CURSOR_PROVIDER_FACTORY;
    private final Consumer<ProviderAtTime<Long>> RESULT_CONSUMER;

    /** @noinspection ConstantConditions*/
    public AnimatedMouseCursorWorker(Function<String, Long> getMouseCursorsByRelativeLocation,
                                     Collection<AnimatedMouseCursorDefinitionDTO>
                                             animatedMouseCursorDefinitionDTOs,
                                     AnimatedMouseCursorProviderFactory
                                                 animatedMouseCursorProviderFactory,
                                     Consumer<ProviderAtTime<Long>> resultConsumer) {
        GET_MOUSE_CURSORS_BY_RELATIVE_LOCATION = Check.ifNull(getMouseCursorsByRelativeLocation,
                "getMouseCursorsByRelativeLocation");
        Check.ifNull(animatedMouseCursorDefinitionDTOs, "animatedMouseCursorDefinitionDTOs");
        if (animatedMouseCursorDefinitionDTOs.isEmpty()) {
            throw new IllegalArgumentException(
                    "AnimatedMouseCursorWorker: animatedMouseCursorDefinitionDTOs is empty");
        }
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
                throw new IllegalArgumentException("AnimatedMouseCursorWorker: " +
                        "animatedMouseCursorDefinitionDTO.Frames is empty within " +
                        "animatedMouseCursorDefinitionDTOs (" +
                        animatedMouseCursorDefinitionDTO.Id + ")");
            }
            int maxFrameMs = 0;
            boolean frameAt0Ms = false;
            for (AnimatedMouseCursorDefinitionDTO.AnimatedMouseCursorFrameDTO frameDTO :
                    animatedMouseCursorDefinitionDTO.Frames) {
                Check.ifNullOrEmpty(frameDTO.Img,
                        "frame within animatedMouseCursorDefinitionDTOs (" +
                                animatedMouseCursorDefinitionDTO.Id + ")");
                frameAt0Ms = frameAt0Ms || frameDTO.Ms == 0;
                maxFrameMs = Math.max(maxFrameMs, frameDTO.Ms);
            }
            if (!frameAt0Ms) {
                throw new IllegalArgumentException("AnimatedMouseCursorWorker: " +
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
        ANIMATED_MOUSE_CURSOR_DTOS = animatedMouseCursorDefinitionDTOs;
        ANIMATED_MOUSE_CURSOR_PROVIDER_FACTORY = Check.ifNull(animatedMouseCursorProviderFactory,
                "animatedMouseCursorProviderFactory");
        RESULT_CONSUMER = Check.ifNull(resultConsumer, "resultConsumer");
    }

    @Override
    public void run() {
        ANIMATED_MOUSE_CURSOR_DTOS.forEach(animatedMouseCursorDefinitionDTO -> {
            HashMap<Integer, Long> cursorsAtMs = new HashMap<>();
            for(AnimatedMouseCursorDefinitionDTO.AnimatedMouseCursorFrameDTO frame :
                    animatedMouseCursorDefinitionDTO.Frames) {
                cursorsAtMs.put(frame.Ms, GET_MOUSE_CURSORS_BY_RELATIVE_LOCATION.apply(frame.Img));
            }
            RESULT_CONSUMER.accept(ANIMATED_MOUSE_CURSOR_PROVIDER_FACTORY.make(
                    animatedMouseCursorDefinitionDTO.Id, cursorsAtMs,
                    animatedMouseCursorDefinitionDTO.Duration,
                    animatedMouseCursorDefinitionDTO.Offset,
                    animatedMouseCursorDefinitionDTO.Paused,
                    animatedMouseCursorDefinitionDTO.Timestamp
            ));
        });
    }
}
