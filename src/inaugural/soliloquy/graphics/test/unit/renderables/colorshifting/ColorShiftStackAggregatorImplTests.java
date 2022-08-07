package inaugural.soliloquy.graphics.test.unit.renderables.colorshifting;

import inaugural.soliloquy.graphics.renderables.colorshifting.ColorShiftStackAggregatorImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeBrightnessShift;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeColorComponentIntensityShift;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeColorRotationShift;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.renderables.colorshifting.ColorComponent;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShiftStackAggregator;
import soliloquy.specs.graphics.renderables.colorshifting.NetColorShifts;

import java.util.ArrayList;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class ColorShiftStackAggregatorImplTests {
    private ColorShiftStackAggregator _colorShiftStackAggregator;

    @BeforeEach
    void setUp() {
        _colorShiftStackAggregator = new ColorShiftStackAggregatorImpl();
    }

    @Test
    void testAggregateWithInvalidParams() {
        float belowNegativeOne = -1.00001f;
        float aboveOne = 1.00001f;

        assertThrows(IllegalArgumentException.class, () -> _colorShiftStackAggregator.aggregate(
                null, 0L
        ));
        assertThrows(IllegalArgumentException.class, () -> _colorShiftStackAggregator.aggregate(
                new ArrayList<ColorShift>() {{
                    add(null);
                }}, 0L
        ));
        assertThrows(IllegalArgumentException.class, () -> _colorShiftStackAggregator.aggregate(
                new ArrayList<ColorShift>() {{
                    add(new FakeBrightnessShift(null, true));
                }}, 0L
        ));
        assertThrows(IllegalArgumentException.class, () -> _colorShiftStackAggregator.aggregate(
                new ArrayList<ColorShift>() {{
                    add(new FakeBrightnessShift(belowNegativeOne, true));
                }}, 0L
        ));
        assertThrows(IllegalArgumentException.class, () -> _colorShiftStackAggregator.aggregate(
                new ArrayList<ColorShift>() {{
                    add(new FakeBrightnessShift(aboveOne, true));
                }}, 0L
        ));
        assertThrows(IllegalArgumentException.class, () -> _colorShiftStackAggregator.aggregate(
                new ArrayList<ColorShift>() {{
                    add(new FakeColorComponentIntensityShift(null, 0f, true));
                }}, 0L
        ));
        assertThrows(IllegalArgumentException.class, () -> _colorShiftStackAggregator.aggregate(
                new ArrayList<ColorShift>() {{
                    add(new FakeColorComponentIntensityShift(ColorComponent.RED, null, true));
                }}, 0L
        ));
        assertThrows(IllegalArgumentException.class, () -> _colorShiftStackAggregator.aggregate(
                new ArrayList<ColorShift>() {{
                    add(new FakeColorComponentIntensityShift(ColorComponent.RED, belowNegativeOne, true));
                }}, 0L
        ));
        assertThrows(IllegalArgumentException.class, () -> _colorShiftStackAggregator.aggregate(
                new ArrayList<ColorShift>() {{
                    add(new FakeColorComponentIntensityShift(ColorComponent.RED, aboveOne, true));
                }}, 0L
        ));
        assertThrows(IllegalArgumentException.class, () -> _colorShiftStackAggregator.aggregate(
                new ArrayList<ColorShift>() {{
                    add(new FakeColorRotationShift(null, true));
                }}, 0L
        ));
        assertThrows(IllegalArgumentException.class, () -> _colorShiftStackAggregator.aggregate(
                new ArrayList<ColorShift>() {{
                    add(new FakeColorRotationShift(belowNegativeOne, true));
                }}, 0L
        ));
        assertThrows(IllegalArgumentException.class, () -> _colorShiftStackAggregator.aggregate(
                new ArrayList<ColorShift>() {{
                    add(new FakeColorRotationShift(aboveOne, true));
                }}, 0L
        ));
    }

    @Test
    void testAggregateSingleColorShift() {
        FakeBrightnessShift brightnessShift = new FakeBrightnessShift(0.123f, false);
        FakeColorComponentIntensityShift redShift =
                new FakeColorComponentIntensityShift(ColorComponent.RED, 0.444f, false);
        FakeColorComponentIntensityShift greenShift =
                new FakeColorComponentIntensityShift(ColorComponent.GREEN, 0.555f, false);
        FakeColorComponentIntensityShift blueShift =
                new FakeColorComponentIntensityShift(ColorComponent.BLUE, 0.666f, false);
        FakeColorRotationShift colorRotationShift = new FakeColorRotationShift(0.789f, false);

        NetColorShifts brightnessShiftNetColorShifts = _colorShiftStackAggregator.aggregate(
                new ArrayList<ColorShift>() {{
                    add(brightnessShift);
                }}, 0L);
        NetColorShifts redIntensityShift = _colorShiftStackAggregator.aggregate(
                new ArrayList<ColorShift>() {{
                    add(redShift);
                }}, 0L);
        NetColorShifts greenIntensityShift = _colorShiftStackAggregator.aggregate(
                new ArrayList<ColorShift>() {{
                    add(greenShift);
                }}, 0L);
        NetColorShifts blueIntensityShift = _colorShiftStackAggregator.aggregate(
                new ArrayList<ColorShift>() {{
                    add(blueShift);
                }}, 0L);
        NetColorShifts colorRotationShiftNetColorShifts = _colorShiftStackAggregator.aggregate(
                new ArrayList<ColorShift>() {{
                    add(colorRotationShift);
                }}, 0L);

        assertNotNull(brightnessShiftNetColorShifts);
        assertEquals(NetColorShifts.class.getCanonicalName(),
                brightnessShiftNetColorShifts.getInterfaceName());
        assertEquals(0.123f, brightnessShiftNetColorShifts.brightnessShift());
        assertEquals(0f, brightnessShiftNetColorShifts.redIntensityShift());
        assertEquals(0f, brightnessShiftNetColorShifts.greenIntensityShift());
        assertEquals(0f, brightnessShiftNetColorShifts.blueIntensityShift());
        assertEquals(0f, brightnessShiftNetColorShifts.colorRotationShift());

        assertNotNull(redIntensityShift);
        assertEquals(NetColorShifts.class.getCanonicalName(),
                redIntensityShift.getInterfaceName());
        assertEquals(0f, redIntensityShift.brightnessShift());
        assertEquals(0.444f, redIntensityShift.redIntensityShift());
        assertEquals(0f, redIntensityShift.greenIntensityShift());
        assertEquals(0f, redIntensityShift.blueIntensityShift());
        assertEquals(0f, redIntensityShift.colorRotationShift());

        assertNotNull(greenIntensityShift);
        assertEquals(NetColorShifts.class.getCanonicalName(),
                greenIntensityShift.getInterfaceName());
        assertEquals(0f, greenIntensityShift.brightnessShift());
        assertEquals(0f, greenIntensityShift.redIntensityShift());
        assertEquals(0.555f, greenIntensityShift.greenIntensityShift());
        assertEquals(0f, greenIntensityShift.blueIntensityShift());
        assertEquals(0f, greenIntensityShift.colorRotationShift());

        assertNotNull(blueIntensityShift);
        assertEquals(NetColorShifts.class.getCanonicalName(),
                blueIntensityShift.getInterfaceName());
        assertEquals(0f, blueIntensityShift.brightnessShift());
        assertEquals(0f, blueIntensityShift.redIntensityShift());
        assertEquals(0f, blueIntensityShift.greenIntensityShift());
        assertEquals(0.666f, blueIntensityShift.blueIntensityShift());
        assertEquals(0f, blueIntensityShift.colorRotationShift());

        assertNotNull(colorRotationShiftNetColorShifts);
        assertEquals(NetColorShifts.class.getCanonicalName(),
                colorRotationShiftNetColorShifts.getInterfaceName());
        assertEquals(0f, colorRotationShiftNetColorShifts.brightnessShift());
        assertEquals(0f, colorRotationShiftNetColorShifts.redIntensityShift());
        assertEquals(0f, colorRotationShiftNetColorShifts.greenIntensityShift());
        assertEquals(0f, colorRotationShiftNetColorShifts.blueIntensityShift());
        assertEquals(0.789f, colorRotationShiftNetColorShifts.colorRotationShift());
    }

    /** @noinspection RedundantCast*/
    @Test
    void testAggregateMultipleValuesOfEachType() {
        FakeBrightnessShift brightnessShift1 = new FakeBrightnessShift(0.123f, false);
        FakeBrightnessShift brightnessShift2 = new FakeBrightnessShift(-0.123f, false);
        FakeColorComponentIntensityShift redShift1 =
                new FakeColorComponentIntensityShift(ColorComponent.RED, -0.444f, false);
        FakeColorComponentIntensityShift redShift2 =
                new FakeColorComponentIntensityShift(ColorComponent.RED, 0.444f, false);
        FakeColorComponentIntensityShift greenShift1 =
                new FakeColorComponentIntensityShift(ColorComponent.GREEN, 0.555f, false);
        FakeColorComponentIntensityShift greenShift2 =
                new FakeColorComponentIntensityShift(ColorComponent.GREEN, -0.555f, false);
        FakeColorComponentIntensityShift blueShift1 =
                new FakeColorComponentIntensityShift(ColorComponent.BLUE, -0.666f, false);
        FakeColorComponentIntensityShift blueShift2 =
                new FakeColorComponentIntensityShift(ColorComponent.BLUE, 0.666f, false);
        FakeColorRotationShift colorRotationShift1 = new FakeColorRotationShift(0.789f, false);
        FakeColorRotationShift colorRotationShift2 = new FakeColorRotationShift(-0.789f, false);

        NetColorShifts netColorShifts = _colorShiftStackAggregator.aggregate(
                new ArrayList<ColorShift>() {{
                    add(brightnessShift1);
                    add(brightnessShift2);
                    add(redShift1);
                    add(redShift2);
                    add(greenShift1);
                    add(greenShift2);
                    add(blueShift1);
                    add(blueShift2);
                    add(colorRotationShift1);
                    add(colorRotationShift2);
                }}, 0L);

        Function<Float, Float> getExpectedValue = value -> value - ((1f + value) * value);

        assertNotNull(netColorShifts);
        assertEquals(NetColorShifts.class.getCanonicalName(),
                netColorShifts.getInterfaceName());
        assertEquals((float)getExpectedValue.apply(0.123f), netColorShifts.brightnessShift());
        assertEquals(-(float)getExpectedValue.apply(0.444f), netColorShifts.redIntensityShift());
        assertEquals((float)getExpectedValue.apply(0.555f), netColorShifts.greenIntensityShift());
        assertEquals(-(float)getExpectedValue.apply(0.666f), netColorShifts.blueIntensityShift());
        assertEquals((float)getExpectedValue.apply(0.789f), netColorShifts.colorRotationShift());
    }

    @Test
    void testAggregateWithOverridesPriorShiftsOfSameType() {
        FakeBrightnessShift brightnessShift1 = new FakeBrightnessShift(0.123f, true);
        FakeBrightnessShift brightnessShift2 = new FakeBrightnessShift(-0.123f, false);
        FakeColorComponentIntensityShift redShift1 =
                new FakeColorComponentIntensityShift(ColorComponent.RED, -0.444f, true);
        FakeColorComponentIntensityShift redShift2 =
                new FakeColorComponentIntensityShift(ColorComponent.RED, 0.444f, false);
        FakeColorComponentIntensityShift greenShift1 =
                new FakeColorComponentIntensityShift(ColorComponent.GREEN, 0.555f, true);
        FakeColorComponentIntensityShift greenShift2 =
                new FakeColorComponentIntensityShift(ColorComponent.GREEN, -0.555f, false);
        FakeColorComponentIntensityShift blueShift1 =
                new FakeColorComponentIntensityShift(ColorComponent.BLUE, -0.666f, true);
        FakeColorComponentIntensityShift blueShift2 =
                new FakeColorComponentIntensityShift(ColorComponent.BLUE, 0.666f, false);
        FakeColorRotationShift colorRotationShift1 = new FakeColorRotationShift(0.789f, true);
        FakeColorRotationShift colorRotationShift2 = new FakeColorRotationShift(-0.789f, false);

        NetColorShifts netColorShifts = _colorShiftStackAggregator.aggregate(
                new ArrayList<ColorShift>() {{
                    add(brightnessShift1);
                    add(brightnessShift2);
                    add(redShift1);
                    add(redShift2);
                    add(greenShift1);
                    add(greenShift2);
                    add(blueShift1);
                    add(blueShift2);
                    add(colorRotationShift1);
                    add(colorRotationShift2);
                }}, 0L);

        assertNotNull(netColorShifts);
        assertEquals(NetColorShifts.class.getCanonicalName(),
                netColorShifts.getInterfaceName());
        assertEquals(0.123f, netColorShifts.brightnessShift());
        assertEquals(-0.444f, netColorShifts.redIntensityShift());
        assertEquals(0.555f, netColorShifts.greenIntensityShift());
        assertEquals(-0.666f, netColorShifts.blueIntensityShift());
        assertEquals(0.789f, netColorShifts.colorRotationShift());
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(ColorShiftStackAggregator.class.getCanonicalName(),
                _colorShiftStackAggregator.getInterfaceName());
    }
}
