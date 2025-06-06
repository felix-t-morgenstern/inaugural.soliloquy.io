package inaugural.soliloquy.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.graphics.renderables.PolygonRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.RenderableStack;
import soliloquy.specs.graphics.rendering.RenderingBoundaries;

import java.util.Map;

public abstract class AbstractPolygonRenderable
        extends AbstractRenderableWithMouseEvents
        implements PolygonRenderable {
    private ProviderAtTime<Integer> backgroundTextureIdProvider;
    private float backgroundTextureTileWidth;
    private float backgroundTextureTileHeight;

    protected AbstractPolygonRenderable(
            ProviderAtTime<Integer> backgroundTextureIdProvider,
            float backgroundTextureTileWidth,
            float backgroundTextureTileHeight,
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
        setBackgroundTextureIdProvider(backgroundTextureIdProvider);
        setBackgroundTextureTileWidth(backgroundTextureTileWidth);
        setBackgroundTextureTileHeight(backgroundTextureTileHeight);
    }

    @Override
    public ProviderAtTime<Integer> getBackgroundTextureIdProvider() {
        return backgroundTextureIdProvider;
    }

    @Override
    public void setBackgroundTextureIdProvider(ProviderAtTime<Integer> backgroundTextureIdProvider)
            throws IllegalArgumentException {
        this.backgroundTextureIdProvider = Check.ifNull(backgroundTextureIdProvider,
                "backgroundTextureIdProvider");
    }

    @Override
    public float getBackgroundTextureTileWidth() {
        return backgroundTextureTileWidth;
    }

    @Override
    public void setBackgroundTextureTileWidth(float backgroundTextureTileWidth)
            throws IllegalArgumentException {
        this.backgroundTextureTileWidth = Check.throwOnLteZero(backgroundTextureTileWidth,
                "backgroundTextureTileWidth");
    }

    @Override
    public float getBackgroundTextureTileHeight() {
        return backgroundTextureTileHeight;
    }

    @Override
    public void setBackgroundTextureTileHeight(float backgroundTextureTileHeight)
            throws IllegalArgumentException {
        this.backgroundTextureTileHeight = Check.throwOnLteZero(backgroundTextureTileHeight,
                "backgroundTextureTileHeight");
    }
}
