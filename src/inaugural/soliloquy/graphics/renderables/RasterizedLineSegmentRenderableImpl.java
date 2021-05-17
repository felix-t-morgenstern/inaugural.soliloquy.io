package inaugural.soliloquy.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.RasterizedLineSegmentRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.awt.*;
import java.util.function.Consumer;

public class RasterizedLineSegmentRenderableImpl implements RasterizedLineSegmentRenderable {
    private final ProviderAtTime<Float> THICKNESS_PROVIDER;
    private final short STIPPLE_PATTERN;
    private final short STIPPLE_FACTOR;
    private final ProviderAtTime<Color> COLOR_PROVIDER;
    private final ProviderAtTime<FloatBox> RENDERING_AREA_PROVIDER;
    private final int Z;
    private final EntityUuid UUID;
    private final Consumer<RasterizedLineSegmentRenderable> DELETE_CONSUMER;

    public RasterizedLineSegmentRenderableImpl(ProviderAtTime<Float> thicknessProvider,
                                               short stipplePattern, short stippleFactor,
                                               ProviderAtTime<Color> colorProvider,
                                               ProviderAtTime<FloatBox> renderingAreaProvider,
                                               int z, EntityUuid uuid,
                                               Consumer<RasterizedLineSegmentRenderable>
                                                       deleteConsumer) {
        THICKNESS_PROVIDER = Check.ifNull(thicknessProvider, "thicknessProvider");
        STIPPLE_PATTERN = Check.throwOnEqualsValue(stipplePattern, (short) 0, "stipplePattern");
        STIPPLE_FACTOR = Check.throwOnLtValue(
                Check.throwOnGtValue(stippleFactor, (short) 256, "stippleFactor"),
                (short) 1, "stippleFactor"
        );
        COLOR_PROVIDER = Check.ifNull(colorProvider, "colorProvider");
        RENDERING_AREA_PROVIDER = Check.ifNull(renderingAreaProvider, "renderingAreaProvider");
        Z = z;
        UUID = Check.ifNull(uuid, "uuid");
        DELETE_CONSUMER = Check.ifNull(deleteConsumer, "deleteConsumer");
    }

    @Override
    public ProviderAtTime<Float> thicknessProvider() {
        return THICKNESS_PROVIDER;
    }

    @Override
    public short stipplePattern() {
        return STIPPLE_PATTERN;
    }

    @Override
    public short stippleFactor() {
        return STIPPLE_FACTOR;
    }

    @Override
    public ProviderAtTime<Color> colorProvider() {
        return COLOR_PROVIDER;
    }

    @Override
    public ProviderAtTime<FloatBox> renderingAreaProvider() {
        return RENDERING_AREA_PROVIDER;
    }

    @Override
    public int z() {
        return Z;
    }

    @Override
    public void delete() {
        DELETE_CONSUMER.accept(this);
    }

    @Override
    public EntityUuid uuid() {
        return UUID;
    }

    @Override
    public String getInterfaceName() {
        return RasterizedLineSegmentRenderable.class.getCanonicalName();
    }
}
