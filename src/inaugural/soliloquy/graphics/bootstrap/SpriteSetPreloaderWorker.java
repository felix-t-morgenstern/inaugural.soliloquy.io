package inaugural.soliloquy.graphics.bootstrap;

import inaugural.soliloquy.graphics.api.dto.SpriteSetDTO;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.infrastructure.Registry;
import soliloquy.specs.graphics.assets.Image;
import soliloquy.specs.graphics.assets.SpriteSet;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.SpriteSetDefinition;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.SpriteSetSnippetDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SpriteSetPreloaderWorker {
    private final java.util.Map<String, Image> IMAGES;
    private final AssetFactory<SpriteSetDefinition, SpriteSet> FACTORY;
    private final Registry<SpriteSet> REGISTRY;

    public SpriteSetPreloaderWorker(java.util.Map<String, Image> images,
                                    AssetFactory<SpriteSetDefinition, SpriteSet> factory,
                                    Registry<SpriteSet> registry) {
        IMAGES = Check.ifNull(images, "images");
        FACTORY = Check.ifNull(factory, "factory");
        REGISTRY = Check.ifNull(registry, "registry");
    }

    @SuppressWarnings("ConstantConditions")
    public void load(Collection<SpriteSetDTO> spriteSetDTOs) {
        Check.ifNull(spriteSetDTOs, "spriteSetDTOs");
        spriteSetDTOs.forEach(dto -> REGISTRY.add(FACTORY.create(makeDefinition(dto))));
    }

    private SpriteSetDefinition makeDefinition(SpriteSetDTO spriteSetDTO) {
        List<SpriteSetSnippetDefinition> snippetDefinitions = new ArrayList<>();
        for(SpriteSetDTO.SpriteSetSnippetDTO spriteSetSnippetDTO : spriteSetDTO.snippets) {
            snippetDefinitions.add(new SpriteSetSnippetDefinition() {
                @Override
                public String type() {
                    return spriteSetSnippetDTO.type;
                }

                @Override
                public String direction() {
                    return spriteSetSnippetDTO.direction;
                }

                @Override
                public Image image() {
                    return IMAGES.get(spriteSetSnippetDTO.imgLoc);
                }

                @Override
                public int leftX() {
                    return spriteSetSnippetDTO.leftX;
                }

                @Override
                public int topY() {
                    return spriteSetSnippetDTO.topY;
                }

                @Override
                public int rightX() {
                    return spriteSetSnippetDTO.rightX;
                }

                @Override
                public int bottomY() {
                    return spriteSetSnippetDTO.bottomY;
                }

                @Override
                public String getInterfaceName() {
                    return SpriteSetSnippetDefinition.class.getCanonicalName();
                }
            });
        }

        return new SpriteSetDefinition() {
            @Override
            public List<SpriteSetSnippetDefinition> snippetDefinitions() {
                return snippetDefinitions;
            }

            @Override
            public String assetId() {
                return spriteSetDTO.id;
            }

            @Override
            public String getInterfaceName() {
                return SpriteSetDefinition.class.getCanonicalName();
            }
        };
    }
}
