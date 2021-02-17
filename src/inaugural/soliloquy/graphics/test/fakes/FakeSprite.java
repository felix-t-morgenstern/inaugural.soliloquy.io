package inaugural.soliloquy.graphics.test.fakes;

import soliloquy.specs.graphics.assets.Image;
import soliloquy.specs.graphics.assets.Sprite;

public class FakeSprite implements Sprite {
    public String Id;

    public FakeSprite(String id) {
        Id = id;
    }

    @Override
    public String id() throws IllegalStateException {
        return Id;
    }

    @Override
    public Image image() {
        return null;
    }

    @Override
    public int leftX() {
        return 0;
    }

    @Override
    public int topY() {
        return 0;
    }

    @Override
    public int rightX() {
        return 0;
    }

    @Override
    public int bottomY() {
        return 0;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
