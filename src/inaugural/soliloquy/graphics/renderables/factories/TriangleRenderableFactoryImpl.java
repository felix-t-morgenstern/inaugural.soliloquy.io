package inaugural.soliloquy.graphics.renderables.factories;

import inaugural.soliloquy.graphics.renderables.TriangleRenderableImpl;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.infrastructure.Pair;
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
    public TriangleRenderable make(ProviderAtTime<Pair<Float, Float>> vertex1LocationProvider,
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
                                   UUID uuid,
                                   Consumer<Renderable> updateZIndexInContainer,
                                   Consumer<Renderable> removeFromContainer)
            throws IllegalArgumentException {
        return new TriangleRenderableImpl(vertex1LocationProvider, vertex1ColorProvider,
                vertex2LocationProvider, vertex2ColorProvider, vertex3LocationProvider,
                vertex3ColorProvider, backgroundTextureIdProvider, backgroundTextureTileWidth,
                backgroundTextureTileHeight, onPress, onRelease, onMouseOver, onMouseLeave, z, uuid,
                updateZIndexInContainer, removeFromContainer);
    }

    @Override
    public String getInterfaceName() {
        return TriangleRenderableFactory.class.getCanonicalName();
    }
}
