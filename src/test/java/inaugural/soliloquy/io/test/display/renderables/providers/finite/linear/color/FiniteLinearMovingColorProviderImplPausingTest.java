package inaugural.soliloquy.io.test.display.renderables.providers.finite.linear.color;

import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.io.graphics.bootstrap.GraphicsCoreLoop;

import static inaugural.soliloquy.io.api.Constants.INTACT_COLOR;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 1920x1080 pixels in the middle of the screen for 6000ms
 * with a titlebar reading "My title bar". The window will contain a picture of a shield,
 * centered in the window, taking up half of the width and three-fourths of the height of the
 * window.
 * 2. Over the first 1000ms, the border will transition from a dark purple to a light blue.
 * 3. The border will begin transitioning from light blue to a dull gray. After half of this
 * transition, i.e. 1000ms, the transition will pause for 2000ms.
 * 4. The transition will complete over the following 1000ms.
 * 5. After a final 1000ms, the window will then close.
 */
class FiniteLinearMovingColorProviderImplPausingTest
        extends FiniteLinearMovingColorProviderImplTest {
    public static void main(String[] args) {
        runTest(windowResolutionManager ->
                        generateRenderablesAndRenderersWithMeshAndShader(BORDER_THICKNESS,
                                        INTACT_COLOR, null, windowResolutionManager),
                FiniteLinearMovingColorProviderImplSimpleTest::graphicsPreloaderLoadAction,
                FiniteLinearMovingColorProviderImplPausingTest::closeAfterSomeTime);
    }

    public static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        CheckedExceptionWrapper.sleep(2000);

        SpriteRenderable.getBorderColorProvider()
                .reportPause(GLOBAL_CLOCK.globalTimestamp());

        CheckedExceptionWrapper.sleep(2000);

        SpriteRenderable.getBorderColorProvider()
                .reportUnpause(GLOBAL_CLOCK.globalTimestamp());

        CheckedExceptionWrapper.sleep(2000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
