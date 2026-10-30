package inaugural.soliloquy.io.test.unit.mouse;

import inaugural.soliloquy.io.bootstrap.CoreLoopImpl;
import inaugural.soliloquy.io.mouse.MouseListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.entities.Consumer;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.input.mouse.Mouse;

import static inaugural.soliloquy.io.api.Constants.*;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

@ExtendWith(MockitoExtension.class)
public class MouseListenerTests {
    private final float X = randomFloatInRange(0f, 1f);
    private final float Y = randomFloatInRange(0f, 1f);
    private final Vertex POSITION = vertexOf(X, Y);
    private final float WHEEL_OFFSET = randomFloat();
    private final long TIMESTAMP = randomLong();

    @Mock private Consumer<MouseListener.FrameMouseEvents> mockActOnMouseLocationAndEvents;

    private MouseListener mouseListener;

    @BeforeEach
    public void setUp() {
        mouseListener = new MouseListener(mockActOnMouseLocationAndEvents);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new MouseListener(null));
    }

    @Test
    public void testMouseButtonPressed() {
        mouseListener.determineMouseEventsAndAct(new CoreLoopImpl.FrameMouseState(
                POSITION,
                mapOf(
                    pairOf(LEFT_MOUSE_BUTTON, false),
                    pairOf(RIGHT_MOUSE_BUTTON, true),
                    pairOf(MIDDLE_MOUSE_BUTTON, false)
                ),
                WHEEL_OFFSET,
                TIMESTAMP));

        var eventsCaptor = ArgumentCaptor.forClass(MouseListener.FrameMouseEvents.class);
        verify(mockActOnMouseLocationAndEvents, atLeastOnce()).accept(eventsCaptor.capture());
        var pressEvent = eventsCaptor.getValue();

        assertEquals(POSITION, pressEvent.loc);
        assertEquals(mapOf(pairOf(RIGHT_MOUSE_BUTTON, Mouse.EventType.PRESS)), pressEvent.buttonPressStates);
        assertEquals(WHEEL_OFFSET, pressEvent.wheelOffset);
        assertEquals(TIMESTAMP, pressEvent.timestamp);
    }

    @Test
    public void testMouseButtonPressedOnlyOncePerStateChange() {
        mouseListener.determineMouseEventsAndAct(new CoreLoopImpl.FrameMouseState(
                POSITION,
                mapOf(
                    pairOf(LEFT_MOUSE_BUTTON, false),
                    pairOf(RIGHT_MOUSE_BUTTON, true),
                    pairOf(MIDDLE_MOUSE_BUTTON, false)
                ),
                WHEEL_OFFSET,
                TIMESTAMP
        ));
        mouseListener.determineMouseEventsAndAct(new CoreLoopImpl.FrameMouseState(
                POSITION,
                mapOf(
                    pairOf(LEFT_MOUSE_BUTTON, false),
                    pairOf(RIGHT_MOUSE_BUTTON, true),
                    pairOf(MIDDLE_MOUSE_BUTTON, false)
                ),
                WHEEL_OFFSET,
                TIMESTAMP
        ));

        var eventsCaptor = ArgumentCaptor.forClass(MouseListener.FrameMouseEvents.class);
        verify(mockActOnMouseLocationAndEvents, atLeastOnce()).accept(eventsCaptor.capture());
        var capturedEvents = eventsCaptor.getAllValues();
        var pressEvent = capturedEvents.get(0);
        var stillPressedEvent = capturedEvents.get(1);

        assertEquals(POSITION, pressEvent.loc);
        assertEquals(mapOf(pairOf(RIGHT_MOUSE_BUTTON, Mouse.EventType.PRESS)), pressEvent.buttonPressStates);
        assertEquals(WHEEL_OFFSET, pressEvent.wheelOffset);
        assertEquals(TIMESTAMP, pressEvent.timestamp);

        assertEquals(POSITION, stillPressedEvent.loc);
        assertEquals(mapOf(), stillPressedEvent.buttonPressStates);
        assertEquals(WHEEL_OFFSET, stillPressedEvent.wheelOffset);
        assertEquals(TIMESTAMP, stillPressedEvent.timestamp);
    }

    @Test
    public void testMouseButtonReleasedAfterPressed() {
        mouseListener.determineMouseEventsAndAct(new CoreLoopImpl.FrameMouseState(
                POSITION,
                mapOf(
                    pairOf(LEFT_MOUSE_BUTTON, false),
                    pairOf(RIGHT_MOUSE_BUTTON, true),
                    pairOf(MIDDLE_MOUSE_BUTTON, false)
                ),
                WHEEL_OFFSET,
                TIMESTAMP
        ));
        mouseListener.determineMouseEventsAndAct(new CoreLoopImpl.FrameMouseState(
                POSITION,
                mapOf(
                    pairOf(LEFT_MOUSE_BUTTON, false),
                    pairOf(RIGHT_MOUSE_BUTTON, false),
                    pairOf(MIDDLE_MOUSE_BUTTON, false)
                ),
                WHEEL_OFFSET,
                TIMESTAMP
        ));

        var eventsCaptor = ArgumentCaptor.forClass(MouseListener.FrameMouseEvents.class);
        verify(mockActOnMouseLocationAndEvents, atLeastOnce()).accept(eventsCaptor.capture());
        var capturedEvents = eventsCaptor.getAllValues();
        var pressEvent = capturedEvents.get(0);
        var releaseEvent = capturedEvents.get(1);

        assertEquals(POSITION, pressEvent.loc);
        assertEquals(mapOf(pairOf(RIGHT_MOUSE_BUTTON, Mouse.EventType.PRESS)), pressEvent.buttonPressStates);
        assertEquals(WHEEL_OFFSET, pressEvent.wheelOffset);
        assertEquals(TIMESTAMP, pressEvent.timestamp);

        assertEquals(POSITION, releaseEvent.loc);
        assertEquals(mapOf(pairOf(RIGHT_MOUSE_BUTTON, Mouse.EventType.RELEASE)), releaseEvent.buttonPressStates);
        assertEquals(WHEEL_OFFSET, releaseEvent.wheelOffset);
        assertEquals(TIMESTAMP, releaseEvent.timestamp);
    }

    @Test
    public void testMouseButtonReleasedAfterPressedOnlyOncePerRelease() {
        mouseListener.determineMouseEventsAndAct(new CoreLoopImpl.FrameMouseState(
                POSITION,
                mapOf(
                    pairOf(LEFT_MOUSE_BUTTON, false),
                    pairOf(RIGHT_MOUSE_BUTTON, true),
                    pairOf(MIDDLE_MOUSE_BUTTON, false)
                ),
                WHEEL_OFFSET,
                TIMESTAMP
        ));
        mouseListener.determineMouseEventsAndAct(new CoreLoopImpl.FrameMouseState(
                POSITION,
                mapOf(
                    pairOf(LEFT_MOUSE_BUTTON, false),
                    pairOf(RIGHT_MOUSE_BUTTON, false),
                    pairOf(MIDDLE_MOUSE_BUTTON, false)
                ),
                WHEEL_OFFSET,
                TIMESTAMP
        ));
        mouseListener.determineMouseEventsAndAct(new CoreLoopImpl.FrameMouseState(
                POSITION,
                mapOf(
                    pairOf(LEFT_MOUSE_BUTTON, false),
                    pairOf(RIGHT_MOUSE_BUTTON, false),
                    pairOf(MIDDLE_MOUSE_BUTTON, false)
                ),
                WHEEL_OFFSET,
                TIMESTAMP
        ));

        var eventsCaptor = ArgumentCaptor.forClass(MouseListener.FrameMouseEvents.class);
        verify(mockActOnMouseLocationAndEvents, atLeastOnce()).accept(eventsCaptor.capture());
        var capturedEvents = eventsCaptor.getAllValues();
        var pressEvent = capturedEvents.get(0);
        var releaseEvent = capturedEvents.get(1);

        assertEquals(POSITION, pressEvent.loc);
        assertEquals(mapOf(pairOf(RIGHT_MOUSE_BUTTON, Mouse.EventType.PRESS)), pressEvent.buttonPressStates);
        assertEquals(WHEEL_OFFSET, pressEvent.wheelOffset);
        assertEquals(TIMESTAMP, pressEvent.timestamp);

        assertEquals(POSITION, releaseEvent.loc);
        assertEquals(mapOf(pairOf(RIGHT_MOUSE_BUTTON, Mouse.EventType.RELEASE)), releaseEvent.buttonPressStates);
        assertEquals(WHEEL_OFFSET, releaseEvent.wheelOffset);
        assertEquals(TIMESTAMP, releaseEvent.timestamp);
    }

    @Test
    public void testDetermineMouseEventsAndActWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.determineMouseEventsAndAct(new CoreLoopImpl.FrameMouseState(
                        null,
                        mapOf(
                            pairOf(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(RIGHT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(MIDDLE_MOUSE_BUTTON), true)
                        ),
                        WHEEL_OFFSET,
                        TIMESTAMP
                )));
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.determineMouseEventsAndAct(new CoreLoopImpl.FrameMouseState(
                        vertexOf(-0.0001f, 0),
                        mapOf(
                            pairOf(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(RIGHT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(MIDDLE_MOUSE_BUTTON), true)
                        ),
                        WHEEL_OFFSET,
                        TIMESTAMP
                )));
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.determineMouseEventsAndAct(new CoreLoopImpl.FrameMouseState(
                        vertexOf(1.0001f, 0),
                        mapOf(
                            pairOf(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(RIGHT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(MIDDLE_MOUSE_BUTTON), true)
                        ),
                        WHEEL_OFFSET,
                        TIMESTAMP
                )));
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.determineMouseEventsAndAct(new CoreLoopImpl.FrameMouseState(
                        vertexOf(0, -0.0001f),
                        mapOf(
                            pairOf(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(RIGHT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(MIDDLE_MOUSE_BUTTON), true)
                        ),
                        WHEEL_OFFSET,
                        TIMESTAMP
                )));
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.determineMouseEventsAndAct(new CoreLoopImpl.FrameMouseState(
                        vertexOf(0, 1.0001f),
                        mapOf(
                            pairOf(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(RIGHT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(MIDDLE_MOUSE_BUTTON), true)
                        ),
                        WHEEL_OFFSET,
                        TIMESTAMP
                )));
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.determineMouseEventsAndAct(new CoreLoopImpl.FrameMouseState(
                        vertexOf(0, 0),
                        null,
                        WHEEL_OFFSET,
                        TIMESTAMP
                )));
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.determineMouseEventsAndAct(new CoreLoopImpl.FrameMouseState(
                        vertexOf(0, 0),
                        mapOf(
                            pairOf(null, true),
                            pairOf(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(RIGHT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(MIDDLE_MOUSE_BUTTON), true)
                        ),
                        WHEEL_OFFSET,
                        TIMESTAMP
                )));
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.determineMouseEventsAndAct(new CoreLoopImpl.FrameMouseState(
                        vertexOf(0, 0),
                        mapOf(
                            pairOf(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON - 1), true),
                            pairOf(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(RIGHT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(MIDDLE_MOUSE_BUTTON), true)
                        ),
                        WHEEL_OFFSET,
                        TIMESTAMP
                )));
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.determineMouseEventsAndAct(new CoreLoopImpl.FrameMouseState(
                        vertexOf(0, 0),
                        mapOf(
                            pairOf(randomIntWithInclusiveFloor(MIDDLE_MOUSE_BUTTON + 1), true),
                            pairOf(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(RIGHT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(MIDDLE_MOUSE_BUTTON), true)
                        ),
                        WHEEL_OFFSET,
                        TIMESTAMP
                )));
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.determineMouseEventsAndAct(new CoreLoopImpl.FrameMouseState(
                        vertexOf(0, 0),
                        mapOf(
                            pairOf(LEFT_MOUSE_BUTTON, null),
                            pairOf(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(RIGHT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(MIDDLE_MOUSE_BUTTON), true)
                        ),
                        WHEEL_OFFSET,
                        TIMESTAMP
                )));
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.determineMouseEventsAndAct(new CoreLoopImpl.FrameMouseState(
                        vertexOf(0, 0),
                        mapOf(
                            pairOf(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(RIGHT_MOUSE_BUTTON), true)
                        ),
                        WHEEL_OFFSET,
                        TIMESTAMP
                )));
    }
}
