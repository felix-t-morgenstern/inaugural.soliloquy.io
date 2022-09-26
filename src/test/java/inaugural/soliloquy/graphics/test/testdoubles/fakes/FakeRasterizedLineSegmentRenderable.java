package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.renderables.RasterizedLineSegmentRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.RenderableStack;

import java.awt.*;
import java.util.UUID;

public class FakeRasterizedLineSegmentRenderable implements RasterizedLineSegmentRenderable {
    public ProviderAtTime<Float> ThicknessProvider;
    public short StipplePattern;
    public short StippleFactor;
    public ProviderAtTime<Color> ColorProvider;
    public ProviderAtTime<Vertex> Vertex1Provider;
    public ProviderAtTime<Vertex> Vertex2Provider;
    public int Z;
    public UUID Uuid;

    public FakeRasterizedLineSegmentRenderable(ProviderAtTime<Vertex> vertex1Provider,
                                               ProviderAtTime<Vertex> vertex2Provider,
                                               ProviderAtTime<Float> thicknessProvider,
                                               short stipplePattern,
                                               short stippleFactor,
                                               ProviderAtTime<Color> colorProvider,
                                               int z, UUID uuid) {
        Vertex1Provider = vertex1Provider;
        Vertex2Provider = vertex2Provider;
        ThicknessProvider = thicknessProvider;
        StipplePattern = stipplePattern;
        StippleFactor = stippleFactor;
        ColorProvider = colorProvider;
        Z = z;
        Uuid = uuid;
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
    public short getStipplePattern() {
        return StipplePattern;
    }

    @Override
    public void setStipplePattern(short i) throws IllegalArgumentException {

    }

    @Override
    public short getStippleFactor() {
        return StippleFactor;
    }

    @Override
    public void setStippleFactor(short i) throws IllegalArgumentException {

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
        return Z;
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
    public String getInterfaceName() {
        return null;
    }

    @Override
    public UUID uuid() {
        return Uuid;
    }
}
