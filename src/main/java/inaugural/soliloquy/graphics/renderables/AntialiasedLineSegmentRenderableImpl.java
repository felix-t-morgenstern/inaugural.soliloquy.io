package inaugural.soliloquy.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.renderables.AntialiasedLineSegmentRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.RenderableStack;

import java.awt.*;

public class AntialiasedLineSegmentRenderableImpl
        extends AbstractLineSegmentRenderable
        implements AntialiasedLineSegmentRenderable {
    private ProviderAtTime<Float> _thicknessGradientPercentProvider;
    private ProviderAtTime<Float> _lengthGradientPercentProvider;

    public AntialiasedLineSegmentRenderableImpl(
            ProviderAtTime<Vertex> vertex1Provider,
            ProviderAtTime<Vertex> vertex2Provider,
            ProviderAtTime<Float> thicknessProvider,
            ProviderAtTime<Color> colorProvider,
            ProviderAtTime<Float> thicknessGradientPercentProvider,
            ProviderAtTime<Float> lengthGradientPercentProvider,
            int z,
            java.util.UUID uuid,
            RenderableStack containingStack) {
        super(vertex1Provider, vertex2Provider, thicknessProvider, colorProvider, z,
                uuid, containingStack);
        setThicknessGradientPercentProvider(thicknessGradientPercentProvider);
        setLengthGradientPercentProvider(lengthGradientPercentProvider);
    }

    @Override
    public ProviderAtTime<Float> getThicknessGradientPercentProvider() {
        return _thicknessGradientPercentProvider;
    }

    @Override
    public void setThicknessGradientPercentProvider(
            ProviderAtTime<Float> thicknessGradientPercentProvider)
            throws IllegalArgumentException {
        _thicknessGradientPercentProvider =
                Check.ifNull(thicknessGradientPercentProvider, "thicknessGradientPercentProvider");
    }

    @Override
    public ProviderAtTime<Float> getLengthGradientPercentProvider() {
        return _lengthGradientPercentProvider;
    }

    @Override
    public void setLengthGradientPercentProvider(
            ProviderAtTime<Float> lengthGradientPercentProvider)
            throws IllegalArgumentException {
        _lengthGradientPercentProvider =
                Check.ifNull(lengthGradientPercentProvider, "lengthGradientPercentProvider");
    }

    @Override
    public String getInterfaceName() {
        return AntialiasedLineSegmentRenderable.class.getCanonicalName();
    }
}
