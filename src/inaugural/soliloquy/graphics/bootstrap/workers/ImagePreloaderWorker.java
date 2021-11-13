package inaugural.soliloquy.graphics.bootstrap.workers;

import inaugural.soliloquy.graphics.api.dto.ImageDefinitionDTO;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.assets.Image;
import soliloquy.specs.graphics.bootstrap.assetfactories.ImageFactory;

import java.util.function.Consumer;

public class ImagePreloaderWorker implements Runnable {
    private final ImageFactory IMAGE_FACTORY;
    private final String RELATIVE_LOCATION;
    private final boolean SUPPORTS_MOUSE_EVENT_CAPTURING;
    private final Consumer<Image> ADD_LOADED_IMAGE;

    /** @noinspection ConstantConditions*/
    public ImagePreloaderWorker(ImageFactory imageFactory,
                                ImageDefinitionDTO imageDefinitionDTO,
                                Consumer<Image> addLoadedImage) {
        IMAGE_FACTORY = Check.ifNull(imageFactory, "imageFactory");
        Check.ifNull(imageDefinitionDTO, "imageDefinitionDTO");
        RELATIVE_LOCATION = Check.ifNullOrEmpty(imageDefinitionDTO.RelativeLocation,
                "imageDefinitionDTO.RelativeLocation");
        SUPPORTS_MOUSE_EVENT_CAPTURING = imageDefinitionDTO.SupportsMouseEvents;
        ADD_LOADED_IMAGE = Check.ifNull(addLoadedImage, "addLoadedImage");
    }

    @Override
    public void run() {
        ADD_LOADED_IMAGE.accept(
                IMAGE_FACTORY.make(RELATIVE_LOCATION, SUPPORTS_MOUSE_EVENT_CAPTURING));
    }
}
