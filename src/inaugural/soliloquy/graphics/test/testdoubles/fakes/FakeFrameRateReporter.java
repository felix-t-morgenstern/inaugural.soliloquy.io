package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.rendering.timing.FrameRateReporter;

import java.util.ArrayList;

public class FakeFrameRateReporter implements FrameRateReporter {
    public ArrayList<Long> Dates = new ArrayList<>();
    public ArrayList<Float> TargetFps = new ArrayList<>();
    public ArrayList<Float> ActualFps = new ArrayList<>();

    @Override
    public void reportFrameRate(long date, Float targetFps, float actualFps)
            throws IllegalArgumentException {
        Dates.add(date);
        TargetFps.add(targetFps);
        ActualFps.add(actualFps);
    }

    @Override
    public Float currentActualFps() {
        return null;
    }

    @Override
    public void activateAggregateOutput(String s) throws IllegalArgumentException {

    }

    @Override
    public void deactivateAggregateOutput(String s) throws IllegalArgumentException {

    }

    @Override
    public void reportPause(long l) throws IllegalArgumentException {

    }

    @Override
    public void reportUnpause(long l) throws IllegalArgumentException {

    }

    @Override
    public Long pausedTimestamp() {
        return null;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
