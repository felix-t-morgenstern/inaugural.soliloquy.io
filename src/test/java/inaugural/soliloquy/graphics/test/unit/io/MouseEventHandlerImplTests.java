package inaugural.soliloquy.graphics.test.unit.io;

import inaugural.soliloquy.graphics.io.MouseEventHandlerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.graphics.io.MouseEventCapturingSpatialIndex;
import soliloquy.specs.graphics.io.MouseEventHandler;
import soliloquy.specs.graphics.renderables.ImageAssetRenderable;
import soliloquy.specs.graphics.rendering.timing.GlobalClock;

import java.util.ArrayList;

import static inaugural.soliloquy.tools.random.Random.randomFloatInRange;
import static inaugural.soliloquy.tools.random.Random.randomLong;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class MouseEventHandlerImplTests {
    @Mock private MouseEventCapturingSpatialIndex _mockMouseEventCapturingSpatialIndex;
    @Mock private GlobalClock _mockGlobalClock;
    @Mock private ImageAssetRenderable _mockImageAssetRenderable;
    @Mock private ImageAssetRenderable _mockImageAssetRenderable2;

    private ArrayList<Long> _mockGlobalClockOutputs;

    private MouseEventHandler _mouseEventHandler;

    @BeforeEach
    void setUp() {
        _mockImageAssetRenderable = mock(ImageAssetRenderable.class);
        _mockImageAssetRenderable2 = mock(ImageAssetRenderable.class);

        _mockMouseEventCapturingSpatialIndex = mock(MouseEventCapturingSpatialIndex.class);
        when(_mockMouseEventCapturingSpatialIndex
                .getCapturingRenderableAtPoint(anyFloat(), anyFloat(), anyLong()))
                .thenReturn(_mockImageAssetRenderable);

        _mockGlobalClock = mock(GlobalClock.class);
        _mockGlobalClockOutputs = new ArrayList<>();
        when(_mockGlobalClock.globalTimestamp())
                .thenAnswer(invocation -> {
                    long output = randomLong();
                    _mockGlobalClockOutputs.add(output);
                    return output;
                });

        _mouseEventHandler =
                new MouseEventHandlerImpl(_mockMouseEventCapturingSpatialIndex, _mockGlobalClock);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> new MouseEventHandlerImpl(
                null, _mockGlobalClock));
        assertThrows(IllegalArgumentException.class, () -> new MouseEventHandlerImpl(
                _mockMouseEventCapturingSpatialIndex, null));
    }

    @Test
    void testMouseOver() {
        float x = randomFloatInRange(0, 1);
        float y = randomFloatInRange(0, 1);

        _mouseEventHandler.actOnMouseLocationAndEvents(x, y, null, null);

        verify(_mockMouseEventCapturingSpatialIndex)
                .getCapturingRenderableAtPoint(x, y, _mockGlobalClockOutputs.get(0));
        verify(_mockImageAssetRenderable)
                .mouseOver(_mockGlobalClockOutputs.get(0));
    }

    @Test
    void testMouseOverOnlyOnce() {
        float x = randomFloatInRange(0, 1);
        float y = randomFloatInRange(0, 1);

        _mouseEventHandler.actOnMouseLocationAndEvents(x, y, null, null);
        _mouseEventHandler.actOnMouseLocationAndEvents(x, y, null, null);

        verify(_mockMouseEventCapturingSpatialIndex)
                .getCapturingRenderableAtPoint(x, y, _mockGlobalClockOutputs.get(0));
        verify(_mockMouseEventCapturingSpatialIndex)
                .getCapturingRenderableAtPoint(x, y, _mockGlobalClockOutputs.get(1));
        verify(_mockImageAssetRenderable)
                .mouseOver(_mockGlobalClockOutputs.get(0));
        verify(_mockImageAssetRenderable, never())
                .mouseOver(_mockGlobalClockOutputs.get(1));
    }

    @Test
    void testMouseLeave() {
        when(_mockMouseEventCapturingSpatialIndex
                .getCapturingRenderableAtPoint(anyFloat(), anyFloat(), anyLong()))
                .thenReturn(_mockImageAssetRenderable)
                .thenReturn(null);

        float x = randomFloatInRange(0, 1);
        float y = randomFloatInRange(0, 1);

        _mouseEventHandler.actOnMouseLocationAndEvents(x, y, null, null);
        _mouseEventHandler.actOnMouseLocationAndEvents(x, y, null, null);

        verify(_mockImageAssetRenderable)
                .mouseOver(_mockGlobalClockOutputs.get(0));
        verify(_mockImageAssetRenderable)
                .mouseLeave(_mockGlobalClockOutputs.get(1));
    }

    @Test
    void testMouseLeaveAndMouseOverNewRenderable() {
        when(_mockMouseEventCapturingSpatialIndex
                .getCapturingRenderableAtPoint(anyFloat(), anyFloat(), anyLong()))
                .thenReturn(_mockImageAssetRenderable)
                .thenReturn(_mockImageAssetRenderable2);

        float x = randomFloatInRange(0, 1);
        float y = randomFloatInRange(0, 1);

        _mouseEventHandler.actOnMouseLocationAndEvents(x, y, null, null);
        _mouseEventHandler.actOnMouseLocationAndEvents(x, y, null, null);

        verify(_mockImageAssetRenderable)
                .mouseOver(_mockGlobalClockOutputs.get(0));
        verify(_mockImageAssetRenderable)
                .mouseLeave(_mockGlobalClockOutputs.get(1));
        verify(_mockImageAssetRenderable2)
                .mouseOver(_mockGlobalClockOutputs.get(1));
    }

    @Test
    void testPressButtonOnRenderable() {
        float x = randomFloatInRange(0, 1);
        float y = randomFloatInRange(0, 1);

        _mouseEventHandler.actOnMouseLocationAndEvents(x, y, GLFW_MOUSE_BUTTON_1,
                MouseEventHandler.EventType.PRESS);

        verify(_mockImageAssetRenderable)
                .press(GLFW_MOUSE_BUTTON_1, _mockGlobalClockOutputs.get(0));
    }

    @Test
    void testPressButtonAfterMouseLeaveToNoRenderable() {
        when(_mockMouseEventCapturingSpatialIndex
                .getCapturingRenderableAtPoint(anyFloat(), anyFloat(), anyLong()))
                .thenReturn(_mockImageAssetRenderable)
                .thenReturn(null);

        float x = randomFloatInRange(0, 1);
        float y = randomFloatInRange(0, 1);

        _mouseEventHandler.actOnMouseLocationAndEvents(x, y, null, null);
        _mouseEventHandler.actOnMouseLocationAndEvents(x, y, GLFW_MOUSE_BUTTON_1,
                MouseEventHandler.EventType.PRESS);

        verify(_mockImageAssetRenderable, never())
                .press(anyInt(), anyLong());
    }

    @Test
    void testReleaseButtonOnRenderable() {
        float x = randomFloatInRange(0, 1);
        float y = randomFloatInRange(0, 1);

        _mouseEventHandler.actOnMouseLocationAndEvents(x, y, GLFW_MOUSE_BUTTON_1,
                MouseEventHandler.EventType.RELEASE);

        verify(_mockImageAssetRenderable)
                .release(GLFW_MOUSE_BUTTON_1, _mockGlobalClockOutputs.get(0));
    }

    @Test
    void testReleaseButtonAfterMouseLeaveToNoRenderable() {
        when(_mockMouseEventCapturingSpatialIndex
                .getCapturingRenderableAtPoint(anyFloat(), anyFloat(), anyLong()))
                .thenReturn(_mockImageAssetRenderable)
                .thenReturn(null);

        float x = randomFloatInRange(0, 1);
        float y = randomFloatInRange(0, 1);

        _mouseEventHandler.actOnMouseLocationAndEvents(x, y, null, null);
        _mouseEventHandler.actOnMouseLocationAndEvents(x, y, GLFW_MOUSE_BUTTON_1,
                MouseEventHandler.EventType.RELEASE);

        verify(_mockImageAssetRenderable, never())
                .release(anyInt(), anyLong());
    }

    @Test
    void testActOnMouseLocationAndEventsWithInvalidInputs() {
        assertThrows(IllegalArgumentException.class, () ->
                _mouseEventHandler.actOnMouseLocationAndEvents(-0.0001f, 0, null, null));
        assertThrows(IllegalArgumentException.class, () ->
                _mouseEventHandler.actOnMouseLocationAndEvents(0, -0.0001f, null, null));
        assertThrows(IllegalArgumentException.class, () ->
                _mouseEventHandler.actOnMouseLocationAndEvents(1.0001f, 0, null, null));
        assertThrows(IllegalArgumentException.class, () ->
                _mouseEventHandler.actOnMouseLocationAndEvents(0, 1.0001f, null, null));
        assertThrows(IllegalArgumentException.class, () ->
                _mouseEventHandler.actOnMouseLocationAndEvents(0, 0, 0, null));
        assertThrows(IllegalArgumentException.class, () ->
                _mouseEventHandler.actOnMouseLocationAndEvents(0, 0, null,
                        MouseEventHandler.EventType.PRESS));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(MouseEventHandler.class.getCanonicalName(),
                _mouseEventHandler.getInterfaceName());
    }
}
