package inaugural.soliloquy.graphics.test.display.rendering.renderers.globalloopinganimationrenderer;

import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 1920x1080 pixels in the middle of the screen for 3000ms
 * with a titlebar reading "My title bar". The window will contain a looping animation of a
 * torch, centered in the screen, which will change its frames every 250ms.
 * 2. The animation will persist for 1125ms.
 * 3. The animation will pause for 1000ms.
 * 4. The animation will resume for 1125ms. (A rainbow version of the torch is used to verify
 * whether animation resumes where it left off.)
 * 5. The window will then close.
 */
public class GlobalLoopingAnimationRendererWithPausingTest
        extends GlobalLoopingAnimationRendererTest {
    public static void main(String[] args) {
        runTest(
                windowResolutionManager -> GlobalLoopingAnimationRendererTest.
                        generateRenderablesAndRenderersWithMeshAndShader(
                                windowResolutionManager,
                                null
                        ),
                GlobalLoopingAnimationRendererTest::graphicsPreloaderLoadAction,
                graphicsCoreLoop -> closeAfterSomeTime(graphicsCoreLoop, TEST_DURATION_MS)
        );
    }

    public static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop, int ms) {
        CheckedExceptionWrapper.sleep(ms / 3);

        var pauseTimestamp = GLOBAL_CLOCK.globalTimestamp();
        GlobalLoopingAnimation.reportPause(pauseTimestamp);
        System.out.println("Paused at " + pauseTimestamp);
        System.out.println("Period modulo offset = " + GlobalLoopingAnimation.periodModuloOffset());

        CheckedExceptionWrapper.sleep(1000);

        var unpauseTimestamp = GLOBAL_CLOCK.globalTimestamp();
        GlobalLoopingAnimation.reportUnpause(unpauseTimestamp);
        System.out.println("Unpaused at " + unpauseTimestamp);
        System.out.println("Period modulo offset = " + GlobalLoopingAnimation.periodModuloOffset());

        CheckedExceptionWrapper.sleep((ms * 2L) / 3);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
