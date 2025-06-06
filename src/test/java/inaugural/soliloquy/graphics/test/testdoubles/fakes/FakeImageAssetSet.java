package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.common.shared.Direction;
import soliloquy.specs.common.valueobjects.Pair;
import soliloquy.specs.graphics.assets.ImageAsset;
import soliloquy.specs.graphics.assets.ImageAssetSet;

import java.util.List;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;



public class FakeImageAssetSet implements ImageAssetSet {
    public String Id;
    public boolean CapturesMouseEvents;
    public ImageAsset ImageAsset;
    public List<Pair<String, Direction>> GetImageAssetForTypeAndDirectionInputs =
            listOf();

    public FakeImageAssetSet(String id) {
        Id = id;
    }

    public FakeImageAssetSet(boolean capturesMouseEvents) {
        CapturesMouseEvents = capturesMouseEvents;
    }

    @Override
    public ImageAsset getImageAssetForTypeAndDirection(String type, Direction direction)
            throws IllegalArgumentException {
        GetImageAssetForTypeAndDirectionInputs.add(pairOf(type, direction));
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
}
