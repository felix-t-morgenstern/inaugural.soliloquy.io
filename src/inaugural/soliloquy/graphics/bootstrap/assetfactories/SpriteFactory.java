package inaugural.soliloquy.graphics.bootstrap.assetfactories;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.assets.Image;
import soliloquy.specs.graphics.assets.Sprite;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.SpriteDefinition;

import static inaugural.soliloquy.tools.Check.*;

public class SpriteFactory implements AssetFactory<SpriteDefinition, Sprite> {
    @Override
    public Sprite create(SpriteDefinition spriteDefinition) throws IllegalArgumentException {
        Check.ifNull(spriteDefinition, "spriteDefinition");

        Check.ifNull(spriteDefinition.image(), "spriteDefinition.image()");

        Check.ifNullOrEmpty(spriteDefinition.image().relativeLocation(),
                "spriteDefinition.image().relativeLocation()");

        throwOnLteZero(spriteDefinition.image().width(), "spriteDefinition.image().width()");
        throwOnLteZero(spriteDefinition.image().height(), "spriteDefinition.image().height()");

        Check.ifNonNegative(spriteDefinition.leftX(), "spriteDefinition.leftX()");
        Check.ifNonNegative(spriteDefinition.topY(), "spriteDefinition.topY()");
        Check.ifNonNegative(spriteDefinition.rightX(), "spriteDefinition.rightX()");
        Check.ifNonNegative(spriteDefinition.bottomY(), "spriteDefinition.bottomY()");

        throwOnSecondLte(spriteDefinition.leftX(), spriteDefinition.rightX(),
                "spriteDefinition.leftX()", "spriteDefinition.rightX()");
        throwOnSecondLte(spriteDefinition.topY(), spriteDefinition.bottomY(),
                "spriteDefinition.topY()", "spriteDefinition.bottomY()");

        throwOnSecondGt(spriteDefinition.image().width(), spriteDefinition.leftX(),
                "spriteDefinition.image().width()", "spriteDefinition.leftX()");
        throwOnSecondGt(spriteDefinition.image().height(), spriteDefinition.topY(),
                "spriteDefinition.image().height()", "spriteDefinition.topY()");
        throwOnSecondGt(spriteDefinition.image().width(), spriteDefinition.rightX(),
                "spriteDefinition.image().width()", "spriteDefinition.rightX()");
        throwOnSecondGt(spriteDefinition.image().height(), spriteDefinition.bottomY(),
                "spriteDefinition.image().height()", "spriteDefinition.bottomY()");

        Check.ifNullOrEmpty(spriteDefinition.assetId(), "spriteDefinition.assetId()");

        return new Sprite() {
            @Override
            public String id() throws IllegalStateException {
                return spriteDefinition.assetId();
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

            @Override
            public String getInterfaceName() {
                return Sprite.class.getCanonicalName();
            }
        };
    }

    @Override
    public String getInterfaceName() {
        return AssetFactory.class.getCanonicalName() + "<" +
                SpriteDefinition.class.getCanonicalName() + "," + Sprite.class.getCanonicalName() +
                ">";
    }
}
