package inaugural.soliloquy.io.test.unit.graphics.renderables.providers;

import inaugural.soliloquy.io.graphics.renderables.providers.LoopingLinearMovingColorProviderImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.io.graphics.renderables.providers.LoopingLinearMovingColorProvider;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static org.junit.jupiter.api.Assertions.*;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

public class LoopingLinearMovingColorProviderImplTests {
    private final Map<Integer, Color> VALUES_AT_TIMES = mapOf();
    private final int TIME_1 = 0;
    private final Color VALUE_1 = new Color(188, 130, 217, 255);
    private final int TIME_2 = 100;
    private final Color VALUE_2 = new Color(8, 79, 35, 127);
    private final int TIME_3 = 300;
    private final Color VALUE_3 = new Color(0, 191, 255, 63);
    private final int TIME_4 = 600;
    private final Color VALUE_4 = new Color(199, 222, 140, 127);
    private final int TIME_5 = 1000;
    private final Color VALUE_5 = new Color(6, 36, 117, 255);

    private final int PERIOD_DURATION = 1500;
    private final int PERIOD_MODULO_OFFSET = 12;

    private final boolean TRANSITION_1_IS_CLOCKWISE = false;
    private final boolean TRANSITION_2_IS_CLOCKWISE = true;
    private final boolean TRANSITION_3_IS_CLOCKWISE = true;
    private final boolean TRANSITION_4_IS_CLOCKWISE = false;
    private final boolean TRANSITION_5_IS_CLOCKWISE = false;

    private final UUID UUID = java.util.UUID.randomUUID();

    private List<Boolean> hueMovementIsClockwise;

    private LoopingLinearMovingColorProvider loopingLinearMovingColorProvider;

    @BeforeEach
    public void setUp() {
        VALUES_AT_TIMES.put(TIME_1, VALUE_1);
        VALUES_AT_TIMES.put(TIME_2, VALUE_2);
        VALUES_AT_TIMES.put(TIME_3, VALUE_3);
        VALUES_AT_TIMES.put(TIME_4, VALUE_4);
        VALUES_AT_TIMES.put(TIME_5, VALUE_5);

        hueMovementIsClockwise = listOf(
            TRANSITION_1_IS_CLOCKWISE,
            TRANSITION_2_IS_CLOCKWISE,
            TRANSITION_3_IS_CLOCKWISE,
            TRANSITION_4_IS_CLOCKWISE,
            TRANSITION_5_IS_CLOCKWISE
        );

        loopingLinearMovingColorProvider = new LoopingLinearMovingColorProviderImpl(UUID,
                VALUES_AT_TIMES, hueMovementIsClockwise, PERIOD_DURATION, PERIOD_MODULO_OFFSET,
                null, null);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingColorProviderImpl(null, VALUES_AT_TIMES,
                        hueMovementIsClockwise, PERIOD_DURATION, PERIOD_MODULO_OFFSET, null,
                        null));

        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingColorProviderImpl(UUID, null,
                        hueMovementIsClockwise, PERIOD_DURATION, PERIOD_MODULO_OFFSET, null,
                        null));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingColorProviderImpl(UUID, mapOf(),
                        hueMovementIsClockwise, PERIOD_DURATION, PERIOD_MODULO_OFFSET, null,
                        null));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingColorProviderImpl(UUID, mapOf(pairOf(null, Color.RED)),
                        hueMovementIsClockwise, PERIOD_DURATION, PERIOD_MODULO_OFFSET, null,
                        null));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingColorProviderImpl(UUID, mapOf(pairOf(0, null)),
                        hueMovementIsClockwise, PERIOD_DURATION, PERIOD_MODULO_OFFSET, null,
                        null));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingColorProviderImpl(UUID, mapOf(pairOf(123, Color.RED)),
                        hueMovementIsClockwise, PERIOD_DURATION, PERIOD_MODULO_OFFSET, null,
                        null));

        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingColorProviderImpl(UUID, VALUES_AT_TIMES,
                        null, PERIOD_DURATION, PERIOD_MODULO_OFFSET, null, null));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingColorProviderImpl(UUID, VALUES_AT_TIMES,
                        listOf(
                            TRANSITION_1_IS_CLOCKWISE,
                            TRANSITION_1_IS_CLOCKWISE,
                            TRANSITION_1_IS_CLOCKWISE,
                            TRANSITION_1_IS_CLOCKWISE,
                            null
                        ), PERIOD_DURATION, PERIOD_MODULO_OFFSET, null, null));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingColorProviderImpl(UUID, VALUES_AT_TIMES,
                        listOf(
                            TRANSITION_1_IS_CLOCKWISE,
                            TRANSITION_1_IS_CLOCKWISE,
                            TRANSITION_1_IS_CLOCKWISE,
                            TRANSITION_1_IS_CLOCKWISE
                        ), PERIOD_DURATION, PERIOD_MODULO_OFFSET, null, null));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingColorProviderImpl(UUID, VALUES_AT_TIMES,
                        listOf(
                            TRANSITION_1_IS_CLOCKWISE,
                            TRANSITION_1_IS_CLOCKWISE,
                            TRANSITION_1_IS_CLOCKWISE,
                            TRANSITION_1_IS_CLOCKWISE,
                            TRANSITION_1_IS_CLOCKWISE,
                            TRANSITION_1_IS_CLOCKWISE
                        ), PERIOD_DURATION, PERIOD_MODULO_OFFSET, null, null));

        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingColorProviderImpl(UUID, VALUES_AT_TIMES,
                        hueMovementIsClockwise, PERIOD_DURATION, PERIOD_DURATION, null, null));

        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingColorProviderImpl(UUID, VALUES_AT_TIMES,
                        hueMovementIsClockwise, PERIOD_DURATION, PERIOD_MODULO_OFFSET, 12L,
                        null));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingColorProviderImpl(UUID, VALUES_AT_TIMES,
                        hueMovementIsClockwise, PERIOD_DURATION, PERIOD_MODULO_OFFSET, 1L, 0L));
    }

    @Test
    public void testUuid() {
        assertSame(UUID, loopingLinearMovingColorProvider.uuid());
    }

    @Test
    public void testMostRecentTimestampAndPausedTimestamp() {
        long pausedTimestamp = 123123L;
        long mostRecentTimestamp = 456456L;

        LoopingLinearMovingColorProvider loopingMovingColorProvider =
                new LoopingLinearMovingColorProviderImpl(UUID, VALUES_AT_TIMES,
                        hueMovementIsClockwise, PERIOD_DURATION, PERIOD_MODULO_OFFSET,
                        pausedTimestamp, mostRecentTimestamp);

        assertEquals(pausedTimestamp,
                (long) loopingMovingColorProvider.pausedTimestamp());
        assertEquals(mostRecentTimestamp,
                (long) loopingMovingColorProvider.mostRecentTimestamp());
    }

    @Test
    public void testValuesAtTimestampsRepresentation() {
        assertNotNull(loopingLinearMovingColorProvider.valuesWithinPeriod());
        assertEquals(VALUES_AT_TIMES,
                loopingLinearMovingColorProvider.valuesWithinPeriod());
        assertNotSame(VALUES_AT_TIMES,
                loopingLinearMovingColorProvider.valuesWithinPeriod());
        assertNotSame(loopingLinearMovingColorProvider.valuesWithinPeriod(),
                loopingLinearMovingColorProvider.valuesWithinPeriod());
    }

    @Test
    public void testHueMovementIsClockwise() {
        assertNotNull(loopingLinearMovingColorProvider.hueMovementIsClockwise());
        assertEquals(hueMovementIsClockwise,
                loopingLinearMovingColorProvider.hueMovementIsClockwise());
        assertNotSame(hueMovementIsClockwise,
                loopingLinearMovingColorProvider.hueMovementIsClockwise());
        assertNotSame(loopingLinearMovingColorProvider.hueMovementIsClockwise(),
                loopingLinearMovingColorProvider.hueMovementIsClockwise());
    }

    @Test
    public void testPeriodDuration() {
        assertEquals(PERIOD_DURATION, loopingLinearMovingColorProvider.periodDuration());
    }

    @Test
    public void testProvideAtKey() {
        assertEquals(VALUE_1,
                loopingLinearMovingColorProvider.provide(TIME_1 - PERIOD_MODULO_OFFSET));
        assertEquals(VALUE_2,
                loopingLinearMovingColorProvider.provide(TIME_2 - PERIOD_MODULO_OFFSET));
        assertEquals(VALUE_3,
                loopingLinearMovingColorProvider.provide(TIME_3 - PERIOD_MODULO_OFFSET));
        assertEquals(VALUE_4,
                loopingLinearMovingColorProvider.provide(TIME_4 - PERIOD_MODULO_OFFSET));
        assertEquals(VALUE_5,
                loopingLinearMovingColorProvider.provide(TIME_5 - PERIOD_MODULO_OFFSET));
    }

    @Test
    public void testProvideValueCounterclockwiseWithoutOverlappingZeroDegrees() {
        long timeAfterTime1 = 50;
        long timestamp = TIME_1 + timeAfterTime1 - PERIOD_MODULO_OFFSET;
        long distanceBetweenTimes = TIME_2 - TIME_1;
        float time2Weight = timeAfterTime1 / (float) distanceBetweenTimes;
        float time1Weight = 1f - time2Weight;

        float[] value1Hsb =
                Color.RGBtoHSB(VALUE_1.getRed(), VALUE_1.getGreen(), VALUE_1.getBlue(), null);
        float[] value2Hsb =
                Color.RGBtoHSB(VALUE_2.getRed(), VALUE_2.getGreen(), VALUE_2.getBlue(), null);

        float hue = (time1Weight * value1Hsb[0]) + (time2Weight * value2Hsb[0]);
        float saturation = (time1Weight * value1Hsb[1]) + (time2Weight * value2Hsb[1]);
        float brightness = (time1Weight * value1Hsb[2]) + (time2Weight * value2Hsb[2]);

        Color rgb = Color.getHSBColor(hue, saturation, brightness);

        int alpha = (int) ((time1Weight * VALUE_1.getAlpha()) + (time2Weight * VALUE_2.getAlpha()));

        Color expected = new Color(rgb.getRed(), rgb.getGreen(), rgb.getBlue(), alpha);

        Color result = loopingLinearMovingColorProvider.provide(timestamp);

        assertEquals(expected, result);
    }

    @Test
    public void testProvideValueClockwiseWithoutOverlappingZeroDegrees() {
        long timeAfterTime2 = 50;
        long timestamp = TIME_2 + timeAfterTime2 - PERIOD_MODULO_OFFSET;
        long distanceBetweenTimes = TIME_3 - TIME_2;
        float time3Weight = timeAfterTime2 / (float) distanceBetweenTimes;
        float time2Weight = 1f - time3Weight;

        float[] value2Hsb =
                Color.RGBtoHSB(VALUE_2.getRed(), VALUE_2.getGreen(), VALUE_2.getBlue(), null);
        float[] value3Hsb =
                Color.RGBtoHSB(VALUE_3.getRed(), VALUE_3.getGreen(), VALUE_3.getBlue(), null);

        float hue = (time2Weight * value2Hsb[0]) + (time3Weight * value3Hsb[0]);
        float saturation = (time2Weight * value2Hsb[1]) + (time3Weight * value3Hsb[1]);
        float brightness = (time2Weight * value2Hsb[2]) + (time3Weight * value3Hsb[2]);

        Color rgb = Color.getHSBColor(hue, saturation, brightness);

        int alpha = (int) ((time2Weight * VALUE_2.getAlpha()) + (time3Weight * VALUE_3.getAlpha()));

        Color expected = new Color(rgb.getRed(), rgb.getGreen(), rgb.getBlue(), alpha);

        Color result = loopingLinearMovingColorProvider.provide(timestamp);

        assertEquals(expected, result);
    }

    @Test
    public void testProvideValueClockwiseWithOverlappingZeroDegrees() {
        long timeAfterTime3 = 250;
        long timestamp = TIME_3 + timeAfterTime3 - PERIOD_MODULO_OFFSET;
        long distanceBetweenTimes = TIME_4 - TIME_3;
        float time4Weight = timeAfterTime3 / (float) distanceBetweenTimes;
        float time3Weight = 1f - time4Weight;

        float[] value3Hsb =
                Color.RGBtoHSB(VALUE_3.getRed(), VALUE_3.getGreen(), VALUE_3.getBlue(), null);
        float[] value4Hsb =
                Color.RGBtoHSB(VALUE_4.getRed(), VALUE_4.getGreen(), VALUE_4.getBlue(), null);

        float hue = value3Hsb[0] + (((value4Hsb[0] + 1f) - value3Hsb[0]) * time4Weight) - 1f;
        float saturation = (time3Weight * value3Hsb[1]) + (time4Weight * value4Hsb[1]);
        float brightness = (time3Weight * value3Hsb[2]) + (time4Weight * value4Hsb[2]);

        Color rgb = Color.getHSBColor(hue, saturation, brightness);

        int alpha = (int) ((time3Weight * VALUE_3.getAlpha()) + (time4Weight * VALUE_4.getAlpha()));

        Color expected = new Color(rgb.getRed(), rgb.getGreen(), rgb.getBlue(), alpha);

        Color result = loopingLinearMovingColorProvider.provide(timestamp);

        assertEquals(expected, result);
    }

    @Test
    public void testProvideValueCounterclockwiseWithOverlappingZeroDegrees() {
        long timeAfterTime4 = 350;
        long timestamp = TIME_4 + timeAfterTime4 - PERIOD_MODULO_OFFSET;
        long distanceBetweenTimes = TIME_5 - TIME_4;
        float time5Weight = timeAfterTime4 / (float) distanceBetweenTimes;
        float time4Weight = 1f - time5Weight;

        float[] value4Hsb =
                Color.RGBtoHSB(VALUE_4.getRed(), VALUE_4.getGreen(), VALUE_4.getBlue(), null);
        float[] value5Hsb =
                Color.RGBtoHSB(VALUE_5.getRed(), VALUE_5.getGreen(), VALUE_5.getBlue(), null);

        float startHue = value4Hsb[0];
        float endHue = value5Hsb[0];
        float distance = ((startHue + 1f) - endHue);
        float hue = startHue - (distance * time5Weight) + 1f;

        float saturation = (time4Weight * value4Hsb[1]) + (time5Weight * value5Hsb[1]);
        float brightness = (time4Weight * value4Hsb[2]) + (time5Weight * value5Hsb[2]);

        Color rgb = Color.getHSBColor(hue, saturation, brightness);

        int alpha = (int) ((time4Weight * VALUE_4.getAlpha()) + (time5Weight * VALUE_5.getAlpha()));

        Color expected = new Color(rgb.getRed(), rgb.getGreen(), rgb.getBlue(), alpha);

        Color result = loopingLinearMovingColorProvider.provide(timestamp);

        assertEquals(expected, result);
    }

    @Test
    public void testProvideValueCounterclockwiseWithOverlappingZeroDegreesAcrossPeriodBoundary() {
        long timeAfterTime5 = 450;
        long timestamp = TIME_5 + timeAfterTime5 - PERIOD_MODULO_OFFSET;
        long distanceBetweenTimes = PERIOD_DURATION - TIME_5;
        float time1Weight = timeAfterTime5 / (float) distanceBetweenTimes;
        float time5Weight = 1f - time1Weight;

        float[] value5Hsb =
                Color.RGBtoHSB(VALUE_5.getRed(), VALUE_5.getGreen(), VALUE_5.getBlue(), null);
        float[] value1Hsb =
                Color.RGBtoHSB(VALUE_1.getRed(), VALUE_1.getGreen(), VALUE_1.getBlue(), null);

        float startHue = value5Hsb[0];
        float endHue = value1Hsb[0];
        float distance = ((startHue + 1f) - endHue);
        float hue = startHue - (distance * time1Weight) + 1f;

        float saturation = (time5Weight * value5Hsb[1]) + (time1Weight * value1Hsb[1]);
        float brightness = (time5Weight * value5Hsb[2]) + (time1Weight * value1Hsb[2]);

        Color rgb = Color.getHSBColor(hue, saturation, brightness);

        int alpha = (int) ((time5Weight * VALUE_5.getAlpha()) + (time1Weight * VALUE_1.getAlpha()));

        Color expected = new Color(rgb.getRed(), rgb.getGreen(), rgb.getBlue(), alpha);

        Color result = loopingLinearMovingColorProvider.provide(timestamp);

        assertEquals(expected, result);
    }

    @Test
    public void testReset() {
        long resetTimestamp = 123123L;

        loopingLinearMovingColorProvider.reset(resetTimestamp);

        assertEquals(VALUE_1, loopingLinearMovingColorProvider.provide(resetTimestamp));
    }

    @Test
    public void testRepresentation() {
        assertEquals(VALUES_AT_TIMES, loopingLinearMovingColorProvider.representation());
        assertNotSame(VALUES_AT_TIMES, loopingLinearMovingColorProvider.representation());
    }
}
