package inaugural.soliloquy.io.graphics.renderables.factories;

import inaugural.soliloquy.io.graphics.renderables.TriangleRenderableImpl;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.RenderableWithMouseEvents.MouseEventInputs;
import soliloquy.specs.io.graphics.renderables.TriangleRenderable;
import soliloquy.specs.io.graphics.renderables.factories.TriangleRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderableStack;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;

import java.awt.*;
import java.util.Map;
import java.util.UUID;

public class TriangleRenderableFactoryImpl implements TriangleRenderableFactory {
    private final RenderingBoundaries RENDERING_BOUNDARIES;

    public TriangleRenderableFactoryImpl(RenderingBoundaries renderingBoundaries) {
        RENDERING_BOUNDARIES = Check.ifNull(renderingBoundaries, "renderingBoundaries");
    }

    @Override
    public TriangleRenderable make(ProviderAtTime<Vertex> vertex1Provider,
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
                                   UUID uuid,
                                   RenderableStack containingStack)
            throws IllegalArgumentException {
        return new TriangleRenderableImpl(vertex1Provider, vertex1ColorProvider,
                vertex2Provider, vertex2ColorProvider, vertex3Provider,
                vertex3ColorProvider, backgroundTextureIdProvider, backgroundTextureTileWidth,
                backgroundTextureTileHeight, onPress, onRelease, onMouseOver, onMouseLeave, z, uuid,
                containingStack, RENDERING_BOUNDARIES);
    }
}
