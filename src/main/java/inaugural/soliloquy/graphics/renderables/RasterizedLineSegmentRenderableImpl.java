package inaugural.soliloquy.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.renderables.RasterizedLineSegmentRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.RenderableStack;

import java.awt.*;
import java.util.UUID;

public class RasterizedLineSegmentRenderableImpl extends AbstractLineSegmentRenderable
        implements RasterizedLineSegmentRenderable {
    private short stipplePattern;
    private short stippleFactor;

    public RasterizedLineSegmentRenderableImpl(ProviderAtTime<Vertex> vertex1Provider,
                                               ProviderAtTime<Vertex> vertex2Provider,
                                               ProviderAtTime<Float> thicknessProvider,
                                               short stipplePattern, short stippleFactor,
                                               ProviderAtTime<Color> colorProvider,
                                               int z, UUID uuid, RenderableStack containingStack) {
        super(vertex1Provider, vertex2Provider, thicknessProvider, colorProvider, z, uuid,
                containingStack);
        setStipplePattern(stipplePattern);
        setStippleFactor(stippleFactor);
    }

    @Override
    public short getStipplePattern() {
        return stipplePattern;
    }

    @Override
    public void setStipplePattern(short stipplePattern) throws IllegalArgumentException {
        this.stipplePattern = Check.throwOnEqualsValue(stipplePattern, (short) 0, "stipplePattern");
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
