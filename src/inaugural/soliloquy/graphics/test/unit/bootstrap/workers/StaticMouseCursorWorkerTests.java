package inaugural.soliloquy.graphics.test.unit.bootstrap.workers;

import inaugural.soliloquy.graphics.api.dto.StaticMouseCursorDTO;
import inaugural.soliloquy.graphics.bootstrap.workers.StaticMouseCursorWorker;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeStaticProviderFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.renderables.providers.StaticProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class StaticMouseCursorWorkerTests {
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

    private final StaticMouseCursorDTO STATIC_MOUSE_CURSOR_DTO_1 =
            new StaticMouseCursorDTO(STATIC_MOUSE_CURSOR_ID_1,
                    STATIC_MOUSE_CURSOR_RELATIVE_LOCATION_1);
    private final StaticMouseCursorDTO STATIC_MOUSE_CURSOR_DTO_2 =
            new StaticMouseCursorDTO(STATIC_MOUSE_CURSOR_ID_2,
                    STATIC_MOUSE_CURSOR_RELATIVE_LOCATION_2);

    private final Collection<StaticMouseCursorDTO> STATIC_MOUSE_CURSOR_DTOS =
            new ArrayList<StaticMouseCursorDTO>() {{
                add(STATIC_MOUSE_CURSOR_DTO_1);
                add(STATIC_MOUSE_CURSOR_DTO_2);
            }};

    private final FakeStaticProviderFactory FACTORY = new FakeStaticProviderFactory();

    private final HashMap<String,StaticProvider<Long>> RESULTS = new HashMap<>();

    private StaticMouseCursorWorker _staticMouseCursorWorker;

    @BeforeEach
    void setUp() {
        _staticMouseCursorWorker = new StaticMouseCursorWorker(STATIC_MOUSE_CURSORS::get, FACTORY,
                STATIC_MOUSE_CURSOR_DTOS, id -> provider -> RESULTS.put(id, provider));
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                new StaticMouseCursorWorker(null, FACTORY,
                        STATIC_MOUSE_CURSOR_DTOS,
                        id -> provider -> RESULTS.put(id, provider)));

        assertThrows(IllegalArgumentException.class, () ->
                new StaticMouseCursorWorker(STATIC_MOUSE_CURSORS::get, null,
                        STATIC_MOUSE_CURSOR_DTOS,
                        id -> provider -> RESULTS.put(id, provider)));

        assertThrows(IllegalArgumentException.class, () ->
                new StaticMouseCursorWorker(STATIC_MOUSE_CURSORS::get, FACTORY,
                        null,
                        id -> provider -> RESULTS.put(id, provider)));
        assertThrows(IllegalArgumentException.class, () ->
                new StaticMouseCursorWorker(STATIC_MOUSE_CURSORS::get, FACTORY,
                        new ArrayList<>(),
                        id -> provider -> RESULTS.put(id, provider)));
        assertThrows(IllegalArgumentException.class, () ->
                new StaticMouseCursorWorker(STATIC_MOUSE_CURSORS::get, FACTORY,
                        new ArrayList<StaticMouseCursorDTO>() {{
                            add(null);
                        }},
                        id -> provider -> RESULTS.put(id, provider)));
        assertThrows(IllegalArgumentException.class, () ->
                new StaticMouseCursorWorker(STATIC_MOUSE_CURSORS::get, FACTORY,
                        new ArrayList<StaticMouseCursorDTO>() {{
                            add(new StaticMouseCursorDTO(null,
                                    STATIC_MOUSE_CURSOR_RELATIVE_LOCATION_1));
                        }},
                        id -> provider -> RESULTS.put(id, provider)));
        assertThrows(IllegalArgumentException.class, () ->
                new StaticMouseCursorWorker(STATIC_MOUSE_CURSORS::get, FACTORY,
                        new ArrayList<StaticMouseCursorDTO>() {{
                            add(new StaticMouseCursorDTO("",
                                    STATIC_MOUSE_CURSOR_RELATIVE_LOCATION_1));
                        }},
                        id -> provider -> RESULTS.put(id, provider)));
        assertThrows(IllegalArgumentException.class, () ->
                new StaticMouseCursorWorker(STATIC_MOUSE_CURSORS::get, FACTORY,
                        new ArrayList<StaticMouseCursorDTO>() {{
                            add(new StaticMouseCursorDTO(STATIC_MOUSE_CURSOR_ID_1,
                                    null));
                        }},
                        id -> provider -> RESULTS.put(id, provider)));
        assertThrows(IllegalArgumentException.class, () ->
                new StaticMouseCursorWorker(STATIC_MOUSE_CURSORS::get, FACTORY,
                        new ArrayList<StaticMouseCursorDTO>() {{
                            add(new StaticMouseCursorDTO(STATIC_MOUSE_CURSOR_ID_1,
                                    ""));
                        }},
                        id -> provider -> RESULTS.put(id, provider)));

        assertThrows(IllegalArgumentException.class, () ->
                new StaticMouseCursorWorker(STATIC_MOUSE_CURSORS::get, FACTORY,
                        STATIC_MOUSE_CURSOR_DTOS,
                        null));
    }

    @Test
    void testRun() {
        _staticMouseCursorWorker.run();

        assertEquals(2, FACTORY.Inputs.size());
        int index = 0;
        for(StaticMouseCursorDTO staticMouseCursorDTO : STATIC_MOUSE_CURSOR_DTOS) {
            assertTrue(RESULTS.containsKey(staticMouseCursorDTO.Id));
            assertEquals(index == 0 ? STATIC_MOUSE_CURSOR_MOUSE_1 : STATIC_MOUSE_CURSOR_MOUSE_2,
                    (long)RESULTS.get(staticMouseCursorDTO.Id).provide(789789L));
            assertNotNull(RESULTS.get(index == 0 ?
                    STATIC_MOUSE_CURSOR_ID_1 :
                    STATIC_MOUSE_CURSOR_ID_2).uuid());
            index++;
        };
    }
}
