package inaugural.soliloquy.io.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.RasterizedLineSegmentRenderable;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import java.awt.*;
import java.util.UUID;

import static inaugural.soliloquy.tools.Tools.defaultIfNull;

public class RasterizedLineSegmentRenderableImpl extends AbstractLineSegmentRenderable
        implements RasterizedLineSegmentRenderable {
    private Short stipplePattern;
    private Short stippleFactor;

    public RasterizedLineSegmentRenderableImpl(ProviderAtTime<Vertex> vertex1Provider,
                                               ProviderAtTime<Vertex> vertex2Provider,
                                               ProviderAtTime<Float> thicknessProvider,
                                               Short stipplePattern, short stippleFactor,
                                               ProviderAtTime<Color> colorProvider,
                                               int z,
                                               UUID uuid,
                                               Component component) {
        super(vertex1Provider, vertex2Provider, thicknessProvider, colorProvider, z, uuid,
                component);
        setStipplePattern(stipplePattern);
        setStippleFactor(stippleFactor);
    }

    @Override
    public Short getStipplePattern() {
        return stipplePattern;
    }

    @Override
    public void setStipplePattern(Short stipplePattern) throws IllegalArgumentException {
        this.stipplePattern = defaultIfNull(
                stipplePattern,
                s -> Check.throwOnEqualsValue(s, (short) 0, "stipplePattern"),
                null
        );
    }

    @Override
    public short getStippleFactor() {
        return stippleFactor;
    }

    @Override
    public void setStippleFactor(short stippleFactor) throws IllegalArgumentException {
        this.stippleFactor = Check.throwOnLtValue(
                Check.throwOnGtValue(stippleFactor, (short) 256, "stippleFactor"),
                (short) 1, "stippleFactor"
        );
    }
}
