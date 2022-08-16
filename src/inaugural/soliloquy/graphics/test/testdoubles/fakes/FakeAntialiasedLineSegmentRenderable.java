package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.common.infrastructure.Pair;
import soliloquy.specs.graphics.renderables.AntialiasedLineSegmentRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;

import java.awt.*;
import java.util.UUID;

public class FakeAntialiasedLineSegmentRenderable implements AntialiasedLineSegmentRenderable {
    public ProviderAtTime<Float> ThicknessGradientPercentProvider;
    public ProviderAtTime<Float> LengthGradientPercentProvider;
    public ProviderAtTime<Float> ThicknessProvider;
    public ProviderAtTime<Color> ColorProvider;
    public ProviderAtTime<Pair<Float, Float>> Vertex1LocationProvider;
    public ProviderAtTime<Pair<Float, Float>> Vertex2LocationProvider;

    public FakeAntialiasedLineSegmentRenderable() {

    }

    public FakeAntialiasedLineSegmentRenderable(
            ProviderAtTime<Float> thicknessGradientPercentProvider,
            ProviderAtTime<Float> lengthGradientPercentProvider,
            ProviderAtTime<Float> thicknessProvider,
            ProviderAtTime<Color> colorProvider,
            ProviderAtTime<Pair<Float, Float>> vertex1LocationProvider,
            ProviderAtTime<Pair<Float, Float>> vertex2LocationProvider) {
        ThicknessGradientPercentProvider = thicknessGradientPercentProvider;
        LengthGradientPercentProvider = lengthGradientPercentProvider;
        ThicknessProvider = thicknessProvider;
        ColorProvider = colorProvider;
        Vertex1LocationProvider = vertex1LocationProvider;
        Vertex2LocationProvider = vertex2LocationProvider;
    }

    @Override
    public ProviderAtTime<Float> getThicknessGradientPercentProvider() {
        return ThicknessGradientPercentProvider;
    }

    @Override
    public void setThicknessGradientPercentProvider(ProviderAtTime<Float> providerAtTime)
            throws IllegalArgumentException {

    }

    @Override
    public ProviderAtTime<Float> getLengthGradientPercentProvider() {
        return LengthGradientPercentProvider;
    }

    @Override
    public void setLengthGradientPercentProvider(ProviderAtTime<Float> providerAtTime)
            throws IllegalArgumentException {

    }

    @Override
    public ProviderAtTime<Pair<Float, Float>> getVertex1LocationProvider() {
        return Vertex1LocationProvider;
    }

    @Override
    public void setVertex1LocationProvider(ProviderAtTime<Pair<Float, Float>> providerAtTime)
            throws IllegalArgumentException {

    }

    @Override
    public ProviderAtTime<Pair<Float, Float>> getVertex2LocationProvider() {
        return Vertex2LocationProvider;
    }

    @Override
    public void setVertex2LocationProvider(ProviderAtTime<Pair<Float, Float>> providerAtTime)
            throws IllegalArgumentException {

    }

    @Override
    public ProviderAtTime<Float> getThicknessProvider() {
        return ThicknessProvider;
    }

    @Override
    public void setThicknessProvider(ProviderAtTime<Float> providerAtTime)
            throws IllegalArgumentException {

    }

    @Override
    public ProviderAtTime<Color> getColorProvider() {
        return ColorProvider;
    }

    @Override
    public void setColorProvider(ProviderAtTime<Color> providerAtTime)
            throws IllegalArgumentException {

    }

    @Override
    public int getZ() {
        return 0;
    }

    @Override
    public void setZ(int i) {

    }

    @Override
    public void delete() {

    }

    @Override
    public UUID uuid() {
        return null;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
