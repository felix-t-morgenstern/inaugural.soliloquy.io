package inaugural.soliloquy.graphics.test.display;

import inaugural.soliloquy.common.test.fakes.FakeCoordinateFactory;
import inaugural.soliloquy.common.test.fakes.FakePairFactory;
import inaugural.soliloquy.graphics.api.WindowResolution;
import inaugural.soliloquy.graphics.bootstrap.GraphicsCoreLoopImpl;
import inaugural.soliloquy.graphics.rendering.WindowResolutionManagerImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.rendering.Mesh;
import soliloquy.specs.graphics.rendering.WindowDisplayMode;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.graphics.rendering.renderers.Renderer;
import soliloquy.specs.graphics.rendering.renderers.StackRenderer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

/**
 * Test acceptance criteria:
 *
 * Broadly, this test goes through a transition between all types of display modes.
 *
 * 1. This test will start by displaying a window in windowed fullscreen mode for 4000ms.
 * 2. The window will switch to windowed mode, with a resolution of 800x600px, for 4000ms.
 * 3. The window will switch to fullscreen mode, changing the screen to have a resolution of
 *    1920x1080px, for 4000ms.
 * 4. The window will switch to fullscreen mode, changing the screen to have a resolution of
 *    3840x2160px, for 4000ms.
 * 5. The window will switch to windowed mode, with a resolution of 800x600px, for 4000ms.
 * 6. The window will switch to fullscreen mode, changing the screen to have a resolution of
 *    3840x2160px, for 4000ms.
 * 7. The window will switch to windowed fullscreen for 4000ms.
 * 8. The window will switch to fullscreen mode, changing the screen to have a resolution of
 *    3840x2160px, for 4000ms.
 * 9. The window will switch to windowed mode, with a resolution of 800x600px, for 4000ms.
 * 10. The window will switch to windowed fullscreen for 4000ms.
 * 11. The window will close.
 *
 */
class WindowManagerImplDisplayChangeTest {
    private final static FakeCoordinateFactory COORDINATE_FACTORY = new FakeCoordinateFactory();
    private final static float[] MESH_DATA =
            new float[] {0f, 1f, 1f, 1f, 1f, 0f, 1f, 0f, 0f, 0f, 0f, 1f};

    public static void main(String[] args) {
        WindowResolutionManagerImpl windowResolutionManager = new WindowResolutionManagerImpl(
                WindowDisplayMode.WINDOWED_FULLSCREEN, WindowResolution.RES_WINDOWED_FULLSCREEN,
                COORDINATE_FACTORY);

        FakeFrameTimer frameTimer = new FakeFrameTimer();
        frameTimer.ShouldExecuteNextFrame = true;
        Function<float[], Function<float[], Mesh>> meshFactory = f1 -> f2 -> null;
        @SuppressWarnings("rawtypes") Collection<Renderer> renderersWithMesh = new ArrayList<>();

        StackRenderer stackRenderer = new FakeStackRenderer();
        @SuppressWarnings("rawtypes") Collection<Renderer> renderersWithShader = new ArrayList<>();
        GraphicsCoreLoop graphicsCoreLoop = new GraphicsCoreLoopImpl("My title bar",
                new FakeGLFWMouseButtonCallback(), frameTimer, 20, windowResolutionManager,
                stackRenderer, new FakeShaderFactory(), renderersWithShader, "_", meshFactory,
                renderersWithMesh, MESH_DATA, MESH_DATA, new FakeGraphicsPreloader());

        graphicsCoreLoop.startup(() ->
                closeAfterSomeTime(graphicsCoreLoop, windowResolutionManager));
    }

    private static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop,
                                           WindowResolutionManager windowResolutionManager) {
        int ms = 4000;

        System.out.println("Starting at windowed fullscreen...");

        CheckedExceptionWrapper.sleep(ms);

        System.out.println("Setting to windowed, small res...");

        windowResolutionManager.setWindowDisplayMode(WindowDisplayMode.WINDOWED);
        windowResolutionManager.setDimensions(800, 600);

        CheckedExceptionWrapper.sleep(ms);

        System.out.println("Setting to fullscreen, med res...");

        windowResolutionManager.setWindowDisplayMode(WindowDisplayMode.FULLSCREEN);
        windowResolutionManager.setDimensions(1920, 1080);

        CheckedExceptionWrapper.sleep(ms);

        System.out.println("Setting to fullscreen, large res...");

        windowResolutionManager.setWindowDisplayMode(WindowDisplayMode.FULLSCREEN);
        windowResolutionManager.setDimensions(3840, 2160);

        CheckedExceptionWrapper.sleep(ms);

        System.out.println("Setting to windowed, small res...");

        windowResolutionManager.setWindowDisplayMode(WindowDisplayMode.WINDOWED);
        windowResolutionManager.setDimensions(800, 600);

        CheckedExceptionWrapper.sleep(ms);

        System.out.println("Setting to fullscreen, large res...");

        windowResolutionManager.setWindowDisplayMode(WindowDisplayMode.FULLSCREEN);
        windowResolutionManager.setDimensions(3840, 2160);

        CheckedExceptionWrapper.sleep(ms);

        System.out.println("Setting to windowed fullscreen...");

        windowResolutionManager.setWindowDisplayMode(WindowDisplayMode.WINDOWED_FULLSCREEN);

        CheckedExceptionWrapper.sleep(ms);

        System.out.println("Setting to fullscreen, large res...");

        windowResolutionManager.setWindowDisplayMode(WindowDisplayMode.FULLSCREEN);
        windowResolutionManager.setDimensions(3840, 2160);

        CheckedExceptionWrapper.sleep(ms);

        System.out.println("Setting to windowed, small res...");

        windowResolutionManager.setWindowDisplayMode(WindowDisplayMode.WINDOWED);
        windowResolutionManager.setDimensions(800, 600);

        CheckedExceptionWrapper.sleep(ms);

        System.out.println("Setting to windowed fullscreen...");

        windowResolutionManager.setWindowDisplayMode(WindowDisplayMode.WINDOWED_FULLSCREEN);

        CheckedExceptionWrapper.sleep(ms);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
