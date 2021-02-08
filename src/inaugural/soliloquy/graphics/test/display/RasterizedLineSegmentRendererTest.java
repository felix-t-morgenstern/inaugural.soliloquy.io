package inaugural.soliloquy.graphics.test.display;

import inaugural.soliloquy.graphics.api.WindowResolution;
import inaugural.soliloquy.graphics.bootstrap.GraphicsCoreLoopImpl;
import inaugural.soliloquy.graphics.rendering.RasterizedLineSegmentRenderer;
import inaugural.soliloquy.graphics.rendering.WindowResolutionManagerImpl;
import inaugural.soliloquy.graphics.test.fakes.FakeFrameTimer;
import inaugural.soliloquy.graphics.test.fakes.FakeGLFWMouseButtonCallback;
import inaugural.soliloquy.graphics.test.fakes.FakeRasterizedLineSegmentRenderable;
import inaugural.soliloquy.graphics.test.fakes.FakeStackRenderer;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.renderables.RasterizedLineSegmentRenderable;
import soliloquy.specs.graphics.rendering.RendererType;
import soliloquy.specs.graphics.rendering.WindowDisplayMode;

import java.util.Random;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

class RasterizedLineSegmentRendererTest {
    public static void main(String[] args) {
        WindowResolutionManagerImpl windowManager =
                new WindowResolutionManagerImpl(WindowDisplayMode.WINDOWED,
                        WindowResolution.RES_800x600);

        FakeFrameTimer frameTimer = new FakeFrameTimer();
        frameTimer.ShouldExecuteNextFrame = true;

        FakeStackRenderer stackRenderer = new FakeStackRenderer();

        RasterizedLineSegmentRenderable rasterizedLineSegmentRenderable =
                new FakeRasterizedLineSegmentRenderable(6f, (short) 0xAAAA, 2,
                        1f, 0.1f, 0f, 0.5f,
                        -0.5f, 0.75f, 1f, -.25f, 1);

        RendererType<RasterizedLineSegmentRenderable> rasterizedLineSegmentRenderer =
                new RasterizedLineSegmentRenderer();

        stackRenderer.RenderAction =
                () -> rasterizedLineSegmentRenderer.render(randomRasterizedLineSegmentRenderable());

        GraphicsCoreLoop graphicsCoreLoop = new GraphicsCoreLoopImpl("My title bar",
                new FakeGLFWMouseButtonCallback(), frameTimer, windowManager, stackRenderer);

        graphicsCoreLoop.startup(() -> closeAfterSomeTime(graphicsCoreLoop));
    }

    private static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        CheckedExceptionWrapper.Sleep(2000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }

    private static RasterizedLineSegmentRenderable randomRasterizedLineSegmentRenderable() {
        Random random = new Random();
        return new FakeRasterizedLineSegmentRenderable(6f, (short) 0xAAAA,
                1 + new Random().nextInt(8),
                new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 1f,
                0f, 0f,
                (2 * new Random().nextFloat()) - 1f,
                (2 * new Random().nextFloat()) - 1f,
                1);
    }
}
