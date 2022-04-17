package inaugural.soliloquy.graphics.test.unit.bootstrap.tasks;

import inaugural.soliloquy.graphics.api.dto.StaticMouseCursorDefinitionDTO;
import inaugural.soliloquy.graphics.bootstrap.tasks.StaticMouseCursorTask;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeStaticProviderFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.renderables.providers.StaticProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class StaticMouseCursorTaskTests {
    private final String STATIC_MOUSE_CURSOR_ID_1 = "staticMouseCursorId1";
    private final String STATIC_MOUSE_CURSOR_ID_2 = "staticMouseCursorId2";

    private final String STATIC_MOUSE_CURSOR_RELATIVE_LOCATION_1 =
            "staticMouseCursorRelativeLocation1";
    private final String STATIC_MOUSE_CURSOR_RELATIVE_LOCATION_2 =
            "staticMouseCursorRelativeLocation2";

    private final long STATIC_MOUSE_CURSOR_MOUSE_1 = 123123L;
    private final long STATIC_MOUSE_CURSOR_MOUSE_2 = 456456L;

    private final HashMap<String, Long> STATIC_MOUSE_CURSORS = new HashMap<String, Long>() {{
        put(STATIC_MOUSE_CURSOR_RELATIVE_LOCATION_1, STATIC_MOUSE_CURSOR_MOUSE_1);
        put(STATIC_MOUSE_CURSOR_RELATIVE_LOCATION_2, STATIC_MOUSE_CURSOR_MOUSE_2);
    }};

    private final StaticMouseCursorDefinitionDTO STATIC_MOUSE_CURSOR_DTO_1 =
            new StaticMouseCursorDefinitionDTO(STATIC_MOUSE_CURSOR_ID_1,
                    STATIC_MOUSE_CURSOR_RELATIVE_LOCATION_1);
    private final StaticMouseCursorDefinitionDTO STATIC_MOUSE_CURSOR_DTO_2 =
            new StaticMouseCursorDefinitionDTO(STATIC_MOUSE_CURSOR_ID_2,
                    STATIC_MOUSE_CURSOR_RELATIVE_LOCATION_2);

    private final Collection<StaticMouseCursorDefinitionDTO> STATIC_MOUSE_CURSOR_DEFINITION_DTOS =
            new ArrayList<StaticMouseCursorDefinitionDTO>() {{
                add(STATIC_MOUSE_CURSOR_DTO_1);
                add(STATIC_MOUSE_CURSOR_DTO_2);
            }};

    private final FakeStaticProviderFactory FACTORY = new FakeStaticProviderFactory();

    private final HashMap<String,StaticProvider<Long>> RESULTS = new HashMap<>();

    private StaticMouseCursorTask _staticMouseCursorTask;

    @BeforeEach
    void setUp() {
        _staticMouseCursorTask = new StaticMouseCursorTask(STATIC_MOUSE_CURSORS::get,
                STATIC_MOUSE_CURSOR_DEFINITION_DTOS,  FACTORY,
                id -> provider -> RESULTS.put(id, provider));
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                new StaticMouseCursorTask(null, STATIC_MOUSE_CURSOR_DEFINITION_DTOS, FACTORY,
                        id -> provider -> RESULTS.put(id, provider)));

        assertThrows(IllegalArgumentException.class, () ->
                new StaticMouseCursorTask(STATIC_MOUSE_CURSORS::get,
                        STATIC_MOUSE_CURSOR_DEFINITION_DTOS, null,
                        id -> provider -> RESULTS.put(id, provider)));

        assertThrows(IllegalArgumentException.class, () ->
                new StaticMouseCursorTask(STATIC_MOUSE_CURSORS::get,
                        null, FACTORY,
                        id -> provider -> RESULTS.put(id, provider)));
        assertThrows(IllegalArgumentException.class, () ->
                new StaticMouseCursorTask(STATIC_MOUSE_CURSORS::get,
                        new ArrayList<>(), FACTORY,
                        id -> provider -> RESULTS.put(id, provider)));
        assertThrows(IllegalArgumentException.class, () ->
                new StaticMouseCursorTask(STATIC_MOUSE_CURSORS::get,
                        new ArrayList<StaticMouseCursorDefinitionDTO>() {{
                            add(null);
                        }},
                        FACTORY,
                        id -> provider -> RESULTS.put(id, provider)));
        assertThrows(IllegalArgumentException.class, () ->
                new StaticMouseCursorTask(STATIC_MOUSE_CURSORS::get,
                        new ArrayList<StaticMouseCursorDefinitionDTO>() {{
                            add(new StaticMouseCursorDefinitionDTO(null,
                                    STATIC_MOUSE_CURSOR_RELATIVE_LOCATION_1));
                        }},
                        FACTORY,
                        id -> provider -> RESULTS.put(id, provider)));
        assertThrows(IllegalArgumentException.class, () ->
                new StaticMouseCursorTask(STATIC_MOUSE_CURSORS::get,
                        new ArrayList<StaticMouseCursorDefinitionDTO>() {{
                            add(new StaticMouseCursorDefinitionDTO("",
                                    STATIC_MOUSE_CURSOR_RELATIVE_LOCATION_1));
                        }},
                        FACTORY,
                        id -> provider -> RESULTS.put(id, provider)));
        assertThrows(IllegalArgumentException.class, () ->
                new StaticMouseCursorTask(STATIC_MOUSE_CURSORS::get,
                        new ArrayList<StaticMouseCursorDefinitionDTO>() {{
                            add(new StaticMouseCursorDefinitionDTO(STATIC_MOUSE_CURSOR_ID_1,
                                    null));
                        }},
                        FACTORY,
                        id -> provider -> RESULTS.put(id, provider)));
        assertThrows(IllegalArgumentException.class, () ->
                new StaticMouseCursorTask(STATIC_MOUSE_CURSORS::get,
                        new ArrayList<StaticMouseCursorDefinitionDTO>() {{
                            add(new StaticMouseCursorDefinitionDTO(STATIC_MOUSE_CURSOR_ID_1,
                                    ""));
                        }},
                        FACTORY,
                        id -> provider -> RESULTS.put(id, provider)));

        assertThrows(IllegalArgumentException.class, () ->
                new StaticMouseCursorTask(STATIC_MOUSE_CURSORS::get,
                        STATIC_MOUSE_CURSOR_DEFINITION_DTOS, FACTORY, null));
    }

    @Test
    void testRun() {
        _staticMouseCursorTask.run();

        assertEquals(2, FACTORY.Inputs.size());
        int index = 0;
        for(StaticMouseCursorDefinitionDTO staticMouseCursorDefinitionDTO :
                STATIC_MOUSE_CURSOR_DEFINITION_DTOS) {
            assertTrue(RESULTS.containsKey(staticMouseCursorDefinitionDTO.Id));
            assertEquals(index == 0 ? STATIC_MOUSE_CURSOR_MOUSE_1 : STATIC_MOUSE_CURSOR_MOUSE_2,
                    (long)RESULTS.get(staticMouseCursorDefinitionDTO.Id).provide(789789L));
            assertNotNull(RESULTS.get(index == 0 ?
                    STATIC_MOUSE_CURSOR_ID_1 :
                    STATIC_MOUSE_CURSOR_ID_2).uuid());
            index++;
        }
    }
}
