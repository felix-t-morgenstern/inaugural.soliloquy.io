package inaugural.soliloquy.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.renderables.providers.LoopingLinearMovingProvider;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.factories.FloatBoxFactory;

import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.generic.Archetypes.generateSimpleArchetype;

public class LoopingLinearMovingFloatBoxProvider
        extends AbstractLoopingLinearMovingProvider<FloatBox>
        implements LoopingLinearMovingProvider<FloatBox> {
    private final FloatBoxFactory FLOAT_BOX_FACTORY;

    public LoopingLinearMovingFloatBoxProvider(UUID uuid,
                                               Map<Integer, FloatBox> valuesAtTimes,
                                               int periodDuration, int periodModuloOffset,
                                               Long pausedTimestamp, Long mostRecentTimestamp,
                                               FloatBoxFactory floatBoxFactory) {
        super(uuid, valuesAtTimes, periodDuration, periodModuloOffset, pausedTimestamp,
                mostRecentTimestamp, generateSimpleArchetype(FloatBox.class));
        FLOAT_BOX_FACTORY = Check.ifNull(floatBoxFactory, "floatBoxFactory");
    }

    @Override
    protected FloatBox interpolate(FloatBox value1, float weight1, FloatBox value2, float weight2,
                                   boolean isClockwise) {
        return Interpolate.floatBoxes(value1, weight1, value2, weight2, FLOAT_BOX_FACTORY);
    }
}
