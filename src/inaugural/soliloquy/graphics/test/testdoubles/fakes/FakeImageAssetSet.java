package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.assets.ImageAsset;
import soliloquy.specs.graphics.assets.ImageAssetSet;

public class FakeImageAssetSet implements ImageAssetSet {
    public String Id;

    public FakeImageAssetSet(String id) {
        Id = id;
    }

    @Override
    public ImageAsset getImageAssetForTypeAndDirection(String s, String s1) throws IllegalArgumentException {
        return null;
    }

    @Override
    public String id() throws IllegalStateException {
        return Id;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
