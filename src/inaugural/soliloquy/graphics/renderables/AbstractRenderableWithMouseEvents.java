package inaugural.soliloquy.graphics.renderables;

import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.graphics.renderables.RenderableWithMouseEvents;
import soliloquy.specs.graphics.rendering.RenderableStack;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractRenderableWithMouseEvents
        extends AbstractRenderable
        implements RenderableWithMouseEvents {
    protected final TimestampValidator TIMESTAMP_VALIDATOR;

    private final Map<Integer, Action<Long>> ON_PRESS;
    private final Map<Integer, Action<Long>> ON_RELEASE;

    protected boolean _capturesMouseEvents;

    private Action<Long> _onMouseOver;
    private Action<Long> _onMouseLeave;

    protected AbstractRenderableWithMouseEvents(boolean capturesMouseEvents,
                                                Map<Integer, Action<Long>> onPress,
                                                Map<Integer, Action<Long>> onRelease,
                                                Action<Long> onMouseOver,
                                                Action<Long> onMouseLeave,
                                                int z,
                                                java.util.UUID uuid,
                                                RenderableStack containingStack) {
        super(z, uuid, containingStack);
        _capturesMouseEvents = capturesMouseEvents;
        ON_PRESS = onPress == null ? new HashMap<>() : onPress;
        ON_RELEASE = onRelease == null ? new HashMap<>() : onRelease;
        _onMouseOver = onMouseOver;
        _onMouseLeave = onMouseLeave;
        TIMESTAMP_VALIDATOR = new TimestampValidator(null);
    }

    protected void throwInConstructorIfFedUnderlyingAssetThatDoesNotSupport() {
        if (!underlyingAssetSupportsMouseEvents()) {
            throw new IllegalArgumentException(className() +
                    ": underlying asset does not support capturing mouse events");
        }
    }

    @Override
    public boolean getCapturesMouseEvents() {
        return _capturesMouseEvents;
    }

    @Override
    public void setCapturesMouseEvents(boolean capturesMouseEvents) {
        if (!underlyingAssetSupportsMouseEvents()) {
            throw new UnsupportedOperationException(className() + ".setCapturesMouseEvents: " +
                    "underlying asset does not support mouse event capturing");
        }

        _capturesMouseEvents = capturesMouseEvents;
    }

    /** @noinspection BooleanMethodIsAlwaysInverted */
    protected abstract boolean underlyingAssetSupportsMouseEvents();

    @Override
    public void press(int mouseButton, long timestamp) throws UnsupportedOperationException {
        throwOnInvalidButton(mouseButton, "press");
        callAction(ON_PRESS.get(mouseButton), timestamp, "press");
    }

    @Override
    public void setOnPress(int mouseButton, Action<Long> onPress) {
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
        callAction(ON_RELEASE.get(mouseButton), timestamp, "release");
    }

    @Override
    public void setOnRelease(int mouseButton, Action<Long> onRelease) {
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

    private Map<Integer, String> getActionIds(Map<Integer, Action<Long>> actions) {
        HashMap<Integer, String> actionIds = new HashMap<>();
        actions.forEach((button, action) -> actionIds.put(button, action.id()));
        return actionIds;
    }

    @Override
    public void mouseOver(long timestamp) throws UnsupportedOperationException {
        callAction(_onMouseOver, timestamp, "mouseOver");
    }

    @Override
    public void setOnMouseOver(Action<Long> onMouseOver) {
        throwIfNotSupportingMouseEvents("setOnMouseOver");
        _onMouseOver = onMouseOver;
    }

    @Override
    public String mouseOverActionId() {
        return actionId(_onMouseOver, "mouseOverActionId");
    }

    @Override
    public void mouseLeave(long timestamp) throws UnsupportedOperationException {
        callAction(_onMouseLeave, timestamp, "mouseLeave");
    }

    @Override
    public void setOnMouseLeave(Action<Long> onMouseLeave) {
        throwIfNotSupportingMouseEvents("setOnMouseLeave");
        _onMouseLeave = onMouseLeave;
    }

    @Override
    public String mouseLeaveActionId() {
        return actionId(_onMouseLeave, "mouseLeaveActionId");
    }

    private String actionId(Action<Long> action, String methodName) {
        throwIfNotSupportingMouseEvents(methodName);
        if (action == null) {
            return null;
        }
        else {
            return action.id();
        }
    }

    private void callAction(Action<Long> action, long timestamp, String methodName) {
        throwIfNotSupportingMouseEvents(methodName);
        TIMESTAMP_VALIDATOR.validateTimestamp(timestamp);
        if (action != null) {
            action.run(timestamp);
        }
    }

    protected void throwIfNotSupportingMouseEvents(String methodName) {
        if (!_capturesMouseEvents) {
            throw new UnsupportedOperationException(className() + "." + methodName +
                    ": mouse events not supported");
        }
    }

    abstract protected String className();
}
