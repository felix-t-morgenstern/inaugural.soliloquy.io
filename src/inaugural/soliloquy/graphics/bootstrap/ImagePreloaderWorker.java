package inaugural.soliloquy.graphics.bootstrap;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.assets.Image;
import soliloquy.specs.graphics.bootstrap.assetfactories.ImageFactory;

import java.util.function.Consumer;

public class ImagePreloaderWorker {
    private final ImageFactory IMAGE_FACTORY;
    private final String RELATIVE_LOCATION;
    private final boolean SUPPORTS_MOUSE_EVENT_CAPTURING;
    private final Consumer<Image> ADD_LOADED_IMAGE;

    public ImagePreloaderWorker(ImageFactory imageFactory,
                                String relativeLocation,
                                boolean supportsMouseEventCapturing,
                                Consumer<Image> addLoadedImage) {
        IMAGE_FACTORY = Check.ifNull(imageFactory, "imageFactory");
        RELATIVE_LOCATION = Check.ifNullOrEmpty(relativeLocation, "relativeLocation");
        SUPPORTS_MOUSE_EVENT_CAPTURING = supportsMouseEventCapturing;
        ADD_LOADED_IMAGE = Check.ifNull(addLoadedImage, "addLoadedImage");
    }

    public void load() {
        ADD_LOADED_IMAGE.accept(
                IMAGE_FACTORY.make(RELATIVE_LOCATION, SUPPORTS_MOUSE_EVENT_CAPTURING));
    }
}
