package inaugural.soliloquy.graphics.test.unit.persistence.renderables.providers;

import inaugural.soliloquy.graphics.persistence.renderables.providers.LoopingLinearMovingProviderHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import soliloquy.specs.common.persistence.PersistentValuesHandler;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.common.persistence.TypeWithOneGenericParamHandler;
import soliloquy.specs.graphics.renderables.providers.LoopingLinearMovingProvider;
import soliloquy.specs.graphics.renderables.providers.factories.LoopingLinearMovingProviderFactory;

import java.util.HashMap;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class LoopingLinearMovingProviderHandlerTests {
    private static final int TIMESTAMP_1 = 123;
    private static final int TIMESTAMP_2 = 456;
    private static final int TIMESTAMP_3 = 789;
    private static final Float VALUE_1 = 0.123f;
    private static final Float VALUE_2 = 0.456f;
    private static final Float VALUE_3 = 0.789f;
    private static final HashMap<Integer, Float> VALUES_WITHIN_PERIOD =
            new HashMap<Integer, Float>() {{
                put(TIMESTAMP_1, VALUE_1);
                put(TIMESTAMP_2, VALUE_2);
                put(TIMESTAMP_3, VALUE_3);
    }};
    private static final int PERIOD_DURATION = 1312;
    private static final int PERIOD_MODULO_OFFSET = 1234;
    private static final Long PAUSED_TIMESTAMP = 123L;
    private static final Long MOST_RECENT_TIMESTAMP = 456L;

    private static final UUID LOOPING_LINEAR_MOVING_PROVIDER_INPUT_UUID = UUID.randomUUID();
    private static final UUID UUID_READ_OUTPUT = UUID.randomUUID();
    private static final String UUID_WRITE_OUTPUT = "uuidWriteOutput";
    private static final float FLOAT_READ_OUTPUT = 0.1312f;
    private static final String FLOAT_WRITE_OUTPUT = "floatWriteOutput";

    private static final int INTEGER_ARCHETYPE = 1312;
    private static final float FLOAT_ARCHETYPE = 0.2624f;

    private static final String WRITTEN_VALUE = "{\"id\":\"uuidWriteOutput\",\"duration\":1312,\"offset\":1234,\"valueAtTimes\":[{\"time\":789,\"value\":\"floatWriteOutput\"},{\"time\":456,\"value\":\"floatWriteOutput\"},{\"time\":123,\"value\":\"floatWriteOutput\"}],\"pausedTimestamp\":123,\"mostRecentTimestamp\":456,\"type\":\"java.lang.Float\"}";

    @Mock private PersistentValuesHandler _persistentValuesHandler;
    @Mock private TypeHandler<UUID> _uuidHandler;
    @Mock private TypeHandler<Float> _floatHandler;

    /** @noinspection rawtypes*/
    @Mock private LoopingLinearMovingProvider _mockLoopingLinearMovingProvider;
    /** @noinspection rawtypes*/
    @Mock private LoopingLinearMovingProvider _mockLoopingLinearMovingProviderFactoryOutput;
    @Mock private LoopingLinearMovingProviderFactory _mockLoopingLinearMovingProviderFactory;

    /** @noinspection rawtypes*/
    private TypeWithOneGenericParamHandler<LoopingLinearMovingProvider>
            _loopingLinearMovingProviderHandler;

    @BeforeEach
    void setUp() {
        _mockLoopingLinearMovingProviderFactoryOutput = mock(LoopingLinearMovingProvider.class);

        _mockLoopingLinearMovingProviderFactory = mock(LoopingLinearMovingProviderFactory.class);
        //noinspection unchecked
        when(_mockLoopingLinearMovingProviderFactory
                .make(any(), anyInt(), anyInt(), anyMap(), anyLong(), anyLong(), any()))
                .thenReturn(_mockLoopingLinearMovingProviderFactoryOutput);

        _mockLoopingLinearMovingProvider = mock(LoopingLinearMovingProvider.class);
        when(_mockLoopingLinearMovingProvider.uuid())
                .thenReturn(LOOPING_LINEAR_MOVING_PROVIDER_INPUT_UUID);
        when(_mockLoopingLinearMovingProvider.periodDuration())
                .thenReturn(PERIOD_DURATION);
        when(_mockLoopingLinearMovingProvider.periodModuloOffset())
                .thenReturn(PERIOD_MODULO_OFFSET);
        when(_mockLoopingLinearMovingProvider.valuesWithinPeriod())
                .thenReturn(VALUES_WITHIN_PERIOD);
        when(_mockLoopingLinearMovingProvider.pausedTimestamp())
                .thenReturn(PAUSED_TIMESTAMP);
        when(_mockLoopingLinearMovingProvider.mostRecentTimestamp())
                .thenReturn(MOST_RECENT_TIMESTAMP);
        when(_mockLoopingLinearMovingProvider.getArchetype())
                .thenReturn(0f);

        //noinspection unchecked
        _uuidHandler = Mockito.mock(TypeHandler.class);
        when(_uuidHandler.read(Mockito.anyString())).thenReturn(UUID_READ_OUTPUT);
        when(_uuidHandler.write(Mockito.any())).thenReturn(UUID_WRITE_OUTPUT);

        //noinspection unchecked
        _floatHandler = Mockito.mock(TypeHandler.class);
        when(_floatHandler.read(Mockito.anyString())).thenReturn(FLOAT_READ_OUTPUT);
        when(_floatHandler.write(Mockito.anyFloat())).thenReturn(FLOAT_WRITE_OUTPUT);

        _persistentValuesHandler = Mockito.mock(PersistentValuesHandler.class);

        //noinspection unchecked,rawtypes
        when(_persistentValuesHandler
                .getTypeHandler(UUID.class.getCanonicalName()))
                .thenReturn((TypeHandler) _uuidHandler);
        //noinspection unchecked,rawtypes
        when(_persistentValuesHandler
                .getTypeHandler(Float.class.getCanonicalName()))
                .thenReturn((TypeHandler) _floatHandler);

        when(_persistentValuesHandler
                .generateArchetype(Integer.class.getCanonicalName()))
                .thenReturn(INTEGER_ARCHETYPE);
        when(_persistentValuesHandler
                .generateArchetype(Float.class.getCanonicalName()))
                .thenReturn(FLOAT_ARCHETYPE);

        _loopingLinearMovingProviderHandler = new LoopingLinearMovingProviderHandler(_uuidHandler,
                _persistentValuesHandler, _mockLoopingLinearMovingProviderFactory);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingProviderHandler(null, _persistentValuesHandler,
                        _mockLoopingLinearMovingProviderFactory));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingProviderHandler(_uuidHandler, null,
                        _mockLoopingLinearMovingProviderFactory));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingProviderHandler(_uuidHandler, _persistentValuesHandler,
                        null));
    }

    @Test
    void testWrite() {
        String writtenValue =
                _loopingLinearMovingProviderHandler.write(_mockLoopingLinearMovingProvider);

        assertEquals(WRITTEN_VALUE, writtenValue);
        verify(_uuidHandler).write(LOOPING_LINEAR_MOVING_PROVIDER_INPUT_UUID);
        verify(_floatHandler).write(VALUE_1);
        verify(_floatHandler).write(VALUE_2);
        verify(_floatHandler).write(VALUE_3);
    }

    @Test
    void testWriteWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingProviderHandler.write(null));
    }

    @Test
    void testRead() {
        //noinspection unchecked
        LoopingLinearMovingProvider<Float> loopingLinearMovingProvider =
                _loopingLinearMovingProviderHandler.read(WRITTEN_VALUE);

        assertNotNull(loopingLinearMovingProvider);
        verify(_mockLoopingLinearMovingProviderFactory, times(1))
                .make(UUID_READ_OUTPUT, PERIOD_DURATION, PERIOD_MODULO_OFFSET,
                        new HashMap<Integer, Float>() {{
                            put(TIMESTAMP_1, FLOAT_READ_OUTPUT);
                            put(TIMESTAMP_2, FLOAT_READ_OUTPUT);
                            put(TIMESTAMP_3, FLOAT_READ_OUTPUT);
                        }}, PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP, FLOAT_ARCHETYPE);
        verify(_uuidHandler).read(UUID_WRITE_OUTPUT);
        verify(_persistentValuesHandler).getTypeHandler(Float.class.getCanonicalName());
        verify(_floatHandler, times(3)).read(FLOAT_WRITE_OUTPUT);
    }

    @Test
    void testReadWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingProviderHandler.read(null));
        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingProviderHandler.read(""));
    }

    @Test
    void testGenerateArchetype() {
        //noinspection unchecked
        LoopingLinearMovingProvider<Integer> generatedArchetype =
                (LoopingLinearMovingProvider<Integer>) _loopingLinearMovingProviderHandler
                        .generateArchetype(Integer.class.getCanonicalName());

        assertNotNull(generatedArchetype);
        assertNotNull(generatedArchetype.getArchetype());
    }

    @Test
    void testGenerateArchetypeWithInvalidInputs() {
        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingProviderHandler.generateArchetype(null));
        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingProviderHandler.generateArchetype(""));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(TypeHandler.class.getCanonicalName() + "<" +
                        LoopingLinearMovingProvider.class.getCanonicalName() + ">",
                _loopingLinearMovingProviderHandler.getInterfaceName());
    }
}
