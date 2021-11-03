package inaugural.soliloquy.graphics.renderables.providers;

import soliloquy.specs.common.valueobjects.EntityUuid;

import java.util.HashMap;
import java.util.Map;

public class LoopingLinearMovingFloatProvider extends AbstractLoopingLinearMovingProvider<Float> {
    public LoopingLinearMovingFloatProvider(EntityUuid uuid, Map<Long, Float> valuesAtTimes,
                                               int periodDuration, int periodModuloOffset,
                                               Long pausedTimestamp, Long mostRecentTimestamp) {
        super(uuid, valuesAtTimes, periodDuration, periodModuloOffset, pausedTimestamp,
                mostRecentTimestamp);
    }

    @Override
    protected Float interpolate(Float value1, float weight1, Float value2, float weight2,
                                boolean isClockwise) {
        return Interpolate.floats(value1, weight1, value2, weight2);
    }

    @Override
    public boolean movementIsLinear() {
        return true;
    }

    @Override
    public Map<Integer, Float> valuesWithinPeriod() {
        return new HashMap<>(VALUES_AT_TIMES);
    }

    @Override
    public Float getArchetype() {
        return 0f;
    }

    @Override
    public int periodDuration() {
        return PERIOD_DURATION;
    }
}
