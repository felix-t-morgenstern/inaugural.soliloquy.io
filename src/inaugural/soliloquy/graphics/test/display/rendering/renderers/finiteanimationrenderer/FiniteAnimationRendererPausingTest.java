package inaugural.soliloquy.graphics.test.display.rendering.renderers.finiteanimationrenderer;

import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

class FiniteAnimationRendererPausingTest extends FiniteAnimationRendererTest {
    public static void main(String[] args) {
        runTest(
                windowResolutionManager -> FiniteAnimationRendererTest
                        .generateRenderablesAndRenderersWithMeshAndShader(
                                windowResolutionManager,
                                null),
                timestamp -> FiniteAnimationRenderer.render(FiniteAnimationRenderable, timestamp),
                FiniteAnimationRendererTest::graphicsPreloaderLoadAction,
                FiniteAnimationRendererPausingTest::closeAfterSomeTime
        );
    }

    public static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        CheckedExceptionWrapper.sleep(1500);

        FiniteAnimationRenderable.reportPause(new FakeGlobalClock().globalTimestamp());

        CheckedExceptionWrapper.sleep(2000);

        FiniteAnimationRenderable.reportUnpause(new FakeGlobalClock().globalTimestamp());

        CheckedExceptionWrapper.sleep(1500);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
