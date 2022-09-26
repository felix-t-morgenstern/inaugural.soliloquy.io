package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.assets.Image;
import soliloquy.specs.graphics.assets.Sprite;

public class FakeSprite implements Sprite {
    public String Id;
    public Image Image;
    public int LeftX;
    public int TopY;
    public int RightX;
    public int BottomY;

    public FakeSprite() {
    }

    public FakeSprite(String id) {
        Id = id;
    }

    public FakeSprite(Image image) {
        Image = image;
    }

    public FakeSprite(String id, Image image) {
        Id = id;
        Image = image;
    }

    public FakeSprite(Image image, int leftX, int topY, int rightX, int bottomY) {
        Image = image;
        LeftX = leftX;
        TopY = topY;
        RightX = rightX;
        BottomY = bottomY;
    }

    @Override
    public String id() throws IllegalStateException {
        return Id;
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

    @Override
    public String getInterfaceName() {
        return null;
    }
}
