package inaugural.soliloquy.graphics.test.unit.bootstrap.tasks;

import inaugural.soliloquy.graphics.api.dto.MouseCursorImageDefinitionDTO;
import inaugural.soliloquy.graphics.bootstrap.tasks.MouseCursorImagePreloaderTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.graphics.bootstrap.assetfactories.MouseCursorImageFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.MouseCursorImageDefinition;

import java.util.ArrayList;

import static inaugural.soliloquy.tools.random.Random.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MouseCursorImagePreloaderTaskTests {
    private final String MOUSE_CURSOR_IMAGE_RELATIVE_LOCATION = randomString();
    private final int HOTSPOT_X = randomInt();
    private final int HOTSPOT_Y = randomInt();
    private final long FACTORY_OUTPUT = randomLong();

    @Mock
    private MouseCursorImageFactory _mockMouseCursorImageFactory;
    private MouseCursorImageDefinition _mockMouseCursorImageFactoryInput;

    private ArrayList<MouseCursorImageDefinitionDTO> _mouseCursorImageDefinitionDTOs;
    private ArrayList<MouseCursorImageFactory.Output> _mouseCursorImages;

    @BeforeEach
    void setUp() {
        _mouseCursorImageDefinitionDTOs = new ArrayList<MouseCursorImageDefinitionDTO>(){{
            add(new MouseCursorImageDefinitionDTO(
                    MOUSE_CURSOR_IMAGE_RELATIVE_LOCATION,
                    HOTSPOT_X,
                    HOTSPOT_Y
            ));
        }};

        _mockMouseCursorImageFactory = mock(MouseCursorImageFactory.class);
        when(_mockMouseCursorImageFactory.make(any()))
                .thenAnswer(invocation -> {
                    MouseCursorImageDefinition definition = invocation.getArgument(0);
                    _mockMouseCursorImageFactoryInput = definition;
                    return new MouseCursorImageFactory.Output(definition.relativeLocation(),
                            FACTORY_OUTPUT);
                });

        _mouseCursorImages = new ArrayList<>();
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> new MouseCursorImagePreloaderTask(
                null,
                _mockMouseCursorImageFactory,
                _mouseCursorImages::add
        ));
        assertThrows(IllegalArgumentException.class, () -> new MouseCursorImagePreloaderTask(
                _mouseCursorImageDefinitionDTOs,
                null,
                _mouseCursorImages::add
        ));
        assertThrows(IllegalArgumentException.class, () -> new MouseCursorImagePreloaderTask(
                _mouseCursorImageDefinitionDTOs,
                _mockMouseCursorImageFactory,
                null
        ));
    }

    @Test
    void testRun() {
        new MouseCursorImagePreloaderTask(
                _mouseCursorImageDefinitionDTOs,
                _mockMouseCursorImageFactory,
                _mouseCursorImages::add)
                .run();

        assertEquals(1, _mouseCursorImages.size());
        assertEquals(MOUSE_CURSOR_IMAGE_RELATIVE_LOCATION,
                _mouseCursorImages.get(0).relativeLocation());
        assertEquals(FACTORY_OUTPUT, _mouseCursorImages.get(0).id());
        assertEquals(_mouseCursorImageDefinitionDTOs.get(0).RelativeLocation,
                _mockMouseCursorImageFactoryInput.relativeLocation());
        assertEquals(_mouseCursorImageDefinitionDTOs.get(0).HotspotX,
                _mockMouseCursorImageFactoryInput.hotspotX());
        assertEquals(_mouseCursorImageDefinitionDTOs.get(0).HotspotY,
                _mockMouseCursorImageFactoryInput.hotspotY());
    }
}
