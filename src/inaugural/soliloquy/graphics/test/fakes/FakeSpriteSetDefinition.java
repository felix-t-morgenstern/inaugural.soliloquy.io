package inaugural.soliloquy.graphics.test.fakes;

import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.SpriteSetDefinition;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.SpriteSetSnippetDefinition;

import java.util.List;

public class FakeSpriteSetDefinition implements SpriteSetDefinition {
    private List<SpriteSetSnippetDefinition> _spriteSetSnippetDefinitions;
    private String _assetId;

    public FakeSpriteSetDefinition(List<SpriteSetSnippetDefinition> spriteSetSnippetDefinitions,
                                   String assetId) {
        _spriteSetSnippetDefinitions = spriteSetSnippetDefinitions;
        _assetId = assetId;
    }

    @Override
    public List<SpriteSetSnippetDefinition> snippetDefinitions() {
        return _spriteSetSnippetDefinitions;
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
