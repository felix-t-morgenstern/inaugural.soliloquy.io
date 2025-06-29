package inaugural.soliloquy.io.test.unit.graphics.renderables.providers.factories;

import inaugural.soliloquy.io.graphics.renderables.providers.StaticProviderImpl;
import inaugural.soliloquy.io.graphics.renderables.providers.factories.StaticProviderFactoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.io.graphics.renderables.providers.factories.StaticProviderFactory;

import java.util.UUID;

import static inaugural.soliloquy.tools.random.Random.randomLong;
import static org.junit.jupiter.api.Assertions.*;

public class StaticProviderFactoryImplTests {
    private final Object VALUE = new Object();
    private final long MOST_RECENT_TIMESTAMP = randomLong();

    private final UUID UUID = java.util.UUID.randomUUID();

    private StaticProviderFactory staticProviderFactory;

    @BeforeEach
    public void setUp() {
        staticProviderFactory = new StaticProviderFactoryImpl();
    }

    @Test
    public void testMake() {
        var staticProvider = staticProviderFactory.make(UUID, VALUE, MOST_RECENT_TIMESTAMP);

        assertNotNull(staticProvider);
        assertSame(UUID, staticProvider.uuid());
        assertSame(VALUE, staticProvider.provide(MOST_RECENT_TIMESTAMP));
        assertEquals(MOST_RECENT_TIMESTAMP, (long) staticProvider.mostRecentTimestamp());
        assertInstanceOf(StaticProviderImpl.class, staticProvider);
    }

    @Test
    public void testMakeWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> staticProviderFactory.make(null, VALUE, MOST_RECENT_TIMESTAMP));
    }
}
