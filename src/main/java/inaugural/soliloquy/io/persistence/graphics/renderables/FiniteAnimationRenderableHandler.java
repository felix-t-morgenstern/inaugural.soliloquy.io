package inaugural.soliloquy.io.persistence.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Consumer;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.io.graphics.assets.Animation;
import soliloquy.specs.io.graphics.renderables.FiniteAnimationRenderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.renderables.factories.FiniteAnimationRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import java.util.UUID;
import java.util.function.Function;

public class FiniteAnimationRenderableHandler
        extends AbstractImageAssetRenderableHandler<Animation, FiniteAnimationRenderable> {
    private final FiniteAnimationRenderableFactory FACTORY;

    public FiniteAnimationRenderableHandler(
            Function<String, Animation> getAnimation,
            @SuppressWarnings("rawtypes") Function<String, Consumer> getConsumer,
            @SuppressWarnings("rawtypes") TypeHandler<ProviderAtTime> providerHandler,
            TypeHandler<ColorShift> shiftHandler,
            FiniteAnimationRenderableFactory factory
    ) {
        super(getAnimation, getConsumer, providerHandler, shiftHandler);
        FACTORY = Check.ifNull(factory, "factory");
    }

    @SuppressWarnings("unchecked")
    @Override
    public FiniteAnimationRenderable read(String writtenVal) throws IllegalArgumentException {
        var dto = JSON.fromJson(Check.ifNullOrEmpty(writtenVal, "writtenVal"),
                FiniteAnimationRenderableDto.class);

        var readProps = new ReadProps<Animation>();
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
                null,
                dto.start,
                dto.pause
        );
    }

    @Override
    public String write(FiniteAnimationRenderable renderable) {
        Check.ifNull(renderable, "renderable");

        var dto = new FiniteAnimationRenderableDto();

        hydrateDto(dto, renderable, renderable.animationId());

        dto.start = renderable.startTimestamp();
        dto.pause = renderable.pausedTimestamp();

        return JSON.toJson(dto);
    }

    public static class FiniteAnimationRenderableDto
            extends Dto {
        public long start;
        public Long pause;
    }
}
