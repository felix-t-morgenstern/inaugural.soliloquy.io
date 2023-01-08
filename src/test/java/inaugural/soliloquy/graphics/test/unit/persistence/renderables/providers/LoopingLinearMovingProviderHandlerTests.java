package inaugural.soliloquy.graphics.test.unit.persistence.renderables.providers;

import inaugural.soliloquy.graphics.persistence.renderables.providers.LoopingLinearMovingProviderHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import soliloquy.specs.common.persistence.PersistentValuesHandler;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.common.persistence.TypeWithOneGenericParamHandler;
import soliloquy.specs.common.valueobjects.Pair;
import soliloquy.specs.graphics.renderables.providers.LoopingLinearMovingProvider;
import soliloquy.specs.graphics.renderables.providers.factories.LoopingLinearMovingProviderFactory;

import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Mock.generateSimpleMockTypeHandler;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class LoopingLinearMovingProviderHandlerTests {
    private static final UUID UUID = java.util.UUID.randomUUID();
    private static final int TIMESTAMP_1 = randomInt();
    private static final int TIMESTAMP_2 = randomInt();
    private static final int TIMESTAMP_3 = randomInt();
    private static final Float VALUE_1 = randomFloat();
    private static final Float VALUE_2 = randomFloat();
    private static final Float VALUE_3 = randomFloat();
    private static final String VALUE_1_WRITTEN = randomString();
    private static final String VALUE_2_WRITTEN = randomString();
    private static final String VALUE_3_WRITTEN = randomString();
    private static final Map<Integer, Float> VALUES_WITHIN_PERIOD =
            mapOf(Pair.of(TIMESTAMP_1, VALUE_1), Pair.of(TIMESTAMP_2, VALUE_2),
                    Pair.of(TIMESTAMP_3, VALUE_3));
    private static final int PERIOD_DURATION = randomInt();
    private static final int PERIOD_MODULO_OFFSET = randomInt();
    private static final Long PAUSED_TIMESTAMP = randomLong();
    private static final Long MOST_RECENT_TIMESTAMP = randomLong();

    @SuppressWarnings("unchecked") private final TypeHandler<Float> FLOAT_HANDLER =
            generateSimpleMockTypeHandler(Pair.of(VALUE_1_WRITTEN, VALUE_1),
                    Pair.of(VALUE_2_WRITTEN, VALUE_2), Pair.of(VALUE_3_WRITTEN, VALUE_3));

    private static final int INTEGER_ARCHETYPE = randomInt();
    private static final float FLOAT_ARCHETYPE = randomFloat();

    private static final String WRITTEN_VALUE = String.format(
            "{\"id\":\"%s\",\"duration\":%d,\"offset\":%d,\"valueAtTimes\":[{\"time\":%d," +
                    "\"value\":\"%s\"},{\"time\":%d,\"value\":\"%s\"},{\"time\":%d," +
                    "\"value\":\"%s\"}],\"pausedTimestamp\":%d,\"mostRecentTimestamp\":%d," +
                    "\"type\":\"java.lang.Float\"}",
            UUID, PERIOD_DURATION, PERIOD_MODULO_OFFSET, TIMESTAMP_3, VALUE_3_WRITTEN, TIMESTAMP_2,
            VALUE_2_WRITTEN, TIMESTAMP_1, VALUE_1_WRITTEN, PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP);

    @Mock private PersistentValuesHandler persistentValuesHandler;

    /** @noinspection rawtypes */
    @Mock private LoopingLinearMovingProvider mockLoopingLinearMovingProvider;
    /** @noinspection rawtypes */
    @Mock private LoopingLinearMovingProvider mockLoopingLinearMovingProviderFactoryOutput;
    @Mock private LoopingLinearMovingProviderFactory mockFactory;

    /** @noinspection rawtypes */
    private TypeWithOneGenericParamHandler<LoopingLinearMovingProvider>
            loopingLinearMovingProviderHandler;

    @BeforeEach
    void setUp() {
        mockLoopingLinearMovingProviderFactoryOutput = mock(LoopingLinearMovingProvider.class);

        mockFactory = mock(LoopingLinearMovingProviderFactory.class);
        //noinspection unchecked
        when(mockFactory
                .make(any(), anyInt(), anyInt(), anyMap(), anyLong(), anyLong(), any()))
                .thenReturn(mockLoopingLinearMovingProviderFactoryOutput);

        mockLoopingLinearMovingProvider = mock(LoopingLinearMovingProvider.class);
        when(mockLoopingLinearMovingProvider.uuid()).thenReturn(UUID);
        when(mockLoopingLinearMovingProvider.periodDuration())
                .thenReturn(PERIOD_DURATION);
        when(mockLoopingLinearMovingProvider.periodModuloOffset())
                .thenReturn(PERIOD_MODULO_OFFSET);
        when(mockLoopingLinearMovingProvider.valuesWithinPeriod())
                .thenReturn(VALUES_WITHIN_PERIOD);
        when(mockLoopingLinearMovingProvider.pausedTimestamp())
                .thenReturn(PAUSED_TIMESTAMP);
        when(mockLoopingLinearMovingProvider.mostRecentTimestamp())
                .thenReturn(MOST_RECENT_TIMESTAMP);
        when(mockLoopingLinearMovingProvider.getArchetype())
                .thenReturn(0f);

        persistentValuesHandler = Mockito.mock(PersistentValuesHandler.class);

        //noinspection unchecked,rawtypes
        when(persistentValuesHandler
                .getTypeHandler(Float.class.getCanonicalName()))
                .thenReturn((TypeHandler) FLOAT_HANDLER);

        when(persistentValuesHandler
                .generateArchetype(Integer.class.getCanonicalName()))
                .thenReturn(INTEGER_ARCHETYPE);
        when(persistentValuesHandler
                .generateArchetype(Float.class.getCanonicalName()))
                .thenReturn(FLOAT_ARCHETYPE);

        loopingLinearMovingProviderHandler =
                new LoopingLinearMovingProviderHandler(persistentValuesHandler, mockFactory);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingProviderHandler(null, mockFactory));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingProviderHandler(persistentValuesHandler, null));
    }

    @Test
    void testWrite() {
        var writtenValue =
                loopingLinearMovingProviderHandler.write(mockLoopingLinearMovingProvider);

        assertEquals(WRITTEN_VALUE, writtenValue);
        verify(FLOAT_HANDLER, times(1)).write(VALUE_1);
        verify(FLOAT_HANDLER, times(1)).write(VALUE_2);
        verify(FLOAT_HANDLER, times(1)).write(VALUE_3);
    }

    @Test
    void testWriteWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingProviderHandler.write(null));
    }

    @Test
    void testRead() {
        //noinspection unchecked
        LoopingLinearMovingProvider<Float> loopingLinearMovingProvider =
                loopingLinearMovingProviderHandler.read(WRITTEN_VALUE);

        assertNotNull(loopingLinearMovingProvider);
        verify(mockFactory, times(1)).make(UUID, PERIOD_DURATION, PERIOD_MODULO_OFFSET,
                mapOf(Pair.of(TIMESTAMP_1, VALUE_1), Pair.of(TIMESTAMP_2, VALUE_2),
                        Pair.of(TIMESTAMP_3, VALUE_3)), PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP,
                FLOAT_ARCHETYPE);
        verify(persistentValuesHandler).getTypeHandler(Float.class.getCanonicalName());
        verify(FLOAT_HANDLER, times(1)).read(VALUE_1_WRITTEN);
        verify(FLOAT_HANDLER, times(1)).read(VALUE_2_WRITTEN);
        verify(FLOAT_HANDLER, times(1)).read(VALUE_3_WRITTEN);
    }

    @Test
    void testReadWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingProviderHandler.read(null));
        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingProviderHandler.read(""));
    }

    @Test
    void testGenerateArchetype() {
        //noinspection unchecked
        var generatedArchetype =
                (LoopingLinearMovingProvider<Integer>) loopingLinearMovingProviderHandler.generateArchetype(
                        Integer.class.getCanonicalName());

        assertNotNull(generatedArchetype);
        assertNotNull(generatedArchetype.getArchetype());
    }

    @Test
    void testGenerateArchetypeWithInvalidInputs() {
        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingProviderHandler.generateArchetype(null));
        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingProviderHandler.generateArchetype(""));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(TypeHandler.class.getCanonicalName() + "<" +
                        LoopingLinearMovingProvider.class.getCanonicalName() + ">",
                loopingLinearMovingProviderHandler.getInterfaceName());
    }
}
