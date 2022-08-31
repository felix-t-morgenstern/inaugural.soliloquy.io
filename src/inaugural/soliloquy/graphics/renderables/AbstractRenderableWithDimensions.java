package inaugural.soliloquy.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.renderables.RenderableWithDimensions;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.RenderableStack;

import java.util.UUID;

public abstract class AbstractRenderableWithDimensions extends AbstractRenderable
        implements RenderableWithDimensions {
    protected ProviderAtTime<FloatBox> _renderingAreaProvider;

    public AbstractRenderableWithDimensions(ProviderAtTime<FloatBox> renderingDimensionsProvider,
                                            int z, UUID uuid, RenderableStack containingStack) {
        super(z, uuid, containingStack);
        setRenderingDimensionsProvider(renderingDimensionsProvider);
    }

    @Override
    public ProviderAtTime<FloatBox> getRenderingDimensionsProvider() {
        return _renderingAreaProvider;
    }

    @Override
    public void setRenderingDimensionsProvider(ProviderAtTime<FloatBox>
                                                       renderingDimensionsProvider)
            throws IllegalArgumentException {
        _renderingAreaProvider = Check.ifNull(renderingDimensionsProvider,
                "renderingDimensionsProvider");
    }
}
