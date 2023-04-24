package inaugural.soliloquy.graphics.test.display.rendering.renderers.trianglerenderer;

import inaugural.soliloquy.graphics.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.graphics.renderables.TriangleRenderableImpl;
import inaugural.soliloquy.graphics.rendering.renderers.TriangleRenderer;
import inaugural.soliloquy.graphics.test.display.DisplayTest;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageDefinition;
import soliloquy.specs.graphics.renderables.providers.StaticProvider;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.List;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.random.Random.randomInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test acceptance criteria:
 * 1. This test will display a window of 800x600 pixels for 2000ms with a titlebar reading "New
 * Window"
 * 2. During the 3000ms, an oblong triangle taking up half of the screen, centered in the middle,
 * will have background tile of some flowers; this tile will repeat four times horizontally, and
 * three times vertically.
 * 3. The window will then close
 */
class TriangleRendererTileTest extends TriangleRendererTest {
    private final static StaticProvider<Vertex> VERTEX_1_PROVIDER =
            staticProvider(Vertex.of(0.2f, 0.2f));
    @SuppressWarnings("unchecked")
    private final static StaticProvider<Color> VERTEX_1_COLOR_PROVIDER = mock(StaticProvider.class);
    private final static StaticProvider<Vertex> VERTEX_2_PROVIDER =
            staticProvider(Vertex.of(0.8f, 0.4f));
    @SuppressWarnings("unchecked")
    private final static StaticProvider<Color> VERTEX_2_COLOR_PROVIDER = mock(StaticProvider.class);
    private final static StaticProvider<Vertex> VERTEX_3_PROVIDER =
            staticProvider(Vertex.of(0.5f, 0.8f));
    @SuppressWarnings("unchecked")
    private final static StaticProvider<Color> VERTEX_3_COLOR_PROVIDER = mock(StaticProvider.class);
    @SuppressWarnings("unchecked")
    private final static StaticProvider<Integer> BACKGROUND_TEXTURE_ID_PROVIDER = mock(StaticProvider.class);
    private final static float BACKGROUND_TEXTURE_TILE_WIDTH = 0.15f;
    private final static float BACKGROUND_TEXTURE_TILE_HEIGHT = 0.2f;
    private final static String TILE_LOCATION =
            "./src/test/resources/images/tiles/sergey-shmidt-koy6FlCCy5s-unsplash.jpg";

    public static void main(String[] args) {
        runTest(
                TriangleRendererTileTest::generateRenderablesAndRenderersWithMeshAndShader,
                () -> {
                    when(VERTEX_1_COLOR_PROVIDER.getArchetype()).thenReturn(Color.BLACK);
                    when(VERTEX_2_COLOR_PROVIDER.getArchetype()).thenReturn(Color.BLACK);
                    when(VERTEX_3_COLOR_PROVIDER.getArchetype()).thenReturn(Color.BLACK);
                    when(BACKGROUND_TEXTURE_ID_PROVIDER.provide(anyLong()))
                            .thenReturn(new ImageFactoryImpl(0.5f)
                                    .make(new ImageDefinition(TILE_LOCATION, false)).textureId());
                    when(BACKGROUND_TEXTURE_ID_PROVIDER.getArchetype()).thenReturn(0);
                    FrameTimer.ShouldExecuteNextFrame = true;
                },
                DisplayTest::closeAfterSomeTime
        );
    }

    /** @noinspection rawtypes */
    public static List<Renderer> generateRenderablesAndRenderersWithMeshAndShader(
            WindowResolutionManager windowResolutionManager) {
        TriangleRenderer = new TriangleRenderer(null);

        TriangleRenderable =
                new TriangleRenderableImpl(VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                        VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                        VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER, BACKGROUND_TEXTURE_TILE_WIDTH,
                        BACKGROUND_TEXTURE_TILE_HEIGHT, null, null, null, null, randomInt(),
                        java.util.UUID.randomUUID(), FirstChildStack, RENDERING_BOUNDARIES);

        Renderers.registerRenderer(TriangleRenderable.getInterfaceName(),
                TriangleRenderer);
        FirstChildStack.add(TriangleRenderable);
        FrameTimer.ShouldExecuteNextFrame = true;

        return listOf(TriangleRenderer);
    }
}
