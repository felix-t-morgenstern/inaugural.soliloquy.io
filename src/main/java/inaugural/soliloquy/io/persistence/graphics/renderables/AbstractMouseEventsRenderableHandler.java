package inaugural.soliloquy.io.persistence.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.persistence.AbstractTypeHandler;
import soliloquy.specs.common.entities.Consumer;
import soliloquy.specs.io.graphics.renderables.RenderableWithMouseEvents;
import soliloquy.specs.ui.EventInputs;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

abstract class AbstractMouseEventsRenderableHandler<TRenderable extends RenderableWithMouseEvents>
        extends AbstractTypeHandler<TRenderable> {
    @SuppressWarnings("rawtypes") protected final Function<String, Consumer> GET_CONSUMER;

    protected AbstractMouseEventsRenderableHandler(
            @SuppressWarnings("rawtypes") Function<String, Consumer> getConsumer) {
        GET_CONSUMER = Check.ifNull(getConsumer, "getConsumer");
    }

    protected void hydrateDto(Dto dto, TRenderable renderable) {
        var onPressIds = renderable.pressConsumerIds();
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

        var onReleaseIds = renderable.releaseConsumerIds();
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

        dto.mouseOver = renderable.mouseOverConsumerId();

        dto.mouseLeave = renderable.mouseLeaveConsumerId();

        dto.z = renderable.getZ();
        dto.uuid = renderable.uuid().toString();
        dto.type = renderable.getClass().getCanonicalName();
    }

    protected void hydrateReadProps(ReadProps readProps, Dto dto) {
        //noinspection unchecked
        readProps.onPress = mapOf(Arrays.stream(dto.onPress)
                .map(p -> pairOf(p.button, (Consumer<EventInputs>) GET_CONSUMER.apply(p.actionId))));
        //noinspection unchecked
        readProps.onRelease = mapOf(Arrays.stream(dto.onRelease)
                .map(p -> pairOf(p.button, (Consumer<EventInputs>) GET_CONSUMER.apply(p.actionId))));
        //noinspection unchecked
        readProps.onMouseOver = GET_CONSUMER.apply(dto.mouseOver);
        //noinspection unchecked
        readProps.onMouseLeave = GET_CONSUMER.apply(dto.mouseLeave);
    }

    protected static class ReadProps {
        Map<Integer, Consumer<EventInputs>> onPress;
        Map<Integer, Consumer<EventInputs>> onRelease;
        Consumer<EventInputs> onMouseOver;
        Consumer<EventInputs> onMouseLeave;
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
