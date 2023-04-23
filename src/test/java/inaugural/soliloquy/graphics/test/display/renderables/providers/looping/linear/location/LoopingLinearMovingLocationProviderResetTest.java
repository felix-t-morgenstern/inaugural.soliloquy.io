package inaugural.soliloquy.graphics.test.display.renderables.providers.looping.linear.location;

import inaugural.soliloquy.graphics.assets.FontImpl;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.mockito.Mockito.when;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a string of text, "Wheee!", white, aligned left, near the top-left of
 * the window.
 * 2. The text will move clockwise from corner to corner. After 2500ms, the movement will reset to
 * the top-left corner; then, it will continue for another 4000ms (i.e. one loop).
 * 3. The window will then close.
 */
public class LoopingLinearMovingLocationProviderResetTest
        extends LoopingLinearMovingLocationProviderTest {
    public static void main(String[] args) {
        runTest(
                LoopingLinearMovingLocationProviderTest::
                        generateRenderablesAndRenderersWithMeshAndShader,
                () -> {
                    when(TextLineRenderable.getFont()).thenReturn(
                            new FontImpl(FontDefinition, FLOAT_BOX_FACTORY));
                    FrameTimer.ShouldExecuteNextFrame = true;
                },
                LoopingLinearMovingLocationProviderResetTest::closeAfterSomeTime
        );
    }

    public static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        LoopingLinearMovingLocationProvider.reset(GLOBAL_CLOCK.globalTimestamp());
        LoopingLinearMovingLocationProvider.reset(GLOBAL_CLOCK.globalTimestamp());

        CheckedExceptionWrapper.sleep(2500);

        LoopingLinearMovingLocationProvider.reset(GLOBAL_CLOCK.globalTimestamp());

        CheckedExceptionWrapper.sleep(4000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
