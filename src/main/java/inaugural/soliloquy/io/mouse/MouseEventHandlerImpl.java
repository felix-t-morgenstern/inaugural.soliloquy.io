package inaugural.soliloquy.io.mouse;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.input.mouse.MouseEventCapturingSpatialIndex;
import soliloquy.specs.io.input.mouse.MouseEventHandler;
import soliloquy.specs.io.graphics.renderables.RenderableWithMouseEvents;

import java.util.Map;
import java.util.Set;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.collections.Collections.setOf;

public class MouseEventHandlerImpl implements MouseEventHandler {
    private final TimestampValidator TIMESTAMP_VALIDATOR = new TimestampValidator(null);

    private final MouseEventCapturingSpatialIndex MOUSE_EVENT_CAPTURING_SPATIAL_INDEX;

    private final Map<Integer, Map<EventType, Set<Runnable>>> PUBLISH_QUEUE;

    private RenderableWithMouseEvents currentMouseOverRenderable;

    public MouseEventHandlerImpl(MouseEventCapturingSpatialIndex mouseEventCapturingSpatialIndex) {
        MOUSE_EVENT_CAPTURING_SPATIAL_INDEX =
                Check.ifNull(mouseEventCapturingSpatialIndex, "mouseEventCapturingSpatialIndex");
        PUBLISH_QUEUE = mapOf();
    }

    @Override
    public void actOnMouseLocationAndEvents(Vertex location, Map<Integer, EventType> buttonEvents,
                                            long timestamp)
            throws IllegalArgumentException {
        TIMESTAMP_VALIDATOR.validateTimestamp(timestamp);

        Check.ifNonNegative(location.X, "location.X");
        Check.ifNonNegative(location.Y, "location.Y");

        Check.throwOnGtValue(location.X, 1f, "location.X");
        Check.throwOnGtValue(location.Y, 1f, "location.Y");

        Check.ifNull(buttonEvents, "buttonEvents");

        var mouseCapturingRenderable = MOUSE_EVENT_CAPTURING_SPATIAL_INDEX
                .getCapturingRenderableAtPoint(location, timestamp);

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
                if (event == EventType.PRESS) {
                    currentMouseOverRenderable.press(button, timestamp);
                }
                else {
                    currentMouseOverRenderable.release(button, timestamp);
                }
            }
        });
    }

    public void subscribeToNextEvent(int button, EventType eventType, Runnable subscriber) {
        Map<EventType, Set<Runnable>> buttonEvents;
        if (PUBLISH_QUEUE.containsKey(button)) {
            buttonEvents = PUBLISH_QUEUE.get(button);
        }
        else {
            buttonEvents = mapOf();
            PUBLISH_QUEUE.put(button, buttonEvents);
            buttonEvents.put(EventType.PRESS, setOf());
            buttonEvents.put(EventType.RELEASE, setOf());
        }
        buttonEvents.get(eventType).add(subscriber);
    }
}
