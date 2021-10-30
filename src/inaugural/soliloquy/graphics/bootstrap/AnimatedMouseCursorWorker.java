package inaugural.soliloquy.graphics.bootstrap;

import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.renderables.providers.factories.AnimatedMouseCursorProviderFactory;

import java.util.HashMap;
import java.util.function.Consumer;

public class AnimatedMouseCursorWorker implements Runnable {
    private final AnimatedMouseCursorDTO ANIMATED_MOUSE_CURSOR_DTO;
    private final HashMap<String, Long> MOUSE_CURSORS_BY_RELATIVE_LOCATION;
    private final AnimatedMouseCursorProviderFactory ANIMATED_MOUSE_CURSOR_PROVIDER_FACTORY;
    private final Consumer<ProviderAtTime<Long>> RESULT_CONSUMER;

    public AnimatedMouseCursorWorker(AnimatedMouseCursorDTO animatedMouseCursorDTO,
                                     HashMap<String, Long> mouseCursorsByRelativeLocation,
                                     AnimatedMouseCursorProviderFactory
                                                 animatedMouseCursorProviderFactory,
                                     Consumer<ProviderAtTime<Long>> resultConsumer) {
        MOUSE_CURSORS_BY_RELATIVE_LOCATION = mouseCursorsByRelativeLocation;
        ANIMATED_MOUSE_CURSOR_DTO = animatedMouseCursorDTO;
        ANIMATED_MOUSE_CURSOR_PROVIDER_FACTORY = animatedMouseCursorProviderFactory;
        RESULT_CONSUMER = resultConsumer;
    }

    @Override
    public void run() {
        HashMap<Long, Long> cursorsAtMs = new HashMap<>();
        for(AnimatedMouseCursorFrameDTO frame : ANIMATED_MOUSE_CURSOR_DTO.Frames) {
            cursorsAtMs.put(frame.Ms, MOUSE_CURSORS_BY_RELATIVE_LOCATION.get(frame.Img));
        }
        RESULT_CONSUMER.accept(ANIMATED_MOUSE_CURSOR_PROVIDER_FACTORY.make(
                ANIMATED_MOUSE_CURSOR_DTO.Id, cursorsAtMs, ANIMATED_MOUSE_CURSOR_DTO.Duration,
                ANIMATED_MOUSE_CURSOR_DTO.Offset, ANIMATED_MOUSE_CURSOR_DTO.Paused,
                ANIMATED_MOUSE_CURSOR_DTO.Timestamp
        ));
    }

    public static class AnimatedMouseCursorDTO {
        public String Id;
        public AnimatedMouseCursorFrameDTO[] Frames;
        public int Duration;
        public int Offset;
        public Long Paused;
        public Long Timestamp;
    }

    public static class AnimatedMouseCursorFrameDTO {
        public long Ms;
        public String Img;
    }
}
