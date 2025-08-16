package inaugural.soliloquy.io.graphics.renderables.providers;

import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.providers.LoopingLinearMovingProvider;

import java.util.Map;
import java.util.UUID;

public class LoopingLinearMovingFloatBoxProvider
        extends AbstractLoopingLinearMovingProvider<FloatBox>
        implements LoopingLinearMovingProvider<FloatBox> {
    public LoopingLinearMovingFloatBoxProvider(UUID uuid,
                                               Map<Integer, FloatBox> valuesAtTimes,
                                               int periodDuration, int periodModuloOffset,
                                               Long pausedTimestamp,
                                               TimestampValidator timestampValidator) {
        super(uuid, valuesAtTimes, periodDuration, periodModuloOffset, pausedTimestamp,
                timestampValidator);
    }

    @Override
    protected FloatBox interpolate(FloatBox value1, float weight1, FloatBox value2, float weight2,
                                   boolean isClockwise) {
        return Interpolate.floatBoxes(value1, weight1, value2, weight2);
    }
}
