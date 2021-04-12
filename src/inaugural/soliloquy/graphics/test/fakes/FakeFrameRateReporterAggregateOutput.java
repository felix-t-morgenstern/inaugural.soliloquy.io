package inaugural.soliloquy.graphics.test.fakes;

import soliloquy.specs.graphics.rendering.timing.FrameRateReporterAggregateOutput;

import java.util.ArrayList;
import java.util.Date;

public class FakeFrameRateReporterAggregateOutput implements FrameRateReporterAggregateOutput {
    public String Id;
    public ArrayList<Date> OutputtedAggregateDates = new ArrayList<>();
    public ArrayList<Float> OutputtedAggregateTargetFps = new ArrayList<>();
    public ArrayList<Float> OutputtedAggregateActualFps = new ArrayList<>();

    public FakeFrameRateReporterAggregateOutput(String id) {
        Id = id;
    }

    @Override
    public void outputAggregateFrameRateData(Date date, Float targetFps, Float actualFps) {
        OutputtedAggregateDates.add(date);
        OutputtedAggregateTargetFps.add(targetFps);
        OutputtedAggregateActualFps.add(actualFps);
    }

    @Override
    public String id() throws IllegalStateException {
        return Id;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
