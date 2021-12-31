package inaugural.soliloquy.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.providers.LoopingMovingColorProvider;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoopingMovingColorProviderImpl extends AbstractLoopingLinearMovingProvider<Color>
        implements LoopingMovingColorProvider {
    private final List<Boolean> HUE_MOVEMENT_IS_CLOCKWISE;

    @SuppressWarnings("ConstantConditions")
    public LoopingMovingColorProviderImpl(EntityUuid uuid, Map<Integer, Color> valuesWithinPeriod,
                                          List<Boolean> hueMovementIsClockwise,
                                          int periodDuration, int periodModuloOffset,
                                          Long pausedTimestamp, Long mostRecentTimestamp) {
        super(uuid, valuesWithinPeriod, periodDuration, periodModuloOffset, pausedTimestamp,
                mostRecentTimestamp);

        HUE_MOVEMENT_IS_CLOCKWISE = new ArrayList<>();
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
        return new ArrayList<>(HUE_MOVEMENT_IS_CLOCKWISE);
    }

    @Override
    public Color getArchetype() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getInterfaceName() {
        return LoopingMovingColorProvider.class.getCanonicalName();
    }
}
