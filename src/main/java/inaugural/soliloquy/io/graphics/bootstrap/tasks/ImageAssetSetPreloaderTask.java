package inaugural.soliloquy.io.graphics.bootstrap.tasks;

import inaugural.soliloquy.io.api.dto.ImageAssetSetDefinitionDTO;
import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.collections.Collections;
import soliloquy.specs.io.graphics.assets.ImageAssetSet;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.ImageAssetSetAssetDefinition;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.ImageAssetSetDefinition;

import java.util.Collection;
import java.util.function.Consumer;

import static soliloquy.specs.io.graphics.assets.ImageAsset.ImageAssetType;
import static soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.ImageAssetSetAssetDefinition.DisplayParam;

public class ImageAssetSetPreloaderTask implements Runnable {
    private final AssetFactory<ImageAssetSetDefinition, ImageAssetSet> FACTORY;
    private final Collection<ImageAssetSetDefinitionDTO> IMAGE_ASSET_SET_DEFINITION_DTOS;
    private final Consumer<ImageAssetSet> PROCESS_RESULT;

    public ImageAssetSetPreloaderTask(Collection<ImageAssetSetDefinitionDTO>
                                              imageAssetSetDefinitionDTOs,
                                      AssetFactory<ImageAssetSetDefinition, ImageAssetSet>
                                              factory,
                                      Consumer<ImageAssetSet> processResult) {
        FACTORY = Check.ifNull(factory, "factory");
        Check.ifNull(imageAssetSetDefinitionDTOs, "imageAssetSetDefinitionDTOs");
        if (imageAssetSetDefinitionDTOs.isEmpty()) {
            throw new IllegalArgumentException(
                    "ImageAssetSetPreloaderTask: imageAssetSetDefinitionDTOs is empty");
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
                throw new IllegalArgumentException("ImageAssetSetPreloaderTask: " +
                        "imageAssetSetDefinitionDTO.assets within imageAssetSetDefinitionDTOs (" +
                        imageAssetSetDefinitionDTO.id + ") is empty");
            }
            for (var imageAssetSetAssetDTO : imageAssetSetDefinitionDTO.assets) {
                Check.ifNullOrEmpty(imageAssetSetAssetDTO.assetId,
                        "imageAssetSetAssetDTO.assetId within imageAssetSetDefinitionDTOs (" +
                                imageAssetSetDefinitionDTO.id + ")");
                if (imageAssetSetAssetDTO.assetType < 1 || imageAssetSetAssetDTO.assetType > 3) {
                    throw new IllegalArgumentException("ImageAssetSetPreloaderTask: " +
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
        var assetDefinitions = Collections.<ImageAssetSetAssetDefinition>listOf();
        for (var assetDTO : imageAssetSetDefinitionDTO.assets) {
            var displayParams = new DisplayParam[assetDTO.displayParams.length];
            for (var i = 0; i < assetDTO.displayParams.length; i++) {
                displayParams[i] = new DisplayParam(assetDTO.displayParams[i].name,
                        assetDTO.displayParams[i].val);
            }

            assetDefinitions.add(new ImageAssetSetAssetDefinition(
                    ImageAssetType.getFromValue(assetDTO.assetType),
                    assetDTO.assetId,
                    displayParams
            ));
        }

        return new ImageAssetSetDefinition(
                imageAssetSetDefinitionDTO.id,
                assetDefinitions
        );
    }
}
