package inaugural.soliloquy.io.test.testdoubles.fakes;

import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.AntialiasedLineSegmentRenderable;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderableStack;

import java.awt.*;
import java.util.UUID;

public class FakeAntialiasedLineSegmentRenderable implements AntialiasedLineSegmentRenderable {
    public ProviderAtTime<Float> ThicknessGradientPercentProvider;
    public ProviderAtTime<Float> LengthGradientPercentProvider;
    public ProviderAtTime<Float> ThicknessProvider;
    public ProviderAtTime<Color> ColorProvider;
    public ProviderAtTime<Vertex> Vertex1Provider;
    public ProviderAtTime<Vertex> Vertex2Provider;

    public FakeAntialiasedLineSegmentRenderable(
            ProviderAtTime<Float> thicknessGradientPercentProvider,
            ProviderAtTime<Float> lengthGradientPercentProvider,
            ProviderAtTime<Float> thicknessProvider,
            ProviderAtTime<Color> colorProvider,
            ProviderAtTime<Vertex> vertex1Provider,
            ProviderAtTime<Vertex> vertex2Provider) {
        ThicknessGradientPercentProvider = thicknessGradientPercentProvider;
        LengthGradientPercentProvider = lengthGradientPercentProvider;
        ThicknessProvider = thicknessProvider;
        ColorProvider = colorProvider;
        Vertex1Provider = vertex1Provider;
        Vertex2Provider = vertex2Provider;
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
    public ProviderAtTime<Vertex> getVertex1Provider() {
        return Vertex1Provider;
    }

    @Override
    public void setVertex1Provider(ProviderAtTime<Vertex> providerAtTime)
            throws IllegalArgumentException {

    }

    @Override
    public ProviderAtTime<Vertex> getVertex2Provider() {
        return Vertex2Provider;
    }

    @Override
    public void setVertex2Provider(ProviderAtTime<Vertex> providerAtTime)
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
    public RenderableStack containingStack() {
        return null;
    }

    @Override
    public void delete() {

    }

    @Override
    public UUID uuid() {
        return null;
    }
}
