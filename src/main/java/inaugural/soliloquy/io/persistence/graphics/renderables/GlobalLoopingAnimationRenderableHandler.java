package inaugural.soliloquy.io.persistence.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.io.graphics.assets.GlobalLoopingAnimation;
import soliloquy.specs.io.graphics.renderables.GlobalLoopingAnimationRenderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.renderables.factories.GlobalLoopingAnimationRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import java.util.UUID;
import java.util.function.Function;

public class GlobalLoopingAnimationRenderableHandler extends
        AbstractImageAssetRenderableHandler<GlobalLoopingAnimation,
                GlobalLoopingAnimationRenderable> {
    private final GlobalLoopingAnimationRenderableFactory FACTORY;

    public GlobalLoopingAnimationRenderableHandler(
            Function<String, GlobalLoopingAnimation> getGlobalLoopingAnimation,
            @SuppressWarnings("rawtypes") Function<String, Action> getAction,
            @SuppressWarnings("rawtypes") TypeHandler<ProviderAtTime> providerHandler,
            TypeHandler<ColorShift> shiftHandler,
            GlobalLoopingAnimationRenderableFactory factory) {
        super(getGlobalLoopingAnimation, getAction, providerHandler, shiftHandler);
        FACTORY = Check.ifNull(factory, "factory");
    }

    @SuppressWarnings("unchecked")
    @Override
    public GlobalLoopingAnimationRenderable read(String writtenVal)
            throws IllegalArgumentException {
        var dto = JSON.fromJson(Check.ifNullOrEmpty(writtenVal, "writtenVal"),
                Dto.class);

        var readProps = new ReadProps<GlobalLoopingAnimation>();
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
    public String write(GlobalLoopingAnimationRenderable renderable) {
        Check.ifNull(renderable, "renderable");

        var dto = new Dto();

        hydrateDto(dto, renderable, renderable.getGlobalLoopingAnimation().id());

        return JSON.toJson(dto);
    }
}
