package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.RenderingBoundaries;

import java.util.Iterator;

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

    @Override
    public Iterator<FloatBox> iterator() {
        return null;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
