package inaugural.soliloquy.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.infrastructure.Pair;
import soliloquy.specs.graphics.renderables.RasterizedLineSegmentRenderable;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;

import java.awt.*;
import java.util.UUID;
import java.util.function.Consumer;

public class RasterizedLineSegmentRenderableImpl extends AbstractLineSegmentRenderable
        implements RasterizedLineSegmentRenderable {
    private short _stipplePattern;
    private short _stippleFactor;

    public RasterizedLineSegmentRenderableImpl(ProviderAtTime<Pair<Float, Float>>
                                                       vertex1LocationProvider,
                                               ProviderAtTime<Pair<Float, Float>>
                                                       vertex2LocationProvider,
                                               ProviderAtTime<Float> thicknessProvider,
                                               short stipplePattern, short stippleFactor,
                                               ProviderAtTime<Color> colorProvider,
                                               int z, UUID uuid,
                                               Consumer<Renderable> updateZIndexInContainer,
                                               Consumer<Renderable> removeFromContainer) {
        super(vertex1LocationProvider, vertex2LocationProvider, thicknessProvider, colorProvider, z,
                uuid, updateZIndexInContainer, removeFromContainer);
        setStipplePattern(stipplePattern);
        setStippleFactor(stippleFactor);
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
    public String getInterfaceName() {
        return RasterizedLineSegmentRenderable.class.getCanonicalName();
    }
}
