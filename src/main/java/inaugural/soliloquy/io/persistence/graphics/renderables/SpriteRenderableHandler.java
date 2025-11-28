package inaugural.soliloquy.io.persistence.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Consumer;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.io.graphics.assets.Sprite;
import soliloquy.specs.io.graphics.renderables.SpriteRenderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.renderables.factories.SpriteRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import java.util.UUID;
import java.util.function.Function;

public class SpriteRenderableHandler
        extends AbstractImageAssetRenderableHandler<Sprite, SpriteRenderable> {
    private final SpriteRenderableFactory FACTORY;

    public SpriteRenderableHandler(
            Function<String, Sprite> getAsset,
            @SuppressWarnings("rawtypes") Function<String, Consumer> getConsumer,
            @SuppressWarnings("rawtypes") TypeHandler<ProviderAtTime> providerHandler,
            TypeHandler<ColorShift> shiftHandler, SpriteRenderableFactory factory) {
        super(getAsset, getConsumer, providerHandler, shiftHandler);
        FACTORY = Check.ifNull(factory, "factory");
    }

    @SuppressWarnings("unchecked")
    @Override
    public SpriteRenderable read(String writtenVal)
            throws IllegalArgumentException {
        var dto = JSON.fromJson(Check.ifNullOrEmpty(writtenVal, "writtenVal"), Dto.class);

        var readProps = new ReadProps<Sprite>();
        hydrateReadProps(readProps, dto);

        return FACTORY.make(
                readProps.asset,
                readProps.borderThickness,
                readProps.borderColor,
                readProps.onPress,
                readProps.onRelease,
                readProps.onMouseOver,
                readProps.onMouseLeave,
                readProps.shifts,
                readProps.area,
                dto.z,
                UUID.fromString(dto.uuid),
                null
        );
    }

    @Override
    public String write(SpriteRenderable renderable) {
        Check.ifNull(renderable, "renderable");

        var dto = new AbstractImageAssetRenderableHandler.Dto();

        hydrateDto(dto, renderable, renderable.getSprite().id());

        return JSON.toJson(dto);
    }
}
