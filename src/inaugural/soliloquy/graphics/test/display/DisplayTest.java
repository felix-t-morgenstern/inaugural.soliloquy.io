package inaugural.soliloquy.graphics.test.display;

import inaugural.soliloquy.graphics.api.WindowResolution;
import inaugural.soliloquy.graphics.bootstrap.GraphicsCoreLoopImpl;
import inaugural.soliloquy.graphics.rendering.MeshImpl;
import inaugural.soliloquy.graphics.rendering.WindowResolutionManagerImpl;
import inaugural.soliloquy.graphics.rendering.factories.ShaderFactoryImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.rendering.Mesh;
import soliloquy.specs.graphics.rendering.WindowDisplayMode;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class DisplayTest {
    protected final static float[] MESH_DATA =
            new float[] {0f, 1f, 1f, 1f, 1f, 0f, 1f, 0f, 0f, 0f, 0f, 1f};
    protected final static FakeRenderingBoundaries RENDERING_BOUNDARIES =
            new FakeRenderingBoundaries();
    protected final static FakeFloatBoxFactory FLOAT_BOX_FACTORY = new FakeFloatBoxFactory();
    protected final static String SHADER_FILENAME_PREFIX = "./res/shaders/defaultShader";
    protected final static EntityUuid UUID = new FakeEntityUuid();
    protected final static WindowResolution RESOLUTION = WindowResolution.RES_1920x1080;
    protected final static FakeGlobalClock GLOBAL_CLOCK = new FakeGlobalClock();

    protected static FakeFrameTimer FrameTimer;

    /** @noinspection rawtypes*/
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

        RENDERING_BOUNDARIES.CurrentBoundaries = new FakeFloatBox(0.0f, 0.0f, 1.0f, 1.0f);

        FakeGraphicsPreloader graphicsPreloader = new FakeGraphicsPreloader();

        List<Renderer> renderersWithMeshAndShader =
                generateRenderablesAndRenderersWithMeshAndShader.apply(windowResolutionManager);

        stackRenderer.RenderAction = stackRendererAction;

        FakeFrameExecutor frameExecutor = new FakeFrameExecutor(stackRenderer, GLOBAL_CLOCK);

        GraphicsCoreLoop graphicsCoreLoop = new GraphicsCoreLoopImpl("My title bar",
                new FakeGLFWMouseButtonCallback(), FrameTimer, 20, windowResolutionManager,
                frameExecutor, new ShaderFactoryImpl(), renderersWithMeshAndShader,
                SHADER_FILENAME_PREFIX, meshFactory, renderersWithMeshAndShader, MESH_DATA,
                MESH_DATA, graphicsPreloader);

        graphicsPreloader.LoadAction = graphicsPreloaderLoadAction;

        graphicsCoreLoop.startup(() -> closeAfterSomeTime.accept(graphicsCoreLoop));
    }

    public static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        CheckedExceptionWrapper.sleep(3000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
