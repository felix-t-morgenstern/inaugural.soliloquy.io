package inaugural.soliloquy.io.test.unit.graphics.renderables.colorshifting;

import inaugural.soliloquy.io.graphics.renderables.colorshifting.ColorShiftStackAggregatorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.io.graphics.renderables.colorshifting.BrightnessShift;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorComponent;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShiftStackAggregator;

import java.util.function.Function;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.testing.Mock.generateMockStaticProvider;
import static org.junit.jupiter.api.Assertions.*;
import static soliloquy.specs.io.graphics.renderables.colorshifting.BrightnessShift.brightnessShift;
import static soliloquy.specs.io.graphics.renderables.colorshifting.ColorComponentIntensityShift.colorComponentShift;
import static soliloquy.specs.io.graphics.renderables.colorshifting.ColorRotationShift.rotationShift;

public class ColorShiftStackAggregatorImplTests {
    private ColorShiftStackAggregator aggregator;

    @BeforeEach
    public void setUp() {
        aggregator = new ColorShiftStackAggregatorImpl();
    }

    @Test
    public void testAggregateWithInvalidArgs() {
        var belowNegativeOne = generateMockStaticProvider(-1.00001f);
        var aboveOne = generateMockStaticProvider(1.00001f);

        assertThrows(IllegalArgumentException.class, () -> aggregator.aggregate(null, 0L));
        assertThrows(IllegalArgumentException.class,
                () -> aggregator.aggregate(listOf((BrightnessShift) null), 0L));
        assertThrows(IllegalArgumentException.class, () -> aggregator.aggregate(
                listOf(brightnessShift(generateMockStaticProvider(null), true)), 0L));
        assertThrows(IllegalArgumentException.class,
                () -> aggregator.aggregate(listOf(brightnessShift(belowNegativeOne, true)), 0L));
        assertThrows(IllegalArgumentException.class,
                () -> aggregator.aggregate(listOf(brightnessShift(aboveOne, true)), 0L));
        assertThrows(IllegalArgumentException.class, () -> aggregator.aggregate(
                listOf(colorComponentShift(generateMockStaticProvider(0f), true, null)), 0L));
        assertThrows(IllegalArgumentException.class, () -> aggregator.aggregate(
                listOf(colorComponentShift(null, true, ColorComponent.RED)), 0L));
        assertThrows(IllegalArgumentException.class, () -> aggregator.aggregate(
                listOf(colorComponentShift(belowNegativeOne, true, ColorComponent.RED)), 0L));
        assertThrows(IllegalArgumentException.class, () -> aggregator.aggregate(
                listOf(colorComponentShift(aboveOne, true, ColorComponent.RED)), 0L));
        assertThrows(IllegalArgumentException.class,
                () -> aggregator.aggregate(listOf(rotationShift(null, true)), 0L));
        assertThrows(IllegalArgumentException.class,
                () -> aggregator.aggregate(listOf(rotationShift(belowNegativeOne, true)), 0L));
        assertThrows(IllegalArgumentException.class,
                () -> aggregator.aggregate(listOf(rotationShift(aboveOne, true)), 0L));
    }

    @Test
    public void testAggregateSingleColorShift() {
        var brightnessShift = brightnessShift(generateMockStaticProvider(0.123f), false);
        var redShift =
                colorComponentShift(generateMockStaticProvider(0.444f), false, ColorComponent.RED);
        var greenShift = colorComponentShift(generateMockStaticProvider(0.555f), false,
                ColorComponent.GREEN);
        var blueShift =
                colorComponentShift(generateMockStaticProvider(0.666f), false, ColorComponent.BLUE);
        var colorRotationShift = rotationShift(generateMockStaticProvider(0.789f), false);

        var brightnessShiftNetColorShifts = aggregator.aggregate(listOf(brightnessShift), 0L);
        var redIntensityShift = aggregator.aggregate(listOf(redShift), 0L);
        var greenIntensityShift = aggregator.aggregate(listOf(greenShift), 0L);
        var blueIntensityShift = aggregator.aggregate(listOf(blueShift), 0L);
        var colorRotationShiftNetColorShifts = aggregator.aggregate(listOf(colorRotationShift), 0L);

        assertNotNull(brightnessShiftNetColorShifts);
        assertEquals(0.123f, brightnessShiftNetColorShifts.BRIGHTNESS_SHIFT);
        assertEquals(0f, brightnessShiftNetColorShifts.RED_INTENSITY_SHIFT);
        assertEquals(0f, brightnessShiftNetColorShifts.GREEN_INTENSITY_SHIFT);
        assertEquals(0f, brightnessShiftNetColorShifts.BLUE_INTENSITY_SHIFT);
        assertEquals(0f, brightnessShiftNetColorShifts.COLOR_ROTATION_SHIFT);

        assertNotNull(redIntensityShift);
        assertEquals(0f, redIntensityShift.BRIGHTNESS_SHIFT);
        assertEquals(0.444f, redIntensityShift.RED_INTENSITY_SHIFT);
        assertEquals(0f, redIntensityShift.GREEN_INTENSITY_SHIFT);
        assertEquals(0f, redIntensityShift.BLUE_INTENSITY_SHIFT);
        assertEquals(0f, redIntensityShift.COLOR_ROTATION_SHIFT);

        assertNotNull(greenIntensityShift);
        assertEquals(0f, greenIntensityShift.BRIGHTNESS_SHIFT);
        assertEquals(0f, greenIntensityShift.RED_INTENSITY_SHIFT);
        assertEquals(0.555f, greenIntensityShift.GREEN_INTENSITY_SHIFT);
        assertEquals(0f, greenIntensityShift.BLUE_INTENSITY_SHIFT);
        assertEquals(0f, greenIntensityShift.COLOR_ROTATION_SHIFT);

        assertNotNull(blueIntensityShift);
        assertEquals(0f, blueIntensityShift.BRIGHTNESS_SHIFT);
        assertEquals(0f, blueIntensityShift.RED_INTENSITY_SHIFT);
        assertEquals(0f, blueIntensityShift.GREEN_INTENSITY_SHIFT);
        assertEquals(0.666f, blueIntensityShift.BLUE_INTENSITY_SHIFT);
        assertEquals(0f, blueIntensityShift.COLOR_ROTATION_SHIFT);

        assertNotNull(colorRotationShiftNetColorShifts);
        assertEquals(0f, colorRotationShiftNetColorShifts.BRIGHTNESS_SHIFT);
        assertEquals(0f, colorRotationShiftNetColorShifts.RED_INTENSITY_SHIFT);
        assertEquals(0f, colorRotationShiftNetColorShifts.GREEN_INTENSITY_SHIFT);
        assertEquals(0f, colorRotationShiftNetColorShifts.BLUE_INTENSITY_SHIFT);
        assertEquals(0.789f, colorRotationShiftNetColorShifts.COLOR_ROTATION_SHIFT);
    }

    @Test
    public void testAggregateMultipleValuesOfEachType() {
        var brightnessShift1 = brightnessShift(generateMockStaticProvider(0.123f), false);
        var brightnessShift2 = brightnessShift(generateMockStaticProvider(-0.123f), false);
        var redShift1 =
                colorComponentShift(generateMockStaticProvider(-0.444f), false, ColorComponent.RED);
        var redShift2 =
                colorComponentShift(generateMockStaticProvider(0.444f), false, ColorComponent.RED);
        var greenShift1 = colorComponentShift(generateMockStaticProvider(0.555f), false,
                ColorComponent.GREEN);
        var greenShift2 = colorComponentShift(generateMockStaticProvider(-0.555f), false,
                ColorComponent.GREEN);
        var blueShift1 = colorComponentShift(generateMockStaticProvider(-0.666f), false,
                ColorComponent.BLUE);
        var blueShift2 =
                colorComponentShift(generateMockStaticProvider(0.666f), false, ColorComponent.BLUE);
        var colorRotationShift1 = rotationShift(generateMockStaticProvider(0.789f), false);
        var colorRotationShift2 = rotationShift(generateMockStaticProvider(-0.789f), false);

        var netColorShifts =
                aggregator.aggregate(listOf(brightnessShift1, brightnessShift2,
                        redShift1,
                        redShift2,
                        greenShift1,
                        greenShift2,
                        blueShift1,
                        blueShift2,
                        colorRotationShift1,
                        colorRotationShift2
                ), 0L);

        Function<Float, Float> getExpectedValue = value -> value - ((1f + value) * value);

        assertNotNull(netColorShifts);
        assertEquals((float) getExpectedValue.apply(0.123f), netColorShifts.BRIGHTNESS_SHIFT);
        assertEquals(-(float) getExpectedValue.apply(0.444f), netColorShifts.RED_INTENSITY_SHIFT);
        assertEquals((float) getExpectedValue.apply(0.555f), netColorShifts.GREEN_INTENSITY_SHIFT);
        assertEquals(-(float) getExpectedValue.apply(0.666f), netColorShifts.BLUE_INTENSITY_SHIFT);
        assertEquals((float) getExpectedValue.apply(0.789f), netColorShifts.COLOR_ROTATION_SHIFT);
    }

    @Test
    public void testAggregateWithOverridesPriorShiftsOfSameType() {
        var brightnessShift1 = brightnessShift(generateMockStaticProvider(0.123f), true);
        var brightnessShift2 = brightnessShift(generateMockStaticProvider(-0.123f), false);
        var redShift1 =
                colorComponentShift(generateMockStaticProvider(-0.444f), true, ColorComponent.RED);
        var redShift2 =
                colorComponentShift(generateMockStaticProvider(0.444f), false, ColorComponent.RED);
        var greenShift1 =
                colorComponentShift(generateMockStaticProvider(0.555f), true, ColorComponent.GREEN);
        var greenShift2 = colorComponentShift(generateMockStaticProvider(-0.555f), false,
                ColorComponent.GREEN);
        var blueShift1 =
                colorComponentShift(generateMockStaticProvider(-0.666f), true, ColorComponent.BLUE);
        var blueShift2 =
                colorComponentShift(generateMockStaticProvider(0.666f), false, ColorComponent.BLUE);
        var colorRotationShift1 = rotationShift(generateMockStaticProvider(0.789f), true);
        var colorRotationShift2 = rotationShift(generateMockStaticProvider(-0.789f), false);

        var netColorShifts =
                aggregator.aggregate(listOf(brightnessShift1, brightnessShift2,
                        redShift1,
                        redShift2,
                        greenShift1,
                        greenShift2,
                        blueShift1,
                        blueShift2,
                        colorRotationShift1,
                        colorRotationShift2
                ), 0L);

        assertNotNull(netColorShifts);
        assertEquals(0.123f, netColorShifts.BRIGHTNESS_SHIFT);
        assertEquals(-0.444f, netColorShifts.RED_INTENSITY_SHIFT);
        assertEquals(0.555f, netColorShifts.GREEN_INTENSITY_SHIFT);
        assertEquals(-0.666f, netColorShifts.BLUE_INTENSITY_SHIFT);
        assertEquals(0.789f, netColorShifts.COLOR_ROTATION_SHIFT);
    }
}
