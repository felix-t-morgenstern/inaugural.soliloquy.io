package inaugural.soliloquy.graphics.test.unit.persistence.renderables.providers;

import inaugural.soliloquy.graphics.persistence.renderables.providers.StaticProviderHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.persistence.PersistenceHandler;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.graphics.renderables.providers.StaticProvider;
import soliloquy.specs.graphics.renderables.providers.factories.StaticProviderFactory;

import java.util.UUID;

import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static inaugural.soliloquy.tools.testing.Mock.generateSimpleMockTypeHandler;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

@ExtendWith(MockitoExtension.class)
public class StaticProviderHandlerTests {
    private final int INT_VALUE = randomInt();
    private final String WRITTEN_INT = randomString();
    private final TypeHandler<Integer> INT_HANDLER =
            generateSimpleMockTypeHandler(pairOf(WRITTEN_INT, INT_VALUE));
    private final long MOST_RECENT_TIMESTAMP = randomLong();
    private final UUID UUID = java.util.UUID.randomUUID();

    private final String WRITTEN_VALUE = String.format(
            "{\"uuid\":\"%s\",\"innerType\":\"java.lang.Integer\",\"val\":\"%s\"," +
                    "\"mostRecentTimestamp\":%d}",
            UUID, WRITTEN_INT, MOST_RECENT_TIMESTAMP);

    @Mock private StaticProvider<Integer> mockStaticProvider;
    @Mock private StaticProviderFactory mockFactory;
    @Mock private PersistenceHandler mockPersistenceHandler;

    /** @noinspection rawtypes */
    private TypeHandler<StaticProvider> staticProviderHandler;

    @BeforeEach
    public void setUp() {
        //noinspection unchecked,rawtypes
        lenient().when(mockPersistenceHandler
                .getTypeHandler(Integer.class.getCanonicalName()))
                .thenReturn((TypeHandler) INT_HANDLER);

        staticProviderHandler = new StaticProviderHandler(mockPersistenceHandler, mockFactory);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new StaticProviderHandler(null, mockFactory));
        assertThrows(IllegalArgumentException.class,
                () -> new StaticProviderHandler(mockPersistenceHandler, null));
    }

    @Test
    public void testWrite() {
        when(mockStaticProvider.uuid()).thenReturn(UUID);
        when(mockStaticProvider.provide(anyLong())).thenReturn(INT_VALUE);
        when(mockStaticProvider.mostRecentTimestamp()).thenReturn(MOST_RECENT_TIMESTAMP);

        var writtenValue = staticProviderHandler.write(mockStaticProvider);

        assertEquals(WRITTEN_VALUE, writtenValue);
        verify(INT_HANDLER, once()).write(INT_VALUE);
    }

    @Test
    public void testWriteWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> staticProviderHandler.write(null));
    }

    @Test
    public void testRead() {
        //noinspection unchecked,rawtypes
        when(mockFactory.make(any(), any(), anyLong())).thenReturn(
                (StaticProvider) mockStaticProvider);

        //noinspection unchecked
        var staticProvider = (StaticProvider<Integer>) staticProviderHandler.read(WRITTEN_VALUE);

        assertNotNull(staticProvider);
        verify(INT_HANDLER, once()).read(WRITTEN_INT);
        verify(mockFactory, once()).make(eq(UUID), eq(INT_VALUE), eq(MOST_RECENT_TIMESTAMP));
    }

    @Test
    public void testReadWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> staticProviderHandler.read(null));
        assertThrows(IllegalArgumentException.class, () -> staticProviderHandler.read(""));
    }
}
