package inaugural.soliloquy.graphics.test.unit.renderables.colorshifting;

import inaugural.soliloquy.graphics.renderables.colorshifting.ColorShiftStackAggregatorImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeBrightnessShift;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeColorComponentShift;
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
                    add(new FakeColorComponentShift(null, 0f, true));
                }}, 0L
        ));
        assertThrows(IllegalArgumentException.class, () -> _colorShiftStackAggregator.aggregate(
                new ArrayList<ColorShift>() {{
                    add(new FakeColorComponentShift(ColorComponent.RED, null, true));
                }}, 0L
        ));
        assertThrows(IllegalArgumentException.class, () -> _colorShiftStackAggregator.aggregate(
                new ArrayList<ColorShift>() {{
                    add(new FakeColorComponentShift(ColorComponent.RED, belowNegativeOne, true));
                }}, 0L
        ));
        assertThrows(IllegalArgumentException.class, () -> _colorShiftStackAggregator.aggregate(
                new ArrayList<ColorShift>() {{
                    add(new FakeColorComponentShift(ColorComponent.RED, aboveOne, true));
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
        FakeColorComponentShift redShift =
                new FakeColorComponentShift(ColorComponent.RED, 0.444f, false);
        FakeColorComponentShift greenShift =
                new FakeColorComponentShift(ColorComponent.GREEN, 0.555f, false);
        FakeColorComponentShift blueShift =
                new FakeColorComponentShift(ColorComponent.BLUE, 0.666f, false);
        FakeColorRotationShift colorRotationShift = new FakeColorRotationShift(0.789f, false);

        NetColorShifts netBrightnessShift = _colorShiftStackAggregator.aggregate(
                new ArrayList<ColorShift>() {{
                    add(brightnessShift);
                }}, 0L);
        NetColorShifts netRedShift = _colorShiftStackAggregator.aggregate(
                new ArrayList<ColorShift>() {{
                    add(redShift);
                }}, 0L);
        NetColorShifts netGreenShift = _colorShiftStackAggregator.aggregate(
                new ArrayList<ColorShift>() {{
                    add(greenShift);
                }}, 0L);
        NetColorShifts netBlueShift = _colorShiftStackAggregator.aggregate(
                new ArrayList<ColorShift>() {{
                    add(blueShift);
                }}, 0L);
        NetColorShifts netColorRotationShift = _colorShiftStackAggregator.aggregate(
                new ArrayList<ColorShift>() {{
                    add(colorRotationShift);
                }}, 0L);

        assertNotNull(netBrightnessShift);
        assertEquals(NetColorShifts.class.getCanonicalName(),
                netBrightnessShift.getInterfaceName());
        assertEquals(0.123f, netBrightnessShift.netBrightnessShift());
        assertEquals(0f, netBrightnessShift.netRedShift());
        assertEquals(0f, netBrightnessShift.netGreenShift());
        assertEquals(0f, netBrightnessShift.netBlueShift());
        assertEquals(0f, netBrightnessShift.netColorRotationShift());

        assertNotNull(netRedShift);
        assertEquals(NetColorShifts.class.getCanonicalName(),
                netRedShift.getInterfaceName());
        assertEquals(0f, netRedShift.netBrightnessShift());
        assertEquals(0.444f, netRedShift.netRedShift());
        assertEquals(0f, netRedShift.netGreenShift());
        assertEquals(0f, netRedShift.netBlueShift());
        assertEquals(0f, netRedShift.netColorRotationShift());

        assertNotNull(netGreenShift);
        assertEquals(NetColorShifts.class.getCanonicalName(),
                netGreenShift.getInterfaceName());
        assertEquals(0f, netGreenShift.netBrightnessShift());
        assertEquals(0f, netGreenShift.netRedShift());
        assertEquals(0.555f, netGreenShift.netGreenShift());
        assertEquals(0f, netGreenShift.netBlueShift());
        assertEquals(0f, netGreenShift.netColorRotationShift());

        assertNotNull(netBlueShift);
        assertEquals(NetColorShifts.class.getCanonicalName(),
                netBlueShift.getInterfaceName());
        assertEquals(0f, netBlueShift.netBrightnessShift());
        assertEquals(0f, netBlueShift.netRedShift());
        assertEquals(0f, netBlueShift.netGreenShift());
        assertEquals(0.666f, netBlueShift.netBlueShift());
        assertEquals(0f, netBlueShift.netColorRotationShift());

        assertNotNull(netColorRotationShift);
        assertEquals(NetColorShifts.class.getCanonicalName(),
                netColorRotationShift.getInterfaceName());
        assertEquals(0f, netColorRotationShift.netBrightnessShift());
        assertEquals(0f, netColorRotationShift.netRedShift());
        assertEquals(0f, netColorRotationShift.netGreenShift());
        assertEquals(0f, netColorRotationShift.netBlueShift());
        assertEquals(0.789f, netColorRotationShift.netColorRotationShift());
    }

    /** @noinspection RedundantCast*/
    @Test
    void testAggregateMultipleValuesOfEachType() {
        FakeBrightnessShift brightnessShift1 = new FakeBrightnessShift(0.123f, false);
        FakeBrightnessShift brightnessShift2 = new FakeBrightnessShift(-0.123f, false);
        FakeColorComponentShift redShift1 =
                new FakeColorComponentShift(ColorComponent.RED, -0.444f, false);
        FakeColorComponentShift redShift2 =
                new FakeColorComponentShift(ColorComponent.RED, 0.444f, false);
        FakeColorComponentShift greenShift1 =
                new FakeColorComponentShift(ColorComponent.GREEN, 0.555f, false);
        FakeColorComponentShift greenShift2 =
                new FakeColorComponentShift(ColorComponent.GREEN, -0.555f, false);
        FakeColorComponentShift blueShift1 =
                new FakeColorComponentShift(ColorComponent.BLUE, -0.666f, false);
        FakeColorComponentShift blueShift2 =
                new FakeColorComponentShift(ColorComponent.BLUE, 0.666f, false);
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
        assertEquals((float)getExpectedValue.apply(0.123f), netColorShifts.netBrightnessShift());
        assertEquals(-(float)getExpectedValue.apply(0.444f), netColorShifts.netRedShift());
        assertEquals((float)getExpectedValue.apply(0.555f), netColorShifts.netGreenShift());
        assertEquals(-(float)getExpectedValue.apply(0.666f), netColorShifts.netBlueShift());
        assertEquals((float)getExpectedValue.apply(0.789f), netColorShifts.netColorRotationShift());
    }

    @Test
    void testAggregateWithOverridesPriorShiftsOfSameType() {
        FakeBrightnessShift brightnessShift1 = new FakeBrightnessShift(0.123f, true);
        FakeBrightnessShift brightnessShift2 = new FakeBrightnessShift(-0.123f, false);
        FakeColorComponentShift redShift1 =
                new FakeColorComponentShift(ColorComponent.RED, -0.444f, true);
        FakeColorComponentShift redShift2 =
                new FakeColorComponentShift(ColorComponent.RED, 0.444f, false);
        FakeColorComponentShift greenShift1 =
                new FakeColorComponentShift(ColorComponent.GREEN, 0.555f, true);
        FakeColorComponentShift greenShift2 =
                new FakeColorComponentShift(ColorComponent.GREEN, -0.555f, false);
        FakeColorComponentShift blueShift1 =
                new FakeColorComponentShift(ColorComponent.BLUE, -0.666f, true);
        FakeColorComponentShift blueShift2 =
                new FakeColorComponentShift(ColorComponent.BLUE, 0.666f, false);
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
        assertEquals(0.123f, netColorShifts.netBrightnessShift());
        assertEquals(-0.444f, netColorShifts.netRedShift());
        assertEquals(0.555f, netColorShifts.netGreenShift());
        assertEquals(-0.666f, netColorShifts.netBlueShift());
        assertEquals(0.789f, netColorShifts.netColorRotationShift());
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(ColorShiftStackAggregator.class.getCanonicalName(),
                _colorShiftStackAggregator.getInterfaceName());
    }
}
