package inaugural.soliloquy.io.test.unit.mouse;

import inaugural.soliloquy.io.mouse.MouseEventCapturingSpatialIndex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.ImageAssetRenderable;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

@ExtendWith(MockitoExtension.class)
public class MouseEventCapturingSpatialIndexTests {
    private final int Z_HIGHER = randomInt();
    private final int Z_LOWER = randomIntWithInclusiveCeiling(Z_HIGHER - 1);
    private final int COMPONENT_TIER_HIGHER = randomInt();
    private final int COMPONENT_TIER_LOWER = randomIntWithInclusiveFloor(COMPONENT_TIER_HIGHER + 1);
    private final FloatBox DIMENSIONS_1 = floatBoxOf(.1f, .1f, .5f, .5f);
    private final FloatBox DIMENSIONS_2 = floatBoxOf(.2f, .2f, .6f, .6f);
    private final FloatBox DIMENSIONS_3 = floatBoxOf(.3f, .3f, .7f, .7f);
    private final FloatBox DIMENSIONS_4 = floatBoxOf(.4f, .4f, .8f, .8f);

    @Mock private Component mockComponentHigher;
    @Mock private Component mockComponentLower;
    @Mock private ProviderAtTime<FloatBox> mockDimensionsProvider1;
    @Mock private ProviderAtTime<FloatBox> mockDimensionsProvider2;
    @Mock private ProviderAtTime<FloatBox> mockDimensionsProvider3;
    @Mock private ProviderAtTime<FloatBox> mockDimensionsProvider4;
    @Mock private ImageAssetRenderable mockRenderable1;
    @Mock private ImageAssetRenderable mockRenderable2;
    @Mock private ImageAssetRenderable mockRenderable3;
    @Mock private ImageAssetRenderable mockRenderable4;

    private MouseEventCapturingSpatialIndex capturing;

    @BeforeEach
    public void setUp() {
        lenient().when(mockComponentHigher.tier()).thenReturn(COMPONENT_TIER_HIGHER);
        lenient().when(mockComponentLower.tier()).thenReturn(COMPONENT_TIER_LOWER);

        lenient().when(mockDimensionsProvider1.provide(anyLong())).thenReturn(DIMENSIONS_1);
        lenient().when(mockDimensionsProvider2.provide(anyLong())).thenReturn(DIMENSIONS_2);
        lenient().when(mockDimensionsProvider3.provide(anyLong())).thenReturn(DIMENSIONS_3);
        lenient().when(mockDimensionsProvider4.provide(anyLong())).thenReturn(DIMENSIONS_4);

        lenient().when(mockRenderable1.getRenderingDimensionsProvider())
                .thenReturn(mockDimensionsProvider1);
        lenient().when(mockRenderable2.getRenderingDimensionsProvider())
                .thenReturn(mockDimensionsProvider2);
        lenient().when(mockRenderable3.getRenderingDimensionsProvider())
                .thenReturn(mockDimensionsProvider3);
        lenient().when(mockRenderable4.getRenderingDimensionsProvider())
                .thenReturn(mockDimensionsProvider4);

        lenient().when(mockRenderable1.getRenderingDimensionsProvider())
                .thenReturn(mockDimensionsProvider1);
        lenient().when(mockRenderable2.getRenderingDimensionsProvider())
                .thenReturn(mockDimensionsProvider2);
        lenient().when(mockRenderable3.getRenderingDimensionsProvider())
                .thenReturn(mockDimensionsProvider3);
        lenient().when(mockRenderable4.getRenderingDimensionsProvider())
                .thenReturn(mockDimensionsProvider4);
        lenient().when(mockRenderable1.getZ()).thenReturn(Z_HIGHER);
        lenient().when(mockRenderable2.getZ()).thenReturn(Z_LOWER);
        lenient().when(mockRenderable3.getZ()).thenReturn(Z_HIGHER);
        lenient().when(mockRenderable4.getZ()).thenReturn(Z_LOWER);
        lenient().when(mockRenderable1.getCapturesMouseEvents()).thenReturn(true);
        lenient().when(mockRenderable2.getCapturesMouseEvents()).thenReturn(true);
        lenient().when(mockRenderable3.getCapturesMouseEvents()).thenReturn(true);
        lenient().when(mockRenderable4.getCapturesMouseEvents()).thenReturn(true);
        lenient().when(mockRenderable1.capturesMouseEventAtPoint(any(), anyLong()))
                .thenReturn(true);
        lenient().when(mockRenderable2.capturesMouseEventAtPoint(any(), anyLong()))
                .thenReturn(true);
        lenient().when(mockRenderable3.capturesMouseEventAtPoint(any(), anyLong()))
                .thenReturn(true);
        lenient().when(mockRenderable4.capturesMouseEventAtPoint(any(), anyLong()))
                .thenReturn(true);
        lenient().when(mockRenderable1.getContainingComponent()).thenReturn(mockComponentHigher);
        lenient().when(mockRenderable2.getContainingComponent()).thenReturn(mockComponentHigher);
        lenient().when(mockRenderable3.getContainingComponent()).thenReturn(mockComponentLower);
        lenient().when(mockRenderable4.getContainingComponent()).thenReturn(mockComponentLower);

        capturing = new MouseEventCapturingSpatialIndex();
    }

    @Test
    public void testPutRenderableAndGetCapturingRenderable() {
        assertNull(capturing.getCapturingRenderable(vertexOf(.1f, .1f), 0L));

        capturing.putRenderable(mockRenderable1);

        assertSame(mockRenderable1,
                capturing.getCapturingRenderable(vertexOf(.1f, .1f), 0L));
        assertNull(capturing
                .getCapturingRenderable(vertexOf(0.09999f, 0.09999f), 0L));
        assertSame(mockRenderable1,
                capturing.getCapturingRenderable(vertexOf(.5f, .5f), 0L));
        assertNull(capturing
                .getCapturingRenderable(vertexOf(0.50001f, 0.50001f), 0L));
    }

    @Test
    public void testRemoveAbsentRenderableDoesNotThrow() {
        capturing.removeRenderable(mockRenderable1);
    }

    @Test
    public void testPutRenderableWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> capturing.putRenderable(null));
    }

    @Test
    public void testRemoveWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                capturing.removeRenderable(null));
    }

    @Test
    public void testRemoveRenderable() {
        assertNull(capturing.getCapturingRenderable(vertexOf(.1f, .1f), 0L));

        capturing.putRenderable(mockRenderable1);

        assertSame(mockRenderable1,
                capturing.getCapturingRenderable(vertexOf(.1f, .1f), 0L));

        capturing.removeRenderable(mockRenderable1);

        assertNull(capturing.getCapturingRenderable(vertexOf(.1f, .1f), 0L));
    }

    @Test
    public void testGetCapturingRenderableWithHighestZIndex() {
        capturing.putRenderable(mockRenderable1);
        capturing.putRenderable(mockRenderable2);
        capturing.putRenderable(mockRenderable3);
        capturing.putRenderable(mockRenderable4);

        assertSame(mockRenderable1,
                capturing.getCapturingRenderable(vertexOf(.45f, .45f), 0L));
    }

    @Test
    public void testGetCapturingRenderableAtPointIgnoresRenderablesNotCapturing() {
        when(mockRenderable1.capturesMouseEventAtPoint(any(), anyLong())).thenReturn(false);
        when(mockRenderable2.capturesMouseEventAtPoint(any(), anyLong())).thenReturn(false);

        capturing.putRenderable(mockRenderable1);
        capturing.putRenderable(mockRenderable2);
        capturing.putRenderable(mockRenderable3);
        capturing.putRenderable(mockRenderable4);

        assertSame(mockRenderable3, capturing
                .getCapturingRenderable(vertexOf(.454f, .456f), 789L));
        verify(mockRenderable1, once()).capturesMouseEventAtPoint(any(), anyLong());
        verify(mockRenderable1, once()).capturesMouseEventAtPoint(
                eq(vertexOf(0.454f, 0.456f)), eq(789L));
        verify(mockRenderable2, once()).capturesMouseEventAtPoint(any(), anyLong());
        verify(mockRenderable2, once()).capturesMouseEventAtPoint(
                eq(vertexOf(0.454f, 0.456f)), eq(789L));
    }
}
