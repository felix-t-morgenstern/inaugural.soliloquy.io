package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.renderables.colorshifting.BrightnessShift;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;

public class FakeBrightnessShift implements BrightnessShift {
    public ProviderAtTime<Float> ShiftAmountProvider;
    public boolean OverridesPriorShiftsOfSameType;

    public FakeBrightnessShift(Float value, boolean overridesPriorShiftsOfSameType) {
        ShiftAmountProvider = new FakeStaticProviderAtTime<>(value);
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

    @Override
    public String getInterfaceName() {
        return null;
    }
}
