package inaugural.soliloquy.io.test.unit.persistence.graphics.renderables.providers;

import inaugural.soliloquy.io.persistence.graphics.renderables.providers.ProgressiveStringProviderHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.renderables.providers.factories.ProgressiveStringProviderFactory;

import java.util.UUID;

import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

public class ProgressiveStringProviderHandlerTests {
    private final String UUID_STRING = "819f5a14-466a-4c3b-af43-11d39cd0c9c9";
    private final String STRING = "string";
    private final long TIME_TO_COMPLETE = 111L;
    private final long START_TIMESTAMP = 222L;
    private final Long PAUSED_TIMESTAMP = 333L;
    private final Long MOST_RECENT_TIMESTAMP = 444L;

    @Mock private ProgressiveStringProviderFactory mockProgressiveStringProviderFactory;
    @Mock private ProviderAtTime<String> mockProgressiveStringProvider;

    private TypeHandler<ProviderAtTime<String>> progressiveStringProviderHandler;

    private final String WRITTEN_VALUE = "{\"uuid\":\"819f5a14-466a-4c3b-af43-11d39cd0c9c9\"," +
            "\"string\":\"string\",\"timeToComplete\":111,\"startTimestamp\":222," +
            "\"pausedTimestamp\":333,\"mostRecentTimestamp\":444}";

    @BeforeEach
    public void setUp() {
        //noinspection unchecked
        mockProgressiveStringProvider = mock(ProviderAtTime.class);
        when(mockProgressiveStringProvider.uuid()).thenReturn(UUID.fromString(UUID_STRING));
        when(mockProgressiveStringProvider.representation()).thenReturn(
                pairOf(STRING, pairOf(TIME_TO_COMPLETE, START_TIMESTAMP)));
        when(mockProgressiveStringProvider.pausedTimestamp()).thenReturn(PAUSED_TIMESTAMP);
        when(mockProgressiveStringProvider.mostRecentTimestamp()).thenReturn(MOST_RECENT_TIMESTAMP);

        mockProgressiveStringProviderFactory = mock(ProgressiveStringProviderFactory.class);
        when(mockProgressiveStringProviderFactory.make(any(), anyString(), anyLong(), anyLong(),
                anyLong(), anyLong())).thenReturn(mockProgressiveStringProvider);

        progressiveStringProviderHandler =
                new ProgressiveStringProviderHandler(mockProgressiveStringProviderFactory);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new ProgressiveStringProviderHandler(null));
    }

    @Test
    public void testWrite() {
        String output = progressiveStringProviderHandler.write(mockProgressiveStringProvider);

        assertEquals(WRITTEN_VALUE, output);
    }

    @Test
    public void testWriteWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> progressiveStringProviderHandler.write(null));
    }

    @Test
    public void testRead() {
        ProviderAtTime<String> output = progressiveStringProviderHandler.read(WRITTEN_VALUE);

        assertNotNull(output);
        assertSame(mockProgressiveStringProvider, output);
        verify(mockProgressiveStringProviderFactory, once()).make(UUID.fromString(UUID_STRING),
                STRING, TIME_TO_COMPLETE, START_TIMESTAMP, PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP);
    }

    @Test
    public void testReadWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> progressiveStringProviderHandler.read(null));
        assertThrows(IllegalArgumentException.class,
                () -> progressiveStringProviderHandler.read(""));
    }
}
