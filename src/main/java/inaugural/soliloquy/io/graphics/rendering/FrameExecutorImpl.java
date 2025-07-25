package inaugural.soliloquy.io.graphics.rendering;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.io.graphics.rendering.FrameExecutor;
import soliloquy.specs.io.graphics.rendering.renderers.ComponentRenderer;
import soliloquy.specs.ui.Component;

import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.function.Consumer;

import static inaugural.soliloquy.tools.collections.Collections.listOf;

public class FrameExecutorImpl implements FrameExecutor {
    private final Component TOP_LEVEL_COMPONENT;
    private final ComponentRenderer COMPONENT_RENDERER;
    private final Semaphore SEMAPHORE;
    private final List<Consumer<Long>> FRAME_BLOCKING_EVENTS;

    public FrameExecutorImpl(Component topLevelComponent, ComponentRenderer componentRenderer,
                             int semaphorePermissions) {
        TOP_LEVEL_COMPONENT = Check.ifNull(topLevelComponent, "topLevelComponent");
        COMPONENT_RENDERER = Check.ifNull(componentRenderer, "componentRenderer");
        SEMAPHORE = new Semaphore(
                Check.throwOnLteZero(semaphorePermissions, "semaphorePermissions"),
                true
        );
        FRAME_BLOCKING_EVENTS = listOf();
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

        COMPONENT_RENDERER.render(TOP_LEVEL_COMPONENT, timestamp);
    }
}
