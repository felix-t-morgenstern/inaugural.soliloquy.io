package inaugural.soliloquy.graphics.renderables.factories;

import inaugural.soliloquy.graphics.renderables.RasterizedLineSegmentRenderableImpl;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.renderables.RasterizedLineSegmentRenderable;
import soliloquy.specs.graphics.renderables.factories.RasterizedLineSegmentRenderableFactory;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.RenderableStack;

import java.awt.*;
import java.util.UUID;

public class RasterizedLineSegmentRenderableFactoryImpl
        implements RasterizedLineSegmentRenderableFactory {
    @Override
    public RasterizedLineSegmentRenderable make(ProviderAtTime<Vertex> vertex1Provider,
                                                ProviderAtTime<Vertex> vertex2Provider,
                                                ProviderAtTime<Float> thicknessProvider,
                                                short stipplePattern,
                                                short stippleFactor,
                                                ProviderAtTime<Color> colorProvider,
                                                int z,
                                                UUID uuid,
                                                RenderableStack containingStack)
            throws IllegalArgumentException {
        return new RasterizedLineSegmentRenderableImpl(vertex1Provider, vertex2Provider,
                thicknessProvider, stipplePattern, stippleFactor, colorProvider, z, uuid,
                containingStack);
    }

    @Override
    public String getInterfaceName() {
        return RasterizedLineSegmentRenderableFactory.class.getCanonicalName();
    }
}
