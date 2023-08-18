package inaugural.soliloquy.graphics.test.unit.persistence.renderables.providers;

import inaugural.soliloquy.graphics.persistence.renderables.providers.ProgressiveStringProviderHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.common.valueobjects.Pair;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.renderables.providers.factories.ProgressiveStringProviderFactory;

import java.util.UUID;

import static inaugural.soliloquy.tools.valueobjects.Pair.pairOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ProgressiveStringProviderHandlerTests {
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
    void setUp() {
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
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new ProgressiveStringProviderHandler(null));
    }

    @Test
    void testWrite() {
        String output = progressiveStringProviderHandler.write(mockProgressiveStringProvider);

        assertEquals(WRITTEN_VALUE, output);
    }

    @Test
    void testWriteWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> progressiveStringProviderHandler.write(null));
    }

    @Test
    void testRead() {
        ProviderAtTime<String> output = progressiveStringProviderHandler.read(WRITTEN_VALUE);

        assertNotNull(output);
        assertSame(mockProgressiveStringProvider, output);
        verify(mockProgressiveStringProviderFactory, times(1)).make(UUID.fromString(UUID_STRING),
                STRING, TIME_TO_COMPLETE, START_TIMESTAMP, PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP);
    }

    @Test
    void testReadWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> progressiveStringProviderHandler.read(null));
        assertThrows(IllegalArgumentException.class,
                () -> progressiveStringProviderHandler.read(""));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(TypeHandler.class.getCanonicalName() + "<" +
                ProviderAtTime.class.getCanonicalName() + "<" + String.class.getCanonicalName() +
                ">>", progressiveStringProviderHandler.getInterfaceName());
    }
}
