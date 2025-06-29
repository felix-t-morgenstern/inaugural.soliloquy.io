package inaugural.soliloquy.io.graphics.renderables.providers;

import java.util.Map;
import java.util.UUID;

public class FiniteLinearMovingFloatProvider extends AbstractFiniteLinearMovingProvider<Float> {
    public FiniteLinearMovingFloatProvider(UUID uuid, Map<Long, Float> valuesAtTimes,
                                           Long pausedTimestamp, Long mostRecentTimestamp) {
        super(uuid, valuesAtTimes, pausedTimestamp, mostRecentTimestamp);
    }

    @Override
    protected Float interpolate(Float value1, float weight1, Float value2, float weight2,
                                int transitionNumber) {
        return Interpolate.floats(value1, weight1, value2, weight2);
    }
}
