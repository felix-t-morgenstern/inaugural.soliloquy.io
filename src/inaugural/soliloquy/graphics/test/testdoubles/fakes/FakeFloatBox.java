package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.rendering.FloatBox;

public class FakeFloatBox implements FloatBox {
    public float LeftX;
    public float TopY;
    public float RightX;
    public float BottomY;

    public FakeFloatBox(float leftX, float topY, float rightX, float bottomY) {
        LeftX = leftX;
        TopY = topY;
        RightX = rightX;
        BottomY = bottomY;
    }

    @Override
    public float leftX() {
        return LeftX;
    }

    @Override
    public float topY() {
        return TopY;
    }

    @Override
    public float rightX() {
        return RightX;
    }

    @Override
    public float bottomY() {
        return BottomY;
    }

    @Override
    public float width() {
        return RightX - LeftX;
    }

    @Override
    public float height() {
        return BottomY - TopY;
    }

    @Override
    public FloatBox intersection(FloatBox floatBox) throws IllegalArgumentException {
        return new FakeFloatBox(
                Math.max(LeftX, floatBox.leftX()),
                Math.max(TopY, floatBox.topY()),
                Math.min(RightX, floatBox.rightX()),
                Math.min(BottomY, floatBox.bottomY()));
    }

    @Override
    public FloatBox translate(float xTranslation, float yTranslation) {
        return new FakeFloatBox(
                LeftX + xTranslation,
                TopY + yTranslation,
                RightX + xTranslation,
                BottomY + yTranslation);
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
