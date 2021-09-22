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
        GetImageAssetForTypeAndDirectionInputs.add(new Pair<String, String>() {
            @Override
            public String getItem1() {
                return type;
            }

            @Override
            public String getItem2() {
                return direction;
            }

            @Override
            public void setItem1(String s) throws IllegalArgumentException {

            }

            @Override
            public void setItem2(String s) throws IllegalArgumentException {

            }

            @Override
            public Pair<String, String> makeClone() {
                return null;
            }

            @Override
            public String getFirstArchetype() throws IllegalStateException {
                return null;
            }

            @Override
            public String getSecondArchetype() throws IllegalStateException {
                return null;
            }

            @Override
            public String getInterfaceName() {
                return null;
            }
        });
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
