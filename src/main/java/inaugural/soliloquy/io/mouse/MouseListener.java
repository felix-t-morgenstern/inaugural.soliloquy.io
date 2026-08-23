package inaugural.soliloquy.io.mouse;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.collections.Collections;
import org.apache.commons.lang3.function.TriConsumer;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.input.mouse.Mouse;

import java.util.Arrays;
import java.util.Map;

import static inaugural.soliloquy.io.api.Constants.ALL_SUPPORTED_MOUSE_BUTTONS;
import static inaugural.soliloquy.tools.Tools.constrain;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;
import static soliloquy.specs.io.input.mouse.Mouse.EventType.PRESS;
import static soliloquy.specs.io.input.mouse.Mouse.EventType.RELEASE;

public class MouseListener {
    private final TriConsumer<Vertex, Map<Integer, Mouse.EventType>, Long>
            ACT_ON_MOUSE_LOC_AND_EVENTS;
    private final Map<Integer, Boolean> MOUSE_BUTTON_STATES;

    public MouseListener(
            TriConsumer<Vertex, Map<Integer, Mouse.EventType>, Long> actOnMouseLocAndEvents
    ) {
        ACT_ON_MOUSE_LOC_AND_EVENTS =
                Check.ifNull(actOnMouseLocAndEvents, "actOnMouseLocAndEvents");

        MOUSE_BUTTON_STATES = mapOf();
        for (int button : ALL_SUPPORTED_MOUSE_BUTTONS) {
            MOUSE_BUTTON_STATES.put(button, false);
        }
    }

    // I'm keeping all the Check calls in here, since mouse clicks are so infrequent in terms of
    // clock cycles that the performance hit is probably negligible, but I don't have any data
    // for that
    public void determineMouseEventsAndAct(Vertex position,
                                           Map<Integer, Boolean> mouseButtonPressStates,
                                           long timestamp)
            throws IllegalArgumentException {
        //TIMESTAMP_VALIDATOR.validateTimestamp(timestamp);
        Check.ifNull(position, "position");
        Check.ifNull(mouseButtonPressStates, "mouseButtonPressStates");
        var constrainedPosition = vertexOf(
                constrain(position.X, 0f, 1f),
                constrain(position.Y, 0f, 1f)
        );
        if (mouseButtonPressStates.size() != ALL_SUPPORTED_MOUSE_BUTTONS.length) {
            throw new IllegalArgumentException(
                    "MouseListenerImpl.registerMousePositionAndButtonStates: not all supported " +
                            "mouse buttons' states reported");
        }

        var mouseButtonEvents = Collections.<Integer, Mouse.EventType>mapOf();
        mouseButtonPressStates.forEach((button, buttonIsPressedNow) -> {
            Check.ifNull(button, "button");
            if (Arrays.stream(ALL_SUPPORTED_MOUSE_BUTTONS).noneMatch(button::equals)) {
                throw new IllegalArgumentException(
                        "MouseListenerImpl.registerMousePositionAndButtonStates: unsupported " +
                                "mouse button reported (" +
                                button + ")");
            }
            Check.ifNull(buttonIsPressedNow, "buttonIsPressedNow");

            if (MOUSE_BUTTON_STATES.get(button) != buttonIsPressedNow) {
                if (buttonIsPressedNow) {
                    mouseButtonEvents.put(button, PRESS);
                }
                else {
                    mouseButtonEvents.put(button, RELEASE);
                }
                MOUSE_BUTTON_STATES.put(button, buttonIsPressedNow);
            }
        });

        ACT_ON_MOUSE_LOC_AND_EVENTS.accept(constrainedPosition, mouseButtonEvents, timestamp);
    }
}
