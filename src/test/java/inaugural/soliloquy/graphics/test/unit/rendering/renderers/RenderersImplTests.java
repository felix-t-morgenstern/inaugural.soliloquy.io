package inaugural.soliloquy.graphics.test.unit.rendering.renderers;

import inaugural.soliloquy.graphics.rendering.renderers.RenderersImpl;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.graphics.renderables.*;
import soliloquy.specs.graphics.rendering.RenderableStack;
import soliloquy.specs.graphics.rendering.renderers.Renderer;
import soliloquy.specs.graphics.rendering.renderers.Renderers;
import soliloquy.specs.graphics.rendering.renderers.StackRenderer;

import static inaugural.soliloquy.tools.random.Random.randomLong;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RenderersImplTests {
    private final Long TIMESTAMP = randomLong();

    @Mock private StackRenderer mockStackRenderer;
    @Mock private AntialiasedLineSegmentRenderable mockAntialiasedLineSegmentRenderable;
    @Mock private RenderableStack mockRenderableStack;
    @Mock private Renderer<AntialiasedLineSegmentRenderable> mockAntialiasedLineSegmentRenderer;
    @Mock private Renderer<ImageAssetSetRenderable> mockImageAssetSetRenderer;
    @Mock private Renderer<TextLineRenderable> mockTextLineRenderer;

    @Mock private TimestampValidator mockTimestampValidator;

    private Renderers renderers;

    @BeforeEach
    public void setUp() {
        lenient().when(mockTimestampValidator.mostRecentTimestamp()).thenReturn(TIMESTAMP);

        renderers = new RenderersImpl(mockTimestampValidator);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new RenderersImpl(null));
    }

    @Test
    public void testRenderStack() {
        renderers.registerStackRenderer(mockStackRenderer);
        renderers.render(mockRenderableStack, TIMESTAMP);

        verify(mockStackRenderer).render(mockRenderableStack, TIMESTAMP);
    }

    @Test
    public void testRenderStackWithoutStackRendererRegistered() {
        assertThrows(IllegalStateException.class,
                () -> renderers.render(mockRenderableStack, randomLong()));
    }

    @Test
    public void testRegisterStackRendererWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> renderers.registerStackRenderer(null));
    }

    @Test
    public void testRegisterRendererAndRender() {
        renderers.registerRenderer(mockAntialiasedLineSegmentRenderable.getClass(),
                mockAntialiasedLineSegmentRenderer);
        renderers.registerRenderer(ImageAssetSetRenderable.class, mockImageAssetSetRenderer);
        renderers.registerRenderer(TextLineRenderable.class, mockTextLineRenderer);

        renderers.render(mockAntialiasedLineSegmentRenderable, TIMESTAMP);

        verify(mockTimestampValidator).validateTimestamp(TIMESTAMP);
        verify(mockAntialiasedLineSegmentRenderer).render(mockAntialiasedLineSegmentRenderable,
                TIMESTAMP);
    }

    @Test
    public void testRenderUnregisteredRenderableType() {
        var unregisteredRenderable = mock(CircleRenderable.class);

        assertThrows(IllegalArgumentException.class,
                () -> renderers.render(unregisteredRenderable, randomLong()));
    }

    @Test
    public void testRenderWithInvalidArgs() {

    }

    @Test
    public void testRenderWithInvalidTimestamp() {
        doThrow(new IllegalArgumentException()).when(mockTimestampValidator)
                .validateTimestamp(anyLong());

        assertThrows(IllegalArgumentException.class,
                () -> renderers.render(mock(Renderable.class), randomLong()));
    }

    @Test
    public void testMostRecentTimestamp() {
        var mostRecentTimestamp = renderers.mostRecentTimestamp();

        assertEquals(TIMESTAMP, mostRecentTimestamp);
        verify(mockTimestampValidator).mostRecentTimestamp();
    }
}
