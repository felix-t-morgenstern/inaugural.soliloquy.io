package inaugural.soliloquy.graphics.test.unit.io;

import inaugural.soliloquy.graphics.io.MouseEventHandlerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.io.MouseEventCapturingSpatialIndex;
import soliloquy.specs.graphics.io.MouseEventHandler;
import soliloquy.specs.graphics.renderables.ImageAssetRenderable;

import java.util.HashMap;

import static inaugural.soliloquy.graphics.api.Constants.LEFT_MOUSE_BUTTON;
import static inaugural.soliloquy.tools.random.Random.randomFloatInRange;
import static inaugural.soliloquy.tools.random.Random.randomLong;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class MouseEventHandlerImplTests {
    private final Vertex POSITION =
            Vertex.of(randomFloatInRange(0f, 1f), randomFloatInRange(0f, 1f));
    private final long TIMESTAMP = randomLong();

    @Mock private MouseEventCapturingSpatialIndex mockMouseEventCapturingSpatialIndex;
    @Mock private ImageAssetRenderable mockImageAssetRenderable;
    @Mock private ImageAssetRenderable mockImageAssetRenderable2;

    private MouseEventHandler mouseEventHandler;

    @BeforeEach
    void setUp() {
        mockImageAssetRenderable = mock(ImageAssetRenderable.class);
        mockImageAssetRenderable2 = mock(ImageAssetRenderable.class);

        mockMouseEventCapturingSpatialIndex = mock(MouseEventCapturingSpatialIndex.class);
        when(mockMouseEventCapturingSpatialIndex
                .getCapturingRenderableAtPoint(any(), anyLong()))
                .thenReturn(mockImageAssetRenderable);

        mouseEventHandler =
                new MouseEventHandlerImpl(mockMouseEventCapturingSpatialIndex);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> new MouseEventHandlerImpl(null));
    }

    @Test
    void testMouseOver() {
        mouseEventHandler.actOnMouseLocationAndEvents(POSITION, new HashMap<>(), TIMESTAMP);

        verify(mockMouseEventCapturingSpatialIndex).getCapturingRenderableAtPoint(POSITION,
                TIMESTAMP);
        verify(mockImageAssetRenderable).mouseOver(TIMESTAMP);
    }

    @Test
    void testMouseOverOnlyOnce() {
        mouseEventHandler.actOnMouseLocationAndEvents(POSITION, new HashMap<>(), TIMESTAMP);
        mouseEventHandler.actOnMouseLocationAndEvents(POSITION, new HashMap<>(),
                TIMESTAMP + 1);

        verify(mockMouseEventCapturingSpatialIndex, times(1))
                .getCapturingRenderableAtPoint(POSITION, TIMESTAMP);
        verify(mockMouseEventCapturingSpatialIndex, times(1))
                .getCapturingRenderableAtPoint(POSITION, TIMESTAMP + 1);
        verify(mockImageAssetRenderable, times(1)).mouseOver(TIMESTAMP);
        verify(mockImageAssetRenderable, never()).mouseOver(TIMESTAMP + 1);
    }

    @Test
    void testMouseLeave() {
        when(mockMouseEventCapturingSpatialIndex
                .getCapturingRenderableAtPoint(any(), anyLong()))
                .thenReturn(mockImageAssetRenderable)
                .thenReturn(null);

        mouseEventHandler.actOnMouseLocationAndEvents(POSITION, new HashMap<>(), TIMESTAMP);
        mouseEventHandler.actOnMouseLocationAndEvents(POSITION, new HashMap<>(),
                TIMESTAMP + 1);

        verify(mockImageAssetRenderable).mouseOver(TIMESTAMP);
        verify(mockImageAssetRenderable).mouseLeave(TIMESTAMP + 1);
    }

    @Test
    void testMouseLeaveAndMouseOverNewRenderable() {
        when(mockMouseEventCapturingSpatialIndex
                .getCapturingRenderableAtPoint(any(), anyLong()))
                .thenReturn(mockImageAssetRenderable)
                .thenReturn(mockImageAssetRenderable2);

        mouseEventHandler.actOnMouseLocationAndEvents(POSITION, new HashMap<>(), TIMESTAMP);
        mouseEventHandler.actOnMouseLocationAndEvents(POSITION, new HashMap<>(),
                TIMESTAMP + 1);

        verify(mockImageAssetRenderable).mouseOver(TIMESTAMP);
        verify(mockImageAssetRenderable).mouseLeave(TIMESTAMP + 1);
        verify(mockImageAssetRenderable2).mouseOver(TIMESTAMP + 1);
    }

    @Test
    void testPressButtonOnRenderable() {
        mouseEventHandler.actOnMouseLocationAndEvents(POSITION,
                new HashMap<Integer, MouseEventHandler.EventType>() {{
                    put(LEFT_MOUSE_BUTTON, MouseEventHandler.EventType.PRESS);
                }}, TIMESTAMP);

        verify(mockImageAssetRenderable)
                .press(LEFT_MOUSE_BUTTON, TIMESTAMP);
    }

    @Test
    void testPressButtonAfterMouseLeaveToNoRenderable() {
        when(mockMouseEventCapturingSpatialIndex
                .getCapturingRenderableAtPoint(any(), anyLong()))
                .thenReturn(mockImageAssetRenderable)
                .thenReturn(null);

        mouseEventHandler.actOnMouseLocationAndEvents(POSITION, new HashMap<>(), TIMESTAMP);
        mouseEventHandler.actOnMouseLocationAndEvents(POSITION,
                new HashMap<Integer, MouseEventHandler.EventType>() {{
                    put(LEFT_MOUSE_BUTTON, MouseEventHandler.EventType.PRESS);
                }}, TIMESTAMP);

        verify(mockImageAssetRenderable, never())
                .press(anyInt(), anyLong());
    }

    @Test
    void testReleaseButtonOnRenderable() {
        mouseEventHandler.actOnMouseLocationAndEvents(POSITION,
                new HashMap<Integer, MouseEventHandler.EventType>() {{
                    put(LEFT_MOUSE_BUTTON, MouseEventHandler.EventType.RELEASE);
                }}, TIMESTAMP);

        verify(mockImageAssetRenderable)
                .release(LEFT_MOUSE_BUTTON, TIMESTAMP);
    }

    @Test
    void testReleaseButtonAfterMouseLeaveToNoRenderable() {
        when(mockMouseEventCapturingSpatialIndex
                .getCapturingRenderableAtPoint(any(), anyLong()))
                .thenReturn(mockImageAssetRenderable)
                .thenReturn(null);

        mouseEventHandler.actOnMouseLocationAndEvents(POSITION, new HashMap<>(), TIMESTAMP);
        mouseEventHandler.actOnMouseLocationAndEvents(POSITION,
                new HashMap<Integer, MouseEventHandler.EventType>() {{
                    put(LEFT_MOUSE_BUTTON, MouseEventHandler.EventType.RELEASE);
                }}, TIMESTAMP);

        verify(mockImageAssetRenderable, never())
                .release(anyInt(), anyLong());
    }

    @Test
    void testMouseButtonEventsAreTriggeredWhenRenderableUnderMouseDoesNotChange() {
        when(mockMouseEventCapturingSpatialIndex
                .getCapturingRenderableAtPoint(any(), anyLong()))
                .thenReturn(mockImageAssetRenderable);

        mouseEventHandler.actOnMouseLocationAndEvents(POSITION, new HashMap<>(), TIMESTAMP);
        mouseEventHandler.actOnMouseLocationAndEvents(POSITION,
                new HashMap<Integer, MouseEventHandler.EventType>() {{
                    put(LEFT_MOUSE_BUTTON, MouseEventHandler.EventType.PRESS);
                }}, TIMESTAMP);

        verify(mockImageAssetRenderable, times(1)).press(LEFT_MOUSE_BUTTON, TIMESTAMP);
    }

    @Test
    void testActOnMouseLocationAndEventsWithInvalidInputs() {
        assertThrows(IllegalArgumentException.class, () ->
                mouseEventHandler.actOnMouseLocationAndEvents(Vertex.of(-0.0001f, 0),
                        new HashMap<>(),
                        TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                mouseEventHandler.actOnMouseLocationAndEvents(Vertex.of(0, -0.0001f),
                        new HashMap<>(),
                        TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                mouseEventHandler.actOnMouseLocationAndEvents(Vertex.of(1.0001f, 0),
                        new HashMap<>(),
                        TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                mouseEventHandler.actOnMouseLocationAndEvents(Vertex.of(0, 1.0001f),
                        new HashMap<>(),
                        TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                mouseEventHandler.actOnMouseLocationAndEvents(POSITION, null, TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                mouseEventHandler.actOnMouseLocationAndEvents(POSITION,
                        new HashMap<Integer, MouseEventHandler.EventType>() {{
                            put(0, null);
                        }}, TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                mouseEventHandler.actOnMouseLocationAndEvents(POSITION,
                        new HashMap<Integer, MouseEventHandler.EventType>() {{
                            put(null, MouseEventHandler.EventType.PRESS);
                        }}, TIMESTAMP));
    }

    @Test
    void testActOnMouseLocationAndEventsWithOutOfDateTimestamp() {
        mouseEventHandler.actOnMouseLocationAndEvents(Vertex.of(0f, 0f), new HashMap<>(),
                TIMESTAMP);

        assertThrows(IllegalArgumentException.class,
                () -> mouseEventHandler.actOnMouseLocationAndEvents(Vertex.of(0f, 0f),
                        new HashMap<>(), TIMESTAMP - 1));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(MouseEventHandler.class.getCanonicalName(),
                mouseEventHandler.getInterfaceName());
    }
}
