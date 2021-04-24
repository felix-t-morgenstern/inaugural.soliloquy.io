package inaugural.soliloquy.graphics.test.testdoubles.spies;

import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeAnimationFrameSnippet;
import soliloquy.specs.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;

import java.util.ArrayList;

public class SpyRenderableAnimation implements ProviderAtTime<AnimationFrameSnippet> {
    public ArrayList<Long> Timestamps = new ArrayList<>();
    public ArrayList<AnimationFrameSnippet> OutputSnippets = new ArrayList<>();

    @Override
    public AnimationFrameSnippet provide(long timestamp) throws IllegalArgumentException {
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

    @Override
    public String getInterfaceName() {
        return null;
    }

    @Override
    public AnimationFrameSnippet getArchetype() {
        return null;
    }
}
