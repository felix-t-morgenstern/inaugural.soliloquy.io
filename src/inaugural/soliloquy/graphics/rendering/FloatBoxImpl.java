package inaugural.soliloquy.graphics.rendering;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.rendering.FloatBox;

public class FloatBoxImpl implements FloatBox {
    private final float LEFT_X;
    private final float TOP_Y;
    private final float RIGHT_X;
    private final float BOTTOM_Y;

    public FloatBoxImpl(float leftX, float topY, float rightX, float bottomY) {
        LEFT_X = leftX;
        TOP_Y = topY;
        RIGHT_X = rightX;
        BOTTOM_Y = bottomY;
    }

    @Override
    public float leftX() {
        return LEFT_X;
    }

    @Override
    public float topY() {
        return TOP_Y;
    }

    @Override
    public float rightX() {
        return RIGHT_X;
    }

    @Override
    public float bottomY() {
        return BOTTOM_Y;
    }

    @Override
    public float width() {
        return RIGHT_X - LEFT_X;
    }

    @Override
    public float height() {
        return BOTTOM_Y - TOP_Y;
    }

    @Override
    public FloatBox intersection(FloatBox floatBox) throws IllegalArgumentException {
        Check.ifNull(floatBox, "floatBox");
        return new FloatBoxImpl(
                Math.max(LEFT_X, floatBox.leftX()),
                Math.max(TOP_Y, floatBox.topY()),
                Math.min(RIGHT_X, floatBox.rightX()),
                Math.min(BOTTOM_Y, floatBox.bottomY()));
    }

    @Override
    public FloatBox translate(float xTranslation, float yTranslation) {
        return new FloatBoxImpl(
                LEFT_X + xTranslation,
                TOP_Y + yTranslation,
                RIGHT_X + xTranslation,
                BOTTOM_Y + yTranslation);
    }

    @Override
    public String getInterfaceName() {
        return FloatBox.class.getCanonicalName();
    }
}
