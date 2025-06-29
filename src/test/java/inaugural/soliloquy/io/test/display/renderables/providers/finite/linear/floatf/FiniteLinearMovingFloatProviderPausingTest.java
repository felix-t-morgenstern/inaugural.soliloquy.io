package inaugural.soliloquy.io.test.display.renderables.providers.finite.linear.floatf;

import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.io.graphics.bootstrap.GraphicsCoreLoop;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 1920x1080 pixels in the middle of the screen for 6000ms
 * with a titlebar reading "My title bar". The window will contain a picture of a shield,
 * centered in the window, taking up half of the width and three-fourths of the height of the
 * window.
 * 2. At first, there should be no (or next-to-no border); for the first 2000ms, it will expand.
 * 3. Its expansion will pause for 2000ms.
 * 4. Its expansion will continue for another 2000ms.
 * 5. The window will then close.
 */
class FiniteLinearMovingFloatProviderPausingTest extends FiniteLinearMovingFloatProviderTest {
    public static void main(String[] args) {
        runTest(windowResolutionManager ->
                        generateRenderablesAndRenderersWithMeshAndShader(BORDER_THICKNESS,
                                        BORDER_COLOR, null, windowResolutionManager),
                FiniteLinearMovingFloatProviderTest::graphicsPreloaderLoadAction,
                FiniteLinearMovingFloatProviderPausingTest::closeAfterSomeTime);
    }

    public static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        CheckedExceptionWrapper.sleep(2000);

        long timestamp = GLOBAL_CLOCK.globalTimestamp();
        SpriteRenderable.getBorderThicknessProvider().reportPause(timestamp);

        CheckedExceptionWrapper.sleep(2000);

        timestamp = GLOBAL_CLOCK.globalTimestamp();
        SpriteRenderable.getBorderThicknessProvider().reportUnpause(timestamp);

        CheckedExceptionWrapper.sleep(2000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
