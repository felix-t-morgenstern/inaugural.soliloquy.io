package inaugural.soliloquy.io.graphics.renderables.colorshifting;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.io.graphics.renderables.colorshifting.*;

import java.util.List;

import static soliloquy.specs.io.graphics.renderables.colorshifting.NetColorShifts.netShifts;

public class ColorShiftStackAggregatorImpl implements ColorShiftStackAggregator {
    @Override
    public NetColorShifts aggregate(List<ColorShift> colorShifts, long timestamp)
            throws IllegalArgumentException {
        Check.ifNull(colorShifts, "colorShifts");

        var netBrightnessShift = 0f;
        var netRedShift = 0f;
        var netGreenShift = 0f;
        var netBlueShift = 0f;
        var netColorRotationShift = 0f;

        var netBrightnessSealed = false;
        var netRedSealed = false;
        var netGreenSealed = false;
        var netBlueSealed = false;
        var netColorRotationSealed = false;

        for (var colorShift : colorShifts) {
            Check.ifNull(colorShift, "colorShift in colorShifts");
            var value = verifyProvidedValue(colorShift.shiftAmountProvider().provide(timestamp));
            var overrides = colorShift.overridesPriorShiftsOfSameType();
            if (colorShift instanceof BrightnessShift) {
                if (!netBrightnessSealed) {
                    netBrightnessShift = getNewValue(netBrightnessShift, value);
                }
                netBrightnessSealed = netBrightnessSealed || overrides;
            }
            if (colorShift instanceof ColorComponentIntensityShift intensityShift) {
                var colorComponent = intensityShift.colorComponent();
                Check.ifNull(colorComponent, "ColorComponent provided by intensityShift");
                switch (colorComponent) {
                    case RED:
                        if (!netRedSealed) {
                            netRedShift = getNewValue(netRedShift, value);
                        }
                        netRedSealed = netRedSealed || overrides;
                        break;
                    case GREEN:
                        if (!netGreenSealed) {
                            netGreenShift = getNewValue(netGreenShift, value);
                        }
                        netGreenSealed = netGreenSealed || overrides;
                        break;
                    case BLUE:
                        if (!netBlueSealed) {
                            netBlueShift = getNewValue(netBlueShift, value);
                        }
                        netBlueSealed = netBlueSealed || overrides;
                        break;
                }
            }
            if (colorShift instanceof ColorRotationShift) {
                if (!netColorRotationSealed) {
                    netColorRotationShift = getNewValue(netColorRotationShift, value);
                }
                netColorRotationSealed = netColorRotationSealed || overrides;
            }

            if (netBrightnessSealed && netRedSealed && netGreenSealed && netBlueSealed &&
                    netColorRotationSealed) {
                break;
            }
        }

        // NB: These variables exist to facilitate use of final or effectively final values for
        //     the NetColorShift values
        var finalBrightnessShift = netBrightnessShift;
        var finalRedShift = netRedShift;
        var finalGreenShift = netGreenShift;
        var finalBlueShift = netBlueShift;
        var finalColorRotationShift = netColorRotationShift;

        return netShifts(finalBrightnessShift, finalRedShift, finalGreenShift, finalBlueShift,
                finalColorRotationShift);
    }

    private float verifyProvidedValue(Float value) {
        Check.ifNull(value, "value provided by colorShift");
        if (value < -1f) {
            throw new IllegalArgumentException(
                    "ColorShiftStackAggregatorImpl: provided value cannot be less than -1");
        }
        if (value > 1f) {
            throw new IllegalArgumentException(
                    "ColorShiftStackAggregatorImpl: provided value cannot be greater than 1");
        }
        return value;
    }

    private float getNewValue(float previousValue, float newModifier) {
        if (newModifier == 0) {
            return previousValue;
        }
        else if (newModifier > 0) {
            return previousValue + ((1f - previousValue) * newModifier);
        }
        else { // newModifier < 0
            return previousValue + ((1f + previousValue) * newModifier);
        }
    }
}
