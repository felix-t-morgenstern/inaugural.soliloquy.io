package inaugural.soliloquy.graphics.renderables.colorshifting;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.renderables.colorshifting.*;

import java.util.List;

public class ColorShiftStackAggregatorImpl implements ColorShiftStackAggregator {
    @Override
    public NetColorShifts aggregate(List<ColorShift> colorShifts, long timestamp)
            throws IllegalArgumentException {
        Check.ifNull(colorShifts, "colorShifts");

        float netBrightnessShift = 0f;
        float netRedShift = 0f;
        float netGreenShift = 0f;
        float netBlueShift = 0f;
        float netColorRotationShift = 0f;

        boolean netBrightnessSealed = false;
        boolean netRedSealed = false;
        boolean netGreenSealed = false;
        boolean netBlueSealed = false;
        boolean netColorRotationSealed = false;

        for(ColorShift colorShift : colorShifts) {
            Check.ifNull(colorShift, "colorShift in colorShifts");
            float value = verifyProvidedValue(colorShift.shiftAmountProvider().provide(timestamp));
            boolean overrides = colorShift.overridesPriorShiftsOfSameType();
            if (colorShift instanceof BrightnessShift) {
                if (!netBrightnessSealed) {
                    netBrightnessShift = getNewValue(netBrightnessShift, value);
                }
                netBrightnessSealed = netBrightnessSealed || overrides;
            }
            if (colorShift instanceof ColorComponentShift) {
                ColorComponentShift colorComponentShift = (ColorComponentShift)colorShift;
                ColorComponent colorComponent = colorComponentShift.colorComponent();
                Check.ifNull(colorComponent, "ColorComponent provided by ColorComponentShift");
                switch(colorComponent) {
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
        float finalNetBrightnessShift = netBrightnessShift;
        float finalNetRedShift = netRedShift;
        float finalNetGreenShift = netGreenShift;
        float finalNetBlueShift = netBlueShift;
        float finalNetColorRotationShift = netColorRotationShift;

        return new NetColorShifts() {
            @Override
            public float netBrightnessShift() {
                return finalNetBrightnessShift;
            }

            @Override
            public float netRedShift() {
                return finalNetRedShift;
            }

            @Override
            public float netGreenShift() {
                return finalNetGreenShift;
            }

            @Override
            public float netBlueShift() {
                return finalNetBlueShift;
            }

            @Override
            public float netColorRotationShift() {
                return finalNetColorRotationShift;
            }

            @Override
            public String getInterfaceName() {
                return NetColorShifts.class.getCanonicalName();
            }
        };
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

    @Override
    public String getInterfaceName() {
        return ColorShiftStackAggregator.class.getCanonicalName();
    }
}
