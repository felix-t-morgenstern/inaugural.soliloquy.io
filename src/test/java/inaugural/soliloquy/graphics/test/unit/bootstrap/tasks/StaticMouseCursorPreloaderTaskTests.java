package inaugural.soliloquy.graphics.test.unit.bootstrap.tasks;

import inaugural.soliloquy.graphics.api.dto.StaticMouseCursorDefinitionDTO;
import inaugural.soliloquy.graphics.bootstrap.tasks.StaticMouseCursorPreloaderTask;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeStaticMouseCursorProviderFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.renderables.providers.StaticMouseCursorProvider;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

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

    private final FakeStaticMouseCursorProviderFactory FACTORY =
            new FakeStaticMouseCursorProviderFactory();

    private final List<StaticMouseCursorProvider> RESULTS = listOf();

    private StaticMouseCursorPreloaderTask staticMouseCursorPreloaderTask;

    @BeforeEach
    public void setUp() {
        staticMouseCursorPreloaderTask = new StaticMouseCursorPreloaderTask(
                STATIC_MOUSE_CURSORS::get, STATIC_MOUSE_CURSOR_DEFINITION_DTOS, FACTORY,
                RESULTS::add);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                new StaticMouseCursorPreloaderTask(null, STATIC_MOUSE_CURSOR_DEFINITION_DTOS,
                        FACTORY, RESULTS::add));

        assertThrows(IllegalArgumentException.class, () ->
                new StaticMouseCursorPreloaderTask(STATIC_MOUSE_CURSORS::get,
                        STATIC_MOUSE_CURSOR_DEFINITION_DTOS, null,
                        RESULTS::add));

        assertThrows(IllegalArgumentException.class, () ->
                new StaticMouseCursorPreloaderTask(STATIC_MOUSE_CURSORS::get,
                        null, FACTORY,
                        RESULTS::add));
        assertThrows(IllegalArgumentException.class, () ->
                new StaticMouseCursorPreloaderTask(STATIC_MOUSE_CURSORS::get,
                        listOf(), FACTORY,
                        RESULTS::add));
        assertThrows(IllegalArgumentException.class, () ->
                new StaticMouseCursorPreloaderTask(STATIC_MOUSE_CURSORS::get,
                        listOf((StaticMouseCursorDefinitionDTO) null),
                        FACTORY,
                        RESULTS::add));
        assertThrows(IllegalArgumentException.class, () ->
                new StaticMouseCursorPreloaderTask(STATIC_MOUSE_CURSORS::get,
                        listOf(new StaticMouseCursorDefinitionDTO(null,
                                    STATIC_MOUSE_CURSOR_RELATIVE_LOCATION_1)),
                        FACTORY,
                        RESULTS::add));
        assertThrows(IllegalArgumentException.class, () ->
                new StaticMouseCursorPreloaderTask(STATIC_MOUSE_CURSORS::get,
                        listOf(new StaticMouseCursorDefinitionDTO("",
                                    STATIC_MOUSE_CURSOR_RELATIVE_LOCATION_1)),
                        FACTORY,
                        RESULTS::add));
        assertThrows(IllegalArgumentException.class, () ->
                new StaticMouseCursorPreloaderTask(STATIC_MOUSE_CURSORS::get,
                        listOf(new StaticMouseCursorDefinitionDTO(STATIC_MOUSE_CURSOR_ID_1,
                                    null)),
                        FACTORY,
                        RESULTS::add));
        assertThrows(IllegalArgumentException.class, () ->
                new StaticMouseCursorPreloaderTask(STATIC_MOUSE_CURSORS::get,
                        listOf(new StaticMouseCursorDefinitionDTO(STATIC_MOUSE_CURSOR_ID_1,
                                    "")),
                        FACTORY,
                        RESULTS::add));

        assertThrows(IllegalArgumentException.class, () ->
                new StaticMouseCursorPreloaderTask(STATIC_MOUSE_CURSORS::get,
                        STATIC_MOUSE_CURSOR_DEFINITION_DTOS, FACTORY, null));
    }

    @Test
    public void testRun() {
        staticMouseCursorPreloaderTask.run();

        assertEquals(2, FACTORY.Inputs.size());
        var index = 0;
        for (StaticMouseCursorDefinitionDTO staticMouseCursorDefinitionDTO :
                STATIC_MOUSE_CURSOR_DEFINITION_DTOS) {
            List<StaticMouseCursorProvider> matchingProviders = RESULTS.stream()
                    .filter(provider -> provider.id().equals(staticMouseCursorDefinitionDTO.Id))
                    .toList();
            assertEquals(1, matchingProviders.size());
            StaticMouseCursorProvider matchingProvider = matchingProviders.getFirst();
            assertEquals(index == 0 ? STATIC_MOUSE_CURSOR_MOUSE_1 : STATIC_MOUSE_CURSOR_MOUSE_2,
                    matchingProvider.provide(789789L));
            index++;
        }
    }
}
