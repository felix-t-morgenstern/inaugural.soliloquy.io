package inaugural.soliloquy.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.infrastructure.Pair;
import soliloquy.specs.graphics.renderables.AntialiasedLineSegmentRenderable;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;

import java.awt.*;
import java.util.function.Consumer;

public class AntialiasedLineSegmentRenderableImpl
        extends AbstractLineSegmentRenderable
        implements AntialiasedLineSegmentRenderable {
    private ProviderAtTime<Float> _thicknessGradientPercentProvider;
    private ProviderAtTime<Float> _lengthGradientPercentProvider;

    public AntialiasedLineSegmentRenderableImpl(
            ProviderAtTime<Pair<Float, Float>> vertex1LocationProvider,
            ProviderAtTime<Pair<Float, Float>> vertex2LocationProvider,
            ProviderAtTime<Float> thicknessProvider,
            ProviderAtTime<Color> colorProvider,
            ProviderAtTime<Float> thicknessGradientPercentProvider,
            ProviderAtTime<Float> lengthGradientPercentProvider,
            int z,
            java.util.UUID uuid,
            Consumer<Renderable> updateZIndexInContainer,
            Consumer<Renderable> removeFromContainer) {
        super(vertex1LocationProvider, vertex2LocationProvider, thicknessProvider, colorProvider, z,
                uuid, updateZIndexInContainer, removeFromContainer);
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
