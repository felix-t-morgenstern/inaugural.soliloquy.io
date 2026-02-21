package inaugural.soliloquy.io.graphics.renderables;

import com.google.common.primitives.Floats;
import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.common.entities.Consumer;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.TriangleRenderable;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.ui.EventInputs;

import java.awt.*;
import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.valueobjects.Vertex.pointIsInTriangle;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;

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
                                  ProviderAtTime<Float> textureTileWidthProvider,
                                  ProviderAtTime<Float> textureTileHeightProvider,
                                  Map<Integer, Consumer<EventInputs>> onPress,
                                  Map<Integer, Consumer<EventInputs>> onRelease,
                                  Consumer<EventInputs> onMouseOver,
                                  Consumer<EventInputs> onMouseLeave,
                                  int z,
                                  java.util.UUID uuid,
                                  Component component,
                                  RenderingBoundaries renderingBoundaries,
                                  TimestampValidator timestampValidator) {
        super(backgroundTextureIdProvider, textureTileWidthProvider, textureTileHeightProvider,
                onPress, onRelease, onMouseOver, onMouseLeave, z, uuid, component,
                renderingBoundaries, timestampValidator);
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
     * Taken from
     * https://www.geeksforgeeks.org/check-whether-a-given-point-lies-inside-a-triangle-or-not/
     * on 2022/08/11
     */
    @Override
    public boolean capturesMouseEventAtPoint(Vertex point, long timestamp)
            throws UnsupportedOperationException, IllegalArgumentException {
        TIMESTAMP_VALIDATOR.validateTimestamp(timestamp);

        return pointIsInTriangle(
                point,
                vertex1Provider.provide(timestamp),
                vertex2Provider.provide(timestamp),
                vertex3Provider.provide(timestamp)
        );
    }

    @Override
    public ProviderAtTime<FloatBox> getRenderingDimensionsProvider() {
        return DIMENS_PROVIDER;
    }

    private final ProviderAtTime<FloatBox> DIMENS_PROVIDER = new ProviderAtTime<>() {
        @Override
        public FloatBox provide(long timestamp) throws IllegalArgumentException {
            var vertex1 = getVertex1Provider().provide(timestamp);
            var vertex2 = getVertex2Provider().provide(timestamp);
            var vertex3 = getVertex3Provider().provide(timestamp);

            var leftX = Floats.min(vertex1.X, vertex2.X, vertex3.X);
            var topY = Floats.min(vertex1.Y, vertex2.Y, vertex3.Y);
            var rightX = Floats.max(vertex1.X, vertex2.X, vertex3.X);
            var bottomY = Floats.max(vertex1.Y, vertex2.Y, vertex3.Y);

            return floatBoxOf(leftX, topY, rightX, bottomY);
        }

        @Override
        public Object representation() {
            throw new UnsupportedOperationException();
        }

        @Override
        public UUID uuid() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void reportPause(long l)
                throws IllegalArgumentException, UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void reportUnpause(long l)
                throws IllegalArgumentException, UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Long pausedTimestamp() {
            throw new UnsupportedOperationException();
        }
    };
}
