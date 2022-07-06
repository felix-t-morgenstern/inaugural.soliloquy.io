package inaugural.soliloquy.graphics.test.display.rendering.renderers.rasterizedlinesegmentrenderer;

import inaugural.soliloquy.graphics.renderables.providers.StaticProviderImpl;
import inaugural.soliloquy.graphics.rendering.renderers.RasterizedLineSegmentRenderer;
import inaugural.soliloquy.graphics.test.display.DisplayTest;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeFloatBox;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeRasterizedLineSegmentRenderable;
import soliloquy.specs.graphics.renderables.RasterizedLineSegmentRenderable;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.ArrayList;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 800x600 pixels for 2000ms with a titlebar reading "New
 *    Window"
 * 2. During the 3000ms, a teal rasterized line segment will be rendered, starting from
 *    (0.25,0.25), and ending at (0.75, 0.5).
 * 3. The window will then close
 *
 */
class RasterizedLineSegmentRendererSimpleTest extends DisplayTest {
    private static RasterizedLineSegmentRenderable RasterizedLineSegmentRenderable;
    private static Renderer<RasterizedLineSegmentRenderable> RasterizedLineSegmentRenderer;

    public static void main(String[] args) {
        runTest(
                RasterizedLineSegmentRendererSimpleTest::
                        generateRenderablesAndRenderersWithMeshAndShader,
                RasterizedLineSegmentRendererSimpleTest::stackRendererAction,
                () -> {},
                DisplayTest::closeAfterSomeTime
        );
    }

    /** @noinspection rawtypes*/
    private static java.util.List<Renderer> generateRenderablesAndRenderersWithMeshAndShader(
            WindowResolutionManager windowResolutionManager) {
        RasterizedLineSegmentRenderable = new FakeRasterizedLineSegmentRenderable(
                new StaticProviderImpl<>(java.util.UUID.randomUUID(), 6f, null), (short) 0xAAAA,
                (short) 16,
                new StaticProviderImpl<>(java.util.UUID.randomUUID(), new Color(18, 201, 159), null),
                // NB: The parameters are in this order to ensure that
                //     RasterizedLineSegmentRenderable does not care about order
                new StaticProviderImpl<>(java.util.UUID.randomUUID(),
                        new FakeFloatBox(0.75f, 0.5f, 0.25f, 0.25f), null),
                1, java.util.UUID.randomUUID());
        RasterizedLineSegmentRenderer = new RasterizedLineSegmentRenderer(null);
        FrameTimer.ShouldExecuteNextFrame = true;

        return new ArrayList<Renderer>() {{
            add(RasterizedLineSegmentRenderer);
        }};
    }

    private static void stackRendererAction(long timestamp) {
        RasterizedLineSegmentRenderer.render(RasterizedLineSegmentRenderable, timestamp);
    }
}
