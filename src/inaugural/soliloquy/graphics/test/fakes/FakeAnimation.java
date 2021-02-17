package inaugural.soliloquy.graphics.test.fakes;

import soliloquy.specs.graphics.assets.Animation;
import soliloquy.specs.graphics.assets.AnimationFrameSnippet;

public class FakeAnimation implements Animation {
    public String Id;

    public FakeAnimation(String id) {
        Id = id;
    }

    @Override
    public int msDuration() {
        return 0;
    }

    @Override
    public AnimationFrameSnippet snippetAtFrame(int i) throws IllegalArgumentException {
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
