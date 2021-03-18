package inaugural.soliloquy.graphics.test.unit.bootstrap;

import inaugural.soliloquy.graphics.bootstrap.ImagePreloaderWorker;
import inaugural.soliloquy.graphics.test.fakes.FakeImageFactory;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.assets.Image;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ImagePreloaderWorkerTests {
    private final FakeImageFactory IMAGE_FACTORY = new FakeImageFactory();
    private final String RELATIVE_LOCATION = "relativeLocation";

    @Test
    void testConstructorWithInvalidParams() {
        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        ArrayList<Image> dummyList = new ArrayList<>();

        assertThrows(IllegalArgumentException.class,
                () -> new ImagePreloaderWorker(null, RELATIVE_LOCATION,
                        true, dummyList::add));
        assertThrows(IllegalArgumentException.class,
                () -> new ImagePreloaderWorker(IMAGE_FACTORY, null,
                        true, dummyList::add));
        assertThrows(IllegalArgumentException.class,
                () -> new ImagePreloaderWorker(IMAGE_FACTORY, "",
                        true, dummyList::add));
        assertThrows(IllegalArgumentException.class,
                () -> new ImagePreloaderWorker(IMAGE_FACTORY, RELATIVE_LOCATION,
                        true, null));
    }

    @Test
    void testLoad() {
        ArrayList<Image> images = new ArrayList<>();

        ImagePreloaderWorker worker = new ImagePreloaderWorker(IMAGE_FACTORY, RELATIVE_LOCATION,
                true, images::add);

        worker.load();

        assertEquals(IMAGE_FACTORY.RelativeLocations.get(0), RELATIVE_LOCATION);
        assertTrue(IMAGE_FACTORY.SupportsEventCapturingValues.get(0));
        assertSame(images.get(0), IMAGE_FACTORY.Outputs.get(0));
    }
}
