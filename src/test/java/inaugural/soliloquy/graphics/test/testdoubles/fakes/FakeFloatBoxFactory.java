package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.factories.FloatBoxFactory;

public class FakeFloatBoxFactory implements FloatBoxFactory {
    @Override
    public FloatBox make(float leftX, float topY, float rightX, float bottomY)
            throws IllegalArgumentException {
        return new FakeFloatBox(leftX, topY, rightX, bottomY);
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
