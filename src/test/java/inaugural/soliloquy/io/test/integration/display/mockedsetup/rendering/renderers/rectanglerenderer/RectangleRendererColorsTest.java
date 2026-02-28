package inaugural.soliloquy.io.test.integration.display.mockedsetup.rendering.renderers.rectanglerenderer;

import inaugural.soliloquy.io.graphics.renderables.RectangleRenderableImpl;
import inaugural.soliloquy.io.graphics.rendering.renderers.BasicTriangleRenderer;
import inaugural.soliloquy.io.graphics.rendering.renderers.RectangleRenderer;
import inaugural.soliloquy.io.graphics.rendering.renderers.TriangleSegmentRenderer;
import inaugural.soliloquy.io.test.integration.display.mockedsetup.DisplayTest;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.Set;

import static inaugural.soliloquy.tools.collections.Collections.setOf;
import static inaugural.soliloquy.tools.testing.Mock.generateMockStaticProvider;
import static org.mockito.Mockito.when;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 800x600 pixels for 2000ms with a titlebar reading "New
 * Window"
 * 2. During the 3000ms, a window taking up half of the screen, centered in the middle, will have a
 * top-left color of red, a top-right color of green, a bottom-right color of blue, and a
 * bottom-left color of white
 * 3. The window will then close
 */
class RectangleRendererColorsTest extends RectangleRendererTest {
    private final static ProviderAtTime<Color> TOP_LEFT_COLOR_PROVIDER =
            generateMockStaticProvider(Color.RED);
    private final static ProviderAtTime<Color> TOP_RIGHT_COLOR_PROVIDER =
            generateMockStaticProvider(Color.GREEN);
    private final static ProviderAtTime<Color> BOTTOM_RIGHT_COLOR_PROVIDER =
            generateMockStaticProvider(Color.BLUE);
    private final static ProviderAtTime<Color> BOTTOM_LEFT_COLOR_PROVIDER =
            generateMockStaticProvider(Color.WHITE);
    private final static ProviderAtTime<Integer> BACKGROUND_TEXTURE_ID_PROVIDER =
            generateMockStaticProvider(null);
    private final static float BACKGROUND_TEXTURE_TILE_WIDTH = 0.25f;
    private final static float BACKGROUND_TEXTURE_TILE_HEIGHT = 0.5f;
    private final static ProviderAtTime<FloatBox> RENDERING_AREA_PROVIDER =
            generateMockStaticProvider(floatBoxOf(0.25f, 0.25f, 0.75f, 0.75f));

    public static void main(String[] args) {
        runTest(
                RectangleRendererColorsTest::generateRenderablesAndRenderersWithMeshAndShader,
                () -> {},
                DisplayTest::closeAfterSomeTime
        );
    }

    @SuppressWarnings("rawtypes")
    public static Set<Renderer> generateRenderablesAndRenderersWithMeshAndShader(
            WindowResolutionManager windowResolutionManager) {
        RectangleRenderer = new RectangleRenderer(TimestampValidator,
                new TriangleSegmentRenderer(RENDERING_BOUNDARIES, new BasicTriangleRenderer()));
        RectangleRenderable = new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER, BOTTOM_LEFT_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER, staticProvider(BACKGROUND_TEXTURE_TILE_WIDTH),
                staticProvider(BACKGROUND_TEXTURE_TILE_HEIGHT), null, null, null, null,
                RENDERING_AREA_PROVIDER, 123, java.util.UUID.randomUUID(), MockFirstChildComponent,
                RENDERING_BOUNDARIES, TimestampValidator);

        Renderers.put(RectangleRenderableImpl.class, RectangleRenderer);
        when(MockFirstChildComponent.contentsRepresentation()).thenReturn(
                setOf(RectangleRenderable));
        FrameTimer.ShouldExecuteNextFrame = true;

        return setOf(RectangleRenderer);
    }
}
