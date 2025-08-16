package inaugural.soliloquy.io.test.integration.display.rendering.renderers.finiteanimationrenderer;

import inaugural.soliloquy.io.test.testdoubles.fakes.FakeColorShiftStackAggregator;
import soliloquy.specs.io.graphics.renderables.colorshifting.NetColorShifts;

public class FiniteAnimationRendererBrightnessShiftTest extends FiniteAnimationRendererTest {
    public static void main(String[] args) {
        var netColorShifts = NetColorShifts.netShifts(0.5f, 0, 0, 0, 0);
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
