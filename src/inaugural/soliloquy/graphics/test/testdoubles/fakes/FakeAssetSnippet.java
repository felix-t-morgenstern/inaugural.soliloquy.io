package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.assets.AssetSnippet;
import soliloquy.specs.graphics.assets.Image;

public class FakeAssetSnippet implements AssetSnippet {
    public Image _image;
    public int _leftX;
    public int _topY;
    public int _rightX;
    public int _bottomY;

    public FakeAssetSnippet() {

    }

    public FakeAssetSnippet(Image image, int leftX, int topY, int rightX, int bottomY) {
        _image = image;
        _leftX = leftX;
        _topY = topY;
        _rightX = rightX;
        _bottomY = bottomY;
    }

    @Override
    public Image image() {
        return _image;
    }

    @Override
    public int leftX() {
        return _leftX;
    }

    @Override
    public int topY() {
        return _topY;
    }

    @Override
    public int rightX() {
        return _rightX;
    }

    @Override
    public int bottomY() {
        return _bottomY;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
