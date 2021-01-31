package inaugural.soliloquy.graphics.test.fakes;

import soliloquy.specs.common.valueobjects.ReadableCoordinate;
import soliloquy.specs.graphics.colorshifting.ColorShiftType;
import soliloquy.specs.graphics.renderables.Renderable;

import java.util.List;

public class FakeRenderable implements Renderable {
    public int _z;

    public FakeRenderable(int z) {
        _z = z;
    }

    @Override
    public boolean capturesMouseEvents() {
        return false;
    }

    @Override
    public void click() throws UnsupportedOperationException {

    }

    @Override
    public void mouseOver() throws UnsupportedOperationException {

    }

    @Override
    public void mouseLeave() throws UnsupportedOperationException {

    }

    @Override
    public ReadableCoordinate screenLocation() {
        return null;
    }

    @Override
    public int z() {
        return _z;
    }

    @Override
    public ReadableCoordinate dimensions() {
        return null;
    }

    @Override
    public List<ColorShiftType> colorShifts() {
        return null;
    }

    @Override
    public List<Renderable> innerRenderables() {
        return null;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
