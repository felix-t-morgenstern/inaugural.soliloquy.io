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
    private float textureTileWidth;
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
        setTextureIdProvider(backgroundTextureIdProvider);
        setTextureTileWidth(backgroundTextureTileWidth);
        setTextureTileHeight(backgroundTextureTileHeight);
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
    public float getTextureTileWidth() {
        return textureTileWidth;
    }

    @Override
    public void setTextureTileWidth(float backgroundTextureTileWidth)
            throws IllegalArgumentException {
        this.textureTileWidth = Check.throwOnLteZero(backgroundTextureTileWidth,
                "backgroundTextureTileWidth");
    }

    @Override
    public float getTextureTileHeight() {
        return backgroundTextureTileHeight;
    }

    @Override
    public void setTextureTileHeight(float backgroundTextureTileHeight)
            throws IllegalArgumentException {
        this.backgroundTextureTileHeight = Check.throwOnLteZero(backgroundTextureTileHeight,
                "backgroundTextureTileHeight");
    }
}
