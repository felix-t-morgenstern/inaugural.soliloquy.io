package inaugural.soliloquy.graphics.test.display.rendering.renderers.finiteanimationrenderer;

import inaugural.soliloquy.graphics.bootstrap.assetfactories.AnimationFactory;
import inaugural.soliloquy.graphics.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.graphics.renderables.FiniteAnimationRenderableImpl;
import inaugural.soliloquy.graphics.rendering.FloatBoxImpl;
import inaugural.soliloquy.graphics.rendering.renderers.FiniteAnimationRenderer;
import inaugural.soliloquy.graphics.test.display.DisplayTest;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeAnimationFrameSnippet;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeColorShiftStackAggregator;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.graphics.assets.Animation;
import soliloquy.specs.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.AnimationDefinition;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageDefinition;
import soliloquy.specs.graphics.renderables.FiniteAnimationRenderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShiftStackAggregator;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.List;
import java.util.Map;

import static inaugural.soliloquy.graphics.api.Constants.MS_PER_SECOND;
import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

class FiniteAnimationRendererTest extends DisplayTest {
    protected final static int NUMBER_OF_FRAMES = 12;
    protected final static int FRAME_WIDTH = 96;
    protected final static int FRAME_HEIGHT = 96;
    protected final static int FRAME_DURATION = MS_PER_SECOND / 16;
    protected final static float ANIMATION_HEIGHT = 0.5f;
    @SuppressWarnings("PointlessArithmeticExpression")
    protected final static float ANIMATION_WIDTH =
            (((float) FRAME_WIDTH / (float) FRAME_HEIGHT) * ANIMATION_HEIGHT)
                    / RESOLUTION.widthToHeightRatio();
    protected final static float MIDPOINT = 0.5f;
    protected final static int MS_PADDING = 500;
    protected final static String EXPLOSION_RELATIVE_LOCATION =
            "./src/test/resources/images/effects/Explosion.png";
    protected final static Map<Integer, AnimationFrameSnippet> FRAMES = mapOf();
    protected final static AssetFactory<AnimationDefinition, Animation> ANIMATION_FACTORY =
            new AnimationFactory();

    protected static AnimationDefinition AnimationDefinition;
    protected static FiniteAnimationRenderable FiniteAnimationRenderable;
    protected static Renderer<FiniteAnimationRenderable> FiniteAnimationRenderer;
    protected static int TestDurationMs;

    /** @noinspection rawtypes */
    protected static List<Renderer> generateRenderablesAndRenderersWithMeshAndShader(
            @SuppressWarnings("unused") WindowResolutionManager windowResolutionManager,
            ColorShiftStackAggregator colorShiftStackAggregator) {
        TestDurationMs = FRAME_DURATION * NUMBER_OF_FRAMES + (MS_PADDING * 2);

        AnimationDefinition = new AnimationDefinition("explosion",
                FRAME_DURATION * NUMBER_OF_FRAMES, FRAMES);

        FiniteAnimationRenderer = new FiniteAnimationRenderer(RENDERING_BOUNDARIES,
                FLOAT_BOX_FACTORY,
                colorShiftStackAggregator == null ?
                        new FakeColorShiftStackAggregator() :
                        colorShiftStackAggregator,
                null);

        return listOf(FiniteAnimationRenderer);
    }

    protected static void graphicsPreloaderLoadAction() {
        var timestamp = GLOBAL_CLOCK.globalTimestamp();
        var renderableImage = new ImageFactoryImpl(0.5f)
                .make(new ImageDefinition(EXPLOSION_RELATIVE_LOCATION, false));
        for (var i = 0; i < NUMBER_OF_FRAMES; i++) {
            FRAMES.put(FRAME_DURATION * i, new FakeAnimationFrameSnippet(renderableImage,
                    FRAME_WIDTH * i, 0, FRAME_WIDTH * (i + 1), FRAME_HEIGHT, 0f, 0f));
        }

        FiniteAnimationRenderable = new FiniteAnimationRenderableImpl(
                ANIMATION_FACTORY.make(AnimationDefinition),
                staticNullProvider(0f),
                staticNullProvider(Color.BLACK),
                listOf(),
                staticProvider(new FloatBoxImpl(
                        MIDPOINT - (ANIMATION_WIDTH / 2f),
                        MIDPOINT - (ANIMATION_HEIGHT / 2f),
                        MIDPOINT + (ANIMATION_WIDTH / 2f),
                        MIDPOINT + (ANIMATION_HEIGHT / 2f))),
                123,
                java.util.UUID.randomUUID(),
                TopLevelStack,
                RENDERING_BOUNDARIES,
                timestamp + MS_PADDING,
                null,
                null
        );

        TopLevelStack.add(FiniteAnimationRenderable);
        Renderers.registerRenderer(FiniteAnimationRenderable.class.getCanonicalName(),
                FiniteAnimationRenderer);

        FrameTimer.ShouldExecuteNextFrame = true;
    }

    public static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop, int ms) {
        CheckedExceptionWrapper.sleep(ms);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
