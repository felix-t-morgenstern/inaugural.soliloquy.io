package inaugural.soliloquy.io.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.io.graphics.renderables.providers.FiniteLinearMovingColorProvider;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.listOf;

public class FiniteLinearMovingColorProviderImpl extends AbstractFiniteLinearMovingProvider<Color>
        implements FiniteLinearMovingColorProvider {
    private final List<Boolean> HUE_MOVEMENT_IS_CLOCKWISE;

    public FiniteLinearMovingColorProviderImpl(UUID uuid, Map<Long, Color> valuesAtTimes,
                                               List<Boolean> hueMovementIsClockwise,
                                               Long pausedTimestamp, Long mostRecentTimestamp) {
        super(uuid, valuesAtTimes, pausedTimestamp, mostRecentTimestamp);
        HUE_MOVEMENT_IS_CLOCKWISE = listOf();
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
                                int transitionNumber) {
        return Interpolate.colors(value1, weight1, value2, weight2,
                HUE_MOVEMENT_IS_CLOCKWISE.get(transitionNumber));
    }

    @Override
    public List<Boolean> hueMovementIsClockwise() {
        return listOf(HUE_MOVEMENT_IS_CLOCKWISE);
    }

    @Override
    public Object representation() {
        return null;
    }
}
