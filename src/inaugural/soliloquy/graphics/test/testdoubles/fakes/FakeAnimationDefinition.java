package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.AnimationDefinition;

import java.util.Map;

public class FakeAnimationDefinition implements AnimationDefinition {
    public int _msDuration;
    public String _id;
    public Map<Integer, AnimationFrameSnippet> _frameSnippetDefinitions;

    public FakeAnimationDefinition(int msDuration, String id,
                                   Map<Integer, AnimationFrameSnippet> frameSnippetDefinitions) {
        _msDuration = msDuration;
        _id = id;
        _frameSnippetDefinitions = frameSnippetDefinitions;
    }

    @Override
    public int msDuration() {
        return _msDuration;
    }

    @Override
    public Map<Integer, AnimationFrameSnippet> frameSnippetDefinitions() {
        return _frameSnippetDefinitions;
    }

    @Override
    public String id() {
        return _id;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
