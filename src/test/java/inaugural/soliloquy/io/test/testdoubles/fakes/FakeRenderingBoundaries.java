package inaugural.soliloquy.io.test.testdoubles.fakes;

import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;

public class FakeRenderingBoundaries implements RenderingBoundaries {
    public FloatBox CurrentBoundaries;

    @Override
    public FloatBox currentBoundaries() {
        return CurrentBoundaries;
    }

    @Override
    public void pushNewBoundaries(FloatBox floatBox) {

    }

    @Override
    public void popMostRecentBoundaries() {

    }

    @Override
    public void clearAllBoundaries() {

    }
}
