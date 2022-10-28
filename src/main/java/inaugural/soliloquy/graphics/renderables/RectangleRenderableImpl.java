package inaugural.soliloquy.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.renderables.RectangleRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.RenderableStack;
import soliloquy.specs.graphics.rendering.RenderingBoundaries;

import java.awt.*;
import java.util.Map;
import java.util.UUID;

public class RectangleRenderableImpl
        extends AbstractPolygonRenderable
        implements RectangleRenderable {
    private ProviderAtTime<Color> _topLeftColorProvider;
    private ProviderAtTime<Color> _topRightColorProvider;
    private ProviderAtTime<Color> _bottomRightColorProvider;
    private ProviderAtTime<Color> _bottomLeftColorProvider;
    protected ProviderAtTime<FloatBox> _renderingDimensionsProvider;

    public RectangleRenderableImpl(ProviderAtTime<Color> topLeftColorProvider,
                                   ProviderAtTime<Color> topRightColorProvider,
                                   ProviderAtTime<Color> bottomRightColorProvider,
                                   ProviderAtTime<Color> bottomLeftColorProvider,
                                   ProviderAtTime<Integer> backgroundTextureIdProvider,
                                   float backgroundTextureTileWidth,
                                   float backgroundTextureTileHeight,
                                   Map<Integer, Action<Long>> onPress,
                                   Map<Integer, Action<Long>> onRelease,
                                   Action<Long> onMouseOver,
                                   Action<Long> onMouseLeave,
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
        return _topLeftColorProvider;
    }

    @Override
    public void setTopLeftColorProvider(ProviderAtTime<Color> topLeftColorProvider)
            throws IllegalArgumentException {
        _topLeftColorProvider = Check.ifNull(topLeftColorProvider, "topLeftColorProvider");
    }

    @Override
    public ProviderAtTime<Color> getTopRightColorProvider() {
        return _topRightColorProvider;
    }

    @Override
    public void setTopRightColorProvider(ProviderAtTime<Color> topRightColorProvider)
            throws IllegalArgumentException {
        _topRightColorProvider = Check.ifNull(topRightColorProvider, "topRightColorProvider");
    }

    @Override
    public ProviderAtTime<Color> getBottomRightColorProvider() {
        return _bottomRightColorProvider;
    }

    @Override
    public void setBottomRightColorProvider(ProviderAtTime<Color> bottomRightColorProvider)
            throws IllegalArgumentException {
        _bottomRightColorProvider = Check.ifNull(bottomRightColorProvider,
                "bottomRightColorProvider");
    }

    @Override
    public ProviderAtTime<Color> getBottomLeftColorProvider() {
        return _bottomLeftColorProvider;
    }

    @Override
    public void setBottomLeftColorProvider(ProviderAtTime<Color> bottomLeftColorProvider)
            throws IllegalArgumentException {
        _bottomLeftColorProvider = Check.ifNull(bottomLeftColorProvider,
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

        if (!_capturesMouseEvents) {
            return false;
        }

        FloatBox renderingBoundaries = RENDERING_BOUNDARIES.currentBoundaries();
        if (point.X < renderingBoundaries.leftX() || point.X > renderingBoundaries.rightX() ||
                point.Y < renderingBoundaries.topY() || point.Y > renderingBoundaries.bottomY()) {
            return false;
        }

        FloatBox renderingDimensions = _renderingDimensionsProvider.provide(timestamp);
        //noinspection RedundantIfStatement
        if (point.X < renderingDimensions.leftX() || point.X > renderingDimensions.rightX() ||
                point.Y < renderingDimensions.topY() || point.Y > renderingDimensions.bottomY()) {
            return false;
        }

        return true;
    }

    @Override
    public String getInterfaceName() {
        return RectangleRenderable.class.getCanonicalName();
    }

    @Override
    public ProviderAtTime<FloatBox> getRenderingDimensionsProvider() {
        return _renderingDimensionsProvider;
    }

    @Override
    public void setRenderingDimensionsProvider(ProviderAtTime<FloatBox>
                                                       renderingDimensionsProvider)
            throws IllegalArgumentException {
        _renderingDimensionsProvider = Check.ifNull(renderingDimensionsProvider,
                "renderingDimensionsProvider");
    }

}
