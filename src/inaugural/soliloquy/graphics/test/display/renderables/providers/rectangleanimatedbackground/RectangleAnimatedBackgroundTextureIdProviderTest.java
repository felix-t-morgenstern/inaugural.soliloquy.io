package inaugural.soliloquy.graphics.test.display.renderables.providers.rectangleanimatedbackground;

import inaugural.soliloquy.graphics.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.graphics.renderables.RectangleRenderableImpl;
import inaugural.soliloquy.graphics.renderables.providers.RectangleAnimatedBackgroundTextureIdProvider;
import inaugural.soliloquy.graphics.rendering.renderers.RectangleRenderer;
import inaugural.soliloquy.graphics.test.display.DisplayTest;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeFloatBox;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeStaticProvider;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageDefinition;
import soliloquy.specs.graphics.renderables.RectangleRenderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShiftStackAggregator;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RectangleAnimatedBackgroundTextureIdProviderTest extends DisplayTest {
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

    private static Renderer<RectangleRenderable> RectangleRenderer;
    private static RectangleRenderable RectangleRenderable;

    protected static RectangleAnimatedBackgroundTextureIdProvider
            RectangleAnimatedBackgroundTextureIdProvider;

    /** @noinspection rawtypes, unused */
    protected static List<Renderer> generateRenderablesAndRenderersWithMeshAndShader(
            float borderThickness, Color borderColor,
            ColorShiftStackAggregator colorShiftStackAggregator,
            WindowResolutionManager windowResolutionManager)
    {
        return new ArrayList<Renderer>() {{
            add(RectangleRenderer = new RectangleRenderer(null));
        }};
    }

    protected static void graphicsPreloaderLoadAction() {
        int msDuration = 2000;
        long currentTimestamp = GLOBAL_CLOCK.globalTimestamp();

        int frame1TextureId =
                new ImageFactoryImpl(0.5f)
                        .make(new ImageDefinition(TILE_LOCATION_1, false)).textureId();
        int frame2TextureId =
                new ImageFactoryImpl(0.5f)
                        .make(new ImageDefinition(TILE_LOCATION_2, false)).textureId();
        int frame3TextureId =
                new ImageFactoryImpl(0.5f)
                        .make(new ImageDefinition(TILE_LOCATION_3, false)).textureId();
        int frame4TextureId =
                new ImageFactoryImpl(0.5f)
                        .make(new ImageDefinition(TILE_LOCATION_4, false)).textureId();
        int frame5TextureId =
                new ImageFactoryImpl(0.5f)
                        .make(new ImageDefinition(TILE_LOCATION_5, false)).textureId();
        int frame6TextureId =
                new ImageFactoryImpl(0.5f)
                        .make(new ImageDefinition(TILE_LOCATION_6, false)).textureId();
        int frame7TextureId =
                new ImageFactoryImpl(0.5f)
                        .make(new ImageDefinition(TILE_LOCATION_7, false)).textureId();
        int frame8TextureId =
                new ImageFactoryImpl(0.5f)
                        .make(new ImageDefinition(TILE_LOCATION_8, false)).textureId();

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
                new RectangleAnimatedBackgroundTextureIdProvider(java.util.UUID.randomUUID(),
                        msDuration, (int)(currentTimestamp % msDuration), frames, null);

        RectangleRenderable = new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER, BOTTOM_LEFT_COLOR_PROVIDER,
                RectangleAnimatedBackgroundTextureIdProvider, BACKGROUND_TEXTURE_TILE_WIDTH,
                BACKGROUND_TEXTURE_TILE_HEIGHT, null, null, null, null,
                RENDERING_AREA_PROVIDER, 123, java.util.UUID.randomUUID(), renderable -> {},
                renderable -> {});

        FrameTimer.ShouldExecuteNextFrame = true;
    }

    protected static void stackRendererAction(long timestamp) {
        RectangleRenderer.render(RectangleRenderable, timestamp);
    }
}
