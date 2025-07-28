package inaugural.soliloquy.io.test.unit.graphics.bootstrap.tasks;

import inaugural.soliloquy.io.api.dto.StaticMouseCursorDefinitionDTO;
import inaugural.soliloquy.io.graphics.bootstrap.tasks.StaticMouseCursorPreloaderTask;
import inaugural.soliloquy.tools.collections.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.StaticMouseCursorProviderDefinition;
import soliloquy.specs.io.graphics.renderables.providers.StaticMouseCursorProvider;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

@ExtendWith(MockitoExtension.class)
public class StaticMouseCursorPreloaderTaskTests {
    private final String STATIC_MOUSE_CURSOR_ID_1 = "staticMouseCursorId1";
    private final String STATIC_MOUSE_CURSOR_ID_2 = "staticMouseCursorId2";

    private final String STATIC_MOUSE_CURSOR_RELATIVE_LOCATION_1 =
            "staticMouseCursorRelativeLocation1";
    private final String STATIC_MOUSE_CURSOR_RELATIVE_LOCATION_2 =
            "staticMouseCursorRelativeLocation2";

    private final Long STATIC_MOUSE_CURSOR_MOUSE_1 = 123123L;
    private final Long STATIC_MOUSE_CURSOR_MOUSE_2 = 456456L;

    private final Map<String, Long> STATIC_MOUSE_CURSORS = mapOf(
            pairOf(STATIC_MOUSE_CURSOR_RELATIVE_LOCATION_1, STATIC_MOUSE_CURSOR_MOUSE_1),
            pairOf(STATIC_MOUSE_CURSOR_RELATIVE_LOCATION_2, STATIC_MOUSE_CURSOR_MOUSE_2)
    );

    private final StaticMouseCursorDefinitionDTO STATIC_MOUSE_CURSOR_DTO_1 =
            new StaticMouseCursorDefinitionDTO(STATIC_MOUSE_CURSOR_ID_1,
                    STATIC_MOUSE_CURSOR_RELATIVE_LOCATION_1);
    private final StaticMouseCursorDefinitionDTO STATIC_MOUSE_CURSOR_DTO_2 =
            new StaticMouseCursorDefinitionDTO(STATIC_MOUSE_CURSOR_ID_2,
                    STATIC_MOUSE_CURSOR_RELATIVE_LOCATION_2);

    private final Collection<StaticMouseCursorDefinitionDTO> STATIC_MOUSE_CURSOR_DEFINITION_DTOS =
            listOf(
                    STATIC_MOUSE_CURSOR_DTO_1,
                    STATIC_MOUSE_CURSOR_DTO_2
            );

    private final List<StaticMouseCursorProvider> RESULTS = listOf();

    @Mock private Function<StaticMouseCursorProviderDefinition, StaticMouseCursorProvider>
            mockFactory;

    private StaticMouseCursorPreloaderTask staticMouseCursorPreloaderTask;

    @BeforeEach
    public void setUp() {
        staticMouseCursorPreloaderTask = new StaticMouseCursorPreloaderTask(
                STATIC_MOUSE_CURSORS::get, STATIC_MOUSE_CURSOR_DEFINITION_DTOS, mockFactory,
                RESULTS::add);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                new StaticMouseCursorPreloaderTask(null, STATIC_MOUSE_CURSOR_DEFINITION_DTOS,
                        mockFactory, RESULTS::add));

        assertThrows(IllegalArgumentException.class, () ->
                new StaticMouseCursorPreloaderTask(STATIC_MOUSE_CURSORS::get,
                        STATIC_MOUSE_CURSOR_DEFINITION_DTOS, null,
                        RESULTS::add));

        assertThrows(IllegalArgumentException.class, () ->
                new StaticMouseCursorPreloaderTask(STATIC_MOUSE_CURSORS::get,
                        null, mockFactory,
                        RESULTS::add));
        assertThrows(IllegalArgumentException.class, () ->
                new StaticMouseCursorPreloaderTask(STATIC_MOUSE_CURSORS::get,
                        listOf(), mockFactory,
                        RESULTS::add));
        assertThrows(IllegalArgumentException.class, () ->
                new StaticMouseCursorPreloaderTask(STATIC_MOUSE_CURSORS::get,
                        listOf((StaticMouseCursorDefinitionDTO) null),
                        mockFactory,
                        RESULTS::add));
        assertThrows(IllegalArgumentException.class, () ->
                new StaticMouseCursorPreloaderTask(STATIC_MOUSE_CURSORS::get,
                        listOf(new StaticMouseCursorDefinitionDTO(null,
                                STATIC_MOUSE_CURSOR_RELATIVE_LOCATION_1)),
                        mockFactory,
                        RESULTS::add));
        assertThrows(IllegalArgumentException.class, () ->
                new StaticMouseCursorPreloaderTask(STATIC_MOUSE_CURSORS::get,
                        listOf(new StaticMouseCursorDefinitionDTO("",
                                STATIC_MOUSE_CURSOR_RELATIVE_LOCATION_1)),
                        mockFactory,
                        RESULTS::add));
        assertThrows(IllegalArgumentException.class, () ->
                new StaticMouseCursorPreloaderTask(STATIC_MOUSE_CURSORS::get,
                        listOf(new StaticMouseCursorDefinitionDTO(STATIC_MOUSE_CURSOR_ID_1,
                                null)),
                        mockFactory,
                        RESULTS::add));
        assertThrows(IllegalArgumentException.class, () ->
                new StaticMouseCursorPreloaderTask(STATIC_MOUSE_CURSORS::get,
                        listOf(new StaticMouseCursorDefinitionDTO(STATIC_MOUSE_CURSOR_ID_1,
                                "")),
                        mockFactory,
                        RESULTS::add));

        assertThrows(IllegalArgumentException.class, () ->
                new StaticMouseCursorPreloaderTask(STATIC_MOUSE_CURSORS::get,
                        STATIC_MOUSE_CURSOR_DEFINITION_DTOS, mockFactory, null));
    }

    @Test
    public void testRun() {
        var definitions = Collections.<StaticMouseCursorProviderDefinition>listOf();
        var mockProvider = mock(StaticMouseCursorProvider.class);
        when(mockFactory.apply(any())).thenAnswer(i -> {
            definitions.add(i.getArgument(0));
            return mockProvider;
        });

        staticMouseCursorPreloaderTask.run();

        verify(mockFactory, times(2)).apply(any());
        assertEquals(1,
                definitions.stream().filter(d -> d.id().equals(STATIC_MOUSE_CURSOR_DTO_1.Id))
                        .count());
        assertEquals(1,
                definitions.stream().filter(d -> d.id().equals(STATIC_MOUSE_CURSOR_DTO_2.Id))
                        .count());
        assertEquals(2, RESULTS.size());
        assertSame(mockProvider, RESULTS.get(0));
        assertSame(mockProvider, RESULTS.get(1));
    }
}
