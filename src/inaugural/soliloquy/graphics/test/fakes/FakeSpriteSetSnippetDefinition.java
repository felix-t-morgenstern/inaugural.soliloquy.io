package inaugural.soliloquy.graphics.test.fakes;

import soliloquy.specs.graphics.assets.Image;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.SpriteSetSnippetDefinition;

public class FakeSpriteSetSnippetDefinition extends FakeAssetSnippet
        implements SpriteSetSnippetDefinition {
    public String _type;
    public String _direction;

    public FakeSpriteSetSnippetDefinition(Image image, int leftX, int topY, int rightX,
                                          int bottomY, String type, String direction) {
        super(image, leftX, topY, rightX, bottomY);
        _type = type;
        _direction = direction;
    }

    @Override
    public String type() {
        return _type;
    }

    @Override
    public String direction() {
        return _direction;
    }
}
