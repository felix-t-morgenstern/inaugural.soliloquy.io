package inaugural.soliloquy.graphics.test.unit.renderables.providers.factories;

import inaugural.soliloquy.graphics.renderables.providers.ProgressiveStringProvider;
import inaugural.soliloquy.graphics.renderables.providers.factories.ProgressiveStringProviderFactoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.renderables.providers.factories.ProgressiveStringProviderFactory;

import java.util.UUID;

import static inaugural.soliloquy.tools.random.Random.*;
import static org.junit.jupiter.api.Assertions.*;

class ProgressiveStringProviderFactoryTests {
    private final UUID UUID = java.util.UUID.randomUUID();
    private final String STRING = randomString();
    private final long START_TIMESTAMP = randomLong();
    private final long TIME_TO_COMPLETE = randomLongWithInclusiveFloor(1L);
    private final long PAUSED_TIMESTAMP = randomLong();
    private final long MOST_RECENT_TIMESTAMP = randomLongWithInclusiveFloor(PAUSED_TIMESTAMP);

    private ProgressiveStringProviderFactory _progressiveStringProviderFactory;

    @BeforeEach
    void setUp() {
        _progressiveStringProviderFactory = new ProgressiveStringProviderFactoryImpl();
    }

    @Test
    void testMake() {
        ProviderAtTime<String> provider =
                _progressiveStringProviderFactory.make(UUID, STRING, START_TIMESTAMP,
                        TIME_TO_COMPLETE, PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP);

        assertNotNull(provider);
        assertTrue(provider instanceof ProgressiveStringProvider);
        assertEquals(UUID, provider.uuid());
        assertEquals((Long) PAUSED_TIMESTAMP, provider.pausedTimestamp());
        assertEquals((Long) MOST_RECENT_TIMESTAMP, provider.mostRecentTimestamp());
    }

    @Test
    void testMakeWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> _progressiveStringProviderFactory.make(null, STRING, START_TIMESTAMP,
                        TIME_TO_COMPLETE, PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> _progressiveStringProviderFactory.make(UUID, null, START_TIMESTAMP,
                        TIME_TO_COMPLETE, PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> _progressiveStringProviderFactory.make(UUID, STRING, START_TIMESTAMP,
                        0L, PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> _progressiveStringProviderFactory.make(UUID, STRING, START_TIMESTAMP,
                        TIME_TO_COMPLETE, PAUSED_TIMESTAMP, null));
        assertThrows(IllegalArgumentException.class,
                () -> _progressiveStringProviderFactory.make(UUID, STRING, START_TIMESTAMP,
                        TIME_TO_COMPLETE, PAUSED_TIMESTAMP, PAUSED_TIMESTAMP - 1));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(ProgressiveStringProviderFactory.class.getCanonicalName(),
                _progressiveStringProviderFactory.getInterfaceName());
    }
}
