package inaugural.soliloquy.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.graphics.renderables.PolygonRenderable;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;

import java.util.Map;
import java.util.function.Consumer;

public abstract class AbstractPolygonRenderable
        extends AbstractRenderableWithMouseEvents
        implements PolygonRenderable {
    private ProviderAtTime<Integer> _backgroundTextureIdProvider;
    private float _backgroundTextureTileWidth;
    private float _backgroundTextureTileHeight;

    protected AbstractPolygonRenderable(
            ProviderAtTime<Integer> backgroundTextureIdProvider,
            float backgroundTextureTileWidth,
            float backgroundTextureTileHeight,
            Map<Integer, Action<Long>> onPress,
            Map<Integer, Action<Long>> onRelease,
            Action<Long> onMouseOver,
            Action<Long> onMouseLeave,
            int z,
            java.util.UUID uuid,
            Consumer<Renderable> updateZIndexInContainer,
            Consumer<Renderable> removeFromContainer
    ) {
        super(false, onPress, onRelease, onMouseOver, onMouseLeave, z, uuid,
                updateZIndexInContainer, removeFromContainer);
        setBackgroundTextureIdProvider(backgroundTextureIdProvider);
        setBackgroundTextureTileWidth(backgroundTextureTileWidth);
        setBackgroundTextureTileHeight(backgroundTextureTileHeight);
    }

    @Override
    public ProviderAtTime<Integer> getBackgroundTextureIdProvider() {
        return _backgroundTextureIdProvider;
    }

    @Override
    public void setBackgroundTextureIdProvider(ProviderAtTime<Integer> backgroundTextureIdProvider)
            throws IllegalArgumentException {
        _backgroundTextureIdProvider = Check.ifNull(backgroundTextureIdProvider,
                "backgroundTextureIdProvider");
    }

    @Override
    public float getBackgroundTextureTileWidth() {
        return _backgroundTextureTileWidth;
    }

    @Override
    public void setBackgroundTextureTileWidth(float backgroundTextureTileWidth)
            throws IllegalArgumentException {
        _backgroundTextureTileWidth = Check.throwOnLteZero(backgroundTextureTileWidth,
                "backgroundTextureTileWidth");
    }

    @Override
    public float getBackgroundTextureTileHeight() {
        return _backgroundTextureTileHeight;
    }

    @Override
    public void setBackgroundTextureTileHeight(float backgroundTextureTileHeight)
            throws IllegalArgumentException {
        _backgroundTextureTileHeight = Check.throwOnLteZero(backgroundTextureTileHeight,
                "backgroundTextureTileHeight");
    }
}
