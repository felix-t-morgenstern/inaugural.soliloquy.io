package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.gamestate.entities.gameevents.firings.FrameBlockingEvent;
import soliloquy.specs.gamestate.entities.gameevents.firings.TriggeredEvent;
import soliloquy.specs.graphics.rendering.FrameExecutor;
import soliloquy.specs.graphics.rendering.renderers.StackRenderer;
import soliloquy.specs.graphics.rendering.timing.GlobalClock;

public class FakeFrameExecutor implements FrameExecutor {
    public StackRenderer StackRenderer;
    public GlobalClock GlobalClock;
    public int NumberOfTimesExecuteCalled;

    public FakeFrameExecutor() {

    }

    public FakeFrameExecutor(StackRenderer stackRenderer, GlobalClock globalClock) {
        StackRenderer = stackRenderer;
        GlobalClock = globalClock;
    }

    @Override
    public void placeEventFiringBlock(TriggeredEvent triggeredEvent) throws IllegalArgumentException {

    }

    @Override
    public void releaseEventFiringBlock(TriggeredEvent triggeredEvent) throws IllegalArgumentException {

    }

    @Override
    public void registerTriggeredEvent(TriggeredEvent triggeredEvent) throws IllegalArgumentException {

    }

    @Override
    public void registerFrameBlockingEvent(FrameBlockingEvent frameBlockingEvent) throws IllegalArgumentException {

    }

    @Override
    public void execute() {
        if (StackRenderer != null) {
            StackRenderer.render(GlobalClock != null ? GlobalClock.globalTimestamp() : 0L);
        }
        NumberOfTimesExecuteCalled++;
    }
}
