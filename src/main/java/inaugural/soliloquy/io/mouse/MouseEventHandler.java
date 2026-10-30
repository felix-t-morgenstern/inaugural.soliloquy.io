package inaugural.soliloquy.io.mouse;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.RenderableWithMouseEvents;
import soliloquy.specs.io.input.mouse.Mouse;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.collections.Collections.setOf;
import static soliloquy.specs.io.input.mouse.Mouse.EventType.PRESS;
import static soliloquy.specs.io.input.mouse.Mouse.EventType.RELEASE;

public class MouseEventHandler {
    private final TimestampValidator TIMESTAMP_VALIDATOR;
    private final BiFunction<Vertex, Long, List<RenderableWithMouseEvents>>
            GET_CAPTURING_RENDERABLES_AT_LOC;

    private final Map<Integer, Map<Mouse.EventType, Set<Runnable>>> PUBLISH_QUEUE;

    private RenderableWithMouseEvents currentMouseButtonOverRenderable;

    public MouseEventHandler(TimestampValidator timestampValidator,
                             BiFunction<Vertex, Long, List<RenderableWithMouseEvents>> getCapturingRenderableAtLoc) {
        TIMESTAMP_VALIDATOR = Check.ifNull(timestampValidator, "timestampValidator");
        GET_CAPTURING_RENDERABLES_AT_LOC =
                Check.ifNull(getCapturingRenderableAtLoc, "getCapturingRenderableAtLoc");
        PUBLISH_QUEUE = mapOf();
    }

    public void actOnMouseLocationAndEvents(MouseListener.FrameMouseEvents events)
            throws IllegalArgumentException {
        TIMESTAMP_VALIDATOR.validateTimestamp(events.timestamp);

        Check.ifNonNegative(events.loc.X, "events.loc.X");
        Check.ifNonNegative(events.loc.Y, "events.loc.Y");

        Check.throwOnGtValue(events.loc.X, 1f, "events.loc.X");
        Check.throwOnGtValue(events.loc.Y, 1f, "events.loc.Y");

        Check.ifNull(events.buttonPressStates, "events.buttonPressStates");

        var mouseCapturingRenderables =
                GET_CAPTURING_RENDERABLES_AT_LOC.apply(events.loc, events.timestamp);

        var mouseButtonCapturingRenderable = mouseCapturingRenderables.stream()
                .filter(RenderableWithMouseEvents::getCapturesMouseButtonEvents)
                .findFirst().orElseGet((() -> null));

        if (mouseButtonCapturingRenderable != currentMouseButtonOverRenderable) {
            if (currentMouseButtonOverRenderable != null) {
                currentMouseButtonOverRenderable.mouseLeave(events.loc, events.timestamp);
            }

            currentMouseButtonOverRenderable = mouseButtonCapturingRenderable;

            if (currentMouseButtonOverRenderable != null) {
                currentMouseButtonOverRenderable.mouseOver(events.loc, events.timestamp);
            }
        }
        events.buttonPressStates.forEach((button, event) -> {
            Check.ifNull(button, "button type in buttonEvents");
            Check.ifNull(event, "event type in buttonEvents");

            if (PUBLISH_QUEUE.containsKey(button)) {
                PUBLISH_QUEUE.get(button).get(event).forEach(Runnable::run);
                PUBLISH_QUEUE.get(button).get(event).clear();
            }

            if (currentMouseButtonOverRenderable != null) {
                if (event == PRESS) {
                    currentMouseButtonOverRenderable.press(button, events.loc, events.timestamp);
                }
                else {
                    currentMouseButtonOverRenderable.release(button, events.loc, events.timestamp);
                }
            }
        });

        if (events.wheelOffset != 0f) {
            mouseCapturingRenderables.stream()
                    .filter(RenderableWithMouseEvents::getCapturesMouseWheelEvents).findFirst()
                    .ifPresent(r -> r.wheelScroll(
                                events.wheelOffset,
                                events.loc,
                                events.timestamp
                    ));
        }
    }

    public void subscribeToNextEvent(int button, Mouse.EventType eventType, Runnable subscriber) {
        Map<Mouse.EventType, Set<Runnable>> buttonEvents;
        if (PUBLISH_QUEUE.containsKey(button)) {
            buttonEvents = PUBLISH_QUEUE.get(button);
        }
        else {
            buttonEvents = mapOf();
            PUBLISH_QUEUE.put(button, buttonEvents);
            buttonEvents.put(PRESS, setOf());
            buttonEvents.put(RELEASE, setOf());
        }
        buttonEvents.get(eventType).add(subscriber);
    }
}
