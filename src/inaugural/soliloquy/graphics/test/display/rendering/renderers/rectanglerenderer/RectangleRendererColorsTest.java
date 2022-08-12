package inaugural.soliloquy.graphics.test.display.rendering.renderers.rectanglerenderer;

import inaugural.soliloquy.graphics.renderables.RectangleRenderableImpl;
import inaugural.soliloquy.graphics.rendering.renderers.RectangleRenderer;
import inaugural.soliloquy.graphics.test.display.DisplayTest;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 800x600 pixels for 2000ms with a titlebar reading "New
 *    Window"
 * 2. During the 3000ms, a window taking up half of the screen, centered in the middle, will have a
 *    top-left color of red, a top-right color of green, a bottom-right color of blue, and a
 *    bottom-left color of white
 * 3. The window will then close
 *
 */
class RectangleRendererColorsTest extends RectangleRendererTest {
    private final static FakeStaticProvider<Color> TOP_LEFT_COLOR_PROVIDER =
            new FakeStaticProvider<>(Color.RED);
    private final static FakeStaticProvider<Color> TOP_RIGHT_COLOR_PROVIDER =
            new FakeStaticProvider<>(Color.GREEN);
    private final static FakeStaticProvider<Color> BOTTOM_RIGHT_COLOR_PROVIDER =
            new FakeStaticProvider<>(Color.BLUE);
    private final static FakeStaticProvider<Color> BOTTOM_LEFT_COLOR_PROVIDER =
            new FakeStaticProvider<>(Color.WHITE);
    private final static FakeStaticProvider<Integer> BACKGROUND_TEXTURE_ID_PROVIDER =
            new FakeStaticProvider<>(null);
    private final static float BACKGROUND_TEXTURE_TILE_WIDTH = 0.25f;
    private final static float BACKGROUND_TEXTURE_TILE_HEIGHT = 0.5f;
    private final static FakeStaticProvider<FloatBox> RENDERING_AREA_PROVIDER =
            new FakeStaticProvider<>(new FakeFloatBox(0.25f, 0.25f, 0.75f, 0.75f));

    public static void main(String[] args) {
        runTest(
                RectangleRendererColorsTest::generateRenderablesAndRenderersWithMeshAndShader,
                timestamp -> RectangleRenderer.render(RectangleRenderable, timestamp),
                () -> {},
                DisplayTest::closeAfterSomeTime
        );
    }

    /** @noinspection rawtypes*/
    public static List<Renderer> generateRenderablesAndRenderersWithMeshAndShader(
            WindowResolutionManager windowResolutionManager) {
        RectangleRenderer = new RectangleRenderer(null);
        RectangleRenderable = new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER, BOTTOM_LEFT_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER, BACKGROUND_TEXTURE_TILE_WIDTH,
                BACKGROUND_TEXTURE_TILE_HEIGHT, null, null, null, null,
                RENDERING_AREA_PROVIDER, 123, java.util.UUID.randomUUID(), renderable -> {},
                renderable -> {});
        FrameTimer.ShouldExecuteNextFrame = true;

        return new ArrayList<Renderer>() {{
            add(RectangleRenderer);
        }};
    }
}
