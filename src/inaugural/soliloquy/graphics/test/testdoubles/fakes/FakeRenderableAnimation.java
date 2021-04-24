package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.assets.Animation;
import soliloquy.specs.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;

public class FakeRenderableAnimation implements ProviderAtTime<AnimationFrameSnippet> {
    public Animation Animation;
    public long StartTimestamp;

    public FakeRenderableAnimation(Animation animation, long startTimestamp) {
        Animation = animation;
        StartTimestamp = startTimestamp;
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
}
