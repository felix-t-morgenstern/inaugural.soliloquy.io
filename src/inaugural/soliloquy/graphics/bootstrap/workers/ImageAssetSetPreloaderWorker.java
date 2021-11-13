package inaugural.soliloquy.graphics.bootstrap.workers;

import inaugural.soliloquy.graphics.api.dto.ImageAssetSetDefinitionDTO;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.assets.ImageAsset;
import soliloquy.specs.graphics.assets.ImageAssetSet;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageAssetSetAssetDefinition;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageAssetSetDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class ImageAssetSetPreloaderWorker implements Runnable {
    private final AssetFactory<ImageAssetSetDefinition, ImageAssetSet> FACTORY;
    private final Collection<ImageAssetSetDefinitionDTO> IMAGE_ASSET_SET_DEFINITION_DTOS;
    private final Consumer<ImageAssetSet> PROCESS_RESULT;

    /** @noinspection ConstantConditions*/
    public ImageAssetSetPreloaderWorker(AssetFactory<ImageAssetSetDefinition, ImageAssetSet>
                                                factory,
                                        Collection<ImageAssetSetDefinitionDTO>
                                                imageAssetSetDefinitionDTOs,
                                        Consumer<ImageAssetSet> processResult) {
        FACTORY = Check.ifNull(factory, "factory");
        Check.ifNull(imageAssetSetDefinitionDTOs, "imageAssetSetDefinitionDTOs");
        if (imageAssetSetDefinitionDTOs.isEmpty()) {
            throw new IllegalArgumentException(
                    "ImageAssetSetPreloaderWorker: imageAssetSetDefinitionDTOs is empty");
        }
        imageAssetSetDefinitionDTOs.forEach(imageAssetSetDefinitionDTO -> {
            Check.ifNull(imageAssetSetDefinitionDTO,
                    "imageAssetSetDefinitionDTO within imageAssetSetDefinitionDTOs");
            Check.ifNullOrEmpty(imageAssetSetDefinitionDTO.id,
                    "imageAssetSetDefinitionDTO.id within imageAssetSetDefinitionDTOs");
            Check.ifNull(imageAssetSetDefinitionDTO.assets,
                    "imageAssetSetDefinitionDTO.assets within imageAssetSetDefinitionDTOs (" +
                    imageAssetSetDefinitionDTO.id + ")");
            if (imageAssetSetDefinitionDTO.assets.length == 0) {
                throw new IllegalArgumentException("ImageAssetSetPreloaderWorker: " +
                        "imageAssetSetDefinitionDTO.assets within imageAssetSetDefinitionDTOs (" +
                        imageAssetSetDefinitionDTO.id + ") is empty");
            }
            for(ImageAssetSetDefinitionDTO.ImageAssetSetAssetDTO imageAssetSetAssetDTO :
                    imageAssetSetDefinitionDTO.assets) {
                Check.ifNullOrEmpty(imageAssetSetAssetDTO.assetId,
                        "imageAssetSetAssetDTO.assetId within imageAssetSetDefinitionDTOs (" +
                        imageAssetSetDefinitionDTO.id + ")");
                if (imageAssetSetAssetDTO.assetType < 1 || imageAssetSetAssetDTO.assetType > 3) {
                    throw new IllegalArgumentException("ImageAssetSetPreloaderWorker: " +
                            "imageAssetSetDefinitionDTO.asset has illegal assetType (" +
                            imageAssetSetAssetDTO.assetType + "), (id = " +
                            imageAssetSetDefinitionDTO.id + ")");
                }
            }
        });
        IMAGE_ASSET_SET_DEFINITION_DTOS = imageAssetSetDefinitionDTOs;
        PROCESS_RESULT = Check.ifNull(processResult, "processResult");
    }

    public void run() {
        IMAGE_ASSET_SET_DEFINITION_DTOS.forEach(dto ->
                PROCESS_RESULT.accept(FACTORY.make(makeDefinition(dto))));
    }

    private ImageAssetSetDefinition makeDefinition(ImageAssetSetDefinitionDTO
                                                           imageAssetSetDefinitionDTO) {
        List<ImageAssetSetAssetDefinition> assetDefinitions = new ArrayList<>();
        for(ImageAssetSetDefinitionDTO.ImageAssetSetAssetDTO assetDTO :
                imageAssetSetDefinitionDTO.assets) {
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
            public String id() {
                return imageAssetSetDefinitionDTO.id;
            }

            @Override
            public String getInterfaceName() {
                return ImageAssetSetDefinition.class.getCanonicalName();
            }
        };
    }
}
