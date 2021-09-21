package inaugural.soliloquy.graphics.test.unit.io;

import inaugural.soliloquy.graphics.io.MouseEventCapturingSpatialIndexImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeFloatBox;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeRenderableWithArea;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.io.MouseEventCapturingSpatialIndex;

import static org.junit.jupiter.api.Assertions.*;

class MouseEventCapturingSpatialIndexImplTests {
    private final FakeRenderableWithArea RenderableWithArea1 = new FakeRenderableWithArea();
    private final FakeRenderableWithArea RenderableWithArea2 = new FakeRenderableWithArea();
    private final FakeRenderableWithArea RenderableWithArea3 = new FakeRenderableWithArea();
    private final FakeRenderableWithArea RenderableWithArea4 = new FakeRenderableWithArea();

    private final FakeFloatBox RenderingDimensions1 = new FakeFloatBox(.1f, .1f, .5f, .5f);
    private final FakeFloatBox RenderingDimensions2 = new FakeFloatBox(.2f, .2f, .6f, .6f);
    private final FakeFloatBox RenderingDimensions3 = new FakeFloatBox(.3f, .3f, .7f, .7f);
    private final FakeFloatBox RenderingDimensions4 = new FakeFloatBox(.4f, .4f, .8f, .8f);

    private MouseEventCapturingSpatialIndex _mouseEventCapturingSpatialIndex;

    @BeforeEach
    void setUp() {
        RenderableWithArea1.RenderingDimensions = RenderingDimensions1;
        RenderableWithArea2.RenderingDimensions = RenderingDimensions2;
        RenderableWithArea3.RenderingDimensions = RenderingDimensions3;
        RenderableWithArea4.RenderingDimensions = RenderingDimensions4;
        RenderableWithArea1.Z = 1;
        RenderableWithArea2.Z = 2;
        RenderableWithArea3.Z = 3;
        RenderableWithArea4.Z = 4;
        RenderableWithArea1.CapturesMouseEvents = true;
        RenderableWithArea2.CapturesMouseEvents = true;
        RenderableWithArea3.CapturesMouseEvents = true;
        RenderableWithArea4.CapturesMouseEvents = true;

        _mouseEventCapturingSpatialIndex = new MouseEventCapturingSpatialIndexImpl();
    }

    @Test
    void testPutRenderableAndGetCapturingRenderableAtPoint() {
        assertNull(_mouseEventCapturingSpatialIndex.getCapturingRenderableAtPoint(.1f, .1f, 0L));

        _mouseEventCapturingSpatialIndex.putRenderable(RenderableWithArea1, RenderingDimensions1);

        assertSame(RenderableWithArea1,
                _mouseEventCapturingSpatialIndex.getCapturingRenderableAtPoint(.1f, .1f, 0L));
        assertNull(_mouseEventCapturingSpatialIndex
                .getCapturingRenderableAtPoint(0.09999f, 0.09999f, 0L));
        assertSame(RenderableWithArea1,
                _mouseEventCapturingSpatialIndex.getCapturingRenderableAtPoint(.5f, .5f, 0L));
        assertNull(_mouseEventCapturingSpatialIndex
                .getCapturingRenderableAtPoint(0.50001f, 0.50001f, 0L));
    }

    @Test
    void testRemoveAbsentRenderableDoesNotThrow() {
        _mouseEventCapturingSpatialIndex.removeRenderable(RenderableWithArea1);
    }

    @Test
    void testPutRenderableWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _mouseEventCapturingSpatialIndex.putRenderable(null, RenderingDimensions1));
        assertThrows(IllegalArgumentException.class, () ->
                _mouseEventCapturingSpatialIndex.putRenderable(RenderableWithArea1, null));
        RenderableWithArea1.CapturesMouseEvents = false;
        assertThrows(IllegalArgumentException.class, () ->
                _mouseEventCapturingSpatialIndex.putRenderable(RenderableWithArea1,
                        RenderingDimensions1));
    }

    @Test
    void testRemoveWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _mouseEventCapturingSpatialIndex.removeRenderable(null));
    }

    @Test
    void testRemoveRenderable() {
        assertNull(_mouseEventCapturingSpatialIndex.getCapturingRenderableAtPoint(.1f, .1f, 0L));

        _mouseEventCapturingSpatialIndex.putRenderable(RenderableWithArea1, RenderingDimensions1);

        assertSame(RenderableWithArea1,
                _mouseEventCapturingSpatialIndex.getCapturingRenderableAtPoint(.1f, .1f, 0L));

        _mouseEventCapturingSpatialIndex.removeRenderable(RenderableWithArea1);

        assertNull(_mouseEventCapturingSpatialIndex.getCapturingRenderableAtPoint(.1f, .1f, 0L));
    }

    @Test
    void testUpdateRenderable() {
        _mouseEventCapturingSpatialIndex.putRenderable(RenderableWithArea1, RenderingDimensions1);

        assertSame(RenderableWithArea1,
                _mouseEventCapturingSpatialIndex.getCapturingRenderableAtPoint(.1f, .1f, 0L));
        assertNull(_mouseEventCapturingSpatialIndex.getCapturingRenderableAtPoint(.6f, .6f, 0L));

        _mouseEventCapturingSpatialIndex.putRenderable(RenderableWithArea1, RenderingDimensions2);

        assertNull(_mouseEventCapturingSpatialIndex.getCapturingRenderableAtPoint(.1f, .1f, 0L));
        assertSame(RenderableWithArea1,
                _mouseEventCapturingSpatialIndex.getCapturingRenderableAtPoint(.6f, .6f, 0L));
    }

    @Test
    void testGetCapturingRenderableAtPointWithHighestZIndex() {
        _mouseEventCapturingSpatialIndex.putRenderable(RenderableWithArea1, RenderingDimensions1);
        _mouseEventCapturingSpatialIndex.putRenderable(RenderableWithArea2, RenderingDimensions2);
        _mouseEventCapturingSpatialIndex.putRenderable(RenderableWithArea3, RenderingDimensions3);
        _mouseEventCapturingSpatialIndex.putRenderable(RenderableWithArea4, RenderingDimensions4);

        assertSame(RenderableWithArea4,
                _mouseEventCapturingSpatialIndex.getCapturingRenderableAtPoint(.45f, .45f, 0L));
    }

    @Test
    void testGetCapturingRenderableAtPointIgnoresRenderablesNotCapturingAtPoint() {
        fail("Relegate capturesMouseEventsAtPixel to assets; test and implement this after");
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(MouseEventCapturingSpatialIndex.class.getCanonicalName(),
                _mouseEventCapturingSpatialIndex.getInterfaceName());
    }
}
