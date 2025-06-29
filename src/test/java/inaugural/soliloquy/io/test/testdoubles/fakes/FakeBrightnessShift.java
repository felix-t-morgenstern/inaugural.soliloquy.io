package inaugural.soliloquy.io.test.testdoubles.fakes;

import soliloquy.specs.io.graphics.renderables.colorshifting.BrightnessShift;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

public class FakeBrightnessShift implements BrightnessShift {
    public ProviderAtTime<Float> ShiftAmountProvider;
    public boolean OverridesPriorShiftsOfSameType;

    public FakeBrightnessShift(Float value, boolean overridesPriorShiftsOfSameType) {
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
