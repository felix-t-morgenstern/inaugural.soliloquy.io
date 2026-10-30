package inaugural.soliloquy.io.test.unit.mouse;

import inaugural.soliloquy.io.mouse.MouseEventCapturingSpatialIndex;
import inaugural.soliloquy.io.mouse.MouseEventHandler;
import inaugural.soliloquy.io.mouse.MouseListener;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.ImageAssetRenderable;

import static inaugural.soliloquy.io.api.Constants.LEFT_MOUSE_BUTTON;
import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;
import static soliloquy.specs.io.input.mouse.Mouse.EventType;

@ExtendWith(MockitoExtension.class)
public class MouseEventHandlerTests {
    private final Vertex MOUSE_LOC =
            vertexOf(randomFloatInRange(0f, 1f), randomFloatInRange(0f, 1f));
    private final long TIMESTAMP = randomLong();

    @Mock private TimestampValidator mockTimestampValidator;
    @Mock private MouseEventCapturingSpatialIndex mockMouseEventCapturingSpatialIndex;
    @Mock private ImageAssetRenderable mockImageAssetRenderable;
    @Mock private ImageAssetRenderable mockImageAssetRenderable2;

    private MouseEventHandler mouseEventHandler;

    @BeforeEach
    public void setUp() {
        lenient().when(mockImageAssetRenderable.getCapturesMouseButtonEvents()).thenReturn(true);

        lenient().when(mockMouseEventCapturingSpatialIndex
                        .getCapturingRenderable(any(), anyLong()))
                .thenReturn(listOf(mockImageAssetRenderable));

        mouseEventHandler = new MouseEventHandler(mockTimestampValidator,
                mockMouseEventCapturingSpatialIndex::getCapturingRenderable);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new MouseEventHandler(null,
                mockMouseEventCapturingSpatialIndex::getCapturingRenderable));
        assertThrows(IllegalArgumentException.class,
                () -> new MouseEventHandler(mockTimestampValidator, null));
    }

    @Test
    public void testWheelScroll() {
        when(mockImageAssetRenderable.getCapturesMouseWheelEvents()).thenReturn(true);
        var wheelScrollAmt = randomFloat();

        mouseEventHandler.actOnMouseLocationAndEvents(
                new MouseListener.FrameMouseEvents(MOUSE_LOC, mapOf(), wheelScrollAmt, TIMESTAMP));

        verify(mockMouseEventCapturingSpatialIndex).getCapturingRenderable(MOUSE_LOC, TIMESTAMP);
        verify(mockImageAssetRenderable).wheelScroll(wheelScrollAmt, MOUSE_LOC, TIMESTAMP);
    }

    @Test
    public void testMouseOver() {
        mouseEventHandler.actOnMouseLocationAndEvents(
                new MouseListener.FrameMouseEvents(MOUSE_LOC, mapOf(), 0f, TIMESTAMP));

        verify(mockMouseEventCapturingSpatialIndex).getCapturingRenderable(MOUSE_LOC, TIMESTAMP);
        verify(mockImageAssetRenderable).mouseOver(MOUSE_LOC, TIMESTAMP);
    }

    @Test
    public void testMouseOverOnlyOnce() {
        mouseEventHandler.actOnMouseLocationAndEvents(
                new MouseListener.FrameMouseEvents(MOUSE_LOC, mapOf(), 0f, TIMESTAMP));
        mouseEventHandler.actOnMouseLocationAndEvents(
                new MouseListener.FrameMouseEvents(MOUSE_LOC, mapOf(), 0f, TIMESTAMP + 1));

        verify(mockMouseEventCapturingSpatialIndex, once())
                .getCapturingRenderable(MOUSE_LOC, TIMESTAMP);
        verify(mockMouseEventCapturingSpatialIndex, once())
                .getCapturingRenderable(MOUSE_LOC, TIMESTAMP + 1);
        verify(mockImageAssetRenderable, once()).mouseOver(MOUSE_LOC, TIMESTAMP);
        verify(mockImageAssetRenderable, never()).mouseOver(MOUSE_LOC, TIMESTAMP + 1);
    }

    @Test
    public void testMouseLeave() {
        when(mockMouseEventCapturingSpatialIndex
                .getCapturingRenderable(any(), anyLong()))
                .thenReturn(listOf(mockImageAssetRenderable))
                .thenReturn(listOf());

        mouseEventHandler.actOnMouseLocationAndEvents(
                new MouseListener.FrameMouseEvents(MOUSE_LOC, mapOf(), 0f, TIMESTAMP));
        mouseEventHandler.actOnMouseLocationAndEvents(
                new MouseListener.FrameMouseEvents(MOUSE_LOC, mapOf(), 0f, TIMESTAMP + 1));

        verify(mockImageAssetRenderable).mouseOver(MOUSE_LOC, TIMESTAMP);
        verify(mockImageAssetRenderable).mouseLeave(MOUSE_LOC, TIMESTAMP + 1);
    }

    @Test
    public void testMouseLeaveAndMouseOverNewRenderable() {
        when(mockImageAssetRenderable2.getCapturesMouseButtonEvents()).thenReturn(true);
        when(mockMouseEventCapturingSpatialIndex
                .getCapturingRenderable(any(), anyLong()))
                .thenReturn(listOf(mockImageAssetRenderable))
                .thenReturn(listOf(mockImageAssetRenderable2));

        mouseEventHandler.actOnMouseLocationAndEvents(
                new MouseListener.FrameMouseEvents(MOUSE_LOC, mapOf(), 0f, TIMESTAMP));
        mouseEventHandler.actOnMouseLocationAndEvents(
                new MouseListener.FrameMouseEvents(MOUSE_LOC, mapOf(), 0f, TIMESTAMP + 1));

        verify(mockImageAssetRenderable).mouseOver(MOUSE_LOC, TIMESTAMP);
        verify(mockImageAssetRenderable).mouseLeave(MOUSE_LOC, TIMESTAMP + 1);
        verify(mockImageAssetRenderable2).mouseOver(MOUSE_LOC, TIMESTAMP + 1);
    }

    @Test
    public void testPressButtonOnRenderable() {
        mouseEventHandler.actOnMouseLocationAndEvents(new MouseListener.FrameMouseEvents(MOUSE_LOC,
                mapOf(pairOf(LEFT_MOUSE_BUTTON, EventType.PRESS)), 0f, TIMESTAMP));

        verify(mockImageAssetRenderable)
                .press(LEFT_MOUSE_BUTTON, MOUSE_LOC, TIMESTAMP);
    }

    @Test
    public void testPressButtonAfterMouseLeaveToNoRenderable() {
        when(mockMouseEventCapturingSpatialIndex
                .getCapturingRenderable(any(), anyLong()))
                .thenReturn(listOf(mockImageAssetRenderable))
                .thenReturn(listOf());

        mouseEventHandler.actOnMouseLocationAndEvents(
                new MouseListener.FrameMouseEvents(MOUSE_LOC, mapOf(), 0f, TIMESTAMP));
        mouseEventHandler.actOnMouseLocationAndEvents(new MouseListener.FrameMouseEvents(MOUSE_LOC,
                mapOf(pairOf(LEFT_MOUSE_BUTTON, EventType.PRESS)), 0f, TIMESTAMP));

        verify(mockImageAssetRenderable, never())
                .press(anyInt(), any(), anyLong());
    }

    @Test
    public void testReleaseButtonOnRenderable() {
        mouseEventHandler.actOnMouseLocationAndEvents(new MouseListener.FrameMouseEvents(MOUSE_LOC,
                mapOf(pairOf(LEFT_MOUSE_BUTTON, EventType.RELEASE)), 0f, TIMESTAMP));

        verify(mockImageAssetRenderable)
                .release(LEFT_MOUSE_BUTTON, MOUSE_LOC, TIMESTAMP);
    }

    @Test
    public void testReleaseButtonAfterMouseLeaveToNoRenderable() {
        when(mockMouseEventCapturingSpatialIndex
                .getCapturingRenderable(any(), anyLong()))
                .thenReturn(listOf(mockImageAssetRenderable))
                .thenReturn(listOf());

        mouseEventHandler.actOnMouseLocationAndEvents(
                new MouseListener.FrameMouseEvents(MOUSE_LOC, mapOf(), 0f, TIMESTAMP));
        mouseEventHandler.actOnMouseLocationAndEvents(new MouseListener.FrameMouseEvents(MOUSE_LOC,
                mapOf(pairOf(LEFT_MOUSE_BUTTON, EventType.RELEASE)), 0f, TIMESTAMP));

        verify(mockImageAssetRenderable, never())
                .release(anyInt(), any(), anyLong());
    }

    @Test
    public void testMouseButtonEventsAreTriggeredWhenRenderableUnderMouseDoesNotChange() {
        when(mockMouseEventCapturingSpatialIndex
                .getCapturingRenderable(any(), anyLong()))
                .thenReturn(listOf(mockImageAssetRenderable));

        mouseEventHandler.actOnMouseLocationAndEvents(
                new MouseListener.FrameMouseEvents(MOUSE_LOC, mapOf(), 0f, TIMESTAMP));
        mouseEventHandler.actOnMouseLocationAndEvents(new MouseListener.FrameMouseEvents(MOUSE_LOC,
                mapOf(pairOf(LEFT_MOUSE_BUTTON, EventType.PRESS)), 0f, TIMESTAMP));

        verify(mockImageAssetRenderable, once()).press(LEFT_MOUSE_BUTTON, MOUSE_LOC, TIMESTAMP);
    }

    @Test
    public void testActOnMouseLocationAndEventsWithInvalidInputs() {
        assertThrows(IllegalArgumentException.class, () ->
                mouseEventHandler.actOnMouseLocationAndEvents(
                        new MouseListener.FrameMouseEvents(vertexOf(-0.0001f, 0), mapOf(),
                                0f, TIMESTAMP)));
        assertThrows(IllegalArgumentException.class, () ->
                mouseEventHandler.actOnMouseLocationAndEvents(
                        new MouseListener.FrameMouseEvents(vertexOf(0, -0.0001f), mapOf(),
                                0f, TIMESTAMP)));
        assertThrows(IllegalArgumentException.class, () ->
                mouseEventHandler.actOnMouseLocationAndEvents(
                        new MouseListener.FrameMouseEvents(vertexOf(1.0001f, 0), mapOf(),
                                0f, TIMESTAMP)));
        assertThrows(IllegalArgumentException.class, () ->
                mouseEventHandler.actOnMouseLocationAndEvents(
                        new MouseListener.FrameMouseEvents(vertexOf(0, 1.0001f), mapOf(),
                                0f, TIMESTAMP)));
        assertThrows(IllegalArgumentException.class, () ->
                mouseEventHandler.actOnMouseLocationAndEvents(
                        new MouseListener.FrameMouseEvents(MOUSE_LOC, null, 0f,
                                TIMESTAMP)));
        assertThrows(IllegalArgumentException.class, () ->
                mouseEventHandler.actOnMouseLocationAndEvents(
                        new MouseListener.FrameMouseEvents(MOUSE_LOC, mapOf(pairOf(0, null)),
                                0f, TIMESTAMP)));
        assertThrows(IllegalArgumentException.class, () ->
                mouseEventHandler.actOnMouseLocationAndEvents(
                        new MouseListener.FrameMouseEvents(MOUSE_LOC,
                                mapOf(pairOf(null, EventType.PRESS)), 0f, TIMESTAMP)));
    }

    @Test
    public void testActOnMouseLocationAndEventsValidatesTimestamp() {
        mouseEventHandler.actOnMouseLocationAndEvents(
                new MouseListener.FrameMouseEvents(vertexOf(0f, 0f), mapOf(), 0f,
                        TIMESTAMP));

        verify(mockTimestampValidator, once()).validateTimestamp(TIMESTAMP);
    }

    @Test
    public void testSubscribeToNextEvent() {
        var mockRunnable = mock(Runnable.class);
        var mouseButton = randomInt();
        mouseEventHandler.subscribeToNextEvent(mouseButton, EventType.PRESS, mockRunnable);

        mouseEventHandler.actOnMouseLocationAndEvents(new MouseListener.FrameMouseEvents(MOUSE_LOC,
                mapOf(pairOf(mouseButton, EventType.PRESS)), 0f, TIMESTAMP));
        mouseEventHandler.actOnMouseLocationAndEvents(new MouseListener.FrameMouseEvents(MOUSE_LOC,
                mapOf(pairOf(mouseButton, EventType.PRESS)), 0f, TIMESTAMP));

        verify(mockRunnable, once()).run();
    }
}
