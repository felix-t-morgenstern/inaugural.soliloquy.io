package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.assets.Image;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.SpriteDefinition;

public class FakeSpriteDefinition extends FakeAssetSnippet implements SpriteDefinition {
    public String _id;

    public FakeSpriteDefinition(Image image, int leftX, int topY, int rightX, int bottomY,
                                String id) {
        super(image, leftX, topY, rightX, bottomY);
        _id = id;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }

    @Override
    public String id() {
        return _id;
    }
}
