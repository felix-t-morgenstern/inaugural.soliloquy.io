package inaugural.soliloquy.io.graphics.renderables.providers;

import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.providers.FiniteSinusoidMovingProvider;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;

public class FiniteSinusoidMovingFloatBoxProvider
        extends AbstractFiniteSinusoidMovingProvider<FloatBox>
        implements FiniteSinusoidMovingProvider<FloatBox> {
    public FiniteSinusoidMovingFloatBoxProvider(UUID uuid,
                                                Map<Long, FloatBox> valuesAtTimes,
                                                List<Float> transitionSharpnesses,
                                                Long pausedTimestamp,
                                                Long mostRecentTimestamp) {
        super(uuid, valuesAtTimes, transitionSharpnesses, pausedTimestamp, mostRecentTimestamp);
    }

    @Override
    protected FloatBox interpolateFromSineWeights(FloatBox value1, FloatBox value2,
                                                  float value2PercentToAdd) {
        return floatBoxOf(
                value1.LEFT_X + ((value2.LEFT_X - value1.LEFT_X) * value2PercentToAdd),
                value1.TOP_Y + ((value2.TOP_Y - value1.TOP_Y) * value2PercentToAdd),
                value1.RIGHT_X + ((value2.RIGHT_X - value1.RIGHT_X) * value2PercentToAdd),
                value1.BOTTOM_Y + ((value2.BOTTOM_Y - value1.BOTTOM_Y) * value2PercentToAdd)
        );
    }

    @Override
    protected String className() {
        return "FiniteSinusoidMovingFloatBoxProvider";
    }
}
