package inaugural.soliloquy.graphics.archetypes;

import soliloquy.specs.common.shared.Cloneable;
import soliloquy.specs.graphics.renderables.Renderable;

@SuppressWarnings("rawtypes")
public class RenderableArchetype implements Renderable {
    @Override
    public float xLoc() {
        return 0;
    }

    @Override
    public float yLoc() {
        return 0;
    }

    @Override
    public float width() {
        return 0;
    }

    @Override
    public float height() {
        return 0;
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
