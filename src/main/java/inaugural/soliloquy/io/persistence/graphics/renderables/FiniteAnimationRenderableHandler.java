package inaugural.soliloquy.io.persistence.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.persistence.AbstractTypeHandler;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.io.graphics.assets.Animation;
import soliloquy.specs.io.graphics.renderables.FiniteAnimationRenderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.renderables.factories.FiniteAnimationRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.ui.EventInputs;

import java.util.Arrays;
import java.util.UUID;
import java.util.function.Function;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

public class FiniteAnimationRenderableHandler
        extends AbstractTypeHandler<FiniteAnimationRenderable> {
    private final Function<String, Animation> GET_ANIMATION;
    @SuppressWarnings("rawtypes") private final Function<String, Action> GET_ACTION;
    @SuppressWarnings("rawtypes") private final TypeHandler<ProviderAtTime> PROVIDER_HANDLER;
    private final TypeHandler<ColorShift> SHIFT_HANDLER;
    private final FiniteAnimationRenderableFactory FACTORY;

    public FiniteAnimationRenderableHandler(
            Function<String, Animation> getAnimation,
            @SuppressWarnings("rawtypes") Function<String, Action> getAction,
            @SuppressWarnings("rawtypes") TypeHandler<ProviderAtTime> providerHandler,
            TypeHandler<ColorShift> shiftHandler,
            FiniteAnimationRenderableFactory factory
    ) {
        GET_ANIMATION = Check.ifNull(getAnimation, "getAnimation");
        GET_ACTION = Check.ifNull(getAction, "getAction");
        PROVIDER_HANDLER = Check.ifNull(providerHandler, "providerHandler");
        SHIFT_HANDLER = Check.ifNull(shiftHandler, "shiftHandler");
        FACTORY = Check.ifNull(factory, "factory");
    }

    @SuppressWarnings("unchecked")
    @Override
    public FiniteAnimationRenderable read(String writtenVal)
            throws IllegalArgumentException {
        var dto = JSON.fromJson(Check.ifNullOrEmpty(writtenVal, "writtenVal"),
                FiniteAnimationRenderableDto.class);

        var animation = GET_ANIMATION.apply(dto.animationId);

        var borderThickness = PROVIDER_HANDLER.read(dto.borderThickness);
        var borderColor = PROVIDER_HANDLER.read(dto.borderColor);

        @SuppressWarnings("unchecked") var onPress = mapOf(Arrays.stream(dto.onPress)
                .map(p -> pairOf(p.button, (Action<EventInputs>) GET_ACTION.apply(p.actionId))));
        @SuppressWarnings("unchecked") var onRelease = mapOf(Arrays.stream(dto.onRelease)
                .map(p -> pairOf(p.button, (Action<EventInputs>) GET_ACTION.apply(p.actionId))));
        var onMouseOver = GET_ACTION.apply(dto.mouseOver);
        var onMouseLeave = GET_ACTION.apply(dto.mouseLeave);

        var shifts = Arrays.stream(dto.colorShifts).map(s -> (ColorShift) SHIFT_HANDLER.read(s)).toList();

        var area = PROVIDER_HANDLER.read(dto.area);

        //noinspection unchecked
        return FACTORY.make(
                animation,
                borderThickness,
                borderColor,
                onPress,
                onRelease,
                onMouseOver,
                onMouseLeave,
                shifts,
                area,
                dto.z,
                UUID.fromString(dto.uuid),
                null,
                dto.start,
                dto.pause,
                null
        );
    }

    @Override
    public String write(FiniteAnimationRenderable renderable) {
        Check.ifNull(renderable, "renderable");

        var animationId = renderable.animationId();
        var borderThickness = PROVIDER_HANDLER.write(renderable.getBorderThicknessProvider());
        var borderColor = PROVIDER_HANDLER.write(renderable.getBorderColorProvider());

        var onPressIds = renderable.pressActionIds();
        var onPress = new FiniteAnimationRenderableDto.ButtonEvent[onPressIds.size()];
        int i = 0;
        for (var onPressId : onPressIds.entrySet()) {
            var buttonEvent = new FiniteAnimationRenderableDto.ButtonEvent();
            buttonEvent.button = onPressId.getKey();
            buttonEvent.actionId = onPressId.getValue();
            onPress[i++] = buttonEvent;
        }

        var onReleaseIds = renderable.releaseActionIds();
        var onRelease = new FiniteAnimationRenderableDto.ButtonEvent[onReleaseIds.size()];
        i = 0;
        for (var onReleaseId : onReleaseIds.entrySet()) {
            var buttonEvent = new FiniteAnimationRenderableDto.ButtonEvent();
            buttonEvent.button = onReleaseId.getKey();
            buttonEvent.actionId = onReleaseId.getValue();
            onRelease[i++] = buttonEvent;
        }

        var shifts = renderable.colorShifts();
        var writtenShifts = new String[shifts.size()];
        for (i = 0; i < shifts.size(); i++) {
            writtenShifts[i] = SHIFT_HANDLER.write(shifts.get(i));
        }

        var area = PROVIDER_HANDLER.write(renderable.getRenderingDimensionsProvider());

        var dto = new FiniteAnimationRenderableDto();
        dto.animationId = animationId;
        dto.borderThickness = borderThickness;
        dto.borderColor = borderColor;
        dto.onPress = onPress;
        dto.onRelease = onRelease;
        dto.mouseOver = renderable.mouseOverActionId();
        dto.mouseLeave = renderable.mouseLeaveActionId();
        dto.colorShifts = writtenShifts;
        dto.area = area;
        dto.z = renderable.getZ();
        dto.uuid = renderable.uuid().toString();
        dto.start = renderable.startTimestamp();
        dto.pause = renderable.pausedTimestamp();

        return JSON.toJson(dto);
    }

    public static class FiniteAnimationRenderableDto {
        public String animationId;
        public String borderThickness;
        public String borderColor;
        public ButtonEvent[] onPress;
        public ButtonEvent[] onRelease;
        public String mouseOver;
        public String mouseLeave;
        public String[] colorShifts;
        public String area;
        public int z;
        public String uuid;
        public long start;
        public Long pause;

        public static class ButtonEvent {
            public int button;
            public String actionId;
        }
    }
}
