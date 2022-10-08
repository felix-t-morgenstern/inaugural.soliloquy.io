package inaugural.soliloquy.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.renderables.providers.FiniteSinusoidMovingProvider;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.factories.FloatBoxFactory;

import java.util.List;
import java.util.Map;

import static inaugural.soliloquy.tools.generic.Archetypes.generateSimpleArchetype;

public class FiniteSinusoidMovingFloatBoxProvider
        extends AbstractFiniteSinusoidMovingProvider<FloatBox>
        implements FiniteSinusoidMovingProvider<FloatBox> {
    private final FloatBoxFactory FLOAT_BOX_FACTORY;

    public FiniteSinusoidMovingFloatBoxProvider(java.util.UUID uuid,
                                                Map<Long, FloatBox> valuesAtTimes,
                                                List<Float> transitionSharpnesses,
                                                Long pausedTimestamp,
                                                Long mostRecentTimestamp,
                                                FloatBoxFactory floatBoxFactory) {
        super(uuid, valuesAtTimes, transitionSharpnesses, pausedTimestamp, mostRecentTimestamp,
                generateSimpleArchetype(FloatBox.class));
        FLOAT_BOX_FACTORY = Check.ifNull(floatBoxFactory, "floatBoxFactory");
    }

    @Override
    protected FloatBox interpolateFromSineWeights(FloatBox value1, FloatBox value2,
                                                  float value2PercentToAdd) {
        return FLOAT_BOX_FACTORY.make(
                value1.leftX() + ((value2.leftX() - value1.leftX()) * value2PercentToAdd),
                value1.topY() + ((value2.topY() - value1.topY()) * value2PercentToAdd),
                value1.rightX() + ((value2.rightX() - value1.rightX()) * value2PercentToAdd),
                value1.bottomY() + ((value2.bottomY() - value1.bottomY()) * value2PercentToAdd)
        );
    }

    @Override
    protected String className() {
        return "FiniteSinusoidMovingFloatBoxProvider";
    }
}
