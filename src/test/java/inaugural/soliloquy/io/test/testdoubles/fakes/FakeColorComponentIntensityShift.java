package inaugural.soliloquy.io.test.testdoubles.fakes;

import soliloquy.specs.io.graphics.renderables.colorshifting.ColorComponent;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorComponentIntensityShift;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

public class FakeColorComponentIntensityShift implements ColorComponentIntensityShift {
    public ColorComponent ColorComponent;
    public ProviderAtTime<Float> ShiftAmountProvider;
    public boolean OverridesPriorShiftsOfSameType;

    public FakeColorComponentIntensityShift(ColorComponent colorComponent, Float value,
                                            boolean overridesPriorShiftsOfSameType) {
        ColorComponent = colorComponent;
        ShiftAmountProvider = new FakeStaticProvider<>(value);
        OverridesPriorShiftsOfSameType = overridesPriorShiftsOfSameType;
    }

    @Override
    public ColorComponent colorComponent() {
        return ColorComponent;
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
