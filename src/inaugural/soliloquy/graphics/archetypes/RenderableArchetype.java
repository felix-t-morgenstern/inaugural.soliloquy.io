package inaugural.soliloquy.graphics.archetypes;

import soliloquy.specs.graphics.renderables.Renderable;

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
}
