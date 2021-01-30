package inaugural.soliloquy.graphics.bootstrap.assetfactories;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.assets.AssetSnippet;
import soliloquy.specs.graphics.assets.Image;
import soliloquy.specs.graphics.assets.SpriteSet;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.SpriteSetDefinition;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.SpriteSetSnippetDefinition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static inaugural.soliloquy.tools.Check.*;
import static inaugural.soliloquy.tools.Tools.emptyIfNull;

public class SpriteSetFactory implements AssetFactory<SpriteSetDefinition, SpriteSet> {
    @Override
    public SpriteSet create(SpriteSetDefinition spriteSetDefinition)
            throws IllegalArgumentException {
        Check.ifNull(spriteSetDefinition, "spriteSetDefinition");

        Check.ifNull(spriteSetDefinition.snippetDefinitions(),
                "spriteSetDefinition.snippetDefinitions()");
        if (spriteSetDefinition.snippetDefinitions().isEmpty()) {
            throw new IllegalArgumentException("SpriteSetFactory.create: " +
                    "spriteSetDefinition.snippetDefinitions() cannot be empty");
        }

        Check.ifNullOrEmpty(spriteSetDefinition.assetId(), "spriteSetDefinition.assetId()");

        return new SpriteSetImpl(spriteSetDefinition.snippetDefinitions(),
                spriteSetDefinition.assetId());
    }

    @Override
    public String getInterfaceName() {
        return AssetFactory.class.getCanonicalName() + "<" +
                SpriteSetDefinition.class.getCanonicalName() + "," +
                SpriteSet.class.getCanonicalName() + ">";
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    class SpriteSetImpl implements SpriteSet {
        private final Map<String, Map<String, AssetSnippet>> SNIPPETS_BY_TYPE_AND_DIRECTION;
        private final String ID;

        public SpriteSetImpl(List<SpriteSetSnippetDefinition> spriteSetSnippetDefinitions,
                             String id) {
            SNIPPETS_BY_TYPE_AND_DIRECTION = new HashMap<>();
            ID = id;

            spriteSetSnippetDefinitions.forEach(spriteSetSnippetDefinition -> {
                String type = emptyIfNull(spriteSetSnippetDefinition.type());
                String direction = emptyIfNull(spriteSetSnippetDefinition.direction());

                throwOnLteZero(spriteSetSnippetDefinition.image().width(),
                        "spriteSetSnippetDefinition.image().width()");
                throwOnLteZero(spriteSetSnippetDefinition.image().height(),
                        "spriteSetSnippetDefinition.image().height()");

                Check.ifNonNegative(spriteSetSnippetDefinition.leftX(),
                        "spriteDefinition.leftX()");
                Check.ifNonNegative(spriteSetSnippetDefinition.topY(),
                        "spriteDefinition.topY()");
                Check.ifNonNegative(spriteSetSnippetDefinition.rightX(),
                        "spriteDefinition.rightX()");
                Check.ifNonNegative(spriteSetSnippetDefinition.bottomY(),
                        "spriteDefinition.bottomY()");

                throwOnSecondLte(spriteSetSnippetDefinition.leftX(),
                        spriteSetSnippetDefinition.rightX(),
                        "spriteSetSnippetDefinition.leftX()",
                        "spriteSetSnippetDefinition.rightX()");
                throwOnSecondLte(spriteSetSnippetDefinition.topY(),
                        spriteSetSnippetDefinition.bottomY(),
                        "spriteSetSnippetDefinition.topY()",
                        "spriteSetSnippetDefinition.bottomY()");

                throwOnSecondGt(spriteSetSnippetDefinition.image().width(),
                        spriteSetSnippetDefinition.leftX(),
                        "spriteSetSnippetDefinition.image().width()",
                        "spriteSetSnippetDefinition.leftX()");
                throwOnSecondGt(spriteSetSnippetDefinition.image().height(),
                        spriteSetSnippetDefinition.topY(),
                        "spriteSetSnippetDefinition.image().height()",
                        "spriteSetSnippetDefinition.topY()");
                throwOnSecondGt(spriteSetSnippetDefinition.image().width(),
                        spriteSetSnippetDefinition.rightX(),
                        "spriteSetSnippetDefinition.image().width()",
                        "spriteSetSnippetDefinition.rightX()");
                throwOnSecondGt(spriteSetSnippetDefinition.image().height(),
                        spriteSetSnippetDefinition.bottomY(),
                        "spriteSetSnippetDefinition.image().height()",
                        "spriteSetSnippetDefinition.bottomY()");

                if (!SNIPPETS_BY_TYPE_AND_DIRECTION.containsKey(type)) {
                    SNIPPETS_BY_TYPE_AND_DIRECTION.put(type, new HashMap<>());
                }

                if (SNIPPETS_BY_TYPE_AND_DIRECTION.get(type).containsKey(direction)) {
                    throw new IllegalArgumentException("SpriteSetImpl: definition cannot " +
                            "contain duplicate snippets with identical type and direction");
                }

                SNIPPETS_BY_TYPE_AND_DIRECTION.get(type).put(direction,
                        new AssetSnippet() {
                            @Override
                            public Image image() {
                                return spriteSetSnippetDefinition.image();
                            }

                            @Override
                            public int leftX() {
                                return spriteSetSnippetDefinition.leftX();
                            }

                            @Override
                            public int topY() {
                                return spriteSetSnippetDefinition.topY();
                            }

                            @Override
                            public int rightX() {
                                return spriteSetSnippetDefinition.rightX();
                            }

                            @Override
                            public int bottomY() {
                                return spriteSetSnippetDefinition.bottomY();
                            }

                            @Override
                            public String getInterfaceName() {
                                return AssetSnippet.class.getCanonicalName();
                            }
                        });
            });
        }

        @Override
        public AssetSnippet getImageAndBoundariesForTypeAndDirection(String type,
                                                                     String direction)
                throws IllegalArgumentException {
            type = emptyIfNull(type);
            direction = emptyIfNull(direction);
            return SNIPPETS_BY_TYPE_AND_DIRECTION.get(type).get(direction);
        }

        @Override
        public String id() throws IllegalStateException {
            return ID;
        }

        @Override
        public String getInterfaceName() {
            return SpriteSet.class.getCanonicalName();
        }
    }
}
