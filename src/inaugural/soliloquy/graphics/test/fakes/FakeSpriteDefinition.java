package inaugural.soliloquy.graphics.test.fakes;

import soliloquy.specs.graphics.assets.Image;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.SpriteDefinition;

public class FakeSpriteDefinition extends FakeAssetSnippet implements SpriteDefinition {
    public String _assetId;

    public FakeSpriteDefinition(Image image, int leftX, int topY, int rightX, int bottomY,
                                String assetId) {
        super(image, leftX, topY, rightX, bottomY);
        _assetId = assetId;
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
