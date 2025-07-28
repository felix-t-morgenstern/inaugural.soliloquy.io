package inaugural.soliloquy.io.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.AntialiasedLineSegmentRenderable;
import soliloquy.specs.io.graphics.renderables.Renderable;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.ui.Component;

import java.awt.*;
import java.util.function.BiConsumer;

public class AntialiasedLineSegmentRenderableImpl
        extends AbstractLineSegmentRenderable
        implements AntialiasedLineSegmentRenderable {
    private ProviderAtTime<Float> thicknessGradientPercentProvider;
    private ProviderAtTime<Float> lengthGradientPercentProvider;

    public AntialiasedLineSegmentRenderableImpl(
            ProviderAtTime<Vertex> vertex1Provider,
            ProviderAtTime<Vertex> vertex2Provider,
            ProviderAtTime<Color> colorProvider,
            ProviderAtTime<Float> thicknessProvider,
            ProviderAtTime<Float> thicknessGradientPercentProvider,
            ProviderAtTime<Float> lengthGradientPercentProvider,
            int z,
            java.util.UUID uuid,
            Component component) {
        super(vertex1Provider, vertex2Provider, thicknessProvider, colorProvider, z,
                uuid, component);
        setThicknessGradientPercentProvider(thicknessGradientPercentProvider);
        setLengthGradientPercentProvider(lengthGradientPercentProvider);
    }

    @Override
    public ProviderAtTime<Float> getThicknessGradientPercentProvider() {
        return thicknessGradientPercentProvider;
    }

    @Override
    public void setThicknessGradientPercentProvider(
            ProviderAtTime<Float> thicknessGradientPercentProvider)
            throws IllegalArgumentException {
        this.thicknessGradientPercentProvider =
                Check.ifNull(thicknessGradientPercentProvider, "thicknessGradientPercentProvider");
    }

    @Override
    public ProviderAtTime<Float> getLengthGradientPercentProvider() {
        return lengthGradientPercentProvider;
    }

    @Override
    public void setLengthGradientPercentProvider(
            ProviderAtTime<Float> lengthGradientPercentProvider)
            throws IllegalArgumentException {
        this.lengthGradientPercentProvider =
                Check.ifNull(lengthGradientPercentProvider, "lengthGradientPercentProvider");
    }
}
