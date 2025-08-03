package inaugural.soliloquy.io.graphics.renderables.factories;

import inaugural.soliloquy.io.graphics.renderables.TriangleRenderableImpl;
import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.TriangleRenderable;
import soliloquy.specs.io.graphics.renderables.factories.TriangleRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.ui.EventInputs;
import soliloquy.specs.ui.Component;

import java.awt.*;
import java.util.Map;
import java.util.UUID;

public class TriangleRenderableFactoryImpl implements TriangleRenderableFactory {
    private final RenderingBoundaries RENDERING_BOUNDARIES;
    private final TimestampValidator TIMESTAMP_VALIDATOR;

    public TriangleRenderableFactoryImpl(RenderingBoundaries renderingBoundaries,
                                         TimestampValidator timestampValidator) {
        RENDERING_BOUNDARIES = Check.ifNull(renderingBoundaries, "renderingBoundaries");
        TIMESTAMP_VALIDATOR = Check.ifNull(timestampValidator, "timestampValidator");
    }

    @Override
    public TriangleRenderable make(ProviderAtTime<Vertex> vertex1Provider,
                                   ProviderAtTime<Color> vertex1ColorProvider,
                                   ProviderAtTime<Vertex> vertex2Provider,
                                   ProviderAtTime<Color> vertex2ColorProvider,
                                   ProviderAtTime<Vertex> vertex3Provider,
                                   ProviderAtTime<Color> vertex3ColorProvider,
                                   ProviderAtTime<Integer> backgroundTextureIdProvider,
                                   ProviderAtTime<Float> textureTileWidthProvider,
                                   ProviderAtTime<Float> textureTileHeightProvider,
                                   Map<Integer, Action<EventInputs>> onPress,
                                   Map<Integer, Action<EventInputs>> onRelease,
                                   Action<EventInputs> onMouseOver,
                                   Action<EventInputs> onMouseLeave,
                                   int z,
                                   UUID uuid,
                                   Component component)
            throws IllegalArgumentException {
        return new TriangleRenderableImpl(vertex1Provider, vertex1ColorProvider,
                vertex2Provider, vertex2ColorProvider, vertex3Provider,
                vertex3ColorProvider, backgroundTextureIdProvider, textureTileWidthProvider,
                textureTileHeightProvider, onPress, onRelease, onMouseOver, onMouseLeave, z, uuid,
                component, RENDERING_BOUNDARIES, TIMESTAMP_VALIDATOR);
    }
}
