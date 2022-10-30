package inaugural.soliloquy.graphics.io;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.io.MouseEventCapturingSpatialIndex;
import soliloquy.specs.graphics.io.MouseEventHandler;
import soliloquy.specs.graphics.renderables.RenderableWithMouseEvents;

import java.util.Map;

public class MouseEventHandlerImpl implements MouseEventHandler {
    private final TimestampValidator TIMESTAMP_VALIDATOR = new TimestampValidator(null);

    private final MouseEventCapturingSpatialIndex MOUSE_EVENT_CAPTURING_SPATIAL_INDEX;

    private RenderableWithMouseEvents registeredMouseOverRenderable;

    public MouseEventHandlerImpl(MouseEventCapturingSpatialIndex mouseEventCapturingSpatialIndex) {
        MOUSE_EVENT_CAPTURING_SPATIAL_INDEX =
                Check.ifNull(mouseEventCapturingSpatialIndex, "mouseEventCapturingSpatialIndex");
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
        // NB: I'm aware I'm looping over this map twice. This is unlikely to impact performance,
        // since buttonEvents should be tiny; also, I want to verify its validity before entering
        // the rest of the method.
        buttonEvents.forEach((button, event) -> {
            Check.ifNull(button, "button type in buttonEvents");
            Check.ifNull(event, "event type in buttonEvents");
        });

        RenderableWithMouseEvents renderable = MOUSE_EVENT_CAPTURING_SPATIAL_INDEX
                .getCapturingRenderableAtPoint(location, timestamp);

        if (renderable != registeredMouseOverRenderable) {
            if (registeredMouseOverRenderable != null) {
                registeredMouseOverRenderable.mouseLeave(timestamp);
            }

            registeredMouseOverRenderable = renderable;

            if (registeredMouseOverRenderable != null) {
                registeredMouseOverRenderable.mouseOver(timestamp);
            }
        }
        if (registeredMouseOverRenderable != null) {
            buttonEvents.forEach((button, event) -> {
                if (event == EventType.PRESS) {
                    registeredMouseOverRenderable.press(button, timestamp);
                }
                else {
                    registeredMouseOverRenderable.release(button, timestamp);
                }
            });
        }
    }

    // TODO: Test and implement this
    @Override
    public String getInterfaceName() {
        return MouseEventHandler.class.getCanonicalName();
    }
}
