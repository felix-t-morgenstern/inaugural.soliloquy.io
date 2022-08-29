package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.common.valueobjects.Pair;
import soliloquy.specs.graphics.assets.Animation;
import soliloquy.specs.graphics.assets.AnimationFrameSnippet;

import java.util.ArrayList;
import java.util.Map;

public class FakeAnimation implements Animation {
    public String Id;
    public int MsDuration;
    public boolean SnippetAtFrameCalled = false;
    public boolean SupportsMouseEventCapturing;
    public FakeAnimationFrameSnippet AnimationFrameSnippet;
    public Map<Integer, AnimationFrameSnippet> AnimationFrameSnippets;
    public ArrayList<Pair<Integer, AnimationFrameSnippet>> SnippetsProvided = new ArrayList<>();

    public FakeAnimation(String id) {
        Id = id;
    }

    public FakeAnimation(String id, Map<Integer, AnimationFrameSnippet> snippets) {
        Id = id;
        AnimationFrameSnippets = snippets;
    }

    public FakeAnimation(int msDuration) {
        MsDuration = msDuration;
    }

    public FakeAnimation(String id, int msDuration) {
        Id = id;
        MsDuration = msDuration;
    }

    public FakeAnimation(String id, int msDuration, boolean supportsMouseEventCapturing) {
        Id = id;
        MsDuration = msDuration;
        SupportsMouseEventCapturing = supportsMouseEventCapturing;
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
        if (AnimationFrameSnippets != null) {
            return AnimationFrameSnippets.get(i);
        }

        SnippetAtFrameCalled = true;
        AnimationFrameSnippet snippetProvided = AnimationFrameSnippet != null ?
                AnimationFrameSnippet :
                new FakeAnimationFrameSnippet();
        SnippetsProvided.add(new Pair<>(i, snippetProvided));
        return snippetProvided;
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
