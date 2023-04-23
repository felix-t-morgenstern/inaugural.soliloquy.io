package inaugural.soliloquy.graphics.test.unit.rendering.renderers;

import inaugural.soliloquy.graphics.rendering.renderers.RenderersImpl;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import soliloquy.specs.graphics.renderables.*;
import soliloquy.specs.graphics.rendering.renderers.Renderer;
import soliloquy.specs.graphics.rendering.renderers.Renderers;

import static inaugural.soliloquy.tools.random.Random.randomLong;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RenderersImplTests {
    private final Long TIMESTAMP = randomLong();

    @Mock private AntialiasedLineSegmentRenderable mockAntialiasedLineSegmentRenderable;
    @Mock private Renderer<AntialiasedLineSegmentRenderable> mockAntialiasedLineSegmentRenderer;
    @Mock private Renderer<ImageAssetSetRenderable> mockImageAssetSetRenderer;
    @Mock private Renderer<TextLineRenderable> mockTextLineRenderer;

    @Mock private TimestampValidator mockTimestampValidator;

    private Renderers renderers;

    @Before
    public void setUp() {
        when(mockAntialiasedLineSegmentRenderable.getInterfaceName())
                .thenReturn(AntialiasedLineSegmentRenderable.class.getCanonicalName());
        when(mockTimestampValidator.mostRecentTimestamp()).thenReturn(TIMESTAMP);

        renderers = new RenderersImpl(mockTimestampValidator);
    }

    @Test
    public void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> new RenderersImpl(null));
    }

    @Test
    public void testRegisterRendererAndRender() {
        renderers.registerRenderer(AntialiasedLineSegmentRenderable.class.getCanonicalName(), mockAntialiasedLineSegmentRenderer);
        renderers.registerRenderer(ImageAssetSetRenderable.class.getCanonicalName(), mockImageAssetSetRenderer);
        renderers.registerRenderer(TextLineRenderable.class.getCanonicalName(), mockTextLineRenderer);

        renderers.render(mockAntialiasedLineSegmentRenderable, TIMESTAMP);

        verify(mockTimestampValidator).validateTimestamp(TIMESTAMP);
        verify(mockAntialiasedLineSegmentRenderer).render(mockAntialiasedLineSegmentRenderable, TIMESTAMP);
    }

    @Test
    public void testRenderUnregisteredRenderableType() {
        var unregisteredRenderable = mock(CircleRenderable.class);

        assertThrows(IllegalArgumentException.class, () -> renderers.render(unregisteredRenderable, randomLong()));
    }

    @Test
    public void testRenderWithInvalidParams() {

    }

    @Test
    public void testRenderWithInvalidTimestamp() {
        doThrow(new IllegalArgumentException()).when(mockTimestampValidator).validateTimestamp(anyLong());

        assertThrows(IllegalArgumentException.class, () -> renderers.render(mock(Renderable.class), randomLong()));
    }

    @Test
    public void testMostRecentTimestamp() {
        var mostRecentTimestamp = renderers.mostRecentTimestamp();

        assertEquals(TIMESTAMP, mostRecentTimestamp);
        verify(mockTimestampValidator).mostRecentTimestamp();
    }

    @Test
    public void testGetInterfaceName() {
        assertEquals(Renderer.class.getCanonicalName(), renderers.getInterfaceName());
    }
}
