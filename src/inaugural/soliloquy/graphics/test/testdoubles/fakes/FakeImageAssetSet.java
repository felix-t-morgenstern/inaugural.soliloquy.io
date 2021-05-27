package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.assets.ImageAsset;
import soliloquy.specs.graphics.assets.ImageAssetSet;

public class FakeImageAssetSet implements ImageAssetSet {
    public String Id;
    public boolean CapturesMouseEvents;

    public FakeImageAssetSet() {
    }

    public FakeImageAssetSet(String id) {
        Id = id;
    }

    public FakeImageAssetSet(boolean capturesMouseEvents) {
        CapturesMouseEvents = capturesMouseEvents;
    }

    public FakeImageAssetSet(String id, boolean capturesMouseEvents) {
        Id = id;
        CapturesMouseEvents = capturesMouseEvents;
    }

    @Override
    public ImageAsset getImageAssetForTypeAndDirection(String s, String s1) throws IllegalArgumentException {
        return null;
    }

    @Override
    public boolean supportsMouseEventCapturing() {
        return CapturesMouseEvents;
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
