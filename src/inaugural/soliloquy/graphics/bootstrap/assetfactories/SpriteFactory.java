package inaugural.soliloquy.graphics.bootstrap.assetfactories;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.assets.Image;
import soliloquy.specs.graphics.assets.Sprite;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.SpriteDefinition;

public class SpriteFactory implements AssetFactory<SpriteDefinition, Sprite> {
    @Override
    public Sprite create(SpriteDefinition spriteDefinition) throws IllegalArgumentException {
        Check.ifNull(spriteDefinition, "spriteDefinition");

        Check.ifNull(spriteDefinition.image(), "spriteDefinition.image()");

        Check.ifNullOrEmpty(spriteDefinition.image().relativeLocation(),
                "spriteDefinition.image().relativeLocation()");

        throwOnZeroOrLess(spriteDefinition.image().width(), "spriteDefinition.image().width()");
        throwOnZeroOrLess(spriteDefinition.image().height(), "spriteDefinition.image().height()");

        throwOnNegative(spriteDefinition.leftX(), "spriteDefinition.leftX()");
        throwOnNegative(spriteDefinition.topY(), "spriteDefinition.topY()");
        throwOnNegative(spriteDefinition.rightX(), "spriteDefinition.rightX()");
        throwOnNegative(spriteDefinition.bottomY(), "spriteDefinition.bottomY()");

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

    private void throwOnNegative(int value, String parameterName) {
        if (value < 0) {
            throw new IllegalArgumentException("SpriteFactory.create: " + parameterName +
                    " (" + value + ") cannot be negative");
        }
    }

    private void throwOnZeroOrLess(int value, String parameterName) {
        if (value <= 0) {
            throw new IllegalArgumentException("SpriteFactory.create: " + parameterName +
                    " (" + value + ") cannot be 0 or less");
        }
    }

    private void throwOnSecondLte(int first, int second,
                                  String firstParamName, String secondParamName) {
        if (second <= first) {
            throw new IllegalArgumentException("SpriteFactory.create: " + secondParamName +
                    " (" + second + ") cannot be less than or equal to " + firstParamName + " (" +
                    first + ")");
        }
    }

    private void throwOnSecondGt(int first, int second,
                                  String firstParamName, String secondParamName) {
        if (second > first) {
            throw new IllegalArgumentException("SpriteFactory.create: " + secondParamName +
                    " (" + second + ") cannot be greater than " + firstParamName + " (" + first +
                    ")");
        }
    }

    @Override
    public String getInterfaceName() {
        return AssetFactory.class.getCanonicalName() + "<" +
                SpriteDefinition.class.getCanonicalName() + "," + Sprite.class.getCanonicalName() +
                ">";
    }
}
