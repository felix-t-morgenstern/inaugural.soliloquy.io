package inaugural.soliloquy.graphics.test.display;

import inaugural.soliloquy.graphics.api.WindowResolution;
import inaugural.soliloquy.graphics.bootstrap.GraphicsCoreLoopImpl;
import inaugural.soliloquy.graphics.io.MouseEventCapturingSpatialIndexImpl;
import inaugural.soliloquy.graphics.io.MouseEventHandlerImpl;
import inaugural.soliloquy.graphics.io.MouseListenerImpl;
import inaugural.soliloquy.graphics.renderables.providers.StaticProviderImpl;
import inaugural.soliloquy.graphics.rendering.FrameExecutorImpl;
import inaugural.soliloquy.graphics.rendering.MeshImpl;
import inaugural.soliloquy.graphics.rendering.RenderableStackImpl;
import inaugural.soliloquy.graphics.rendering.WindowResolutionManagerImpl;
import inaugural.soliloquy.graphics.rendering.factories.ShaderFactoryImpl;
import inaugural.soliloquy.graphics.rendering.renderers.RenderersImpl;
import inaugural.soliloquy.graphics.rendering.renderers.StackRendererImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.graphics.assets.Sprite;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.io.MouseCursor;
import soliloquy.specs.graphics.io.MouseEventCapturingSpatialIndex;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.renderables.providers.StaticProvider;
import soliloquy.specs.graphics.rendering.*;
import soliloquy.specs.graphics.rendering.renderers.Renderer;
import soliloquy.specs.graphics.rendering.renderers.Renderers;

import java.awt.*;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

import static inaugural.soliloquy.graphics.api.Constants.WHOLE_SCREEN;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DisplayTest {
    protected final static float[] MESH_DATA =
            new float[]{0f, 1f, 1f, 1f, 1f, 0f, 1f, 0f, 0f, 0f, 0f, 1f};
    protected final static RenderingBoundaries RENDERING_BOUNDARIES =
            mock(RenderingBoundaries.class);
    protected final static FakeFloatBoxFactory FLOAT_BOX_FACTORY = new FakeFloatBoxFactory();
    protected final static String SHADER_FILENAME_PREFIX =
            "./src/main/resources/shaders/defaultShader";
    protected final static UUID UUID = java.util.UUID.randomUUID();

    protected final static ProviderAtTime<Float> ZERO_PROVIDER =
            new StaticProviderImpl<>(java.util.UUID.randomUUID(), 0f, null);
    protected final static ProviderAtTime<Color> BLACK_PROVIDER =
            new StaticProviderImpl<>(java.util.UUID.randomUUID(), Color.BLACK, null);

    protected final static WindowResolution RESOLUTION = WindowResolution.RES_1920x1080;
    protected final static FakeGlobalClock GLOBAL_CLOCK = new FakeGlobalClock();

    protected final static TimestampValidator TIMESTAMP_VALIDATOR = new TimestampValidator(null);

    protected static FakeFrameTimer FrameTimer;
    protected static RenderableStack TopLevelStack;
    protected static Renderers Renderers;
    protected static MouseCursor MouseCursor = new FakeMouseCursor();
    protected static MouseEventCapturingSpatialIndex MOUSE_EVENT_CAPTURING_SPATIAL_INDEX;

    /** @noinspection rawtypes */
    protected static void runTest(Function<WindowResolutionManager, List<Renderer>>
                                          generateRenderablesAndRenderersWithMeshAndShader,
                                  Runnable graphicsPreloaderLoadAction,
                                  Consumer<GraphicsCoreLoop> closeAfterSomeTime) {
        WindowResolutionManagerImpl windowResolutionManager = new WindowResolutionManagerImpl(
                WindowDisplayMode.WINDOWED, RESOLUTION);

        FrameTimer = new FakeFrameTimer();

        Function<float[], Function<float[], Mesh>> meshFactory = f1 -> f2 -> new MeshImpl(f1, f2);

        Renderers = new RenderersImpl(TIMESTAMP_VALIDATOR);

        var stackRenderer = new StackRendererImpl(Renderers, RENDERING_BOUNDARIES, null);

        when(RENDERING_BOUNDARIES.currentBoundaries()).thenReturn(WHOLE_SCREEN);

        FakeGraphicsPreloader graphicsPreloader = new FakeGraphicsPreloader();

        MOUSE_EVENT_CAPTURING_SPATIAL_INDEX = new MouseEventCapturingSpatialIndexImpl();

        var mouseEventHandler = new MouseEventHandlerImpl(MOUSE_EVENT_CAPTURING_SPATIAL_INDEX);

        var mouseListener = new MouseListenerImpl(mouseEventHandler);

        TopLevelStack = new RenderableStackImpl();

        var frameExecutor = new FrameExecutorImpl(TopLevelStack, stackRenderer, 100);

        var renderersWithMeshAndShader =
                generateRenderablesAndRenderersWithMeshAndShader.apply(windowResolutionManager);

        var graphicsCoreLoop =
                new GraphicsCoreLoopImpl("My title bar", new FakeGLFWMouseButtonCallback(),
                        FrameTimer, 0, windowResolutionManager, GLOBAL_CLOCK, frameExecutor,
                        new ShaderFactoryImpl(), renderersWithMeshAndShader, SHADER_FILENAME_PREFIX,
                        meshFactory, renderersWithMeshAndShader, MESH_DATA, MESH_DATA,
                        graphicsPreloader, MouseCursor, mouseListener);

        graphicsPreloader.LoadAction = graphicsPreloaderLoadAction;

        graphicsCoreLoop.startup(() -> closeAfterSomeTime.accept(graphicsCoreLoop));
    }

    public static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        closeAfterSomeTime(graphicsCoreLoop, 3000);
    }

    public static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop, int totalMs) {
        CheckedExceptionWrapper.sleep(totalMs);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }

    protected static <T> StaticProvider<T> staticProvider(T value) {
        return new StaticProviderImpl<>(java.util.UUID.randomUUID(), value, null);
    }

    protected static <T> StaticProvider<T> staticNullProvider(T archetype) {
        return new StaticProviderImpl<>(java.util.UUID.randomUUID(), null, archetype, null);
    }

    protected static Sprite generateMockSprite(int leftX, int topY, int rightX, int bottomY) {
        var mockSprite = mock(Sprite.class);

        when(mockSprite.leftX()).thenReturn(leftX);
        when(mockSprite.topY()).thenReturn(topY);
        when(mockSprite.rightX()).thenReturn(rightX);
        when(mockSprite.bottomY()).thenReturn(bottomY);

        return mockSprite;
    }
}
