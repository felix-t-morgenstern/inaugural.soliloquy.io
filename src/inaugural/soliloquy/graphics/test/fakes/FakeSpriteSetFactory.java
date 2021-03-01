package inaugural.soliloquy.graphics.test.fakes;

import soliloquy.specs.graphics.assets.SpriteSet;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.SpriteSetDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeSpriteSetFactory implements AssetFactory<SpriteSetDefinition, SpriteSet> {
    public final Map<String, SpriteSetDefinition> INPUTS = new HashMap<>();
    public final List<SpriteSet> OUTPUTS = new ArrayList<>();

    @Override
    public SpriteSet make(SpriteSetDefinition spriteSetDefinition) throws IllegalArgumentException {
        INPUTS.put(spriteSetDefinition.assetId(), spriteSetDefinition);
        SpriteSet createdSpriteSet = new FakeSpriteSet(spriteSetDefinition.assetId());
        OUTPUTS.add(createdSpriteSet);
        return createdSpriteSet;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
