package inaugural.soliloquy.io.test.display.rendering.renderers.trianglerenderer;

import inaugural.soliloquy.io.graphics.renderables.TriangleRenderableImpl;
import inaugural.soliloquy.io.test.display.DisplayTest;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.providers.StaticProvider;
import soliloquy.specs.io.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.List;

import static inaugural.soliloquy.tools.random.Random.randomInt;
import static org.mockito.Mockito.mock;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

/**
 * Test acceptance criteria:
 * 1. This test will display a window of 800x600 pixels for 2000ms with a titlebar reading "New
 * Window"
 * 2. During the 3000ms, an oblong triangle taking up half of the screen, centered in the middle,
 * will have a top-left color of red, a top-right color of green, and a bottom color of blue.
 * 3. The window will then close
 */
class TriangleRendererColorsTest extends TriangleRendererTest {
    private final static StaticProvider<Vertex> VERTEX_1_PROVIDER =
            staticProvider(vertexOf(0.2f, 0.2f));
    private final static StaticProvider<Color> VERTEX_1_COLOR_PROVIDER =
            staticProvider(Color.RED);
    private final static StaticProvider<Vertex> VERTEX_2_PROVIDER =
            staticProvider(vertexOf(0.8f, 0.4f));
    private final static StaticProvider<Color> VERTEX_2_COLOR_PROVIDER =
            staticProvider(Color.GREEN);
    private final static StaticProvider<Vertex> VERTEX_3_PROVIDER =
            staticProvider(vertexOf(0.5f, 0.8f));
    private final static StaticProvider<Color> VERTEX_3_COLOR_PROVIDER =
            staticProvider(Color.BLUE);
    @SuppressWarnings("unchecked")
    private final static StaticProvider<Integer> BACKGROUND_TEXTURE_ID_PROVIDER =
            mock(StaticProvider.class);
    private final static float BACKGROUND_TEXTURE_TILE_WIDTH = 0.25f;
    private final static float BACKGROUND_TEXTURE_TILE_HEIGHT = 0.5f;

    public static void main(String[] args) {
        runTest(
                TriangleRendererColorsTest::generateRenderablesAndRenderersWithMeshAndShader,
                () -> {},
                DisplayTest::closeAfterSomeTime
        );
    }

    /** @noinspection rawtypes */
    public static List<Renderer> generateRenderablesAndRenderersWithMeshAndShader(
            WindowResolutionManager windowResolutionManager) {
        return generateRenderersWithMeshAndShader(windowResolutionManager,
                new TriangleRenderableImpl(VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                        VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                        VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER, BACKGROUND_TEXTURE_TILE_WIDTH,
                        BACKGROUND_TEXTURE_TILE_HEIGHT, null, null, null, null, randomInt(),
                        java.util.UUID.randomUUID(), FirstChildStack, RENDERING_BOUNDARIES));
    }
}
