package inaugural.soliloquy.graphics.bootstrap;

import inaugural.soliloquy.graphics.assets.ImageImpl;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.assets.Image;

import java.util.function.Consumer;

public class ImagePreloaderWorker {
    private final String RELATIVE_LOCATION;
    private final boolean SUPPORTS_MOUSE_EVENT_CAPTURING;
    private final float MOUSE_EVENT_CAPTURING_ALPHA_THRESHOLD;
    private final Consumer<Image> ADD_LOADED_IMAGE;

    public ImagePreloaderWorker(String relativeLocation,
                                Consumer<Image> addLoadedImage) {
        RELATIVE_LOCATION = Check.ifNullOrEmpty(relativeLocation, "relativeLocation");
        SUPPORTS_MOUSE_EVENT_CAPTURING = false;
        MOUSE_EVENT_CAPTURING_ALPHA_THRESHOLD = 0f;
        ADD_LOADED_IMAGE = Check.ifNull(addLoadedImage, "addLoadedImage");
    }

    // NB: If and only if mouseEventCapturingAlphaThreshold is defined, then we assume that mouse
    // event capturing is enabled
    public ImagePreloaderWorker(String relativeLocation,
                                float mouseEventCapturingAlphaThreshold,
                                Consumer<Image> addLoadedImage) {
        RELATIVE_LOCATION = Check.ifNullOrEmpty(relativeLocation, "relativeLocation");
        SUPPORTS_MOUSE_EVENT_CAPTURING = true;
        MOUSE_EVENT_CAPTURING_ALPHA_THRESHOLD = mouseEventCapturingAlphaThreshold;
        ADD_LOADED_IMAGE = Check.ifNull(addLoadedImage, "addLoadedImage");
    }

    public void load() {
        ADD_LOADED_IMAGE.accept(SUPPORTS_MOUSE_EVENT_CAPTURING ?
                new ImageImpl(RELATIVE_LOCATION, MOUSE_EVENT_CAPTURING_ALPHA_THRESHOLD) :
                new ImageImpl(RELATIVE_LOCATION));
    }
}
