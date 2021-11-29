package inaugural.soliloquy.graphics.test.display.renderables.providers;

import inaugural.soliloquy.graphics.api.WindowResolution;
import inaugural.soliloquy.graphics.bootstrap.GraphicsCoreLoopImpl;
import inaugural.soliloquy.graphics.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.graphics.renderables.providers.RectangleAnimatedBackgroundTextureIdProvider;
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
import java.util.HashMap;
import java.util.function.Function;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

/*
  Test acceptance criteria:
  <p>
  1. This test will display a window of 1024x768 pixels with a titlebar reading "New Window"
  2. During the 3000ms, a window taking up half of the screen, centered in the middle, will have
     background tile of a stone floor, moving to the right, repeating every 2000ms. Each tile will
     take up 5% of the screen width.
  3. This animation will run for 3000ms. Then, it will pause for 2000ms. Then, it will continue for
     another 3000ms.
  4. The window will then close
 */
public class RectangleAnimatedBackgroundTextureIdProviderTestWithPausing {
    private final static float[] MESH_DATA =
            new float[] {0f, 1f, 1f, 1f, 1f, 0f, 1f, 0f, 0f, 0f, 0f, 1f};
    private final static FakeGlobalClock GLOBAL_CLOCK = new FakeGlobalClock();

    private final static FakeStaticProvider<Color> TOP_LEFT_COLOR_PROVIDER =
            new FakeStaticProvider<>(null);
    private final static FakeStaticProvider<Color> TOP_RIGHT_COLOR_PROVIDER =
            new FakeStaticProvider<>(null);
    private final static FakeStaticProvider<Color> BOTTOM_RIGHT_COLOR_PROVIDER =
            new FakeStaticProvider<>(null);
    private final static FakeStaticProvider<Color> BOTTOM_LEFT_COLOR_PROVIDER =
            new FakeStaticProvider<>(null);
    private final static float BACKGROUND_TEXTURE_TILE_WIDTH = 0.1f;
    private final static float BACKGROUND_TEXTURE_TILE_HEIGHT = 0.075f;
    private final static FakeStaticProvider<FloatBox> RENDERING_AREA_PROVIDER =
            new FakeStaticProvider<>(new FakeFloatBox(0.25f, 0.25f, 0.75f, 0.75f));
    private final static EntityUuid UUID = new FakeEntityUuid();
    private final static String TILE_LOCATION_1 =
            "./res/images/backgrounds/stone_tile_1.png";
    private final static String TILE_LOCATION_2 =
            "./res/images/backgrounds/stone_tile_2.png";
    private final static String TILE_LOCATION_3 =
            "./res/images/backgrounds/stone_tile_3.png";
    private final static String TILE_LOCATION_4 =
            "./res/images/backgrounds/stone_tile_4.png";
    private final static String TILE_LOCATION_5 =
            "./res/images/backgrounds/stone_tile_5.png";
    private final static String TILE_LOCATION_6 =
            "./res/images/backgrounds/stone_tile_6.png";
    private final static String TILE_LOCATION_7 =
            "./res/images/backgrounds/stone_tile_7.png";
    private final static String TILE_LOCATION_8 =
            "./res/images/backgrounds/stone_tile_8.png";

    private static RectangleAnimatedBackgroundTextureIdProvider
            RectangleAnimatedBackgroundTextureIdProvider;

    public static void main(String[] args) {
        WindowResolutionManagerImpl windowManager = new WindowResolutionManagerImpl(
                WindowDisplayMode.WINDOWED, WindowResolution.RES_1024x768);

        FakeFrameTimer frameTimer = new FakeFrameTimer();
        frameTimer.ShouldExecuteNextFrame = false;

        FakeStackRenderer stackRenderer = new FakeStackRenderer();
        @SuppressWarnings("rawtypes") Collection<Renderer> renderersWithShader = new ArrayList<>();
        Function<float[], Function<float[], Mesh>> meshFactory = f1 -> f2 -> new FakeMesh();
        @SuppressWarnings("rawtypes") Collection<Renderer> renderersWithMesh = new ArrayList<>();

        FakeGraphicsPreloader graphicsPreloader = new FakeGraphicsPreloader();

        RectangleRenderer rectangleRenderer = new RectangleRenderer(null);

        FakeFrameExecutor frameExecutor = new FakeFrameExecutor(stackRenderer, null);

        GraphicsCoreLoop graphicsCoreLoop = new GraphicsCoreLoopImpl("My title bar",
                new FakeGLFWMouseButtonCallback(), frameTimer, 20, windowManager, frameExecutor,
                new FakeShaderFactory(), renderersWithShader, "_", meshFactory, renderersWithMesh,
                MESH_DATA, MESH_DATA, graphicsPreloader, new FakeMouseCursor());

        graphicsPreloader.LoadAction = () -> {
            int msDuration = 2000;
            long currentTimestamp = GLOBAL_CLOCK.globalTimestamp();

            int frame1TextureId =
                    new ImageFactoryImpl(0.5f).make(TILE_LOCATION_1, false).textureId();
            int frame2TextureId =
                    new ImageFactoryImpl(0.5f).make(TILE_LOCATION_2, false).textureId();
            int frame3TextureId =
                    new ImageFactoryImpl(0.5f).make(TILE_LOCATION_3, false).textureId();
            int frame4TextureId =
                    new ImageFactoryImpl(0.5f).make(TILE_LOCATION_4, false).textureId();
            int frame5TextureId =
                    new ImageFactoryImpl(0.5f).make(TILE_LOCATION_5, false).textureId();
            int frame6TextureId =
                    new ImageFactoryImpl(0.5f).make(TILE_LOCATION_6, false).textureId();
            int frame7TextureId =
                    new ImageFactoryImpl(0.5f).make(TILE_LOCATION_7, false).textureId();
            int frame8TextureId =
                    new ImageFactoryImpl(0.5f).make(TILE_LOCATION_8, false).textureId();

            HashMap<Integer, Integer> frames = new HashMap<>();
            frames.put(0, frame1TextureId);
            frames.put(msDuration / 8, frame2TextureId);
            frames.put((msDuration * 2) / 8, frame3TextureId);
            frames.put((msDuration * 3) / 8, frame4TextureId);
            frames.put((msDuration * 4) / 8, frame5TextureId);
            frames.put((msDuration * 5) / 8, frame6TextureId);
            frames.put((msDuration * 6) / 8, frame7TextureId);
            frames.put((msDuration * 7) / 8, frame8TextureId);

            RectangleAnimatedBackgroundTextureIdProvider =
                    new RectangleAnimatedBackgroundTextureIdProvider(new FakeEntityUuid(),
                            msDuration, (int)(currentTimestamp % msDuration), frames, null);

            FakeRectangleRenderable rectangleRenderable =
                    new FakeRectangleRenderable(TOP_LEFT_COLOR_PROVIDER, TOP_RIGHT_COLOR_PROVIDER,
                            BOTTOM_RIGHT_COLOR_PROVIDER, BOTTOM_LEFT_COLOR_PROVIDER,
                            RectangleAnimatedBackgroundTextureIdProvider,
                            BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT, null,
                            null, RENDERING_AREA_PROVIDER, UUID);

            stackRenderer.RenderAction = timestamp ->
                    rectangleRenderer.render(rectangleRenderable, timestamp);
            frameTimer.ShouldExecuteNextFrame = true;
        };

        graphicsCoreLoop.startup(() -> closeAfterSomeTime(graphicsCoreLoop));
    }

    private static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        CheckedExceptionWrapper.sleep(3000);

        RectangleAnimatedBackgroundTextureIdProvider.reportPause(GLOBAL_CLOCK.globalTimestamp());

        CheckedExceptionWrapper.sleep(2000);

        RectangleAnimatedBackgroundTextureIdProvider.reportUnpause(GLOBAL_CLOCK.globalTimestamp());

        CheckedExceptionWrapper.sleep(3000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
