package inaugural.soliloquy.graphics.renderables.providers;

import soliloquy.specs.common.valueobjects.EntityUuid;

import java.util.Map;

public class FiniteLinearMovingFloatProvider extends AbstractFiniteLinearMovingProvider<Float> {
    public FiniteLinearMovingFloatProvider(EntityUuid uuid, Map<Long, Float> valuesAtTimes,
                                           Long pausedTimestamp, Long mostRecentTimestamp) {
        super(uuid, valuesAtTimes, pausedTimestamp, mostRecentTimestamp);
    }

    @Override
    public Float getArchetype() {
        return 0f;
    }

    @Override
    protected Float interpolate(Float value1, float weight1, Float value2, float weight2,
                                boolean isClockwise) {
        return (value1 * weight1) + (value2 * weight2);
    }
}
