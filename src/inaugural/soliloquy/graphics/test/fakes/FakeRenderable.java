package inaugural.soliloquy.graphics.test.fakes;

import soliloquy.specs.common.shared.Cloneable;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.rendering.FloatBox;

@SuppressWarnings("rawtypes")
public class FakeRenderable implements Renderable {
    public int Z;
    public FloatBox RenderingArea;

    public FakeRenderable(int z) {
        Z = z;
    }

    @Override
    public FloatBox renderingArea() {
        return RenderingArea;
    }

    @Override
    public int z() {
        return Z;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Cloneable makeClone() {
        return null;
    }
}
