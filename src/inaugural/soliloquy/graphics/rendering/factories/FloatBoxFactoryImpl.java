package inaugural.soliloquy.graphics.rendering.factories;

import inaugural.soliloquy.graphics.rendering.FloatBoxImpl;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.factories.FloatBoxFactory;

public class FloatBoxFactoryImpl implements FloatBoxFactory {
    @Override
    public FloatBox make(float leftX, float topY, float rightX, float bottomY)
            throws IllegalArgumentException {
        return new FloatBoxImpl(leftX, topY, rightX, bottomY);
    }

    @Override
    public String getInterfaceName() {
        return FloatBoxFactory.class.getCanonicalName();
    }
}
