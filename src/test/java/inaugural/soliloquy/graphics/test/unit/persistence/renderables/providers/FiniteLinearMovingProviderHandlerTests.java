package inaugural.soliloquy.graphics.test.unit.persistence.renderables.providers;

import inaugural.soliloquy.graphics.persistence.renderables.providers.FiniteLinearMovingProviderHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import soliloquy.specs.common.persistence.PersistenceHandler;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.graphics.renderables.providers.FiniteLinearMovingProvider;
import soliloquy.specs.graphics.renderables.providers.factories.FiniteLinearMovingProviderFactory;

import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Mock.generateMockMap;
import static inaugural.soliloquy.tools.testing.Mock.generateSimpleMockTypeHandler;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

public class FiniteLinearMovingProviderHandlerTests {
    private final Long TIMESTAMP_1 = randomLong();
    private final Long TIMESTAMP_2 = randomLong();
    private final Long TIMESTAMP_3 = randomLong();
    private final Float VALUE_1 = randomFloat();
    private final Float VALUE_2 = randomFloat();
    private final Float VALUE_3 = randomFloat();
    private final Long PAUSED_TIMESTAMP = randomLong();
    private final Long MOST_RECENT_TIMESTAMP = randomLong();

    private final UUID UUID = java.util.UUID.randomUUID();

    private final String FLOAT_WRITE_OUTPUT_1 = randomString();
    private final String FLOAT_WRITE_OUTPUT_2 = randomString();
    private final String FLOAT_WRITE_OUTPUT_3 = randomString();
    private final TypeHandler<Float> MOCK_FLOAT_HANDLER = generateSimpleMockTypeHandler(
            pairOf(FLOAT_WRITE_OUTPUT_1, VALUE_1),
            pairOf(FLOAT_WRITE_OUTPUT_2, VALUE_2),
            pairOf(FLOAT_WRITE_OUTPUT_3, VALUE_3));



    private final String WRITTEN_VALUE = String.format(
            "{\"uuid\":\"%s\",\"valueType\":\"java.lang.Float\",\"values\":[{\"timestamp\":%d," +
                    "\"value\":\"%s\"},{\"timestamp\":%d,\"value\":\"%s\"},{\"timestamp\":%d," +
                    "\"value\":\"%s\"}],\"pausedTimestamp\":%d,\"mostRecentTimestamp\":%d}",
            UUID, TIMESTAMP_1, FLOAT_WRITE_OUTPUT_1, TIMESTAMP_2, FLOAT_WRITE_OUTPUT_2, TIMESTAMP_3,
            FLOAT_WRITE_OUTPUT_3, PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP);

    private Map<Long, Float> mockValuesAtTimestamps;

    @Mock private FiniteLinearMovingProviderFactory mockFactory;
    @Mock private FiniteLinearMovingProvider<Float> mockFiniteLinearMovingProvider;
    @Mock private PersistenceHandler mockPersistenceHandler;

    @SuppressWarnings("rawtypes")
    private TypeHandler<FiniteLinearMovingProvider> handler;

    @BeforeEach
    public void setUp() {
        mockPersistenceHandler = mock(PersistenceHandler.class);
        //noinspection unchecked,rawtypes
        when(mockPersistenceHandler
                .getTypeHandler(Float.class.getCanonicalName()))
                .thenReturn((TypeHandler) MOCK_FLOAT_HANDLER);

        mockValuesAtTimestamps = generateMockMap(
                pairOf(TIMESTAMP_1, VALUE_1),
                pairOf(TIMESTAMP_2, VALUE_2),
                pairOf(TIMESTAMP_3, VALUE_3)
        );

        //noinspection unchecked
        mockFiniteLinearMovingProvider =
                (FiniteLinearMovingProvider<Float>) mock(FiniteLinearMovingProvider.class);
        when(mockFiniteLinearMovingProvider.uuid()).thenReturn(UUID);
        when(mockFiniteLinearMovingProvider.valuesAtTimestampsRepresentation())
                .thenReturn(mockValuesAtTimestamps);
        when(mockFiniteLinearMovingProvider.pausedTimestamp()).thenReturn(PAUSED_TIMESTAMP);
        when(mockFiniteLinearMovingProvider.mostRecentTimestamp()).thenReturn(
                MOST_RECENT_TIMESTAMP);

        mockFactory = mock(FiniteLinearMovingProviderFactory.class);
        //noinspection unchecked,rawtypes
        when(mockFactory.make(any(), anyMap(), anyLong(), anyLong()))
                .thenReturn((FiniteLinearMovingProvider) mockFiniteLinearMovingProvider);

        handler = new FiniteLinearMovingProviderHandler(mockPersistenceHandler, mockFactory);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteLinearMovingProviderHandler(null, mockFactory));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteLinearMovingProviderHandler(mockPersistenceHandler, null));
    }

    @Test
    public void testWrite() {
        var writtenValue = handler.write(mockFiniteLinearMovingProvider);

        assertEquals(WRITTEN_VALUE, writtenValue);

        var inOrder = inOrder(mockFiniteLinearMovingProvider, mockPersistenceHandler,
                mockValuesAtTimestamps, MOCK_FLOAT_HANDLER);
        inOrder.verify(mockFiniteLinearMovingProvider).uuid();
        inOrder.verify(mockFiniteLinearMovingProvider).valuesAtTimestampsRepresentation();
        inOrder.verify(mockPersistenceHandler).getTypeHandler(Float.class.getCanonicalName());
        //noinspection ResultOfMethodCallIgnored
        inOrder.verify(mockValuesAtTimestamps).size();
        inOrder.verify(MOCK_FLOAT_HANDLER).write(VALUE_1);
        inOrder.verify(MOCK_FLOAT_HANDLER).write(VALUE_2);
        inOrder.verify(MOCK_FLOAT_HANDLER).write(VALUE_3);
        inOrder.verify(mockFiniteLinearMovingProvider).pausedTimestamp();
        inOrder.verify(mockFiniteLinearMovingProvider).mostRecentTimestamp();
    }

    @Test
    public void testWriteWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> handler.write(null));
    }

    @Test
    public void testRead() {
        var output = handler.read(WRITTEN_VALUE);

        assertSame(mockFiniteLinearMovingProvider, output);
        var inOrder = inOrder(mockPersistenceHandler, MOCK_FLOAT_HANDLER, mockFactory);
        inOrder.verify(mockPersistenceHandler).getTypeHandler(Float.class.getCanonicalName());
        inOrder.verify(MOCK_FLOAT_HANDLER).read(FLOAT_WRITE_OUTPUT_1);
        inOrder.verify(MOCK_FLOAT_HANDLER).read(FLOAT_WRITE_OUTPUT_2);
        inOrder.verify(MOCK_FLOAT_HANDLER).read(FLOAT_WRITE_OUTPUT_3);
        var factoryCaptor = ArgumentCaptor.forClass(Map.class);
        //noinspection unchecked
        inOrder.verify(mockFactory)
                .make(eq(UUID), (Map<Long, Float>) factoryCaptor.capture(), eq(PAUSED_TIMESTAMP),
                        eq(MOST_RECENT_TIMESTAMP));
        //noinspection unchecked
        var factoryValuesAtTimestamps = (Map<Long, Float>) factoryCaptor.getValue();
        assertEquals(3, factoryValuesAtTimestamps.size());
        assertEquals(VALUE_1, factoryValuesAtTimestamps.get(TIMESTAMP_1));
        assertEquals(VALUE_2, factoryValuesAtTimestamps.get(TIMESTAMP_2));
        assertEquals(VALUE_3, factoryValuesAtTimestamps.get(TIMESTAMP_3));
    }

    @Test
    public void testReadWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                handler.read(null));
        assertThrows(IllegalArgumentException.class, () ->
                handler.read(""));
    }
}
