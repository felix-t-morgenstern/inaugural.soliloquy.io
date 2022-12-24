package inaugural.soliloquy.graphics.test.display;

import inaugural.soliloquy.graphics.api.WindowResolution;
import inaugural.soliloquy.graphics.bootstrap.GraphicsCoreLoopImpl;
import inaugural.soliloquy.graphics.io.MouseEventCapturingSpatialIndexImpl;
import inaugural.soliloquy.graphics.io.MouseEventHandlerImpl;
import inaugural.soliloquy.graphics.io.MouseListenerImpl;
import inaugural.soliloquy.graphics.rendering.FrameExecutorImpl;
import inaugural.soliloquy.graphics.rendering.MeshImpl;
import inaugural.soliloquy.graphics.rendering.WindowResolutionManagerImpl;
import inaugural.soliloquy.graphics.rendering.factories.ShaderFactoryImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.io.MouseCursor;
import soliloquy.specs.graphics.io.MouseEventCapturingSpatialIndex;
import soliloquy.specs.graphics.io.MouseEventHandler;
import soliloquy.specs.graphics.io.MouseListener;
import soliloquy.specs.graphics.rendering.*;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

import static inaugural.soliloquy.graphics.api.Constants.WHOLE_SCREEN;
import static inaugural.soliloquy.tools.generic.Archetypes.generateSimpleArchetype;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class DisplayTest {
    protected final static float[] MESH_DATA =
            new float[]{0f, 1f, 1f, 1f, 1f, 0f, 1f, 0f, 0f, 0f, 0f, 1f};
    protected final static FakeRenderingBoundaries RENDERING_BOUNDARIES =
            new FakeRenderingBoundaries();
    protected final static FakeFloatBoxFactory FLOAT_BOX_FACTORY = new FakeFloatBoxFactory();
    protected final static String SHADER_FILENAME_PREFIX =
            "./src/main/resources/shaders/defaultShader";
    protected final static UUID UUID = java.util.UUID.randomUUID();
    protected final static WindowResolution RESOLUTION = WindowResolution.RES_1920x1080;
    protected final static FakeGlobalClock GLOBAL_CLOCK = new FakeGlobalClock();
    protected final static FakeRenderableStack RENDERING_STACK = new FakeRenderableStack();

    protected static FakeFrameTimer FrameTimer;
    protected static MouseCursor MouseCursor = new FakeMouseCursor();
    protected static MouseEventCapturingSpatialIndex MOUSE_EVENT_CAPTURING_SPATIAL_INDEX;

    /** @noinspection rawtypes */
    protected static void runTest(Function<WindowResolutionManager, List<Renderer>>
                                          generateRenderablesAndRenderersWithMeshAndShader,
                                  Consumer<Long> stackRendererAction,
                                  Runnable graphicsPreloaderLoadAction,
                                  Consumer<GraphicsCoreLoop> closeAfterSomeTime) {
        WindowResolutionManagerImpl windowResolutionManager = new WindowResolutionManagerImpl(
                WindowDisplayMode.WINDOWED, RESOLUTION);

        FrameTimer = new FakeFrameTimer();

        Function<float[], Function<float[], Mesh>> meshFactory = f1 -> f2 -> new MeshImpl(f1, f2);

        FakeStackRenderer stackRenderer = new FakeStackRenderer();

        RENDERING_BOUNDARIES.CurrentBoundaries = WHOLE_SCREEN;

        FakeGraphicsPreloader graphicsPreloader = new FakeGraphicsPreloader();

        MOUSE_EVENT_CAPTURING_SPATIAL_INDEX = new MouseEventCapturingSpatialIndexImpl();

        MouseEventHandler mouseEventHandler =
                new MouseEventHandlerImpl(MOUSE_EVENT_CAPTURING_SPATIAL_INDEX);

        MouseListener mouseListener = new MouseListenerImpl(mouseEventHandler);

        stackRenderer.RenderAction = stackRendererAction;

        FrameExecutor frameExecutor =
                new FrameExecutorImpl(generateSimpleArchetype(RenderableStack.class), stackRenderer,
                        100);

        List<Renderer> renderersWithMeshAndShader =
                generateRenderablesAndRenderersWithMeshAndShader.apply(windowResolutionManager);

        GraphicsCoreLoop graphicsCoreLoop =
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
}
