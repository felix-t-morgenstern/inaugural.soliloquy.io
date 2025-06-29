package inaugural.soliloquy.io.test.display.renderables.providers.string;

import inaugural.soliloquy.io.graphics.assets.FontImpl;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.io.graphics.bootstrap.GraphicsCoreLoop;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.mockito.Mockito.when;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a string of text, "I appear over time!", white, aligned left, near the
 * left edge of the window, vertically centered, gradually, starting at 2000ms.
 * 2. The text will pause after 2500ms; this pause will last for 2000ms, then the text will
 * complete displaying at roughly 6000ms. The text will remain on-screen for an additional
 * 2000ms.
 * 3. The window will then close.
 */
class ProgressiveStringProviderDisplayTestWithPausing
        extends ProgressiveStringProviderDisplayTest {
    public static void main(String[] args) {
        runTest(windowResolutionManager ->
                        ProgressiveStringProviderDisplayTest
                                .generateRenderablesAndRenderersWithMeshAndShader(
                                        windowResolutionManager,
                                        2000L,
                                        2000L
                                ),
                () -> {
                    when(TextLineRenderable.getFont()).thenReturn(new FontImpl(FontDefinition));
                    FrameTimer.ShouldExecuteNextFrame = true;
                },
                ProgressiveStringProviderDisplayTestWithPausing::closeAfterSomeTime);
    }

    public static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        CheckedExceptionWrapper.sleep(2500);

        LineTextProvider.reportPause(GLOBAL_CLOCK.globalTimestamp());

        CheckedExceptionWrapper.sleep(2000);

        LineTextProvider.reportUnpause(GLOBAL_CLOCK.globalTimestamp());

        CheckedExceptionWrapper.sleep(3500);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
