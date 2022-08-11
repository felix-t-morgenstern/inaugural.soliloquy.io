package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.common.infrastructure.Pair;
import soliloquy.specs.graphics.assets.ImageAsset;
import soliloquy.specs.graphics.assets.ImageAssetSet;

import java.util.ArrayList;

public class FakeImageAssetSet implements ImageAssetSet {
    public String Id;
    public boolean CapturesMouseEvents;
    public ImageAsset ImageAsset;
    public ArrayList<Pair<String, String>> GetImageAssetForTypeAndDirectionInputs =
            new ArrayList<>();

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
    public ImageAsset getImageAssetForTypeAndDirection(String type, String direction)
            throws IllegalArgumentException {
        GetImageAssetForTypeAndDirectionInputs.add(new Pair<>(type, direction));
        return ImageAsset;
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
