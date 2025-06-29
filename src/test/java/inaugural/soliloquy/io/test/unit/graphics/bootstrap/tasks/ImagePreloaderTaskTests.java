package inaugural.soliloquy.io.test.unit.graphics.bootstrap.tasks;

import inaugural.soliloquy.io.api.dto.ImageDefinitionDTO;
import inaugural.soliloquy.io.graphics.bootstrap.tasks.ImagePreloaderTask;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeImageFactory;
import org.junit.jupiter.api.Test;
import soliloquy.specs.io.graphics.assets.Image;

import java.util.List;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static org.junit.jupiter.api.Assertions.*;

public class ImagePreloaderTaskTests {
    private final FakeImageFactory IMAGE_FACTORY = new FakeImageFactory();
    private final String RELATIVE_LOCATION = "relativeLocation";

    @Test
    public void testConstructorWithInvalidArgs() {
        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        List<Image> dummyList = listOf();

        assertThrows(IllegalArgumentException.class,
                () -> new ImagePreloaderTask(null, IMAGE_FACTORY, dummyList::add));
        assertThrows(IllegalArgumentException.class,
                () -> new ImagePreloaderTask(
                        listOf(new ImageDefinitionDTO(RELATIVE_LOCATION, true)),
                        null,
                        dummyList::add));
        assertThrows(IllegalArgumentException.class,
                () -> new ImagePreloaderTask(
                        listOf(new ImageDefinitionDTO(RELATIVE_LOCATION, true)),
                        IMAGE_FACTORY, null));
    }

    @Test
    public void testRun() {
        List<Image> images = listOf();

        ImagePreloaderTask worker = new ImagePreloaderTask(
                listOf(new ImageDefinitionDTO(RELATIVE_LOCATION, true)),
                IMAGE_FACTORY, images::add);

        worker.run();

        assertEquals(IMAGE_FACTORY.RelativeLocations.getFirst(), RELATIVE_LOCATION);
        assertTrue(IMAGE_FACTORY.SupportsEventCapturingValues.getFirst());
        assertSame(images.getFirst(), IMAGE_FACTORY.Outputs.getFirst());
    }
}
