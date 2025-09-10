package inaugural.soliloquy.io.test.integration.display.mockedsetup.rendering.renderers.rasterizedlinesegmentrenderer;

import inaugural.soliloquy.io.graphics.renderables.RasterizedLineSegmentRenderableImpl;
import inaugural.soliloquy.io.graphics.rendering.renderers.RasterizedLineSegmentRenderer;
import inaugural.soliloquy.io.test.integration.display.mockedsetup.DisplayTest;
import soliloquy.specs.io.graphics.renderables.RasterizedLineSegmentRenderable;
import soliloquy.specs.io.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import java.util.Set;

import static inaugural.soliloquy.tools.collections.Collections.setOf;
import static inaugural.soliloquy.tools.random.Random.randomColor;
import static org.mockito.Mockito.when;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 800x600 pixels for 2000ms with a titlebar reading "New
 * Window"
 * 2. During the 3000ms, a teal rasterized line segment will be rendered, starting from
 * (0.25,0.25), and ending at (0.75, 0.5).
 * 3. The window will then close
 */
class RasterizedLineSegmentRendererSimpleTest extends DisplayTest {
    private static RasterizedLineSegmentRenderable RasterizedLineSegmentRenderable1;
    private static RasterizedLineSegmentRenderable RasterizedLineSegmentRenderable2;
    private static Renderer<RasterizedLineSegmentRenderable> RasterizedLineSegmentRenderer;

    public static void main(String[] args) {
        runTest(
                RasterizedLineSegmentRendererSimpleTest::
                        generateRenderablesAndRenderersWithMeshAndShader,
                () -> {},
                DisplayTest::closeAfterSomeTime
        );
    }

    /** @noinspection rawtypes */
    private static Set<Renderer> generateRenderablesAndRenderersWithMeshAndShader(
            WindowResolutionManager windowResolutionManager) {
        RasterizedLineSegmentRenderable1 = new RasterizedLineSegmentRenderableImpl(
                // NB: The coordinates are in this order to ensure that
                // RasterizedLineSegmentRenderable does not care about order
                staticProvider(vertexOf(0.75f, 0.5f)),
                staticProvider(vertexOf(0.25f, 0.25f)),
                staticProvider(6f), (short) 0xAAAA, (short) 16,
                staticProvider(randomColor()),
                1, java.util.UUID.randomUUID(), MockFirstChildComponent);
        RasterizedLineSegmentRenderable2 = new RasterizedLineSegmentRenderableImpl(
                // NB: The coordinates are in this order to ensure that
                // RasterizedLineSegmentRenderable does not care about order
                staticProvider(vertexOf(0.1f, 0.4f)),
                staticProvider(vertexOf(0.9f, 0.6f)),
                staticProvider(8f), null, (short) 1,
                staticProvider(randomColor()),
                2, java.util.UUID.randomUUID(), MockFirstChildComponent);
        RasterizedLineSegmentRenderer = new RasterizedLineSegmentRenderer(TimestampValidator);

        Renderers.put(RasterizedLineSegmentRenderableImpl.class, RasterizedLineSegmentRenderer);
        when(MockFirstChildComponent.contentsRepresentation()).thenReturn(setOf(
                RasterizedLineSegmentRenderable1, RasterizedLineSegmentRenderable2));
        FrameTimer.ShouldExecuteNextFrame = true;

        return setOf(RasterizedLineSegmentRenderer);
    }
}
