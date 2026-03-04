package inaugural.soliloquy.io.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.common.entities.Consumer;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.PolygonRenderable;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.ui.EventInputs;

import java.util.Map;

public abstract class AbstractPolygonRenderable
        extends AbstractRenderableWithMouseEvents
        implements PolygonRenderable {
    private ProviderAtTime<Integer> textureIdProvider;
    private ProviderAtTime<Float> textureTilesPerWidthProvider;
    private ProviderAtTime<Float> textureXOffsetProvider;
    private ProviderAtTime<Float> textureTilesPerHeightProvider;
    private ProviderAtTime<Float> textureYOffsetProvider;

    protected AbstractPolygonRenderable(
            ProviderAtTime<Integer> textureIdProvider,
            ProviderAtTime<Float> textureTilesPerWidthProvider,
            ProviderAtTime<Float> textureXOffsetProvider,
            ProviderAtTime<Float> textureTilesPerHeightProvider,
            ProviderAtTime<Float> textureYOffsetProvider,
            Map<Integer, Consumer<EventInputs>> onPress,
            Map<Integer, Consumer<EventInputs>> onRelease,
            Consumer<EventInputs> onMouseOver,
            Consumer<EventInputs> onMouseLeave,
            int z,
            java.util.UUID uuid,
            Component containingComponent,
            RenderingBoundaries renderingBoundaries,
            TimestampValidator timestampValidator
    ) {
        super(onPress, onRelease, onMouseOver, onMouseLeave, z, uuid, containingComponent,
                renderingBoundaries, timestampValidator);
        setTextureIdProvider(textureIdProvider);
        setTextureTilesPerWidthProvider(textureTilesPerWidthProvider);
        setTextureXOffsetProvider(textureXOffsetProvider);
        setTextureTilesPerHeightProvider(textureTilesPerHeightProvider);
        setTextureYOffsetProvider(textureYOffsetProvider);
    }

    @Override
    public ProviderAtTime<Integer> getTextureIdProvider() {
        return textureIdProvider;
    }

    @Override
    public void setTextureIdProvider(ProviderAtTime<Integer> backgroundTextureIdProvider)
            throws IllegalArgumentException {
        this.textureIdProvider = Check.ifNull(backgroundTextureIdProvider,
                "backgroundTextureIdProvider");
    }

    @Override
    public ProviderAtTime<Float> getTextureTilesPerWidthProvider() {
        return textureTilesPerWidthProvider;
    }

    @Override
    public void setTextureTilesPerWidthProvider(ProviderAtTime<Float> provider)
            throws IllegalArgumentException {
        textureTilesPerWidthProvider = Check.ifNull(provider, "provider");
    }

    @Override
    public ProviderAtTime<Float> getTextureXOffsetProvider() {
        return textureXOffsetProvider;
    }

    @Override
    public void setTextureXOffsetProvider(ProviderAtTime<Float> provider)
            throws IllegalArgumentException {
        textureXOffsetProvider = Check.ifNull(provider, "provider");
    }

    @Override
    public ProviderAtTime<Float> getTextureTilesPerHeightProvider() {
        return textureTilesPerHeightProvider;
    }

    @Override
    public void setTextureTilesPerHeightProvider(ProviderAtTime<Float> provider)
            throws IllegalArgumentException {
        textureTilesPerHeightProvider = Check.ifNull(provider, "provider");
    }

    @Override
    public ProviderAtTime<Float> getTextureYOffsetProvider() {
        return textureYOffsetProvider;
    }

    @Override
    public void setTextureYOffsetProvider(ProviderAtTime<Float> provider)
            throws IllegalArgumentException {
        textureYOffsetProvider = Check.ifNull(provider, "provider");
    }
}
