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
 * 2. After 1000ms, i.e. four frames, the animation will reset to the initial frame.
 * 3. The animation will continue running for 2250ms.
 * 4. The window will then close.
 */
class GlobalLoopingAnimationRendererResetTest extends GlobalLoopingAnimationRendererTest {
    public static void main(String[] args) {
        runTest(
                windowResolutionManager -> GlobalLoopingAnimationRendererTest.
                        generateRenderablesAndRenderersWithMeshAndShader(
                                windowResolutionManager,
                                null
                        ),
                timestamp -> GlobalLoopingAnimationRenderer
                        .render(GlobalLoopingAnimationRenderable, timestamp),
                GlobalLoopingAnimationRendererTest::graphicsPreloaderLoadAction,
                GlobalLoopingAnimationRendererResetTest::runThenClose
        );
    }

    private static void runThenClose(GraphicsCoreLoop graphicsCoreLoop) {
        GlobalLoopingAnimation.reset(GLOBAL_CLOCK.globalTimestamp());

        CheckedExceptionWrapper.sleep(1000);

        GlobalLoopingAnimation.reset(GLOBAL_CLOCK.globalTimestamp());

        CheckedExceptionWrapper.sleep(2250);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
