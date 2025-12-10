package inaugural.soliloquy.io.test.integration.display.mockedsetup.rendering.renderers.trianglerenderer;

import inaugural.soliloquy.io.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.io.graphics.renderables.TriangleRenderableImpl;
import inaugural.soliloquy.io.graphics.rendering.renderers.TriangleRenderer;
import inaugural.soliloquy.io.test.integration.display.mockedsetup.DisplayTest;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.bootstrap.assetfactories.definitions.ImageDefinition;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.Set;

import static inaugural.soliloquy.tools.collections.Collections.setOf;
import static inaugural.soliloquy.tools.random.Random.randomInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

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
    private final static ProviderAtTime<Vertex> VERTEX_1_PROVIDER =
            staticProvider(vertexOf(0.2f, 0.2f));
    @SuppressWarnings("unchecked")
    private final static ProviderAtTime<Color> VERTEX_1_COLOR_PROVIDER = mock(ProviderAtTime.class);
    private final static ProviderAtTime<Vertex> VERTEX_2_PROVIDER =
            staticProvider(vertexOf(0.8f, 0.4f));
    @SuppressWarnings("unchecked")
    private final static ProviderAtTime<Color> VERTEX_2_COLOR_PROVIDER = mock(ProviderAtTime.class);
    private final static ProviderAtTime<Vertex> VERTEX_3_PROVIDER =
            staticProvider(vertexOf(0.5f, 0.8f));
    @SuppressWarnings("unchecked")
    private final static ProviderAtTime<Color> VERTEX_3_COLOR_PROVIDER = mock(ProviderAtTime.class);
    @SuppressWarnings("unchecked")
    private final static ProviderAtTime<Integer> BACKGROUND_TEXTURE_ID_PROVIDER =
            mock(ProviderAtTime.class);
    private final static float BACKGROUND_TEXTURE_TILE_WIDTH = 0.15f;
    private final static float BACKGROUND_TEXTURE_TILE_HEIGHT = 0.2f;
    private final static String TILE_LOCATION =
            "./src/test/resources/images/tiles/sergey-shmidt-koy6FlCCy5s-unsplash.jpg";

    public static void main(String[] args) {
        runTest(
                TriangleRendererTileTest::generateRenderablesAndRenderersWithMeshAndShader,
                () -> {
                    when(BACKGROUND_TEXTURE_ID_PROVIDER.provide(anyLong()))
                            .thenReturn(new ImageFactoryImpl(0.5f)
                                    .make(new ImageDefinition(TILE_LOCATION, false)).textureId());
                    FrameTimer.ShouldExecuteNextFrame = true;
                },
                DisplayTest::closeAfterSomeTime
        );
    }

    /** @noinspection rawtypes */
    public static Set<Renderer> generateRenderablesAndRenderersWithMeshAndShader(
            WindowResolutionManager windowResolutionManager) {
        TriangleRenderer = new TriangleRenderer(TimestampValidator, RENDERING_BOUNDARIES);

        TriangleRenderable =
                new TriangleRenderableImpl(
                        VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                        VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                        VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER,
                        staticProvider(BACKGROUND_TEXTURE_TILE_WIDTH),
                        staticProvider(BACKGROUND_TEXTURE_TILE_HEIGHT), null, null, null, null,
                        randomInt(),
                        java.util.UUID.randomUUID(), MockFirstChildComponent,
                        RENDERING_BOUNDARIES, TimestampValidator);

        Renderers.put(TriangleRenderableImpl.class, TriangleRenderer);
        when(MockFirstChildComponent.contentsRepresentation()).thenReturn(setOf(TriangleRenderable));
        FrameTimer.ShouldExecuteNextFrame = true;

        return setOf(TriangleRenderer);
    }
}
