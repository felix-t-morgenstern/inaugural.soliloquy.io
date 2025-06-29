package inaugural.soliloquy.io.graphics.renderables.providers;

import soliloquy.specs.common.valueobjects.FloatBox;

import java.util.Map;
import java.util.UUID;

public class FiniteLinearMovingFloatBoxProvider
        extends AbstractFiniteLinearMovingProvider<FloatBox> {
    public FiniteLinearMovingFloatBoxProvider(UUID uuid,
                                              Map<Long, FloatBox> valuesAtTimes,
                                              Long pausedTimestamp, Long mostRecentTimestamp) {
        super(uuid, valuesAtTimes, pausedTimestamp, mostRecentTimestamp);
    }

    @Override
    protected FloatBox interpolate(FloatBox value1, float weight1,
                                   FloatBox value2, float weight2,
                                   int transitionNumber) {
        return Interpolate.floatBoxes(value1, weight1, value2, weight2);
    }
}
