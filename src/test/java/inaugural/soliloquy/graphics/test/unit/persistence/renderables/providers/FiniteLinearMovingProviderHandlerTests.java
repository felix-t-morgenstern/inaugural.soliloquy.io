package inaugural.soliloquy.graphics.test.unit.persistence.renderables.providers;

import inaugural.soliloquy.graphics.persistence.renderables.providers.FiniteLinearMovingProviderHandler;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeFiniteLinearMovingProvider;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeFiniteLinearMovingProviderFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import soliloquy.specs.common.persistence.PersistentValuesHandler;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.common.persistence.TypeWithOneGenericParamHandler;
import soliloquy.specs.graphics.renderables.providers.FiniteLinearMovingProvider;

import java.util.HashMap;
import java.util.UUID;

import static inaugural.soliloquy.tools.testing.Assertions.assertOnlyContains;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FiniteLinearMovingProviderHandlerTests {
    private static final Long TIMESTAMP_1 = 123L;
    private static final Long TIMESTAMP_2 = 456L;
    private static final Long TIMESTAMP_3 = 789L;
    private static final Float VALUE_1 = 0.123f;
    private static final Float VALUE_2 = 0.456f;
    private static final Float VALUE_3 = 0.789f;
    private static final HashMap<Long, Float> VALUES_AT_TIMESTAMPS = new HashMap<>() {{
        put(TIMESTAMP_1, VALUE_1);
        put(TIMESTAMP_2, VALUE_2);
        put(TIMESTAMP_3, VALUE_3);
    }};
    private static final Long PAUSED_TIMESTAMP = 123L;
    private static final Long MOST_RECENT_TIMESTAMP = 456L;
    private FakeFiniteLinearMovingProvider<Float> finiteLinearMovingProvider;

    private static final UUID UUID = java.util.UUID.randomUUID();
    private static final float FLOAT_READ_OUTPUT = 0.1312f;
    private static final String FLOAT_WRITE_OUTPUT = "floatWriteOutput";

    private static final int INTEGER_ARCHETYPE = 1312;

    private static final FakeFiniteLinearMovingProviderFactory FACTORY =
            new FakeFiniteLinearMovingProviderFactory();

    // TODO: Mock Map's iterator to make
    private static final String WRITTEN_VALUE = String.format(
            "{\"uuid\":\"%s\",\"valueType\":\"java.lang.Float\"," +
                    "\"values\":[{\"timestamp\":456,\"value\":\"floatWriteOutput\"}," +
                    "{\"timestamp\":789,\"value\":\"floatWriteOutput\"},{\"timestamp\":123," +
                    "\"value\":\"floatWriteOutput\"}],\"pausedTimestamp\":123," +
                    "\"mostRecentTimestamp\":456}", UUID);

    @Mock private PersistentValuesHandler persistentValuesHandler;
    @Mock private TypeHandler<Float> floatHandler;

    @SuppressWarnings("rawtypes")
    private TypeWithOneGenericParamHandler<FiniteLinearMovingProvider>
            finiteLinearMovingProviderHandler;

    @BeforeEach
    void setUp() {
        //noinspection unchecked
        floatHandler = Mockito.mock(TypeHandler.class);
        when(floatHandler.read(Mockito.anyString())).thenReturn(FLOAT_READ_OUTPUT);
        when(floatHandler.write(Mockito.anyFloat())).thenReturn(FLOAT_WRITE_OUTPUT);

        persistentValuesHandler = Mockito.mock(PersistentValuesHandler.class);

        //noinspection unchecked,rawtypes
        when(persistentValuesHandler
                .getTypeHandler(Float.class.getCanonicalName()))
                .thenReturn((TypeHandler) floatHandler);

        when(persistentValuesHandler
                .generateArchetype(Integer.class.getCanonicalName()))
                .thenReturn(INTEGER_ARCHETYPE);

        finiteLinearMovingProvider =
                new FakeFiniteLinearMovingProvider<>(UUID, VALUES_AT_TIMESTAMPS, PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP, -123123f);

        finiteLinearMovingProviderHandler =
                new FiniteLinearMovingProviderHandler(persistentValuesHandler, FACTORY);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteLinearMovingProviderHandler(null, FACTORY));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteLinearMovingProviderHandler(persistentValuesHandler, null));
    }

    @Test
    void testWrite() {
        var writtenValue = finiteLinearMovingProviderHandler.write(finiteLinearMovingProvider);

        assertEquals(WRITTEN_VALUE, writtenValue);
        verify(floatHandler).write(VALUE_1);
        verify(floatHandler).write(VALUE_2);
        verify(floatHandler).write(VALUE_3);
    }

    @Test
    void testWriteWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> finiteLinearMovingProviderHandler.write(null));
    }

    @Test
    void testRead() {
        //noinspection unchecked
        FiniteLinearMovingProvider<Float> finiteLinearMovingProvider =
                finiteLinearMovingProviderHandler.read(WRITTEN_VALUE);

        assertNotNull(finiteLinearMovingProvider);
        assertOnlyContains(FACTORY.InputUuids, UUID);
        assertEquals(1, FACTORY.InputValuesAtTimestamps.size());
        assertEquals(3, FACTORY.InputValuesAtTimestamps.get(0).size());
        assertEquals(FLOAT_READ_OUTPUT, FACTORY.InputValuesAtTimestamps.get(0).get(TIMESTAMP_1));
        assertEquals(FLOAT_READ_OUTPUT, FACTORY.InputValuesAtTimestamps.get(0).get(TIMESTAMP_2));
        assertEquals(FLOAT_READ_OUTPUT, FACTORY.InputValuesAtTimestamps.get(0).get(TIMESTAMP_3));
        assertOnlyContains(FACTORY.InputPausedTimestamps, PAUSED_TIMESTAMP);
        assertOnlyContains(FACTORY.InputMostRecentTimestamps, MOST_RECENT_TIMESTAMP);
        assertOnlyContains(FACTORY.Outputs, finiteLinearMovingProvider);
        verify(persistentValuesHandler).getTypeHandler(Float.class.getCanonicalName());
        verify(floatHandler, times(3)).read(FLOAT_WRITE_OUTPUT);
    }

    @Test
    void testReadWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingProviderHandler.read(null));
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingProviderHandler.read(""));
    }

    @Test
    void testGenerateArchetype() {
        //noinspection unchecked
        FiniteLinearMovingProvider<Integer> generatedArchetype =
                (FiniteLinearMovingProvider<Integer>) finiteLinearMovingProviderHandler
                        .generateArchetype(Integer.class.getCanonicalName());

        assertNotNull(generatedArchetype);
        assertNotNull(generatedArchetype.getArchetype());
    }

    @Test
    void testGenerateArchetypeWithInvalidInputs() {
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingProviderHandler.generateArchetype(null));
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingProviderHandler.generateArchetype(""));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(TypeHandler.class.getCanonicalName() + "<" +
                        FiniteLinearMovingProvider.class.getCanonicalName() + ">",
                finiteLinearMovingProviderHandler.getInterfaceName());
    }
}
