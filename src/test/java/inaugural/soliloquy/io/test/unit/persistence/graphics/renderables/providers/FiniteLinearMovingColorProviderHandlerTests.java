package inaugural.soliloquy.io.test.unit.persistence.graphics.renderables.providers;

import inaugural.soliloquy.io.persistence.graphics.renderables.providers.FiniteLinearMovingColorProviderHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.io.graphics.renderables.providers.FiniteLinearMovingColorProvider;
import soliloquy.specs.io.graphics.renderables.providers.factories.FiniteLinearMovingColorProviderFactory;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

public class FiniteLinearMovingColorProviderHandlerTests {
    private static final Long TIMESTAMP_1 = 123L;
    private static final Long TIMESTAMP_2 = 456L;
    private static final Long TIMESTAMP_3 = 789L;
    private static final Color VALUE_1 = Color.MAGENTA;
    private static final Color VALUE_2 = Color.RED;
    private static final Color VALUE_3 = Color.BLACK;
    private static final Map<Long, Color> COLORS_AT_TIMESTAMPS = mapOf(
        pairOf(TIMESTAMP_1, VALUE_1),
        pairOf(TIMESTAMP_2, VALUE_2),
        pairOf(TIMESTAMP_3, VALUE_3)
    );
    private static final List<Boolean> HUE_MOVEMENT_IS_CLOCKWISE = listOf(
        true,
        false,
        false
    );
    private static final Long PAUSED_TIMESTAMP = 123L;
    private static final Long MOST_RECENT_TIMESTAMP = 456L;

    private static final String UUID_WRITE_OUTPUT = "uuidWriteOutput";

    @Mock private FiniteLinearMovingColorProvider mockFiniteLinearMovingColorProvider;
    @Mock private FiniteLinearMovingColorProviderFactory
            mockFiniteLinearMovingColorProviderFactory;
    @Mock private FiniteLinearMovingColorProvider mockFiniteLinearMovingColorProviderFactoryOutput;
    @Mock private static UUID finiteLinearMovingColorProviderInputUuid;
    @Mock private static UUID uuidReadOutput;
    @Mock private TypeHandler<UUID> uuidHandler;

    private TypeHandler<FiniteLinearMovingColorProvider> finiteLinearMovingColorProviderHandler;

    private static final String WRITTEN_VALUE =
            "{\"uuid\":\"uuidWriteOutput\",\"colors\":[{\"timestamp\":123,\"r\":255,\"g\":0," +
                    "\"b\":255,\"a\":255},{\"timestamp\":456,\"r\":255,\"g\":0,\"b\":0," +
                    "\"a\":255},{\"timestamp\":789,\"r\":0,\"g\":0,\"b\":0,\"a\":255}]," +
                    "\"movementIsClockwise\":[true,false,false],\"pausedTimestamp\":123," +
                    "\"mostRecentTimestamp\":456}";

    @BeforeEach
    public void setUp() {
        mockFiniteLinearMovingColorProvider = mock(FiniteLinearMovingColorProvider.class);
        when(mockFiniteLinearMovingColorProvider.uuid())
                .thenReturn(finiteLinearMovingColorProviderInputUuid);
        when(mockFiniteLinearMovingColorProvider.valuesAtTimestampsRepresentation())
                .thenReturn(COLORS_AT_TIMESTAMPS);
        when(mockFiniteLinearMovingColorProvider.hueMovementIsClockwise())
                .thenReturn(HUE_MOVEMENT_IS_CLOCKWISE);
        when(mockFiniteLinearMovingColorProvider.pausedTimestamp()).thenReturn(PAUSED_TIMESTAMP);
        when(mockFiniteLinearMovingColorProvider.mostRecentTimestamp())
                .thenReturn(MOST_RECENT_TIMESTAMP);

        //noinspection unchecked
        uuidHandler = Mockito.mock(TypeHandler.class);
        when(uuidHandler.read(Mockito.anyString())).thenReturn(uuidReadOutput);
        when(uuidHandler.write(Mockito.any())).thenReturn(UUID_WRITE_OUTPUT);

        mockFiniteLinearMovingColorProviderFactoryOutput =
                mock(FiniteLinearMovingColorProvider.class);

        mockFiniteLinearMovingColorProviderFactory =
                mock(FiniteLinearMovingColorProviderFactory.class);
        when(mockFiniteLinearMovingColorProviderFactory
                .make(any(), anyMap(), anyList(), anyLong(), anyLong()))
                .thenReturn(mockFiniteLinearMovingColorProviderFactoryOutput);

        finiteLinearMovingColorProviderHandler = new FiniteLinearMovingColorProviderHandler(
                uuidHandler, mockFiniteLinearMovingColorProviderFactory);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingColorProviderHandler(null,
                        mockFiniteLinearMovingColorProviderFactory));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingColorProviderHandler(uuidHandler,
                        null));
    }

    @Test
    public void testWrite() {
        assertEquals(WRITTEN_VALUE, finiteLinearMovingColorProviderHandler
                .write(mockFiniteLinearMovingColorProvider));
    }

    @Test
    public void testWriteWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingColorProviderHandler.write(null));
    }

    @Test
    public void testRead() {
        FiniteLinearMovingColorProvider readValue =
                finiteLinearMovingColorProviderHandler.read(WRITTEN_VALUE);

        assertSame(mockFiniteLinearMovingColorProviderFactoryOutput, readValue);
        verify(uuidHandler, once()).read(UUID_WRITE_OUTPUT);
        verify(mockFiniteLinearMovingColorProviderFactory, once())
                .make(uuidReadOutput, COLORS_AT_TIMESTAMPS, HUE_MOVEMENT_IS_CLOCKWISE,
                        PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP);
    }

    @Test
    public void testReadWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingColorProviderHandler.read(null));
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingColorProviderHandler.read(""));
    }
}
