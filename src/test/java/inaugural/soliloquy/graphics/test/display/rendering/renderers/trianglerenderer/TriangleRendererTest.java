package inaugural.soliloquy.graphics.test.display.rendering.renderers.trianglerenderer;

import inaugural.soliloquy.graphics.renderables.TriangleRenderableImpl;
import inaugural.soliloquy.graphics.rendering.renderers.TriangleRenderer;
import inaugural.soliloquy.graphics.test.display.DisplayTest;
import soliloquy.specs.graphics.renderables.TriangleRenderable;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.util.List;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.random.Random.randomInt;

class TriangleRendererTest extends DisplayTest {
    protected static TriangleRenderable TriangleRenderable;
    protected static Renderer<TriangleRenderable> TriangleRenderer;

    /** @noinspection rawtypes */
    public static List<Renderer> generateRenderersWithMeshAndShader(
            WindowResolutionManager windowResolutionManager,
            TriangleRenderable renderable) {
        TriangleRenderer = new TriangleRenderer(null);

        TriangleRenderable = renderable;

        Renderers.registerRenderer(TriangleRenderable.getInterfaceName(),
                TriangleRenderer);
        TopLevelStack.add(TriangleRenderable);
        FrameTimer.ShouldExecuteNextFrame = true;

        return listOf(TriangleRenderer);
    }
}
