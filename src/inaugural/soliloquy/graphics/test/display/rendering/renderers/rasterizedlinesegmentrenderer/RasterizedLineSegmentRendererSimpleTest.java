package inaugural.soliloquy.graphics.test.display.rendering.renderers.rasterizedlinesegmentrenderer;

import inaugural.soliloquy.common.test.fakes.FakeCoordinateFactory;
import inaugural.soliloquy.graphics.api.WindowResolution;
import inaugural.soliloquy.graphics.bootstrap.GraphicsCoreLoopImpl;
import inaugural.soliloquy.graphics.renderables.providers.StaticProviderImpl;
import inaugural.soliloquy.graphics.rendering.WindowResolutionManagerImpl;
import inaugural.soliloquy.graphics.rendering.renderers.RasterizedLineSegmentRenderer;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.renderables.RasterizedLineSegmentRenderable;
import soliloquy.specs.graphics.rendering.Mesh;
import soliloquy.specs.graphics.rendering.WindowDisplayMode;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 800x600 pixels for 2000ms with a titlebar reading "New
 *    Window"
 * 2. During the 2000ms, a teal rasterized line segment will be rendered, starting from
 *    (0.25,0.25), and ending at (0.75, 0.5).
 * 3. The window will then close
 *
 */
class RasterizedLineSegmentRendererSimpleTest {
    private final static FakeCoordinateFactory COORDINATE_FACTORY = new FakeCoordinateFactory();
    private final static float[] MESH_DATA =
            new float[] {0f, 1f, 1f, 1f, 1f, 0f, 1f, 0f, 0f, 0f, 0f, 1f};

    public static void main(String[] args) {
        WindowResolutionManagerImpl windowManager = new WindowResolutionManagerImpl(
                WindowDisplayMode.WINDOWED, WindowResolution.RES_800x600, COORDINATE_FACTORY);

        FakeFrameTimer frameTimer = new FakeFrameTimer();
        frameTimer.ShouldExecuteNextFrame = true;

        FakeStackRenderer stackRenderer = new FakeStackRenderer();
        @SuppressWarnings("rawtypes") Collection<Renderer> renderersWithShader = new ArrayList<>();
        Function<float[], Function<float[],Mesh>> meshFactory = f1 -> f2 -> new FakeMesh();
        @SuppressWarnings("rawtypes") Collection<Renderer> renderersWithMesh = new ArrayList<>();

        Renderer<RasterizedLineSegmentRenderable> rasterizedLineSegmentRenderer =
                new RasterizedLineSegmentRenderer();

        stackRenderer.RenderAction = timestamp ->
                rasterizedLineSegmentRenderer.render(makeRasterizedLineSegmentRenderable(),
                        timestamp);

        FakeFrameExecutor frameExecutor = new FakeFrameExecutor(stackRenderer, null);

        GraphicsCoreLoop graphicsCoreLoop = new GraphicsCoreLoopImpl("My title bar",
                new FakeGLFWMouseButtonCallback(), frameTimer, 20, windowManager, frameExecutor,
                new FakeShaderFactory(), renderersWithShader, "_", meshFactory, renderersWithMesh,
                MESH_DATA, MESH_DATA, new FakeGraphicsPreloader());

        graphicsCoreLoop.startup(() -> closeAfterSomeTime(graphicsCoreLoop));
    }

    private static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        CheckedExceptionWrapper.sleep(2000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }

    private static RasterizedLineSegmentRenderable makeRasterizedLineSegmentRenderable() {
        return new FakeRasterizedLineSegmentRenderable(
                new StaticProviderImpl<>(new FakeEntityUuid(), 6f), (short) 0xAAAA,
                (short) 16,
                new StaticProviderImpl<>(new FakeEntityUuid(), new Color(18, 201, 159)),
                // NB: The parameters are in this order to ensure that
                //     RasterizedLineSegmentRenderable does not care about order
                new StaticProviderImpl<>(new FakeEntityUuid(),
                        new FakeFloatBox(0.75f, 0.5f, 0.25f, 0.25f)),
                1, new FakeEntityUuid());
    }
}
