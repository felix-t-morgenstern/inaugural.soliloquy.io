package inaugural.soliloquy.io.test.unit.graphics.renderables.providers;

import inaugural.soliloquy.io.graphics.renderables.providers.FiniteLinearMovingColorProviderImpl;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.io.graphics.renderables.providers.FiniteLinearMovingColorProvider;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.randomLong;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

@ExtendWith(MockitoExtension.class)
public class FiniteLinearMovingColorProviderImplTests {
    private final Map<Long, Color> VALUES_AT_TIMES = mapOf();
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

    private final UUID UUID = java.util.UUID.randomUUID();

    private List<Boolean> hueMovementIsClockwise;

    @Mock private TimestampValidator mockTimestampValidator;

    private FiniteLinearMovingColorProvider provider;

    @BeforeEach
    public void setUp() {
        VALUES_AT_TIMES.put(TIME_1, VALUE_1);
        VALUES_AT_TIMES.put(TIME_2, VALUE_2);
        VALUES_AT_TIMES.put(TIME_3, VALUE_3);
        VALUES_AT_TIMES.put(TIME_4, VALUE_4);
        VALUES_AT_TIMES.put(TIME_5, VALUE_5);

        var transition2IsClockwise = true;
        var transition3IsClockwise = true;
        var transition4IsClockwise = false;

        hueMovementIsClockwise = listOf(
                TRANSITION_1_IS_CLOCKWISE,
                transition2IsClockwise,
                transition3IsClockwise,
                transition4IsClockwise
        );

        provider = new FiniteLinearMovingColorProviderImpl(UUID,
                VALUES_AT_TIMES, hueMovementIsClockwise, null, mockTimestampValidator);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingColorProviderImpl(null, VALUES_AT_TIMES,
                        hueMovementIsClockwise, null, mockTimestampValidator));

        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingColorProviderImpl(UUID, null,
                        hueMovementIsClockwise, null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingColorProviderImpl(UUID, mapOf(),
                        hueMovementIsClockwise, null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingColorProviderImpl(UUID, mapOf(pairOf(null, Color.RED)),
                        hueMovementIsClockwise, null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingColorProviderImpl(UUID, mapOf(pairOf(123L, null)),
                        hueMovementIsClockwise, null, mockTimestampValidator));

        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingColorProviderImpl(UUID, VALUES_AT_TIMES,
                        null, null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingColorProviderImpl(UUID, VALUES_AT_TIMES,
                        listOf(
                                TRANSITION_1_IS_CLOCKWISE,
                                TRANSITION_1_IS_CLOCKWISE,
                                TRANSITION_1_IS_CLOCKWISE,
                                null
                        ), null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingColorProviderImpl(UUID, VALUES_AT_TIMES,
                        listOf(
                                TRANSITION_1_IS_CLOCKWISE,
                                TRANSITION_1_IS_CLOCKWISE,
                                TRANSITION_1_IS_CLOCKWISE
                        ), null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingColorProviderImpl(UUID, VALUES_AT_TIMES,
                        listOf(
                                TRANSITION_1_IS_CLOCKWISE,
                                TRANSITION_1_IS_CLOCKWISE,
                                TRANSITION_1_IS_CLOCKWISE,
                                TRANSITION_1_IS_CLOCKWISE,
                                TRANSITION_1_IS_CLOCKWISE
                        ), null, mockTimestampValidator));

        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingColorProviderImpl(UUID, VALUES_AT_TIMES,
                        hueMovementIsClockwise, null, null));
    }

    @Test
    public void testUuid() {
        assertSame(UUID, provider.uuid());
    }

    @Test
    public void testValuesAtTimestampsRepresentation() {
        assertNotNull(provider.valuesAtTimestampsRepresentation());
        assertEquals(VALUES_AT_TIMES,
                provider.valuesAtTimestampsRepresentation());
        assertNotSame(VALUES_AT_TIMES,
                provider.valuesAtTimestampsRepresentation());
        assertNotSame(provider.valuesAtTimestampsRepresentation(),
                provider.valuesAtTimestampsRepresentation());
    }

    @Test
    public void testHueMovementIsClockwise() {
        assertNotNull(provider.hueMovementIsClockwise());
        assertEquals(hueMovementIsClockwise,
                provider.hueMovementIsClockwise());
        assertNotSame(hueMovementIsClockwise,
                provider.hueMovementIsClockwise());
        assertNotSame(provider.hueMovementIsClockwise(),
                provider.hueMovementIsClockwise());
    }

    @Test
    public void testProvideAtExtremes() {
        assertEquals(VALUE_1, provider.provide(TIME_1 - 1));
        assertEquals(VALUE_1, provider.provide(TIME_1));
        assertEquals(VALUE_5, provider.provide(TIME_5));
        assertEquals(VALUE_5, provider.provide(TIME_5 + 1));
    }

    @Test
    public void testProvideInterpolatedValueCounterclockwiseWithoutOverlappingZeroDegrees() {
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

        Color result = provider.provide(timestamp);

        assertEquals(expected, result);
    }

    @Test
    public void testProvideInterpolatedValueClockwiseWithoutOverlappingZeroDegrees() {
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

        Color result = provider.provide(timestamp);

        assertEquals(expected, result);
    }

    @Test
    public void testProvideInterpolatedValueClockwiseWithOverlappingZeroDegrees() {
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

        Color result = provider.provide(timestamp);

        assertEquals(expected, result);
    }

    @Test
    public void testProvideInterpolatedValueCounterclockwiseWithOverlappingZeroDegrees() {
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

        Color result = provider.provide(timestamp);

        assertEquals(expected, result);
    }

    @Test
    public void testPausedTimestamp() {
        var pausedTimestamp = randomLong();
        when(mockTimestampValidator.mostRecentTimestamp()).thenReturn(pausedTimestamp);

        var pausedProvider = new FiniteLinearMovingColorProviderImpl(UUID, VALUES_AT_TIMES,
                hueMovementIsClockwise, pausedTimestamp, mockTimestampValidator);

        assertEquals(pausedTimestamp, (long) pausedProvider.pausedTimestamp());
    }

    @Test
    public void testProvideWhenPaused() {
        provider.reportPause(TIME_1);

        assertEquals(VALUE_1, provider.provide(123123123L));
    }

    @Test
    public void testReportPauseWhilePausedOrViceVersa() {
        var timestamp = randomLong();
        when(mockTimestampValidator.mostRecentTimestamp()).thenReturn(timestamp);

        assertThrows(UnsupportedOperationException.class,
                () -> provider.reportUnpause(timestamp));

        provider.reportPause(timestamp);

        assertThrows(UnsupportedOperationException.class,
                () -> provider.reportPause(timestamp));
    }

    @Test
    public void testReportPauseAndUnpauseUpdatesTimestampsForValues() {
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

        provider.reportPause(timestamp);
        provider.reportUnpause(timestamp + pauseDuration);

        Map<Long, Color> valuesAtTimestampsRepresentation =
                provider.valuesAtTimestampsRepresentation();

        assertEquals(VALUE_1, valuesAtTimestampsRepresentation.get(TIME_1 + pauseDuration));
        assertEquals(VALUE_2, valuesAtTimestampsRepresentation.get(TIME_2 + pauseDuration));
        assertEquals(VALUE_3, valuesAtTimestampsRepresentation.get(TIME_3 + pauseDuration));

        assertEquals(expected,
                provider.provide(timestamp + pauseDuration));
    }
}
