package inaugural.soliloquy.io.test.testdoubles.fakes;

import soliloquy.specs.io.graphics.assets.AssetSnippet;
import soliloquy.specs.io.graphics.assets.Image;

public class FakeAssetSnippet implements AssetSnippet {
    public Image Image;
    public int LeftX;
    public int TopY;
    public int RightX;
    public int BottomY;

    public FakeAssetSnippet(Image image, int leftX, int topY, int rightX, int bottomY) {
        Image = image;
        LeftX = leftX;
        TopY = topY;
        RightX = rightX;
        BottomY = bottomY;
    }

    @Override
    public Image image() {
        return Image;
    }

    @Override
    public int leftX() {
        return LeftX;
    }

    @Override
    public int topY() {
        return TopY;
    }

    @Override
    public int rightX() {
        return RightX;
    }

    @Override
    public int bottomY() {
        return BottomY;
    }
}
