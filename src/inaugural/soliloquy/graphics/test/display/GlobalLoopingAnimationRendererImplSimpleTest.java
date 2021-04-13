package inaugural.soliloquy.graphics.test.display;

import inaugural.soliloquy.graphics.api.WindowResolution;
import inaugural.soliloquy.graphics.bootstrap.GraphicsCoreLoopImpl;
import inaugural.soliloquy.graphics.bootstrap.assetfactories.AnimationFactory;
import inaugural.soliloquy.graphics.rendering.GlobalLoopingAnimationRenderer;
import inaugural.soliloquy.graphics.rendering.MeshImpl;
import inaugural.soliloquy.graphics.rendering.WindowResolutionManagerImpl;
import inaugural.soliloquy.graphics.rendering.factories.ShaderFactoryImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.graphics.assets.Animation;
import soliloquy.specs.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.AnimationDefinition;
import soliloquy.specs.graphics.renderables.GlobalLoopingAnimationRenderable;
import soliloquy.specs.graphics.rendering.Mesh;
import soliloquy.specs.graphics.rendering.Renderer;
import soliloquy.specs.graphics.rendering.WindowDisplayMode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.function.Function;

import static inaugural.soliloquy.graphics.api.Constants.MS_PER_SECOND;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 1920x1080 pixels in the middle of the screen for 3000ms
 *    with a titlebar reading "My title bar". The window will contain a looping animation of a
 *    torch, centered in the screen, which will change its frames every 250ms. The animation will
 *    persist for 2250ms.
 * 2. The window will then close.
 *
 */
class GlobalLoopingAnimationRendererImplSimpleTest {
    private final static float[] MESH_DATA =
            new float[] {0f, 1f, 1f, 1f, 1f, 0f, 1f, 0f, 0f, 0f, 0f, 1f};
    private final static FakeRenderingBoundaries RENDERING_BOUNDARIES =
            new FakeRenderingBoundaries();
    private final static FakeFloatBoxFactory FLOAT_BOX_FACTORY = new FakeFloatBoxFactory();
    private final static String TORCH_RELATIVE_LOCATION =
            "./res/images/fixtures/animated_torch.png";
    private static final String SHADER_FILENAME_PREFIX = "./res/shaders/defaultShader";

    private static FakeGlobalLoopingAnimationRenderable GlobalLoopingAnimationRenderable;

    public static void main(String[] args) {
        WindowResolution resolution = WindowResolution.RES_1920x1080;

        WindowResolutionManagerImpl windowManager =
                new WindowResolutionManagerImpl(WindowDisplayMode.WINDOWED, resolution);

        FakeFrameTimer frameTimer = new FakeFrameTimer();
        Function<float[], Function<float[], Mesh>> meshFactory = f1 -> f2 -> new MeshImpl(f1, f2);

        FakeStackRenderer stackRenderer = new FakeStackRenderer();

        RENDERING_BOUNDARIES.CurrentBoundaries = new FakeFloatBox(0.0f, 0.0f, 1.0f, 1.0f);

        FakeImageLoadable renderableImage = new FakeImageLoadable(TORCH_RELATIVE_LOCATION);

        FakeGlobalClock globalClock = new FakeGlobalClock();

        AssetFactory<AnimationDefinition, Animation> animationFactory = new AnimationFactory();

        HashMap<Integer, AnimationFrameSnippet> frames = new HashMap<>();

        int numberOfFrames = 9;
        int frameWidth = 32;
        int frameHeight = 64;
        int frameDuration = MS_PER_SECOND / 4;
        int loopsToDisplay = 3;
        for (int i = 0; i < numberOfFrames; i++) {
            frames.put(frameDuration * i, new FakeAnimationFrameSnippet(renderableImage,
                    frameWidth * i, 0, frameWidth * (i + 1), frameHeight, 0f, 0f));
        }

        FakeAnimationDefinition animationDefinition =
                new FakeAnimationDefinition(frameDuration * numberOfFrames, "torch", frames);

        FakeRenderableAnimation renderableAnimation = new FakeRenderableAnimation(null, 0L);

        float screenHeight = 0.5f;
        float screenWidth = ((float)frameWidth / (float)frameHeight)
                / resolution.widthToHeightRatio();
        float midpoint = 0.5f;

        GlobalLoopingAnimationRenderable = new FakeGlobalLoopingAnimationRenderable(
                renderableAnimation,
                new ArrayList<>(),
                new FakeFloatBox(
                        midpoint - (screenWidth / 2f),
                        midpoint - (screenHeight / 2f),
                        midpoint + (screenWidth / 2f),
                        midpoint + (screenHeight / 2f)));

        FakeGraphicsPreloader graphicsPreloader = new FakeGraphicsPreloader();

        Renderer<GlobalLoopingAnimationRenderable> globalLoopingAnimationRenderer =
                new GlobalLoopingAnimationRenderer(RENDERING_BOUNDARIES, FLOAT_BOX_FACTORY);
        @SuppressWarnings("rawtypes") Collection<Renderer> renderersWithMesh =
                new ArrayList<Renderer>() {{
                    add(globalLoopingAnimationRenderer);
                }};
        @SuppressWarnings("rawtypes") Collection<Renderer> renderersWithShader =
                new ArrayList<Renderer>() {{
                    add(globalLoopingAnimationRenderer);
                }};

        stackRenderer.RenderAction = timestamp ->
                globalLoopingAnimationRenderer.render(GlobalLoopingAnimationRenderable, timestamp);

        GraphicsCoreLoop graphicsCoreLoop = new GraphicsCoreLoopImpl("My title bar",
                new FakeGLFWMouseButtonCallback(), frameTimer, 20, windowManager, stackRenderer,
                new ShaderFactoryImpl(), renderersWithShader, SHADER_FILENAME_PREFIX, meshFactory,
                renderersWithMesh, MESH_DATA, MESH_DATA, graphicsPreloader);

        graphicsPreloader.LoadAction = () -> {
            renderableImage.load();
            renderableAnimation.Animation = animationFactory.make(animationDefinition);
            renderableAnimation.StartTimestamp = globalClock.globalTimestamp();
            frameTimer.ShouldExecuteNextFrame = true;
        };

        graphicsCoreLoop.startup(() ->
                closeAfterSomeTime(graphicsCoreLoop,
                        frameDuration * numberOfFrames * loopsToDisplay));
    }

    private static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop, int ms) {
        CheckedExceptionWrapper.sleep(ms);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
