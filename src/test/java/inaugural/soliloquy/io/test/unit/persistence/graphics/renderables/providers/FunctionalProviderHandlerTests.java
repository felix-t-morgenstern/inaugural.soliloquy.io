package inaugural.soliloquy.io.test.unit.persistence.graphics.renderables.providers;

import inaugural.soliloquy.io.persistence.graphics.renderables.providers.FunctionalProviderHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.io.graphics.renderables.providers.FunctionalProvider;
import soliloquy.specs.io.graphics.renderables.providers.factories.FunctionalProviderFactory;

import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static inaugural.soliloquy.tools.testing.Mock.hydrateMockHandler;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

@ExtendWith(MockitoExtension.class)
public class FunctionalProviderHandlerTests {
    private final UUID UUID = randomUUID();
    private final String PROVIDE_ID = randomString();
    private final String PAUSE_ID = randomString();
    private final String UNPAUSE_ID = randomString();
    private final Long PAUSE_TIMESTAMP = randomLong();
    private final String DATA_WRITTEN = randomString();

    @Mock private FunctionalProviderFactory mockFactory;
    @Mock private FunctionalProvider<Integer> mockProvider;

    @SuppressWarnings("rawtypes") @Mock private Map mockData;
    @SuppressWarnings("rawtypes") @Mock private TypeHandler<Map> mockDataHandler;

    private final String WRITTEN_VALUE = String.format(
            "{\"uuid\":\"%s\",\"provideId\":\"%s\",\"pauseId\":\"%s\",\"unpauseId\":\"%s\"," +
                    "\"pause\":%s,\"data\":\"%s\"}",
            UUID, PROVIDE_ID, PAUSE_ID, UNPAUSE_ID, PAUSE_TIMESTAMP, DATA_WRITTEN
    );

    @SuppressWarnings("rawtypes") private TypeHandler<FunctionalProvider> handler;

    @BeforeEach
    public void setUp() {
        hydrateMockHandler(mockDataHandler, pairOf(mockData, DATA_WRITTEN));

        handler = new FunctionalProviderHandler(mockDataHandler, mockFactory);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new FunctionalProviderHandler(null, mockFactory));
        assertThrows(IllegalArgumentException.class,
                () -> new FunctionalProviderHandler(mockDataHandler, null));
    }

    @Test
    public void testWrite() {
        when(mockProvider.uuid()).thenReturn(UUID);
        //noinspection unchecked
        when(mockProvider.representation()).thenReturn(
                new FunctionalProvider.Representation(PROVIDE_ID, PAUSE_ID, UNPAUSE_ID,
                        PAUSE_TIMESTAMP, mockData));

        var output = handler.write(mockProvider);

        assertEquals(WRITTEN_VALUE, output);
        verify(mockProvider, once()).uuid();
        verify(mockProvider, once()).representation();
        verify(mockDataHandler, once()).write(mockData);
    }

    @Test
    public void testWriteWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> handler.write(null));
    }

    @Test
    public void testRead() {
        //noinspection rawtypes,unchecked
        when(mockFactory.make(any(), anyString(), anyString(), anyString(), anyLong(), anyMap()))
                .thenReturn((FunctionalProvider) mockProvider);

        var output = handler.read(WRITTEN_VALUE);

        assertNotNull(output);
        assertSame(mockProvider, output);
        verify(mockDataHandler, once()).read(DATA_WRITTEN);
        //noinspection unchecked
        verify(mockFactory, once()).make(
                eq(UUID),
                eq(PROVIDE_ID),
                eq(PAUSE_ID),
                eq(UNPAUSE_ID),
                eq(PAUSE_TIMESTAMP),
                same(mockData)
        );
    }

    @Test
    public void testReadWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> handler.read(null));
        assertThrows(IllegalArgumentException.class, () -> handler.read(""));
    }
}
