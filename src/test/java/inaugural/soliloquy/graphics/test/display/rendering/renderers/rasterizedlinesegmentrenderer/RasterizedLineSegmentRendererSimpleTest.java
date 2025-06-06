package inaugural.soliloquy.graphics.test.display.rendering.renderers.rasterizedlinesegmentrenderer;

import inaugural.soliloquy.graphics.renderables.RasterizedLineSegmentRenderableImpl;
import inaugural.soliloquy.graphics.rendering.renderers.RasterizedLineSegmentRenderer;
import inaugural.soliloquy.graphics.test.display.DisplayTest;
import soliloquy.specs.graphics.renderables.RasterizedLineSegmentRenderable;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.awt.*;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
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
    private static RasterizedLineSegmentRenderable RasterizedLineSegmentRenderable;
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
    private static java.util.List<Renderer> generateRenderablesAndRenderersWithMeshAndShader(
            WindowResolutionManager windowResolutionManager) {
        RasterizedLineSegmentRenderable = new RasterizedLineSegmentRenderableImpl(
                // NB: The coordinates are in this order to ensure that
                // RasterizedLineSegmentRenderable does not care about order
                staticProvider(vertexOf(0.75f, 0.5f)),
                staticProvider(vertexOf(0.25f, 0.25f)),
                staticProvider(6f), (short) 0xAAAA, (short) 16,
                staticProvider(new Color(18, 201, 159)),
                1, java.util.UUID.randomUUID(), FirstChildStack);
        RasterizedLineSegmentRenderer = new RasterizedLineSegmentRenderer(null);

        Renderers.registerRenderer(RasterizedLineSegmentRenderableImpl.class,
                RasterizedLineSegmentRenderer);
        FirstChildStack.add(RasterizedLineSegmentRenderable);
        FrameTimer.ShouldExecuteNextFrame = true;

        return listOf(RasterizedLineSegmentRenderer);
    }

    private static void stackRendererAction(long timestamp) {
        RasterizedLineSegmentRenderer.render(RasterizedLineSegmentRenderable, timestamp);
    }
}
