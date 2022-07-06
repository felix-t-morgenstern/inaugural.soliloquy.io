package inaugural.soliloquy.graphics.test.unit.persistence.renderables.providers;

import inaugural.soliloquy.graphics.persistence.renderables.providers.StaticProviderHandler;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeStaticProvider;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeStaticProviderFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import soliloquy.specs.common.persistence.PersistentValuesHandler;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.common.persistence.TypeWithOneGenericParamHandler;
import soliloquy.specs.graphics.renderables.providers.StaticProvider;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StaticProviderHandlerTests {
    private static final int STATIC_PROVIDER_INPUT_VALUE = 123123;
    private FakeStaticProvider<Integer> _staticProvider;
    private static final FakeStaticProviderFactory STATIC_PROVIDER_FACTORY =
            new FakeStaticProviderFactory();
    private static final int INTEGER_READ_OUTPUT = 456456;
    private static final String INTEGER_WRITE_OUTPUT = "integerWriteOutput";
    private static final int INTEGER_ARCHETYPE = 1312;
    private static final String UUID_WRITE_OUTPUT = "uuidWriteOutput";
    private static final long MOST_RECENT_TIMESTAMP = 789789L;
    private static final UUID STATIC_PROVIDER_INPUT_UUID = UUID.randomUUID();
    private static final UUID UUID_READ_OUTPUT = UUID.randomUUID();

    private static final String WRITTEN_VALUE = "{\"uuid\":\"uuidWriteOutput\",\"innerType\":\"java.lang.Integer\",\"val\":\"integerWriteOutput\",\"mostRecentTimestamp\":789789}";

    @Mock private PersistentValuesHandler _persistentValuesHandler;
    @Mock private TypeHandler<Integer> _integerHandler;
    @Mock private TypeHandler<UUID> _uuidHandler;

    /** @noinspection rawtypes*/
    private TypeWithOneGenericParamHandler<StaticProvider> _staticProviderHandler;

    @BeforeEach
    void setUp() {
        _persistentValuesHandler = Mockito.mock(PersistentValuesHandler.class);
        //noinspection unchecked
        _integerHandler = Mockito.mock(TypeHandler.class);
        when(_integerHandler.read(Mockito.anyString())).thenReturn(INTEGER_READ_OUTPUT);
        when(_integerHandler.write(Mockito.anyInt())).thenReturn(INTEGER_WRITE_OUTPUT);
        //noinspection unchecked
        _uuidHandler = Mockito.mock(TypeHandler.class);
        when(_uuidHandler.read(Mockito.anyString())).thenReturn(UUID_READ_OUTPUT);
        when(_uuidHandler.write(Mockito.any())).thenReturn(UUID_WRITE_OUTPUT);
        //noinspection unchecked,rawtypes
        when(_persistentValuesHandler
                .getTypeHandler(Integer.class.getCanonicalName()))
                .thenReturn((TypeHandler) _integerHandler);
        //noinspection unchecked,rawtypes
        when(_persistentValuesHandler
                .getTypeHandler(UUID.class.getCanonicalName()))
                .thenReturn((TypeHandler) _uuidHandler);
        when(_persistentValuesHandler.generateArchetype(Integer.class.getCanonicalName()))
                .thenReturn(INTEGER_ARCHETYPE);

        _staticProvider =
                new FakeStaticProvider<>(STATIC_PROVIDER_INPUT_VALUE, STATIC_PROVIDER_INPUT_UUID);

        _staticProviderHandler = new StaticProviderHandler(
                _uuidHandler, _persistentValuesHandler, STATIC_PROVIDER_FACTORY);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                new StaticProviderHandler(null,
                        _persistentValuesHandler, STATIC_PROVIDER_FACTORY));
        assertThrows(IllegalArgumentException.class, () ->
                new StaticProviderHandler(_uuidHandler,
                        null, STATIC_PROVIDER_FACTORY));
        assertThrows(IllegalArgumentException.class, () ->
                new StaticProviderHandler(_uuidHandler,
                        _persistentValuesHandler, null));
    }

    @Test
    void testGetArchetype() {
        assertNotNull(_staticProviderHandler.getArchetype());
        assertEquals(StaticProvider.class.getCanonicalName(),
                _staticProviderHandler.getArchetype().getInterfaceName());
    }

    @Test
    void testWrite() {
        _staticProvider.MostRecentTimestamp = MOST_RECENT_TIMESTAMP;

        String writtenValue = _staticProviderHandler.write(_staticProvider);

        assertEquals(WRITTEN_VALUE, writtenValue);
        assertEquals(1, _staticProvider.TimestampInputs.size());
        assertEquals(MOST_RECENT_TIMESTAMP, (long) _staticProvider.TimestampInputs.get(0));
        verify(_integerHandler).write(STATIC_PROVIDER_INPUT_VALUE);
        verify(_uuidHandler).write(STATIC_PROVIDER_INPUT_UUID);
    }

    @Test
    void testWriteWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _staticProviderHandler.write(null));
    }

    @Test
    void testRead() {
        //noinspection unchecked
        StaticProvider<Integer> staticProvider =
                (StaticProvider<Integer>) _staticProviderHandler.read(WRITTEN_VALUE);

        assertNotNull(staticProvider);
        assertSame(UUID_READ_OUTPUT, staticProvider.uuid());
        assertEquals(INTEGER_READ_OUTPUT, (int)staticProvider.provide(123123L));
        assertEquals(MOST_RECENT_TIMESTAMP, (long)staticProvider.mostRecentTimestamp());
        verify(_integerHandler).read(INTEGER_WRITE_OUTPUT);
        verify(_uuidHandler).read(UUID_WRITE_OUTPUT);
    }

    @Test
    void testReadWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _staticProviderHandler.read(null));
        assertThrows(IllegalArgumentException.class, () -> _staticProviderHandler.read(""));
    }

    @Test
    void testGenerateArchetype() {
        //noinspection unchecked
        StaticProvider<Integer> generatedArchetype =
                (StaticProvider<Integer>) _staticProviderHandler
                        .generateArchetype(Integer.class.getCanonicalName());

        assertNotNull(generatedArchetype);
        assertNotNull(generatedArchetype.getArchetype());
    }

    @Test
    void testGenerateArchetypeWithInvalidInputs() {
        assertThrows(IllegalArgumentException.class, () ->
                _staticProviderHandler.generateArchetype(null));
        assertThrows(IllegalArgumentException.class, () ->
                _staticProviderHandler.generateArchetype(""));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(TypeHandler.class.getCanonicalName() +"<" +
                        StaticProvider.class.getCanonicalName() + ">",
                _staticProviderHandler.getInterfaceName());
    }
}
