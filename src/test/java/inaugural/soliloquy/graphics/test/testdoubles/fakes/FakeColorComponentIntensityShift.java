package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.renderables.colorshifting.ColorComponent;
import soliloquy.specs.graphics.renderables.colorshifting.ColorComponentIntensityShift;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;

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
