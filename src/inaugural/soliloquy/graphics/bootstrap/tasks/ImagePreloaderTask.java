package inaugural.soliloquy.graphics.bootstrap.tasks;

import inaugural.soliloquy.graphics.api.dto.ImageDefinitionDTO;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.assets.Image;
import soliloquy.specs.graphics.bootstrap.assetfactories.ImageFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageDefinition;

import java.util.Collection;
import java.util.function.Consumer;

public class ImagePreloaderTask implements Runnable {
    private final ImageFactory FACTORY;
    private final Collection<ImageDefinitionDTO> IMAGE_DEFINITION_DTOS;
    private final Consumer<Image> ADD_LOADED_IMAGE;

    public ImagePreloaderTask(Collection<ImageDefinitionDTO> imageDefinitionDTOs,
                              ImageFactory factory,
                              Consumer<Image> processResult) {
        FACTORY = Check.ifNull(factory, "factory");
        Check.ifNull(imageDefinitionDTOs, "imageDefinitionDTOs");
        IMAGE_DEFINITION_DTOS = Check.ifNull(imageDefinitionDTOs, "imageDefinitionDTOs");
        ADD_LOADED_IMAGE = Check.ifNull(processResult, "processResult");
    }

    @Override
    public void run() {
        IMAGE_DEFINITION_DTOS.forEach(dto ->
                ADD_LOADED_IMAGE.accept(
                        FACTORY.make(new ImageDefinition(
                                dto.RelativeLocation,
                                dto.SupportsMouseEvents
                        ))));
    }
}
