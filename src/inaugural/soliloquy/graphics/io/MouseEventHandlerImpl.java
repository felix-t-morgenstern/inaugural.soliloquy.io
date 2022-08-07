package inaugural.soliloquy.graphics.io;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.io.MouseEventCapturingSpatialIndex;
import soliloquy.specs.graphics.io.MouseEventHandler;
import soliloquy.specs.graphics.renderables.RenderableWithArea;
import soliloquy.specs.graphics.rendering.timing.GlobalClock;

public class MouseEventHandlerImpl implements MouseEventHandler {
    private final MouseEventCapturingSpatialIndex MOUSE_EVENT_CAPTURING_SPATIAL_INDEX;
    private final GlobalClock GLOBAL_CLOCK;

    private RenderableWithArea _registeredMouseOverRenderable;

    public MouseEventHandlerImpl(MouseEventCapturingSpatialIndex mouseEventCapturingSpatialIndex,
                                 GlobalClock globalClock)
    {
        MOUSE_EVENT_CAPTURING_SPATIAL_INDEX =
                Check.ifNull(mouseEventCapturingSpatialIndex, "mouseEventCapturingSpatialIndex");
        GLOBAL_CLOCK = Check.ifNull(globalClock, "globalClock");
    }

    @Override
    public void actOnMouseLocationAndEvents(float x, float y, Integer mouseButton,
                                            EventType eventType)
            throws IllegalArgumentException {
        Check.ifNonNegative(x, "x");
        Check.ifNonNegative(y, "y");

        Check.throwOnGtValue(x, 1f, "x");
        Check.throwOnGtValue(y, 1f, "y");

        if (mouseButton != null && eventType == null) {
            throw new IllegalArgumentException(
                    "MouseEventHandlerImpl.actOnMouseLocationAndEvents: if mouseButton is " +
                            "defined, eventType must also be defined");
        }
        if (mouseButton == null && eventType != null) {
            throw new IllegalArgumentException(
                    "MouseEventHandlerImpl.actOnMouseLocationAndEvents: if eventType is " +
                            "defined, mouseButton must also be defined");
        }

        long timestamp = GLOBAL_CLOCK.globalTimestamp();

        RenderableWithArea renderableWithArea = MOUSE_EVENT_CAPTURING_SPATIAL_INDEX
                .getCapturingRenderableAtPoint(x, y, timestamp);

        if (renderableWithArea != _registeredMouseOverRenderable) {
            if (_registeredMouseOverRenderable != null) {
                _registeredMouseOverRenderable.mouseLeave(timestamp);
            }

            _registeredMouseOverRenderable = renderableWithArea;

            if (_registeredMouseOverRenderable != null) {
                _registeredMouseOverRenderable.mouseOver(timestamp);
            }
        }

        if (mouseButton != null && _registeredMouseOverRenderable != null) {
            if (eventType == EventType.PRESS) {
                _registeredMouseOverRenderable.press(mouseButton, timestamp);
            }
            if (eventType == EventType.RELEASE) {
                _registeredMouseOverRenderable.release(mouseButton, timestamp);
            }
        }
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
