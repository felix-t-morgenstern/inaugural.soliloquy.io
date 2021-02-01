package inaugural.soliloquy.graphics.test.fakes;

import soliloquy.specs.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.AnimationDefinition;

import java.util.Map;

public class FakeAnimationDefinition implements AnimationDefinition {
    public int _msDuration;
    public String _assetId;
    public Map<Integer, AnimationFrameSnippet> _frameSnippetDefinitions;

    public FakeAnimationDefinition(int msDuration, String assetId,
                                   Map<Integer, AnimationFrameSnippet> frameSnippetDefinitions) {
        _msDuration = msDuration;
        _assetId = assetId;
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
    public String assetId() {
        return _assetId;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
