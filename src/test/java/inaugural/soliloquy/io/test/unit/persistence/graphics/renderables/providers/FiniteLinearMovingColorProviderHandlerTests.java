package inaugural.soliloquy.io.test.unit.persistence.graphics.renderables.providers;

import inaugural.soliloquy.io.persistence.graphics.renderables.providers.FiniteLinearMovingColorProviderHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.io.graphics.renderables.providers.FiniteLinearMovingColorProvider;
import soliloquy.specs.io.graphics.renderables.providers.factories.FiniteLinearMovingColorProviderFactory;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

@ExtendWith(MockitoExtension.class)
public class FiniteLinearMovingColorProviderHandlerTests {
    private final UUID UUID = java.util.UUID.randomUUID();
    private final Long TIMESTAMP_1 = randomLong();
    private final Long TIMESTAMP_2 = randomLongWithInclusiveFloor(TIMESTAMP_1 + 1);
    private final Long TIMESTAMP_3 = randomLongWithInclusiveFloor(TIMESTAMP_2 + 1);
    private final Color VALUE_1 = randomColor();
    private final Color VALUE_2 = randomColor();
    private final Color VALUE_3 = randomColor();
    private final Map<Long, Color> VALUES_AT_TIMESTAMPS = mapOf(
        pairOf(TIMESTAMP_1, VALUE_1),
        pairOf(TIMESTAMP_2, VALUE_2),
        pairOf(TIMESTAMP_3, VALUE_3)
    );
    private final List<Boolean> HUE_MOVEMENT_IS_CLOCKWISE = listOf(
        randomBoolean(),
        randomBoolean(),
        randomBoolean()
    );
    private final Long PAUSED_TIMESTAMP = randomLong();

    @Mock private FiniteLinearMovingColorProvider mockProvider;
    @Mock private FiniteLinearMovingColorProviderFactory mockFactory;

    private TypeHandler<FiniteLinearMovingColorProvider> handler;

    private final String WRITTEN_VALUE =
            "{\"uuid\":\"uuidWriteOutput\",\"colors\":[{\"timestamp\":123,\"r\":255,\"g\":0," +
                    "\"b\":255,\"a\":255},{\"timestamp\":456,\"r\":255,\"g\":0,\"b\":0," +
                    "\"a\":255},{\"timestamp\":789,\"r\":0,\"g\":0,\"b\":0,\"a\":255}]," +
                    "\"movementIsClockwise\":[true,false,false],\"pausedTimestamp\":123," +
                    "\"mostRecentTimestamp\":456}";

    @BeforeEach
    public void setUp() {
        mockFactory =
                mock(FiniteLinearMovingColorProviderFactory.class);

        handler = new FiniteLinearMovingColorProviderHandler(mockFactory);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingColorProviderHandler(null));
    }

    @Test
    public void testWrite() {
        when(mockProvider.uuid()).thenReturn(UUID);
        when(mockProvider.valuesAtTimestampsRepresentation()).thenReturn(VALUES_AT_TIMESTAMPS);
        when(mockProvider.hueMovementIsClockwise()).thenReturn(HUE_MOVEMENT_IS_CLOCKWISE);
        when(mockProvider.pausedTimestamp()).thenReturn(PAUSED_TIMESTAMP);

        assertEquals(WRITTEN_VALUE, handler.write(mockProvider));

        verify(mockProvider, once()).uuid();
        verify(mockProvider, once()).valuesAtTimestampsRepresentation();
        verify(mockProvider, once()).hueMovementIsClockwise();
        verify(mockProvider, once()).pausedTimestamp();
    }

    @Test
    public void testWriteWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> handler.write(null));
    }

    @Test
    public void testRead() {
        when(mockFactory.make(any(), anyMap(), anyList(), anyLong())).thenReturn(mockProvider);

        var provider = handler.read(WRITTEN_VALUE);

        assertSame(mockProvider, provider);
        verify(mockFactory, once()).make(any(), anyMap(), anyList(), any());
        verify(mockFactory, once()).make(
                eq(UUID),
                eq(VALUES_AT_TIMESTAMPS),
                eq(HUE_MOVEMENT_IS_CLOCKWISE),
                eq(PAUSED_TIMESTAMP)
        );
    }

    @Test
    public void testReadWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> handler.read(null));
        assertThrows(IllegalArgumentException.class, () -> handler.read(""));
    }
}
