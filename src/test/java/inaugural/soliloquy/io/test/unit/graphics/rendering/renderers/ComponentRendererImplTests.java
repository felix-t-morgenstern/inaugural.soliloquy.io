package inaugural.soliloquy.io.test.unit.graphics.rendering.renderers;

import inaugural.soliloquy.io.graphics.rendering.renderers.ComponentRendererImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.Renderable;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.io.graphics.rendering.renderers.ComponentRenderer;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;
import soliloquy.specs.ui.Component;

import java.util.Map;

import static inaugural.soliloquy.tools.collections.Collections.*;
import static inaugural.soliloquy.tools.random.Random.randomFloatBox;
import static inaugural.soliloquy.tools.random.Random.randomLong;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static inaugural.soliloquy.tools.testing.Mock.generateMockMap;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

@ExtendWith(MockitoExtension.class)
public class ComponentRendererImplTests {
    private final long MOST_RECENT_TIMESTAMP = randomLong();

    private Map<Class<?>, Renderer<? extends Renderable>> mockRenderers;

    @Mock private Renderable mockRenderable1;
    @Mock private Renderable mockRenderable2;
    @Mock private Renderable mockRenderable3;
    @Mock private Renderer<Renderable> mockRenderer;
    @Mock private RenderingBoundaries mockRenderingBoundaries;
    @Mock private Component mockComponent;

    private ComponentRenderer renderer;

    @BeforeEach
    public void setUp() {
        mockRenderers = generateMockMap(
                pairOf(mockRenderable1.getClass(), mockRenderer),
                pairOf(mockRenderable2.getClass(), mockRenderer),
                pairOf(mockRenderable3.getClass(), mockRenderer));

        renderer = new ComponentRendererImpl(mockRenderers, mockRenderingBoundaries,
                MOST_RECENT_TIMESTAMP);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new ComponentRendererImpl(null, mockRenderingBoundaries,
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> new ComponentRendererImpl(mockRenderers, null, MOST_RECENT_TIMESTAMP));
    }

    @Test
    public void testRender() {
        var boundaries = randomFloatBox();
        //noinspection unchecked
        var mockBoundariesProvider = (ProviderAtTime<FloatBox>) mock(ProviderAtTime.class);
        when(mockBoundariesProvider.provide(anyLong())).thenReturn(boundaries);
        when(mockComponent.getRenderingBoundariesProvider()).thenReturn(mockBoundariesProvider);
        when(mockRenderable1.getZ()).thenReturn(1);
        when(mockRenderable2.getZ()).thenReturn(2);
        when(mockRenderable3.getZ()).thenReturn(3);
        when(mockComponent.contents())
                .thenReturn(setOf(mockRenderable1, mockRenderable2, mockRenderable3));

        renderer.render(mockComponent, MOST_RECENT_TIMESTAMP);

        var inOrder =
                inOrder(mockBoundariesProvider, mockRenderingBoundaries, mockComponent,
                        mockRenderers, mockRenderer);
        inOrder.verify(mockComponent, once()).getRenderingBoundariesProvider();
        inOrder.verify(mockBoundariesProvider, once()).provide(MOST_RECENT_TIMESTAMP);
        inOrder.verify(mockRenderingBoundaries, once()).pushNewBoundaries(boundaries);
        inOrder.verify(mockComponent, once()).contents();
        inOrder.verify(mockRenderers, once()).get(mockRenderable3.getClass());
        inOrder.verify(mockRenderer, once()).render(mockRenderable3, MOST_RECENT_TIMESTAMP);
        inOrder.verify(mockRenderers, once()).get(mockRenderable2.getClass());
        inOrder.verify(mockRenderer, once()).render(mockRenderable2, MOST_RECENT_TIMESTAMP);
        inOrder.verify(mockRenderers, once()).get(mockRenderable1.getClass());
        inOrder.verify(mockRenderer, once()).render(mockRenderable1, MOST_RECENT_TIMESTAMP);
        inOrder.verify(mockRenderingBoundaries, once()).popMostRecentBoundaries();
    }

    @Test
    public void testRenderOutdatedTimestamp() {
        assertThrows(IllegalArgumentException.class, () ->
                renderer.render(mockComponent, MOST_RECENT_TIMESTAMP - 1L));
    }
}
