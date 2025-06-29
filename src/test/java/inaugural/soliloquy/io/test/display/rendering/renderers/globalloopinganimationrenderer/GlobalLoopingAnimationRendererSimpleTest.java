package inaugural.soliloquy.io.test.display.rendering.renderers.globalloopinganimationrenderer;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 1920x1080 pixels in the middle of the screen for 3000ms
 * with a titlebar reading "My title bar". The window will contain a looping animation of a
 * torch, centered in the screen, which will change its frames every 250ms. The animation will
 * persist for 2250ms.
 * 2. The window will then close.
 */
class GlobalLoopingAnimationRendererSimpleTest extends GlobalLoopingAnimationRendererTest {
    public static void main(String[] args) {
        runTest(
                windowResolutionManager -> generateRenderablesAndRenderersWithMeshAndShader(
                                windowResolutionManager,
                                null
                        ),
                GlobalLoopingAnimationRendererTest::graphicsPreloaderLoadAction,
                graphicsCoreLoop -> closeAfterSomeTime(graphicsCoreLoop, TEST_DURATION_MS)
        );
    }
}
