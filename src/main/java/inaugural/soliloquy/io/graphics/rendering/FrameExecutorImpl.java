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
    private final ComponentRenderer COMPONENT_RENDERER;
    private final Semaphore SEMAPHORE;
    private final List<Consumer<Long>> FRAME_BLOCKING_EVENTS;

    private Component topLevelComponent;

    public FrameExecutorImpl(ComponentRenderer componentRenderer,
                             int semaphorePermissions) {
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
    public void setTopLevelComponent(Component component) throws IllegalArgumentException {
        if (topLevelComponent != null) {
            topLevelComponent.delete();
        }
        topLevelComponent = Check.ifNull(component, "component");
    }

    @Override
    public void execute(long timestamp) {
        if (topLevelComponent == null) {
            throw new IllegalStateException("FrameExecutorImpl.execute: no top-level component");
        }
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

        COMPONENT_RENDERER.render(topLevelComponent, timestamp);
    }
}
