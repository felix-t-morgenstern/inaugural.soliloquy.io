package inaugural.soliloquy.io.persistence.graphics.renderables;

import inaugural.soliloquy.io.persistence.graphics.renderables.providers.ProviderHandler;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.TriangleRenderable;
import soliloquy.specs.io.graphics.renderables.factories.TriangleRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import java.awt.*;
import java.util.UUID;
import java.util.function.Function;

public class TriangleRenderableHandler extends AbstractPolygonRenderableHandler<TriangleRenderable> {
    private final TriangleRenderableFactory FACTORY;

    public TriangleRenderableHandler(
            @SuppressWarnings("rawtypes") Function<String, Action> getAction,
            ProviderHandler providerHandler, TriangleRenderableFactory factory) {
        super(getAction, providerHandler);
        FACTORY = Check.ifNull(factory, "factory");
    }

    @SuppressWarnings("unchecked")
    @Override
    public TriangleRenderable read(String writtenVal) throws IllegalArgumentException {
        Check.ifNullOrEmpty(writtenVal, "writtenVal");

        var dto = JSON.fromJson(writtenVal, Dto.class);

        var readProps = new AbstractPolygonRenderableHandler.ReadProps();
        hydrateReadProps(readProps, dto);

        var vertex1 = PROVIDER_HANDLER.read(dto.vertex1);
        var vertex1Color = PROVIDER_HANDLER.read(dto.vertex1Color);
        var vertex2 = PROVIDER_HANDLER.read(dto.vertex2);
        var vertex2Color = PROVIDER_HANDLER.read(dto.vertex2Color);
        var vertex3 = PROVIDER_HANDLER.read(dto.vertex3);
        var vertex3Color = PROVIDER_HANDLER.read(dto.vertex3Color);

        return FACTORY.make(
                vertex1,
                vertex1Color,
                vertex2,
                vertex2Color,
                vertex3,
                vertex3Color,
                readProps.texId,
                readProps.texWidth,
                readProps.texHeight,
                readProps.onPress,
                readProps.onRelease,
                readProps.onMouseOver,
                readProps.onMouseLeave,
                dto.z,
                UUID.fromString(dto.uuid),
                null
        );
    }

    @Override
    public String write(TriangleRenderable renderable) {
        Check.ifNull(renderable, "renderable");

        var dto = new Dto();

        hydrateDto(dto, renderable);

        dto.vertex1 = PROVIDER_HANDLER.write(renderable.getVertex1Provider());
        dto.vertex1Color = PROVIDER_HANDLER.write(renderable.getVertex1ColorProvider());
        dto.vertex2 = PROVIDER_HANDLER.write(renderable.getVertex2Provider());
        dto.vertex2Color = PROVIDER_HANDLER.write(renderable.getVertex2ColorProvider());
        dto.vertex3 = PROVIDER_HANDLER.write(renderable.getVertex3Provider());
        dto.vertex3Color = PROVIDER_HANDLER.write(renderable.getVertex3ColorProvider());


        return JSON.toJson(dto);
    }

    private static class Dto extends AbstractPolygonRenderableHandler.Dto {
        String vertex1;
        String vertex1Color;
        String vertex2;
        String vertex2Color;
        String vertex3;
        String vertex3Color;
    }

    private static class ReadProps extends AbstractPolygonRenderableHandler.ReadProps {
        ProviderAtTime<Vertex> vertex1;
        ProviderAtTime<Color> vertex1Color;
        ProviderAtTime<Vertex> vertex2;
        ProviderAtTime<Color> vertex2Color;
        ProviderAtTime<Vertex> vertex3;
        ProviderAtTime<Color> vertex3Color;
    }
}
