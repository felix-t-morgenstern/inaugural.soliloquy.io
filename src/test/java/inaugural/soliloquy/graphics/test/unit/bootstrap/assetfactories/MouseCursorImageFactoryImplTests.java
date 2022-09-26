package inaugural.soliloquy.graphics.test.unit.bootstrap.assetfactories;

import inaugural.soliloquy.graphics.bootstrap.assetfactories.MouseCursorImageFactoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.bootstrap.assetfactories.MouseCursorImageFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.MouseCursorImageDefinition;

import static inaugural.soliloquy.tools.random.Random.randomIntInRange;
import static inaugural.soliloquy.tools.random.Random.randomString;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MouseCursorImageFactoryImplTests {
    private final String RELATIVE_LOCATION =
            "./src/test/resources/images/mouse_cursors/cursor_green_default.png";
    private final int IMAGE_WIDTH = 128;
    private final int IMAGE_HEIGHT = 126;
    private final int HOTSPOT_X = randomIntInRange(0, IMAGE_WIDTH - 1);
    private final int HOTSPOT_Y = randomIntInRange(0, IMAGE_HEIGHT - 1);

    private MouseCursorImageFactory _mouseCursorImageFactory;

    @BeforeEach
    void setUp() {
        _mouseCursorImageFactory = new MouseCursorImageFactoryImpl();
    }

    @Test
    void testMake() {
        // NB: This method only returns a long, which will typically be 0, so this method is not
        //     doing much to test the actual functionality. Pradoxically, testMakeWithInvalidParams
        //     does more to validate that, since it throws an exception when the hotspots are
        //     beyond the bounds of the image, which can only work if the image is actually loaded.
        _mouseCursorImageFactory.make(
                new MouseCursorImageDefinition(
                        RELATIVE_LOCATION,
                        HOTSPOT_X,
                        HOTSPOT_Y
                )
        );
    }

    @Test
    void testMakeWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _mouseCursorImageFactory.make(
                null
        ));

        assertThrows(IllegalArgumentException.class, () -> _mouseCursorImageFactory.make(
                new MouseCursorImageDefinition(
                        null,
                        HOTSPOT_X,
                        HOTSPOT_Y
                )
        ));
        assertThrows(IllegalArgumentException.class, () -> _mouseCursorImageFactory.make(
                new MouseCursorImageDefinition(
                        "",
                        HOTSPOT_X,
                        HOTSPOT_Y
                )
        ));
        assertThrows(IllegalArgumentException.class, () -> _mouseCursorImageFactory.make(
                new MouseCursorImageDefinition(
                        randomString(),
                        HOTSPOT_X,
                        HOTSPOT_Y
                )
        ));

        assertThrows(IllegalArgumentException.class, () -> _mouseCursorImageFactory.make(
                new MouseCursorImageDefinition(
                        RELATIVE_LOCATION,
                        -1,
                        HOTSPOT_Y
                )
        ));
        assertThrows(IllegalArgumentException.class, () -> _mouseCursorImageFactory.make(
                new MouseCursorImageDefinition(
                        RELATIVE_LOCATION,
                        IMAGE_WIDTH,
                        HOTSPOT_Y
                )
        ));

        assertThrows(IllegalArgumentException.class, () -> _mouseCursorImageFactory.make(
                new MouseCursorImageDefinition(
                        RELATIVE_LOCATION,
                        HOTSPOT_X,
                        -1
                )
        ));
        assertThrows(IllegalArgumentException.class, () -> _mouseCursorImageFactory.make(
                new MouseCursorImageDefinition(
                        RELATIVE_LOCATION,
                        HOTSPOT_X,
                        IMAGE_HEIGHT
                )
        ));
    }
}
