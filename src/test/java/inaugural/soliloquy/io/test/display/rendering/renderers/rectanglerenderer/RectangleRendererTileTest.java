package inaugural.soliloquy.io.test.display.rendering.renderers.rectanglerenderer;

import inaugural.soliloquy.io.graphics.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.io.graphics.renderables.RectangleRenderableImpl;
import inaugural.soliloquy.io.graphics.rendering.renderers.RectangleRenderer;
import inaugural.soliloquy.io.test.display.DisplayTest;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.ImageDefinition;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.List;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.setOf;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;

/**
 * Test acceptance criteria:
 * 1. This test will display a window of 800x600 pixels for 2000ms with a titlebar reading "New
 * Window"
 * 2. During the 3000ms, a window taking up half of the screen, centered in the middle, will have
 * background tile of some flowers; this tile will repeat twice horizontally, and three times
 * vertically
 * 3. The window will then close
 */
class RectangleRendererTileTest extends RectangleRendererTest {
    private final static ProviderAtTime<Color> TOP_LEFT_COLOR_PROVIDER = staticProvider(null);
    private final static ProviderAtTime<Color> TOP_RIGHT_COLOR_PROVIDER = staticProvider(null);
    private final static ProviderAtTime<Color> BOTTOM_RIGHT_COLOR_PROVIDER = staticProvider(null);
    private final static ProviderAtTime<Color> BOTTOM_LEFT_COLOR_PROVIDER = staticProvider(null);
    private final static float BACKGROUND_TEXTURE_TILE_WIDTH = 0.25f;
    private final static float BACKGROUND_TEXTURE_TILE_HEIGHT = 0.16667f;
    private final static ProviderAtTime<FloatBox> RENDERING_AREA_PROVIDER =
            staticProvider(floatBoxOf(0.25f, 0.25f, 0.75f, 0.75f));
    private final static String TILE_LOCATION =
            "./src/test/resources/images/tiles/sergey-shmidt-koy6FlCCy5s-unsplash.jpg";

    @SuppressWarnings("unchecked")
    private static final ProviderAtTime<Integer> MOCK_BACKGROUND_TEXTURE_ID_PROVIDER =
            mock(ProviderAtTime.class);

    public static void main(String[] args) {
        runTest(
                RectangleRendererTileTest::generateRenderablesAndRenderersWithMeshAndShader,
                () -> {
                    var image = new ImageFactoryImpl(0.5f)
                            .make(new ImageDefinition(TILE_LOCATION, false));
                    when(MOCK_BACKGROUND_TEXTURE_ID_PROVIDER.provide(anyLong()))
                            .thenReturn(image.textureId());
                    FrameTimer.ShouldExecuteNextFrame = true;
                },
                DisplayTest::closeAfterSomeTime
        );
    }

    /** @noinspection rawtypes */
    public static List<Renderer> generateRenderablesAndRenderersWithMeshAndShader(
            WindowResolutionManager windowResolutionManager) {
        RectangleRenderer = new RectangleRenderer(null);
        RectangleRenderable = new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER, BOTTOM_LEFT_COLOR_PROVIDER,
                MOCK_BACKGROUND_TEXTURE_ID_PROVIDER, staticProvider(BACKGROUND_TEXTURE_TILE_WIDTH),
                staticProvider(BACKGROUND_TEXTURE_TILE_HEIGHT), null, null, null, null,
                RENDERING_AREA_PROVIDER, 123, java.util.UUID.randomUUID(), MockFirstChildComponent,
                DUMMY_REMOVE, RENDERING_BOUNDARIES);

        Renderers.put(RectangleRenderableImpl.class, RectangleRenderer);
        when(MockFirstChildComponent.contents()).thenReturn(setOf(RectangleRenderable));

        return listOf(RectangleRenderer);
    }
}
