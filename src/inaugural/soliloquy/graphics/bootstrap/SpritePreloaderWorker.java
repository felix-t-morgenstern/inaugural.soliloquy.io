package inaugural.soliloquy.graphics.bootstrap;

import inaugural.soliloquy.graphics.api.dto.SpriteDTO;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.infrastructure.Registry;
import soliloquy.specs.graphics.assets.Image;
import soliloquy.specs.graphics.assets.Sprite;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.SpriteDefinition;

import java.util.Collection;

public class SpritePreloaderWorker {
    private final java.util.Map<String, Image> IMAGES;
    private final AssetFactory<SpriteDefinition, Sprite> FACTORY;
    private final Registry<Sprite> REGISTRY;

    public SpritePreloaderWorker(java.util.Map<String, Image> images,
                                 AssetFactory<SpriteDefinition, Sprite> factory,
                                 Registry<Sprite> registry) {
        IMAGES = Check.ifNull(images, "images");
        FACTORY = Check.ifNull(factory, "factory");
        REGISTRY = Check.ifNull(registry, "registry");
    }

    @SuppressWarnings("ConstantConditions")
    public void run(Collection<SpriteDTO> spriteDTOs) {
        Check.ifNull(spriteDTOs, "spriteDTOs");
        spriteDTOs.forEach(dto -> REGISTRY.add(FACTORY.make(
                new SpriteDefinition() {
                    @Override
                    public Image image() {
                        return IMAGES.get(dto.imgLoc);
                    }

                    @Override
                    public int leftX() {
                        return dto.leftX;
                    }

                    @Override
                    public int topY() {
                        return dto.topY;
                    }

                    @Override
                    public int rightX() {
                        return dto.rightX;
                    }

                    @Override
                    public int bottomY() {
                        return dto.bottomY;
                    }

                    @Override
                    public String id() {
                        return dto.id;
                    }

                    @Override
                    public String getInterfaceName() {
                        return SpriteDefinition.class.getCanonicalName();
                    }
                })
        ));
    }
}
