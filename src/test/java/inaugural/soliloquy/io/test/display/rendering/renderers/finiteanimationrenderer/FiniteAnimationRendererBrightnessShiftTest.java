package inaugural.soliloquy.io.test.display.rendering.renderers.finiteanimationrenderer;

import inaugural.soliloquy.io.test.testdoubles.fakes.FakeColorShiftStackAggregator;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeNetColorShifts;

public class FiniteAnimationRendererBrightnessShiftTest extends FiniteAnimationRendererTest {
    public static void main(String[] args) {
        var netColorShifts = new FakeNetColorShifts();
        netColorShifts.BrightnessShift = 0.5f;
        var colorShiftStackAggregator = new FakeColorShiftStackAggregator(netColorShifts);

        runTest(
                windowResolutionManager -> FiniteAnimationRendererTest
                        .generateRenderablesAndRenderersWithMeshAndShader(
                                windowResolutionManager,
                                colorShiftStackAggregator),
                FiniteAnimationRendererTest::graphicsPreloaderLoadAction,
                graphicsCoreLoop -> closeAfterSomeTime(graphicsCoreLoop, TestDurationMs)
        );
    }
}
