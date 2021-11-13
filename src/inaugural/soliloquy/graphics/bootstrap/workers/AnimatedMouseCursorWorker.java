package inaugural.soliloquy.graphics.bootstrap.workers;

import inaugural.soliloquy.graphics.api.dto.AnimatedMouseCursorDTO;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.renderables.providers.factories.AnimatedMouseCursorProviderFactory;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public class AnimatedMouseCursorWorker implements Runnable {
    private final AnimatedMouseCursorDTO ANIMATED_MOUSE_CURSOR_DTO;
    private final Function<String, Long> GET_MOUSE_CURSORS_BY_RELATIVE_LOCATION;
    private final AnimatedMouseCursorProviderFactory ANIMATED_MOUSE_CURSOR_PROVIDER_FACTORY;
    private final Consumer<ProviderAtTime<Long>> RESULT_CONSUMER;

    public AnimatedMouseCursorWorker(AnimatedMouseCursorDTO animatedMouseCursorDTO,
                                     Function<String, Long> getMouseCursorsByRelativeLocation,
                                     AnimatedMouseCursorProviderFactory
                                                 animatedMouseCursorProviderFactory,
                                     Consumer<ProviderAtTime<Long>> resultConsumer) {
        GET_MOUSE_CURSORS_BY_RELATIVE_LOCATION = getMouseCursorsByRelativeLocation;
        ANIMATED_MOUSE_CURSOR_DTO = animatedMouseCursorDTO;
        ANIMATED_MOUSE_CURSOR_PROVIDER_FACTORY = animatedMouseCursorProviderFactory;
        RESULT_CONSUMER = resultConsumer;
    }

    @Override
    public void run() {
        HashMap<Long, Long> cursorsAtMs = new HashMap<>();
        for(AnimatedMouseCursorDTO.AnimatedMouseCursorFrameDTO frame : ANIMATED_MOUSE_CURSOR_DTO.Frames) {
            cursorsAtMs.put(frame.Ms, GET_MOUSE_CURSORS_BY_RELATIVE_LOCATION.apply(frame.Img));
        }
        RESULT_CONSUMER.accept(ANIMATED_MOUSE_CURSOR_PROVIDER_FACTORY.make(
                ANIMATED_MOUSE_CURSOR_DTO.Id, cursorsAtMs, ANIMATED_MOUSE_CURSOR_DTO.Duration,
                ANIMATED_MOUSE_CURSOR_DTO.Offset, ANIMATED_MOUSE_CURSOR_DTO.Paused,
                ANIMATED_MOUSE_CURSOR_DTO.Timestamp
        ));
    }
}
