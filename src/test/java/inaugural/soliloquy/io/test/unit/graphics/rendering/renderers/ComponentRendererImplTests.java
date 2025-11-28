package inaugural.soliloquy.io.test.unit.graphics.rendering.renderers;

import inaugural.soliloquy.io.graphics.rendering.renderers.ComponentRendererImpl;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.entities.BiConsumer;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.Renderable;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.io.graphics.rendering.renderers.ComponentRenderer;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import java.util.Map;

import static inaugural.soliloquy.tools.collections.Collections.*;
import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static inaugural.soliloquy.tools.testing.Mock.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

@ExtendWith(MockitoExtension.class)
public class ComponentRendererImplTests {
    private Map<Class<?>, Renderer<? extends Renderable>> mockRenderers;

    @Mock private Renderable mockRenderable1;
    @Mock private Renderable mockRenderable2;
    @Mock private Renderable mockRenderable3;
    @Mock private Renderer<Renderable> mockRenderer;
    @Mock private RenderingBoundaries mockRenderingBoundaries;
    @Mock private Component mockComponent;
    @Mock private BiConsumer<Component, Long> mockPrerenderComponent;
    @Mock private TimestampValidator mockTimestampValidator;

    private ComponentRenderer renderer;

    @BeforeEach
    public void setUp() {
        mockRenderers = generateMockMap(
                pairOf(mockRenderable1.getClass(), mockRenderer),
                pairOf(mockRenderable2.getClass(), mockRenderer),
                pairOf(mockRenderable3.getClass(), mockRenderer));

        renderer = new ComponentRendererImpl(mockRenderers, mockPrerenderComponent,
                mockRenderingBoundaries, mockTimestampValidator);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new ComponentRendererImpl(null, mockPrerenderComponent,
                        mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new ComponentRendererImpl(mockRenderers, null, mockRenderingBoundaries,
                        mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new ComponentRendererImpl(mockRenderers, mockPrerenderComponent, null,
                        mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new ComponentRendererImpl(mockRenderers, mockPrerenderComponent,
                        mockRenderingBoundaries, null));
    }

    @Test
    public void testRender() {
        var timestamp = randomLong();
        var boundaries = randomFloatBox();
        //noinspection unchecked
        var mockBoundariesProvider = (ProviderAtTime<FloatBox>) mock(ProviderAtTime.class);
        when(mockBoundariesProvider.provide(anyLong())).thenReturn(boundaries);
        when(mockComponent.getRenderingBoundariesProvider()).thenReturn(mockBoundariesProvider);
        when(mockRenderable1.getZ()).thenReturn(1);
        when(mockRenderable2.getZ()).thenReturn(2);
        when(mockRenderable3.getZ()).thenReturn(3);
        when(mockComponent.contentsRepresentation())
                .thenReturn(setOf(mockRenderable1, mockRenderable2, mockRenderable3));

        renderer.render(mockComponent, timestamp);

        var inOrder =
                inOrder(mockPrerenderComponent, mockBoundariesProvider, mockRenderingBoundaries,
                        mockComponent, mockRenderers, mockRenderer, mockTimestampValidator);
        inOrder.verify(mockTimestampValidator, once()).validateTimestamp(timestamp);
        inOrder.verify(mockPrerenderComponent, once()).accept(mockComponent, timestamp);
        inOrder.verify(mockComponent, once()).getRenderingBoundariesProvider();
        inOrder.verify(mockBoundariesProvider, once()).provide(timestamp);
        inOrder.verify(mockRenderingBoundaries, once()).pushNewBoundaries(boundaries);
        inOrder.verify(mockComponent, once()).contentsRepresentation();
        verify(mockRenderers, times(3)).get(mockRenderable1.getClass());
        verify(mockRenderer, once()).render(mockRenderable3, timestamp);
        verify(mockRenderer, once()).render(mockRenderable2, timestamp);
        verify(mockRenderer, once()).render(mockRenderable1, timestamp);
        inOrder.verify(mockRenderingBoundaries, once()).popMostRecentBoundaries();
    }
}
