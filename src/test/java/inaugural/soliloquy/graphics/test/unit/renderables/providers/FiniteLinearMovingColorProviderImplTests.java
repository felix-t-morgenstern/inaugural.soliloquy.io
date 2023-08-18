package inaugural.soliloquy.graphics.test.unit.renderables.providers;

import inaugural.soliloquy.graphics.renderables.providers.FiniteLinearMovingColorProviderImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.renderables.providers.FiniteLinearMovingColorProvider;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FiniteLinearMovingColorProviderImplTests {
    private final HashMap<Long, Color> VALUES_AT_TIMES = new HashMap<>();
    private final long TIME_1 = 100L;
    private final Color VALUE_1 = new Color(188, 130, 217, 255);
    private final long TIME_2 = 300L;
    private final Color VALUE_2 = new Color(8, 79, 35, 127);
    private final long TIME_3 = 500L;
    private final Color VALUE_3 = new Color(0, 191, 255, 63);
    private final long TIME_4 = 800L;
    private final Color VALUE_4 = new Color(199, 222, 140, 127);
    private final long TIME_5 = 1200L;
    private final Color VALUE_5 = new Color(6, 36, 117, 255);

    private final boolean TRANSITION_1_IS_CLOCKWISE = false;
    private final boolean TRANSITION_2_IS_CLOCKWISE = true;
    private final boolean TRANSITION_3_IS_CLOCKWISE = true;
    private final boolean TRANSITION_4_IS_CLOCKWISE = false;

    private final UUID UUID = java.util.UUID.randomUUID();

    private ArrayList<Boolean> hueMovementIsClockwise;

    private final long MOST_RECENT_TIMESTAMP = 34L;

    private FiniteLinearMovingColorProvider finiteLinearMovingColorProvider;

    @BeforeEach
    void setUp() {
        VALUES_AT_TIMES.put(TIME_1, VALUE_1);
        VALUES_AT_TIMES.put(TIME_2, VALUE_2);
        VALUES_AT_TIMES.put(TIME_3, VALUE_3);
        VALUES_AT_TIMES.put(TIME_4, VALUE_4);
        VALUES_AT_TIMES.put(TIME_5, VALUE_5);

        hueMovementIsClockwise = new ArrayList<Boolean>() {{
            add(TRANSITION_1_IS_CLOCKWISE);
            add(TRANSITION_2_IS_CLOCKWISE);
            add(TRANSITION_3_IS_CLOCKWISE);
            add(TRANSITION_4_IS_CLOCKWISE);
        }};

        finiteLinearMovingColorProvider = new FiniteLinearMovingColorProviderImpl(UUID,
                VALUES_AT_TIMES, hueMovementIsClockwise, null, MOST_RECENT_TIMESTAMP);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingColorProviderImpl(null, VALUES_AT_TIMES,
                        hueMovementIsClockwise, null, MOST_RECENT_TIMESTAMP));

        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingColorProviderImpl(UUID, null,
                        hueMovementIsClockwise, null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingColorProviderImpl(UUID, new HashMap<>(),
                        hueMovementIsClockwise, null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingColorProviderImpl(UUID, new HashMap<Long, Color>() {{
                    put(null, Color.RED);
                }},
                        hueMovementIsClockwise, null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingColorProviderImpl(UUID, new HashMap<Long, Color>() {{
                    put(123L, null);
                }},
                        hueMovementIsClockwise, null, MOST_RECENT_TIMESTAMP));

        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingColorProviderImpl(UUID, VALUES_AT_TIMES,
                        null, null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingColorProviderImpl(UUID, VALUES_AT_TIMES,
                        new ArrayList<Boolean>() {{
                            add(TRANSITION_1_IS_CLOCKWISE);
                            add(TRANSITION_1_IS_CLOCKWISE);
                            add(TRANSITION_1_IS_CLOCKWISE);
                            add(null);
                        }}, null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingColorProviderImpl(UUID, VALUES_AT_TIMES,
                        new ArrayList<Boolean>() {{
                            add(TRANSITION_1_IS_CLOCKWISE);
                            add(TRANSITION_1_IS_CLOCKWISE);
                            add(TRANSITION_1_IS_CLOCKWISE);
                        }}, null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingColorProviderImpl(UUID, VALUES_AT_TIMES,
                        new ArrayList<Boolean>() {{
                            add(TRANSITION_1_IS_CLOCKWISE);
                            add(TRANSITION_1_IS_CLOCKWISE);
                            add(TRANSITION_1_IS_CLOCKWISE);
                            add(TRANSITION_1_IS_CLOCKWISE);
                            add(TRANSITION_1_IS_CLOCKWISE);
                        }}, null, MOST_RECENT_TIMESTAMP));

        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingColorProviderImpl(UUID, VALUES_AT_TIMES,
                        hueMovementIsClockwise, 12L, null));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingColorProviderImpl(UUID, VALUES_AT_TIMES,
                        hueMovementIsClockwise, MOST_RECENT_TIMESTAMP + 1,
                        MOST_RECENT_TIMESTAMP));
    }

    @Test
    void testUuid() {
        assertSame(UUID, finiteLinearMovingColorProvider.uuid());
    }

    @Test
    void testMostRecentTimestamp() {
        assertEquals(MOST_RECENT_TIMESTAMP,
                (long) finiteLinearMovingColorProvider.mostRecentTimestamp());
    }

    @Test
    void testValuesAtTimestampsRepresentation() {
        assertNotNull(finiteLinearMovingColorProvider.valuesAtTimestampsRepresentation());
        assertEquals(VALUES_AT_TIMES,
                finiteLinearMovingColorProvider.valuesAtTimestampsRepresentation());
        assertNotSame(VALUES_AT_TIMES,
                finiteLinearMovingColorProvider.valuesAtTimestampsRepresentation());
        assertNotSame(finiteLinearMovingColorProvider.valuesAtTimestampsRepresentation(),
                finiteLinearMovingColorProvider.valuesAtTimestampsRepresentation());
    }

    @Test
    void testHueMovementIsClockwise() {
        assertNotNull(finiteLinearMovingColorProvider.hueMovementIsClockwise());
        assertEquals(hueMovementIsClockwise,
                finiteLinearMovingColorProvider.hueMovementIsClockwise());
        assertNotSame(hueMovementIsClockwise,
                finiteLinearMovingColorProvider.hueMovementIsClockwise());
        assertNotSame(finiteLinearMovingColorProvider.hueMovementIsClockwise(),
                finiteLinearMovingColorProvider.hueMovementIsClockwise());
    }

    @Test
    void testArchetype() {
        assertNotNull(finiteLinearMovingColorProvider.archetype());
    }

    @Test
    void testProvideAtExtremes() {
        assertEquals(VALUE_1, finiteLinearMovingColorProvider.provide(TIME_1 - 1));
        assertEquals(VALUE_1, finiteLinearMovingColorProvider.provide(TIME_1));
        assertEquals(VALUE_5, finiteLinearMovingColorProvider.provide(TIME_5));
        assertEquals(VALUE_5, finiteLinearMovingColorProvider.provide(TIME_5 + 1));
    }

    @Test
    void testProvideInterpolatedValueCounterclockwiseWithoutOverlappingZeroDegrees() {
        long timeAfterTime1 = 50;
        long timestamp = TIME_1 + timeAfterTime1;
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

        Color result = finiteLinearMovingColorProvider.provide(timestamp);

        assertEquals(expected, result);
    }

    @Test
    void testProvideInterpolatedValueClockwiseWithoutOverlappingZeroDegrees() {
        long timeAfterTime2 = 50;
        long timestamp = TIME_2 + timeAfterTime2;
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

        Color result = finiteLinearMovingColorProvider.provide(timestamp);

        assertEquals(expected, result);
    }

    @Test
    void testProvideInterpolatedValueClockwiseWithOverlappingZeroDegrees() {
        long timeAfterTime3 = 250;
        long timestamp = TIME_3 + timeAfterTime3;
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

        Color result = finiteLinearMovingColorProvider.provide(timestamp);

        assertEquals(expected, result);
    }

    @Test
    void testProvideInterpolatedValueCounterclockwiseWithOverlappingZeroDegrees() {
        long timeAfterTime4 = 350;
        long timestamp = TIME_4 + timeAfterTime4;
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

        Color result = finiteLinearMovingColorProvider.provide(timestamp);

        assertEquals(expected, result);
    }

    @Test
    void testPausedTimestamp() {
        long pausedTimestamp = 12L;
        FiniteLinearMovingColorProvider finiteLinearMovingColorProvider =
                new FiniteLinearMovingColorProviderImpl(UUID, VALUES_AT_TIMES,
                        hueMovementIsClockwise, pausedTimestamp, MOST_RECENT_TIMESTAMP);

        assertEquals(pausedTimestamp,
                (long) finiteLinearMovingColorProvider.pausedTimestamp());
    }

    @Test
    void testProvideWhenPaused() {
        finiteLinearMovingColorProvider.reportPause(TIME_1);

        assertEquals(VALUE_1, finiteLinearMovingColorProvider.provide(123123123L));
    }

    @Test
    void testProvideOrReportPauseOrUnpauseWithInvalidTimestampAndMostRecentTimestamp() {
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingColorProvider.provide(MOST_RECENT_TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingColorProvider.reportPause(MOST_RECENT_TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingColorProvider.reportUnpause(MOST_RECENT_TIMESTAMP - 1));

        finiteLinearMovingColorProvider.provide(MOST_RECENT_TIMESTAMP + 1);

        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingColorProvider.provide(MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingColorProvider.reportPause(MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingColorProvider.reportUnpause(MOST_RECENT_TIMESTAMP));
        assertEquals(MOST_RECENT_TIMESTAMP + 1,
                (long) finiteLinearMovingColorProvider.mostRecentTimestamp());

        finiteLinearMovingColorProvider.reportPause(MOST_RECENT_TIMESTAMP + 2);

        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingColorProvider.provide(MOST_RECENT_TIMESTAMP + 1));
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingColorProvider.reportPause(MOST_RECENT_TIMESTAMP + 1));
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingColorProvider.reportUnpause(MOST_RECENT_TIMESTAMP + 1));
        assertEquals(MOST_RECENT_TIMESTAMP + 2,
                (long) finiteLinearMovingColorProvider.mostRecentTimestamp());
        assertEquals(MOST_RECENT_TIMESTAMP + 2,
                (long) finiteLinearMovingColorProvider.pausedTimestamp());

        finiteLinearMovingColorProvider.reportUnpause(MOST_RECENT_TIMESTAMP + 3);

        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingColorProvider.provide(MOST_RECENT_TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingColorProvider.reportPause(MOST_RECENT_TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingColorProvider.reportUnpause(MOST_RECENT_TIMESTAMP + 2));
        assertEquals(MOST_RECENT_TIMESTAMP + 3,
                (long) finiteLinearMovingColorProvider.mostRecentTimestamp());
        assertNull(finiteLinearMovingColorProvider.pausedTimestamp());
    }

    @Test
    void testReportPauseWhilePausedOrViceVersa() {
        assertThrows(UnsupportedOperationException.class, () ->
                finiteLinearMovingColorProvider.reportUnpause(MOST_RECENT_TIMESTAMP));

        finiteLinearMovingColorProvider.reportPause(MOST_RECENT_TIMESTAMP);

        assertThrows(UnsupportedOperationException.class, () ->
                finiteLinearMovingColorProvider.reportPause(MOST_RECENT_TIMESTAMP));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(FiniteLinearMovingColorProvider.class.getCanonicalName(),
                finiteLinearMovingColorProvider.getInterfaceName());
    }

    @Test
    void testReportPauseAndUnpauseUpdatesTimestampsForValues() {
        long pauseDuration = 123123L;
        long timeAfterTime1 = 50;
        long timestamp = TIME_1 + timeAfterTime1;
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

        finiteLinearMovingColorProvider.reportPause(MOST_RECENT_TIMESTAMP);
        finiteLinearMovingColorProvider.reportUnpause(MOST_RECENT_TIMESTAMP + pauseDuration);

        Map<Long, Color> valuesAtTimestampsRepresentation =
                finiteLinearMovingColorProvider.valuesAtTimestampsRepresentation();

        assertEquals(VALUE_1, valuesAtTimestampsRepresentation.get(TIME_1 + pauseDuration));
        assertEquals(VALUE_2, valuesAtTimestampsRepresentation.get(TIME_2 + pauseDuration));
        assertEquals(VALUE_3, valuesAtTimestampsRepresentation.get(TIME_3 + pauseDuration));

        assertEquals(expected,
                finiteLinearMovingColorProvider.provide(timestamp + pauseDuration));
    }
}
