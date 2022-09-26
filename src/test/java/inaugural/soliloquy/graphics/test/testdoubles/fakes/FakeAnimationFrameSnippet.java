package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.graphics.assets.Image;

public class FakeAnimationFrameSnippet extends FakeAssetSnippet implements AnimationFrameSnippet {
    public float OffsetX;
    public float OffsetY;

    public FakeAnimationFrameSnippet() {
        this(new FakeImage("", 100, 100), 0, 0, 0, 0, 0f, 0f);
    }

    public FakeAnimationFrameSnippet(Image image) {
        this(image, 0, 0, 0, 0, 0, 0);
    }

    public FakeAnimationFrameSnippet(Image image, int leftX, int topY, int rightX, int bottomY,
                                     float offsetX, float offsetY) {
        super(image, leftX, topY, rightX, bottomY);

        OffsetX = offsetX;
        OffsetY = offsetY;
    }

    @Override
    public float offsetX() {
        return OffsetX;
    }

    @Override
    public float offsetY() {
        return OffsetY;
    }
}
