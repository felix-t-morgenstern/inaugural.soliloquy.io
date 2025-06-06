package inaugural.soliloquy.graphics.test.unit.io;

import inaugural.soliloquy.graphics.io.MouseListenerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.io.MouseEventHandler;
import soliloquy.specs.graphics.io.MouseListener;

import static inaugural.soliloquy.graphics.api.Constants.*;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

public class MouseListenerImplTests {
    private final float X = randomFloatInRange(0f, 1f);
    private final float Y = randomFloatInRange(0f, 1f);
    private final Vertex POSITION = vertexOf(X, Y);
    private final long TIMESTAMP = randomLong();

    @Mock private MouseEventHandler mockMouseEventHandler;

    private MouseListener mouseListener;

    @BeforeEach
    public void setUp() {
        mockMouseEventHandler = mock(MouseEventHandler.class);

        mouseListener = new MouseListenerImpl(mockMouseEventHandler);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new MouseListenerImpl(null));
    }

    @Test
    public void testMouseButtonPressed() {
        mouseListener.registerMousePositionAndButtonStates(POSITION,
                mapOf(
                    pairOf(LEFT_MOUSE_BUTTON, false),
                    pairOf(RIGHT_MOUSE_BUTTON, true),
                    pairOf(MIDDLE_MOUSE_BUTTON, false)
                ), TIMESTAMP);

        verify(mockMouseEventHandler, once()).actOnMouseLocationAndEvents(eq(POSITION),
                eq(mapOf(
                    pairOf(RIGHT_MOUSE_BUTTON, MouseEventHandler.EventType.PRESS)
                )), eq(TIMESTAMP));
    }

    @Test
    public void testMouseButtonPressedOnlyOncePerStateChange() {
        mouseListener.registerMousePositionAndButtonStates(POSITION,
                mapOf(
                    pairOf(LEFT_MOUSE_BUTTON, false),
                    pairOf(RIGHT_MOUSE_BUTTON, true),
                    pairOf(MIDDLE_MOUSE_BUTTON, false)
                ), TIMESTAMP);
        mouseListener.registerMousePositionAndButtonStates(POSITION,
                mapOf(
                    pairOf(LEFT_MOUSE_BUTTON, false),
                    pairOf(RIGHT_MOUSE_BUTTON, true),
                    pairOf(MIDDLE_MOUSE_BUTTON, false)
                ), TIMESTAMP);

        verify(mockMouseEventHandler, once()).actOnMouseLocationAndEvents(eq(POSITION),
                eq(mapOf(
                    pairOf(RIGHT_MOUSE_BUTTON, MouseEventHandler.EventType.PRESS)
                )), eq(TIMESTAMP));
    }

    @Test
    public void testMouseButtonReleasedAfterPressed() {
        mouseListener.registerMousePositionAndButtonStates(POSITION,
                mapOf(
                    pairOf(LEFT_MOUSE_BUTTON, false),
                    pairOf(RIGHT_MOUSE_BUTTON, true),
                    pairOf(MIDDLE_MOUSE_BUTTON, false)
                ), TIMESTAMP);
        mouseListener.registerMousePositionAndButtonStates(POSITION,
                mapOf(
                    pairOf(LEFT_MOUSE_BUTTON, false),
                    pairOf(RIGHT_MOUSE_BUTTON, false),
                    pairOf(MIDDLE_MOUSE_BUTTON, false)
                ), TIMESTAMP);

        verify(mockMouseEventHandler, once()).actOnMouseLocationAndEvents(eq(POSITION),
                eq(mapOf(
                    pairOf(RIGHT_MOUSE_BUTTON, MouseEventHandler.EventType.RELEASE)
                )), eq(TIMESTAMP));
    }

    @Test
    public void testMouseButtonReleasedAfterPressedOnlyOncePerRelease() {
        mouseListener.registerMousePositionAndButtonStates(POSITION,
                mapOf(
                    pairOf(LEFT_MOUSE_BUTTON, false),
                    pairOf(RIGHT_MOUSE_BUTTON, true),
                    pairOf(MIDDLE_MOUSE_BUTTON, false)
                ), TIMESTAMP);
        mouseListener.registerMousePositionAndButtonStates(POSITION,
                mapOf(
                    pairOf(LEFT_MOUSE_BUTTON, false),
                    pairOf(RIGHT_MOUSE_BUTTON, false),
                    pairOf(MIDDLE_MOUSE_BUTTON, false)
                ), TIMESTAMP);
        mouseListener.registerMousePositionAndButtonStates(POSITION,
                mapOf(
                    pairOf(LEFT_MOUSE_BUTTON, false),
                    pairOf(RIGHT_MOUSE_BUTTON, false),
                    pairOf(MIDDLE_MOUSE_BUTTON, false)
                ), TIMESTAMP);

        verify(mockMouseEventHandler, once()).actOnMouseLocationAndEvents(eq(POSITION),
                eq(mapOf(
                    pairOf(RIGHT_MOUSE_BUTTON, MouseEventHandler.EventType.RELEASE)
                )), eq(TIMESTAMP));
    }

    @Test
    public void testRegisterMousePositionAndButtonStatesWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.registerMousePositionAndButtonStates(null,
                        mapOf(
                            pairOf(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(RIGHT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(MIDDLE_MOUSE_BUTTON), true)
                        ),
                        TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.registerMousePositionAndButtonStates(vertexOf(-0.0001f, 0),
                        mapOf(
                            pairOf(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(RIGHT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(MIDDLE_MOUSE_BUTTON), true)
                        ), TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.registerMousePositionAndButtonStates(vertexOf(1.0001f, 0),
                        mapOf(
                            pairOf(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(RIGHT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(MIDDLE_MOUSE_BUTTON), true)
                        ), TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.registerMousePositionAndButtonStates(vertexOf(0, -0.0001f),
                        mapOf(
                            pairOf(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(RIGHT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(MIDDLE_MOUSE_BUTTON), true)
                        ), TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.registerMousePositionAndButtonStates(vertexOf(0, 1.0001f),
                        mapOf(
                            pairOf(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(RIGHT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(MIDDLE_MOUSE_BUTTON), true)
                        ), TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.registerMousePositionAndButtonStates(vertexOf(0, 0), null,
                        TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.registerMousePositionAndButtonStates(vertexOf(0, 0),
                        mapOf(
                            pairOf(null, true),
                            pairOf(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(RIGHT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(MIDDLE_MOUSE_BUTTON), true)
                        ), TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.registerMousePositionAndButtonStates(vertexOf(0, 0),
                        mapOf(
                            pairOf(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON - 1), true),
                            pairOf(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(RIGHT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(MIDDLE_MOUSE_BUTTON), true)
                        ), TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.registerMousePositionAndButtonStates(vertexOf(0, 0),
                        mapOf(
                            pairOf(randomIntWithInclusiveFloor(MIDDLE_MOUSE_BUTTON + 1), true),
                            pairOf(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(RIGHT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(MIDDLE_MOUSE_BUTTON), true)
                        ), TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.registerMousePositionAndButtonStates(vertexOf(0, 0),
                        mapOf(
                            pairOf(LEFT_MOUSE_BUTTON, null),
                            pairOf(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(RIGHT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(MIDDLE_MOUSE_BUTTON), true)
                        ), TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.registerMousePositionAndButtonStates(vertexOf(0, 0),
                        mapOf(
                            pairOf(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(RIGHT_MOUSE_BUTTON), true)
                        ), TIMESTAMP));
    }

    @Test
    public void testThrowsOnOutdatedTimestamp() {
        mouseListener.registerMousePositionAndButtonStates(vertexOf(0, 0),
                mapOf(
                    pairOf(LEFT_MOUSE_BUTTON, false),
                    pairOf(RIGHT_MOUSE_BUTTON, false),
                    pairOf(MIDDLE_MOUSE_BUTTON, false)
                ), TIMESTAMP);

        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.registerMousePositionAndButtonStates(vertexOf(0, 0),
                        mapOf(
                            pairOf(LEFT_MOUSE_BUTTON, false),
                            pairOf(RIGHT_MOUSE_BUTTON, false),
                            pairOf(MIDDLE_MOUSE_BUTTON, false)
                        ), TIMESTAMP - 1));
    }
}
