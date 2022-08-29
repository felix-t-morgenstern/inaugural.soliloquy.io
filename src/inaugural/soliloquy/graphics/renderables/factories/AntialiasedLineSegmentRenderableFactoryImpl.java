package inaugural.soliloquy.graphics.renderables.factories;

import inaugural.soliloquy.graphics.renderables.AntialiasedLineSegmentRenderableImpl;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.renderables.AntialiasedLineSegmentRenderable;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.factories.AntialiasedLineSegmentRenderableFactory;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;

import java.awt.*;
import java.util.function.Consumer;

public class AntialiasedLineSegmentRenderableFactoryImpl
        implements AntialiasedLineSegmentRenderableFactory {
    private final Consumer<Renderable> UPDATE_Z_INDEX_IN_CONTAINER;
    private final Consumer<Renderable> REMOVE_FROM_CONTAINER;

    public AntialiasedLineSegmentRenderableFactoryImpl(
            Consumer<Renderable> updateZIndexInContainer,
            Consumer<Renderable> removeFromContainer) {
        UPDATE_Z_INDEX_IN_CONTAINER =
                Check.ifNull(updateZIndexInContainer, "updateZIndexInContainer");
        REMOVE_FROM_CONTAINER = Check.ifNull(removeFromContainer, "removeFromContainer");
    }

    @Override
    public AntialiasedLineSegmentRenderable make(ProviderAtTime<Vertex> vertex1Provider,
                                                 ProviderAtTime<Vertex> vertex2Provider,
                                                 ProviderAtTime<Float> thicknessProvider,
                                                 ProviderAtTime<Color> colorProvider,
                                                 ProviderAtTime<Float>
                                                         thicknessGradientPercentProvider,
                                                 ProviderAtTime<Float>
                                                         lengthGradientPercentProvider,
                                                 int z,
                                                 java.util.UUID uuid)
            throws IllegalArgumentException {
        return new AntialiasedLineSegmentRenderableImpl(vertex1Provider, vertex2Provider,
                thicknessProvider, colorProvider, thicknessGradientPercentProvider,
                lengthGradientPercentProvider, z, uuid, UPDATE_Z_INDEX_IN_CONTAINER,
                REMOVE_FROM_CONTAINER);
    }

    @Override
    public String getInterfaceName() {
        return AntialiasedLineSegmentRenderableFactory.class.getCanonicalName();
    }
}
