package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.renderables.colorshifting.NetColorShifts;

public class FakeNetColorShifts implements NetColorShifts {
    public float NetBrightnessShift;
    public float NetRedShift;
    public float NetGreenShift;
    public float NetBlueShift;
    public float NetColorRotationShift;

    public FakeNetColorShifts() {

    }

    @Override
    public float netBrightnessShift() {
        return NetBrightnessShift;
    }

    @Override
    public float netRedShift() {
        return NetRedShift;
    }

    @Override
    public float netGreenShift() {
        return NetGreenShift;
    }

    @Override
    public float netBlueShift() {
        return NetBlueShift;
    }

    @Override
    public float netColorRotationShift() {
        return NetColorRotationShift;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
