package inaugural.soliloquy.graphics.test.unit.renderables.providers;

import inaugural.soliloquy.graphics.renderables.providers.StaticProviderImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.graphics.renderables.providers.StaticProvider;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class StaticProviderImplTests {
    private final Object PROVIDED_VALUE = new Object();
    private final long MOST_RECENT_TIMESTAMP = 111L;

    private final UUID UUID = java.util.UUID.randomUUID();

    private StaticProvider<Object> _staticProvider;

    @BeforeEach
    void setUp() {
        _staticProvider = new StaticProviderImpl<>(UUID, PROVIDED_VALUE, MOST_RECENT_TIMESTAMP);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                new StaticProviderImpl<>(null, PROVIDED_VALUE, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                new StaticProviderImpl<>(UUID, null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> new StaticProviderImpl<>(UUID, PROVIDED_VALUE, null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> new StaticProviderImpl<>(null, PROVIDED_VALUE, PROVIDED_VALUE,
                        MOST_RECENT_TIMESTAMP));
    }

    @Test
    void testUuid() {
        assertSame(UUID, _staticProvider.uuid());
    }

    @Test
    void testProvide() {
        assertSame(PROVIDED_VALUE, _staticProvider.provide(MOST_RECENT_TIMESTAMP));
    }

    @Test
    void testCallsMadeToPriorTimestamps() {
        long timestamp1 = 123L;
        long timestamp2 = 456L;
        long timestamp3 = 789L;

        _staticProvider.provide(timestamp1);

        assertThrows(IllegalArgumentException.class,
                () -> _staticProvider.provide(timestamp1 - 1));
        assertThrows(IllegalArgumentException.class,
                () -> _staticProvider.reportPause(timestamp1 - 1));
        assertThrows(IllegalArgumentException.class,
                () -> _staticProvider.reportUnpause(timestamp1 - 1));

        _staticProvider.reportPause(timestamp2);

        assertThrows(IllegalArgumentException.class,
                () -> _staticProvider.provide(timestamp2 - 1));
        assertThrows(IllegalArgumentException.class,
                () -> _staticProvider.reportPause(timestamp2 - 1));
        assertThrows(IllegalArgumentException.class,
                () -> _staticProvider.reportUnpause(timestamp2 - 1));

        _staticProvider.reportUnpause(timestamp3);

        assertThrows(IllegalArgumentException.class,
                () -> _staticProvider.provide(timestamp3 - 1));
        assertThrows(IllegalArgumentException.class,
                () -> _staticProvider.reportPause(timestamp3 - 1));
        assertThrows(IllegalArgumentException.class,
                () -> _staticProvider.reportUnpause(timestamp3 - 1));
    }

    @Test
    void testPausedTimestamp() {
        assertThrows(UnsupportedOperationException.class, _staticProvider::pausedTimestamp);
    }

    @Test
    void testMostRecentTimestamp() {
        assertEquals(MOST_RECENT_TIMESTAMP, (long)_staticProvider.mostRecentTimestamp());
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(StaticProvider.class.getCanonicalName() + "<" +
                Object.class.getCanonicalName() + ">",
                _staticProvider.getInterfaceName());
    }
}
