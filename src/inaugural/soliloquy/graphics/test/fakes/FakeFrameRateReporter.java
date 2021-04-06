package inaugural.soliloquy.graphics.test.fakes;

import soliloquy.specs.graphics.rendering.FrameRateReporter;

import java.util.ArrayList;
import java.util.Date;

public class FakeFrameRateReporter implements FrameRateReporter {
    public ArrayList<Date> Dates = new ArrayList<>();
    public ArrayList<Float> TargetFps = new ArrayList<>();
    public ArrayList<Float> ActualFps = new ArrayList<>();

    @Override
    public void reportFrameRate(Date date, Float targetFps, float actualFps)
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
    public String getInterfaceName() {
        return null;
    }
}
