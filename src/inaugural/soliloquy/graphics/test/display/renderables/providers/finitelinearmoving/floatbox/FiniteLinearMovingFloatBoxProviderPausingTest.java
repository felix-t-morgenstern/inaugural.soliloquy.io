package inaugural.soliloquy.graphics.test.display.renderables.providers.finitelinearmoving.floatbox;

import inaugural.soliloquy.graphics.test.display.DisplayTest;
import inaugural.soliloquy.graphics.test.display.renderables.providers.finitelinearmoving.FiniteLinearMovingProviderTest;
import inaugural.soliloquy.graphics.test.display.rendering.renderers.spriterenderer.SpriteRendererTest;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;

import static inaugural.soliloquy.graphics.api.Constants.INTACT_COLOR;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 1920x1080 pixels in the middle of the screen for 6000ms
 * with a titlebar reading "My title bar". The window will contain a picture of a shield,
 * located in the upper-left corner, at 50% size.
 * 2. The shield will move to the center of the window, while growing towards 100%, for the first
 * 2000ms. It will pause for 2000ms. It will then resume over the next 1000ms. The window will
 * remain open for an additional 1000ms.
 * 3. The window will then close.
 */
class FiniteLinearMovingFloatBoxProviderPausingTest
        extends FiniteLinearMovingFloatBoxProviderTest {
    public static void main(String[] args) {
        DisplayTest.runTest(windowResolutionManager ->
                        FiniteLinearMovingProviderTest.generateRenderablesAndRenderersWithMeshAndShader(0f,
                                INTACT_COLOR, null, windowResolutionManager),
                FiniteLinearMovingProviderTest::stackRendererAction,
                FiniteLinearMovingFloatBoxProviderTest::graphicsPreloaderLoadAction,
                FiniteLinearMovingFloatBoxProviderPausingTest::closeAfterSomeTime);
    }

    public static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        CheckedExceptionWrapper.sleep(2000);

        long timestamp = GLOBAL_CLOCK.globalTimestamp();
        SpriteRendererTest.SpriteRenderable.RenderingDimensionsProvider.reportPause(timestamp);

        CheckedExceptionWrapper.sleep(2000);

        timestamp = GLOBAL_CLOCK.globalTimestamp();
        SpriteRendererTest.SpriteRenderable.RenderingDimensionsProvider.reportUnpause(timestamp);

        CheckedExceptionWrapper.sleep(2000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
