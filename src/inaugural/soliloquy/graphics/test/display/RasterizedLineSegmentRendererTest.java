package inaugural.soliloquy.graphics.test.display;

import inaugural.soliloquy.graphics.api.WindowResolution;
import inaugural.soliloquy.graphics.bootstrap.GraphicsCoreLoopImpl;
import inaugural.soliloquy.graphics.rendering.RasterizedLineSegmentRenderer;
import inaugural.soliloquy.graphics.rendering.WindowResolutionManagerImpl;
import inaugural.soliloquy.graphics.test.fakes.*;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.renderables.RasterizedLineSegmentRenderable;
import soliloquy.specs.graphics.rendering.Mesh;
import soliloquy.specs.graphics.rendering.Renderer;
import soliloquy.specs.graphics.rendering.WindowDisplayMode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.function.Function;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 800x600 pixels for 2000ms with a titlebar reading "New
 *    Window"
 * 2. During the 2000ms, random rasterized line segments will radiate from the center of the
 *    screen in random directions. These segments will have an evenly-spaced dash pattern, though
 *    the density of these dashes will be randomized. The colors of the segments will also be
 *    randomized. (The timing of the rendering of these segments is not regulated in any way; it is
 *    expected that new, randomized segments will be generated very rapidly.)
 * 3. The window will then close
 *
 */
class RasterizedLineSegmentRendererTest {
    private final static float[] MESH_DATA =
            new float[] {0f, 1f, 1f, 1f, 1f, 0f, 1f, 0f, 0f, 0f, 0f, 1f};

    public static void main(String[] args) {
        WindowResolutionManagerImpl windowManager =
                new WindowResolutionManagerImpl(WindowDisplayMode.WINDOWED,
                        WindowResolution.RES_800x600);

        FakeFrameTimer frameTimer = new FakeFrameTimer();
        frameTimer.ShouldExecuteNextFrame = true;

        FakeStackRenderer stackRenderer = new FakeStackRenderer();
        Collection<Renderer> renderersWithShader = new ArrayList<>();
        Function<float[], Function<float[],Mesh>> meshFactory = f1 -> f2 -> new FakeMesh();
        Collection<Renderer> renderersWithMesh = new ArrayList<>();

        Renderer<RasterizedLineSegmentRenderable> rasterizedLineSegmentRenderer =
                new RasterizedLineSegmentRenderer();

        stackRenderer.RenderAction =
                () -> rasterizedLineSegmentRenderer.render(randomRasterizedLineSegmentRenderable());

        GraphicsCoreLoop graphicsCoreLoop = new GraphicsCoreLoopImpl("My title bar",
                new FakeGLFWMouseButtonCallback(), frameTimer, 20, windowManager, stackRenderer,
                new FakeShaderFactory(), renderersWithShader, "_", meshFactory, renderersWithMesh,
                MESH_DATA, MESH_DATA, new FakeGraphicsPreloader());

        graphicsCoreLoop.startup(() -> closeAfterSomeTime(graphicsCoreLoop));
    }

    private static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        CheckedExceptionWrapper.sleep(2000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }

    private static RasterizedLineSegmentRenderable randomRasterizedLineSegmentRenderable() {
        return new FakeRasterizedLineSegmentRenderable(6f, (short) 0xAAAA,
                1 + new Random().nextInt(8),
                new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 1f,
                new FakeFloatBox(0f, 0f,
                        (2 * new Random().nextFloat()) - 1f,
                        (2 * new Random().nextFloat()) - 1f),
                1);
    }
}
