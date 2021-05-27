package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.RasterizedLineSegmentRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.awt.*;

public class FakeRasterizedLineSegmentRenderable implements RasterizedLineSegmentRenderable {
    public ProviderAtTime<Float> ThicknessProvider;
    public short StipplePattern;
    public short StippleFactor;
    public ProviderAtTime<Color> ColorProvider;
    public ProviderAtTime<FloatBox> RenderingDimensionsProvider;
    public int Z;
    public EntityUuid Uuid;

    public FakeRasterizedLineSegmentRenderable(ProviderAtTime<Float> thicknessProvider,
                                               short stipplePattern,
                                               short stippleFactor,
                                               ProviderAtTime<Color> colorProvider,
                                               ProviderAtTime<FloatBox> renderingDimensionsProvider,
                                               int z, EntityUuid uuid) {
        ThicknessProvider = thicknessProvider;
        StipplePattern = stipplePattern;
        StippleFactor = stippleFactor;
        ColorProvider = colorProvider;
        RenderingDimensionsProvider = renderingDimensionsProvider;
        Z = z;
        Uuid = uuid;
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
    public ProviderAtTime<FloatBox> getRenderingDimensionsProvider() {
        return RenderingDimensionsProvider;
    }

    @Override
    public void setRenderingDimensionsProvider(ProviderAtTime<FloatBox>
                                                       renderingDimensionsProvider)
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
    public EntityUuid uuid() {
        return Uuid;
    }
}
