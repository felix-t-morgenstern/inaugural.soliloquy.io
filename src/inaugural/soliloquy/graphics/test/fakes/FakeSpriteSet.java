package inaugural.soliloquy.graphics.test.fakes;

import soliloquy.specs.graphics.assets.AssetSnippet;
import soliloquy.specs.graphics.assets.SpriteSet;

public class FakeSpriteSet implements SpriteSet {
    public String Id;

    public FakeSpriteSet(String id) {
        Id = id;
    }

    @Override
    public AssetSnippet getImageAndBoundariesForTypeAndDirection(String s, String s1) throws IllegalArgumentException {
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
