package inaugural.soliloquy.graphics.test.unit.rendering.renderers;

import inaugural.soliloquy.graphics.rendering.renderers.StackRendererImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.RenderableStack;
import soliloquy.specs.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.graphics.rendering.renderers.Renderers;
import soliloquy.specs.graphics.rendering.renderers.StackRenderer;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.randomFloatBox;
import static inaugural.soliloquy.tools.random.Random.randomLong;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

public class StackRendererImplTests {
    private final long MOST_RECENT_TIMESTAMP = randomLong();

    @Mock private RenderingBoundaries mockRenderingBoundaries;
    @Mock private Renderers mockRenderers;
    @Mock private RenderableStack mockStack;

    private StackRenderer stackRenderer;

    @BeforeEach
    public void setUp() {
        mockRenderers = mock(Renderers.class);
        mockRenderingBoundaries = mock(RenderingBoundaries.class);
        mockStack = mock(RenderableStack.class);

        stackRenderer =
                new StackRendererImpl(mockRenderers, mockRenderingBoundaries,
                        MOST_RECENT_TIMESTAMP);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new StackRendererImpl(null, mockRenderingBoundaries, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> new StackRendererImpl(mockRenderers, null, MOST_RECENT_TIMESTAMP));
    }

    @Test
    public void testRender() {
        var boundaries = randomFloatBox();
        //noinspection unchecked
        var mockBoundariesProvider = (ProviderAtTime<FloatBox>) mock(ProviderAtTime.class);
        when(mockBoundariesProvider.provide(anyLong())).thenReturn(boundaries);
        when(mockStack.getRenderingBoundariesProvider()).thenReturn(mockBoundariesProvider);
        var renderable1 = mock(Renderable.class);
        var renderable2 = mock(Renderable.class);
        var renderable3 = mock(Renderable.class);
        when(mockStack.renderablesByZIndexRepresentation()).thenReturn(
                mapOf(pairOf(1, listOf(renderable1)), pairOf(2, listOf(renderable2, renderable3))));

        stackRenderer.render(mockStack, MOST_RECENT_TIMESTAMP);

        var inOrder =
                inOrder(mockBoundariesProvider, mockRenderingBoundaries, mockStack, mockRenderers);
        inOrder.verify(mockStack, once()).getRenderingBoundariesProvider();
        inOrder.verify(mockBoundariesProvider, once()).provide(MOST_RECENT_TIMESTAMP);
        inOrder.verify(mockRenderingBoundaries, once()).pushNewBoundaries(boundaries);
        inOrder.verify(mockStack, once()).renderablesByZIndexRepresentation();
        inOrder.verify(mockRenderers, once()).render(renderable2, MOST_RECENT_TIMESTAMP);
        inOrder.verify(mockRenderers, once()).render(renderable3, MOST_RECENT_TIMESTAMP);
        inOrder.verify(mockRenderers, once()).render(renderable1, MOST_RECENT_TIMESTAMP);
        inOrder.verify(mockRenderingBoundaries, once()).popMostRecentBoundaries();
    }

    @Test
    public void testRenderOutdatedTimestamp() {
        assertThrows(IllegalArgumentException.class, () ->
                stackRenderer.render(mockStack, MOST_RECENT_TIMESTAMP - 1L));
    }
}
