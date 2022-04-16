package inaugural.soliloquy.graphics.test.unit.bootstrap.tasks;

import inaugural.soliloquy.graphics.api.dto.MouseCursorImageDTO;
import inaugural.soliloquy.graphics.bootstrap.tasks.MouseCursorImagePreloaderTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class MouseCursorImagePreloaderTaskTests {
    private final String MOUSE_CURSOR_IMAGE_RELATIVE_LOCATION =
            "./res/images/mouse_cursors/cursor_green_default.png";
    private final int HOTSPOT_X = 12;
    private final int HOTSPOT_Y = 34;

    private HashMap<String, Long> _mouseCursorImages;

    @BeforeEach
    void setUp() {
        _mouseCursorImages = new HashMap<>();
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> new MouseCursorImagePreloaderTask(
                null,
                relativeLocation -> mouseCursorImage ->
                        _mouseCursorImages.put(relativeLocation, mouseCursorImage)));
        assertThrows(IllegalArgumentException.class, () -> new MouseCursorImagePreloaderTask(
                new MouseCursorImageDTO(null, HOTSPOT_X, HOTSPOT_Y),
                relativeLocation -> mouseCursorImage ->
                        _mouseCursorImages.put(relativeLocation, mouseCursorImage)));
        assertThrows(IllegalArgumentException.class, () -> new MouseCursorImagePreloaderTask(
                new MouseCursorImageDTO("", HOTSPOT_X, HOTSPOT_Y),
                relativeLocation -> mouseCursorImage ->
                        _mouseCursorImages.put(relativeLocation, mouseCursorImage)));
        assertThrows(IllegalArgumentException.class, () -> new MouseCursorImagePreloaderTask(
                new MouseCursorImageDTO(MOUSE_CURSOR_IMAGE_RELATIVE_LOCATION, -1, HOTSPOT_Y),
                relativeLocation -> mouseCursorImage ->
                        _mouseCursorImages.put(relativeLocation, mouseCursorImage)));
        assertThrows(IllegalArgumentException.class, () -> new MouseCursorImagePreloaderTask(
                new MouseCursorImageDTO(MOUSE_CURSOR_IMAGE_RELATIVE_LOCATION, HOTSPOT_X, -1),
                relativeLocation -> mouseCursorImage ->
                        _mouseCursorImages.put(relativeLocation, mouseCursorImage)));
        assertThrows(IllegalArgumentException.class, () -> new MouseCursorImagePreloaderTask(
                new MouseCursorImageDTO(MOUSE_CURSOR_IMAGE_RELATIVE_LOCATION, HOTSPOT_X,
                        HOTSPOT_Y),
                null));
    }

    @Test
    void testRun() {
        new MouseCursorImagePreloaderTask(
                new MouseCursorImageDTO(MOUSE_CURSOR_IMAGE_RELATIVE_LOCATION, HOTSPOT_X,
                        HOTSPOT_Y),
                relativeLocation -> mouseCursorImage ->
                        _mouseCursorImages.put(relativeLocation, mouseCursorImage))
                .run();

        assertEquals(1, _mouseCursorImages.size());
        assertTrue(_mouseCursorImages.containsKey(MOUSE_CURSOR_IMAGE_RELATIVE_LOCATION));
        assertNotNull(_mouseCursorImages.get(MOUSE_CURSOR_IMAGE_RELATIVE_LOCATION));
    }

    @Test
    void testRunWithHotspotsOutOfBounds() {
        MouseCursorImagePreloaderTask hotspotXOutOfBounds = new MouseCursorImagePreloaderTask(
                new MouseCursorImageDTO(MOUSE_CURSOR_IMAGE_RELATIVE_LOCATION, 128, HOTSPOT_Y),
                        relativeLocation -> mouseCursorImage ->
                                _mouseCursorImages.put(relativeLocation, mouseCursorImage));

        MouseCursorImagePreloaderTask hotspotYOutOfBounds = new MouseCursorImagePreloaderTask(
                new MouseCursorImageDTO(MOUSE_CURSOR_IMAGE_RELATIVE_LOCATION, HOTSPOT_X, 126),
                        relativeLocation -> mouseCursorImage ->
                                _mouseCursorImages.put(relativeLocation, mouseCursorImage));

        assertThrows(IllegalStateException.class, hotspotXOutOfBounds::run);
        assertThrows(IllegalStateException.class, hotspotYOutOfBounds::run);
    }
}
