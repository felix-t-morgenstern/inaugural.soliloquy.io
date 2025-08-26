package inaugural.soliloquy.io.persistence.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.persistence.AbstractTypeHandler;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.io.graphics.renderables.RenderableWithMouseEvents;
import soliloquy.specs.ui.EventInputs;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

public abstract class AbstractMouseEventsRenderableHandler<TRenderable extends RenderableWithMouseEvents>
        extends AbstractTypeHandler<TRenderable> {
    @SuppressWarnings("rawtypes") protected final Function<String, Action> GET_ACTION;

    protected AbstractMouseEventsRenderableHandler(
            @SuppressWarnings("rawtypes") Function<String, Action> getAction) {
        GET_ACTION = Check.ifNull(getAction, "getAction");
    }

    protected void hydrateDto(Dto dto, TRenderable renderable) {
        var onPressIds = renderable.pressActionIds();
        var onPress =
                new FiniteAnimationRenderableHandler.FiniteAnimationRenderableDto.ButtonEvent[onPressIds.size()];
        int i = 0;
        for (var onPressId : onPressIds.entrySet()) {
            var buttonEvent =
                    new FiniteAnimationRenderableHandler.FiniteAnimationRenderableDto.ButtonEvent();
            buttonEvent.button = onPressId.getKey();
            buttonEvent.actionId = onPressId.getValue();
            onPress[i++] = buttonEvent;
        }
        dto.onPress = onPress;

        var onReleaseIds = renderable.releaseActionIds();
        var onRelease =
                new FiniteAnimationRenderableHandler.FiniteAnimationRenderableDto.ButtonEvent[onReleaseIds.size()];
        i = 0;
        for (var onReleaseId : onReleaseIds.entrySet()) {
            var buttonEvent =
                    new FiniteAnimationRenderableHandler.FiniteAnimationRenderableDto.ButtonEvent();
            buttonEvent.button = onReleaseId.getKey();
            buttonEvent.actionId = onReleaseId.getValue();
            onRelease[i++] = buttonEvent;
        }
        dto.onRelease = onRelease;

        dto.mouseOver = renderable.mouseOverActionId();

        dto.mouseLeave = renderable.mouseLeaveActionId();

        dto.z = renderable.getZ();
        dto.uuid = renderable.uuid().toString();
        dto.type = renderable.getClass().getCanonicalName();
    }

    protected void hydrateReadProps(ReadProps readProps, Dto dto) {
        //noinspection unchecked
        readProps.onPress = mapOf(Arrays.stream(dto.onPress)
                .map(p -> pairOf(p.button, (Action<EventInputs>) GET_ACTION.apply(p.actionId))));
        //noinspection unchecked
        readProps.onRelease = mapOf(Arrays.stream(dto.onRelease)
                .map(p -> pairOf(p.button, (Action<EventInputs>) GET_ACTION.apply(p.actionId))));
        //noinspection unchecked
        readProps.onMouseOver = GET_ACTION.apply(dto.mouseOver);
        //noinspection unchecked
        readProps.onMouseLeave = GET_ACTION.apply(dto.mouseLeave);
    }

    protected static class ReadProps {
        Map<Integer, Action<EventInputs>> onPress;
        Map<Integer, Action<EventInputs>> onRelease;
        Action<EventInputs> onMouseOver;
        Action<EventInputs> onMouseLeave;
    }

    protected static class Dto {
        public ButtonEvent[] onPress;
        public ButtonEvent[] onRelease;
        public String mouseOver;
        public String mouseLeave;
        public int z;
        public String uuid;
        public String type;

        protected static class ButtonEvent {
            public int button;
            public String actionId;
        }
    }
}
