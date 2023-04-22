package inaugural.soliloquy.graphics.test.display.rendering.renderers.stackrenderer;

import inaugural.soliloquy.graphics.api.WindowResolution;
import inaugural.soliloquy.graphics.bootstrap.GraphicsCoreLoopImpl;
import inaugural.soliloquy.graphics.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.graphics.renderables.SpriteRenderableImpl;
import inaugural.soliloquy.graphics.renderables.providers.StaticProviderImpl;
import inaugural.soliloquy.graphics.rendering.*;
import inaugural.soliloquy.graphics.rendering.factories.ShaderFactoryImpl;
import inaugural.soliloquy.graphics.rendering.renderers.SpriteRenderer;
import inaugural.soliloquy.graphics.rendering.renderers.StackRendererImpl;
import inaugural.soliloquy.graphics.test.display.DisplayTest;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageDefinition;
import soliloquy.specs.graphics.io.MouseCursor;
import soliloquy.specs.graphics.io.MouseListener;
import soliloquy.specs.graphics.rendering.Mesh;
import soliloquy.specs.graphics.rendering.WindowDisplayMode;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static inaugural.soliloquy.graphics.api.Constants.WHOLE_SCREEN;
import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StackRendererTest extends DisplayTest {
    private final static String AXE_07_LOCATION =
            "./src/test/resources/images/items/Axe_512x512_NoBG_07.png";
    private final static String AXE_09_LOCATION =
            "./src/test/resources/images/items/Axe_512x512_NoBG_09.png";
    private final static String SWORD_06_LOCATION =
            "./src/test/resources/images/items/Sword06_986×2658.png";

    public static void runTest(Consumer<GraphicsCoreLoop> closeAfterSomeTime) {
        var resolution = WindowResolution.RES_1920x1080;

        var windowResolutionManager = new WindowResolutionManagerImpl(WindowDisplayMode.WINDOWED, resolution);

        var frameTimer = mock(soliloquy.specs.graphics.rendering.timing.FrameTimer.class);
        Function<float[], Function<float[], Mesh>> meshFactory = f1 -> f2 -> new MeshImpl(f1, f2);

        var renderer = new FakeRenderer();

        var renderableStack = new RenderableStackImpl();

        when(RENDERING_BOUNDARIES.currentBoundaries()).thenReturn(WHOLE_SCREEN);

        var stackRenderer = new StackRendererImpl(renderer, RENDERING_BOUNDARIES, null);

        var spriteAxe07Width = 512;
        var spriteAxe07Height = 512;
        var spriteAxe07 = generateMockSprite(0, 0, spriteAxe07Width, spriteAxe07Height);
        var spriteAxe09Width = 512;
        var spriteAxe09Height = 512;
        var spriteAxe09 = generateMockSprite(0, 0, spriteAxe09Width, spriteAxe09Height);
        var spriteSword06Width = 986;
        var spriteSword06Height = 2658;
        var spriteSword06 = generateMockSprite(0, 0, spriteSword06Width, spriteSword06Height);

        var axeScreenHeight = 0.5f;
        var swordScreenHeight = 1.0f;

        var spriteAxe07ScreenWidth = (spriteAxe07Width / (float) spriteAxe07Height) *
                axeScreenHeight / resolution.widthToHeightRatio();
        var spriteAxe07TopY = 0.125f;
        var spriteAxe07LeftX = 0.625f - (spriteAxe07ScreenWidth / 2f);
        var spriteRenderable1 = new SpriteRenderableImpl(
                spriteAxe07,
                ZERO_PROVIDER, BLACK_PROVIDER,
                listOf(),
                new StaticProviderImpl<>(java.util.UUID.randomUUID(), new FloatBoxImpl(
                        spriteAxe07LeftX,
                        spriteAxe07TopY,
                        spriteAxe07LeftX + spriteAxe07ScreenWidth,
                        spriteAxe07TopY + axeScreenHeight
                ), null),
                1, java.util.UUID.randomUUID(), renderableStack, RENDERING_BOUNDARIES);

        var spriteAxe09ScreenWidth = (spriteAxe09Width / (float) spriteAxe09Height) *
                axeScreenHeight / resolution.widthToHeightRatio();
        var spriteAxe09TopY = 0.375f;
        var spriteAxe09LeftX = 0.375f - (spriteAxe09ScreenWidth / 2f);
        var spriteRenderable2 = new SpriteRenderableImpl(
                spriteAxe09,
                ZERO_PROVIDER, BLACK_PROVIDER,
                listOf(),
                new StaticProviderImpl<>(java.util.UUID.randomUUID(), new FakeFloatBox(
                        spriteAxe09LeftX,
                        spriteAxe09TopY,
                        spriteAxe09LeftX + spriteAxe09ScreenWidth,
                        spriteAxe09TopY + axeScreenHeight
                ), null),
                1, java.util.UUID.randomUUID(), renderableStack, RENDERING_BOUNDARIES);

        var spriteSword06ScreenWidth = 0.3710f;
        var spriteSword06TopY = 0f;
        var spriteSword06LeftX = 0.5f - (spriteSword06ScreenWidth / 2f);
        var spriteRenderable3 = new SpriteRenderableImpl(
                spriteSword06,
                ZERO_PROVIDER, BLACK_PROVIDER,
                listOf(),
                new StaticProviderImpl<>(java.util.UUID.randomUUID(), new FakeFloatBox(
                        spriteSword06LeftX,
                        spriteSword06TopY,
                        spriteSword06LeftX + spriteSword06ScreenWidth,
                        spriteSword06TopY + swordScreenHeight
                ), null),
                1, java.util.UUID.randomUUID(), renderableStack, RENDERING_BOUNDARIES);

        var graphicsPreloader = new FakeGraphicsPreloader();

        var spriteRenderer = new SpriteRenderer(RENDERING_BOUNDARIES,
                FLOAT_BOX_FACTORY, windowResolutionManager, new FakeColorShiftStackAggregator(),
                null);
        //noinspection rawtypes
        List<Renderer> renderersWithMesh = listOf(spriteRenderer);
        //noinspection rawtypes
        List<Renderer> renderersWithShader = listOf(spriteRenderer);

        renderer.SpriteRenderer = spriteRenderer;
        renderableStack.add(spriteRenderable1);
        renderableStack.add(spriteRenderable2);
        renderableStack.add(spriteRenderable3);

        var frameExecutor = new FrameExecutorImpl(renderableStack, stackRenderer, 100);

        var graphicsCoreLoop =
                new GraphicsCoreLoopImpl("My title bar", new FakeGLFWMouseButtonCallback(),
                        frameTimer, 20, windowResolutionManager, GLOBAL_CLOCK, frameExecutor,
                        new ShaderFactoryImpl(), renderersWithShader, SHADER_FILENAME_PREFIX,
                        meshFactory, renderersWithMesh, MESH_DATA, MESH_DATA, graphicsPreloader,
                        mock(MouseCursor.class), mock(MouseListener.class));

        graphicsPreloader.LoadAction = () -> {
            when(spriteAxe07.image()).thenReturn(new ImageFactoryImpl(0.5f)
                    .make(new ImageDefinition(AXE_07_LOCATION, false)));
            when(spriteAxe09.image()).thenReturn(new ImageFactoryImpl(0.5f)
                    .make(new ImageDefinition(AXE_09_LOCATION, false)));
            when(spriteSword06.image()).thenReturn(new ImageFactoryImpl(0.5f)
                    .make(new ImageDefinition(SWORD_06_LOCATION, false)));
            when(frameTimer.shouldExecuteNextFrame()).thenReturn(true);
        };

        graphicsCoreLoop.startup(() -> closeAfterSomeTime.accept(graphicsCoreLoop));
    }
}
