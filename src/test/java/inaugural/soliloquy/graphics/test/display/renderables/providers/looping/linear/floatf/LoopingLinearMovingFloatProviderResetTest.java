package inaugural.soliloquy.graphics.test.display.renderables.providers.looping.linear.floatf;

import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.renderables.providers.ResettableProvider;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 1920x1080 pixels in the middle of the screen for 3000ms
 * with a titlebar reading "My title bar". The window will contain a picture of a shield,
 * centered in the window, taking up half of the width and three-fourths of the height of the
 * window.
 * 2. The border around the shield will pulse for 3000ms. Then, the animation will reset, and it
 * will continue for a further 3000ms.
 * 3. The window will then close.
 */
class LoopingLinearMovingFloatProviderResetTest extends LoopingLinearMovingFloatProviderTest {
    public static void main(String[] args) {
        runTest(windowResolutionManager ->
                        generateRenderablesAndRenderersWithMeshAndShader(BORDER_THICKNESS,
                                BORDER_COLOR, null, windowResolutionManager),
                LoopingLinearMovingFloatProviderTest::graphicsPreloaderLoadAction,
                LoopingLinearMovingFloatProviderResetTest::closeAfterSomeTime);
    }

    public static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        ((ResettableProvider<Float>) BORDER_THICKNESS_PROVIDER)
                .reset(GLOBAL_CLOCK.globalTimestamp());

        CheckedExceptionWrapper.sleep(3000);

        ((ResettableProvider<Float>) BORDER_THICKNESS_PROVIDER)
                .reset(GLOBAL_CLOCK.globalTimestamp());

        CheckedExceptionWrapper.sleep(3000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
