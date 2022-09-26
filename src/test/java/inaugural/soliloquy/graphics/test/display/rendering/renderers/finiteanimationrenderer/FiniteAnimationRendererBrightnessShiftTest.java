package inaugural.soliloquy.graphics.test.display.rendering.renderers.finiteanimationrenderer;

import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeColorShiftStackAggregator;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeNetColorShifts;

public class FiniteAnimationRendererBrightnessShiftTest extends FiniteAnimationRendererTest {
    public static void main(String[] args) {
        FakeNetColorShifts netColorShifts = new FakeNetColorShifts();
        netColorShifts.BrightnessShift = 0.5f;
        FakeColorShiftStackAggregator colorShiftStackAggregator =
                new FakeColorShiftStackAggregator(netColorShifts);

        runTest(
                windowResolutionManager -> FiniteAnimationRendererTest
                        .generateRenderablesAndRenderersWithMeshAndShader(
                                windowResolutionManager,
                                colorShiftStackAggregator),
                timestamp -> FiniteAnimationRenderer.render(FiniteAnimationRenderable, timestamp),
                FiniteAnimationRendererTest::graphicsPreloaderLoadAction,
                graphicsCoreLoop -> closeAfterSomeTime(graphicsCoreLoop, TestDurationMs)
        );
    }
}
