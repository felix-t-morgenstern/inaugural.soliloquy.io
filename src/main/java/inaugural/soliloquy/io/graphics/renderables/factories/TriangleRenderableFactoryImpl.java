package inaugural.soliloquy.io.graphics.renderables.factories;

import inaugural.soliloquy.io.graphics.renderables.TriangleRenderableImpl;
import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.common.entities.Consumer;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.TriangleRenderable;
import soliloquy.specs.io.graphics.renderables.factories.TriangleRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.ui.EventInputs;

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
                                   UUID uuid,
                                   Component component)
            throws IllegalArgumentException {
        return new TriangleRenderableImpl(vertex1Provider, vertex1ColorProvider, vertex2Provider,
                vertex2ColorProvider, vertex3Provider, vertex3ColorProvider, textureIdProvider,
                textureTilesPerWidthProvider, textureXOffsetProvider, textureTilesPerHeightProvider,
                textureYOffsetProvider, onPress, onRelease, onMouseOver, onMouseLeave, z, uuid,
                component, RENDERING_BOUNDARIES, TIMESTAMP_VALIDATOR);
    }
}
