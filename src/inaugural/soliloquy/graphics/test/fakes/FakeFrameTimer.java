package inaugural.soliloquy.graphics.test.fakes;

import soliloquy.specs.graphics.rendering.FrameTimer;

public class FakeFrameTimer implements FrameTimer {
    private int _pollingInterval;

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
        return false;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
