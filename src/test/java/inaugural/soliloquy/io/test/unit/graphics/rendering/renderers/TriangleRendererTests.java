package inaugural.soliloquy.io.test.unit.graphics.rendering.renderers;

import inaugural.soliloquy.io.graphics.rendering.renderers.TriangleRenderer;
import inaugural.soliloquy.io.graphics.rendering.renderers.TriangleSegmentRenderer;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.TriangleRenderable;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import java.awt.*;

import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TriangleRendererTests {
    private final Vertex VERTEX_1 = randomVertex();
    private final Color COLOR_1 = randomColor();
    private final Vertex VERTEX_2 = randomVertex();
    private final Color COLOR_2 = randomColor();
    private final Vertex VERTEX_3 = randomVertex();
    private final Color COLOR_3 = randomColor();
    private final int TEXTURE_ID = randomInt();
    private final float TEXTURE_WIDTH = randomFloat();
    private final float TEXTURE_HEIGHT = randomFloat();

    @Mock private ProviderAtTime<Vertex> mockVertex1Provider;
    @Mock private ProviderAtTime<Color> mockColor1Provider;
    @Mock private ProviderAtTime<Vertex> mockVertex2Provider;
    @Mock private ProviderAtTime<Color> mockColor2Provider;
    @Mock private ProviderAtTime<Vertex> mockVertex3Provider;
    @Mock private ProviderAtTime<Color> mockColor3Provider;
    @Mock private ProviderAtTime<Integer> mockTextureIdProvider;
    @Mock private ProviderAtTime<Float> mockTextureWidthProvider;
    @Mock private ProviderAtTime<Float> mockTextureHeightProvider;

    @Mock private TriangleRenderable mockRenderable;

    @Mock private TimestampValidator mockTimestampValidator;
    @Mock private TriangleSegmentRenderer mockTriangleSegmentRenderer;

    private Renderer<TriangleRenderable> renderer;

    @BeforeEach
    public void setUp() {
        lenient().when(mockVertex1Provider.provide(anyLong())).thenReturn(VERTEX_1);
        lenient().when(mockColor1Provider.provide(anyLong())).thenReturn(COLOR_1);
        lenient().when(mockVertex2Provider.provide(anyLong())).thenReturn(VERTEX_2);
        lenient().when(mockColor2Provider.provide(anyLong())).thenReturn(COLOR_2);
        lenient().when(mockVertex3Provider.provide(anyLong())).thenReturn(VERTEX_3);
        lenient().when(mockColor3Provider.provide(anyLong())).thenReturn(COLOR_3);
        lenient().when(mockTextureIdProvider.provide(anyLong())).thenReturn(TEXTURE_ID);
        lenient().when(mockTextureWidthProvider.provide(anyLong())).thenReturn(TEXTURE_WIDTH);
        lenient().when(mockTextureHeightProvider.provide(anyLong())).thenReturn(TEXTURE_HEIGHT);

        lenient().when(mockRenderable.getVertex1Provider()).thenReturn(mockVertex1Provider);
        lenient().when(mockRenderable.getVertex1ColorProvider()).thenReturn(mockColor1Provider);
        lenient().when(mockRenderable.getVertex2Provider()).thenReturn(mockVertex2Provider);
        lenient().when(mockRenderable.getVertex2ColorProvider()).thenReturn(mockColor2Provider);
        lenient().when(mockRenderable.getVertex3Provider()).thenReturn(mockVertex3Provider);
        lenient().when(mockRenderable.getVertex3ColorProvider()).thenReturn(mockColor3Provider);
        lenient().when(mockRenderable.getTextureIdProvider()).thenReturn(mockTextureIdProvider);
        lenient().when(mockRenderable.getTextureTileWidthProvider()).thenReturn(mockTextureWidthProvider);
        lenient().when(mockRenderable.getTextureTileHeightProvider()).thenReturn(mockTextureHeightProvider);

        renderer = new TriangleRenderer(mockTimestampValidator, mockTriangleSegmentRenderer);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new TriangleRenderer(null, mockTriangleSegmentRenderer));
        assertThrows(IllegalArgumentException.class,
                () -> new TriangleRenderer(mockTimestampValidator, null));
    }

    @Test
    public void testRender() {
        var timestamp = randomLong();

        renderer.render(mockRenderable, timestamp);

        verify(mockRenderable, once()).getVertex1Provider();
        verify(mockVertex1Provider, once()).provide(timestamp);

        verify(mockRenderable, once()).getVertex1ColorProvider();
        verify(mockColor1Provider, once()).provide(timestamp);

        verify(mockRenderable, once()).getVertex2Provider();
        verify(mockVertex2Provider, once()).provide(timestamp);

        verify(mockRenderable, once()).getVertex2ColorProvider();
        verify(mockColor2Provider, once()).provide(timestamp);

        verify(mockRenderable, once()).getVertex3Provider();
        verify(mockVertex3Provider, once()).provide(timestamp);

        verify(mockRenderable, once()).getVertex3ColorProvider();
        verify(mockColor3Provider, once()).provide(timestamp);

        verify(mockRenderable, once()).getTextureIdProvider();
        verify(mockTextureIdProvider, once()).provide(timestamp);

        verify(mockRenderable, once()).getTextureTileWidthProvider();
        verify(mockTextureWidthProvider, once()).provide(timestamp);

        verify(mockRenderable, once()).getTextureTileHeightProvider();
        verify(mockTextureHeightProvider, once()).provide(timestamp);

        verify(mockTriangleSegmentRenderer, once()).draw(
                VERTEX_1,
                COLOR_1,
                VERTEX_2,
                COLOR_2,
                VERTEX_3,
                COLOR_3,
                TEXTURE_ID,
                TEXTURE_WIDTH,
                TEXTURE_HEIGHT
        );
    }
}
