package inaugural.soliloquy.graphics.bootstrap.assetfactories;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.assets.Image;
import soliloquy.specs.graphics.assets.Sprite;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.SpriteDefinition;

public class SpriteFactory extends AbstractAssetFactory<SpriteDefinition, Sprite> {
    @Override
    public Sprite make(SpriteDefinition spriteDefinition) throws IllegalArgumentException {
        Check.ifNull(spriteDefinition, "spriteDefinition");

        throwOnInvalidSnippetDefinition(spriteDefinition, "spriteDefinition");

        Check.ifNullOrEmpty(spriteDefinition.id(), "spriteDefinition.id()");

        return new Sprite() {
            @Override
            public String id() throws IllegalStateException {
                return spriteDefinition.id();
            }

            @Override
            public Image image() {
                return spriteDefinition.image();
            }

            @Override
            public int leftX() {
                return spriteDefinition.leftX();
            }

            @Override
            public int topY() {
                return spriteDefinition.topY();
            }

            @Override
            public int rightX() {
                return spriteDefinition.rightX();
            }

            @Override
            public int bottomY() {
                return spriteDefinition.bottomY();
            }
        };
    }
}
