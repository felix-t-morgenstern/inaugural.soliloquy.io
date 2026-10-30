package inaugural.soliloquy.io.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.common.entities.Consumer;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.RenderableWithMouseEvents;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.io.input.mouse.Mouse;
import soliloquy.specs.ui.EventInputs;

import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.Tools.defaultIfNull;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static soliloquy.specs.io.input.mouse.Mouse.EventType.*;
import static soliloquy.specs.ui.EventInputs.eventInputs;

public abstract class AbstractRenderableWithMouseEvents
        extends AbstractRenderable
        implements RenderableWithMouseEvents {
    protected final RenderingBoundaries RENDERING_BOUNDARIES;
    protected final TimestampValidator TIMESTAMP_VALIDATOR;

    private final Map<Integer, Consumer<EventInputs>> ON_PRESS;
    private final Map<Integer, Consumer<EventInputs>> ON_RELEASE;

    protected boolean capturesMouseButtonEvents;
    protected boolean capturesMouseWheelEvents;

    private Consumer<EventInputs> onWheelScroll;
    private Consumer<EventInputs> onMouseOver;
    private Consumer<EventInputs> onMouseLeave;

    protected AbstractRenderableWithMouseEvents(Map<Integer, Consumer<EventInputs>> onPress,
                                                Map<Integer, Consumer<EventInputs>> onRelease,
                                                Consumer<EventInputs> onWheelScroll,
                                                Consumer<EventInputs> onMouseOver,
                                                Consumer<EventInputs> onMouseLeave,
                                                int z,
                                                UUID uuid,
                                                Component containingComponent,
                                                RenderingBoundaries renderingBoundaries,
                                                TimestampValidator timestampValidator) {
        super(z, uuid, containingComponent);
        ON_PRESS = defaultIfNull(onPress, mapOf());
        ON_RELEASE = defaultIfNull(onRelease, mapOf());
        this.onWheelScroll = onWheelScroll;
        this.onMouseOver = onMouseOver;
        this.onMouseLeave = onMouseLeave;
        TIMESTAMP_VALIDATOR = Check.ifNull(timestampValidator, "timestampValidator");
        RENDERING_BOUNDARIES = Check.ifNull(renderingBoundaries, "renderingBoundaries");
    }

    @Override
    public boolean getCapturesMouseButtonEvents() {
        return capturesMouseButtonEvents;
    }

    @Override
    public void setCapturesMouseButtonEvents(boolean capturesMouseButtonEvents) {
        if (!underlyingAssetSupportsMouseEvents()) {
            throw new UnsupportedOperationException(className() +
                    ".setCapturesMouseButtonEvents: underlying asset does not support mouse event" +
                    " capturing");
        }

        this.capturesMouseButtonEvents = capturesMouseButtonEvents;
    }

    @Override
    public boolean getCapturesMouseWheelEvents() {
        return capturesMouseWheelEvents;
    }

    @Override
    public void setCapturesMouseWheelEvents(boolean capturesMouseWheelEvents) {
        if (!underlyingAssetSupportsMouseEvents()) {
            throw new UnsupportedOperationException(className() +
                    ".setCapturesMouseWheelEvents: underlying asset does not support mouse event " +
                    "capturing");
        }

        this.capturesMouseWheelEvents = capturesMouseWheelEvents;
    }

    /** @noinspection BooleanMethodIsAlwaysInverted */
    protected abstract boolean underlyingAssetSupportsMouseEvents();

    @Override
    public void press(int mouseButton, Vertex mouseLoc, long timestamp)
            throws UnsupportedOperationException {
        throwOnInvalidButton(mouseButton, "press");
        callConsumer(
                mouseButton,
                ON_PRESS.get(mouseButton),
                timestamp,
                PRESS,
                Check.ifNull(mouseLoc, "mouseLoc"),
                "press"
        );
    }

    @Override
    public void setOnPress(int mouseButton, Consumer<EventInputs> onPress) {
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
    public Map<Integer, String> pressConsumerIds() {
        return getConsumerIds(ON_PRESS);
    }

    @Override
    public void release(int mouseButton, Vertex mouseLoc, long timestamp)
            throws UnsupportedOperationException {
        throwOnInvalidButton(mouseButton, "release");
        callConsumer(
                mouseButton,
                ON_RELEASE.get(mouseButton),
                timestamp,
                RELEASE,
                Check.ifNull(mouseLoc, "mouseLoc"),
                "release"
        );
    }

    @Override
    public void setOnRelease(int mouseButton,
                             Consumer<EventInputs> onRelease) {
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
    public Map<Integer, String> releaseConsumerIds() {
        return getConsumerIds(ON_RELEASE);
    }

    private void throwOnInvalidButton(int button, String methodName) {
        if (button < 0 || button > 7) {
            throw new IllegalArgumentException(className() + "." + methodName +
                    ": mouseButton (" + button + ") must be between 0 and 7");
        }
    }

    private Map<Integer, String> getConsumerIds(
            Map<Integer, Consumer<EventInputs>> actions) {
        Map<Integer, String> actionIds = mapOf();
        actions.forEach((button, action) -> actionIds.put(button, action.id()));
        return actionIds;
    }

    @Override
    public void wheelScroll(float offset, Vertex mouseLoc, long timestamp) {
        throwIfNotSupportingMouseEvents("wheelScroll");
        TIMESTAMP_VALIDATOR.validateTimestamp(timestamp);
        if (onWheelScroll != null) {
            onWheelScroll.accept(
                    eventInputs(timestamp)
                            .withMouseScrollWheelEvent(
                                    mouseLoc,
                                    offset,
                                    this,
                                    this.containingComponent
                            )
            );
        }
    }

    @Override
    public void setOnWheelScroll(Consumer<EventInputs> onWheelScroll) {
        throwIfNotSupportingMouseEvents("setOnWheelScroll");
        this.onWheelScroll = onWheelScroll;
    }

    @Override
    public String wheelScrollConsumerId() {
        return actionId(onWheelScroll, "wheelScrollConsumerId");
    }

    @Override
    public void mouseOver(Vertex mouseLoc, long timestamp) throws UnsupportedOperationException {
        callConsumer(
                null,
                onMouseOver,
                timestamp,
                MOUSE_OVER,
                Check.ifNull(mouseLoc, "mouseLoc"),
                "mouseOver"
        );
    }

    @Override
    public void setOnMouseOver(Consumer<EventInputs> onMouseOver) {
        throwIfNotSupportingMouseEvents("setOnMouseOver");
        this.onMouseOver = onMouseOver;
    }

    @Override
    public String mouseOverConsumerId() {
        return actionId(onMouseOver, "mouseOverConsumerId");
    }

    @Override
    public void mouseLeave(Vertex mouseLoc, long timestamp) throws UnsupportedOperationException {
        callConsumer(
                null,
                onMouseLeave,
                timestamp,
                MOUSE_LEAVE,
                Check.ifNull(mouseLoc, "mouseLoc"),
                "mouseLeave"
        );
    }

    @Override
    public void setOnMouseLeave(Consumer<EventInputs> onMouseLeave) {
        throwIfNotSupportingMouseEvents("setOnMouseLeave");
        this.onMouseLeave = onMouseLeave;
    }

    @Override
    public String mouseLeaveConsumerId() {
        return actionId(onMouseLeave, "mouseLeaveConsumerId");
    }

    private String actionId(Consumer<EventInputs> action,
                            String methodName) {
        throwIfNotSupportingMouseEvents(methodName);
        if (action == null) {
            return null;
        }
        else {
            return action.id();
        }
    }

    private void callConsumer(Integer mouseButton,
                              Consumer<EventInputs> action,
                              long timestamp,
                              Mouse.EventType eventType,
                              Vertex mouseLoc,
                              String methodName) {
        throwIfNotSupportingMouseEvents(methodName);
        TIMESTAMP_VALIDATOR.validateTimestamp(timestamp);
        if (action != null) {
            action.accept(
                    eventInputs(timestamp)
                            .withMouseEvent(
                                    mouseButton,
                                    eventType,
                                    mouseLoc,
                                    this,
                                    this.containingComponent
                            )
            );
        }
    }

    protected void throwIfNotSupportingMouseEvents(String methodName) {
        if (!capturesMouseButtonEvents && !capturesMouseWheelEvents) {
            throw new UnsupportedOperationException(className() + "." + methodName +
                    ": mouse events not supported");
        }
    }

    abstract protected String className();
}
