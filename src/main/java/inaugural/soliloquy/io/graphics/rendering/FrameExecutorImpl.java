package inaugural.soliloquy.io.graphics.rendering;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.rendering.FrameExecutor;
import soliloquy.specs.io.graphics.rendering.renderers.ComponentRenderer;

import java.util.List;
import java.util.function.Consumer;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.concurrency.Concurrency.waitUntilTasksCompleted;
import static java.util.concurrent.CompletableFuture.runAsync;

public class FrameExecutorImpl implements FrameExecutor {
    private final ComponentRenderer COMPONENT_RENDERER;
    private final List<Consumer<Long>> FRAME_BLOCKING_EVENTS;
    private final Runnable REPORT_FRAME_COMPLETION;

    private Component topLevelComponent;

    public FrameExecutorImpl(ComponentRenderer componentRenderer,
                             Runnable reportFrameCompletion) {
        COMPONENT_RENDERER = Check.ifNull(componentRenderer, "componentRenderer");
        FRAME_BLOCKING_EVENTS = listOf();
        REPORT_FRAME_COMPLETION = Check.ifNull(reportFrameCompletion, "reportFrameCompletion");
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
        // A while loop is used here instead of an if statement, since frame blocking events may
        // emerge within other frame-blocking events, and those should block the same frame
        while (!FRAME_BLOCKING_EVENTS.isEmpty()) {
            var frameBlockingTasks = FRAME_BLOCKING_EVENTS.stream()
                    .map(event -> runAsync(() -> event.accept(timestamp))).toList();
            FRAME_BLOCKING_EVENTS.clear();
            waitUntilTasksCompleted(frameBlockingTasks, () -> false);
        }

        COMPONENT_RENDERER.render(topLevelComponent, timestamp);

        REPORT_FRAME_COMPLETION.run();
    }
}
