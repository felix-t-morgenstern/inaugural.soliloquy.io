package inaugural.soliloquy.io.graphics.renderables.factories;

import inaugural.soliloquy.io.graphics.renderables.RasterizedLineSegmentRenderableImpl;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.RasterizedLineSegmentRenderable;
import soliloquy.specs.io.graphics.renderables.factories.RasterizedLineSegmentRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderableStack;

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
}
