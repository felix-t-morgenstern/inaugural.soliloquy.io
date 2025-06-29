package inaugural.soliloquy.io.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.io.graphics.renderables.providers.LoopingLinearMovingColorProvider;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.listOf;

public class LoopingLinearMovingColorProviderImpl
        extends AbstractLoopingLinearMovingProvider<Color>
        implements LoopingLinearMovingColorProvider {
    private final List<Boolean> HUE_MOVEMENT_IS_CLOCKWISE;

    public LoopingLinearMovingColorProviderImpl(UUID uuid,
                                                Map<Integer, Color> valuesWithinPeriod,
                                                List<Boolean> hueMovementIsClockwise,
                                                int periodDuration, int periodModuloOffset,
                                                Long pausedTimestamp, Long mostRecentTimestamp) {
        super(uuid, valuesWithinPeriod, periodDuration, periodModuloOffset, pausedTimestamp,
                mostRecentTimestamp);

        HUE_MOVEMENT_IS_CLOCKWISE = listOf();
        Check.ifNull(hueMovementIsClockwise, "hueMovementIsClockwise");
        if (hueMovementIsClockwise.size() != valuesWithinPeriod.size()) {
            throw new IllegalArgumentException("FiniteLinearMovingColorProviderImpl: " +
                    "hueMovementIsClockwise (size = " + hueMovementIsClockwise.size() + ") must " +
                    "have one fewer item than valuesWithinPeriod (size = " +
                    valuesWithinPeriod.size() + ")");
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
        return listOf(HUE_MOVEMENT_IS_CLOCKWISE);
    }
}
