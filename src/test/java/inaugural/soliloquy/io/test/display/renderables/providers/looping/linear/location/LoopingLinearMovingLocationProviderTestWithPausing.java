package inaugural.soliloquy.io.test.display.renderables.providers.looping.linear.location;

import inaugural.soliloquy.io.graphics.assets.FontImpl;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.io.graphics.bootstrap.GraphicsCoreLoop;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.mockito.Mockito.when;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a string of text, "Wheee!", white, aligned left, near the top-left of
 * the window.
 * 2. The text will move clockwise from corner to corner. After 3000ms, the movement will pause for
 * 2000ms; after the pause, it will continue moving for another 5000ms.
 * 3. The window will then close.
 */
public class LoopingLinearMovingLocationProviderTestWithPausing
        extends LoopingLinearMovingLocationProviderTest {
    public static void main(String[] args) {
        runTest(
                LoopingLinearMovingLocationProviderTest::
                        generateRenderablesAndRenderersWithMeshAndShader,
                () -> {
                    when(TextLineRenderable.getFont()).thenReturn(new FontImpl(FontDefinition));
                    FrameTimer.ShouldExecuteNextFrame = true;
                },
                LoopingLinearMovingLocationProviderTestWithPausing::closeAfterSomeTime
        );
    }

    public static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        LoopingLinearMovingLocationProvider.reset(GLOBAL_CLOCK.globalTimestamp());

        CheckedExceptionWrapper.sleep(3000);

        LoopingLinearMovingLocationProvider.reportPause(GLOBAL_CLOCK.globalTimestamp());

        CheckedExceptionWrapper.sleep(2000);

        LoopingLinearMovingLocationProvider.reportUnpause(GLOBAL_CLOCK.globalTimestamp());

        CheckedExceptionWrapper.sleep(5000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
