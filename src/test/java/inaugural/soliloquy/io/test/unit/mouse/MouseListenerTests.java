package inaugural.soliloquy.io.test.unit.mouse;

import inaugural.soliloquy.io.mouse.MouseListener;
import org.apache.commons.lang3.function.TriConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.input.mouse.Mouse;

import java.util.Map;

import static inaugural.soliloquy.io.api.Constants.*;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

@ExtendWith(MockitoExtension.class)
public class MouseListenerTests {
    private final float X = randomFloatInRange(0f, 1f);
    private final float Y = randomFloatInRange(0f, 1f);
    private final Vertex POSITION = vertexOf(X, Y);
    private final long TIMESTAMP = randomLong();

    @Mock private TriConsumer<Vertex, Map<Integer, Mouse.EventType>, Long> mockActOnMouseLocationAndEvents;

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
        mouseListener.determineMouseEventsAndAct(POSITION,
                mapOf(
                    pairOf(LEFT_MOUSE_BUTTON, false),
                    pairOf(RIGHT_MOUSE_BUTTON, true),
                    pairOf(MIDDLE_MOUSE_BUTTON, false)
                ), TIMESTAMP);

        verify(mockActOnMouseLocationAndEvents, once()).accept(
                eq(POSITION),
                eq(mapOf(
                    pairOf(RIGHT_MOUSE_BUTTON, Mouse.EventType.PRESS)
                )),
                eq(TIMESTAMP)
        );
    }

    @Test
    public void testMouseButtonPressedOnlyOncePerStateChange() {
        mouseListener.determineMouseEventsAndAct(POSITION,
                mapOf(
                    pairOf(LEFT_MOUSE_BUTTON, false),
                    pairOf(RIGHT_MOUSE_BUTTON, true),
                    pairOf(MIDDLE_MOUSE_BUTTON, false)
                ), TIMESTAMP);
        mouseListener.determineMouseEventsAndAct(POSITION,
                mapOf(
                    pairOf(LEFT_MOUSE_BUTTON, false),
                    pairOf(RIGHT_MOUSE_BUTTON, true),
                    pairOf(MIDDLE_MOUSE_BUTTON, false)
                ), TIMESTAMP);

        verify(mockActOnMouseLocationAndEvents, once()).accept(
                eq(POSITION),
                eq(mapOf(
                    pairOf(RIGHT_MOUSE_BUTTON, Mouse.EventType.PRESS)
                )),
                eq(TIMESTAMP)
        );
    }

    @Test
    public void testMouseButtonReleasedAfterPressed() {
        mouseListener.determineMouseEventsAndAct(POSITION,
                mapOf(
                    pairOf(LEFT_MOUSE_BUTTON, false),
                    pairOf(RIGHT_MOUSE_BUTTON, true),
                    pairOf(MIDDLE_MOUSE_BUTTON, false)
                ), TIMESTAMP);
        mouseListener.determineMouseEventsAndAct(POSITION,
                mapOf(
                    pairOf(LEFT_MOUSE_BUTTON, false),
                    pairOf(RIGHT_MOUSE_BUTTON, false),
                    pairOf(MIDDLE_MOUSE_BUTTON, false)
                ), TIMESTAMP);

        verify(mockActOnMouseLocationAndEvents, once()).accept(
                eq(POSITION),
                eq(mapOf(
                    pairOf(RIGHT_MOUSE_BUTTON, Mouse.EventType.RELEASE)
                )),
                eq(TIMESTAMP)
        );
    }

    @Test
    public void testMouseButtonReleasedAfterPressedOnlyOncePerRelease() {
        mouseListener.determineMouseEventsAndAct(POSITION,
                mapOf(
                    pairOf(LEFT_MOUSE_BUTTON, false),
                    pairOf(RIGHT_MOUSE_BUTTON, true),
                    pairOf(MIDDLE_MOUSE_BUTTON, false)
                ), TIMESTAMP);
        mouseListener.determineMouseEventsAndAct(POSITION,
                mapOf(
                    pairOf(LEFT_MOUSE_BUTTON, false),
                    pairOf(RIGHT_MOUSE_BUTTON, false),
                    pairOf(MIDDLE_MOUSE_BUTTON, false)
                ), TIMESTAMP);
        mouseListener.determineMouseEventsAndAct(POSITION,
                mapOf(
                    pairOf(LEFT_MOUSE_BUTTON, false),
                    pairOf(RIGHT_MOUSE_BUTTON, false),
                    pairOf(MIDDLE_MOUSE_BUTTON, false)
                ), TIMESTAMP);

        verify(mockActOnMouseLocationAndEvents, once()).accept(
                eq(POSITION),
                eq(mapOf(
                    pairOf(RIGHT_MOUSE_BUTTON, Mouse.EventType.RELEASE)
                )),
                eq(TIMESTAMP)
        );
    }

    @Test
    public void testDetermineMouseEventsAndActWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.determineMouseEventsAndAct(null,
                        mapOf(
                            pairOf(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(RIGHT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(MIDDLE_MOUSE_BUTTON), true)
                        ),
                        TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.determineMouseEventsAndAct(vertexOf(-0.0001f, 0),
                        mapOf(
                            pairOf(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(RIGHT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(MIDDLE_MOUSE_BUTTON), true)
                        ), TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.determineMouseEventsAndAct(vertexOf(1.0001f, 0),
                        mapOf(
                            pairOf(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(RIGHT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(MIDDLE_MOUSE_BUTTON), true)
                        ), TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.determineMouseEventsAndAct(vertexOf(0, -0.0001f),
                        mapOf(
                            pairOf(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(RIGHT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(MIDDLE_MOUSE_BUTTON), true)
                        ), TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.determineMouseEventsAndAct(vertexOf(0, 1.0001f),
                        mapOf(
                            pairOf(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(RIGHT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(MIDDLE_MOUSE_BUTTON), true)
                        ), TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.determineMouseEventsAndAct(vertexOf(0, 0), null,
                        TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.determineMouseEventsAndAct(vertexOf(0, 0),
                        mapOf(
                            pairOf(null, true),
                            pairOf(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(RIGHT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(MIDDLE_MOUSE_BUTTON), true)
                        ), TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.determineMouseEventsAndAct(vertexOf(0, 0),
                        mapOf(
                            pairOf(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON - 1), true),
                            pairOf(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(RIGHT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(MIDDLE_MOUSE_BUTTON), true)
                        ), TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.determineMouseEventsAndAct(vertexOf(0, 0),
                        mapOf(
                            pairOf(randomIntWithInclusiveFloor(MIDDLE_MOUSE_BUTTON + 1), true),
                            pairOf(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(RIGHT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(MIDDLE_MOUSE_BUTTON), true)
                        ), TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.determineMouseEventsAndAct(vertexOf(0, 0),
                        mapOf(
                            pairOf(LEFT_MOUSE_BUTTON, null),
                            pairOf(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(RIGHT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(MIDDLE_MOUSE_BUTTON), true)
                        ), TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> mouseListener.determineMouseEventsAndAct(vertexOf(0, 0),
                        mapOf(
                            pairOf(randomIntWithInclusiveFloor(LEFT_MOUSE_BUTTON), true),
                            pairOf(randomIntWithInclusiveFloor(RIGHT_MOUSE_BUTTON), true)
                        ), TIMESTAMP));
    }
}
