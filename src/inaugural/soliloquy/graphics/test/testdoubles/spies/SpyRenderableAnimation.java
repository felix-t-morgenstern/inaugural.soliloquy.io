package inaugural.soliloquy.graphics.test.testdoubles.spies;

import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeAnimationFrameSnippet;
import soliloquy.specs.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.graphics.renderables.RenderableAnimation;

import java.util.ArrayList;

public class SpyRenderableAnimation implements RenderableAnimation {
    public ArrayList<Long> Timestamps = new ArrayList<>();
    public ArrayList<AnimationFrameSnippet> OutputSnippets = new ArrayList<>();

    @Override
    public AnimationFrameSnippet currentSnippet(long timestamp) throws IllegalArgumentException {
        Timestamps.add(timestamp);
        FakeAnimationFrameSnippet output = new FakeAnimationFrameSnippet();
        OutputSnippets.add(output);
        return output;
    }

    @Override
    public void reportPause(long l) throws IllegalArgumentException {

    }

    @Override
    public void reportUnpause(long l) throws IllegalArgumentException {

    }
}
