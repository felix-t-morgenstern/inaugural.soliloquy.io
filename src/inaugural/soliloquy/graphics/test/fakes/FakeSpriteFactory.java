package inaugural.soliloquy.graphics.test.fakes;

import soliloquy.specs.graphics.assets.Sprite;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.SpriteDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeSpriteFactory implements AssetFactory<SpriteDefinition, Sprite> {
    public final Map<String,SpriteDefinition> INPUTS = new HashMap<>();
    public final List<Sprite> OUTPUTS = new ArrayList<>();

    @Override
    public Sprite make(SpriteDefinition spriteDefinition) throws IllegalArgumentException {
        INPUTS.put(spriteDefinition.assetId(), spriteDefinition);
        Sprite createdSprite = new FakeSprite(spriteDefinition.assetId());
        OUTPUTS.add(createdSprite);
        return createdSprite;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
