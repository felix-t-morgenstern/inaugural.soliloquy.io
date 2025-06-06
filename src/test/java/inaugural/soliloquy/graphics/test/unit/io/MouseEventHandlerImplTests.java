package inaugural.soliloquy.graphics.test.unit.io;

import inaugural.soliloquy.graphics.io.MouseEventHandlerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.io.MouseEventCapturingSpatialIndex;
import soliloquy.specs.graphics.io.MouseEventHandler;
import soliloquy.specs.graphics.renderables.ImageAssetRenderable;


import static inaugural.soliloquy.graphics.api.Constants.LEFT_MOUSE_BUTTON;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.randomFloatInRange;
import static inaugural.soliloquy.tools.random.Random.randomLong;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

public class MouseEventHandlerImplTests {
    private final Vertex POSITION =
            vertexOf(randomFloatInRange(0f, 1f), randomFloatInRange(0f, 1f));
    private final long TIMESTAMP = randomLong();

    @Mock private MouseEventCapturingSpatialIndex mockMouseEventCapturingSpatialIndex;
    @Mock private ImageAssetRenderable mockImageAssetRenderable;
    @Mock private ImageAssetRenderable mockImageAssetRenderable2;

    private MouseEventHandler mouseEventHandler;

    @BeforeEach
    public void setUp() {
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
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new MouseEventHandlerImpl(null));
    }

    @Test
    public void testMouseOver() {
        mouseEventHandler.actOnMouseLocationAndEvents(POSITION, mapOf(), TIMESTAMP);

        verify(mockMouseEventCapturingSpatialIndex).getCapturingRenderableAtPoint(POSITION,
                TIMESTAMP);
        verify(mockImageAssetRenderable).mouseOver(TIMESTAMP);
    }

    @Test
    public void testMouseOverOnlyOnce() {
        mouseEventHandler.actOnMouseLocationAndEvents(POSITION, mapOf(), TIMESTAMP);
        mouseEventHandler.actOnMouseLocationAndEvents(POSITION, mapOf(),
                TIMESTAMP + 1);

        verify(mockMouseEventCapturingSpatialIndex, once())
                .getCapturingRenderableAtPoint(POSITION, TIMESTAMP);
        verify(mockMouseEventCapturingSpatialIndex, once())
                .getCapturingRenderableAtPoint(POSITION, TIMESTAMP + 1);
        verify(mockImageAssetRenderable, once()).mouseOver(TIMESTAMP);
        verify(mockImageAssetRenderable, never()).mouseOver(TIMESTAMP + 1);
    }

    @Test
    public void testMouseLeave() {
        when(mockMouseEventCapturingSpatialIndex
                .getCapturingRenderableAtPoint(any(), anyLong()))
                .thenReturn(mockImageAssetRenderable)
                .thenReturn(null);

        mouseEventHandler.actOnMouseLocationAndEvents(POSITION, mapOf(), TIMESTAMP);
        mouseEventHandler.actOnMouseLocationAndEvents(POSITION, mapOf(),
                TIMESTAMP + 1);

        verify(mockImageAssetRenderable).mouseOver(TIMESTAMP);
        verify(mockImageAssetRenderable).mouseLeave(TIMESTAMP + 1);
    }

    @Test
    public void testMouseLeaveAndMouseOverNewRenderable() {
        when(mockMouseEventCapturingSpatialIndex
                .getCapturingRenderableAtPoint(any(), anyLong()))
                .thenReturn(mockImageAssetRenderable)
                .thenReturn(mockImageAssetRenderable2);

        mouseEventHandler.actOnMouseLocationAndEvents(POSITION, mapOf(), TIMESTAMP);
        mouseEventHandler.actOnMouseLocationAndEvents(POSITION, mapOf(),
                TIMESTAMP + 1);

        verify(mockImageAssetRenderable).mouseOver(TIMESTAMP);
        verify(mockImageAssetRenderable).mouseLeave(TIMESTAMP + 1);
        verify(mockImageAssetRenderable2).mouseOver(TIMESTAMP + 1);
    }

    @Test
    public void testPressButtonOnRenderable() {
        mouseEventHandler.actOnMouseLocationAndEvents(POSITION,
                mapOf(
                    pairOf(LEFT_MOUSE_BUTTON, MouseEventHandler.EventType.PRESS)
                ), TIMESTAMP);

        verify(mockImageAssetRenderable)
                .press(LEFT_MOUSE_BUTTON, TIMESTAMP);
    }

    @Test
    public void testPressButtonAfterMouseLeaveToNoRenderable() {
        when(mockMouseEventCapturingSpatialIndex
                .getCapturingRenderableAtPoint(any(), anyLong()))
                .thenReturn(mockImageAssetRenderable)
                .thenReturn(null);

        mouseEventHandler.actOnMouseLocationAndEvents(POSITION, mapOf(), TIMESTAMP);
        mouseEventHandler.actOnMouseLocationAndEvents(POSITION,
                mapOf(
                    pairOf(LEFT_MOUSE_BUTTON, MouseEventHandler.EventType.PRESS)
                ), TIMESTAMP);

        verify(mockImageAssetRenderable, never())
                .press(anyInt(), anyLong());
    }

    @Test
    public void testReleaseButtonOnRenderable() {
        mouseEventHandler.actOnMouseLocationAndEvents(POSITION,
                mapOf(
                    pairOf(LEFT_MOUSE_BUTTON, MouseEventHandler.EventType.RELEASE)
                ), TIMESTAMP);

        verify(mockImageAssetRenderable)
                .release(LEFT_MOUSE_BUTTON, TIMESTAMP);
    }

    @Test
    public void testReleaseButtonAfterMouseLeaveToNoRenderable() {
        when(mockMouseEventCapturingSpatialIndex
                .getCapturingRenderableAtPoint(any(), anyLong()))
                .thenReturn(mockImageAssetRenderable)
                .thenReturn(null);

        mouseEventHandler.actOnMouseLocationAndEvents(POSITION, mapOf(), TIMESTAMP);
        mouseEventHandler.actOnMouseLocationAndEvents(POSITION,
                mapOf(
                    pairOf(LEFT_MOUSE_BUTTON, MouseEventHandler.EventType.RELEASE)
                ), TIMESTAMP);

        verify(mockImageAssetRenderable, never())
                .release(anyInt(), anyLong());
    }

    @Test
    public void testMouseButtonEventsAreTriggeredWhenRenderableUnderMouseDoesNotChange() {
        when(mockMouseEventCapturingSpatialIndex
                .getCapturingRenderableAtPoint(any(), anyLong()))
                .thenReturn(mockImageAssetRenderable);

        mouseEventHandler.actOnMouseLocationAndEvents(POSITION, mapOf(), TIMESTAMP);
        mouseEventHandler.actOnMouseLocationAndEvents(POSITION,
                mapOf(
                    pairOf(LEFT_MOUSE_BUTTON, MouseEventHandler.EventType.PRESS)
                ), TIMESTAMP);

        verify(mockImageAssetRenderable, once()).press(LEFT_MOUSE_BUTTON, TIMESTAMP);
    }

    @Test
    public void testActOnMouseLocationAndEventsWithInvalidInputs() {
        assertThrows(IllegalArgumentException.class, () ->
                mouseEventHandler.actOnMouseLocationAndEvents(vertexOf(-0.0001f, 0),
                        mapOf(),
                        TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                mouseEventHandler.actOnMouseLocationAndEvents(vertexOf(0, -0.0001f),
                        mapOf(),
                        TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                mouseEventHandler.actOnMouseLocationAndEvents(vertexOf(1.0001f, 0),
                        mapOf(),
                        TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                mouseEventHandler.actOnMouseLocationAndEvents(vertexOf(0, 1.0001f),
                        mapOf(),
                        TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                mouseEventHandler.actOnMouseLocationAndEvents(POSITION, null, TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                mouseEventHandler.actOnMouseLocationAndEvents(POSITION,
                        mapOf(
                            pairOf(0, null)
                        ), TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                mouseEventHandler.actOnMouseLocationAndEvents(POSITION,
                        mapOf(
                            pairOf(null, MouseEventHandler.EventType.PRESS)
                        ), TIMESTAMP));
    }

    @Test
    public void testActOnMouseLocationAndEventsWithOutOfDateTimestamp() {
        mouseEventHandler.actOnMouseLocationAndEvents(vertexOf(0f, 0f), mapOf(),
                TIMESTAMP);

        assertThrows(IllegalArgumentException.class,
                () -> mouseEventHandler.actOnMouseLocationAndEvents(vertexOf(0f, 0f),
                        mapOf(), TIMESTAMP - 1));
    }
}
