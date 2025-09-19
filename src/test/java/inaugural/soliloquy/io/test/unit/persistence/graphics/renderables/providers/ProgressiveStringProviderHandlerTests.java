package inaugural.soliloquy.io.test.unit.persistence.graphics.renderables.providers;

import inaugural.soliloquy.io.graphics.renderables.providers.ProgressiveStringProvider;
import inaugural.soliloquy.io.persistence.graphics.renderables.providers.ProgressiveStringProviderHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.renderables.providers.factories.ProgressiveStringProviderFactory;

import java.util.UUID;

import static inaugural.soliloquy.tools.random.Random.randomLong;
import static inaugural.soliloquy.tools.random.Random.randomString;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

@ExtendWith(MockitoExtension.class)
public class ProgressiveStringProviderHandlerTests {
    private final UUID UUID = java.util.UUID.randomUUID();
    private final String STRING = randomString();
    private final long TIME_TO_COMPLETE = randomLong();
    private final long START_TIMESTAMP = randomLong();
    private final Long PAUSED_TIMESTAMP = randomLong();

    @Mock private ProgressiveStringProviderFactory mockFactory;
    @Mock private ProviderAtTime<String> mockProvider;

    private TypeHandler<ProviderAtTime<String>> handler;

    private final String WRITTEN_VALUE = String.format(
            "{\"uuid\":\"%s\",\"string\":\"%s\",\"timeToComplete\":%d,\"startTimestamp\":%d," +
                    "\"pausedTimestamp\":%d}",
            UUID, STRING, TIME_TO_COMPLETE, START_TIMESTAMP, PAUSED_TIMESTAMP);

    @BeforeEach
    public void setUp() {
        handler = new ProgressiveStringProviderHandler(mockFactory);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new ProgressiveStringProviderHandler(null));
    }

    @Test
    public void testWrite() {
        when(mockProvider.uuid()).thenReturn(UUID);
        when(mockProvider.representation()).thenReturn(
                new ProgressiveStringProvider.Representation(STRING, TIME_TO_COMPLETE,
                        START_TIMESTAMP));
        when(mockProvider.pausedTimestamp()).thenReturn(PAUSED_TIMESTAMP);

        var output = handler.write(mockProvider);

        assertEquals(WRITTEN_VALUE, output);
    }

    @Test
    public void testWriteWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> handler.write(null));
    }

    @Test
    public void testRead() {
        when(mockFactory.make(any(), anyString(), anyLong(), anyLong(), anyLong()))
                .thenReturn(mockProvider);

        var output = handler.read(WRITTEN_VALUE);

        assertNotNull(output);
        assertSame(mockProvider, output);
        verify(mockFactory, once()).make(
                UUID,
                STRING,
                TIME_TO_COMPLETE,
                START_TIMESTAMP,
                PAUSED_TIMESTAMP
        );
    }

    @Test
    public void testReadWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> handler.read(null));
        assertThrows(IllegalArgumentException.class, () -> handler.read(""));
    }
}
