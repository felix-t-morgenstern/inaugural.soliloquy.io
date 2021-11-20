package inaugural.soliloquy.graphics.renderables.providers;

import soliloquy.specs.common.valueobjects.EntityUuid;

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
    public Float getArchetype() {
        return 0f;
    }
}
