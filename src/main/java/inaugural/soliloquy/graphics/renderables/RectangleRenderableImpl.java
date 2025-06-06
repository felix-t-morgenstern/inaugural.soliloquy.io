package inaugural.soliloquy.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.renderables.RectangleRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.RenderableStack;
import soliloquy.specs.graphics.rendering.RenderingBoundaries;

import java.awt.*;
import java.util.Map;
import java.util.UUID;

public class RectangleRenderableImpl
        extends AbstractPolygonRenderable
        implements RectangleRenderable {
    private ProviderAtTime<Color> topLeftColorProvider;
    private ProviderAtTime<Color> topRightColorProvider;
    private ProviderAtTime<Color> bottomRightColorProvider;
    private ProviderAtTime<Color> bottomLeftColorProvider;
    protected ProviderAtTime<FloatBox> renderingDimensionsProvider;

    public RectangleRenderableImpl(ProviderAtTime<Color> topLeftColorProvider,
                                   ProviderAtTime<Color> topRightColorProvider,
                                   ProviderAtTime<Color> bottomRightColorProvider,
                                   ProviderAtTime<Color> bottomLeftColorProvider,
                                   ProviderAtTime<Integer> backgroundTextureIdProvider,
                                   float backgroundTextureTileWidth,
                                   float backgroundTextureTileHeight,
                                   Map<Integer, Action<MouseEventInputs>> onPress,
                                   Map<Integer, Action<MouseEventInputs>> onRelease,
                                   Action<MouseEventInputs> onMouseOver,
                                   Action<MouseEventInputs> onMouseLeave,
                                   ProviderAtTime<FloatBox> renderingDimensionsProvider,
                                   int z,
                                   UUID uuid,
                                   RenderableStack containingStack,
                                   RenderingBoundaries renderingBoundaries) {
        super(backgroundTextureIdProvider, backgroundTextureTileWidth, backgroundTextureTileHeight,
                onPress, onRelease, onMouseOver, onMouseLeave, z, uuid, containingStack,
                renderingBoundaries);
        setTopLeftColorProvider(topLeftColorProvider);
        setTopRightColorProvider(topRightColorProvider);
        setBottomRightColorProvider(bottomRightColorProvider);
        setBottomLeftColorProvider(bottomLeftColorProvider);
        setRenderingDimensionsProvider(renderingDimensionsProvider);
    }

    @Override
    protected boolean underlyingAssetSupportsMouseEvents() {
        return true;
    }

    @Override
    protected String className() {
        return RectangleRenderableImpl.class.getCanonicalName();
    }

    @Override
    public ProviderAtTime<Color> getTopLeftColorProvider() {
        return topLeftColorProvider;
    }

    @Override
    public void setTopLeftColorProvider(ProviderAtTime<Color> topLeftColorProvider)
            throws IllegalArgumentException {
        this.topLeftColorProvider = Check.ifNull(topLeftColorProvider, "topLeftColorProvider");
    }

    @Override
    public ProviderAtTime<Color> getTopRightColorProvider() {
        return topRightColorProvider;
    }

    @Override
    public void setTopRightColorProvider(ProviderAtTime<Color> topRightColorProvider)
            throws IllegalArgumentException {
        this.topRightColorProvider = Check.ifNull(topRightColorProvider, "topRightColorProvider");
    }

    @Override
    public ProviderAtTime<Color> getBottomRightColorProvider() {
        return bottomRightColorProvider;
    }

    @Override
    public void setBottomRightColorProvider(ProviderAtTime<Color> bottomRightColorProvider)
            throws IllegalArgumentException {
        this.bottomRightColorProvider = Check.ifNull(bottomRightColorProvider,
                "bottomRightColorProvider");
    }

    @Override
    public ProviderAtTime<Color> getBottomLeftColorProvider() {
        return bottomLeftColorProvider;
    }

    @Override
    public void setBottomLeftColorProvider(ProviderAtTime<Color> bottomLeftColorProvider)
            throws IllegalArgumentException {
        this.bottomLeftColorProvider = Check.ifNull(bottomLeftColorProvider,
                "bottomLeftColorProvider");
    }

    @Override
    public boolean capturesMouseEventAtPoint(Vertex point, long timestamp)
            throws UnsupportedOperationException, IllegalArgumentException {
        TIMESTAMP_VALIDATOR.validateTimestamp(timestamp);

        if (point.X < 0f || point.X > 1f || point.Y < 0f || point.Y > 1f) {
            throw new IllegalArgumentException(
                    "RectangleRenderableImpl.capturesMouseEventAtPoint: cannot check for mouse " +
                            "event capture at point beyond screen (" +
                            point.X + "," + point.Y + ")");
        }

        if (!capturesMouseEvents) {
            return false;
        }

        FloatBox renderingBoundaries = RENDERING_BOUNDARIES.currentBoundaries();
        if (point.X < renderingBoundaries.LEFT_X || point.X > renderingBoundaries.RIGHT_X ||
                point.Y < renderingBoundaries.TOP_Y || point.Y > renderingBoundaries.BOTTOM_Y) {
            return false;
        }

        FloatBox renderingDimensions = renderingDimensionsProvider.provide(timestamp);
        //noinspection RedundantIfStatement
        if (point.X < renderingDimensions.LEFT_X || point.X > renderingDimensions.RIGHT_X ||
                point.Y < renderingDimensions.TOP_Y || point.Y > renderingDimensions.BOTTOM_Y) {
            return false;
        }

        return true;
    }

    @Override
    public ProviderAtTime<FloatBox> getRenderingDimensionsProvider() {
        return renderingDimensionsProvider;
    }

    @Override
    public void setRenderingDimensionsProvider(ProviderAtTime<FloatBox>
                                                       renderingDimensionsProvider)
            throws IllegalArgumentException {
        this.renderingDimensionsProvider = Check.ifNull(renderingDimensionsProvider,
                "renderingDimensionsProvider");
    }

}
