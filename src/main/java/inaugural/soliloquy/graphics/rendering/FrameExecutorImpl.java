package inaugural.soliloquy.graphics.rendering;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.rendering.FrameExecutor;
import soliloquy.specs.graphics.rendering.RenderableStack;
import soliloquy.specs.graphics.rendering.renderers.StackRenderer;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.function.Consumer;

public class FrameExecutorImpl implements FrameExecutor {
    private final RenderableStack TOP_LEVEL_STACK;
    private final StackRenderer STACK_RENDERER;
    private final Semaphore SEMAPHORE;
    private final ArrayList<Consumer<Long>> FRAME_BLOCKING_EVENTS;

    public FrameExecutorImpl(RenderableStack topLevelStack, StackRenderer stackRenderer,
                             int semaphorePermissions) {
        TOP_LEVEL_STACK = Check.ifNull(topLevelStack, "topLevelStack");
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
    public void execute(long timestamp) {
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

        STACK_RENDERER.render(TOP_LEVEL_STACK, timestamp);
    }

    @Override
    public String getInterfaceName() {
        return FrameExecutor.class.getCanonicalName();
    }
}
