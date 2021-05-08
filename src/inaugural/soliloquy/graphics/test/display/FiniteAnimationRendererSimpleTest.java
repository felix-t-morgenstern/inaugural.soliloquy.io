package inaugural.soliloquy.graphics.test.display;

import inaugural.soliloquy.common.test.fakes.FakeCoordinateFactory;
import inaugural.soliloquy.graphics.api.WindowResolution;
import inaugural.soliloquy.graphics.bootstrap.GraphicsCoreLoopImpl;
import inaugural.soliloquy.graphics.bootstrap.assetfactories.AnimationFactory;
import inaugural.soliloquy.graphics.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.graphics.renderables.providers.StaticProvider;
import inaugural.soliloquy.graphics.rendering.MeshImpl;
import inaugural.soliloquy.graphics.rendering.WindowResolutionManagerImpl;
import inaugural.soliloquy.graphics.rendering.factories.ShaderFactoryImpl;
import inaugural.soliloquy.graphics.rendering.renderers.FiniteAnimationRenderer;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.graphics.assets.Animation;
import soliloquy.specs.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.graphics.assets.Image;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.AnimationDefinition;
import soliloquy.specs.graphics.renderables.FiniteAnimationRenderable;
import soliloquy.specs.graphics.rendering.Mesh;
import soliloquy.specs.graphics.rendering.WindowDisplayMode;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.function.Function;

import static inaugural.soliloquy.graphics.api.Constants.MS_PER_SECOND;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

class FiniteAnimationRendererSimpleTest {
    private final static FakeCoordinateFactory COORDINATE_FACTORY = new FakeCoordinateFactory();
    private final static float[] MESH_DATA =
            new float[] {0f, 1f, 1f, 1f, 1f, 0f, 1f, 0f, 0f, 0f, 0f, 1f};
    private final static FakeRenderingBoundaries RENDERING_BOUNDARIES =
            new FakeRenderingBoundaries();
    private final static FakeFloatBoxFactory FLOAT_BOX_FACTORY = new FakeFloatBoxFactory();
    private final static String EXPLOSION_RELATIVE_LOCATION =
            "./res/images/effects/Explosion.png";
    private static final String SHADER_FILENAME_PREFIX = "./res/shaders/defaultShader";

    private static FakeFiniteAnimationRenderable FiniteAnimationRenderable;

    public static void main(String[] args) {
        WindowResolution resolution = WindowResolution.RES_1920x1080;

        WindowResolutionManagerImpl windowManager = new WindowResolutionManagerImpl(
                WindowDisplayMode.WINDOWED, resolution, COORDINATE_FACTORY);

        FakeFrameTimer frameTimer = new FakeFrameTimer();
        Function<float[], Function<float[], Mesh>> meshFactory = f1 -> f2 -> new MeshImpl(f1, f2);

        FakeStackRenderer stackRenderer = new FakeStackRenderer();

        RENDERING_BOUNDARIES.CurrentBoundaries = new FakeFloatBox(0.0f, 0.0f, 1.0f, 1.0f);

        FakeGlobalClock globalClock = new FakeGlobalClock();

        AssetFactory<AnimationDefinition, Animation> animationFactory = new AnimationFactory();

        HashMap<Integer, AnimationFrameSnippet> frames = new HashMap<>();

        int numberOfFrames = 12;
        int frameWidth = 96;
        int frameHeight = 96;
        int frameDuration = MS_PER_SECOND / 16;

        FakeAnimationDefinition animationDefinition =
                new FakeAnimationDefinition(frameDuration * numberOfFrames, "explosion", frames);

        float animationHeight = 0.5f;
        float animationWidth = (((float)frameWidth / (float)frameHeight) * animationHeight)
                / resolution.widthToHeightRatio();
        float midpoint = 0.5f;
        int msPadding = 500;

        FiniteAnimationRenderable = new FakeFiniteAnimationRenderable(null, new ArrayList<>(),
                new StaticProvider<>(new FakeFloatBox(
                        midpoint - (animationWidth / 2f),
                        midpoint - (animationHeight / 2f),
                        midpoint + (animationWidth / 2f),
                        midpoint + (animationHeight / 2f))),
                0L, new FakeEntityUuid());

        FakeGraphicsPreloader graphicsPreloader = new FakeGraphicsPreloader();

        Renderer<FiniteAnimationRenderable> finiteAnimationRenderer =
                new FiniteAnimationRenderer(RENDERING_BOUNDARIES, FLOAT_BOX_FACTORY);
        @SuppressWarnings("rawtypes") Collection<Renderer> renderersWithMesh =
                new ArrayList<Renderer>() {{
                    add(finiteAnimationRenderer);
                }};
        @SuppressWarnings("rawtypes") Collection<Renderer> renderersWithShader =
                new ArrayList<Renderer>() {{
                    add(finiteAnimationRenderer);
                }};

        stackRenderer.RenderAction = timestamp ->
                finiteAnimationRenderer.render(FiniteAnimationRenderable, timestamp);

        GraphicsCoreLoop graphicsCoreLoop = new GraphicsCoreLoopImpl("My title bar",
                new FakeGLFWMouseButtonCallback(), frameTimer, 20, windowManager, stackRenderer,
                new ShaderFactoryImpl(), renderersWithShader, SHADER_FILENAME_PREFIX, meshFactory,
                renderersWithMesh, MESH_DATA, MESH_DATA, graphicsPreloader);

        graphicsPreloader.LoadAction = () -> {
            long timestamp = globalClock.globalTimestamp();
            Image renderableImage = new ImageFactoryImpl(0.5f)
                    .make(EXPLOSION_RELATIVE_LOCATION, false);
            for (int i = 0; i < numberOfFrames; i++) {
                frames.put(frameDuration * i, new FakeAnimationFrameSnippet(renderableImage,
                        frameWidth * i, 0, frameWidth * (i + 1), frameHeight, 0f, 0f));
            }
            FiniteAnimationRenderable.Animation = animationFactory.make(animationDefinition);
            FiniteAnimationRenderable.StartTimestamp = timestamp + msPadding;
            frameTimer.ShouldExecuteNextFrame = true;
        };

        graphicsCoreLoop.startup(() ->
                closeAfterSomeTime(graphicsCoreLoop,
                        frameDuration * numberOfFrames + (msPadding * 2)));
    }

    private static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop, int ms) {
        CheckedExceptionWrapper.sleep(ms);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
