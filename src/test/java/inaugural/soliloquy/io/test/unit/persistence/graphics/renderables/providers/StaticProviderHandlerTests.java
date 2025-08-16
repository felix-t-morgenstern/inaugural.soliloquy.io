package inaugural.soliloquy.io.test.unit.persistence.graphics.renderables.providers;

import inaugural.soliloquy.io.persistence.graphics.renderables.providers.StaticProviderHandler;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.persistence.PersistenceHandler;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.io.graphics.renderables.providers.StaticProvider;
import soliloquy.specs.io.graphics.renderables.providers.factories.StaticProviderFactory;

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

    @Mock private StaticProvider<Integer> mockProvider;
    @Mock private StaticProviderFactory mockFactory;
    @Mock private PersistenceHandler mockPersistenceHandler;
    @Mock private TimestampValidator mockTimestampValidator;

    /** @noinspection rawtypes */
    private TypeHandler<StaticProvider> handler;

    @BeforeEach
    public void setUp() {
        //noinspection unchecked,rawtypes
        lenient().when(mockPersistenceHandler
                        .getTypeHandler(Integer.class.getCanonicalName()))
                .thenReturn((TypeHandler) INT_HANDLER);

        handler = new StaticProviderHandler(mockPersistenceHandler, mockFactory,
                mockTimestampValidator);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new StaticProviderHandler(null, mockFactory, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new StaticProviderHandler(mockPersistenceHandler, null,
                        mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new StaticProviderHandler(mockPersistenceHandler, mockFactory, null));
    }

    @Test
    public void testWrite() {
        when(mockProvider.uuid()).thenReturn(UUID);
        when(mockProvider.provide(anyLong())).thenReturn(INT_VALUE);

        var writtenValue = handler.write(mockProvider);

        assertEquals(WRITTEN_VALUE, writtenValue);
        verify(INT_HANDLER, once()).write(INT_VALUE);
    }

    @Test
    public void testWriteWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> handler.write(null));
    }

    @Test
    public void testRead() {
        //noinspection unchecked,rawtypes
        when(mockFactory.make(any(), any())).thenReturn((StaticProvider) mockProvider);

        //noinspection unchecked
        var staticProvider = (StaticProvider<Integer>) handler.read(WRITTEN_VALUE);

        assertNotNull(staticProvider);
        assertSame(mockProvider, staticProvider);
        verify(INT_HANDLER, once()).read(WRITTEN_INT);
        verify(mockFactory, once()).make(eq(UUID), eq(INT_VALUE));
    }

    @Test
    public void testReadWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> handler.read(null));
        assertThrows(IllegalArgumentException.class, () -> handler.read(""));
    }
}
