package inaugural.soliloquy.io.test.unit.graphics.renderables.providers;

import inaugural.soliloquy.io.graphics.renderables.providers.RectangleAnimatedBackgroundTextureIdProvider;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.io.graphics.renderables.providers.LoopingLinearMovingProvider;

import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
/** @noinspection FieldCanBeLocal */
public class RectangleAnimatedBackgroundTextureIdProviderTests {
    private final int PERIOD_DURATION = 4000;
    private final int PERIOD_MODULO_OFFSET = 123;
    private final long PAUSED_TIMESTAMP = 45L;

    private final int MS_1 = 0;
    private final int MS_1_VALUE = 111;
    private final int MS_2 = 100;
    private final int MS_2_VALUE = 222;
    private final int MS_3 = 300;
    private final int MS_3_VALUE = 333;
    private final int MS_4 = 600;
    private final int MS_4_VALUE = 444;
    private final int MS_5 = 1000;
    private final int MS_5_VALUE = 555;
    private final int MS_6 = 1500;
    private final int MS_6_VALUE = 666;
    private final int MS_7 = 2100;
    private final int MS_7_VALUE = 777;
    private final int MS_8 = 2800;
    private final int MS_8_VALUE = 888;
    private final int MS_9 = 3600;
    private final int MS_9_VALUE = 999;
    private final Map<Integer, Integer> VALUES_WITHIN_PERIOD = mapOf();

    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private TimestampValidator mockTimestampValidator;

    private LoopingLinearMovingProvider<Integer> provider;

    @BeforeEach
    public void setUp() {
        VALUES_WITHIN_PERIOD.put(MS_1, MS_1_VALUE);
        VALUES_WITHIN_PERIOD.put(MS_2, MS_2_VALUE);
        VALUES_WITHIN_PERIOD.put(MS_3, MS_3_VALUE);
        VALUES_WITHIN_PERIOD.put(MS_4, MS_4_VALUE);
        VALUES_WITHIN_PERIOD.put(MS_5, MS_5_VALUE);
        VALUES_WITHIN_PERIOD.put(MS_6, MS_6_VALUE);
        VALUES_WITHIN_PERIOD.put(MS_7, MS_7_VALUE);
        VALUES_WITHIN_PERIOD.put(MS_8, MS_8_VALUE);
        VALUES_WITHIN_PERIOD.put(MS_9, MS_9_VALUE);

        provider =
                new RectangleAnimatedBackgroundTextureIdProvider(UUID, PERIOD_DURATION,
                        PERIOD_MODULO_OFFSET, VALUES_WITHIN_PERIOD, null,
                        mockTimestampValidator);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleAnimatedBackgroundTextureIdProvider(null, PERIOD_DURATION,
                        PERIOD_MODULO_OFFSET, VALUES_WITHIN_PERIOD, PAUSED_TIMESTAMP,
                        mockTimestampValidator));

        assertThrows(IllegalArgumentException.class, () ->
                new RectangleAnimatedBackgroundTextureIdProvider(UUID, 0,
                        0, VALUES_WITHIN_PERIOD, PAUSED_TIMESTAMP,
                        mockTimestampValidator));

        assertThrows(IllegalArgumentException.class, () ->
                new RectangleAnimatedBackgroundTextureIdProvider(UUID, PERIOD_DURATION,
                        PERIOD_DURATION, VALUES_WITHIN_PERIOD, PAUSED_TIMESTAMP,
                        mockTimestampValidator));

        assertThrows(IllegalArgumentException.class, () ->
                new RectangleAnimatedBackgroundTextureIdProvider(UUID, PERIOD_DURATION,
                        -1, VALUES_WITHIN_PERIOD, PAUSED_TIMESTAMP,
                        mockTimestampValidator));

        assertThrows(IllegalArgumentException.class, () ->
                new RectangleAnimatedBackgroundTextureIdProvider(UUID, PERIOD_DURATION,
                        PERIOD_MODULO_OFFSET, null, PAUSED_TIMESTAMP,
                        mockTimestampValidator));

        assertThrows(IllegalArgumentException.class, () ->
                new RectangleAnimatedBackgroundTextureIdProvider(UUID, PERIOD_DURATION,
                        PERIOD_MODULO_OFFSET, VALUES_WITHIN_PERIOD, PAUSED_TIMESTAMP,
                        mockTimestampValidator));
        // NB: Constructors being invoked here are simply to test whether no exception is thrown
        //     when no exception is expected
        when(mockTimestampValidator.mostRecentTimestamp()).thenReturn(PAUSED_TIMESTAMP);
        new RectangleAnimatedBackgroundTextureIdProvider(UUID, PERIOD_DURATION,
                PERIOD_MODULO_OFFSET,
                VALUES_WITHIN_PERIOD, PAUSED_TIMESTAMP,
                mockTimestampValidator);

        VALUES_WITHIN_PERIOD.remove(MS_1);
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleAnimatedBackgroundTextureIdProvider(UUID, PERIOD_DURATION,
                        PERIOD_MODULO_OFFSET, VALUES_WITHIN_PERIOD, PAUSED_TIMESTAMP,
                        mockTimestampValidator));
        VALUES_WITHIN_PERIOD.put(MS_1, MS_1_VALUE);

        new RectangleAnimatedBackgroundTextureIdProvider(UUID, PERIOD_DURATION,
                PERIOD_MODULO_OFFSET,
                VALUES_WITHIN_PERIOD, PAUSED_TIMESTAMP, mockTimestampValidator);

        VALUES_WITHIN_PERIOD.put(PERIOD_DURATION, 123);
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleAnimatedBackgroundTextureIdProvider(UUID, PERIOD_DURATION,
                        PERIOD_MODULO_OFFSET, VALUES_WITHIN_PERIOD, PAUSED_TIMESTAMP,
                        mockTimestampValidator));
        VALUES_WITHIN_PERIOD.remove(PERIOD_DURATION);

        new RectangleAnimatedBackgroundTextureIdProvider(UUID, PERIOD_DURATION,
                PERIOD_MODULO_OFFSET,
                VALUES_WITHIN_PERIOD, PAUSED_TIMESTAMP, mockTimestampValidator);

        VALUES_WITHIN_PERIOD.put(-1, 123);
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleAnimatedBackgroundTextureIdProvider(UUID, PERIOD_DURATION,
                        PERIOD_MODULO_OFFSET, VALUES_WITHIN_PERIOD, PAUSED_TIMESTAMP,
                        mockTimestampValidator));
        VALUES_WITHIN_PERIOD.remove(-1);

        new RectangleAnimatedBackgroundTextureIdProvider(UUID, PERIOD_DURATION,
                PERIOD_MODULO_OFFSET,
                VALUES_WITHIN_PERIOD, PAUSED_TIMESTAMP, mockTimestampValidator);

        assertThrows(IllegalArgumentException.class, () ->
                new RectangleAnimatedBackgroundTextureIdProvider(UUID, PERIOD_DURATION,
                        PERIOD_MODULO_OFFSET, VALUES_WITHIN_PERIOD, PAUSED_TIMESTAMP,
                        null));
    }

    @Test
    public void testUuid() {
        assertSame(UUID, provider.uuid());
    }

    @Test
    public void testValuesWithinPeriod() {
        Map<Integer, Integer> valuesWithinPeriod =
                provider.valuesWithinPeriod();

        assertNotNull(valuesWithinPeriod);
        assertNotSame(VALUES_WITHIN_PERIOD, valuesWithinPeriod);
        assertEquals(VALUES_WITHIN_PERIOD.size(), valuesWithinPeriod.size());
        VALUES_WITHIN_PERIOD.forEach((k, v) -> assertEquals(v, valuesWithinPeriod.get(k)));
    }

    @Test
    public void testPeriodDuration() {
        assertEquals(PERIOD_DURATION,
                provider.periodDuration());
    }

    @Test
    public void testPeriodModuloOffset() {
        assertEquals(PERIOD_MODULO_OFFSET,
                provider.periodModuloOffset());
    }

    @Test
    public void testReset() {
        long resetTimestamp = 123123L;

        provider.reset(resetTimestamp);

        assertEquals(PERIOD_DURATION - (resetTimestamp % PERIOD_DURATION),
                provider.periodModuloOffset());
    }

    @Test
    public void testProvide() {
        long timestamp1 = MS_3 - PERIOD_MODULO_OFFSET;
        long timestamp2 = timestamp1 - 1;

        int providedValue2 = provider.provide(timestamp2);
        int providedValue1 = provider.provide(timestamp1);

        assertEquals(MS_3_VALUE, providedValue1);
        assertEquals(MS_2_VALUE, providedValue2);
    }

    @Test
    public void testProvideWhenPaused() {
        long pauseTimestamp = MS_3 - PERIOD_MODULO_OFFSET;
        long provideTimestamp = pauseTimestamp + 123456L;

        provider.reportPause(pauseTimestamp);

        int providedValue =
                provider.provide(provideTimestamp);

        assertEquals(MS_3_VALUE, providedValue);
    }

    @Test
    public void testProvideWhenResumed() {
        long pauseTimestamp = MS_3 - PERIOD_MODULO_OFFSET - 1;
        long unpauseTimestamp = pauseTimestamp + 123456L;

        provider.reportPause(pauseTimestamp);
        provider.reportUnpause(unpauseTimestamp);

        int providedValue1 =
                provider.provide(unpauseTimestamp);
        int providedValue2 =
                provider.provide(unpauseTimestamp + 1);

        assertEquals(MS_2_VALUE, providedValue1);
        assertEquals(MS_3_VALUE, providedValue2);
    }

    @Test
    public void testReportPauseWhilePausedOrViceVersa() {
        assertThrows(UnsupportedOperationException.class, () -> provider.reportUnpause(PAUSED_TIMESTAMP));

        provider.reportPause(PAUSED_TIMESTAMP);

        assertThrows(UnsupportedOperationException.class, () -> provider.reportPause(PAUSED_TIMESTAMP));
    }

    @Test
    public void testPauseTimestamp() {
        provider.reportPause(PAUSED_TIMESTAMP);

        assertEquals(PAUSED_TIMESTAMP, (long) provider.pausedTimestamp());
        verify(mockTimestampValidator, atLeastOnce()).validateTimestamp(PAUSED_TIMESTAMP);
    }

    @Test
    public void testRepresentation() {
        assertEquals(VALUES_WITHIN_PERIOD, provider.representation());
        assertNotSame(VALUES_WITHIN_PERIOD, provider.representation());
    }
}
