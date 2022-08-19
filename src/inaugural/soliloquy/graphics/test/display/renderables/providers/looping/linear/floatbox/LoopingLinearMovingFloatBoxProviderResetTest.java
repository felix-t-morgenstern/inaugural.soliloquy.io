package inaugural.soliloquy.graphics.test.display.renderables.providers.looping.linear.floatbox;

import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.renderables.providers.ResettableProvider;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.awt.*;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 1920x1080 pixels in the middle of the screen for 8000ms
 * with a titlebar reading "My title bar". The window will contain a picture of a shield,
 * in the top-left corner of the window, taking up one eighth of the width and three-sixteenths
 * of the height of the window.
 * 2. Over 3000ms, the shield will move clockwise from one corner of the screen to another.
 * 2. After the first 3000ms, the shield will reset to the upper-left corner of the screen. The
 * shield will continue rotating as it had before for another 8000ms.
 * 3. The window will then close.
 */
class LoopingLinearMovingFloatBoxProviderResetTest
        extends LoopingLinearMovingFloatBoxProviderTest {
    public static void main(String[] args) {
        runTest(windowResolutionManager ->
                        generateRenderablesAndRenderersWithMeshAndShader(0f,
                                Color.WHITE, null, windowResolutionManager),
                LoopingLinearMovingFloatBoxProviderTest::stackRendererAction,
                LoopingLinearMovingFloatBoxProviderTest::graphicsPreloaderLoadAction,
                LoopingLinearMovingFloatBoxProviderResetTest::closeAfterSomeTime);
    }

    public static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        ((ResettableProvider<FloatBox>) RENDERING_DIMENSIONS_PROVIDER)
                .reset(GLOBAL_CLOCK.globalTimestamp());

        CheckedExceptionWrapper.sleep(3000);

        ((ResettableProvider<FloatBox>) RENDERING_DIMENSIONS_PROVIDER)
                .reset(GLOBAL_CLOCK.globalTimestamp());

        CheckedExceptionWrapper.sleep(8000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
