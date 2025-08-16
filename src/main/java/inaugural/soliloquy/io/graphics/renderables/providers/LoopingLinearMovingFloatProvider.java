package inaugural.soliloquy.io.graphics.renderables.providers;

import inaugural.soliloquy.tools.timing.TimestampValidator;

import java.util.Map;
import java.util.UUID;

public class LoopingLinearMovingFloatProvider extends AbstractLoopingLinearMovingProvider<Float> {
    public LoopingLinearMovingFloatProvider(UUID uuid,
                                            Map<Integer, Float> valuesAtTimes,
                                            int periodDuration,
                                            int periodModuloOffset,
                                            Long pausedTimestamp,
                                            TimestampValidator timestampValidator) {
        super(uuid, valuesAtTimes, periodDuration, periodModuloOffset, pausedTimestamp,
                timestampValidator);
    }

    @Override
    protected Float interpolate(Float value1, float weight1, Float value2, float weight2,
                                boolean isClockwise) {
        return Interpolate.floats(value1, weight1, value2, weight2);
    }
}
