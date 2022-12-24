package inaugural.soliloquy.graphics.test.unit.rendering.renderers;

import inaugural.soliloquy.graphics.rendering.renderers.StackRendererImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.RenderableStack;
import soliloquy.specs.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.graphics.rendering.renderers.Renderer;
import soliloquy.specs.graphics.rendering.renderers.StackRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static inaugural.soliloquy.tools.random.Random.randomLong;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StackRendererImplTests {
    private final long MOST_RECENT_TIMESTAMP = randomLong();

    @Mock private RenderingBoundaries mockRenderingBoundaries;
    @Mock private Renderer<Renderable> mockRenderer;
    @Mock private RenderableStack mockStack;

    private StackRenderer stackRenderer;

    @BeforeEach
    void setUp() {
        //noinspection unchecked
        mockRenderer = (Renderer<Renderable>) mock(Renderer.class);
        mockRenderingBoundaries = mock(RenderingBoundaries.class);
        mockStack = mock(RenderableStack.class);

        stackRenderer =
                new StackRendererImpl(mockRenderer, mockRenderingBoundaries, MOST_RECENT_TIMESTAMP);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new StackRendererImpl(null, mockRenderingBoundaries, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> new StackRendererImpl(mockRenderer, null, MOST_RECENT_TIMESTAMP));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(StackRenderer.class.getCanonicalName(), stackRenderer.getInterfaceName());
    }

    @Test
    void testRender() {
        FloatBox mockBoundaries = mock(FloatBox.class);
        //noinspection unchecked
        ProviderAtTime<FloatBox> mockBoundariesProvider =
                (ProviderAtTime<FloatBox>) mock(ProviderAtTime.class);
        when(mockBoundariesProvider.provide(anyLong())).thenReturn(mockBoundaries);
        when(mockStack.getRenderingBoundariesProvider()).thenReturn(mockBoundariesProvider);
        Renderable renderable1 = mock(Renderable.class);
        Renderable renderable2 = mock(Renderable.class);
        Renderable renderable3 = mock(Renderable.class);
        when(mockStack.renderablesByZIndexRepresentation()).thenReturn(
                new HashMap<Integer, List<Renderable>>() {{
                    put(1, new ArrayList<Renderable>() {{
                        add(renderable1);
                    }});
                    put(2, new ArrayList<Renderable>() {{
                        add(renderable2);
                        add(renderable3);
                    }});
                }});

        stackRenderer.render(mockStack, MOST_RECENT_TIMESTAMP);

        InOrder inOrder =
                inOrder(mockBoundariesProvider, mockRenderingBoundaries, mockStack, mockRenderer);
        inOrder.verify(mockStack, times(1)).getRenderingBoundariesProvider();
        inOrder.verify(mockBoundariesProvider, times(1)).provide(MOST_RECENT_TIMESTAMP);
        inOrder.verify(mockRenderingBoundaries, times(1)).pushNewBoundaries(mockBoundaries);
        inOrder.verify(mockStack, times(1)).renderablesByZIndexRepresentation();
        inOrder.verify(mockRenderer, times(1)).render(renderable2, MOST_RECENT_TIMESTAMP);
        inOrder.verify(mockRenderer, times(1)).render(renderable3, MOST_RECENT_TIMESTAMP);
        inOrder.verify(mockRenderer, times(1)).render(renderable1, MOST_RECENT_TIMESTAMP);
        inOrder.verify(mockRenderingBoundaries, times(1)).popMostRecentBoundaries();
    }

    @Test
    void testRenderOutdatedTimestamp() {
        assertThrows(IllegalArgumentException.class, () ->
                stackRenderer.render(mockStack, MOST_RECENT_TIMESTAMP - 1L));
    }
}
