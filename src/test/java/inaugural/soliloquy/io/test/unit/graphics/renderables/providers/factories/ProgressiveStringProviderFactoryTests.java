package inaugural.soliloquy.io.test.unit.graphics.renderables.providers.factories;

import inaugural.soliloquy.io.graphics.renderables.providers.ProgressiveStringProvider;
import inaugural.soliloquy.io.graphics.renderables.providers.factories.ProgressiveStringProviderFactoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.renderables.providers.factories.ProgressiveStringProviderFactory;

import java.util.UUID;

import static inaugural.soliloquy.tools.random.Random.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProgressiveStringProviderFactoryTests {
    private final UUID UUID = java.util.UUID.randomUUID();
    private final String STRING = randomString();
    private final long START_TIMESTAMP = randomLong();
    private final long TIME_TO_COMPLETE = randomLongWithInclusiveFloor(1L);
    private final long PAUSED_TIMESTAMP = randomLong();
    private final long MOST_RECENT_TIMESTAMP = randomLongWithInclusiveFloor(PAUSED_TIMESTAMP);

    private ProgressiveStringProviderFactory progressiveStringProviderFactory;

    @BeforeEach
    public void setUp() {
        progressiveStringProviderFactory = new ProgressiveStringProviderFactoryImpl();
    }

    @Test
    public void testMake() {
        var provider = progressiveStringProviderFactory.make(UUID, STRING, START_TIMESTAMP,
                TIME_TO_COMPLETE, PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP);

        assertNotNull(provider);
        assertInstanceOf(ProgressiveStringProvider.class, provider);
        assertEquals(UUID, provider.uuid());
        assertEquals((Long) PAUSED_TIMESTAMP, provider.pausedTimestamp());
        assertEquals((Long) MOST_RECENT_TIMESTAMP, provider.mostRecentTimestamp());
    }

    @Test
    public void testMakeWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> progressiveStringProviderFactory.make(null, STRING, START_TIMESTAMP,
                        TIME_TO_COMPLETE, PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> progressiveStringProviderFactory.make(UUID, null, START_TIMESTAMP,
                        TIME_TO_COMPLETE, PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> progressiveStringProviderFactory.make(UUID, STRING, START_TIMESTAMP,
                        0L, PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> progressiveStringProviderFactory.make(UUID, STRING, START_TIMESTAMP,
                        TIME_TO_COMPLETE, PAUSED_TIMESTAMP, null));
        assertThrows(IllegalArgumentException.class,
                () -> progressiveStringProviderFactory.make(UUID, STRING, START_TIMESTAMP,
                        TIME_TO_COMPLETE, PAUSED_TIMESTAMP, PAUSED_TIMESTAMP - 1));
    }
}
