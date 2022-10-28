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

import static inaugural.soliloquy.tools.random.Random.randomFloatInRange;
import static inaugural.soliloquy.tools.random.Random.randomLong;
import static org.junit.jupiter.api.Assertions.*;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class MouseEventHandlerImplTests {
    private final long TIMESTAMP = randomLong();

    @Mock private MouseEventCapturingSpatialIndex _mockMouseEventCapturingSpatialIndex;
    @Mock private ImageAssetRenderable _mockImageAssetRenderable;
    @Mock private ImageAssetRenderable _mockImageAssetRenderable2;

    private MouseEventHandler _mouseEventHandler;

    @BeforeEach
    void setUp() {
        _mockImageAssetRenderable = mock(ImageAssetRenderable.class);
        _mockImageAssetRenderable2 = mock(ImageAssetRenderable.class);

        _mockMouseEventCapturingSpatialIndex = mock(MouseEventCapturingSpatialIndex.class);
        when(_mockMouseEventCapturingSpatialIndex
                .getCapturingRenderableAtPoint(any(), anyLong()))
                .thenReturn(_mockImageAssetRenderable);

        _mouseEventHandler =
                new MouseEventHandlerImpl(_mockMouseEventCapturingSpatialIndex);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> new MouseEventHandlerImpl(null));
    }

    @Test
    void testMouseOver() {
        float x = randomFloatInRange(0, 1);
        float y = randomFloatInRange(0, 1);

        _mouseEventHandler.actOnMouseLocationAndEvents(Vertex.of(x, y), new HashMap<>(), TIMESTAMP);

        verify(_mockMouseEventCapturingSpatialIndex).getCapturingRenderableAtPoint(Vertex.of(x, y), TIMESTAMP);
        verify(_mockImageAssetRenderable).mouseOver(TIMESTAMP);
    }

    @Test
    void testMouseOverOnlyOnce() {
        float x = randomFloatInRange(0, 1);
        float y = randomFloatInRange(0, 1);

        _mouseEventHandler.actOnMouseLocationAndEvents(Vertex.of(x, y), new HashMap<>(), TIMESTAMP);
        _mouseEventHandler.actOnMouseLocationAndEvents(Vertex.of(x, y), new HashMap<>(), TIMESTAMP + 1);

        verify(_mockMouseEventCapturingSpatialIndex, times(1))
                .getCapturingRenderableAtPoint(Vertex.of(x, y), TIMESTAMP);
        verify(_mockMouseEventCapturingSpatialIndex, times(1))
                .getCapturingRenderableAtPoint(Vertex.of(x, y), TIMESTAMP + 1);
        verify(_mockImageAssetRenderable, times(1)).mouseOver(TIMESTAMP);
        verify(_mockImageAssetRenderable, never()).mouseOver(TIMESTAMP + 1);
    }

    @Test
    void testMouseLeave() {
        when(_mockMouseEventCapturingSpatialIndex
                .getCapturingRenderableAtPoint(any(), anyLong()))
                .thenReturn(_mockImageAssetRenderable)
                .thenReturn(null);

        float x = randomFloatInRange(0, 1);
        float y = randomFloatInRange(0, 1);

        _mouseEventHandler.actOnMouseLocationAndEvents(Vertex.of(x, y), new HashMap<>(), TIMESTAMP);
        _mouseEventHandler.actOnMouseLocationAndEvents(Vertex.of(x, y), new HashMap<>(), TIMESTAMP + 1);

        verify(_mockImageAssetRenderable).mouseOver(TIMESTAMP);
        verify(_mockImageAssetRenderable).mouseLeave(TIMESTAMP + 1);
    }

    @Test
    void testMouseLeaveAndMouseOverNewRenderable() {
        when(_mockMouseEventCapturingSpatialIndex
                .getCapturingRenderableAtPoint(any(), anyLong()))
                .thenReturn(_mockImageAssetRenderable)
                .thenReturn(_mockImageAssetRenderable2);

        float x = randomFloatInRange(0, 1);
        float y = randomFloatInRange(0, 1);

        _mouseEventHandler.actOnMouseLocationAndEvents(Vertex.of(x, y), new HashMap<>(), TIMESTAMP);
        _mouseEventHandler.actOnMouseLocationAndEvents(Vertex.of(x, y), new HashMap<>(), TIMESTAMP + 1);

        verify(_mockImageAssetRenderable).mouseOver(TIMESTAMP);
        verify(_mockImageAssetRenderable).mouseLeave(TIMESTAMP + 1);
        verify(_mockImageAssetRenderable2).mouseOver(TIMESTAMP + 1);
    }

    @Test
    void testPressButtonOnRenderable() {
        float x = randomFloatInRange(0, 1);
        float y = randomFloatInRange(0, 1);

        _mouseEventHandler.actOnMouseLocationAndEvents(Vertex.of(x, y),
                new HashMap<Integer, MouseEventHandler.EventType>() {{
                    put(GLFW_MOUSE_BUTTON_1, MouseEventHandler.EventType.PRESS);
                }}, TIMESTAMP);

        verify(_mockImageAssetRenderable)
                .press(GLFW_MOUSE_BUTTON_1, TIMESTAMP);
    }

    @Test
    void testPressButtonAfterMouseLeaveToNoRenderable() {
        when(_mockMouseEventCapturingSpatialIndex
                .getCapturingRenderableAtPoint(any(), anyLong()))
                .thenReturn(_mockImageAssetRenderable)
                .thenReturn(null);

        float x = randomFloatInRange(0, 1);
        float y = randomFloatInRange(0, 1);

        _mouseEventHandler.actOnMouseLocationAndEvents(Vertex.of(x, y), new HashMap<>(), TIMESTAMP);
        _mouseEventHandler.actOnMouseLocationAndEvents(Vertex.of(x, y),
                new HashMap<Integer, MouseEventHandler.EventType>() {{
                    put(GLFW_MOUSE_BUTTON_1, MouseEventHandler.EventType.PRESS);
                }}, TIMESTAMP);

        verify(_mockImageAssetRenderable, never())
                .press(anyInt(), anyLong());
    }

    @Test
    void testReleaseButtonOnRenderable() {
        float x = randomFloatInRange(0, 1);
        float y = randomFloatInRange(0, 1);

        _mouseEventHandler.actOnMouseLocationAndEvents(Vertex.of(x, y),
                new HashMap<Integer, MouseEventHandler.EventType>() {{
                    put(GLFW_MOUSE_BUTTON_1, MouseEventHandler.EventType.RELEASE);
                }}, TIMESTAMP);

        verify(_mockImageAssetRenderable)
                .release(GLFW_MOUSE_BUTTON_1, TIMESTAMP);
    }

    @Test
    void testReleaseButtonAfterMouseLeaveToNoRenderable() {
        when(_mockMouseEventCapturingSpatialIndex
                .getCapturingRenderableAtPoint(any(), anyLong()))
                .thenReturn(_mockImageAssetRenderable)
                .thenReturn(null);

        float x = randomFloatInRange(0, 1);
        float y = randomFloatInRange(0, 1);

        _mouseEventHandler.actOnMouseLocationAndEvents(Vertex.of(x, y), new HashMap<>(), TIMESTAMP);
        _mouseEventHandler.actOnMouseLocationAndEvents(Vertex.of(x, y),
                new HashMap<Integer, MouseEventHandler.EventType>() {{
                    put(GLFW_MOUSE_BUTTON_1, MouseEventHandler.EventType.RELEASE);
                }}, TIMESTAMP);

        verify(_mockImageAssetRenderable, never())
                .release(anyInt(), anyLong());
    }

    @Test
    void testActOnMouseLocationAndEventsWithInvalidInputs() {
        assertThrows(IllegalArgumentException.class, () ->
                _mouseEventHandler.actOnMouseLocationAndEvents(Vertex.of(-0.0001f, 0), new HashMap<>(),
                        TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                _mouseEventHandler.actOnMouseLocationAndEvents(Vertex.of(0, -0.0001f), new HashMap<>(),
                        TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                _mouseEventHandler.actOnMouseLocationAndEvents(Vertex.of(1.0001f, 0), new HashMap<>(),
                        TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                _mouseEventHandler.actOnMouseLocationAndEvents(Vertex.of(0, 1.0001f), new HashMap<>(),
                        TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                _mouseEventHandler.actOnMouseLocationAndEvents(Vertex.of(0, 0), null,
                        TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                _mouseEventHandler.actOnMouseLocationAndEvents(Vertex.of(0, 0),
                        new HashMap<Integer, MouseEventHandler.EventType>() {{
                            put(0, null);
                        }}, TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                _mouseEventHandler.actOnMouseLocationAndEvents(Vertex.of(0, 0),
                        new HashMap<Integer, MouseEventHandler.EventType>() {{
                            put(null, MouseEventHandler.EventType.PRESS);
                        }}, TIMESTAMP));
    }

    @Test
    void testActOnMouseLocationAndEventsWithOutOfDateTimestamp() {
        _mouseEventHandler.actOnMouseLocationAndEvents(Vertex.of(0f, 0f), new HashMap<>(),
                TIMESTAMP);

        assertThrows(IllegalArgumentException.class,
                () -> _mouseEventHandler.actOnMouseLocationAndEvents(Vertex.of(0f, 0f),
                        new HashMap<>(), TIMESTAMP - 1));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(MouseEventHandler.class.getCanonicalName(),
                _mouseEventHandler.getInterfaceName());
    }
}
