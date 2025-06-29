package inaugural.soliloquy.io.test.testdoubles.fakes;

import soliloquy.specs.io.graphics.renderables.colorshifting.ColorRotationShift;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

public class FakeColorRotationShift implements ColorRotationShift {
    public ProviderAtTime<Float> ShiftAmountProvider;
    public boolean OverridesPriorShiftsOfSameType;

    public FakeColorRotationShift(Float value, boolean overridesPriorShiftsOfSameType) {
        ShiftAmountProvider = new FakeStaticProvider<>(value);
        OverridesPriorShiftsOfSameType = overridesPriorShiftsOfSameType;
    }

    @Override
    public ProviderAtTime<Float> shiftAmountProvider() {
        return ShiftAmountProvider;
    }

    @Override
    public boolean overridesPriorShiftsOfSameType() {
        return OverridesPriorShiftsOfSameType;
    }
}
