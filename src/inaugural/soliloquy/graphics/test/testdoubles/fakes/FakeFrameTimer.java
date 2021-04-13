package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.rendering.timing.FrameTimer;

public class FakeFrameTimer implements FrameTimer {
    public boolean ShouldExecuteNextFrame;

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
        return ShouldExecuteNextFrame;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
