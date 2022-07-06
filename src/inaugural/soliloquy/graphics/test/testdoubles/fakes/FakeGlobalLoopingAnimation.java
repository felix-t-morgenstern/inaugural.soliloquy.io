package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.assets.Animation;
import soliloquy.specs.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.graphics.assets.GlobalLoopingAnimation;

import java.util.UUID;

public class FakeGlobalLoopingAnimation implements GlobalLoopingAnimation {
    public String Id;
    public Animation Animation;
    public long StartTimestamp;
    public boolean SupportsMouseEvents;

    public FakeGlobalLoopingAnimation(String id) {
        Id = id;
    }

    public FakeGlobalLoopingAnimation(Animation animation, long startTimestamp) {
        Animation = animation;
        StartTimestamp = startTimestamp;
    }

    public FakeGlobalLoopingAnimation(boolean supportsMouseEvents) {
        SupportsMouseEvents = supportsMouseEvents;
    }

    @Override
    public AnimationFrameSnippet provide(long l) throws IllegalArgumentException {
        return Animation.snippetAtFrame((int)(l - StartTimestamp) % Animation.msDuration());
    }

    @Override
    public void reportPause(long l) throws IllegalArgumentException {

    }

    @Override
    public void reportUnpause(long l) throws IllegalArgumentException {

    }

    @Override
    public String getInterfaceName() {
        return null;
    }

    @Override
    public AnimationFrameSnippet getArchetype() {
        return null;
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
