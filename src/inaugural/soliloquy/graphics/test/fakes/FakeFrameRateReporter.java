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
    public String getInterfaceName() {
        return null;
    }
}
