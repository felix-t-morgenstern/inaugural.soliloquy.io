package inaugural.soliloquy.graphics.test.unit.renderables.providers.factories;

import inaugural.soliloquy.graphics.renderables.providers.StaticProviderImpl;
import inaugural.soliloquy.graphics.renderables.providers.factories.StaticProviderFactoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.renderables.providers.factories.StaticProviderFactory;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class StaticProviderFactoryImplTests {
    private final Object VALUE = new Object();
    private final Object ARCHETYPE = new Object();
    private final long MOST_RECENT_TIMESTAMP = 123123L;

    private final UUID UUID = java.util.UUID.randomUUID();

    private StaticProviderFactory _staticProviderFactory;

    @BeforeEach
    void setUp() {
        _staticProviderFactory = new StaticProviderFactoryImpl();
    }

    @Test
    void testMake() {
        ProviderAtTime<Object> staticProvider =
                _staticProviderFactory.make(UUID, VALUE, ARCHETYPE, MOST_RECENT_TIMESTAMP);

        assertNotNull(staticProvider);
        assertSame(UUID, staticProvider.uuid());
        assertSame(VALUE, staticProvider.provide(MOST_RECENT_TIMESTAMP));
        assertSame(ARCHETYPE, staticProvider.getArchetype());
        assertEquals(MOST_RECENT_TIMESTAMP, (long) staticProvider.mostRecentTimestamp());
        assertTrue(staticProvider instanceof StaticProviderImpl);
    }

    @Test
    void testMakeWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> _staticProviderFactory.make(null, VALUE, ARCHETYPE, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> _staticProviderFactory.make(UUID, VALUE, null, MOST_RECENT_TIMESTAMP));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(StaticProviderFactory.class.getCanonicalName(),
                _staticProviderFactory.getInterfaceName());
    }
}
