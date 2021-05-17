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
    public ProviderAtTime<FloatBox> RenderingAreaProvider;
    public int Z;
    public EntityUuid Uuid;

    public FakeRasterizedLineSegmentRenderable(ProviderAtTime<Float> thicknessProvider,
                                               short stipplePattern,
                                               short stippleFactor,
                                               ProviderAtTime<Color> colorProvider,
                                               ProviderAtTime<FloatBox> renderingAreaProvider,
                                               int z, EntityUuid uuid) {
        ThicknessProvider = thicknessProvider;
        StipplePattern = stipplePattern;
        StippleFactor = stippleFactor;
        ColorProvider = colorProvider;
        RenderingAreaProvider = renderingAreaProvider;
        Z = z;
        Uuid = uuid;
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
    public short stippleFactor() {
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
    public EntityUuid uuid() {
        return Uuid;
    }
}
