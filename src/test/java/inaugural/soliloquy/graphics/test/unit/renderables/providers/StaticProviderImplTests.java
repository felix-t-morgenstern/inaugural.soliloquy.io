package inaugural.soliloquy.graphics.test.unit.renderables.providers;

import inaugural.soliloquy.graphics.renderables.providers.StaticProviderImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.renderables.providers.StaticProvider;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class StaticProviderImplTests {
    private final Object PROVIDED_VALUE = new Object();
    private final long MOST_RECENT_TIMESTAMP = 111L;

    private final UUID UUID = java.util.UUID.randomUUID();

    private StaticProvider<Object> staticProvider;

    @BeforeEach
    public void setUp() {
        staticProvider = new StaticProviderImpl<>(UUID, PROVIDED_VALUE, MOST_RECENT_TIMESTAMP);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                new StaticProviderImpl<>(null, PROVIDED_VALUE, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> new StaticProviderImpl<>(null, PROVIDED_VALUE, MOST_RECENT_TIMESTAMP));
    }

    @Test
    public void testUuid() {
        assertSame(UUID, staticProvider.uuid());
    }

    @Test
    public void testProvide() {
        assertSame(PROVIDED_VALUE, staticProvider.provide(MOST_RECENT_TIMESTAMP));
    }

    @Test
    public void testCallsMadeToPriorTimestamps() {
        long timestamp1 = 123L;
        long timestamp2 = 456L;
        long timestamp3 = 789L;

        staticProvider.provide(timestamp1);

        assertThrows(IllegalArgumentException.class,
                () -> staticProvider.provide(timestamp1 - 1));
        assertThrows(IllegalArgumentException.class,
                () -> staticProvider.reportPause(timestamp1 - 1));
        assertThrows(IllegalArgumentException.class,
                () -> staticProvider.reportUnpause(timestamp1 - 1));

        staticProvider.reportPause(timestamp2);

        assertThrows(IllegalArgumentException.class,
                () -> staticProvider.provide(timestamp2 - 1));
        assertThrows(IllegalArgumentException.class,
                () -> staticProvider.reportPause(timestamp2 - 1));
        assertThrows(IllegalArgumentException.class,
                () -> staticProvider.reportUnpause(timestamp2 - 1));

        staticProvider.reportUnpause(timestamp3);

        assertThrows(IllegalArgumentException.class,
                () -> staticProvider.provide(timestamp3 - 1));
        assertThrows(IllegalArgumentException.class,
                () -> staticProvider.reportPause(timestamp3 - 1));
        assertThrows(IllegalArgumentException.class,
                () -> staticProvider.reportUnpause(timestamp3 - 1));
    }

    @Test
    public void testPausedTimestamp() {
        assertThrows(UnsupportedOperationException.class, staticProvider::pausedTimestamp);
    }

    @Test
    public void testMostRecentTimestamp() {
        assertEquals(MOST_RECENT_TIMESTAMP, (long) staticProvider.mostRecentTimestamp());
    }

    @Test
    public void testRepresentation() {
        assertEquals(PROVIDED_VALUE, staticProvider.representation());
    }
}
