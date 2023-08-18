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
import soliloquy.specs.common.valueobjects.Pair;
import soliloquy.specs.graphics.renderables.providers.StaticProvider;

import java.util.UUID;

import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Mock.generateSimpleMockTypeHandler;
import static inaugural.soliloquy.tools.valueobjects.Pair.pairOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StaticProviderHandlerTests {
    private final FakeStaticProviderFactory FACTORY = new FakeStaticProviderFactory();
    private final int INT_VALUE = randomInt();
    private final String WRITTEN_INT = randomString();
    private final TypeHandler<Integer> INT_HANDLER =
            generateSimpleMockTypeHandler(pairOf(WRITTEN_INT, INT_VALUE));
    private final int INTEGER_ARCHETYPE = randomInt();
    private final long MOST_RECENT_TIMESTAMP = randomLong();
    private final UUID UUID = java.util.UUID.randomUUID();

    private FakeStaticProvider<Integer> staticProvider;

    private final String WRITTEN_VALUE = String.format(
            "{\"uuid\":\"%s\",\"innerType\":\"java.lang.Integer\",\"val\":\"%s\"," +
                    "\"mostRecentTimestamp\":%d}",
            UUID, WRITTEN_INT, MOST_RECENT_TIMESTAMP);

    @Mock private PersistentValuesHandler persistentValuesHandler;

    /** @noinspection rawtypes */
    private TypeWithOneGenericParamHandler<StaticProvider> _staticProviderHandler;

    @BeforeEach
    void setUp() {
        persistentValuesHandler = Mockito.mock(PersistentValuesHandler.class);
        //noinspection unchecked,rawtypes
        when(persistentValuesHandler
                .getTypeHandler(Integer.class.getCanonicalName()))
                .thenReturn((TypeHandler) INT_HANDLER);
        when(persistentValuesHandler.generateArchetype(Integer.class.getCanonicalName()))
                .thenReturn(INTEGER_ARCHETYPE);

        staticProvider = new FakeStaticProvider<>(INT_VALUE, UUID);

        _staticProviderHandler = new StaticProviderHandler(persistentValuesHandler, FACTORY);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new StaticProviderHandler(null, FACTORY));
        assertThrows(IllegalArgumentException.class,
                () -> new StaticProviderHandler(persistentValuesHandler, null));
    }

    @Test
    void testArchetype() {
        assertNotNull(_staticProviderHandler.archetype());
        assertEquals(StaticProvider.class.getCanonicalName(),
                _staticProviderHandler.archetype().getInterfaceName());
    }

    @Test
    void testWrite() {
        staticProvider.MostRecentTimestamp = MOST_RECENT_TIMESTAMP;

        var writtenValue = _staticProviderHandler.write(staticProvider);

        assertEquals(WRITTEN_VALUE, writtenValue);
        assertEquals(1, staticProvider.TimestampInputs.size());
        assertEquals(MOST_RECENT_TIMESTAMP, (long) staticProvider.TimestampInputs.get(0));
        verify(INT_HANDLER, times(1)).write(INT_VALUE);
    }

    @Test
    void testWriteWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _staticProviderHandler.write(null));
    }

    @Test
    void testRead() {
        //noinspection unchecked
        var staticProvider = (StaticProvider<Integer>) _staticProviderHandler.read(WRITTEN_VALUE);

        assertNotNull(staticProvider);
        assertEquals(UUID, staticProvider.uuid());
        assertEquals(INT_VALUE, (int) staticProvider.provide(randomLong()));
        assertEquals(MOST_RECENT_TIMESTAMP, (long) staticProvider.mostRecentTimestamp());
        verify(INT_HANDLER, times(1)).read(WRITTEN_INT);
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
        assertNotNull(generatedArchetype.archetype());
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
        assertEquals(TypeHandler.class.getCanonicalName() + "<" +
                        StaticProvider.class.getCanonicalName() + ">",
                _staticProviderHandler.getInterfaceName());
    }
}
