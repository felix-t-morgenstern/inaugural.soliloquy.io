package inaugural.soliloquy.io.test.unit.mouse;

import inaugural.soliloquy.io.mouse.MouseEventCapturingSpatialIndexImpl;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeImageAssetRenderable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.mouse.MouseEventCapturingSpatialIndex;

import static org.junit.jupiter.api.Assertions.*;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

public class MouseEventCapturingSpatialIndexImplTests {
    private final FakeImageAssetRenderable ImageAssetRenderable1 = new FakeImageAssetRenderable();
    private final FakeImageAssetRenderable ImageAssetRenderable2 = new FakeImageAssetRenderable();
    private final FakeImageAssetRenderable ImageAssetRenderable3 = new FakeImageAssetRenderable();
    private final FakeImageAssetRenderable ImageAssetRenderable4 = new FakeImageAssetRenderable();

    private final FloatBox RenderingDimensions1 = floatBoxOf(.1f, .1f, .5f, .5f);
    private final FloatBox RenderingDimensions2 = floatBoxOf(.2f, .2f, .6f, .6f);
    private final FloatBox RenderingDimensions3 = floatBoxOf(.3f, .3f, .7f, .7f);
    private final FloatBox RenderingDimensions4 = floatBoxOf(.4f, .4f, .8f, .8f);

    private MouseEventCapturingSpatialIndex mouseEventCapturingSpatialIndex;

    @BeforeEach
    public void setUp() {
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

        mouseEventCapturingSpatialIndex = new MouseEventCapturingSpatialIndexImpl();
    }

    @Test
    public void testPutRenderableAndGetCapturingRenderableAtPoint() {
        assertNull(mouseEventCapturingSpatialIndex.getCapturingRenderableAtPoint(vertexOf(.1f, .1f), 0L));

        mouseEventCapturingSpatialIndex.putRenderable(ImageAssetRenderable1, RenderingDimensions1);

        assertSame(ImageAssetRenderable1,
                mouseEventCapturingSpatialIndex.getCapturingRenderableAtPoint(vertexOf(.1f, .1f), 0L));
        assertNull(mouseEventCapturingSpatialIndex
                .getCapturingRenderableAtPoint(vertexOf(0.09999f, 0.09999f), 0L));
        assertSame(ImageAssetRenderable1,
                mouseEventCapturingSpatialIndex.getCapturingRenderableAtPoint(vertexOf(.5f, .5f), 0L));
        assertNull(mouseEventCapturingSpatialIndex
                .getCapturingRenderableAtPoint(vertexOf(0.50001f, 0.50001f), 0L));
    }

    @Test
    public void testRemoveAbsentRenderableDoesNotThrow() {
        mouseEventCapturingSpatialIndex.removeRenderable(ImageAssetRenderable1);
    }

    @Test
    public void testPutRenderableWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                mouseEventCapturingSpatialIndex.putRenderable(null, RenderingDimensions1));
        assertThrows(IllegalArgumentException.class, () ->
                mouseEventCapturingSpatialIndex.putRenderable(ImageAssetRenderable1, null));
        ImageAssetRenderable1.CapturesMouseEvents = false;
        assertThrows(IllegalArgumentException.class, () ->
                mouseEventCapturingSpatialIndex.putRenderable(ImageAssetRenderable1,
                        RenderingDimensions1));
    }

    @Test
    public void testRemoveWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                mouseEventCapturingSpatialIndex.removeRenderable(null));
    }

    @Test
    public void testRemoveRenderable() {
        assertNull(mouseEventCapturingSpatialIndex.getCapturingRenderableAtPoint(vertexOf(.1f, .1f), 0L));

        mouseEventCapturingSpatialIndex.putRenderable(ImageAssetRenderable1, RenderingDimensions1);

        assertSame(ImageAssetRenderable1,
                mouseEventCapturingSpatialIndex.getCapturingRenderableAtPoint(vertexOf(.1f, .1f), 0L));

        mouseEventCapturingSpatialIndex.removeRenderable(ImageAssetRenderable1);

        assertNull(mouseEventCapturingSpatialIndex.getCapturingRenderableAtPoint(vertexOf(.1f, .1f), 0L));
    }

    @Test
    public void testUpdateRenderable() {
        mouseEventCapturingSpatialIndex.putRenderable(ImageAssetRenderable1, RenderingDimensions1);

        assertSame(ImageAssetRenderable1,
                mouseEventCapturingSpatialIndex.getCapturingRenderableAtPoint(vertexOf(.1f, .1f), 0L));
        assertNull(mouseEventCapturingSpatialIndex.getCapturingRenderableAtPoint(vertexOf(.6f, .6f), 0L));

        mouseEventCapturingSpatialIndex.putRenderable(ImageAssetRenderable1, RenderingDimensions2);

        assertNull(mouseEventCapturingSpatialIndex.getCapturingRenderableAtPoint(vertexOf(.1f, .1f), 0L));
        assertSame(ImageAssetRenderable1,
                mouseEventCapturingSpatialIndex.getCapturingRenderableAtPoint(vertexOf(.6f, .6f), 0L));
    }

    @Test
    public void testGetCapturingRenderableAtPointWithHighestZIndex() {
        mouseEventCapturingSpatialIndex.putRenderable(ImageAssetRenderable1, RenderingDimensions1);
        mouseEventCapturingSpatialIndex.putRenderable(ImageAssetRenderable2, RenderingDimensions2);
        mouseEventCapturingSpatialIndex.putRenderable(ImageAssetRenderable3, RenderingDimensions3);
        mouseEventCapturingSpatialIndex.putRenderable(ImageAssetRenderable4, RenderingDimensions4);

        assertSame(ImageAssetRenderable4,
                mouseEventCapturingSpatialIndex.getCapturingRenderableAtPoint(vertexOf(.45f, .45f), 0L));
    }

    @Test
    public void testGetCapturingRenderableAtPointIgnoresRenderablesNotCapturingAtPoint() {
        ImageAssetRenderable3.CapturesMouseEventsAtPoint = false;
        ImageAssetRenderable4.CapturesMouseEventsAtPoint = false;

        mouseEventCapturingSpatialIndex.putRenderable(ImageAssetRenderable1, RenderingDimensions1);
        mouseEventCapturingSpatialIndex.putRenderable(ImageAssetRenderable2, RenderingDimensions2);
        mouseEventCapturingSpatialIndex.putRenderable(ImageAssetRenderable3, RenderingDimensions3);
        mouseEventCapturingSpatialIndex.putRenderable(ImageAssetRenderable4, RenderingDimensions4);

        assertSame(ImageAssetRenderable2, mouseEventCapturingSpatialIndex
                .getCapturingRenderableAtPoint(vertexOf(.454f, .456f), 789L));
        assertEquals(1, ImageAssetRenderable4.CapturesMouseEventsAtPointInputLocations.size());
        assertEquals(1, ImageAssetRenderable4.CapturesMouseEventsAtPointInputTimestamps.size());
        assertEquals(0.454f,
                ImageAssetRenderable4.CapturesMouseEventsAtPointInputLocations.getFirst().X);
        assertEquals(0.456f,
                ImageAssetRenderable4.CapturesMouseEventsAtPointInputLocations.getFirst().Y);
        assertEquals(789L,
                (long) ImageAssetRenderable4.CapturesMouseEventsAtPointInputTimestamps.getFirst());
        assertEquals(1, ImageAssetRenderable3.CapturesMouseEventsAtPointInputLocations.size());
        assertEquals(1, ImageAssetRenderable3.CapturesMouseEventsAtPointInputTimestamps.size());
        assertEquals(0.454f,
                ImageAssetRenderable3.CapturesMouseEventsAtPointInputLocations.getFirst().X);
        assertEquals(0.456f,
                ImageAssetRenderable3.CapturesMouseEventsAtPointInputLocations.getFirst().Y);
        assertEquals(789L,
                (long) ImageAssetRenderable3.CapturesMouseEventsAtPointInputTimestamps.getFirst());
    }
}
