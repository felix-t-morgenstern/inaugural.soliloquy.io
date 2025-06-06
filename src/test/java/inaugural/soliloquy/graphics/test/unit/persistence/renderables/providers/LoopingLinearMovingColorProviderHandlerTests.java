package inaugural.soliloquy.graphics.test.unit.persistence.renderables.providers;

import inaugural.soliloquy.graphics.persistence.renderables.providers.LoopingLinearMovingColorProviderHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.graphics.renderables.providers.LoopingLinearMovingColorProvider;
import soliloquy.specs.graphics.renderables.providers.factories.LoopingLinearMovingColorProviderFactory;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

@ExtendWith(MockitoExtension.class)
public class LoopingLinearMovingColorProviderHandlerTests {
    private final Map<Integer, Color> VALUES_WITHIN_PERIOD = mapOf(
            pairOf(111, Color.RED),
            pairOf(222, Color.GREEN),
            pairOf(333, Color.BLUE)
    );
    private final int PERIOD_DURATION = 444;
    private final int PERIOD_MODULO_OFFSET = 555;
    private final Long PAUSED_TIMESTAMP = 666L;
    private final long MOST_RECENT_TIMESTAMP = 777L;
    private final List<Boolean> HUE_MOVEMENT_IS_CLOCKWISE = listOf(true, false, false);

    private static final UUID UUID = java.util.UUID.randomUUID();

    @Mock private LoopingLinearMovingColorProvider mockLoopingLinearMovingColorProvider;
    @Mock private LoopingLinearMovingColorProviderFactory
            mockLoopingLinearMovingColorProviderFactory;

    private TypeHandler<LoopingLinearMovingColorProvider> handler;

    private final String WRITTEN_DATA = String.format(
            "{\"uuid\":\"%s\",\"periodTimestamps\":[333,222,111]," +
                    "\"periodValues\":[{\"r\":0,\"g\":0,\"b\":255,\"a\":255},{\"r\":0,\"g\":255," +
                    "\"b\":0,\"a\":255},{\"r\":255,\"g\":0,\"b\":0,\"a\":255}]," +
                    "\"hueMovementIsClockwise\":[true,false,false],\"periodDuration\":444," +
                    "\"periodModuloOffset\":555,\"pausedTimestamp\":666," +
                    "\"mostRecentTimestamp\":777}",
            UUID);

    @BeforeEach
    void setUp() {
        handler = new LoopingLinearMovingColorProviderHandler(
                mockLoopingLinearMovingColorProviderFactory);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new LoopingLinearMovingColorProviderHandler(null));
    }

    @Test
    void testWrite() {
        when(mockLoopingLinearMovingColorProvider.uuid())
                .thenReturn(UUID);
        when(mockLoopingLinearMovingColorProvider.valuesWithinPeriod())
                .thenReturn(VALUES_WITHIN_PERIOD);
        when(mockLoopingLinearMovingColorProvider.periodDuration())
                .thenReturn(PERIOD_DURATION);
        when(mockLoopingLinearMovingColorProvider.periodModuloOffset())
                .thenReturn(PERIOD_MODULO_OFFSET);
        when(mockLoopingLinearMovingColorProvider.pausedTimestamp())
                .thenReturn(PAUSED_TIMESTAMP);
        when(mockLoopingLinearMovingColorProvider.mostRecentTimestamp())
                .thenReturn(MOST_RECENT_TIMESTAMP);
        when(mockLoopingLinearMovingColorProvider.hueMovementIsClockwise())
                .thenReturn(HUE_MOVEMENT_IS_CLOCKWISE);

        var output = handler.write(mockLoopingLinearMovingColorProvider);

        assertEquals(WRITTEN_DATA, output);
    }

    @Test
    void testWriteWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> handler.write(null));
    }

    @Test
    void testRead() {
        when(mockLoopingLinearMovingColorProviderFactory
                .make(any(), anyMap(), anyList(), anyInt(), anyInt(), anyLong(), anyLong()))
                .thenReturn(mockLoopingLinearMovingColorProvider);

        var output = handler.read(WRITTEN_DATA);

        assertSame(mockLoopingLinearMovingColorProvider, output);
        verify(mockLoopingLinearMovingColorProviderFactory)
                .make(UUID, VALUES_WITHIN_PERIOD, HUE_MOVEMENT_IS_CLOCKWISE, PERIOD_DURATION,
                        PERIOD_MODULO_OFFSET, PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP);
    }

    @Test
    void testReadWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                handler.read(null));
        assertThrows(IllegalArgumentException.class, () ->
                handler.read(""));
    }
}
