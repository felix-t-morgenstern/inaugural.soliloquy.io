package inaugural.soliloquy.graphics.archetypes;

import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.rendering.FloatBox;

public class RenderableArchetype implements Renderable {
    @Override
    public FloatBox renderingArea() {
        return null;
    }

    @Override
    public int z() {
        return 0;
    }

    @Override
    public void delete() {

    }

    // TODO: Test this
    @Override
    public String getInterfaceName() {
        return Renderable.class.getCanonicalName();
    }

    @Override
    public EntityUuid id() {
        return null;
    }
}
