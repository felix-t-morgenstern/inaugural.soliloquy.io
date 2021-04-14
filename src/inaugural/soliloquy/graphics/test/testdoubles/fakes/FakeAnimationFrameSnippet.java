package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.graphics.assets.Image;

public class FakeAnimationFrameSnippet extends FakeAssetSnippet implements AnimationFrameSnippet {
    public float _offsetX;
    public float _offsetY;

    public FakeAnimationFrameSnippet() {
        this(new FakeImage("", 100, 100), 0, 0, 0, 0, 0f, 0f);
    }

    public FakeAnimationFrameSnippet(Image image, int leftX, int topY, int rightX, int bottomY,
                                     float offsetX, float offsetY) {
        super(image, leftX, topY, rightX, bottomY);

        _offsetX = offsetX;
        _offsetY = offsetY;
    }

    @Override
    public float offsetX() {
        return _offsetX;
    }

    @Override
    public float offsetY() {
        return _offsetY;
    }
}
