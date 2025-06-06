package inaugural.soliloquy.graphics.test.unit.persistence.renderables.providers;

import inaugural.soliloquy.graphics.persistence.renderables.providers.LoopingLinearMovingProviderHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import soliloquy.specs.common.persistence.PersistenceHandler;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.graphics.renderables.providers.LoopingLinearMovingProvider;
import soliloquy.specs.graphics.renderables.providers.factories.LoopingLinearMovingProviderFactory;

import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Mock.generateMockMap;
import static inaugural.soliloquy.tools.testing.Mock.generateSimpleMockTypeHandler;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

public class LoopingLinearMovingProviderHandlerTests {
    private final UUID UUID = java.util.UUID.randomUUID();
    private final int TIMESTAMP_1 = randomInt();
    private final int TIMESTAMP_2 = randomInt();
    private final int TIMESTAMP_3 = randomInt();
    private final Float VALUE_1 = randomFloat();
    private final Float VALUE_2 = randomFloat();
    private final Float VALUE_3 = randomFloat();
    private final String VALUE_1_WRITTEN = randomString();
    private final String VALUE_2_WRITTEN = randomString();
    private final String VALUE_3_WRITTEN = randomString();
    private final int PERIOD_DURATION = randomInt();
    private final int PERIOD_MODULO_OFFSET = randomInt();
    private final Long PAUSED_TIMESTAMP = randomLong();
    private final Long MOST_RECENT_TIMESTAMP = randomLong();

    private final TypeHandler<Float> MOCK_FLOAT_HANDLER =
            generateSimpleMockTypeHandler(pairOf(VALUE_1_WRITTEN, VALUE_1),
                    pairOf(VALUE_2_WRITTEN, VALUE_2), pairOf(VALUE_3_WRITTEN, VALUE_3));

    private final String WRITTEN_VALUE = String.format(
            "{\"id\":\"%s\",\"duration\":%d,\"offset\":%d,\"valueAtTimes\":[{\"time\":%d," +
                    "\"value\":\"%s\"},{\"time\":%d,\"value\":\"%s\"},{\"time\":%d," +
                    "\"value\":\"%s\"}],\"pausedTimestamp\":%d,\"mostRecentTimestamp\":%d," +
                    "\"type\":\"java.lang.Float\"}",
            UUID, PERIOD_DURATION, PERIOD_MODULO_OFFSET, TIMESTAMP_1, VALUE_1_WRITTEN, TIMESTAMP_2,
            VALUE_2_WRITTEN, TIMESTAMP_3, VALUE_3_WRITTEN, PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP);

    @Mock private PersistenceHandler mockPersistenceHandler;

    @Mock private Map<Integer, Float> mockValuesWithinPeriod;
    /** @noinspection rawtypes */
    @Mock private LoopingLinearMovingProvider mockLoopingLinearMovingProvider;
    /** @noinspection rawtypes */
    @Mock private LoopingLinearMovingProvider mockLoopingLinearMovingProviderFactoryOutput;
    @Mock private LoopingLinearMovingProviderFactory mockFactory;

    /** @noinspection rawtypes */
    private TypeHandler<LoopingLinearMovingProvider> loopingLinearMovingProviderHandler;

    @BeforeEach
    public void setUp() {
        mockLoopingLinearMovingProviderFactoryOutput = mock(LoopingLinearMovingProvider.class);

        mockFactory = mock(LoopingLinearMovingProviderFactory.class);
        //noinspection unchecked
        when(mockFactory
                .make(any(), anyInt(), anyInt(), anyMap(), anyLong(), anyLong()))
                .thenReturn(mockLoopingLinearMovingProviderFactoryOutput);

        mockValuesWithinPeriod = generateMockMap(
                pairOf(TIMESTAMP_1, VALUE_1),
                pairOf(TIMESTAMP_2, VALUE_2),
                pairOf(TIMESTAMP_3, VALUE_3));

        mockLoopingLinearMovingProvider = mock(LoopingLinearMovingProvider.class);
        when(mockLoopingLinearMovingProvider.uuid()).thenReturn(UUID);
        when(mockLoopingLinearMovingProvider.periodDuration())
                .thenReturn(PERIOD_DURATION);
        when(mockLoopingLinearMovingProvider.periodModuloOffset())
                .thenReturn(PERIOD_MODULO_OFFSET);
        when(mockLoopingLinearMovingProvider.valuesWithinPeriod())
                .thenReturn(mockValuesWithinPeriod);
        when(mockLoopingLinearMovingProvider.pausedTimestamp())
                .thenReturn(PAUSED_TIMESTAMP);
        when(mockLoopingLinearMovingProvider.mostRecentTimestamp())
                .thenReturn(MOST_RECENT_TIMESTAMP);

        mockPersistenceHandler = Mockito.mock(PersistenceHandler.class);

        //noinspection unchecked,rawtypes
        when(mockPersistenceHandler
                .getTypeHandler(Float.class.getCanonicalName()))
                .thenReturn((TypeHandler) MOCK_FLOAT_HANDLER);

        loopingLinearMovingProviderHandler =
                new LoopingLinearMovingProviderHandler(mockPersistenceHandler, mockFactory);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingProviderHandler(null, mockFactory));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingProviderHandler(mockPersistenceHandler, null));
    }

    @Test
    public void testWrite() {
        var writtenValue =
                loopingLinearMovingProviderHandler.write(mockLoopingLinearMovingProvider);

        assertEquals(WRITTEN_VALUE, writtenValue);
        var inOrder = inOrder(mockLoopingLinearMovingProvider, mockPersistenceHandler,
                MOCK_FLOAT_HANDLER, mockValuesWithinPeriod);
        inOrder.verify(mockLoopingLinearMovingProvider).uuid();
        inOrder.verify(mockLoopingLinearMovingProvider).periodDuration();
        inOrder.verify(mockLoopingLinearMovingProvider).periodModuloOffset();
        inOrder.verify(mockPersistenceHandler).getTypeHandler(Float.class.getCanonicalName());
        inOrder.verify(mockLoopingLinearMovingProvider).valuesWithinPeriod();
        //noinspection ResultOfMethodCallIgnored
        inOrder.verify(mockValuesWithinPeriod).size();
        inOrder.verify(MOCK_FLOAT_HANDLER).write(VALUE_1);
        inOrder.verify(MOCK_FLOAT_HANDLER).write(VALUE_2);
        inOrder.verify(MOCK_FLOAT_HANDLER).write(VALUE_3);
        inOrder.verify(mockLoopingLinearMovingProvider).pausedTimestamp();
        inOrder.verify(mockLoopingLinearMovingProvider).mostRecentTimestamp();
    }

    @Test
    public void testWriteWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingProviderHandler.write(null));
    }

    @Test
    public void testRead() {
        var output = loopingLinearMovingProviderHandler.read(WRITTEN_VALUE);

        assertSame(mockLoopingLinearMovingProviderFactoryOutput, output);
        var inOrder = inOrder(mockPersistenceHandler, MOCK_FLOAT_HANDLER, mockFactory);
        inOrder.verify(mockPersistenceHandler).getTypeHandler(Float.class.getCanonicalName());
        inOrder.verify(MOCK_FLOAT_HANDLER).read(VALUE_1_WRITTEN);
        inOrder.verify(MOCK_FLOAT_HANDLER).read(VALUE_2_WRITTEN);
        inOrder.verify(MOCK_FLOAT_HANDLER).read(VALUE_3_WRITTEN);
        var valuesWithinPeriodCapture = ArgumentCaptor.forClass(Map.class);
        //noinspection unchecked
        inOrder.verify(mockFactory).make(eq(UUID), eq(PERIOD_DURATION), eq(PERIOD_MODULO_OFFSET),
                valuesWithinPeriodCapture.capture(), eq(PAUSED_TIMESTAMP),
                eq(MOST_RECENT_TIMESTAMP));
        //noinspection unchecked
        var factoryValuesWithinPeriod = (Map<Integer, Float>) valuesWithinPeriodCapture.getValue();
        assertEquals(3, factoryValuesWithinPeriod.size());
        assertEquals(VALUE_1, factoryValuesWithinPeriod.get(TIMESTAMP_1));
        assertEquals(VALUE_2, factoryValuesWithinPeriod.get(TIMESTAMP_2));
        assertEquals(VALUE_3, factoryValuesWithinPeriod.get(TIMESTAMP_3));
    }

    @Test
    public void testReadWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingProviderHandler.read(null));
        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingProviderHandler.read(""));
    }
}
