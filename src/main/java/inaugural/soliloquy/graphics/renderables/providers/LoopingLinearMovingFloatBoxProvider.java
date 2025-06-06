package inaugural.soliloquy.graphics.renderables.providers;

import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.graphics.renderables.providers.LoopingLinearMovingProvider;

import java.util.Map;
import java.util.UUID;

public class LoopingLinearMovingFloatBoxProvider
        extends AbstractLoopingLinearMovingProvider<FloatBox>
        implements LoopingLinearMovingProvider<FloatBox> {
    public LoopingLinearMovingFloatBoxProvider(UUID uuid,
                                               Map<Integer, FloatBox> valuesAtTimes,
                                               int periodDuration, int periodModuloOffset,
                                               Long pausedTimestamp, Long mostRecentTimestamp) {
        super(uuid, valuesAtTimes, periodDuration, periodModuloOffset, pausedTimestamp,
                mostRecentTimestamp);
    }

    @Override
    protected FloatBox interpolate(FloatBox value1, float weight1, FloatBox value2, float weight2,
                                   boolean isClockwise) {
        return Interpolate.floatBoxes(value1, weight1, value2, weight2);
    }
}
