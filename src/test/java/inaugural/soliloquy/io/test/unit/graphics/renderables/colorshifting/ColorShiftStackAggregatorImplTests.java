package inaugural.soliloquy.io.test.unit.graphics.renderables.colorshifting;

import inaugural.soliloquy.io.graphics.renderables.colorshifting.ColorShiftStackAggregatorImpl;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeBrightnessShift;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeColorComponentIntensityShift;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeColorRotationShift;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.io.graphics.renderables.colorshifting.*;

import java.util.function.Function;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static org.junit.jupiter.api.Assertions.*;

public class ColorShiftStackAggregatorImplTests {
    private ColorShiftStackAggregator colorShiftStackAggregator;

    @BeforeEach
    public void setUp() {
        colorShiftStackAggregator = new ColorShiftStackAggregatorImpl();
    }

    @Test
    public void testAggregateWithInvalidArgs() {
        var belowNegativeOne = -1.00001f;
        var aboveOne = 1.00001f;

        assertThrows(IllegalArgumentException.class,
                () -> colorShiftStackAggregator.aggregate(null, 0L));
        assertThrows(IllegalArgumentException.class,
                () -> colorShiftStackAggregator.aggregate(listOf((BrightnessShift) null), 0L));
        assertThrows(IllegalArgumentException.class, () -> colorShiftStackAggregator.aggregate(
                listOf(new FakeBrightnessShift(null, true)), 0L));
        assertThrows(IllegalArgumentException.class, () -> colorShiftStackAggregator.aggregate(
                listOf(new FakeBrightnessShift(belowNegativeOne, true)), 0L));
        assertThrows(IllegalArgumentException.class, () -> colorShiftStackAggregator.aggregate(
                listOf(new FakeBrightnessShift(aboveOne, true)), 0L));
        assertThrows(IllegalArgumentException.class, () -> colorShiftStackAggregator.aggregate(
                listOf(new FakeColorComponentIntensityShift(null, 0f, true)), 0L));
        assertThrows(IllegalArgumentException.class, () -> colorShiftStackAggregator.aggregate(
                listOf(new FakeColorComponentIntensityShift(ColorComponent.RED, null, true)), 0L));
        assertThrows(IllegalArgumentException.class, () -> colorShiftStackAggregator.aggregate(
                listOf(new FakeColorComponentIntensityShift(ColorComponent.RED, belowNegativeOne,
                        true)), 0L));
        assertThrows(IllegalArgumentException.class, () -> colorShiftStackAggregator.aggregate(
                listOf(new FakeColorComponentIntensityShift(ColorComponent.RED, aboveOne, true)),
                0L));
        assertThrows(IllegalArgumentException.class, () -> colorShiftStackAggregator.aggregate(
                listOf(new FakeColorRotationShift(null, true)), 0L));
        assertThrows(IllegalArgumentException.class, () -> colorShiftStackAggregator.aggregate(
                listOf(new FakeColorRotationShift(belowNegativeOne, true)), 0L));
        assertThrows(IllegalArgumentException.class, () -> colorShiftStackAggregator.aggregate(
                listOf(new FakeColorRotationShift(aboveOne, true)), 0L));
    }

    @Test
    public void testAggregateSingleColorShift() {
        var brightnessShift = new FakeBrightnessShift(0.123f, false);
        var redShift = new FakeColorComponentIntensityShift(ColorComponent.RED, 0.444f, false);
        var greenShift = new FakeColorComponentIntensityShift(ColorComponent.GREEN, 0.555f, false);
        var blueShift = new FakeColorComponentIntensityShift(ColorComponent.BLUE, 0.666f, false);
        var colorRotationShift = new FakeColorRotationShift(0.789f, false);

        var brightnessShiftNetColorShifts =
                colorShiftStackAggregator.aggregate(listOf(brightnessShift), 0L);
        var redIntensityShift = colorShiftStackAggregator.aggregate(listOf(redShift), 0L);
        var greenIntensityShift = colorShiftStackAggregator.aggregate(listOf(greenShift), 0L);
        var blueIntensityShift = colorShiftStackAggregator.aggregate(listOf(blueShift), 0L);
        var colorRotationShiftNetColorShifts =
                colorShiftStackAggregator.aggregate(listOf(colorRotationShift), 0L);

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
        var brightnessShift1 = new FakeBrightnessShift(0.123f, false);
        var brightnessShift2 = new FakeBrightnessShift(-0.123f, false);
        var redShift1 = new FakeColorComponentIntensityShift(ColorComponent.RED, -0.444f, false);
        var redShift2 = new FakeColorComponentIntensityShift(ColorComponent.RED, 0.444f, false);
        var greenShift1 = new FakeColorComponentIntensityShift(ColorComponent.GREEN, 0.555f, false);
        var greenShift2 =
                new FakeColorComponentIntensityShift(ColorComponent.GREEN, -0.555f, false);
        var blueShift1 = new FakeColorComponentIntensityShift(ColorComponent.BLUE, -0.666f, false);
        var blueShift2 = new FakeColorComponentIntensityShift(ColorComponent.BLUE, 0.666f, false);
        var colorRotationShift1 = new FakeColorRotationShift(0.789f, false);
        var colorRotationShift2 = new FakeColorRotationShift(-0.789f, false);

        var netColorShifts =
                colorShiftStackAggregator.aggregate(listOf(brightnessShift1, brightnessShift2,
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
        var brightnessShift1 = new FakeBrightnessShift(0.123f, true);
        var brightnessShift2 = new FakeBrightnessShift(-0.123f, false);
        var redShift1 = new FakeColorComponentIntensityShift(ColorComponent.RED, -0.444f, true);
        var redShift2 = new FakeColorComponentIntensityShift(ColorComponent.RED, 0.444f, false);
        var greenShift1 = new FakeColorComponentIntensityShift(ColorComponent.GREEN, 0.555f, true);
        var greenShift2 =
                new FakeColorComponentIntensityShift(ColorComponent.GREEN, -0.555f, false);
        var blueShift1 = new FakeColorComponentIntensityShift(ColorComponent.BLUE, -0.666f, true);
        var blueShift2 = new FakeColorComponentIntensityShift(ColorComponent.BLUE, 0.666f, false);
        var colorRotationShift1 = new FakeColorRotationShift(0.789f, true);
        var colorRotationShift2 = new FakeColorRotationShift(-0.789f, false);

        var netColorShifts =
                colorShiftStackAggregator.aggregate(listOf(brightnessShift1, brightnessShift2,
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
