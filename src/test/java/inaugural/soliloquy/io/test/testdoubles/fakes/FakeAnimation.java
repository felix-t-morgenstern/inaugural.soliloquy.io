package inaugural.soliloquy.io.test.testdoubles.fakes;

import soliloquy.specs.common.valueobjects.Pair;
import soliloquy.specs.io.graphics.assets.Animation;
import soliloquy.specs.io.graphics.assets.AnimationFrameSnippet;

import java.util.List;
import java.util.Map;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

public class FakeAnimation implements Animation {
    public String Id;
    public int MsDuration;
    public boolean SnippetAtFrameCalled = false;
    public boolean SupportsMouseEventCapturing;
    public FakeAnimationFrameSnippet AnimationFrameSnippet;
    public Map<Integer, AnimationFrameSnippet> AnimationFrameSnippets;
    public List<Pair<Integer, AnimationFrameSnippet>> SnippetsProvided = listOf();

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
        SnippetsProvided.add(pairOf(i, snippetProvided));
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
}
