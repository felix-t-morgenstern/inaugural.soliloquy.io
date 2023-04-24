package inaugural.soliloquy.graphics.test.display.renderables.providers.rectangleanimatedbackground;

import inaugural.soliloquy.graphics.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.graphics.renderables.RectangleRenderableImpl;
import inaugural.soliloquy.graphics.renderables.providers.RectangleAnimatedBackgroundTextureIdProvider;
import inaugural.soliloquy.graphics.rendering.FloatBoxImpl;
import inaugural.soliloquy.graphics.rendering.renderers.RectangleRenderer;
import inaugural.soliloquy.graphics.test.display.DisplayTest;
import soliloquy.specs.common.valueobjects.Pair;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageDefinition;
import soliloquy.specs.graphics.renderables.RectangleRenderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShiftStackAggregator;
import soliloquy.specs.graphics.renderables.providers.StaticProvider;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.List;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;

public class RectangleAnimatedBackgroundTextureIdProviderTest extends DisplayTest {
    private final static StaticProvider<Color> TOP_LEFT_COLOR_PROVIDER =
            staticNullProvider(Color.BLACK);
    private final static StaticProvider<Color> TOP_RIGHT_COLOR_PROVIDER =
            staticNullProvider(Color.BLACK);
    private final static StaticProvider<Color> BOTTOM_RIGHT_COLOR_PROVIDER =
            staticNullProvider(Color.BLACK);
    private final static StaticProvider<Color> BOTTOM_LEFT_COLOR_PROVIDER =
            staticNullProvider(Color.BLACK);
    private final static float BACKGROUND_TEXTURE_TILE_WIDTH = 0.1f;
    private final static float BACKGROUND_TEXTURE_TILE_HEIGHT = 0.075f;
    private final static StaticProvider<FloatBox> RENDERING_AREA_PROVIDER =
            staticProvider(new FloatBoxImpl(0.25f, 0.25f, 0.75f, 0.75f));
    private final static String TILE_LOCATION_1 =
            "./src/test/resources/images/backgrounds/stone_tile_1.png";
    private final static String TILE_LOCATION_2 =
            "./src/test/resources/images/backgrounds/stone_tile_2.png";
    private final static String TILE_LOCATION_3 =
            "./src/test/resources/images/backgrounds/stone_tile_3.png";
    private final static String TILE_LOCATION_4 =
            "./src/test/resources/images/backgrounds/stone_tile_4.png";
    private final static String TILE_LOCATION_5 =
            "./src/test/resources/images/backgrounds/stone_tile_5.png";
    private final static String TILE_LOCATION_6 =
            "./src/test/resources/images/backgrounds/stone_tile_6.png";
    private final static String TILE_LOCATION_7 =
            "./src/test/resources/images/backgrounds/stone_tile_7.png";
    private final static String TILE_LOCATION_8 =
            "./src/test/resources/images/backgrounds/stone_tile_8.png";

    private static Renderer<RectangleRenderable> RectangleRenderer;
    private static RectangleRenderable RectangleRenderable;

    protected static RectangleAnimatedBackgroundTextureIdProvider
            RectangleAnimatedBackgroundTextureIdProvider;

    /** @noinspection rawtypes, unused */
    protected static List<Renderer> generateRenderablesAndRenderersWithMeshAndShader(
            float borderThickness, Color borderColor,
            ColorShiftStackAggregator colorShiftStackAggregator,
            WindowResolutionManager windowResolutionManager) {
        return listOf(RectangleRenderer = new RectangleRenderer(null));
    }

    protected static void graphicsPreloaderLoadAction() {
        var msDuration = 2000;
        long currentTimestamp = GLOBAL_CLOCK.globalTimestamp();

        var frame1TextureId =
                new ImageFactoryImpl(0.5f)
                        .make(new ImageDefinition(TILE_LOCATION_1, false)).textureId();
        var frame2TextureId =
                new ImageFactoryImpl(0.5f)
                        .make(new ImageDefinition(TILE_LOCATION_2, false)).textureId();
        var frame3TextureId =
                new ImageFactoryImpl(0.5f)
                        .make(new ImageDefinition(TILE_LOCATION_3, false)).textureId();
        var frame4TextureId =
                new ImageFactoryImpl(0.5f)
                        .make(new ImageDefinition(TILE_LOCATION_4, false)).textureId();
        var frame5TextureId =
                new ImageFactoryImpl(0.5f)
                        .make(new ImageDefinition(TILE_LOCATION_5, false)).textureId();
        var frame6TextureId =
                new ImageFactoryImpl(0.5f)
                        .make(new ImageDefinition(TILE_LOCATION_6, false)).textureId();
        var frame7TextureId =
                new ImageFactoryImpl(0.5f)
                        .make(new ImageDefinition(TILE_LOCATION_7, false)).textureId();
        var frame8TextureId =
                new ImageFactoryImpl(0.5f)
                        .make(new ImageDefinition(TILE_LOCATION_8, false)).textureId();

        var frames = mapOf(
                Pair.of(0, frame1TextureId),
                Pair.of(msDuration / 8, frame2TextureId),
                Pair.of((msDuration * 2) / 8, frame3TextureId),
                Pair.of((msDuration * 3) / 8, frame4TextureId),
                Pair.of((msDuration * 4) / 8, frame5TextureId),
                Pair.of((msDuration * 5) / 8, frame6TextureId),
                Pair.of((msDuration * 6) / 8, frame7TextureId),
                Pair.of((msDuration * 7) / 8, frame8TextureId)
        );

        RectangleAnimatedBackgroundTextureIdProvider =
                new RectangleAnimatedBackgroundTextureIdProvider(java.util.UUID.randomUUID(),
                        msDuration, (int) (currentTimestamp % msDuration), frames, null, null);

        RectangleRenderable = new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER, BOTTOM_LEFT_COLOR_PROVIDER,
                RectangleAnimatedBackgroundTextureIdProvider, BACKGROUND_TEXTURE_TILE_WIDTH,
                BACKGROUND_TEXTURE_TILE_HEIGHT, null, null, null, null,
                RENDERING_AREA_PROVIDER, 123, java.util.UUID.randomUUID(), FirstChildStack,
                RENDERING_BOUNDARIES);

        FirstChildStack.add(RectangleRenderable);
        Renderers.registerRenderer(RectangleRenderable.class.getCanonicalName(), RectangleRenderer);

        FrameTimer.ShouldExecuteNextFrame = true;
    }

    protected static void stackRendererAction(long timestamp) {
        RectangleRenderer.render(RectangleRenderable, timestamp);
    }
}
