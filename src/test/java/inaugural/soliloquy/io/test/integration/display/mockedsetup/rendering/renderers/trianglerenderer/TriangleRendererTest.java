package inaugural.soliloquy.io.test.integration.display.mockedsetup.rendering.renderers.trianglerenderer;

import inaugural.soliloquy.io.graphics.renderables.TriangleRenderableImpl;
import inaugural.soliloquy.io.graphics.rendering.renderers.TriangleRenderer;
import inaugural.soliloquy.io.test.integration.display.mockedsetup.DisplayTest;
import soliloquy.specs.io.graphics.renderables.TriangleRenderable;
import soliloquy.specs.io.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import java.util.Set;

import static inaugural.soliloquy.tools.collections.Collections.setOf;
import static org.mockito.Mockito.when;

class TriangleRendererTest extends DisplayTest {
    protected static TriangleRenderable TriangleRenderable;
    protected static Renderer<TriangleRenderable> TriangleRenderer;

    /** @noinspection rawtypes */
    public static Set<Renderer> generateRenderersWithMeshAndShader(
            WindowResolutionManager windowResolutionManager,
            TriangleRenderable renderable) {
        TriangleRenderer = new TriangleRenderer(null);

        TriangleRenderable = renderable;

        Renderers.put(TriangleRenderableImpl.class,
                TriangleRenderer);
        when(MockFirstChildComponent.contents()).thenReturn(setOf(TriangleRenderable));
        FrameTimer.ShouldExecuteNextFrame = true;

        return setOf(TriangleRenderer);
    }
}
