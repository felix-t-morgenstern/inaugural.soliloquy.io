package inaugural.soliloquy.io.persistence.graphics.renderables;

import inaugural.soliloquy.io.persistence.graphics.renderables.providers.ProviderHandler;
import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.persistence.AbstractTypeHandler;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.io.graphics.renderables.RasterizedLineSegmentRenderable;
import soliloquy.specs.io.graphics.renderables.factories.RasterizedLineSegmentRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import java.util.UUID;

public class RasterizedLineSegmentRenderableHandler extends
        AbstractTypeHandler<RasterizedLineSegmentRenderable> {
    @SuppressWarnings("rawtypes") private final TypeHandler<ProviderAtTime> PROVIDER_HANDLER;
    private final RasterizedLineSegmentRenderableFactory FACTORY;

    public RasterizedLineSegmentRenderableHandler(
            @SuppressWarnings("rawtypes") TypeHandler<ProviderAtTime> providerHandler,
            RasterizedLineSegmentRenderableFactory factory) {
        PROVIDER_HANDLER = Check.ifNull(providerHandler, "providerHandler");
        FACTORY = Check.ifNull(factory, "factory");
    }

    @SuppressWarnings("unchecked")
    @Override
    public RasterizedLineSegmentRenderable read(String writtenVal)
            throws IllegalArgumentException {
        var dto = JSON.fromJson(writtenVal, Dto.class);

        var vertex1 = PROVIDER_HANDLER.read(dto.vertex1);
        var vertex2 = PROVIDER_HANDLER.read(dto.vertex2);
        var color = PROVIDER_HANDLER.read(dto.color);
        var thickness = PROVIDER_HANDLER.read(dto.thickness);

        return FACTORY.make(vertex1, vertex2, thickness, dto.stipplePattern, dto.stippleFactor,
                color, dto.z, UUID.fromString(dto.uuid), null);
    }

    @Override
    public String write(RasterizedLineSegmentRenderable renderable) {
        Check.ifNull(renderable, "renderable");
        var dto = new Dto();
        dto.type = renderable.getClass().getCanonicalName();
        dto.vertex1 = PROVIDER_HANDLER.write(renderable.getVertex1Provider());
        dto.vertex2 = PROVIDER_HANDLER.write(renderable.getVertex2Provider());
        dto.color = PROVIDER_HANDLER.write(renderable.getColorProvider());
        dto.thickness = PROVIDER_HANDLER.write(renderable.getThicknessProvider());
        dto.stipplePattern = renderable.getStipplePattern();
        dto.stippleFactor = renderable.getStippleFactor();
        dto.z = renderable.getZ();
        dto.uuid = renderable.uuid().toString();

        return JSON.toJson(dto);
    }

    public static class Dto extends ProviderHandler.ProviderDTO {
        public String vertex1;
        public String vertex2;
        public String color;
        public String thickness;
        public short stipplePattern;
        public short stippleFactor;
        public int z;
        public String uuid;
    }
}
