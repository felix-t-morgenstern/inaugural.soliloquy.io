package inaugural.soliloquy.graphics.renderables.factories;

import inaugural.soliloquy.graphics.renderables.AntialiasedLineSegmentRenderableImpl;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.renderables.AntialiasedLineSegmentRenderable;
import soliloquy.specs.graphics.renderables.factories.AntialiasedLineSegmentRenderableFactory;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.RenderableStack;

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
