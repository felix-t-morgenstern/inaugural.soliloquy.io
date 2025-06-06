package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.assets.Sprite;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.SpriteDefinition;

import java.util.List;
import java.util.Map;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;

public class FakeSpriteFactory implements AssetFactory<SpriteDefinition, Sprite> {
    public final Map<String, SpriteDefinition> INPUTS = mapOf();
    public final List<Sprite> OUTPUTS = listOf();

    @Override
    public Sprite make(SpriteDefinition spriteDefinition) throws IllegalArgumentException {
        INPUTS.put(spriteDefinition.id(), spriteDefinition);
        var createdSprite = new FakeSprite(spriteDefinition.id());
        OUTPUTS.add(createdSprite);
        return createdSprite;
    }
}
