package inaugural.soliloquy.graphics.test.display.rendering.renderers.finiteanimationrenderer;

import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

/**
 * Test acceptance criteria:
 *
 * 1. An 800x600 window will open. An explosion will be displayed in the center of the window.
 * 2. After 500ms, the explosion will pause, for 500ms.
 * 3. After the pause, the explosion will continue.
 * 4. The window will close.
 */
class FiniteAnimationRendererPausingTest extends FiniteAnimationRendererTest {
    public static void main(String[] args) {
        runTest(
                windowResolutionManager -> generateRenderablesAndRenderersWithMeshAndShader(
                                windowResolutionManager,
                                null),
                FiniteAnimationRendererTest::graphicsPreloaderLoadAction,
                FiniteAnimationRendererPausingTest::closeAfterSomeTime
        );
    }

    public static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        CheckedExceptionWrapper.sleep(500);

        FiniteAnimationRenderable.reportPause(GLOBAL_CLOCK.globalTimestamp());

        CheckedExceptionWrapper.sleep(500);

        FiniteAnimationRenderable.reportUnpause(GLOBAL_CLOCK.globalTimestamp());

        CheckedExceptionWrapper.sleep(1500);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
