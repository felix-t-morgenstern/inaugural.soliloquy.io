package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.assets.Animation;
import soliloquy.specs.graphics.assets.AnimationFrameSnippet;

public class FakeAnimation implements Animation {
    public String Id;
    public int MsDuration;
    public boolean SnippetAtFrameCalled = false;
    public boolean SupportsMouseEventCapturing;

    public FakeAnimation(String id) {
        Id = id;
    }

    public FakeAnimation(String id, int msDuration) {
        Id = id;
        MsDuration = msDuration;
    }

    public FakeAnimation(String id, boolean supportsMouseEventCapturing) {
        Id = id;
        SupportsMouseEventCapturing = supportsMouseEventCapturing;
    }

    @Override
    public int msDuration() {
        return MsDuration;
    }

    @Override
    public AnimationFrameSnippet snippetAtFrame(int i) throws IllegalArgumentException {
        SnippetAtFrameCalled = true;
        return new FakeAnimationFrameSnippet();
    }

    @Override
    public boolean supportsMouseEventCapturing() {
        return SupportsMouseEventCapturing;
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
