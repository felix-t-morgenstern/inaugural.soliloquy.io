package inaugural.soliloquy.graphics.renderables.factories;

import inaugural.soliloquy.graphics.renderables.TriangleRenderableImpl;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.TriangleRenderable;
import soliloquy.specs.graphics.renderables.factories.TriangleRenderableFactory;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;

import java.awt.*;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class TriangleRenderableFactoryImpl implements TriangleRenderableFactory {
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
                                   Map<Integer, Action<Long>> onPress,
                                   Map<Integer, Action<Long>> onRelease,
                                   Action<Long> onMouseOver,
                                   Action<Long> onMouseLeave,
                                   int z,
                                   UUID uuid,
                                   Consumer<Renderable> updateZIndexInContainer,
                                   Consumer<Renderable> removeFromContainer)
            throws IllegalArgumentException {
        return new TriangleRenderableImpl(vertex1Provider, vertex1ColorProvider,
                vertex2Provider, vertex2ColorProvider, vertex3Provider,
                vertex3ColorProvider, backgroundTextureIdProvider, backgroundTextureTileWidth,
                backgroundTextureTileHeight, onPress, onRelease, onMouseOver, onMouseLeave, z, uuid,
                updateZIndexInContainer, removeFromContainer);
    }

    @Override
    public String getInterfaceName() {
        return TriangleRenderableFactory.class.getCanonicalName();
    }
}
