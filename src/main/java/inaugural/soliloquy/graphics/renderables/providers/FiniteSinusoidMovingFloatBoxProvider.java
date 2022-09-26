package inaugural.soliloquy.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.renderables.providers.FiniteSinusoidMovingProvider;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.factories.FloatBoxFactory;

import java.util.List;
import java.util.Map;

public class FiniteSinusoidMovingFloatBoxProvider
        extends AbstractFiniteSinusoidMovingProvider<FloatBox>
        implements FiniteSinusoidMovingProvider<FloatBox> {
    private final FloatBoxFactory FLOAT_BOX_FACTORY;

    private static final FloatBox ARCHETYPE = new FloatBoxArchetype();

    public FiniteSinusoidMovingFloatBoxProvider(java.util.UUID uuid,
                                                Map<Long, FloatBox> valuesAtTimes,
                                                List<Float> transitionSharpnesses,
                                                Long pausedTimestamp,
                                                Long mostRecentTimestamp,
                                                FloatBoxFactory floatBoxFactory) {
        super(uuid, valuesAtTimes, transitionSharpnesses, pausedTimestamp, mostRecentTimestamp);
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

    @Override
    public FloatBox getArchetype() {
        return ARCHETYPE;
    }

    private static class FloatBoxArchetype implements FloatBox {
        @Override
        public float leftX() {
            return 0;
        }

        @Override
        public float topY() {
            return 0;
        }

        @Override
        public float rightX() {
            return 0;
        }

        @Override
        public float bottomY() {
            return 0;
        }

        @Override
        public float width() {
            return 0;
        }

        @Override
        public float height() {
            return 0;
        }

        @Override
        public FloatBox intersection(FloatBox floatBox) throws IllegalArgumentException {
            return null;
        }

        @Override
        public FloatBox translate(float v, float v1) {
            return null;
        }

        @Override
        public String getInterfaceName() {
            return FloatBox.class.getCanonicalName();
        }
    }
}
