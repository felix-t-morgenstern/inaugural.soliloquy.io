package inaugural.soliloquy.io.test.unit.graphics.renderables.providers;

import inaugural.soliloquy.io.graphics.renderables.providers.StaticProviderImpl;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import java.util.UUID;

import static inaugural.soliloquy.tools.random.Random.randomLong;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class StaticProviderImplTests {
    private final Object PROVIDED_VALUE = new Object();

    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private TimestampValidator mockTimestampValidator;

    private ProviderAtTime<Object> provider;

    @BeforeEach
    public void setUp() {
        provider = new StaticProviderImpl<>(UUID, PROVIDED_VALUE, mockTimestampValidator);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                new StaticProviderImpl<>(null, null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new StaticProviderImpl<>(UUID, null, null));
    }

    @Test
    public void testUuid() {
        assertSame(UUID, provider.uuid());
    }

    @Test
    public void testProvide() {
        var timestamp = randomLong();

        assertSame(PROVIDED_VALUE, provider.provide(timestamp));
        verify(mockTimestampValidator, atLeastOnce()).validateTimestamp(timestamp);
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
