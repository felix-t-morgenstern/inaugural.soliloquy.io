package inaugural.soliloquy.io.test.testdoubles.fakes;

import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShiftStackAggregator;
import soliloquy.specs.io.graphics.renderables.colorshifting.NetColorShifts;

import java.util.List;

import static soliloquy.specs.io.graphics.renderables.colorshifting.NetColorShifts.netShifts;

public class FakeColorShiftStackAggregator implements ColorShiftStackAggregator {
    public Long Input;
    public NetColorShifts Output;

    public FakeColorShiftStackAggregator() {
        Output = netShifts(0, 0, 0, 0, 0);
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
