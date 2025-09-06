package inaugural.soliloquy.io.test.unit.persistence.graphics.renderables.providers;

import inaugural.soliloquy.io.persistence.graphics.renderables.providers.FiniteSinusoidMovingProviderHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.persistence.PersistenceHandler;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.io.graphics.renderables.providers.FiniteSinusoidMovingProvider;
import soliloquy.specs.io.graphics.renderables.providers.factories.FiniteSinusoidMovingProviderFactory;

import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.arrayFloats;
import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static inaugural.soliloquy.tools.testing.Mock.generateMockMap;
import static inaugural.soliloquy.tools.testing.Mock.generateSimpleMockTypeHandler;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

@ExtendWith(MockitoExtension.class)
public class FiniteSinusoidMovingProviderHandlerTests {
    private final Long TIMESTAMP_1 = randomLong();
    private final Long TIMESTAMP_2 = randomLong();
    private final Long TIMESTAMP_3 = randomLong();
    private final float SHARPNESS = randomFloat();
    private final Float VALUE_1 = randomFloat();
    private final Float VALUE_2 = randomFloat();
    private final Float VALUE_3 = randomFloat();
    private final Long PAUSED_TIMESTAMP = randomLong();

    private final UUID UUID = java.util.UUID.randomUUID();

    private final String FLOAT_WRITE_OUTPUT_1 = randomString();
    private final String FLOAT_WRITE_OUTPUT_2 = randomString();
    private final String FLOAT_WRITE_OUTPUT_3 = randomString();
    private final TypeHandler<Float> MOCK_FLOAT_HANDLER = generateSimpleMockTypeHandler(
            pairOf(FLOAT_WRITE_OUTPUT_1, VALUE_1),
            pairOf(FLOAT_WRITE_OUTPUT_2, VALUE_2),
            pairOf(FLOAT_WRITE_OUTPUT_3, VALUE_3)
    );

    private final String WRITTEN_VALUE = String.format(
            "{\"uuid\":\"%s\",\"type\":\"java.lang.Float\",\"vals\":[{\"time\":%d," +
                    "\"val\":\"%s\"},{\"time\":%d,\"val\":\"%s\"},{\"time\":%d,\"val\":\"%s\"}]," +
                    "\"sharpnesses\":[%s],\"pausedTimestamp\":%d}",
            UUID, TIMESTAMP_1, FLOAT_WRITE_OUTPUT_1, TIMESTAMP_2, FLOAT_WRITE_OUTPUT_2, TIMESTAMP_3,
            FLOAT_WRITE_OUTPUT_3, SHARPNESS, PAUSED_TIMESTAMP);

    private Map<Long, Float> mockValuesAtTimestamps;

    @Mock private FiniteSinusoidMovingProviderFactory mockFactory;
    @Mock private FiniteSinusoidMovingProvider<Float> mockProvider;
    @Mock private PersistenceHandler mockPersistenceHandler;

    @SuppressWarnings("rawtypes")
    private TypeHandler<FiniteSinusoidMovingProvider> handler;

    @BeforeEach
    public void setUp() {
        //noinspection unchecked,rawtypes
        lenient().when(mockPersistenceHandler.getTypeHandler(anyString()))
                .thenReturn((TypeHandler) MOCK_FLOAT_HANDLER);

        mockValuesAtTimestamps = generateMockMap(
                pairOf(TIMESTAMP_1, VALUE_1),
                pairOf(TIMESTAMP_2, VALUE_2),
                pairOf(TIMESTAMP_3, VALUE_3)
        );

        handler = new FiniteSinusoidMovingProviderHandler(mockPersistenceHandler, mockFactory);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingProviderHandler(null, mockFactory));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingProviderHandler(mockPersistenceHandler, null));
    }

    @Test
    public void testWrite() {
        when(mockProvider.uuid()).thenReturn(UUID);
        when(mockProvider.valuesAtTimestampsRepresentation()).thenReturn(mockValuesAtTimestamps);
        when(mockProvider.transitionSharpnesses()).thenReturn(arrayFloats(SHARPNESS));
        when(mockProvider.pausedTimestamp()).thenReturn(PAUSED_TIMESTAMP);

        var writtenValue = handler.write(mockProvider);

        assertEquals(WRITTEN_VALUE, writtenValue);

        var inOrder = inOrder(mockProvider, mockPersistenceHandler,
                mockValuesAtTimestamps, MOCK_FLOAT_HANDLER);
        inOrder.verify(mockProvider).uuid();
        inOrder.verify(mockProvider).valuesAtTimestampsRepresentation();
        inOrder.verify(mockPersistenceHandler).getTypeHandler(Float.class.getCanonicalName());
        //noinspection ResultOfMethodCallIgnored
        inOrder.verify(mockValuesAtTimestamps).size();
        inOrder.verify(MOCK_FLOAT_HANDLER).write(VALUE_1);
        inOrder.verify(MOCK_FLOAT_HANDLER).write(VALUE_2);
        inOrder.verify(MOCK_FLOAT_HANDLER).write(VALUE_3);
        inOrder.verify(mockProvider).pausedTimestamp();
    }

    @Test
    public void testWriteWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> handler.write(null));
    }

    @Test
    public void testRead() {
        //noinspection unchecked,rawtypes
        when(mockFactory.make(any(), anyMap(), any(), anyLong()))
                .thenReturn((FiniteSinusoidMovingProvider) mockProvider);

        var output = handler.read(WRITTEN_VALUE);

        assertSame(mockProvider, output);
        var inOrder = inOrder(mockPersistenceHandler, MOCK_FLOAT_HANDLER, mockFactory);
        inOrder.verify(mockPersistenceHandler, once())
                .getTypeHandler(Float.class.getCanonicalName());
        inOrder.verify(MOCK_FLOAT_HANDLER, once()).read(FLOAT_WRITE_OUTPUT_1);
        inOrder.verify(MOCK_FLOAT_HANDLER, once()).read(FLOAT_WRITE_OUTPUT_2);
        inOrder.verify(MOCK_FLOAT_HANDLER, once()).read(FLOAT_WRITE_OUTPUT_3);
        var factoryCaptor = ArgumentCaptor.forClass(Map.class);
        //noinspection unchecked
        inOrder.verify(mockFactory, once()).make(
                eq(UUID),
                (Map<Long, Float>) factoryCaptor.capture(),
                eq(arrayFloats(SHARPNESS)),
                eq(PAUSED_TIMESTAMP)
        );
        //noinspection unchecked
        var factoryValuesAtTimestamps = (Map<Long, Float>) factoryCaptor.getValue();
        assertEquals(3, factoryValuesAtTimestamps.size());
        assertEquals(VALUE_1, factoryValuesAtTimestamps.get(TIMESTAMP_1));
        assertEquals(VALUE_2, factoryValuesAtTimestamps.get(TIMESTAMP_2));
        assertEquals(VALUE_3, factoryValuesAtTimestamps.get(TIMESTAMP_3));
    }

    @Test
    public void testReadWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                handler.read(null));
        assertThrows(IllegalArgumentException.class, () ->
                handler.read(""));
    }
}
