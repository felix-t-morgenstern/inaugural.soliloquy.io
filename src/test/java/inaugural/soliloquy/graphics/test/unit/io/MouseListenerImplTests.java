package inaugural.soliloquy.graphics.test.unit.io;

import inaugural.soliloquy.graphics.io.MouseListenerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.io.MouseEventHandler;
import soliloquy.specs.graphics.io.MouseListener;

import java.util.HashMap;

import static inaugural.soliloquy.graphics.api.Constants.*;
import static inaugural.soliloquy.tools.random.Random.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class MouseListenerImplTests {
    private final float X = randomFloatInRange(0f, 1f);
    private final float Y = randomFloatInRange(0f, 1f);
    private final Vertex POSITION = Vertex.of(X, Y);
    private final long TIMESTAMP = randomLong();

    @Mock private MouseEventHandler mockMouseEventHandler;

    private MouseListener mouseListener;

    @BeforeEach
    void setUp() {
        mockMouseEventHandler = mock(MouseEventHandler.class);

        mouseListener = new MouseListenerImpl(mockMouseEventHandler);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> new MouseListenerImpl(null));
    }

    @Test
    void testMouseButtonPressed() {
        mouseListener.registerMousePositionAndButtonStates(POSITION,
                new HashMap<Integer, Boolean>() {{
                    put(LEFT_MOUSE_BUTTON, false);
                    put(RIGHT_MOUSE_BUTTON, true);
                    put(MIDDLE_MOUSE_BUTTON, false);
                }}, TIMESTAMP);

        verify(mockMouseEventHandler, times(1)).actOnMouseLocationAndEvents(eq(POSITION),
                eq(new HashMap<Integer, MouseEventHandler.EventType>() {{
                    put(RIGHT_MOUSE_BUTTON, MouseEventHandler.EventType.PRESS);
                }}), eq(TIMESTAMP));
    }

    @Test
    void testMouseButtonPressedOnlyOncePerStateChange() {
        mouseListener.registerMousePositionAndButtonStates(POSITION,
                new HashMap<Integer, Boolean>() {{
                    put(LEFT_MOUSE_BUTTON, false);
                    put(RIGHT_MOUSE_BUTTON, true);
                    put(MIDDLE_MOUSE_BUTTON, false);
                }}, TIMESTAMP);
        mouseListener.registerMousePositionAndButtonStates(POSITION,
                new HashMap<Integer, Boolean>() {{
                    put(LEFT_MOUSE_BUTTON, false);
                    put(RIGHT_MOUSE_BUTTON, true);
                    put(MIDDLE_MOUSE_BUTTON, false);
                }}, TIMESTAMP);

        verify(mockMouseEventHandler, times(1)).actOnMouseLocationAndEvents(eq(POSITION),
                eq(new HashMap<Integer, MouseEventHandler.EventType>() {{
                    put(RIGHT_MOUSE_BUTTON, MouseEventHandler.EventType.PRESS);
                }}), eq(TIMESTAMP));
    }

    @Test
    void testMouseButtonReleasedAfterPressed() {
        mouseListener.registerMousePositionAndButtonStates(POSITION,
                new HashMap<Integer, Boolean>() {{
                    put(LEFT_MOUSE_BUTTON, false);
                    put(RIGHT_MOUSE_BUTTON, true);
                    put(MIDDLE_MOUSE_BUTTON, false);
                }}, TIMESTAMP);
        mouseListener.registerMousePositionAndButtonStates(POSITION,
                new HashMap<Integer, Boolean>() {{
                    put(LEFT_MOUSE_BUTTON, false);
                    put(RIGHT_MOUSE_BUTTON, false);
                    put(MIDDLE_MOUSE_BUTTON, false);
                }}, TIMESTAMP);

        verify(mockMouseEventHandler, times(1)).actOnMouseLocationAndEvents(eq(POSITION),
                eq(new HashMap<Integer, MouseEventHandler.EventType>() {{
                    put(RIGHT_MOUSE_BUTTON, MouseEventHandler.EventType.RELEASE);
                }}), eq(TIMESTAMP));
    }

    @Test
    void testMouseButtonReleasedAfterPressedOnlyOncePerRelease() {
        mouseListener.registerMousePositionAndButtonStates(POSITION,
                new HashMap<Integer, Boolean>() {{
                    put(LEFT_MOUSE_BUTTON, false);
                    put(RIGHT_MOUSE_BUTTON, true);
                    put(MIDDLE_MOUSE_BUTTON, false);
                }}, TIMESTAMP);
        mouseListener.registerMousePositionAndButtonStates(POSITION,
                new HashMap<Integer, Boolean>() {{
                    put(LEFT_MOUSE_BUTTON, false);
                    put(RIGHT_MOUSE_BUTTON, false);
                    put(MIDDLE_MOUSE_BUTTON, false);
                }}, TIMESTAMP);
        mouseListener.registerMousePositionAndButtonStates(POSITION,
                new HashMap<Integer, Boolean>() {{
                    put(LEFT_MOUSE_BUTTON, false);
                    put(RIGHT_MOUSE_BUTTON, false);
                    put(MIDDLE_MOUSE_BUTTON, false);
                }}, TIMESTAMP);

        verify(mockMouseEventHandler, times(1)).actOnMouseLocationAndEvents(eq(POSITION),
                eq(new HashMap<Integer, MouseEventHandler.EventType>() {{
                    put(RIGHT_MOUSE_BUTTON, MouseEventHandler.EventType.RELEASE);
                }}), eq(TIMESTAMP));
    }

    @Test
    void testRegisterMousePositionAndButtonStatesWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.registerMousePositionAndButtonStates(null,
                        new HashMap<Integer, Boolean>() {{
                            put(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON), true);
                            put(randomIntWithInclusiveFloor(RIGHT_MOUSE_BUTTON), true);
                            put(randomIntWithInclusiveFloor(MIDDLE_MOUSE_BUTTON), true);
                        }},
                        TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.registerMousePositionAndButtonStates(Vertex.of(-0.0001f, 0),
                        new HashMap<Integer, Boolean>() {{
                            put(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON), true);
                            put(randomIntWithInclusiveFloor(RIGHT_MOUSE_BUTTON), true);
                            put(randomIntWithInclusiveFloor(MIDDLE_MOUSE_BUTTON), true);
                        }}, TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.registerMousePositionAndButtonStates(Vertex.of(1.0001f, 0),
                        new HashMap<Integer, Boolean>() {{
                            put(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON), true);
                            put(randomIntWithInclusiveFloor(RIGHT_MOUSE_BUTTON), true);
                            put(randomIntWithInclusiveFloor(MIDDLE_MOUSE_BUTTON), true);
                        }}, TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.registerMousePositionAndButtonStates(Vertex.of(0, -0.0001f),
                        new HashMap<Integer, Boolean>() {{
                            put(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON), true);
                            put(randomIntWithInclusiveFloor(RIGHT_MOUSE_BUTTON), true);
                            put(randomIntWithInclusiveFloor(MIDDLE_MOUSE_BUTTON), true);
                        }}, TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.registerMousePositionAndButtonStates(Vertex.of(0, 1.0001f),
                        new HashMap<Integer, Boolean>() {{
                            put(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON), true);
                            put(randomIntWithInclusiveFloor(RIGHT_MOUSE_BUTTON), true);
                            put(randomIntWithInclusiveFloor(MIDDLE_MOUSE_BUTTON), true);
                        }}, TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.registerMousePositionAndButtonStates(Vertex.of(0, 0), null,
                        TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.registerMousePositionAndButtonStates(Vertex.of(0, 0),
                        new HashMap<Integer, Boolean>() {{
                            put(null, true);
                            put(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON), true);
                            put(randomIntWithInclusiveFloor(RIGHT_MOUSE_BUTTON), true);
                            put(randomIntWithInclusiveFloor(MIDDLE_MOUSE_BUTTON), true);
                        }}, TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.registerMousePositionAndButtonStates(Vertex.of(0, 0),
                        new HashMap<Integer, Boolean>() {{
                            put(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON - 1), true);
                            put(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON), true);
                            put(randomIntWithInclusiveFloor(RIGHT_MOUSE_BUTTON), true);
                            put(randomIntWithInclusiveFloor(MIDDLE_MOUSE_BUTTON), true);
                        }}, TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.registerMousePositionAndButtonStates(Vertex.of(0, 0),
                        new HashMap<Integer, Boolean>() {{
                            put(randomIntWithInclusiveFloor(MIDDLE_MOUSE_BUTTON + 1), true);
                            put(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON), true);
                            put(randomIntWithInclusiveFloor(RIGHT_MOUSE_BUTTON), true);
                            put(randomIntWithInclusiveFloor(MIDDLE_MOUSE_BUTTON), true);
                        }}, TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.registerMousePositionAndButtonStates(Vertex.of(0, 0),
                        new HashMap<Integer, Boolean>() {{
                            put(LEFT_MOUSE_BUTTON, null);
                            put(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON), true);
                            put(randomIntWithInclusiveFloor(RIGHT_MOUSE_BUTTON), true);
                            put(randomIntWithInclusiveFloor(MIDDLE_MOUSE_BUTTON), true);
                        }}, TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.registerMousePositionAndButtonStates(Vertex.of(0, 0),
                        new HashMap<Integer, Boolean>() {{
                            put(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON), true);
                            put(randomIntWithInclusiveFloor(RIGHT_MOUSE_BUTTON), true);
                        }}, TIMESTAMP));
    }

    @Test
    void testThrowsOnOutdatedTimestamp() {
        mouseListener.registerMousePositionAndButtonStates(Vertex.of(0, 0),
                new HashMap<Integer, Boolean>() {{
                    put(LEFT_MOUSE_BUTTON, false);
                    put(RIGHT_MOUSE_BUTTON, false);
                    put(MIDDLE_MOUSE_BUTTON, false);
                }}, TIMESTAMP);

        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.registerMousePositionAndButtonStates(Vertex.of(0, 0),
                        new HashMap<Integer, Boolean>() {{
                            put(LEFT_MOUSE_BUTTON, false);
                            put(RIGHT_MOUSE_BUTTON, false);
                            put(MIDDLE_MOUSE_BUTTON, false);
                        }}, TIMESTAMP - 1));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(MouseListener.class.getCanonicalName(), mouseListener.getInterfaceName());
    }
}
