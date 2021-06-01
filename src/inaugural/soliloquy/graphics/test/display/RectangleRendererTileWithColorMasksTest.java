package inaugural.soliloquy.graphics.test.display;

import inaugural.soliloquy.common.test.fakes.FakeCoordinateFactory;
import inaugural.soliloquy.graphics.api.WindowResolution;
import inaugural.soliloquy.graphics.bootstrap.GraphicsCoreLoopImpl;
import inaugural.soliloquy.graphics.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.graphics.rendering.WindowResolutionManagerImpl;
import inaugural.soliloquy.graphics.rendering.renderers.RectangleRenderer;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.Mesh;
import soliloquy.specs.graphics.rendering.WindowDisplayMode;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 800x600 pixels for 2000ms with a titlebar reading "New
 *    Window"
 * 2. During the 3000ms, a window taking up half of the screen, centered in the middle, will have
 *    background tile of some flowers; this tile will repeat twice horizontally, and three times
 *    vertically. There will also be color "masks" on each corner, permitting only certain amounts
 *    of each channel through. The top-left corner will only permit the red channel; the top-right
 *    will only permit the green; the bottom-right will only permit the blue; and the bottom-left
 *    will permit all channels.
 * 3. The window will then close
 *
 */
class RectangleRendererTileWithColorMasksTest {
    private final static FakeCoordinateFactory COORDINATE_FACTORY = new FakeCoordinateFactory();
    private final static float[] MESH_DATA =
            new float[] {0f, 1f, 1f, 1f, 1f, 0f, 1f, 0f, 0f, 0f, 0f, 1f};

    private final static FakeStaticProviderAtTime<Color> TOP_LEFT_COLOR_PROVIDER =
            new FakeStaticProviderAtTime<>(Color.RED);
    private final static FakeStaticProviderAtTime<Color> TOP_RIGHT_COLOR_PROVIDER =
            new FakeStaticProviderAtTime<>(Color.GREEN);
    private final static FakeStaticProviderAtTime<Color> BOTTOM_RIGHT_COLOR_PROVIDER =
            new FakeStaticProviderAtTime<>(Color.BLUE);
    private final static FakeStaticProviderAtTime<Color> BOTTOM_LEFT_COLOR_PROVIDER =
            new FakeStaticProviderAtTime<>(Color.WHITE);
    private final static FakeStaticProviderAtTime<Integer> BACKGROUND_TEXTURE_ID_PROVIDER =
            new FakeStaticProviderAtTime<>(null);
    private final static float BACKGROUND_TEXTURE_TILE_WIDTH = 0.25f;
    private final static float BACKGROUND_TEXTURE_TILE_HEIGHT = 0.16667f;
    private final static FakeStaticProviderAtTime<FloatBox> RENDERING_AREA_PROVIDER =
            new FakeStaticProviderAtTime<>(new FakeFloatBox(0.25f, 0.25f, 0.75f, 0.75f));
    private final static EntityUuid UUID = new FakeEntityUuid();
    private final static String TILE_LOCATION =
            "./res/images/tiles/sergey-shmidt-koy6FlCCy5s-unsplash.jpg";

    public static void main(String[] args) {
        WindowResolutionManagerImpl windowManager = new WindowResolutionManagerImpl(
                WindowDisplayMode.WINDOWED, WindowResolution.RES_800x600, COORDINATE_FACTORY);

        FakeFrameTimer frameTimer = new FakeFrameTimer();
        frameTimer.ShouldExecuteNextFrame = false;

        FakeStackRenderer stackRenderer = new FakeStackRenderer();
        @SuppressWarnings("rawtypes") Collection<Renderer> renderersWithShader = new ArrayList<>();
        Function<float[], Function<float[], Mesh>> meshFactory = f1 -> f2 -> new FakeMesh();
        @SuppressWarnings("rawtypes") Collection<Renderer> renderersWithMesh = new ArrayList<>();

        FakeGraphicsPreloader graphicsPreloader = new FakeGraphicsPreloader();

        RectangleRenderer rectangleRenderer = new RectangleRenderer();

        FakeRectangleRenderable rectangleRenderable =
                new FakeRectangleRenderable(TOP_LEFT_COLOR_PROVIDER, TOP_RIGHT_COLOR_PROVIDER,
                        BOTTOM_RIGHT_COLOR_PROVIDER, BOTTOM_LEFT_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER, BACKGROUND_TEXTURE_TILE_WIDTH,
                        BACKGROUND_TEXTURE_TILE_HEIGHT, null, null, RENDERING_AREA_PROVIDER, UUID);

        stackRenderer.RenderAction = timestamp ->
                rectangleRenderer.render(rectangleRenderable, timestamp);

        FakeFrameExecutor frameExecutor = new FakeFrameExecutor(stackRenderer, null);

        GraphicsCoreLoop graphicsCoreLoop = new GraphicsCoreLoopImpl("My title bar",
                new FakeGLFWMouseButtonCallback(), frameTimer, 20, windowManager, frameExecutor,
                new FakeShaderFactory(), renderersWithShader, "_", meshFactory, renderersWithMesh,
                MESH_DATA, MESH_DATA, graphicsPreloader);

        graphicsPreloader.LoadAction = () -> {
            BACKGROUND_TEXTURE_ID_PROVIDER.ProvidedValue =
                    new ImageFactoryImpl(0.5f).make(TILE_LOCATION, false).textureId();
            frameTimer.ShouldExecuteNextFrame = true;
        };

        graphicsCoreLoop.startup(() -> closeAfterSomeTime(graphicsCoreLoop));
    }

    private static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        CheckedExceptionWrapper.sleep(3000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
