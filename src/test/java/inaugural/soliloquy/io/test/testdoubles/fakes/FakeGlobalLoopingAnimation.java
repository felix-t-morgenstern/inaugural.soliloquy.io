package inaugural.soliloquy.io.test.testdoubles.fakes;

import soliloquy.specs.io.graphics.assets.Animation;
import soliloquy.specs.io.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.io.graphics.assets.GlobalLoopingAnimation;

import java.util.UUID;

public class FakeGlobalLoopingAnimation implements GlobalLoopingAnimation {
    public String Id;
    public Animation Animation;
    public long StartTimestamp;
    public boolean SupportsMouseEvents;

    public FakeGlobalLoopingAnimation(String id) {
        Id = id;
    }

    public FakeGlobalLoopingAnimation(boolean supportsMouseEvents) {
        SupportsMouseEvents = supportsMouseEvents;
    }

    @Override
    public AnimationFrameSnippet provide(long l) throws IllegalArgumentException {
        return Animation.snippetAtFrame((int) (l - StartTimestamp) % Animation.msDuration());
    }

    @Override
    public Object representation() {
        return null;
    }

    @Override
    public void reportPause(long l) throws IllegalArgumentException {

    }

    @Override
    public void reportUnpause(long l) throws IllegalArgumentException {

    }

    @Override
    public boolean supportsMouseEvents() {
        return SupportsMouseEvents;
    }

    @Override
    public String animationId() {
        return null;
    }

    @Override
    public int periodModuloOffset() {
        return 0;
    }

    @Override
    public Long pausedTimestamp() {
        return null;
    }

    @Override
    public UUID uuid() {
        return null;
    }

    @Override
    public Long mostRecentTimestamp() {
        return null;
    }

    @Override
    public String id() throws IllegalStateException {
        return Id;
    }

    @Override
    public void reset(long l) throws IllegalArgumentException {

    }
}
