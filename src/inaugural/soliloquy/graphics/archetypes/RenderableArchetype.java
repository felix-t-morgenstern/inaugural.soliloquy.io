package inaugural.soliloquy.graphics.archetypes;

import soliloquy.specs.common.shared.Cloneable;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.rendering.FloatBox;

@SuppressWarnings("rawtypes")
public class RenderableArchetype implements Renderable {
    @Override
    public FloatBox renderingArea() {
        return null;
    }

    @Override
    public int z() {
        return 0;
    }

    // TODO: Test this
    @Override
    public String getInterfaceName() {
        return Renderable.class.getCanonicalName();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Cloneable makeClone() {
        return null;
    }
}
