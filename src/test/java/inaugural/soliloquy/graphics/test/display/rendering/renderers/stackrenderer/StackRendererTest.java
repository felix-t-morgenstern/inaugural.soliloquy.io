package inaugural.soliloquy.graphics.test.display.rendering.renderers.stackrenderer;

import inaugural.soliloquy.graphics.api.WindowResolution;
import inaugural.soliloquy.graphics.bootstrap.GraphicsCoreLoopImpl;
import inaugural.soliloquy.graphics.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.graphics.renderables.providers.StaticProviderImpl;
import inaugural.soliloquy.graphics.rendering.FrameExecutorImpl;
import inaugural.soliloquy.graphics.rendering.MeshImpl;
import inaugural.soliloquy.graphics.rendering.RenderableStackImpl;
import inaugural.soliloquy.graphics.rendering.WindowResolutionManagerImpl;
import inaugural.soliloquy.graphics.rendering.factories.ShaderFactoryImpl;
import inaugural.soliloquy.graphics.rendering.renderers.SpriteRenderer;
import inaugural.soliloquy.graphics.rendering.renderers.StackRendererImpl;
import inaugural.soliloquy.graphics.test.display.DisplayTest;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageDefinition;
import soliloquy.specs.graphics.io.MouseListener;
import soliloquy.specs.graphics.renderables.SpriteRenderable;
import soliloquy.specs.graphics.rendering.FrameExecutor;
import soliloquy.specs.graphics.rendering.Mesh;
import soliloquy.specs.graphics.rendering.RenderableStack;
import soliloquy.specs.graphics.rendering.WindowDisplayMode;
import soliloquy.specs.graphics.rendering.renderers.Renderer;
import soliloquy.specs.graphics.rendering.renderers.StackRenderer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

import static inaugural.soliloquy.graphics.api.Constants.WHOLE_SCREEN;
import static inaugural.soliloquy.graphics.api.Constants.WHOLE_SCREEN_PROVIDER;
import static org.mockito.Mockito.mock;

public class StackRendererTest extends DisplayTest {
    private final static String AXE_07_LOCATION =
            "./src/test/resources/images/items/Axe_512x512_NoBG_07.png";
    private final static String AXE_09_LOCATION =
            "./src/test/resources/images/items/Axe_512x512_NoBG_09.png";
    private final static String SWORD_06_LOCATION =
            "./src/test/resources/images/items/Sword06_986×2658.png";

    public static void runTest(Consumer<GraphicsCoreLoop> closeAfterSomeTime) {
        WindowResolution resolution = WindowResolution.RES_1920x1080;

        WindowResolutionManagerImpl windowResolutionManager = new WindowResolutionManagerImpl(
                WindowDisplayMode.WINDOWED, resolution);

        FakeFrameTimer frameTimer = new FakeFrameTimer();
        Function<float[], Function<float[], Mesh>> meshFactory = f1 -> f2 -> new MeshImpl(f1, f2);

        FakeRenderer renderer = new FakeRenderer();

        RenderableStack renderableStack = new RenderableStackImpl();

        StackRenderer stackRenderer =
                new StackRendererImpl(renderer, RENDERING_BOUNDARIES, null);

        RENDERING_BOUNDARIES.CurrentBoundaries = WHOLE_SCREEN;

        int spriteAxe07Width = 512;
        int spriteAxe07Height = 512;
        FakeSprite spriteAxe07 =
                new FakeSprite(null, 0, 0, spriteAxe07Width, spriteAxe07Height);
        int spriteAxe09Width = 512;
        int spriteAxe09Height = 512;
        FakeSprite spriteAxe09 =
                new FakeSprite(null, 0, 0, spriteAxe09Width, spriteAxe09Height);
        int spriteSword06Width = 986;
        int spriteSword06Height = 2658;
        FakeSprite spriteSword06 =
                new FakeSprite(null, 0, 0, spriteSword06Width, spriteSword06Height);

        float axeScreenHeight = 0.5f;
        float swordScreenHeight = 1.0f;

        float spriteAxe07ScreenWidth = (spriteAxe07Width / (float) spriteAxe07Height) *
                axeScreenHeight / resolution.widthToHeightRatio();
        float spriteAxe07TopY = 0.125f;
        float spriteAxe07LeftX = 0.625f - (spriteAxe07ScreenWidth / 2f);
        FakeSpriteRenderable spriteRenderable1 = new FakeSpriteRenderable(spriteAxe07,
                new ArrayList<>(),
                new StaticProviderImpl<>(java.util.UUID.randomUUID(), new FakeFloatBox(
                        spriteAxe07LeftX,
                        spriteAxe07TopY,
                        spriteAxe07LeftX + spriteAxe07ScreenWidth,
                        spriteAxe07TopY + axeScreenHeight
                ), null),
                1, java.util.UUID.randomUUID());

        float spriteAxe09ScreenWidth = (spriteAxe09Width / (float) spriteAxe09Height) *
                axeScreenHeight / resolution.widthToHeightRatio();
        float spriteAxe09TopY = 0.375f;
        float spriteAxe09LeftX = 0.375f - (spriteAxe09ScreenWidth / 2f);
        FakeSpriteRenderable spriteRenderable2 = new FakeSpriteRenderable(spriteAxe09,
                new ArrayList<>(),
                new StaticProviderImpl<>(java.util.UUID.randomUUID(), new FakeFloatBox(
                        spriteAxe09LeftX,
                        spriteAxe09TopY,
                        spriteAxe09LeftX + spriteAxe09ScreenWidth,
                        spriteAxe09TopY + axeScreenHeight
                ), null),
                3, java.util.UUID.randomUUID());

        float spriteSword06ScreenWidth = 0.3710f;
        float spriteSword06TopY = 0f;
        float spriteSword06LeftX = 0.5f - (spriteSword06ScreenWidth / 2f);
        FakeSpriteRenderable spriteRenderable3 = new FakeSpriteRenderable(spriteSword06,
                new ArrayList<>(),
                new StaticProviderImpl<>(java.util.UUID.randomUUID(), new FakeFloatBox(
                        spriteSword06LeftX,
                        spriteSword06TopY,
                        spriteSword06LeftX + spriteSword06ScreenWidth,
                        spriteSword06TopY + swordScreenHeight
                ), null),
                2, java.util.UUID.randomUUID());

        FakeGraphicsPreloader graphicsPreloader = new FakeGraphicsPreloader();

        Renderer<SpriteRenderable> spriteRenderer = new SpriteRenderer(RENDERING_BOUNDARIES,
                FLOAT_BOX_FACTORY, windowResolutionManager, new FakeColorShiftStackAggregator(),
                null);
        @SuppressWarnings("rawtypes") Collection<Renderer> renderersWithMesh =
                new ArrayList<Renderer>() {{
                    add(spriteRenderer);
                }};
        @SuppressWarnings("rawtypes") Collection<Renderer> renderersWithShader =
                new ArrayList<Renderer>() {{
                    add(spriteRenderer);
                }};

        renderer.SpriteRenderer = spriteRenderer;
        renderableStack.add(spriteRenderable1);
        renderableStack.add(spriteRenderable2);
        renderableStack.add(spriteRenderable3);

        FrameExecutor frameExecutor = new FrameExecutorImpl(renderableStack, stackRenderer, 100);

        GraphicsCoreLoop graphicsCoreLoop =
                new GraphicsCoreLoopImpl("My title bar", new FakeGLFWMouseButtonCallback(),
                        frameTimer, 20, windowResolutionManager, GLOBAL_CLOCK, frameExecutor,
                        new ShaderFactoryImpl(), renderersWithShader, SHADER_FILENAME_PREFIX,
                        meshFactory, renderersWithMesh, MESH_DATA, MESH_DATA, graphicsPreloader,
                        new FakeMouseCursor(), mock(MouseListener.class));

        graphicsPreloader.LoadAction = () -> {
            spriteAxe07.Image = new ImageFactoryImpl(0.5f)
                    .make(new ImageDefinition(AXE_07_LOCATION, false));
            spriteAxe09.Image = new ImageFactoryImpl(0.5f)
                    .make(new ImageDefinition(AXE_09_LOCATION, false));
            spriteSword06.Image = new ImageFactoryImpl(0.5f)
                    .make(new ImageDefinition(SWORD_06_LOCATION, false));
            frameTimer.ShouldExecuteNextFrame = true;
        };

        graphicsCoreLoop.startup(() -> closeAfterSomeTime.accept(graphicsCoreLoop));
    }
}
