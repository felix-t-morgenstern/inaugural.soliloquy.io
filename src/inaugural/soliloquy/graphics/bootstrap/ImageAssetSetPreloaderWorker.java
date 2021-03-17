package inaugural.soliloquy.graphics.bootstrap;

import inaugural.soliloquy.graphics.api.dto.ImageAssetSetDTO;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.infrastructure.Registry;
import soliloquy.specs.graphics.assets.ImageAsset;
import soliloquy.specs.graphics.assets.ImageAssetSet;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageAssetSetAssetDefinition;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageAssetSetDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ImageAssetSetPreloaderWorker {
    private final AssetFactory<ImageAssetSetDefinition, ImageAssetSet> FACTORY;
    private final Registry<ImageAssetSet> REGISTRY;

    public ImageAssetSetPreloaderWorker(AssetFactory<ImageAssetSetDefinition, ImageAssetSet>
                                                factory,
                                        Registry<ImageAssetSet> registry) {
        FACTORY = Check.ifNull(factory, "factory");
        REGISTRY = Check.ifNull(registry, "registry");
    }

    @SuppressWarnings("ConstantConditions")
    public void load(Collection<ImageAssetSetDTO> imageAssetSetDTOs) {
        Check.ifNull(imageAssetSetDTOs, "imageAssetSetDTOs");
        imageAssetSetDTOs.forEach(dto -> REGISTRY.add(FACTORY.make(makeDefinition(dto))));
    }

    private ImageAssetSetDefinition makeDefinition(ImageAssetSetDTO imageAssetSetDTO) {
        List<ImageAssetSetAssetDefinition> assetDefinitions = new ArrayList<>();
        for(ImageAssetSetDTO.ImageAssetSetAssetDTO assetDTO : imageAssetSetDTO.assets) {
            assetDefinitions.add(new ImageAssetSetAssetDefinition() {
                @Override
                public String type() {
                    return assetDTO.type;
                }

                @Override
                public String direction() {
                    return assetDTO.direction;
                }

                @Override
                public ImageAsset.ImageAssetType assetType() {
                    return ImageAsset.ImageAssetType.getFromValue(assetDTO.assetType);
                }

                @Override
                public String assetId() {
                    return assetDTO.assetId;
                }

                @Override
                public String getInterfaceName() {
                    return ImageAssetSetAssetDefinition.class.getCanonicalName();
                }
            });
        }

        return new ImageAssetSetDefinition() {
            @Override
            public List<ImageAssetSetAssetDefinition> assetDefinitions() {
                return assetDefinitions;
            }

            @Override
            public String assetId() {
                return imageAssetSetDTO.id;
            }

            @Override
            public String getInterfaceName() {
                return ImageAssetSetDefinition.class.getCanonicalName();
            }
        };
    }
}
