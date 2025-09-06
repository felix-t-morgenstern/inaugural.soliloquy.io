package inaugural.soliloquy.io.graphics.renderables.providers;

import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.io.graphics.renderables.providers.FiniteSinusoidMovingProvider;

import java.util.Map;
import java.util.UUID;

public class FiniteSinusoidMovingFloatProvider extends AbstractFiniteSinusoidMovingProvider<Float>
        implements FiniteSinusoidMovingProvider<Float> {
    public FiniteSinusoidMovingFloatProvider(UUID uuid,
                                             Map<Long, Float> valuesAtTimes,
                                             float[] transitionSharpnesses,
                                             Long pausedTimestamp,
                                             TimestampValidator timestampValidator) {
        super(uuid, valuesAtTimes, transitionSharpnesses, pausedTimestamp, timestampValidator);
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
