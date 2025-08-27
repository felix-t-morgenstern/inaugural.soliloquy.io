package inaugural.soliloquy.io.persistence.graphics.renderables;

import inaugural.soliloquy.io.persistence.graphics.renderables.providers.ProviderHandler;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.io.graphics.renderables.RectangleRenderable;
import soliloquy.specs.io.graphics.renderables.factories.RectangleRenderableFactory;

import java.util.UUID;
import java.util.function.Function;

public class RectangleRenderableHandler extends AbstractPolygonRenderableHandler<RectangleRenderable> {
    private final RectangleRenderableFactory FACTORY;

    public RectangleRenderableHandler(
            @SuppressWarnings("rawtypes") Function<String, Action> getAction,
            ProviderHandler providerHandler, RectangleRenderableFactory factory) {
        super(getAction, providerHandler);
        FACTORY = Check.ifNull(factory, "factory");
    }

    @SuppressWarnings("unchecked")
    @Override
    public RectangleRenderable read(String writtenVal) throws IllegalArgumentException {
        Check.ifNullOrEmpty(writtenVal, "writtenVal");

        var dto = JSON.fromJson(writtenVal, Dto.class);

        var readProps = new ReadProps();
        hydrateReadProps(readProps, dto);

        var topLeftColor = PROVIDER_HANDLER.read(dto.topLeftColor);
        var topRightColor = PROVIDER_HANDLER.read(dto.topRightColor);
        var bottomLeftColor = PROVIDER_HANDLER.read(dto.bottomLeftColor);
        var bottomRightColor = PROVIDER_HANDLER.read(dto.bottomRightColor);

        var area = PROVIDER_HANDLER.read(dto.area);

        return FACTORY.make(
                topLeftColor,
                topRightColor,
                bottomLeftColor,
                bottomRightColor,
                readProps.texId,
                readProps.texWidth,
                readProps.texHeight,
                readProps.onPress,
                readProps.onRelease,
                readProps.onMouseOver,
                readProps.onMouseLeave,
                area,
                dto.z,
                UUID.fromString(dto.uuid),
                null
        );
    }

    @Override
    public String write(RectangleRenderable renderable) {
        Check.ifNull(renderable, "renderable");

        var dto = new Dto();

        hydrateDto(dto, renderable);

        dto.topLeftColor = PROVIDER_HANDLER.write(renderable.getTopLeftColorProvider());
        dto.topRightColor = PROVIDER_HANDLER.write(renderable.getTopRightColorProvider());
        dto.bottomLeftColor = PROVIDER_HANDLER.write(renderable.getBottomLeftColorProvider());
        dto.bottomRightColor = PROVIDER_HANDLER.write(renderable.getBottomRightColorProvider());

        dto.area = PROVIDER_HANDLER.write(renderable.getRenderingDimensionsProvider());

        return JSON.toJson(dto);
    }

    protected static class Dto extends AbstractPolygonRenderableHandler.Dto {
        String topLeftColor;
        String topRightColor;
        String bottomLeftColor;
        String bottomRightColor;
        String area;
    }
}
