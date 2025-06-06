package inaugural.soliloquy.graphics.renderables.providers;

import soliloquy.specs.graphics.renderables.providers.FiniteSinusoidMovingProvider;

import java.util.List;
import java.util.Map;

public class FiniteSinusoidMovingFloatProvider extends AbstractFiniteSinusoidMovingProvider<Float>
        implements FiniteSinusoidMovingProvider<Float> {
    public FiniteSinusoidMovingFloatProvider(java.util.UUID uuid,
                                             Map<Long, Float> valuesAtTimes,
                                             List<Float> transitionSharpnesses,
                                             Long pausedTimestamp,
                                             Long mostRecentTimestamp) {
        super(uuid, valuesAtTimes, transitionSharpnesses, pausedTimestamp, mostRecentTimestamp);
    }

    @Override
    protected Float interpolateFromSineWeights(Float value1, Float value2,
                                               float value2PercentToAdd) {
        float distanceBetweenValues = value2 - value1;
        return value1 + (distanceBetweenValues * value2PercentToAdd);
    }

    @Override
    protected String className() {
        return "FiniteSinusoidMovingFloatProvider";
    }
}
