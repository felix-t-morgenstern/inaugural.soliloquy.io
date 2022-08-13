package inaugural.soliloquy.graphics.rendering.factories;

import inaugural.soliloquy.graphics.rendering.RenderableStackImpl;
import soliloquy.specs.graphics.rendering.RenderableStack;
import soliloquy.specs.graphics.rendering.factories.RenderableStackFactory;

public class RenderableStackFactoryImpl implements RenderableStackFactory {
    @Override
    public RenderableStack make()
            throws IllegalArgumentException {
        return new RenderableStackImpl();
    }

    @Override
    public String getInterfaceName() {
        return RenderableStackFactory.class.getCanonicalName();
    }
}
