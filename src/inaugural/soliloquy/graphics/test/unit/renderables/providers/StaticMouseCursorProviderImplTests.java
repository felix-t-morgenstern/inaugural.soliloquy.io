package inaugural.soliloquy.graphics.test.unit.renderables.providers;

import inaugural.soliloquy.graphics.renderables.providers.StaticMouseCursorProviderImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.renderables.providers.StaticMouseCursorProvider;

import static inaugural.soliloquy.tools.random.Random.*;
import static org.junit.jupiter.api.Assertions.*;

class StaticMouseCursorProviderImplTests {
    private final String ID = randomString();
    private final Long PROVIDED_VALUE = randomLong();
    private final Long MOST_RECENT_TIMESTAMP = randomLong();
    
    private StaticMouseCursorProvider _staticMouseCursorProvider;
    
    @BeforeEach
    void setUp() {
        _staticMouseCursorProvider = 
                new StaticMouseCursorProviderImpl(ID, PROVIDED_VALUE, MOST_RECENT_TIMESTAMP);
    }
    
    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> new StaticMouseCursorProviderImpl(
                null, PROVIDED_VALUE, MOST_RECENT_TIMESTAMP
        ));
        assertThrows(IllegalArgumentException.class, () -> new StaticMouseCursorProviderImpl(
                "", PROVIDED_VALUE, MOST_RECENT_TIMESTAMP
        ));
    }

    @Test
    void testUuid() {
        assertThrows(UnsupportedOperationException.class, _staticMouseCursorProvider::uuid);
    }

    @Test
    void testId() {
        assertEquals(ID, _staticMouseCursorProvider.id());
    }

    @Test
    void testProvide() {
        assertEquals(PROVIDED_VALUE, _staticMouseCursorProvider.provide(MOST_RECENT_TIMESTAMP));
    }

    @Test
    void testCallsMadeToPriorTimestamps() {
        long timestamp1 = randomLongWithInclusiveFloor(MOST_RECENT_TIMESTAMP + 1);
        long timestamp2 = randomLongWithInclusiveFloor(timestamp1 + 1);
        long timestamp3 = randomLongWithInclusiveFloor(timestamp2 + 1);

        _staticMouseCursorProvider.provide(timestamp1);

        assertThrows(IllegalArgumentException.class,
                () -> _staticMouseCursorProvider.provide(timestamp1 - 1));
        assertThrows(IllegalArgumentException.class,
                () -> _staticMouseCursorProvider.reportPause(timestamp1 - 1));
        assertThrows(IllegalArgumentException.class,
                () -> _staticMouseCursorProvider.reportUnpause(timestamp1 - 1));

        _staticMouseCursorProvider.reportPause(timestamp2);

        assertThrows(IllegalArgumentException.class,
                () -> _staticMouseCursorProvider.provide(timestamp2 - 1));
        assertThrows(IllegalArgumentException.class,
                () -> _staticMouseCursorProvider.reportPause(timestamp2 - 1));
        assertThrows(IllegalArgumentException.class,
                () -> _staticMouseCursorProvider.reportUnpause(timestamp2 - 1));

        _staticMouseCursorProvider.reportUnpause(timestamp3);

        assertThrows(IllegalArgumentException.class,
                () -> _staticMouseCursorProvider.provide(timestamp3 - 1));
        assertThrows(IllegalArgumentException.class,
                () -> _staticMouseCursorProvider.reportPause(timestamp3 - 1));
        assertThrows(IllegalArgumentException.class,
                () -> _staticMouseCursorProvider.reportUnpause(timestamp3 - 1));
    }

    @Test
    void testPausedTimestamp() {
        assertThrows(UnsupportedOperationException.class,
                _staticMouseCursorProvider::pausedTimestamp);
    }

    @Test
    void testMostRecentTimestamp() {
        assertEquals(MOST_RECENT_TIMESTAMP, _staticMouseCursorProvider.mostRecentTimestamp());
    }
}
