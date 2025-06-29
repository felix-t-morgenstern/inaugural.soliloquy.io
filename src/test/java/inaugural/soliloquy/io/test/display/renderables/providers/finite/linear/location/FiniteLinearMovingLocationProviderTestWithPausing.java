package inaugural.soliloquy.io.test.display.renderables.providers.finite.linear.location;

import inaugural.soliloquy.io.graphics.assets.FontImpl;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.io.graphics.bootstrap.GraphicsCoreLoop;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.mockito.Mockito.when;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a string of text, "Whee!!!", white, moving from the top-left of the
 * screen, starting at 1000ms, towards the middle-right of the screen over 1000ms, and then to
 * the lower-left of the screen over the next 1000ms.
 * 2. Roughly mid-way through this movement, the movement will pause for 1000ms.
 * 3. The window will remain open for another 1000ms.
 * 4. The window will then close.
 */
class FiniteLinearMovingLocationProviderTestWithPausing
        extends FiniteLinearMovingLocationProviderDisplayTest {
    public static void main(String[] args) {
        runTest(FiniteLinearMovingLocationProviderDisplayTest::generateRenderablesAndRenderersWithMeshAndShader,
                () -> {
                    when(TextLineRenderable.getFont()).thenReturn(new FontImpl(FontDefinition));
                    FrameTimer.ShouldExecuteNextFrame = true;
                },
                FiniteLinearMovingLocationProviderTestWithPausing::closeAfterSomeTime);
    }

    public static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        CheckedExceptionWrapper.sleep(1000);

        RenderingLocationProvider.reportPause(GLOBAL_CLOCK.globalTimestamp());

        CheckedExceptionWrapper.sleep(1000);

        RenderingLocationProvider.reportUnpause(GLOBAL_CLOCK.globalTimestamp());

        CheckedExceptionWrapper.sleep(2500);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
