package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.rendering.FrameExecutor;
import soliloquy.specs.graphics.rendering.renderers.StackRenderer;
import soliloquy.specs.graphics.rendering.timing.GlobalClock;

import java.util.function.Consumer;

public class FakeFrameExecutor implements FrameExecutor {
    public StackRenderer StackRenderer;
    public GlobalClock GlobalClock;
    public int NumberOfTimesExecuteCalled;

    public FakeFrameExecutor() {

    }

    public FakeFrameExecutor(StackRenderer stackRenderer, GlobalClock globalClock) {
        StackRenderer = stackRenderer;
        GlobalClock = globalClock == null ? new FakeGlobalClock() : globalClock;
    }

    @Override
    public void registerFrameBlockingEvent(Consumer<Long> consumer)
            throws IllegalArgumentException {

    }

    @Override
    public void execute() {
        if (StackRenderer != null) {
            StackRenderer.render(GlobalClock.globalTimestamp());
        }
        NumberOfTimesExecuteCalled++;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
