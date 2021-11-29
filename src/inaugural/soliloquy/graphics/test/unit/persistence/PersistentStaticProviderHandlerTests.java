package inaugural.soliloquy.graphics.test.unit.persistence;

import inaugural.soliloquy.graphics.persistence.PersistentStaticProviderHandler;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeEntityUuid;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeStaticProvider;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeStaticProviderFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import soliloquy.specs.common.persistence.PersistentValueTypeHandler;
import soliloquy.specs.common.persistence.PersistentValuesHandler;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.providers.StaticProvider;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PersistentStaticProviderHandlerTests {
    private static final int STATIC_PROVIDER_INPUT_VALUE = 123123;
    private static final FakeEntityUuid STATIC_PROVIDER_INPUT_UUID = new FakeEntityUuid();
    private static final FakeStaticProvider<Integer> STATIC_PROVIDER =
            new FakeStaticProvider<>(STATIC_PROVIDER_INPUT_VALUE, STATIC_PROVIDER_INPUT_UUID);
    private static final FakeStaticProviderFactory STATIC_PROVIDER_FACTORY =
            new FakeStaticProviderFactory();
    private static final int INTEGER_READ_OUTPUT = 456456;
    private static final String INTEGER_WRITE_OUTPUT = "integerWriteOutput";
    private static final FakeEntityUuid UUID_READ_OUTPUT = new FakeEntityUuid();
    private static final String UUID_WRITE_OUTPUT = "uuidWriteOutput";
    private static final long MOST_RECENT_TIMESTAMP = 789789L;

    private static final String WRITTEN_VALUE = "{\"uuid\":\"uuidWriteOutput\",\"innerType\":\"java.lang.Integer\",\"val\":\"integerWriteOutput\",\"mostRecentTimestamp\":789789}";

    @Mock
    private PersistentValuesHandler _persistentValuesHandler;
    @Mock
    private PersistentValueTypeHandler<Integer> _persistentIntegerHandler;
    @Mock
    private PersistentValueTypeHandler<EntityUuid> _persistentEntityUuidHandler;

    private PersistentValueTypeHandler<StaticProvider<?>> _persistentStaticProviderHandler;

    @BeforeEach
    void setUp() {
        _persistentValuesHandler = Mockito.mock(PersistentValuesHandler.class);
        //noinspection unchecked
        _persistentIntegerHandler = Mockito.mock(PersistentValueTypeHandler.class);
        when(_persistentIntegerHandler.read(Mockito.anyString())).thenReturn(INTEGER_READ_OUTPUT);
        when(_persistentIntegerHandler.write(Mockito.anyInt())).thenReturn(INTEGER_WRITE_OUTPUT);
        //noinspection unchecked
        _persistentEntityUuidHandler = Mockito.mock(PersistentValueTypeHandler.class);
        when(_persistentEntityUuidHandler.read(Mockito.anyString())).thenReturn(UUID_READ_OUTPUT);
        when(_persistentEntityUuidHandler.write(Mockito.any())).thenReturn(UUID_WRITE_OUTPUT);
        //noinspection unchecked,rawtypes
        when(_persistentValuesHandler
                .getPersistentValueTypeHandler(Integer.class.getCanonicalName()))
                .thenReturn((PersistentValueTypeHandler)_persistentIntegerHandler);
        //noinspection unchecked,rawtypes
        when(_persistentValuesHandler
                .getPersistentValueTypeHandler(EntityUuid.class.getCanonicalName()))
                .thenReturn((PersistentValueTypeHandler)_persistentEntityUuidHandler);

        _persistentStaticProviderHandler = new PersistentStaticProviderHandler(
                _persistentEntityUuidHandler, _persistentValuesHandler, STATIC_PROVIDER_FACTORY);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                new PersistentStaticProviderHandler(null,
                        _persistentValuesHandler, STATIC_PROVIDER_FACTORY));
        assertThrows(IllegalArgumentException.class, () ->
                new PersistentStaticProviderHandler(_persistentEntityUuidHandler,
                        null, STATIC_PROVIDER_FACTORY));
        assertThrows(IllegalArgumentException.class, () ->
                new PersistentStaticProviderHandler(_persistentEntityUuidHandler,
                        _persistentValuesHandler, null));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(PersistentValueTypeHandler.class.getCanonicalName() +"<" +
                StaticProvider.class.getCanonicalName() + ">",
                _persistentStaticProviderHandler.getInterfaceName());
    }

    @Test
    void testGetArchetype() {
        assertNotNull(_persistentStaticProviderHandler.getArchetype());
        assertEquals(StaticProvider.class.getCanonicalName(),
                _persistentStaticProviderHandler.getArchetype().getInterfaceName());
    }

    @Test
    void testWrite() {
        STATIC_PROVIDER.MostRecentTimestamp = MOST_RECENT_TIMESTAMP;

        String writtenValue = _persistentStaticProviderHandler.write(STATIC_PROVIDER);

        assertEquals(WRITTEN_VALUE, writtenValue);
        assertEquals(1, STATIC_PROVIDER.TimestampInputs.size());
        assertEquals(MOST_RECENT_TIMESTAMP, (long)STATIC_PROVIDER.TimestampInputs.get(0));
        verify(_persistentIntegerHandler).write(STATIC_PROVIDER_INPUT_VALUE);
        verify(_persistentEntityUuidHandler).write(STATIC_PROVIDER_INPUT_UUID);
    }

    @Test
    void testRead() {
        //noinspection unchecked
        StaticProvider<Integer> staticProvider =
                (StaticProvider<Integer>)_persistentStaticProviderHandler.read(WRITTEN_VALUE);

        assertNotNull(staticProvider);
        assertSame(UUID_READ_OUTPUT, staticProvider.uuid());
        assertEquals(INTEGER_READ_OUTPUT, (int)staticProvider.provide(123123L));
        assertEquals(MOST_RECENT_TIMESTAMP, (long)staticProvider.mostRecentTimestamp());
        verify(_persistentIntegerHandler).read(INTEGER_WRITE_OUTPUT);
        verify(_persistentEntityUuidHandler).read(UUID_WRITE_OUTPUT);
    }
}
