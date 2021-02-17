package inaugural.soliloquy.graphics.bootstrap;

import inaugural.soliloquy.graphics.api.dto.AnimationDTO;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.infrastructure.Registry;
import soliloquy.specs.graphics.assets.Animation;
import soliloquy.specs.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.graphics.assets.Image;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.AnimationDefinition;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.SpriteSetDefinition;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.SpriteSetSnippetDefinition;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AnimationPreloaderWorker {
    private final java.util.Map<String, Image> IMAGES;
    private final AssetFactory<AnimationDefinition, Animation> FACTORY;
    private final Registry<Animation> REGISTRY;

    public AnimationPreloaderWorker(java.util.Map<String, Image> images,
                                    AssetFactory<AnimationDefinition, Animation> factory,
                                    Registry<Animation> registry) {
        IMAGES = Check.ifNull(images, "images");
        FACTORY = Check.ifNull(factory, "factory");
        REGISTRY = Check.ifNull(registry, "registry");
    }

    @SuppressWarnings("ConstantConditions")
    public void load(Collection<AnimationDTO> animationDTOs) {
        Check.ifNull(animationDTOs, "animationDTOs");
        animationDTOs.forEach(dto -> REGISTRY.add(FACTORY.create(makeDefinition(dto))));
    }

    private AnimationDefinition makeDefinition(AnimationDTO animationDTO) {
        Map<Integer,AnimationFrameSnippet> snippetDefinitions = new HashMap<>();
        for(AnimationDTO.AnimationFrameDTO frameSnippetDTO : animationDTO.frames) {
            snippetDefinitions.put(frameSnippetDTO.ms, new AnimationFrameSnippet() {
                @Override
                public Image image() {
                    return IMAGES.get(frameSnippetDTO.imgLoc);
                }

                @Override
                public int leftX() {
                    return frameSnippetDTO.leftX;
                }

                @Override
                public int topY() {
                    return frameSnippetDTO.topY;
                }

                @Override
                public int rightX() {
                    return frameSnippetDTO.rightX;
                }

                @Override
                public int bottomY() {
                    return frameSnippetDTO.bottomY;
                }

                @Override
                public float offsetX() {
                    return frameSnippetDTO.offsetX;
                }

                @Override
                public float offsetY() {
                    return frameSnippetDTO.offsetY;
                }

                @Override
                public String getInterfaceName() {
                    return AnimationFrameSnippet.class.getCanonicalName();
                }
            });
        }

        return new AnimationDefinition() {
            @Override
            public int msDuration() {
                return animationDTO.msDur;
            }

            @Override
            public Map<Integer, AnimationFrameSnippet> frameSnippetDefinitions() {
                return snippetDefinitions;
            }

            @Override
            public String assetId() {
                return animationDTO.id;
            }

            @Override
            public String getInterfaceName() {
                return AnimationDefinition.class.getCanonicalName();
            }
        };
    }
}
