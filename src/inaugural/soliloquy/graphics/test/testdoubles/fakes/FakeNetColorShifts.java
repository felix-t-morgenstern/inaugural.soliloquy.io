package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.renderables.colorshifting.NetColorShifts;

public class FakeNetColorShifts implements NetColorShifts {
    public float BrightnessShift;
    public float RedIntensityShift;
    public float GreenIntensityShift;
    public float BlueIntensityShift;
    public float ColorRotationShift;

    public FakeNetColorShifts() {

    }

    @Override
    public float brightnessShift() {
        return BrightnessShift;
    }

    @Override
    public float redIntensityShift() {
        return RedIntensityShift;
    }

    @Override
    public float greenIntensityShift() {
        return GreenIntensityShift;
    }

    @Override
    public float blueIntensityShift() {
        return BlueIntensityShift;
    }

    @Override
    public float colorRotationShift() {
        return ColorRotationShift;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
