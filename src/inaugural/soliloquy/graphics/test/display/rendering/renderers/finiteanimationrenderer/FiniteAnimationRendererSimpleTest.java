package inaugural.soliloquy.graphics.test.display.rendering.renderers.finiteanimationrenderer;

/**
 * Test acceptance criteria:
 *
 * 1. An 800x600 window will open. An explosion will be displayed in the center of the window over
 * roughly 1250ms.
 * 2. The window will close.
 */
class FiniteAnimationRendererSimpleTest extends FiniteAnimationRendererTest {
    public static void main(String[] args) {
        runTest(
                windowResolutionManager -> FiniteAnimationRendererTest
                        .generateRenderablesAndRenderersWithMeshAndShader(
                                windowResolutionManager,
                                null),
                timestamp -> FiniteAnimationRenderer.render(FiniteAnimationRenderable, timestamp),
                FiniteAnimationRendererTest::graphicsPreloaderLoadAction,
                graphicsCoreLoop -> closeAfterSomeTime(graphicsCoreLoop, TestDurationMs)
        );
    }
}
