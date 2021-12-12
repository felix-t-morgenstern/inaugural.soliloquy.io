package inaugural.soliloquy.graphics.test.display.renderables.providers;

import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;

import java.awt.*;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 1024x768 pixels with a titlebar reading "New Window"
 * 2. During the 3000ms, a window taking up half of the screen, centered in the middle, will have
 *    background tile of a stone floor, moving to the right, repeating every 2000ms. Each tile will
 *    take up 5% of the screen width.
 * 3. This animation will run for 3000ms. Then, it will reset to its initial position. Then, it
 *    will continue for another 5000ms.
 * 4. The window will then close
 **/
public class RectangleAnimatedBackgroundTextureIdProviderResetTest
        extends RectangleAnimatedBackgroundTextureIdProviderTest {

    public static void main(String[] args) {
        runTest(
                windowResolutionManager -> generateRenderablesAndRenderersWithMeshAndShader(
                        0f, Color.BLACK, null, windowResolutionManager
                ),
                RectangleAnimatedBackgroundTextureIdProviderTest::stackRendererAction,
                RectangleAnimatedBackgroundTextureIdProviderTest::graphicsPreloaderLoadAction,
                RectangleAnimatedBackgroundTextureIdProviderResetTest::closeAfterSomeTime
        );
    }

    public static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        RectangleAnimatedBackgroundTextureIdProvider.reset(GLOBAL_CLOCK.globalTimestamp());

        CheckedExceptionWrapper.sleep(3000);

        RectangleAnimatedBackgroundTextureIdProvider.reset(GLOBAL_CLOCK.globalTimestamp());

        CheckedExceptionWrapper.sleep(5000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
