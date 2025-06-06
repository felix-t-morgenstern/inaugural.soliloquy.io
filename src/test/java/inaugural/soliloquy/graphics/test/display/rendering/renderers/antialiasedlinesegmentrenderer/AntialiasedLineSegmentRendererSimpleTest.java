package inaugural.soliloquy.graphics.test.display.rendering.renderers.antialiasedlinesegmentrenderer;

import inaugural.soliloquy.graphics.renderables.AntialiasedLineSegmentRenderableImpl;
import inaugural.soliloquy.graphics.rendering.renderers.AntialiasedLineSegmentRenderer;
import inaugural.soliloquy.graphics.test.display.DisplayTest;
import soliloquy.specs.graphics.renderables.AntialiasedLineSegmentRenderable;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.List;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.random.Random.randomInt;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 800x600 pixels for 2000ms with a titlebar reading "New
 * Window"
 * 2. During the 3000ms, a very thin, red line segment will stretch from [0.25,0.25] to [0.75,0.75],
 * while a very thick, purple segment will stretch from [0.5,0.25] to [0.5,0.75]. A gradient of
 * transparent to opaque will be clearly visible on the edges of the thicker, purple line.
 * 3. The window will then close
 */
class AntialiasedLineSegmentRendererSimpleTest extends DisplayTest {
    private static AntialiasedLineSegmentRenderable AntialiasedLineSegmentRenderable1;
    private static AntialiasedLineSegmentRenderable AntialiasedLineSegmentRenderable2;
    private static Renderer<AntialiasedLineSegmentRenderable> AntialiasedLineSegmentRenderer;

    public static void main(String[] args) {
        runTest(
                AntialiasedLineSegmentRendererSimpleTest::
                        generateRenderablesAndRenderersWithMeshAndShader,
                () -> {},
                AntialiasedLineSegmentRendererSimpleTest::closeAfterSomeTime
        );
    }

    /** @noinspection rawtypes */
    private static List<Renderer> generateRenderablesAndRenderersWithMeshAndShader(
            WindowResolutionManager windowResolutionManager) {
        AntialiasedLineSegmentRenderer =
                new AntialiasedLineSegmentRenderer(windowResolutionManager, null);

        AntialiasedLineSegmentRenderable1 = new AntialiasedLineSegmentRenderableImpl(
                staticProvider(vertexOf(0.75f, 0.75f)),
                staticProvider(vertexOf(0.25f, 0.25f)),
                staticProvider(0.000625f),
                staticProvider(Color.RED),
                staticProvider(0.1f),
                staticProvider(0.01f),
                randomInt(),
                java.util.UUID.randomUUID(),
                FirstChildStack
        );

        AntialiasedLineSegmentRenderable2 = new AntialiasedLineSegmentRenderableImpl(
                staticProvider(vertexOf(0.5f, 0.75f)),
                staticProvider(vertexOf(0.5f, 0.25f)),
                staticProvider(0.1f),
                staticProvider(new Color(40, 0, 255)),
                staticProvider(0.05f),
                staticProvider(0.05f),
                randomInt(),
                java.util.UUID.randomUUID(),
                FirstChildStack
        );

        FirstChildStack.add(AntialiasedLineSegmentRenderable1);
        FirstChildStack.add(AntialiasedLineSegmentRenderable2);
        Renderers.registerRenderer(AntialiasedLineSegmentRenderableImpl.class,
                AntialiasedLineSegmentRenderer);

        FrameTimer.ShouldExecuteNextFrame = true;

        return listOf(AntialiasedLineSegmentRenderer);
    }

    private static void stackRendererAction(long timestamp) {
        AntialiasedLineSegmentRenderer.render(AntialiasedLineSegmentRenderable2, timestamp);
        AntialiasedLineSegmentRenderer.render(AntialiasedLineSegmentRenderable1, timestamp);
    }
}
