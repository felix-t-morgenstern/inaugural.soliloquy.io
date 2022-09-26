package inaugural.soliloquy.graphics.rendering.factories;

import inaugural.soliloquy.graphics.rendering.RenderableStackImpl;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.RenderableStack;
import soliloquy.specs.graphics.rendering.factories.RenderableStackFactory;

import java.util.UUID;

public class RenderableStackFactoryImpl implements RenderableStackFactory {
    @Override
    public RenderableStack makeTopLevelStack() {
        return new RenderableStackImpl();
    }

    @Override
    public RenderableStack makeContainedStack(UUID uuid, int z,
                                              ProviderAtTime<FloatBox> renderingBoundariesProvider,
                                              RenderableStack containingStack)
            throws IllegalArgumentException {
        return new RenderableStackImpl(uuid, z, renderingBoundariesProvider, containingStack);
    }

    @Override
    public String getInterfaceName() {
        return RenderableStackFactory.class.getCanonicalName();
    }
}
