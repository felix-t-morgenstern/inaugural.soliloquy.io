package inaugural.soliloquy.io.test.unit.persistence.graphics.renderables.providers;

import inaugural.soliloquy.io.persistence.graphics.renderables.providers.LoopingLinearMovingColorProviderHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.io.graphics.renderables.providers.LoopingLinearMovingColorProvider;
import soliloquy.specs.io.graphics.renderables.providers.factories.LoopingLinearMovingColorProviderFactory;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

@ExtendWith(MockitoExtension.class)
public class LoopingLinearMovingColorProviderHandlerTests {
    private final UUID UUID = java.util.UUID.randomUUID();
    private final int TIMESTAMP_1 = randomInt();
    private final int TIMESTAMP_2 = randomIntWithInclusiveFloor(TIMESTAMP_1 + 1);
    private final int TIMESTAMP_3 = randomIntWithInclusiveFloor(TIMESTAMP_2 + 1);
    private final Color VALUE_1 = randomColor();
    private final Color VALUE_2 = randomColor();
    private final Color VALUE_3 = randomColor();
    private final Map<Integer, Color> VALUES_WITHIN_PERIOD = mapOf(
            pairOf(TIMESTAMP_1, VALUE_1),
            pairOf(TIMESTAMP_2, VALUE_2),
            pairOf(TIMESTAMP_3, VALUE_3)
    );
    private final List<Boolean> HUE_MOVEMENT_IS_CLOCKWISE = listOf(
            randomBoolean(),
            randomBoolean(),
            randomBoolean()
    );
    private final int PERIOD_DURATION = randomInt();
    private final int PERIOD_MODULO_OFFSET = randomInt();
    private final Long PAUSED_TIMESTAMP = randomLong();

    @Mock private LoopingLinearMovingColorProvider mockProvider;
    @Mock private LoopingLinearMovingColorProviderFactory mockFactory;

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
        handler = new LoopingLinearMovingColorProviderHandler(mockFactory);
    }

    @Test
    public void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new LoopingLinearMovingColorProviderHandler(null));
    }

    @Test
    public void testWrite() {
        when(mockProvider.uuid())
                .thenReturn(UUID);
        when(mockProvider.valuesWithinPeriod())
                .thenReturn(VALUES_WITHIN_PERIOD);
        when(mockProvider.periodDuration())
                .thenReturn(PERIOD_DURATION);
        when(mockProvider.periodModuloOffset())
                .thenReturn(PERIOD_MODULO_OFFSET);
        when(mockProvider.pausedTimestamp())
                .thenReturn(PAUSED_TIMESTAMP);
        when(mockProvider.hueMovementIsClockwise())
                .thenReturn(HUE_MOVEMENT_IS_CLOCKWISE);

        var output = handler.write(mockProvider);

        assertEquals(WRITTEN_DATA, output);
    }

    @Test
    public void testWriteWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> handler.write(null));
    }

    @Test
    public void testRead() {
        when(mockFactory.make(any(), anyMap(), anyList(), anyInt(), anyInt(), anyLong()))
                .thenReturn(mockProvider);

        var output = handler.read(WRITTEN_DATA);

        assertSame(mockProvider, output);
        verify(mockFactory)
                .make(UUID, VALUES_WITHIN_PERIOD, HUE_MOVEMENT_IS_CLOCKWISE, PERIOD_DURATION,
                        PERIOD_MODULO_OFFSET, PAUSED_TIMESTAMP);
    }

    @Test
    public void testReadWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                handler.read(null));
        assertThrows(IllegalArgumentException.class, () ->
                handler.read(""));
    }
}
