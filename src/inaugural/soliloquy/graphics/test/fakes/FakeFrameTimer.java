package inaugural.soliloquy.graphics.test.fakes;

import soliloquy.specs.graphics.rendering.FrameTimer;

public class FakeFrameTimer implements FrameTimer {
    private int _pollingInterval;

    public boolean ShouldExecuteNextFrame;

    @Override
    public int getPollingInterval() {
        return _pollingInterval;
    }

    @Override
    public void setPollingInterval(int i) throws IllegalArgumentException {
        _pollingInterval = i;
    }

    @Override
    public void setTargetFps(int i) throws IllegalArgumentException {

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
