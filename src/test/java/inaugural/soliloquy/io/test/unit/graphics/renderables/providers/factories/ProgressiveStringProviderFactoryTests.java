package inaugural.soliloquy.io.test.unit.graphics.renderables.providers.factories;

import inaugural.soliloquy.io.graphics.renderables.providers.ProgressiveStringProvider;
import inaugural.soliloquy.io.graphics.renderables.providers.factories.ProgressiveStringProviderFactoryImpl;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.io.graphics.renderables.providers.factories.ProgressiveStringProviderFactory;

import java.util.UUID;

import static inaugural.soliloquy.tools.random.Random.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProgressiveStringProviderFactoryTests {
    private final UUID UUID = java.util.UUID.randomUUID();
    private final String STRING = randomString();
    private final long START_TIMESTAMP = randomLong();
    private final long TIME_TO_COMPLETE = randomLongWithInclusiveFloor(1L);
    private final long PAUSED_TIMESTAMP = randomLong();

    @Mock private TimestampValidator mockTimestampValidator;

    private ProgressiveStringProviderFactory factory;

    @BeforeEach
    public void setUp() {
        factory = new ProgressiveStringProviderFactoryImpl(mockTimestampValidator);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new ProgressiveStringProviderFactoryImpl(null));
    }

    @Test
    public void testMake() {
        var provider = factory.make(UUID, STRING, START_TIMESTAMP, TIME_TO_COMPLETE, null);
        when(mockTimestampValidator.mostRecentTimestamp()).thenReturn(PAUSED_TIMESTAMP);

        provider.reportPause(PAUSED_TIMESTAMP);

        assertNotNull(provider);
        assertInstanceOf(ProgressiveStringProvider.class, provider);
        assertEquals(UUID, provider.uuid());
        assertEquals((Long) PAUSED_TIMESTAMP, provider.pausedTimestamp());
        verify(mockTimestampValidator, atLeastOnce()).validateTimestamp(PAUSED_TIMESTAMP);
    }

    @Test
    public void testMakeWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(null, STRING, START_TIMESTAMP,
                        TIME_TO_COMPLETE, PAUSED_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(UUID, null, START_TIMESTAMP,
                        TIME_TO_COMPLETE, PAUSED_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(UUID, STRING, START_TIMESTAMP,
                        0L, PAUSED_TIMESTAMP));
    }
}
