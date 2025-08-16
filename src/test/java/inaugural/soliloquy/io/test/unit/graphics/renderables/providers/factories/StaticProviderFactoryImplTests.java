package inaugural.soliloquy.io.test.unit.graphics.renderables.providers.factories;

import inaugural.soliloquy.io.graphics.renderables.providers.StaticProviderImpl;
import inaugural.soliloquy.io.graphics.renderables.providers.factories.StaticProviderFactoryImpl;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.io.graphics.renderables.providers.factories.StaticProviderFactory;

import java.util.UUID;

import static inaugural.soliloquy.tools.random.Random.randomLong;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class StaticProviderFactoryImplTests {
    private final Object VALUE = new Object();

    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private TimestampValidator mockTimestampValidator;

    private StaticProviderFactory factory;

    @BeforeEach
    public void setUp() {
        factory = new StaticProviderFactoryImpl(mockTimestampValidator);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new StaticProviderFactoryImpl(null));
    }

    @Test
    public void testMake() {
        var timestamp = randomLong();
        var provider = factory.make(UUID, VALUE);
        var provided = provider.provide(timestamp);

        assertNotNull(provider);
        assertSame(UUID, provider.uuid());
        assertSame(VALUE, provided);
        assertInstanceOf(StaticProviderImpl.class, provider);
        verify(mockTimestampValidator, atLeastOnce()).validateTimestamp(timestamp);
    }

    @Test
    public void testMakeWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> factory.make(null, VALUE));
    }
}
