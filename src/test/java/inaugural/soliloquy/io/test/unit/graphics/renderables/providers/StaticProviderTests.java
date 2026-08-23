package inaugural.soliloquy.io.test.unit.graphics.renderables.providers;

import inaugural.soliloquy.io.graphics.renderables.providers.StaticProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import java.util.UUID;

import static inaugural.soliloquy.tools.random.Random.randomLong;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class StaticProviderTests {
    private final Object PROVIDED_VALUE = new Object();

    private final UUID UUID = java.util.UUID.randomUUID();

    private ProviderAtTime<Object> provider;

    @BeforeEach
    public void setUp() {
        provider = new StaticProvider<>(UUID, PROVIDED_VALUE);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new StaticProvider<>(null, null));
    }

    @Test
    public void testUuid() {
        assertSame(UUID, provider.uuid());
    }

    @Test
    public void testProvide() {
        var timestamp = randomLong();

        assertSame(PROVIDED_VALUE, provider.provide(timestamp));
    }

    @Test
    public void testPausedTimestamp() {
        assertThrows(UnsupportedOperationException.class, provider::pausedTimestamp);
    }

    @Test
    public void testRepresentation() {
        assertEquals(PROVIDED_VALUE, provider.representation());
    }
}
