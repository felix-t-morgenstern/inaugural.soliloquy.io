package inaugural.soliloquy.graphics.test.unit.bootstrap.tasks;

import inaugural.soliloquy.graphics.api.dto.MouseCursorImageDefinitionDTO;
import inaugural.soliloquy.graphics.bootstrap.tasks.MouseCursorImagePreloaderTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.graphics.bootstrap.assetfactories.MouseCursorImageFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.MouseCursorImageDefinition;


import java.util.List;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.random.Random.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MouseCursorImagePreloaderTaskTests {
    private final String MOUSE_CURSOR_IMAGE_RELATIVE_LOCATION = randomString();
    private final int HOTSPOT_X = randomInt();
    private final int HOTSPOT_Y = randomInt();
    private final long FACTORY_OUTPUT = randomLong();

    @Mock
    private MouseCursorImageFactory mockMouseCursorImageFactory;
    private MouseCursorImageDefinition mockMouseCursorImageFactoryInput;

    private List<MouseCursorImageDefinitionDTO> mouseCursorImageDefinitionDTOs;
    private List<MouseCursorImageFactory.Output> mouseCursorImages;

    @BeforeEach
    public void setUp() {
        mouseCursorImageDefinitionDTOs = listOf(
            new MouseCursorImageDefinitionDTO(
                    MOUSE_CURSOR_IMAGE_RELATIVE_LOCATION,
                    HOTSPOT_X,
                    HOTSPOT_Y)
        );

        mockMouseCursorImageFactory = mock(MouseCursorImageFactory.class);
        when(mockMouseCursorImageFactory.make(any()))
                .thenAnswer(invocation -> {
                    MouseCursorImageDefinition definition = invocation.getArgument(0);
                    mockMouseCursorImageFactoryInput = definition;
                    return new MouseCursorImageFactory.Output(definition.relativeLocation(),
                            FACTORY_OUTPUT);
                });

        mouseCursorImages = listOf();
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new MouseCursorImagePreloaderTask(
                null,
                mockMouseCursorImageFactory,
                mouseCursorImages::add
        ));
        assertThrows(IllegalArgumentException.class, () -> new MouseCursorImagePreloaderTask(
                mouseCursorImageDefinitionDTOs,
                null,
                mouseCursorImages::add
        ));
        assertThrows(IllegalArgumentException.class, () -> new MouseCursorImagePreloaderTask(
                mouseCursorImageDefinitionDTOs,
                mockMouseCursorImageFactory,
                null
        ));
    }

    @Test
    public void testRun() {
        new MouseCursorImagePreloaderTask(
                mouseCursorImageDefinitionDTOs,
                mockMouseCursorImageFactory,
                mouseCursorImages::add)
                .run();

        assertEquals(1, mouseCursorImages.size());
        assertEquals(MOUSE_CURSOR_IMAGE_RELATIVE_LOCATION,
                mouseCursorImages.getFirst().relativeLocation());
        assertEquals(FACTORY_OUTPUT, mouseCursorImages.getFirst().id());
        assertEquals(mouseCursorImageDefinitionDTOs.getFirst().RelativeLocation,
                mockMouseCursorImageFactoryInput.relativeLocation());
        assertEquals(mouseCursorImageDefinitionDTOs.getFirst().HotspotX,
                mockMouseCursorImageFactoryInput.hotspotX());
        assertEquals(mouseCursorImageDefinitionDTOs.getFirst().HotspotY,
                mockMouseCursorImageFactoryInput.hotspotY());
    }
}
