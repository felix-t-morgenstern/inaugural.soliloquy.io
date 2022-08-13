package inaugural.soliloquy.graphics.rendering;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.rendering.FrameExecutor;
import soliloquy.specs.graphics.rendering.renderers.StackRenderer;
import soliloquy.specs.graphics.rendering.timing.GlobalClock;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.function.Consumer;

public class FrameExecutorImpl implements FrameExecutor {
    private final GlobalClock GLOBAL_CLOCK;
    private final StackRenderer STACK_RENDERER;
    private final Semaphore SEMAPHORE;
    private final ArrayList<Consumer<Long>> FRAME_BLOCKING_EVENTS;

    public FrameExecutorImpl(GlobalClock globalClock, StackRenderer stackRenderer,
                             int semaphorePermissions) {
        GLOBAL_CLOCK = Check.ifNull(globalClock, "globalClock");
        STACK_RENDERER = Check.ifNull(stackRenderer, "stackRenderer");
        SEMAPHORE = new Semaphore(
                Check.throwOnLteZero(semaphorePermissions, "semaphorePermissions"),
                true
        );
        FRAME_BLOCKING_EVENTS = new ArrayList<>();
    }

    @Override
    public void registerFrameBlockingEvent(Consumer<Long> frameBlockingEvent)
            throws IllegalArgumentException {
        FRAME_BLOCKING_EVENTS.add(Check.ifNull(frameBlockingEvent, "frameBlockingEvent"));
    }

    @Override
    public void execute() {
        long timestamp = GLOBAL_CLOCK.globalTimestamp();

        for (Consumer<Long> frameBlockingEvent : FRAME_BLOCKING_EVENTS) {
            try {
                SEMAPHORE.acquire();
                new Thread(() -> {
                    frameBlockingEvent.accept(timestamp);
                    SEMAPHORE.release();
                }).start();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        FRAME_BLOCKING_EVENTS.clear();

        STACK_RENDERER.render(timestamp);
    }

    @Override
    public String getInterfaceName() {
        return FrameExecutor.class.getCanonicalName();
    }
}
