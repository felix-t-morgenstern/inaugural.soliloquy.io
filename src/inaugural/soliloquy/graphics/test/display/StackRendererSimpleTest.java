package inaugural.soliloquy.graphics.test.display;

import inaugural.soliloquy.common.test.fakes.FakeCoordinateFactory;
import inaugural.soliloquy.common.test.fakes.FakeListFactory;
import inaugural.soliloquy.common.test.fakes.FakeMapFactory;
import inaugural.soliloquy.graphics.api.WindowResolution;
import inaugural.soliloquy.graphics.bootstrap.GraphicsCoreLoopImpl;
import inaugural.soliloquy.graphics.renderables.providers.StaticProvider;
import inaugural.soliloquy.graphics.rendering.MeshImpl;
import inaugural.soliloquy.graphics.rendering.RenderableStackImpl;
import inaugural.soliloquy.graphics.rendering.WindowResolutionManagerImpl;
import inaugural.soliloquy.graphics.rendering.factories.ShaderFactoryImpl;
import inaugural.soliloquy.graphics.rendering.renderers.SpriteRenderer;
import inaugural.soliloquy.graphics.rendering.renderers.StackRendererImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.renderables.SpriteRenderable;
import soliloquy.specs.graphics.rendering.Mesh;
import soliloquy.specs.graphics.rendering.RenderableStack;
import soliloquy.specs.graphics.rendering.WindowDisplayMode;
import soliloquy.specs.graphics.rendering.renderers.Renderer;
import soliloquy.specs.graphics.rendering.renderers.StackRenderer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 1920x1080 pixels in the middle of the screen for 3000ms
 *    with a titlebar reading "My title bar". The window will contain three Sprites: A fiery axe,
 *    an evil sword, and a nature axe. The fiery axe will be in the lower-left, the evil sword will
 *    be in the center, and the nature axe will be in the upper-right. The evil sword will be
 *    displayed above the fiery axe, and the nature axe above the evil sword.
 * 2. The window will then close.
 *
 */
class StackRendererSimpleTest {
    private final static FakeCoordinateFactory COORDINATE_FACTORY = new FakeCoordinateFactory();
    private final static float[] MESH_DATA =
            new float[] {0f, 1f, 1f, 1f, 1f, 0f, 1f, 0f, 0f, 0f, 0f, 1f};
    private final static FakeRenderingBoundaries RENDERING_BOUNDARIES =
            new FakeRenderingBoundaries();
    private final static FakeFloatBoxFactory FLOAT_BOX_FACTORY = new FakeFloatBoxFactory();
    private final static String AXE_07_LOCATION = "./res/images/items/Axe_512x512_NoBG_07.png";
    private final static String AXE_09_LOCATION = "./res/images/items/Axe_512x512_NoBG_09.png";
    private final static String SWORD_06_LOCATION = "./res/images/items/Sword06_986×2658.png";
    private static final String SHADER_FILENAME_PREFIX = "./res/shaders/defaultShader";

    private static FakeSpriteRenderable SpriteRenderable1;
    private static FakeSpriteRenderable SpriteRenderable2;
    private static FakeSpriteRenderable SpriteRenderable3;

    public static void main(String[] args) {
        WindowResolution resolution = WindowResolution.RES_1920x1080;

        WindowResolutionManagerImpl windowResolutionManager = new WindowResolutionManagerImpl(
                WindowDisplayMode.WINDOWED, resolution, COORDINATE_FACTORY);

        FakeFrameTimer frameTimer = new FakeFrameTimer();
        Function<float[], Function<float[], Mesh>> meshFactory = f1 -> f2 -> new MeshImpl(f1, f2);

        FakeRenderer renderer = new FakeRenderer();

        FakeMapFactory mapFactory = new FakeMapFactory();
        FakeListFactory listFactory = new FakeListFactory();

        RenderableStack renderableStack = new RenderableStackImpl(mapFactory, listFactory);

        StackRenderer stackRenderer = new StackRendererImpl(renderableStack, renderer);

        RENDERING_BOUNDARIES.CurrentBoundaries = new FakeFloatBox(0.0f, 0.0f, 1.0f, 1.0f);

        FakeImageLoadable renderableImageAxe07 = new FakeImageLoadable(AXE_07_LOCATION);
        FakeImageLoadable renderableImageAxe09 = new FakeImageLoadable(AXE_09_LOCATION);
        FakeImageLoadable renderableImageSword06 = new FakeImageLoadable(SWORD_06_LOCATION);

        int spriteAxe07Width = 512;
        int spriteAxe07Height = 512;
        FakeSprite spriteAxe07 =
                new FakeSprite(renderableImageAxe07, 0, 0, spriteAxe07Width, spriteAxe07Height);
        int spriteAxe09Width = 512;
        int spriteAxe09Height = 512;
        FakeSprite spriteAxe09 =
                new FakeSprite(renderableImageAxe09, 0, 0, spriteAxe09Width, spriteAxe09Height);
        int spriteSword06Width = 986;
        int spriteSword06Height = 2658;
        FakeSprite spriteSword06 =
                new FakeSprite(renderableImageSword06, 0, 0, spriteSword06Width,
                        spriteSword06Height);

        float axeScreenHeight = 0.5f;
        float swordScreenHeight = 1.0f;

        float spriteAxe07ScreenWidth = (spriteAxe07Width / (float)spriteAxe07Height) *
                axeScreenHeight / resolution.widthToHeightRatio();
        float spriteAxe07TopY = 0.125f;
        float spriteAxe07LeftX = 0.625f - (spriteAxe07ScreenWidth / 2f);
        SpriteRenderable1 = new FakeSpriteRenderable(spriteAxe07, new ArrayList<>(),
                new StaticProvider<>(new FakeFloatBox(
                        spriteAxe07LeftX,
                        spriteAxe07TopY,
                        spriteAxe07LeftX + spriteAxe07ScreenWidth,
                        spriteAxe07TopY + axeScreenHeight
                )),
                1, new FakeEntityUuid());

        float spriteAxe09ScreenWidth = (spriteAxe09Width / (float)spriteAxe09Height) *
                axeScreenHeight / resolution.widthToHeightRatio();
        float spriteAxe09TopY = 0.375f;
        float spriteAxe09LeftX = 0.375f - (spriteAxe09ScreenWidth / 2f);
        SpriteRenderable2 = new FakeSpriteRenderable(spriteAxe09, new ArrayList<>(),
                new StaticProvider<>(new FakeFloatBox(
                        spriteAxe09LeftX,
                        spriteAxe09TopY,
                        spriteAxe09LeftX + spriteAxe09ScreenWidth,
                        spriteAxe09TopY + axeScreenHeight
                )),
                3, new FakeEntityUuid());

        float spriteSword06ScreenWidth = 0.3710f;
        float spriteSword06TopY = 0f;
        float spriteSword06LeftX = 0.5f - (spriteSword06ScreenWidth / 2f);
        SpriteRenderable3 = new FakeSpriteRenderable(spriteSword06, new ArrayList<>(),
                new StaticProvider<>(new FakeFloatBox(
                        spriteSword06LeftX,
                        spriteSword06TopY,
                        spriteSword06LeftX + spriteSword06ScreenWidth,
                        spriteSword06TopY + swordScreenHeight
                )),
                2, new FakeEntityUuid());

        FakeGraphicsPreloader graphicsPreloader = new FakeGraphicsPreloader();

        Renderer<SpriteRenderable> spriteRenderer = new SpriteRenderer(RENDERING_BOUNDARIES,
                FLOAT_BOX_FACTORY, windowResolutionManager);
        @SuppressWarnings("rawtypes") Collection<Renderer> renderersWithMesh =
                new ArrayList<Renderer>() {{
                    add(spriteRenderer);
                }};
        @SuppressWarnings("rawtypes") Collection<Renderer> renderersWithShader =
                new ArrayList<Renderer>() {{
                    add(spriteRenderer);
                }};

        renderer.SpriteRenderer = spriteRenderer;
        renderableStack.add(SpriteRenderable1);
        renderableStack.add(SpriteRenderable2);
        renderableStack.add(SpriteRenderable3);

        GraphicsCoreLoop graphicsCoreLoop = new GraphicsCoreLoopImpl("My title bar",
                new FakeGLFWMouseButtonCallback(), frameTimer, 20, windowResolutionManager, stackRenderer,
                new ShaderFactoryImpl(), renderersWithShader, SHADER_FILENAME_PREFIX, meshFactory,
                renderersWithMesh, MESH_DATA, MESH_DATA, graphicsPreloader);

        graphicsPreloader.LoadAction = () -> {
            renderableImageAxe07.load();
            renderableImageAxe09.load();
            renderableImageSword06.load();
            frameTimer.ShouldExecuteNextFrame = true;
        };

        graphicsCoreLoop.startup(() -> closeAfterSomeTime(graphicsCoreLoop));
    }

    private static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        CheckedExceptionWrapper.sleep(3000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
