package inaugural.soliloquy.io.persistence.graphics.renderables;

import inaugural.soliloquy.io.graphics.renderables.AntialiasedLineSegmentRenderableImpl;
import inaugural.soliloquy.io.persistence.graphics.renderables.providers.ProviderHandler;
import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.persistence.AbstractTypeHandler;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.io.graphics.renderables.AntialiasedLineSegmentRenderable;
import soliloquy.specs.io.graphics.renderables.factories.AntialiasedLineSegmentRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import java.util.UUID;

public class AntialiasedLineSegmentRenderableHandler extends
        AbstractTypeHandler<AntialiasedLineSegmentRenderable> {
    @SuppressWarnings("rawtypes") private final TypeHandler<ProviderAtTime> PROVIDER_HANDLER;
    private final AntialiasedLineSegmentRenderableFactory FACTORY;

    public AntialiasedLineSegmentRenderableHandler(
            @SuppressWarnings("rawtypes") TypeHandler<ProviderAtTime> providerHandler,
            AntialiasedLineSegmentRenderableFactory factory) {
        PROVIDER_HANDLER = Check.ifNull(providerHandler, "providerHandler");
        FACTORY = Check.ifNull(factory, "factory");
    }

    @Override
    public String typeHandled() {
        return AntialiasedLineSegmentRenderableImpl.class.getCanonicalName();
    }

    @SuppressWarnings("unchecked")
    @Override
    public AntialiasedLineSegmentRenderable read(String writtenVal)
            throws IllegalArgumentException {
        var dto = JSON.fromJson(writtenVal, AntialiasedLineSegmentRenderableDTO.class);

        var vertex1 = PROVIDER_HANDLER.read(dto.vertex1);
        var vertex2 = PROVIDER_HANDLER.read(dto.vertex2);
        var color = PROVIDER_HANDLER.read(dto.color);
        var thickness = PROVIDER_HANDLER.read(dto.thickness);
        var thicknessGradientPercent = PROVIDER_HANDLER.read(dto.thicknessGradientPercent);
        var lengthGradientPercent = PROVIDER_HANDLER.read(dto.lengthGradientPercent);

        return FACTORY.make(vertex1, vertex2, color, thickness, thicknessGradientPercent,
                lengthGradientPercent, dto.z, UUID.fromString(dto.uuid), null);
    }

    @Override
    public String write(AntialiasedLineSegmentRenderable renderable) {
        Check.ifNull(renderable, "renderable");
        var dto = new AntialiasedLineSegmentRenderableDTO();
        dto.type = renderable.getClass().getCanonicalName();
        dto.vertex1 = PROVIDER_HANDLER.write(renderable.getVertex1Provider());
        dto.vertex2 = PROVIDER_HANDLER.write(renderable.getVertex2Provider());
        dto.color = PROVIDER_HANDLER.write(renderable.getColorProvider());
        dto.thickness = PROVIDER_HANDLER.write(renderable.getThicknessProvider());
        dto.thicknessGradientPercent =
                PROVIDER_HANDLER.write(renderable.getThicknessGradientPercentProvider());
        dto.lengthGradientPercent =
                PROVIDER_HANDLER.write(renderable.getLengthGradientPercentProvider());
        dto.z = renderable.getZ();
        dto.uuid = renderable.uuid().toString();

        return JSON.toJson(dto);
    }

    public static class AntialiasedLineSegmentRenderableDTO extends ProviderHandler.ProviderDTO {
        public String vertex1;
        public String vertex2;
        public String color;
        public String thickness;
        public String thicknessGradientPercent;
        public String lengthGradientPercent;
        public int z;
        public String uuid;
    }
}
