package inaugural.soliloquy.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.renderables.providers.FiniteLinearMovingColorProvider;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FiniteLinearMovingColorProviderImpl extends AbstractFiniteLinearMovingProvider<Color>
        implements FiniteLinearMovingColorProvider {
    private final List<Boolean> HUE_MOVEMENT_IS_CLOCKWISE;

    /** @noinspection ConstantConditions */
    public FiniteLinearMovingColorProviderImpl(UUID uuid, Map<Long, Color> valuesAtTimes,
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
        return Interpolate.colors(value1, weight1, value2, weight2, isClockwise);
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
        return Color.BLACK;
    }

    @Override
    public String getInterfaceName() {
        return FiniteLinearMovingColorProvider.class.getCanonicalName();
    }
}
