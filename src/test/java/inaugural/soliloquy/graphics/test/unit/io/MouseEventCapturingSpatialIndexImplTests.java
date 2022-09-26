package inaugural.soliloquy.graphics.test.unit.io;

import inaugural.soliloquy.graphics.io.MouseEventCapturingSpatialIndexImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeFloatBox;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeImageAssetRenderable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.io.MouseEventCapturingSpatialIndex;

import static org.junit.jupiter.api.Assertions.*;

class MouseEventCapturingSpatialIndexImplTests {
    private final FakeImageAssetRenderable ImageAssetRenderable1 = new FakeImageAssetRenderable();
    private final FakeImageAssetRenderable ImageAssetRenderable2 = new FakeImageAssetRenderable();
    private final FakeImageAssetRenderable ImageAssetRenderable3 = new FakeImageAssetRenderable();
    private final FakeImageAssetRenderable ImageAssetRenderable4 = new FakeImageAssetRenderable();

    private final FakeFloatBox RenderingDimensions1 = new FakeFloatBox(.1f, .1f, .5f, .5f);
    private final FakeFloatBox RenderingDimensions2 = new FakeFloatBox(.2f, .2f, .6f, .6f);
    private final FakeFloatBox RenderingDimensions3 = new FakeFloatBox(.3f, .3f, .7f, .7f);
    private final FakeFloatBox RenderingDimensions4 = new FakeFloatBox(.4f, .4f, .8f, .8f);

    private MouseEventCapturingSpatialIndex _mouseEventCapturingSpatialIndex;

    @BeforeEach
    void setUp() {
        ImageAssetRenderable1.RenderingDimensions = RenderingDimensions1;
        ImageAssetRenderable2.RenderingDimensions = RenderingDimensions2;
        ImageAssetRenderable3.RenderingDimensions = RenderingDimensions3;
        ImageAssetRenderable4.RenderingDimensions = RenderingDimensions4;
        ImageAssetRenderable1.Z = 1;
        ImageAssetRenderable2.Z = 2;
        ImageAssetRenderable3.Z = 3;
        ImageAssetRenderable4.Z = 4;
        ImageAssetRenderable1.CapturesMouseEvents = true;
        ImageAssetRenderable2.CapturesMouseEvents = true;
        ImageAssetRenderable3.CapturesMouseEvents = true;
        ImageAssetRenderable4.CapturesMouseEvents = true;

        _mouseEventCapturingSpatialIndex = new MouseEventCapturingSpatialIndexImpl();
    }

    @Test
    void testPutRenderableAndGetCapturingRenderableAtPoint() {
        assertNull(_mouseEventCapturingSpatialIndex.getCapturingRenderableAtPoint(.1f, .1f, 0L));

        _mouseEventCapturingSpatialIndex.putRenderable(ImageAssetRenderable1, RenderingDimensions1);

        assertSame(ImageAssetRenderable1,
                _mouseEventCapturingSpatialIndex.getCapturingRenderableAtPoint(.1f, .1f, 0L));
        assertNull(_mouseEventCapturingSpatialIndex
                .getCapturingRenderableAtPoint(0.09999f, 0.09999f, 0L));
        assertSame(ImageAssetRenderable1,
                _mouseEventCapturingSpatialIndex.getCapturingRenderableAtPoint(.5f, .5f, 0L));
        assertNull(_mouseEventCapturingSpatialIndex
                .getCapturingRenderableAtPoint(0.50001f, 0.50001f, 0L));
    }

    @Test
    void testRemoveAbsentRenderableDoesNotThrow() {
        _mouseEventCapturingSpatialIndex.removeRenderable(ImageAssetRenderable1);
    }

    @Test
    void testPutRenderableWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _mouseEventCapturingSpatialIndex.putRenderable(null, RenderingDimensions1));
        assertThrows(IllegalArgumentException.class, () ->
                _mouseEventCapturingSpatialIndex.putRenderable(ImageAssetRenderable1, null));
        ImageAssetRenderable1.CapturesMouseEvents = false;
        assertThrows(IllegalArgumentException.class, () ->
                _mouseEventCapturingSpatialIndex.putRenderable(ImageAssetRenderable1,
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

        _mouseEventCapturingSpatialIndex.putRenderable(ImageAssetRenderable1, RenderingDimensions1);

        assertSame(ImageAssetRenderable1,
                _mouseEventCapturingSpatialIndex.getCapturingRenderableAtPoint(.1f, .1f, 0L));

        _mouseEventCapturingSpatialIndex.removeRenderable(ImageAssetRenderable1);

        assertNull(_mouseEventCapturingSpatialIndex.getCapturingRenderableAtPoint(.1f, .1f, 0L));
    }

    @Test
    void testUpdateRenderable() {
        _mouseEventCapturingSpatialIndex.putRenderable(ImageAssetRenderable1, RenderingDimensions1);

        assertSame(ImageAssetRenderable1,
                _mouseEventCapturingSpatialIndex.getCapturingRenderableAtPoint(.1f, .1f, 0L));
        assertNull(_mouseEventCapturingSpatialIndex.getCapturingRenderableAtPoint(.6f, .6f, 0L));

        _mouseEventCapturingSpatialIndex.putRenderable(ImageAssetRenderable1, RenderingDimensions2);

        assertNull(_mouseEventCapturingSpatialIndex.getCapturingRenderableAtPoint(.1f, .1f, 0L));
        assertSame(ImageAssetRenderable1,
                _mouseEventCapturingSpatialIndex.getCapturingRenderableAtPoint(.6f, .6f, 0L));
    }

    @Test
    void testGetCapturingRenderableAtPointWithHighestZIndex() {
        _mouseEventCapturingSpatialIndex.putRenderable(ImageAssetRenderable1, RenderingDimensions1);
        _mouseEventCapturingSpatialIndex.putRenderable(ImageAssetRenderable2, RenderingDimensions2);
        _mouseEventCapturingSpatialIndex.putRenderable(ImageAssetRenderable3, RenderingDimensions3);
        _mouseEventCapturingSpatialIndex.putRenderable(ImageAssetRenderable4, RenderingDimensions4);

        assertSame(ImageAssetRenderable4,
                _mouseEventCapturingSpatialIndex.getCapturingRenderableAtPoint(.45f, .45f, 0L));
    }

    @Test
    void testGetCapturingRenderableAtPointIgnoresRenderablesNotCapturingAtPoint() {
        ImageAssetRenderable3.CapturesMouseEventsAtPoint = false;
        ImageAssetRenderable4.CapturesMouseEventsAtPoint = false;

        _mouseEventCapturingSpatialIndex.putRenderable(ImageAssetRenderable1, RenderingDimensions1);
        _mouseEventCapturingSpatialIndex.putRenderable(ImageAssetRenderable2, RenderingDimensions2);
        _mouseEventCapturingSpatialIndex.putRenderable(ImageAssetRenderable3, RenderingDimensions3);
        _mouseEventCapturingSpatialIndex.putRenderable(ImageAssetRenderable4, RenderingDimensions4);

        assertSame(ImageAssetRenderable2, _mouseEventCapturingSpatialIndex
                .getCapturingRenderableAtPoint(.454f, .456f, 789L));
        assertEquals(1, ImageAssetRenderable4.CapturesMouseEventsAtPointInputLocations.size());
        assertEquals(1, ImageAssetRenderable4.CapturesMouseEventsAtPointInputTimestamps.size());
        assertEquals(0.454f,
                ImageAssetRenderable4.CapturesMouseEventsAtPointInputLocations.get(0).x);
        assertEquals(0.456f,
                ImageAssetRenderable4.CapturesMouseEventsAtPointInputLocations.get(0).y);
        assertEquals(789L,
                (long) ImageAssetRenderable4.CapturesMouseEventsAtPointInputTimestamps.get(0));
        assertEquals(1, ImageAssetRenderable3.CapturesMouseEventsAtPointInputLocations.size());
        assertEquals(1, ImageAssetRenderable3.CapturesMouseEventsAtPointInputTimestamps.size());
        assertEquals(0.454f,
                ImageAssetRenderable3.CapturesMouseEventsAtPointInputLocations.get(0).x);
        assertEquals(0.456f,
                ImageAssetRenderable3.CapturesMouseEventsAtPointInputLocations.get(0).y);
        assertEquals(789L,
                (long) ImageAssetRenderable3.CapturesMouseEventsAtPointInputTimestamps.get(0));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(MouseEventCapturingSpatialIndex.class.getCanonicalName(),
                _mouseEventCapturingSpatialIndex.getInterfaceName());
    }
}
