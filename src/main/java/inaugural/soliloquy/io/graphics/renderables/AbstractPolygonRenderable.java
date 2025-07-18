package inaugural.soliloquy.io.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.io.graphics.renderables.PolygonRenderable;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderableStack;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;

import java.util.Map;

public abstract class AbstractPolygonRenderable
        extends AbstractRenderableWithMouseEvents
        implements PolygonRenderable {
    private ProviderAtTime<Integer> backgroundTextureIdProvider;
    private ProviderAtTime<Float> textureTileWidthProvider;
    private ProviderAtTime<Float> textureTileHeightProvider;

    protected AbstractPolygonRenderable(
            ProviderAtTime<Integer> textureIdProvider,
            ProviderAtTime<Float> textureTileWidthProvider,
            ProviderAtTime<Float> textureTileHeight,
            Map<Integer, Action<MouseEventInputs>> onPress,
            Map<Integer, Action<MouseEventInputs>> onRelease,
            Action<MouseEventInputs> onMouseOver,
            Action<MouseEventInputs> onMouseLeave,
            int z,
            java.util.UUID uuid,
            RenderableStack containingStack,
            RenderingBoundaries renderingBoundaries
    ) {
        super(false, onPress, onRelease, onMouseOver, onMouseLeave, z, uuid, containingStack,
                renderingBoundaries);
        setTextureIdProvider(textureIdProvider);
        setTextureTileWidthProvider(textureTileWidthProvider);
        setTextureTileHeightProvider(textureTileHeight);
    }

    @Override
    public ProviderAtTime<Integer> getTextureIdProvider() {
        return backgroundTextureIdProvider;
    }

    @Override
    public void setTextureIdProvider(ProviderAtTime<Integer> backgroundTextureIdProvider)
            throws IllegalArgumentException {
        this.backgroundTextureIdProvider = Check.ifNull(backgroundTextureIdProvider,
                "backgroundTextureIdProvider");
    }

    @Override
    public ProviderAtTime<Float> getTextureTileWidthProvider() {
        return textureTileWidthProvider;
    }

    @Override
    public void setTextureTileWidthProvider(ProviderAtTime<Float> provider)
            throws IllegalArgumentException {
        textureTileWidthProvider = Check.ifNull(provider, "provider");
    }

    @Override
    public ProviderAtTime<Float> getTextureTileHeightProvider() {
        return textureTileHeightProvider;
    }

    @Override
    public void setTextureTileHeightProvider(ProviderAtTime<Float> provider)
            throws IllegalArgumentException {
        textureTileHeightProvider = Check.ifNull(provider, "provider");
    }
}
