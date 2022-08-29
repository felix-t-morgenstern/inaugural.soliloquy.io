package inaugural.soliloquy.graphics.test.display.rendering.renderers.trianglerenderer;

import inaugural.soliloquy.graphics.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.graphics.renderables.TriangleRenderableImpl;
import inaugural.soliloquy.graphics.rendering.renderers.TriangleRenderer;
import inaugural.soliloquy.graphics.test.display.DisplayTest;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeStaticProvider;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageDefinition;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static inaugural.soliloquy.tools.random.Random.randomInt;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 800x600 pixels for 2000ms with a titlebar reading "New
 * Window"
 * 2. During the 3000ms, an oblong triangle taking up half of the screen, centered in the middle,
 * will have background tile of some flowers; this tile will repeat four times horizontally, and
 * three times vertically.
 * 3. The window will then close
 */
class TriangleRendererTileTest extends TriangleRendererTest {
    private final static FakeStaticProvider<Vertex> VERTEX_1_PROVIDER =
            new FakeStaticProvider<>(Vertex.of(0.2f, 0.2f));
    private final static FakeStaticProvider<Color> VERTEX_1_COLOR_PROVIDER =
            new FakeStaticProvider<>(null);
    private final static FakeStaticProvider<Vertex> VERTEX_2_PROVIDER =
            new FakeStaticProvider<>(Vertex.of(0.8f, 0.4f));
    private final static FakeStaticProvider<Color> VERTEX_2_COLOR_PROVIDER =
            new FakeStaticProvider<>(null);
    private final static FakeStaticProvider<Vertex> VERTEX_3_PROVIDER =
            new FakeStaticProvider<>(Vertex.of(0.5f, 0.8f));
    private final static FakeStaticProvider<Color> VERTEX_3_COLOR_PROVIDER =
            new FakeStaticProvider<>(null);
    private final static FakeStaticProvider<Integer> BACKGROUND_TEXTURE_ID_PROVIDER =
            new FakeStaticProvider<>(null);
    private final static float BACKGROUND_TEXTURE_TILE_WIDTH = 0.15f;
    private final static float BACKGROUND_TEXTURE_TILE_HEIGHT = 0.2f;
    private final static String TILE_LOCATION =
            "./res/images/tiles/sergey-shmidt-koy6FlCCy5s-unsplash.jpg";

    public static void main(String[] args) {
        runTest(
                TriangleRendererTileTest::generateRenderablesAndRenderersWithMeshAndShader,
                timestamp -> TriangleRenderer.render(TriangleRenderable, timestamp),
                () -> {
                    BACKGROUND_TEXTURE_ID_PROVIDER.ProvidedValue =
                            new ImageFactoryImpl(0.5f)
                                    .make(new ImageDefinition(TILE_LOCATION, false)).textureId();
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
                        java.util.UUID.randomUUID(), r -> {}, r -> {});

        FrameTimer.ShouldExecuteNextFrame = true;

        return new ArrayList<Renderer>() {{
            add(TriangleRenderer);
        }};
    }
}
