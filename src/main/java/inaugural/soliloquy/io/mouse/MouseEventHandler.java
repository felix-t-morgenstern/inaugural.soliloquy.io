package inaugural.soliloquy.io.mouse;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.RenderableWithMouseEvents;
import soliloquy.specs.io.input.mouse.Mouse;

import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.collections.Collections.setOf;
import static soliloquy.specs.io.input.mouse.Mouse.EventType.PRESS;
import static soliloquy.specs.io.input.mouse.Mouse.EventType.RELEASE;

public class MouseEventHandler {
    private final TimestampValidator TIMESTAMP_VALIDATOR;
    private final BiFunction<Vertex, Long, RenderableWithMouseEvents>
            GET_CAPTURING_RENDERABLE_AT_LOC;

    private final Map<Integer, Map<Mouse.EventType, Set<Runnable>>> PUBLISH_QUEUE;

    private RenderableWithMouseEvents currentMouseOverRenderable;

    public MouseEventHandler(TimestampValidator timestampValidator,
                             BiFunction<Vertex, Long, RenderableWithMouseEvents> getCapturingRenderableAtLoc) {
        TIMESTAMP_VALIDATOR = Check.ifNull(timestampValidator, "timestampValidator");
        GET_CAPTURING_RENDERABLE_AT_LOC =
                Check.ifNull(getCapturingRenderableAtLoc, "getCapturingRenderableAtLoc");
        PUBLISH_QUEUE = mapOf();
    }

    public void actOnMouseLocationAndEvents(Vertex location,
                                            Map<Integer, Mouse.EventType> buttonEvents,
                                            long timestamp)
            throws IllegalArgumentException {
        TIMESTAMP_VALIDATOR.validateTimestamp(timestamp);

        Check.ifNonNegative(location.X, "location.X");
        Check.ifNonNegative(location.Y, "location.Y");

        Check.throwOnGtValue(location.X, 1f, "location.X");
        Check.throwOnGtValue(location.Y, 1f, "location.Y");

        Check.ifNull(buttonEvents, "buttonEvents");

        var mouseCapturingRenderable = GET_CAPTURING_RENDERABLE_AT_LOC.apply(location, timestamp);

        if (mouseCapturingRenderable != currentMouseOverRenderable) {
            if (currentMouseOverRenderable != null) {
                currentMouseOverRenderable.mouseLeave(timestamp);
            }

            currentMouseOverRenderable = mouseCapturingRenderable;

            if (currentMouseOverRenderable != null) {
                currentMouseOverRenderable.mouseOver(timestamp);
            }
        }
        buttonEvents.forEach((button, event) -> {
            Check.ifNull(button, "button type in buttonEvents");
            Check.ifNull(event, "event type in buttonEvents");

            if (PUBLISH_QUEUE.containsKey(button)) {
                PUBLISH_QUEUE.get(button).get(event).forEach(Runnable::run);
                PUBLISH_QUEUE.get(button).get(event).clear();
            }

            if (currentMouseOverRenderable != null) {
                if (event == PRESS) {
                    currentMouseOverRenderable.press(button, timestamp);
                }
                else {
                    currentMouseOverRenderable.release(button, timestamp);
                }
            }
        });
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
