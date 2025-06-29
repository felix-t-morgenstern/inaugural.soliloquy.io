package inaugural.soliloquy.io.test.display.rendering.renderers.trianglerenderer;

import inaugural.soliloquy.io.graphics.renderables.TriangleRenderableImpl;
import inaugural.soliloquy.io.graphics.rendering.renderers.TriangleRenderer;
import inaugural.soliloquy.io.test.display.DisplayTest;
import soliloquy.specs.io.graphics.renderables.TriangleRenderable;
import soliloquy.specs.io.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import java.util.List;

import static inaugural.soliloquy.tools.collections.Collections.listOf;

class TriangleRendererTest extends DisplayTest {
    protected static TriangleRenderable TriangleRenderable;
    protected static Renderer<TriangleRenderable> TriangleRenderer;

    /** @noinspection rawtypes */
    public static List<Renderer> generateRenderersWithMeshAndShader(
            WindowResolutionManager windowResolutionManager,
            TriangleRenderable renderable) {
        TriangleRenderer = new TriangleRenderer(null);

        TriangleRenderable = renderable;

        Renderers.registerRenderer(TriangleRenderableImpl.class,
                TriangleRenderer);
        FirstChildStack.add(TriangleRenderable);
        FrameTimer.ShouldExecuteNextFrame = true;

        return listOf(TriangleRenderer);
    }
}
