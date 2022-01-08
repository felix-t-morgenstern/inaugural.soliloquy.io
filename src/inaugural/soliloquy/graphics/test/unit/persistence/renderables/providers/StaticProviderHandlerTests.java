package inaugural.soliloquy.graphics.test.unit.persistence.renderables.providers;

import inaugural.soliloquy.graphics.persistence.renderables.providers.StaticProviderHandler;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeEntityUuid;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeStaticProvider;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeStaticProviderFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import soliloquy.specs.common.persistence.PersistentValuesHandler;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.common.persistence.TypeWithOneGenericParamHandler;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.providers.StaticProvider;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class StaticProviderHandlerTests {
    private static final int STATIC_PROVIDER_INPUT_VALUE = 123123;
    private static final FakeEntityUuid STATIC_PROVIDER_INPUT_UUID = new FakeEntityUuid();
    private static final FakeStaticProvider<Integer> STATIC_PROVIDER =
            new FakeStaticProvider<>(STATIC_PROVIDER_INPUT_VALUE, STATIC_PROVIDER_INPUT_UUID);
    private static final FakeStaticProviderFactory STATIC_PROVIDER_FACTORY =
            new FakeStaticProviderFactory();
    private static final int INTEGER_READ_OUTPUT = 456456;
    private static final String INTEGER_WRITE_OUTPUT = "integerWriteOutput";
    private static final int INTEGER_ARCHETYPE = 1312;
    private static final FakeEntityUuid UUID_READ_OUTPUT = new FakeEntityUuid();
    private static final String UUID_WRITE_OUTPUT = "uuidWriteOutput";
    private static final long MOST_RECENT_TIMESTAMP = 789789L;

    private static final String WRITTEN_VALUE = "{\"uuid\":\"uuidWriteOutput\",\"innerType\":\"java.lang.Integer\",\"val\":\"integerWriteOutput\",\"mostRecentTimestamp\":789789}";

    @Mock
    private PersistentValuesHandler _persistentValuesHandler;
    @Mock
    private TypeHandler<Integer> _integerHandler;
    @Mock
    private TypeHandler<EntityUuid> _entityUuidHandler;

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
        _entityUuidHandler = Mockito.mock(TypeHandler.class);
        when(_entityUuidHandler.read(Mockito.anyString())).thenReturn(UUID_READ_OUTPUT);
        when(_entityUuidHandler.write(Mockito.any())).thenReturn(UUID_WRITE_OUTPUT);
        //noinspection unchecked,rawtypes
        when(_persistentValuesHandler
                .getTypeHandler(Integer.class.getCanonicalName()))
                .thenReturn((TypeHandler) _integerHandler);
        //noinspection unchecked,rawtypes
        when(_persistentValuesHandler
                .getTypeHandler(EntityUuid.class.getCanonicalName()))
                .thenReturn((TypeHandler) _entityUuidHandler);
        when(_persistentValuesHandler.generateArchetype(Integer.class.getCanonicalName()))
                .thenReturn(INTEGER_ARCHETYPE);

        _staticProviderHandler = new StaticProviderHandler(
                _entityUuidHandler, _persistentValuesHandler, STATIC_PROVIDER_FACTORY);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                new StaticProviderHandler(null,
                        _persistentValuesHandler, STATIC_PROVIDER_FACTORY));
        assertThrows(IllegalArgumentException.class, () ->
                new StaticProviderHandler(_entityUuidHandler,
                        null, STATIC_PROVIDER_FACTORY));
        assertThrows(IllegalArgumentException.class, () ->
                new StaticProviderHandler(_entityUuidHandler,
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
        STATIC_PROVIDER.MostRecentTimestamp = MOST_RECENT_TIMESTAMP;

        String writtenValue = _staticProviderHandler.write(STATIC_PROVIDER);

        assertEquals(WRITTEN_VALUE, writtenValue);
        assertEquals(1, STATIC_PROVIDER.TimestampInputs.size());
        assertEquals(MOST_RECENT_TIMESTAMP, (long)STATIC_PROVIDER.TimestampInputs.get(0));
        verify(_integerHandler).write(STATIC_PROVIDER_INPUT_VALUE);
        verify(_entityUuidHandler).write(STATIC_PROVIDER_INPUT_UUID);
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
        verify(_entityUuidHandler).read(UUID_WRITE_OUTPUT);
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
