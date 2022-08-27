package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.common.infrastructure.Pair;
import soliloquy.specs.graphics.renderables.RasterizedLineSegmentRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.awt.*;
import java.util.UUID;

public class FakeRasterizedLineSegmentRenderable implements RasterizedLineSegmentRenderable {
    public ProviderAtTime<Float> ThicknessProvider;
    public short StipplePattern;
    public short StippleFactor;
    public ProviderAtTime<Color> ColorProvider;
    public ProviderAtTime<Pair<Float, Float>> Vertex1LocationProvider;
    public ProviderAtTime<Pair<Float, Float>> Vertex2LocationProvider;
    public int Z;
    public UUID Uuid;

    public FakeRasterizedLineSegmentRenderable(ProviderAtTime<Pair<Float, Float>> vertex1LocationProvider,
                                               ProviderAtTime<Pair<Float, Float>> vertex2LocationProvider,
                                               ProviderAtTime<Float> thicknessProvider,
                                               short stipplePattern,
                                               short stippleFactor,
                                               ProviderAtTime<Color> colorProvider,
                                               int z, UUID uuid) {
        Vertex1LocationProvider = vertex1LocationProvider;
        Vertex2LocationProvider = vertex2LocationProvider;
        ThicknessProvider = thicknessProvider;
        StipplePattern = stipplePattern;
        StippleFactor = stippleFactor;
        ColorProvider = colorProvider;
        Z = z;
        Uuid = uuid;
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
