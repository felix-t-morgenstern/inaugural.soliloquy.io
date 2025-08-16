package inaugural.soliloquy.io.test.integration.display;

import inaugural.soliloquy.io.api.WindowResolution;
import inaugural.soliloquy.io.graphics.bootstrap.GraphicsCoreLoopImpl;
import inaugural.soliloquy.io.graphics.renderables.providers.StaticProviderImpl;
import inaugural.soliloquy.io.graphics.rendering.FrameExecutorImpl;
import inaugural.soliloquy.io.graphics.rendering.MeshImpl;
import inaugural.soliloquy.io.graphics.rendering.RenderingBoundariesImpl;
import inaugural.soliloquy.io.graphics.rendering.WindowResolutionManagerImpl;
import inaugural.soliloquy.io.graphics.rendering.factories.ShaderFactoryImpl;
import inaugural.soliloquy.io.graphics.rendering.renderers.ComponentRendererImpl;
import inaugural.soliloquy.io.mouse.MouseEventCapturingSpatialIndexImpl;
import inaugural.soliloquy.io.mouse.MouseEventHandlerImpl;
import inaugural.soliloquy.io.mouse.MouseListener;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeFrameTimer;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeGlobalClock;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeGraphicsPreloader;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.assets.Sprite;
import soliloquy.specs.io.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.io.graphics.renderables.Renderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShiftStackAggregator;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.renderables.providers.StaticProvider;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.io.graphics.rendering.WindowDisplayMode;
import soliloquy.specs.io.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;
import soliloquy.specs.io.input.mouse.MouseCursor;
import soliloquy.specs.io.input.mouse.MouseEventCapturingSpatialIndex;
import soliloquy.specs.ui.Component;

import java.awt.*;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

import static inaugural.soliloquy.io.api.Constants.WHOLE_SCREEN;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.collections.Collections.setOf;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static soliloquy.specs.io.graphics.renderables.colorshifting.NetColorShifts.netShifts;

public class DisplayTest {
    protected final static float[] MESH_DATA =
            new float[]{0f, 1f, 1f, 1f, 1f, 0f, 1f, 0f, 0f, 0f, 0f, 1f};
    protected final static RenderingBoundaries RENDERING_BOUNDARIES = new RenderingBoundariesImpl();
    protected final static String SHADER_FILENAME_PREFIX =
            "./src/main/resources/shaders/defaultShader";
    protected final static UUID UUID = java.util.UUID.randomUUID();

    protected final static ProviderAtTime<Float> ZERO_PROVIDER =
            new StaticProviderImpl<>(java.util.UUID.randomUUID(), 0f, null);
    protected final static ProviderAtTime<Color> BLACK_PROVIDER =
            new StaticProviderImpl<>(java.util.UUID.randomUUID(), Color.BLACK, null);

    public final static ProviderAtTime<FloatBox> WHOLE_SCREEN_PROVIDER =
            new StaticProviderImpl<>(java.util.UUID.randomUUID(), WHOLE_SCREEN, mock(TimestampValidator.class));

    protected final static WindowResolution RESOLUTION = WindowResolution.RES_1680x1050;
    protected final static FakeGlobalClock GLOBAL_CLOCK = new FakeGlobalClock();

    protected static Map<Class<?>, Renderer<? extends Renderable>> Renderers;
    protected static FakeFrameTimer FrameTimer;
    protected static Component MockTopLevelComponent;
    protected static Component MockFirstChildComponent;
    protected static MouseCursor MouseCursor = mock(MouseCursor.class);
    protected static MouseEventCapturingSpatialIndex MouseEventCapturingSpatialIndex;
    protected static ColorShiftStackAggregator MockShiftAggregator;
    protected static TimestampValidator TimestampValidator;

    /** @noinspection rawtypes */
    protected static void runTest(Function<WindowResolutionManager, Set<Renderer>>
                                          generateRenderablesAndRenderersWithMeshAndShader,
                                  Runnable graphicsPreloaderLoadAction,
                                  Consumer<GraphicsCoreLoop> closeAfterSomeTime) {
        var windowResolutionManager =
                new WindowResolutionManagerImpl(WindowDisplayMode.WINDOWED, RESOLUTION);

        FrameTimer = new FakeFrameTimer();

        Renderers = mapOf();
        TimestampValidator = new TimestampValidator(null);
        var componentRenderer = new ComponentRendererImpl(Renderers, RENDERING_BOUNDARIES, TimestampValidator);

        var graphicsPreloader = new FakeGraphicsPreloader();

        MouseEventCapturingSpatialIndex = new MouseEventCapturingSpatialIndexImpl();

        var mouseEventHandler = new MouseEventHandlerImpl(MouseEventCapturingSpatialIndex);

        MockShiftAggregator = mock(ColorShiftStackAggregator.class);
        when(MockShiftAggregator.aggregate(any(), anyLong())).thenReturn(netShifts(0, 0, 0, 0, 0));

        var mouseListener = new MouseListener(mouseEventHandler);

        MockTopLevelComponent = mock(Component.class);
        when(MockTopLevelComponent.getRenderingBoundariesProvider()).thenReturn(
                WHOLE_SCREEN_PROVIDER);
        MockFirstChildComponent = mock(Component.class);
        when(MockFirstChildComponent.component()).thenReturn(MockTopLevelComponent);
        when(MockFirstChildComponent.getRenderingBoundariesProvider()).thenReturn(
                WHOLE_SCREEN_PROVIDER);
        when(MockTopLevelComponent.contents()).thenReturn(setOf(MockFirstChildComponent));

        var frameExecutor = new FrameExecutorImpl(componentRenderer, 100);
        frameExecutor.setTopLevelComponent(MockTopLevelComponent);

        var renderersWithMeshAndShader =
                generateRenderablesAndRenderersWithMeshAndShader.apply(windowResolutionManager);

        var graphicsCoreLoop =
                new GraphicsCoreLoopImpl("My title bar", FrameTimer, 0, windowResolutionManager,
                        GLOBAL_CLOCK, frameExecutor, new ShaderFactoryImpl(),
                        renderersWithMeshAndShader, SHADER_FILENAME_PREFIX, MeshImpl::new,
                        renderersWithMeshAndShader, MESH_DATA, MESH_DATA, graphicsPreloader,
                        MouseCursor, mouseListener);

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

    protected static <T> StaticProvider<T> staticNullProvider() {
        return new StaticProviderImpl<>(java.util.UUID.randomUUID(), null, null);
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
