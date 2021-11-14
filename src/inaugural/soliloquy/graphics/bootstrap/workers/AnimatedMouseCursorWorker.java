package inaugural.soliloquy.graphics.bootstrap.workers;

import inaugural.soliloquy.graphics.api.dto.AnimatedMouseCursorDTO;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.renderables.providers.factories.AnimatedMouseCursorProviderFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public class AnimatedMouseCursorWorker implements Runnable {
    private final Collection<AnimatedMouseCursorDTO> ANIMATED_MOUSE_CURSOR_DTOS;
    private final Function<String, Long> GET_MOUSE_CURSORS_BY_RELATIVE_LOCATION;
    private final AnimatedMouseCursorProviderFactory ANIMATED_MOUSE_CURSOR_PROVIDER_FACTORY;
    private final Consumer<ProviderAtTime<Long>> RESULT_CONSUMER;

    /** @noinspection ConstantConditions*/
    public AnimatedMouseCursorWorker(Function<String, Long> getMouseCursorsByRelativeLocation,
                                     Collection<AnimatedMouseCursorDTO> animatedMouseCursorDTOs,
                                     AnimatedMouseCursorProviderFactory
                                                 animatedMouseCursorProviderFactory,
                                     Consumer<ProviderAtTime<Long>> resultConsumer) {
        GET_MOUSE_CURSORS_BY_RELATIVE_LOCATION = Check.ifNull(getMouseCursorsByRelativeLocation,
                "getMouseCursorsByRelativeLocation");
        Check.ifNull(animatedMouseCursorDTOs, "animatedMouseCursorDTOs");
        if (animatedMouseCursorDTOs.isEmpty()) {
            throw new IllegalArgumentException(
                    "AnimatedMouseCursorWorker: animatedMouseCursorDTOs is empty");
        }
        animatedMouseCursorDTOs.forEach(animatedMouseCursorDTO -> {
            Check.ifNull(animatedMouseCursorDTO,
                    "animatedMouseCursorDTO within animatedMouseCursorDTOs");
            Check.ifNullOrEmpty(animatedMouseCursorDTO.Id,
                    "animatedMouseCursorDTO.Id within animatedMouseCursorDTOs");
            Check.ifNull(animatedMouseCursorDTO.Frames,
                    "animatedMouseCursorDTO.Frames within animatedMouseCursorDTOs (" +
                    animatedMouseCursorDTO.Id + ")");
            if (animatedMouseCursorDTO.Frames.length == 0) {
                throw new IllegalArgumentException("AnimatedMouseCursorWorker: " +
                        "animatedMouseCursorDTO.Frames is empty within animatedMouseCursorDTOs (" +
                        animatedMouseCursorDTO.Id + ")");
            }
            int maxFrameMs = 0;
            boolean frameAt0Ms = false;
            for (AnimatedMouseCursorDTO.AnimatedMouseCursorFrameDTO frameDTO :
                    animatedMouseCursorDTO.Frames) {
                Check.ifNullOrEmpty(frameDTO.Img,
                        "frame within animatedMouseCursorDTOs (" +
                                animatedMouseCursorDTO.Id + ")");
                frameAt0Ms = frameAt0Ms || frameDTO.Ms == 0;
                maxFrameMs = Math.max(maxFrameMs, frameDTO.Ms);
            }
            if (!frameAt0Ms) {
                throw new IllegalArgumentException("AnimatedMouseCursorWorker: " +
                        "animatedMouseCursorDTO.Frames has no frame at 0ms within " +
                        "animatedMouseCursorDTOs (" + animatedMouseCursorDTO.Id + ")");
            }
            Check.throwOnSecondLte(maxFrameMs, animatedMouseCursorDTO.Duration,
                    "maximum frame ms", "animatedMouseCursorDTO.Duration within " +
                            "animatedMouseCursorDTOs (" + animatedMouseCursorDTO.Id + ")");
            Check.throwOnSecondLte(animatedMouseCursorDTO.Offset, animatedMouseCursorDTO.Duration,
                    "animatedMouseCursorDTO.Offset", "animatedMouseCursorDTO.Duration within " +
                            "animatedMouseCursorDTOs (" + animatedMouseCursorDTO.Id + ")");
            Check.throwOnLtValue(animatedMouseCursorDTO.Offset, 0, "animatedMouseCursorDTO.Offset " +
                    "within animatedMouseCursorDTOs (" + animatedMouseCursorDTO.Id + ")");
            if (animatedMouseCursorDTO.Paused != null &&
                    animatedMouseCursorDTO.Timestamp == null) {
                throw new IllegalArgumentException("animatedMouseCursorDTO.Paused is non-null " +
                        "while animatedMouseCursorDTO.Timestamp is null (id = " +
                        animatedMouseCursorDTO.Id + ")");
            }
            Check.throwOnSecondLte(animatedMouseCursorDTO.Paused, animatedMouseCursorDTO.Timestamp,
                    "animatedMouseCursorDTO.Paused", "animatedMouseCursorDTO.Timestamp within " +
                            "animatedMouseCursorDTOs (" + animatedMouseCursorDTO.Id + ")");
        });
        ANIMATED_MOUSE_CURSOR_DTOS = animatedMouseCursorDTOs;
        ANIMATED_MOUSE_CURSOR_PROVIDER_FACTORY = Check.ifNull(animatedMouseCursorProviderFactory,
                "animatedMouseCursorProviderFactory");
        RESULT_CONSUMER = Check.ifNull(resultConsumer, "resultConsumer");
    }

    @Override
    public void run() {
        ANIMATED_MOUSE_CURSOR_DTOS.forEach(animatedMouseCursorDTO -> {
            HashMap<Integer, Long> cursorsAtMs = new HashMap<>();
            for(AnimatedMouseCursorDTO.AnimatedMouseCursorFrameDTO frame :
                    animatedMouseCursorDTO.Frames) {
                cursorsAtMs.put(frame.Ms, GET_MOUSE_CURSORS_BY_RELATIVE_LOCATION.apply(frame.Img));
            }
            RESULT_CONSUMER.accept(ANIMATED_MOUSE_CURSOR_PROVIDER_FACTORY.make(
                    animatedMouseCursorDTO.Id, cursorsAtMs, animatedMouseCursorDTO.Duration,
                    animatedMouseCursorDTO.Offset, animatedMouseCursorDTO.Paused,
                    animatedMouseCursorDTO.Timestamp
            ));
        });
    }
}
