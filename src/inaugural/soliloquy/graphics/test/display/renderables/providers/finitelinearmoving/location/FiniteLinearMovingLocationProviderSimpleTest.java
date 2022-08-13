package inaugural.soliloquy.graphics.test.display.renderables.providers.finitelinearmoving.location;

import inaugural.soliloquy.graphics.assets.FontImpl;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a string of text, "Whee!!!", white, moving from the top-left of the
 * screen, starting at 1000ms, towards the middle-right of the screen over 1000ms, and then to
 * the lower-left of the screen over the next 1000ms.
 * 2. The window will remain open for another 1000ms.
 * 3. The window will then close.
 */
class FiniteLinearMovingLocationProviderSimpleTest
        extends FiniteLinearMovingLocationProviderDisplayTest {
    public static void main(String[] args) {
        runTest(FiniteLinearMovingLocationProviderDisplayTest::generateRenderablesAndRenderersWithMeshAndShader,
                timestamp -> TextLineRenderer.render(TextLineRenderable, timestamp),
                () -> {
                    TextLineRenderable.Font =
                            new FontImpl(FontDefinition, FLOAT_BOX_FACTORY, COORDINATE_FACTORY);
                    FrameTimer.ShouldExecuteNextFrame = true;
                },
                FiniteLinearMovingLocationProviderSimpleTest::closeAfterSomeTime);
    }

    public static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        CheckedExceptionWrapper.sleep(4000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
