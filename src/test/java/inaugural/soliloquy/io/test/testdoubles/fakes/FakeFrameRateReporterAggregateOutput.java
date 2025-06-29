package inaugural.soliloquy.io.test.testdoubles.fakes;

import soliloquy.specs.io.graphics.rendering.timing.FrameRateReporterAggregateOutput;

import java.util.Date;
import java.util.List;

import static inaugural.soliloquy.tools.collections.Collections.listOf;

public class FakeFrameRateReporterAggregateOutput implements FrameRateReporterAggregateOutput {
    public String Id;
    public List<Date> OutputtedAggregateDates = listOf();
    public List<Float> OutputtedAggregateTargetFps = listOf();
    public List<Float> OutputtedAggregateActualFps = listOf();

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
}
