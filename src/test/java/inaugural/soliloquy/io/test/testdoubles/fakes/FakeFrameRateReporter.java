package inaugural.soliloquy.io.test.testdoubles.fakes;

import soliloquy.specs.io.graphics.rendering.timing.FrameRateReporter;

import java.util.List;

import static inaugural.soliloquy.tools.collections.Collections.listOf;


public class FakeFrameRateReporter implements FrameRateReporter {
    public List<Long> Dates = listOf();
    public List<Float> TargetFps = listOf();
    public List<Float> ActualFps = listOf();

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
}
