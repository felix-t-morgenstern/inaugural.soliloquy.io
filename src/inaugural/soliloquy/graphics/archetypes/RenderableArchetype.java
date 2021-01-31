package inaugural.soliloquy.graphics.archetypes;

import soliloquy.specs.common.valueobjects.ReadableCoordinate;
import soliloquy.specs.graphics.colorshifting.ColorShiftType;
import soliloquy.specs.graphics.renderables.Renderable;

import java.util.List;

public class RenderableArchetype implements Renderable {
    @Override
    public boolean capturesMouseEvents() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void click() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void mouseOver() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void mouseLeave() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ReadableCoordinate screenLocation() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int z() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ReadableCoordinate dimensions() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<ColorShiftType> colorShifts() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Renderable> innerRenderables() {
        throw new UnsupportedOperationException();
    }

    // TODO: Test this
    @Override
    public String getInterfaceName() {
        return Renderable.class.getCanonicalName();
    }
}
