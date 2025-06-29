package inaugural.soliloquy.io.test.display.rendering.renderers.globalloopinganimationrenderer;

import inaugural.soliloquy.io.graphics.bootstrap.assetfactories.AnimationFactory;
import inaugural.soliloquy.io.graphics.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.io.graphics.renderables.GlobalLoopingAnimationRenderableImpl;
import inaugural.soliloquy.io.graphics.renderables.providers.GlobalLoopingAnimationImpl;
import inaugural.soliloquy.io.graphics.rendering.renderers.GlobalLoopingAnimationRenderer;
import inaugural.soliloquy.io.test.display.DisplayTest;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeColorShiftStackAggregator;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.io.graphics.assets.Animation;
import soliloquy.specs.io.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.io.graphics.assets.GlobalLoopingAnimation;
import soliloquy.specs.io.graphics.assets.Image;
import soliloquy.specs.io.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.AnimationDefinition;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.ImageDefinition;
import soliloquy.specs.io.graphics.renderables.GlobalLoopingAnimationRenderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShiftStackAggregator;
import soliloquy.specs.io.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.List;
import java.util.Map;

import static inaugural.soliloquy.io.api.Constants.MS_PER_SECOND;
import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;

class GlobalLoopingAnimationRendererTest extends DisplayTest {
    protected final static AssetFactory<AnimationDefinition, Animation> ANIMATION_FACTORY =
            new AnimationFactory();
    protected final static Map<Integer, AnimationFrameSnippet> FRAMES = mapOf();
    protected final static String TORCH_RELATIVE_LOCATION =
            "./src/test/resources/images/fixtures/animated_torch_numbered.png";

    protected static final int NUMBER_OF_FRAMES = 9;
    protected static final int FRAME_WIDTH = 32;
    protected static final int FRAME_HEIGHT = 64;
    protected static final int FRAME_DURATION = MS_PER_SECOND / 4;
    protected static final int LOOPS_TO_DISPLAY = 3;
    protected static final int MS_DURATION = FRAME_DURATION * NUMBER_OF_FRAMES;
    protected static final float ANIMATION_HEIGHT = 0.5f;
    protected static final float ANIMATION_WIDTH = ((float) FRAME_WIDTH / (float) FRAME_HEIGHT)
            / RESOLUTION.widthToHeightRatio();
    protected static final float MIDPOINT = 0.5f;
    protected static final int TEST_DURATION_MS =
            FRAME_DURATION * NUMBER_OF_FRAMES * LOOPS_TO_DISPLAY;

    protected static GlobalLoopingAnimation GlobalLoopingAnimation;
    protected static GlobalLoopingAnimationRenderable GlobalLoopingAnimationRenderable;
    protected static Renderer<GlobalLoopingAnimationRenderable> GlobalLoopingAnimationRenderer;

    /** @noinspection rawtypes, unused */
    protected static List<Renderer> generateRenderablesAndRenderersWithMeshAndShader(
            WindowResolutionManager windowResolutionManager,
            ColorShiftStackAggregator colorShiftStackAggregator) {
        GlobalLoopingAnimationRenderer =
                new GlobalLoopingAnimationRenderer(
                        RENDERING_BOUNDARIES,
                        colorShiftStackAggregator == null ?
                                new FakeColorShiftStackAggregator() :
                                colorShiftStackAggregator,
                        null);

        return listOf(GlobalLoopingAnimationRenderer);
    }

    protected static void graphicsPreloaderLoadAction() {
        var renderableImage = new ImageFactoryImpl(0.5f)
                .make(new ImageDefinition(TORCH_RELATIVE_LOCATION, false));
        for (var i = 0; i < NUMBER_OF_FRAMES; i++) {
            FRAMES.put(FRAME_DURATION * i,
                    makeSnippet(renderableImage, FRAME_WIDTH * i, 0, FRAME_WIDTH * (i + 1),
                            FRAME_HEIGHT, 0f, 0f));
        }
        var globalLoopingAnimationStartTimestamp = GLOBAL_CLOCK.globalTimestamp();

        var animationDefinition = new AnimationDefinition("torch", MS_DURATION, FRAMES);

        var animation = ANIMATION_FACTORY.make(animationDefinition);

        var periodModuloOffset =
                MS_DURATION - (int) (globalLoopingAnimationStartTimestamp % (MS_DURATION));

        GlobalLoopingAnimation = new GlobalLoopingAnimationImpl("globalLoopingAnimationId",
                animation, periodModuloOffset, null);

        GlobalLoopingAnimationRenderable =
                new GlobalLoopingAnimationRenderableImpl(GlobalLoopingAnimation,
                        staticNullProvider(0f),
                        staticNullProvider(Color.BLACK),
                        listOf(),
                        staticProvider(floatBoxOf(
                                MIDPOINT - (ANIMATION_WIDTH / 2f),
                                MIDPOINT - (ANIMATION_HEIGHT / 2f),
                                MIDPOINT + (ANIMATION_WIDTH / 2f),
                                MIDPOINT + (ANIMATION_HEIGHT / 2f))),
                        0, java.util.UUID.randomUUID(), FirstChildStack, RENDERING_BOUNDARIES);

        FirstChildStack.add(GlobalLoopingAnimationRenderable);
        Renderers.registerRenderer(GlobalLoopingAnimationRenderableImpl.class,
                GlobalLoopingAnimationRenderer);

        FrameTimer.ShouldExecuteNextFrame = true;
    }

    public static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop, int ms) {
        CheckedExceptionWrapper.sleep(ms);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }

    private static AnimationFrameSnippet makeSnippet(Image renderableImage, int leftX, int topY,
                                                     int rightX, int bottomY, float offsetX,
                                                     float offsetY) {
        return new AnimationFrameSnippet() {
            @Override
            public float offsetX() {
                return offsetX;
            }

            @Override
            public float offsetY() {
                return offsetY;
            }

            @Override
            public Image image() {
                return renderableImage;
            }

            @Override
            public int leftX() {
                return leftX;
            }

            @Override
            public int topY() {
                return topY;
            }

            @Override
            public int rightX() {
                return rightX;
            }

            @Override
            public int bottomY() {
                return bottomY;
            }
        };
    }
}
