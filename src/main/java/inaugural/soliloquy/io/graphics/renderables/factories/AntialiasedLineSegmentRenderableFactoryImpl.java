package inaugural.soliloquy.io.graphics.renderables.factories;

import inaugural.soliloquy.io.graphics.renderables.AntialiasedLineSegmentRenderableImpl;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.AntialiasedLineSegmentRenderable;
import soliloquy.specs.io.graphics.renderables.factories.AntialiasedLineSegmentRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderableStack;

import java.awt.*;

public class AntialiasedLineSegmentRenderableFactoryImpl
        implements AntialiasedLineSegmentRenderableFactory {
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
                                                 java.util.UUID uuid,
                                                 RenderableStack containingStack)
            throws IllegalArgumentException {
        return new AntialiasedLineSegmentRenderableImpl(vertex1Provider, vertex2Provider,
                thicknessProvider, colorProvider, thicknessGradientPercentProvider,
                lengthGradientPercentProvider, z, uuid, containingStack);
    }
}
