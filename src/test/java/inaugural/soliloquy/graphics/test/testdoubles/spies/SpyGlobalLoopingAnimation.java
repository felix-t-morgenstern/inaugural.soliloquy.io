package inaugural.soliloquy.graphics.test.testdoubles.spies;

import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeAnimationFrameSnippet;
import soliloquy.specs.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.graphics.assets.GlobalLoopingAnimation;

import java.util.List;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.listOf;

public class SpyGlobalLoopingAnimation implements GlobalLoopingAnimation {
    public List<Long> Timestamps = listOf();
    public List<AnimationFrameSnippet> OutputSnippets = listOf();

    @Override
    public AnimationFrameSnippet provide(long timestamp) throws IllegalArgumentException {
        Timestamps.add(timestamp);
        FakeAnimationFrameSnippet output = new FakeAnimationFrameSnippet();
        OutputSnippets.add(output);
        return output;
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
        return false;
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
        return null;
    }

    @Override
    public void reset(long l) throws IllegalArgumentException {

    }
}
