package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.rendering.timing.FrameTimer;

import java.util.List;

public class FakeFrameTimer implements FrameTimer {
    public boolean ShouldExecuteNextFrame;
    public List<Object> AddThisWhenLoadIsCalled;

    @Override
    public void setTargetFps(Float i) throws IllegalArgumentException {

    }

    @Override
    public void start() throws UnsupportedOperationException {

    }

    @Override
    public void stop() throws UnsupportedOperationException {

    }

    @Override
    public void registerFrameExecution() throws UnsupportedOperationException {

    }

    @Override
    public boolean shouldExecuteNextFrame() {
        if (AddThisWhenLoadIsCalled != null) {
            AddThisWhenLoadIsCalled.add(this);
        }
        return ShouldExecuteNextFrame;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
