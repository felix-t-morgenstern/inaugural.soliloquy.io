package inaugural.soliloquy.io.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.TriangleRenderable;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderableStack;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;

import java.awt.*;
import java.util.Map;

public class TriangleRenderableImpl
        extends AbstractPolygonRenderable
        implements TriangleRenderable {
    private ProviderAtTime<Vertex> vertex1Provider;
    private ProviderAtTime<Color> vertex1ColorProvider;
    private ProviderAtTime<Vertex> vertex2Provider;
    private ProviderAtTime<Color> vertex2ColorProvider;
    private ProviderAtTime<Vertex> vertex3Provider;
    private ProviderAtTime<Color> vertex3ColorProvider;

    public TriangleRenderableImpl(ProviderAtTime<Vertex> vertex1Provider,
                                  ProviderAtTime<Color> vertex1ColorProvider,
                                  ProviderAtTime<Vertex> vertex2Provider,
                                  ProviderAtTime<Color> vertex2ColorProvider,
                                  ProviderAtTime<Vertex> vertex3Provider,
                                  ProviderAtTime<Color> vertex3ColorProvider,
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
                                  RenderingBoundaries renderingBoundaries) {
        super(backgroundTextureIdProvider, backgroundTextureTileWidth, backgroundTextureTileHeight,
                onPress, onRelease, onMouseOver, onMouseLeave, z, uuid, containingStack,
                renderingBoundaries);
        setVertex1Provider(vertex1Provider);
        setVertex1ColorProvider(vertex1ColorProvider);
        setVertex2Provider(vertex2Provider);
        setVertex2ColorProvider(vertex2ColorProvider);
        setVertex3Provider(vertex3Provider);
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
    public ProviderAtTime<Vertex> getVertex1Provider() {
        return vertex1Provider;
    }

    @Override
    public void setVertex1Provider(ProviderAtTime<Vertex> provider)
            throws IllegalArgumentException {
        vertex1Provider = Check.ifNull(provider, "provider");
    }

    @Override
    public ProviderAtTime<Color> getVertex1ColorProvider() {
        return vertex1ColorProvider;
    }

    @Override
    public void setVertex1ColorProvider(ProviderAtTime<Color> provider)
            throws IllegalArgumentException {
        vertex1ColorProvider = Check.ifNull(provider, "provider");
    }

    @Override
    public ProviderAtTime<Vertex> getVertex2Provider() {
        return vertex2Provider;
    }

    @Override
    public void setVertex2Provider(ProviderAtTime<Vertex> provider)
            throws IllegalArgumentException {
        vertex2Provider = Check.ifNull(provider, "provider");
    }

    @Override
    public ProviderAtTime<Color> getVertex2ColorProvider() {
        return vertex2ColorProvider;
    }

    @Override
    public void setVertex2ColorProvider(ProviderAtTime<Color> provider)
            throws IllegalArgumentException {
        vertex2ColorProvider = Check.ifNull(provider, "provider");
    }

    @Override
    public ProviderAtTime<Vertex> getVertex3Provider() {
        return vertex3Provider;
    }

    @Override
    public void setVertex3Provider(ProviderAtTime<Vertex> provider)
            throws IllegalArgumentException {
        vertex3Provider = Check.ifNull(provider, "provider");
    }

    @Override
    public ProviderAtTime<Color> getVertex3ColorProvider() {
        return vertex3ColorProvider;
    }

    @Override
    public void setVertex3ColorProvider(ProviderAtTime<Color> provider)
            throws IllegalArgumentException {
        vertex3ColorProvider = Check.ifNull(provider, "provider");
    }

    /*
     * Taken from https://www.geeksforgeeks
     * .org/check-whether-a-given-point-lies-inside-a-triangle-or-not/ on 2022/08/11
     */
    @Override
    public boolean capturesMouseEventAtPoint(Vertex point, long timestamp)
            throws UnsupportedOperationException, IllegalArgumentException {
        TIMESTAMP_VALIDATOR.validateTimestamp(timestamp);

        Vertex v1 = vertex1Provider.provide(timestamp);
        Vertex v2 = vertex2Provider.provide(timestamp);
        Vertex v3 = vertex3Provider.provide(timestamp);

        float renderableArea = area(v1, v2, v3);

        float area1 = area(point, v1, v2);
        float area2 = area(point, v2, v3);
        float area3 = area(point, v3, v1);

        return renderableArea == (area1 + area2 + area3);
    }

    private float area(Vertex v1, Vertex v2, Vertex v3) {
        return Math.abs(
                ((v1.X * (v2.Y - v3.Y)) + (v2.X * (v3.Y - v1.Y)) + (v3.X * (v1.Y - v2.Y))) / 2.0f);
    }
}
