package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.assets.Sprite;
import soliloquy.specs.graphics.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.SpriteRenderable;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.util.List;

public class FakeSpriteRenderable implements SpriteRenderable {
    public Sprite Sprite;
    public List<ColorShift> ColorShifts;
    public FloatBox RenderingArea;

    public FakeSpriteRenderable(Sprite sprite, List<ColorShift> colorShifts,
                                FloatBox renderingArea) {
        Sprite = sprite;
        ColorShifts = colorShifts;
        RenderingArea = renderingArea;
    }

    @Override
    public Sprite sprite() {
        return Sprite;
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
    public List<ColorShift> colorShifts() {
        return ColorShifts;
    }

    @Override
    public FloatBox renderingArea() {
        return RenderingArea;
    }

    @Override
    public int z() {
        return 0;
    }

    @Override
    public void delete() {

    }

    @Override
    public String getInterfaceName() {
        return null;
    }

    @Override
    public EntityUuid id() {
        return null;
    }
}
