package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.RasterizedLineSegmentRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.awt.*;

public class FakeRasterizedLineSegmentRenderable implements RasterizedLineSegmentRenderable {
    public ProviderAtTime<Float> ThicknessProvider;
    public short StipplePattern;
    public int StippleFactor;
    public ProviderAtTime<Color> ColorProvider;
    public ProviderAtTime<FloatBox> RenderingAreaProvider;
    public int Z;

    public FakeRasterizedLineSegmentRenderable(ProviderAtTime<Float> thicknessProvider,
                                               short stipplePattern,
                                               int stippleFactor,
                                               ProviderAtTime<Color> colorProvider,
                                               ProviderAtTime<FloatBox> renderingAreaProvider,
                                               int z) {
        ThicknessProvider = thicknessProvider;
        StipplePattern = stipplePattern;
        StippleFactor = stippleFactor;
        ColorProvider = colorProvider;
        RenderingAreaProvider = renderingAreaProvider;
        Z = z;
    }

    @Override
    public ProviderAtTime<Float> thicknessProvider() {
        return ThicknessProvider;
    }

    @Override
    public short stipplePattern() {
        return StipplePattern;
    }

    @Override
    public int stippleFactor() {
        return StippleFactor;
    }

    @Override
    public ProviderAtTime<Color> colorProvider() {
        return ColorProvider;
    }

    @Override
    public ProviderAtTime<FloatBox> renderingAreaProvider() {
        return RenderingAreaProvider;
    }

    @Override
    public int z() {
        return Z;
    }

    @Override
    public void delete() {

    }

    @Override
    public String getInterfaceName() {
        return null;
    }

    @Override
    public EntityUuid id() {
        return null;
    }
}
