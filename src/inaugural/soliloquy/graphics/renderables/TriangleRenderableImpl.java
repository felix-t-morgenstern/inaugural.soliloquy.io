package inaugural.soliloquy.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.infrastructure.Pair;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.TriangleRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;

import java.awt.*;
import java.util.Map;
import java.util.function.Consumer;

public class TriangleRenderableImpl
        extends AbstractPolygonRenderable
        implements TriangleRenderable {
    private ProviderAtTime<Pair<Float, Float>> _vertex1LocationProvider;
    private ProviderAtTime<Color> _vertex1ColorProvider;
    private ProviderAtTime<Pair<Float, Float>> _vertex2LocationProvider;
    private ProviderAtTime<Color> _vertex2ColorProvider;
    private ProviderAtTime<Pair<Float, Float>> _vertex3LocationProvider;
    private ProviderAtTime<Color> _vertex3ColorProvider;

    public TriangleRenderableImpl(ProviderAtTime<Pair<Float, Float>> vertex1LocationProvider,
                                  ProviderAtTime<Color> vertex1ColorProvider,
                                  ProviderAtTime<Pair<Float, Float>> vertex2LocationProvider,
                                  ProviderAtTime<Color> vertex2ColorProvider,
                                  ProviderAtTime<Pair<Float, Float>> vertex3LocationProvider,
                                  ProviderAtTime<Color> vertex3ColorProvider,
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
                                  Consumer<Renderable> removeFromContainer) {
        super(backgroundTextureIdProvider, backgroundTextureTileWidth, backgroundTextureTileHeight,
                onPress, onRelease, onMouseOver, onMouseLeave,
                z, uuid,
                updateZIndexInContainer, removeFromContainer);
        setVertex1LocationProvider(vertex1LocationProvider);
        setVertex1ColorProvider(vertex1ColorProvider);
        setVertex2LocationProvider(vertex2LocationProvider);
        setVertex2ColorProvider(vertex2ColorProvider);
        setVertex3LocationProvider(vertex3LocationProvider);
        setVertex3ColorProvider(vertex3ColorProvider);
    }

    @Override
    protected boolean underlyingAssetSupportsMouseEvents() {
        return true;
    }

    @Override
    protected String className() {
        return TriangleRenderableImpl.class.getName();
    }

    @Override
    public ProviderAtTime<Pair<Float, Float>> getVertex1LocationProvider() {
        return _vertex1LocationProvider;
    }

    @Override
    public void setVertex1LocationProvider(ProviderAtTime<Pair<Float, Float>> provider)
            throws IllegalArgumentException {
        _vertex1LocationProvider = Check.ifNull(provider, "provider");
    }

    @Override
    public ProviderAtTime<Color> getVertex1ColorProvider() {
        return _vertex1ColorProvider;
    }

    @Override
    public void setVertex1ColorProvider(ProviderAtTime<Color> provider)
            throws IllegalArgumentException {
        _vertex1ColorProvider = Check.ifNull(provider, "provider");
    }

    @Override
    public ProviderAtTime<Pair<Float, Float>> getVertex2LocationProvider() {
        return _vertex2LocationProvider;
    }

    @Override
    public void setVertex2LocationProvider(ProviderAtTime<Pair<Float, Float>> provider)
            throws IllegalArgumentException {
        _vertex2LocationProvider = Check.ifNull(provider, "provider");
    }

    @Override
    public ProviderAtTime<Color> getVertex2ColorProvider() {
        return _vertex2ColorProvider;
    }

    @Override
    public void setVertex2ColorProvider(ProviderAtTime<Color> provider)
            throws IllegalArgumentException {
        _vertex2ColorProvider = Check.ifNull(provider, "provider");
    }

    @Override
    public ProviderAtTime<Pair<Float, Float>> getVertex3LocationProvider() {
        return _vertex3LocationProvider;
    }

    @Override
    public void setVertex3LocationProvider(ProviderAtTime<Pair<Float, Float>> provider)
            throws IllegalArgumentException {
        _vertex3LocationProvider = Check.ifNull(provider, "provider");
    }

    @Override
    public ProviderAtTime<Color> getVertex3ColorProvider() {
        return _vertex3ColorProvider;
    }

    @Override
    public void setVertex3ColorProvider(ProviderAtTime<Color> provider)
            throws IllegalArgumentException {
        _vertex3ColorProvider = Check.ifNull(provider, "provider");
    }

    /*
     * Taken from https://www.geeksforgeeks.org/check-whether-a-given-point-lies-inside-a-triangle-or-not/ on 2022/08/11
     */
    @Override
    public boolean capturesMouseEventAtPoint(float x, float y, long timestamp)
            throws UnsupportedOperationException, IllegalArgumentException {
        TIMESTAMP_VALIDATOR.validateTimestamp(timestamp);

        Pair<Float, Float> point = new Pair<>(x, y);
        Pair<Float, Float> v1 = _vertex1LocationProvider.provide(timestamp);
        Pair<Float, Float> v2 = _vertex2LocationProvider.provide(timestamp);
        Pair<Float, Float> v3 = _vertex3LocationProvider.provide(timestamp);

        float renderableArea = area(v1, v2, v3);

        float area1 = area(point, v1, v2);
        float area2 = area(point, v2, v3);
        float area3 = area(point, v3, v1);

        return renderableArea == (area1 + area2 + area3);
    }

    private float area(Pair<Float, Float> v1, Pair<Float, Float> v2, Pair<Float, Float> v3) {
        return Math.abs(((v1.getItem1() * (v2.getItem2() - v3.getItem2())) +
                (v2.getItem1() * (v3.getItem2() - v1.getItem2())) +
                (v3.getItem1() * (v1.getItem2() - v2.getItem2())))
                / 2.0f);
    }

    @Override
    public String getInterfaceName() {
        return TriangleRenderable.class.getCanonicalName();
    }
}
