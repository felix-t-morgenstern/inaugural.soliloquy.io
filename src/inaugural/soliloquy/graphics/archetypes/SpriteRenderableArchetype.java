package inaugural.soliloquy.graphics.archetypes;

import soliloquy.specs.graphics.assets.Sprite;
import soliloquy.specs.graphics.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.SpriteRenderable;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.util.List;

public class SpriteRenderableArchetype implements SpriteRenderable {
    @Override
    public Sprite sprite() {
        return null;
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
        return null;
    }

    @Override
    public FloatBox renderingArea() {
        return null;
    }

    @Override
    public int z() {
        return 0;
    }

    @Override
    public Renderable<SpriteRenderable> makeClone() {
        return null;
    }

    @Override
    public String getInterfaceName() {
        return SpriteRenderable.class.getCanonicalName();
    }
}
