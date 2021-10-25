package inaugural.soliloquy.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.providers.FiniteLinearMovingColorProvider;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FiniteLinearMovingColorProviderImpl extends AbstractFiniteLinearMovingProvider<Color>
        implements FiniteLinearMovingColorProvider  {
    private final List<Boolean> HUE_MOVEMENT_IS_CLOCKWISE;

    /** @noinspection ConstantConditions*/
    public FiniteLinearMovingColorProviderImpl(EntityUuid uuid, Map<Long, Color> valuesAtTimes,
                                               List<Boolean> hueMovementIsClockwise,
                                               Long pausedTimestamp, Long mostRecentTimestamp) {
        super(uuid, valuesAtTimes, pausedTimestamp, mostRecentTimestamp);
        HUE_MOVEMENT_IS_CLOCKWISE = new ArrayList<>();
        Check.ifNull(hueMovementIsClockwise, "hueMovementIsClockwise");
        if (hueMovementIsClockwise.size() != valuesAtTimes.size() - 1) {
            throw new IllegalArgumentException("FiniteLinearMovingColorProviderImpl: " +
                    "hueMovementIsClockwise (size = " + hueMovementIsClockwise.size() + ") must " +
                    "have one fewer item than valuesAtTimes (size = " + valuesAtTimes.size() +
                    ")");
        }
        hueMovementIsClockwise.forEach(isClockwise -> HUE_MOVEMENT_IS_CLOCKWISE.add(
                Check.ifNull(isClockwise, "isClockwise in hueMovementIsClockwise")));
    }

    @Override
    protected Color interpolate(Color value1, float weight1, Color value2, float weight2,
                                boolean isClockwise) {
        float[] value1Hsb =
                Color.RGBtoHSB(value1.getRed(), value1.getGreen(), value1.getBlue(), null);
        float[] value2Hsb =
                Color.RGBtoHSB(value2.getRed(), value2.getGreen(), value2.getBlue(), null);

        float hue;
        float startHue = value1Hsb[0];
        float endHue = value2Hsb[0];
        float distance;
        if (isClockwise) {
            distance = endHue - startHue;
            while (distance < 0f) {
                distance += 1f;
            }
            hue = startHue + (distance * weight2);
        }
        else {
            distance = startHue - endHue;
            if (distance < 0) {
                distance += 1;
            }
            hue = startHue - (distance * weight2);
        }
        if (hue < 0f) {
            hue += 1f;
        }
        else if (hue > 1f) {
            hue -= 1f;
        }

        float saturation = (weight1 * value1Hsb[1]) + (weight2 * value2Hsb[1]);
        float brightness = (weight1 * value1Hsb[2]) + (weight2 * value2Hsb[2]);

        Color rgb = Color.getHSBColor(hue, saturation, brightness);

        int alpha = (int)((weight1 * value1.getAlpha()) + (weight2 * value2.getAlpha()));

        return new Color(rgb.getRed(), rgb.getGreen(), rgb.getBlue(), alpha);
    }

    @Override
    protected boolean isClockwise(int transition) {
        return HUE_MOVEMENT_IS_CLOCKWISE.get(transition);
    }

    @Override
    public List<Boolean> hueMovementIsClockwise() {
        return new ArrayList<>(HUE_MOVEMENT_IS_CLOCKWISE);
    }

    @Override
    public Color getArchetype() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getInterfaceName() {
        return FiniteLinearMovingColorProvider.class.getCanonicalName();
    }
}
