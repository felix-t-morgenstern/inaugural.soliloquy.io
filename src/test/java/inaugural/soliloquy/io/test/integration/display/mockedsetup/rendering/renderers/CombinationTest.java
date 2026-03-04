package inaugural.soliloquy.io.test.integration.display.mockedsetup.rendering.renderers;

import inaugural.soliloquy.io.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.io.graphics.renderables.RectangleRenderableImpl;
import inaugural.soliloquy.io.graphics.renderables.SpriteRenderableImpl;
import inaugural.soliloquy.io.graphics.rendering.renderers.BasicTriangleRenderer;
import inaugural.soliloquy.io.graphics.rendering.renderers.RectangleRenderer;
import inaugural.soliloquy.io.graphics.rendering.renderers.SpriteRenderer;
import inaugural.soliloquy.io.graphics.rendering.renderers.TriangleSegmentRenderer;
import inaugural.soliloquy.io.test.integration.display.mockedsetup.DisplayTest;
import inaugural.soliloquy.io.test.integration.display.mockedsetup.rendering.renderers.spriterenderer.SpriteRendererTest;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeSprite;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.bootstrap.assetfactories.definitions.ImageDefinition;
import soliloquy.specs.io.graphics.renderables.RectangleRenderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShiftStackAggregator;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.Set;
import java.util.function.Supplier;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.setOf;
import static inaugural.soliloquy.tools.testing.Mock.generateMockStaticProvider;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;

public class CombinationTest extends SpriteRendererTest {
    protected static RectangleRenderable RectangleRenderable;
    protected static Renderer<RectangleRenderable> RectangleRenderer;

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
    private final static ProviderAtTime<FloatBox> RECT_RENDERING_AREA_PROVIDER =
            WHOLE_SCREEN_PROVIDER;
    private final static String TILE_LOCATION =
            "./src/test/resources/images/tiles/sergey-shmidt-koy6FlCCy5s-unsplash.jpg";

    public static void main(String[] args) {
        runTest(
                resManager -> generateRenderablesAndRenderersWithMeshAndShader(
                        0,
                        Color.BLACK,
                        null,
                        resManager::windowWidthToHeightRatio
                ),
                CombinationTest::graphicsPreloaderLoadAction,
                DisplayTest::closeAfterSomeTime
        );
    }

    @SuppressWarnings("rawtypes")
    public static Set<Renderer> generateRenderablesAndRenderersWithMeshAndShader(
            float borderThickness, Color borderColor,
            ColorShiftStackAggregator colorShiftStackAggregator,
            Supplier<Float> getScreenWToHRatio) {
        Sprite = new FakeSprite(null, 266, 271, 313, 343);

        SpriteRenderingDimensions = floatBoxOf(0.25f, 0.125f, 0.75f, 0.875f);

        SpriteRenderable = new SpriteRenderableImpl(Sprite, staticProvider(borderThickness),
                staticProvider(borderColor), listOf(), staticProvider(SpriteRenderingDimensions), 1,
                java.util.UUID.randomUUID(), MockTopLevelComponent, RENDERING_BOUNDARIES,
                TimestampValidator);

        SpriteRenderer = new SpriteRenderer(RENDERING_BOUNDARIES,
                getScreenWToHRatio,
                colorShiftStackAggregator == null ?
                        mock(ColorShiftStackAggregator.class) :
                        colorShiftStackAggregator,
                TimestampValidator);

        Renderers.put(SpriteRenderableImpl.class, SpriteRenderer);



        RectangleRenderer = new RectangleRenderer(TimestampValidator,
                new TriangleSegmentRenderer(RENDERING_BOUNDARIES, new BasicTriangleRenderer()));
        RectangleRenderable = new RectangleRenderableImpl(
                TOP_LEFT_COLOR_PROVIDER,
                TOP_RIGHT_COLOR_PROVIDER,
                BOTTOM_RIGHT_COLOR_PROVIDER,
                BOTTOM_LEFT_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER,
                staticProvider(1f),
                staticProvider(0f),
                staticProvider(1f),
                staticProvider(0f),
                null,
                null,
                null,
                null,
                RECT_RENDERING_AREA_PROVIDER,
                0,
                java.util.UUID.randomUUID(),
                MockFirstChildComponent,
                RENDERING_BOUNDARIES,
                TimestampValidator
        );

        Renderers.put(RectangleRenderableImpl.class, RectangleRenderer);



        lenient().when(MockFirstChildComponent.contentsRepresentation()).thenReturn(
                setOf(RectangleRenderable, SpriteRenderable));

        return setOf(SpriteRenderer, RectangleRenderer);
    }

    protected static void graphicsPreloaderLoadAction() {
        var imageFactory = new ImageFactoryImpl(0.5f);
        var rectTileImage = imageFactory.make(new ImageDefinition(TILE_LOCATION, false));
        RectangleRenderable.setTextureIdProvider(staticProvider(rectTileImage.textureId()));
        RectangleRenderable.setTextureTilesPerWidthProvider(staticProvider(1f));
        RectangleRenderable.setTextureTilesPerHeightProvider(staticProvider(1f));
        Sprite.Image = imageFactory.make(new ImageDefinition(RPG_WEAPONS_RELATIVE_LOCATION, true));
        FrameTimer.ShouldExecuteNextFrame = true;
    }
}
