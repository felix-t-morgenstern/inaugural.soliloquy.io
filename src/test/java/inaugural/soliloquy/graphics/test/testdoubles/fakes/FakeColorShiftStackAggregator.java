package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShiftStackAggregator;
import soliloquy.specs.graphics.renderables.colorshifting.NetColorShifts;

import java.util.List;

public class FakeColorShiftStackAggregator implements ColorShiftStackAggregator {
    public Long Input;
    public NetColorShifts Output;

    public FakeColorShiftStackAggregator() {
        Output = new FakeNetColorShifts();
    }

    public FakeColorShiftStackAggregator(NetColorShifts netColorShifts) {
        Output = netColorShifts;
    }

    @Override
    public NetColorShifts aggregate(List<ColorShift> colorShifts, long timestamp)
            throws IllegalArgumentException {
        Input = timestamp;
        return Output;
    }
}
