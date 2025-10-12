package inaugural.soliloquy.io.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.RenderableWithMouseEvents;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.ui.EventInputs;

import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.io.api.Constants.LEFT_MOUSE_BUTTON;
import static inaugural.soliloquy.io.api.Constants.RIGHT_MOUSE_BUTTON;
import static inaugural.soliloquy.tools.Tools.defaultIfNull;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static soliloquy.specs.io.input.mouse.MouseEventHandler.EventType;
import static soliloquy.specs.ui.EventInputs.eventInputs;

public abstract class AbstractRenderableWithMouseEvents
        extends AbstractRenderable
        implements RenderableWithMouseEvents {
    protected final RenderingBoundaries RENDERING_BOUNDARIES;
    protected final TimestampValidator TIMESTAMP_VALIDATOR;

    private final Map<Integer, Action<EventInputs>> ON_PRESS;
    private final Map<Integer, Action<EventInputs>> ON_RELEASE;

    protected boolean capturesMouseEvents;

    private Action<EventInputs> onMouseOver;
    private Action<EventInputs> onMouseLeave;

    protected AbstractRenderableWithMouseEvents(Map<Integer, Action<EventInputs>> onPress,
                                                Map<Integer, Action<EventInputs>> onRelease,
                                                Action<EventInputs> onMouseOver,
                                                Action<EventInputs> onMouseLeave,
                                                int z,
                                                UUID uuid,
                                                Component containingComponent,
                                                RenderingBoundaries renderingBoundaries,
                                                TimestampValidator timestampValidator) {
        super(z, uuid, containingComponent);
        ON_PRESS = defaultIfNull(onPress, mapOf());
        ON_RELEASE = defaultIfNull(onRelease, mapOf());
        this.onMouseOver = onMouseOver;
        this.onMouseLeave = onMouseLeave;
        TIMESTAMP_VALIDATOR = Check.ifNull(timestampValidator, "timestampValidator");
        RENDERING_BOUNDARIES = Check.ifNull(renderingBoundaries, "renderingBoundaries");
    }

    @Override
    public boolean getCapturesMouseEvents() {
        return capturesMouseEvents;
    }

    @Override
    public void setCapturesMouseEvents(boolean capturesMouseEvents) {
        if (!underlyingAssetSupportsMouseEvents()) {
            throw new UnsupportedOperationException(className() + ".setCapturesMouseEvents: " +
                    "underlying asset does not support mouse event capturing");
        }

        this.capturesMouseEvents = capturesMouseEvents;
    }

    /** @noinspection BooleanMethodIsAlwaysInverted */
    protected abstract boolean underlyingAssetSupportsMouseEvents();

    @Override
    public void press(int mouseButton, long timestamp) throws UnsupportedOperationException {
        throwOnInvalidButton(mouseButton, "press");
        callAction(
                LEFT_MOUSE_BUTTON,
                ON_PRESS.get(mouseButton),
                timestamp,
                EventType.PRESS,
                "press"
        );
    }

    @Override
    public void setOnPress(int mouseButton, Action<EventInputs> onPress) {
        throwIfNotSupportingMouseEvents("setOnPress");
        throwOnInvalidButton(mouseButton, "setOnPress");
        if (onPress == null) {
            ON_PRESS.remove(mouseButton);
        }
        else {
            ON_PRESS.put(mouseButton, onPress);
        }
    }

    @Override
    public Map<Integer, String> pressActionIds() {
        return getActionIds(ON_PRESS);
    }

    @Override
    public void release(int mouseButton, long timestamp) throws UnsupportedOperationException {
        throwOnInvalidButton(mouseButton, "release");
        callAction(
                RIGHT_MOUSE_BUTTON,
                ON_RELEASE.get(mouseButton),
                timestamp,
                EventType.RELEASE,
                "release"
        );
    }

    @Override
    public void setOnRelease(int mouseButton,
                             Action<EventInputs> onRelease) {
        throwIfNotSupportingMouseEvents("setOnRelease");
        throwOnInvalidButton(mouseButton, "setOnRelease");
        if (onRelease == null) {
            ON_RELEASE.remove(mouseButton);
        }
        else {
            ON_RELEASE.put(mouseButton, onRelease);
        }
    }

    @Override
    public Map<Integer, String> releaseActionIds() {
        return getActionIds(ON_RELEASE);
    }

    private void throwOnInvalidButton(int button, String methodName) {
        if (button < 0 || button > 7) {
            throw new IllegalArgumentException(className() + "." + methodName +
                    ": mouseButton (" + button + ") must be between 0 and 7");
        }
    }

    private Map<Integer, String> getActionIds(
            Map<Integer, Action<EventInputs>> actions) {
        Map<Integer, String> actionIds = mapOf();
        actions.forEach((button, action) -> actionIds.put(button, action.id()));
        return actionIds;
    }

    @Override
    public void mouseOver(long timestamp) throws UnsupportedOperationException {
        callAction(
                null,
                onMouseOver,
                timestamp,
                EventType.MOUSE_OVER,
                "mouseOver"
        );
    }

    @Override
    public void setOnMouseOver(Action<EventInputs> onMouseOver) {
        throwIfNotSupportingMouseEvents("setOnMouseOver");
        this.onMouseOver = onMouseOver;
    }

    @Override
    public String mouseOverActionId() {
        return actionId(onMouseOver, "mouseOverActionId");
    }

    @Override
    public void mouseLeave(long timestamp) throws UnsupportedOperationException {
        callAction(
                null,
                onMouseLeave,
                timestamp,
                EventType.MOUSE_LEAVE,
                "mouseLeave"
        );
    }

    @Override
    public void setOnMouseLeave(Action<EventInputs> onMouseLeave) {
        throwIfNotSupportingMouseEvents("setOnMouseLeave");
        this.onMouseLeave = onMouseLeave;
    }

    @Override
    public String mouseLeaveActionId() {
        return actionId(onMouseLeave, "mouseLeaveActionId");
    }

    private String actionId(Action<EventInputs> action,
                            String methodName) {
        throwIfNotSupportingMouseEvents(methodName);
        if (action == null) {
            return null;
        }
        else {
            return action.id();
        }
    }

    private void callAction(Integer mouseButton,
                            Action<EventInputs> action,
                            long timestamp,
                            EventType eventType,
                            String methodName) {
        throwIfNotSupportingMouseEvents(methodName);
        TIMESTAMP_VALIDATOR.validateTimestamp(timestamp);
        if (action != null) {
            action.accept(
                    eventInputs(timestamp)
                            .withMouseEvent(mouseButton, eventType, this, this.containingComponent)
            );
        }
    }

    protected void throwIfNotSupportingMouseEvents(String methodName) {
        if (!capturesMouseEvents) {
            throw new UnsupportedOperationException(className() + "." + methodName +
                    ": mouse events not supported");
        }
    }

    abstract protected String className();
}
