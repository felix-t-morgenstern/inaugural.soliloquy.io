package inaugural.soliloquy.io.persistence.graphics.renderables;

import inaugural.soliloquy.io.persistence.graphics.renderables.providers.ProviderHandler;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Consumer;
import soliloquy.specs.io.graphics.renderables.PolygonRenderable;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import java.util.function.Function;

abstract class AbstractPolygonRenderableHandler<TRenderable extends PolygonRenderable> extends AbstractMouseEventsRenderableHandler<TRenderable> {
    protected final ProviderHandler PROVIDER_HANDLER;

    protected AbstractPolygonRenderableHandler(
            @SuppressWarnings("rawtypes") Function<String, Consumer> getConsumer,
            ProviderHandler providerHandler) {
        super(getConsumer);
        PROVIDER_HANDLER = Check.ifNull(providerHandler, "providerHandler");
    }

    protected void hydrateDto(Dto dto, TRenderable renderable) {
        super.hydrateDto(dto, renderable);

        dto.texId = PROVIDER_HANDLER.write(renderable.getTextureIdProvider());
        dto.texWidth = PROVIDER_HANDLER.write(renderable.getTextureTilesPerWidthProvider());
        dto.texXOffset = PROVIDER_HANDLER.write(renderable.getTextureXOffsetProvider());
        dto.texHeight = PROVIDER_HANDLER.write(renderable.getTextureTilesPerHeightProvider());
        dto.texYOffset = PROVIDER_HANDLER.write(renderable.getTextureYOffsetProvider());
    }

    protected void hydrateReadProps(ReadProps readProps, Dto dto) {
        super.hydrateReadProps(readProps, dto);

        readProps.texId = PROVIDER_HANDLER.read(dto.texId);
        readProps.texWidth = PROVIDER_HANDLER.read(dto.texWidth);
        readProps.texXOffset = PROVIDER_HANDLER.read(dto.texXOffset);
        readProps.texHeight = PROVIDER_HANDLER.read(dto.texHeight);
        readProps.texYOffset = PROVIDER_HANDLER.read(dto.texYOffset);
    }

    protected static class Dto extends AbstractMouseEventsRenderableHandler.Dto {
        String texId;
        String texWidth;
        String texXOffset;
        String texHeight;
        String texYOffset;
    }

    protected static class ReadProps extends AbstractMouseEventsRenderableHandler.ReadProps {
        ProviderAtTime<Integer> texId;
        ProviderAtTime<Float> texWidth;
        ProviderAtTime<Float> texXOffset;
        ProviderAtTime<Float> texHeight;
        ProviderAtTime<Float> texYOffset;
    }
}
