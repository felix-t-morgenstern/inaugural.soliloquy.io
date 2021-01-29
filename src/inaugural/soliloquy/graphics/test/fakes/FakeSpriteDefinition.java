package inaugural.soliloquy.graphics.test.fakes;

import soliloquy.specs.graphics.assets.Image;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.SpriteDefinition;

public class FakeSpriteDefinition implements SpriteDefinition {
    public Image _image;
    public int _leftX;
    public int _topY;
    public int _rightX;
    public int _bottomY;
    public String _assetId;

    public FakeSpriteDefinition(Image image, int leftX, int topY, int rightX, int bottomY,
                                String assetId) {
        _image = image;
        _leftX = leftX;
        _topY = topY;
        _rightX = rightX;
        _bottomY = bottomY;
        _assetId = assetId;
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

    @Override
    public String assetId() {
        return _assetId;
    }
}
