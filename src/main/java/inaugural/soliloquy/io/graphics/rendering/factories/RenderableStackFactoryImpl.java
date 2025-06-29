package inaugural.soliloquy.io.graphics.rendering.factories;

import inaugural.soliloquy.io.graphics.rendering.RenderableStackImpl;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderableStack;
import soliloquy.specs.io.graphics.rendering.factories.RenderableStackFactory;

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
}
