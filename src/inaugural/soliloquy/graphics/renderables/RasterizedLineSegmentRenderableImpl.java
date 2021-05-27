package inaugural.soliloquy.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.RasterizedLineSegmentRenderable;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.awt.*;
import java.util.function.Consumer;

public class RasterizedLineSegmentRenderableImpl extends AbstractRenderableWithDimensions
        implements RasterizedLineSegmentRenderable {
    private ProviderAtTime<Float> _thicknessProvider;
    private short _stipplePattern;
    private short _stippleFactor;
    private ProviderAtTime<Color> _colorProvider;

    public RasterizedLineSegmentRenderableImpl(ProviderAtTime<Float> thicknessProvider,
                                               short stipplePattern, short stippleFactor,
                                               ProviderAtTime<Color> colorProvider,
                                               ProviderAtTime<FloatBox> renderingAreaProvider,
                                               int z, EntityUuid uuid,
                                               Consumer<Renderable> updateZIndexInContainer,
                                               Consumer<Renderable> removeFromContainer) {
        super(renderingAreaProvider, z, uuid, updateZIndexInContainer, removeFromContainer);
        setThicknessProvider(thicknessProvider);
        setStipplePattern(stipplePattern);
        setStippleFactor(stippleFactor);
        setColorProvider(colorProvider);
    }

    @Override
    public ProviderAtTime<Float> getThicknessProvider() {
        return _thicknessProvider;
    }

    @Override
    public void setThicknessProvider(ProviderAtTime<Float> thicknessProvider)
            throws IllegalArgumentException {
        _thicknessProvider = Check.ifNull(thicknessProvider, "thicknessProvider");
    }

    @Override
    public short getStipplePattern() {
        return _stipplePattern;
    }

    @Override
    public void setStipplePattern(short stipplePattern) throws IllegalArgumentException {
        _stipplePattern = Check.throwOnEqualsValue(stipplePattern, (short) 0, "stipplePattern");
    }

    @Override
    public short getStippleFactor() {
        return _stippleFactor;
    }

    @Override
    public void setStippleFactor(short stippleFactor) throws IllegalArgumentException {
        _stippleFactor = Check.throwOnLtValue(
                Check.throwOnGtValue(stippleFactor, (short) 256, "stippleFactor"),
                (short) 1, "stippleFactor"
        );
    }

    @Override
    public ProviderAtTime<Color> getColorProvider() {
        return _colorProvider;
    }

    @Override
    public void setColorProvider(ProviderAtTime<Color> colorProvider)
            throws IllegalArgumentException {
        _colorProvider = Check.ifNull(colorProvider, "colorProvider");
    }

    @Override
    public String getInterfaceName() {
        return RasterizedLineSegmentRenderable.class.getCanonicalName();
    }
}
