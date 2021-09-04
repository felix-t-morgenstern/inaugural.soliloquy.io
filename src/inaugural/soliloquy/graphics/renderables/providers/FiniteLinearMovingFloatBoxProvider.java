package inaugural.soliloquy.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.factories.FloatBoxFactory;

import java.util.Map;

public class FiniteLinearMovingFloatBoxProvider
        extends AbstractFiniteLinearMovingProvider<FloatBox> {
    private final FloatBoxFactory FLOAT_BOX_FACTORY;

    private static final FloatBox ARCHETYPE = new FloatBoxArchetype();

    public FiniteLinearMovingFloatBoxProvider(FloatBoxFactory floatBoxFactory, EntityUuid uuid,
                                              Map<Long, FloatBox> valuesAtTimes,
                                              Long pausedTimestamp, Long mostRecentTimestamp) {
        super(uuid, valuesAtTimes, pausedTimestamp, mostRecentTimestamp);
        FLOAT_BOX_FACTORY = Check.ifNull(floatBoxFactory, "floatBoxFactory");
    }

    @Override
    protected FloatBox interpolate(FloatBox value1, float weight1,
                                   FloatBox value2, float weight2) {
        float value1WeightedLeftX = value1.leftX() * weight1;
        float value1WeightedTopY = value1.topY() * weight1;
        float value1WeightedRightX = value1.rightX() * weight1;
        float value1WeightedBottomY = value1.bottomY() * weight1;

        float value2WeightedLeftX = value2.leftX() * weight2;
        float value2WeightedTopY = value2.topY() * weight2;
        float value2WeightedRightX = value2.rightX() * weight2;
        float value2WeightedBottomY = value2.bottomY() * weight2;

        return FLOAT_BOX_FACTORY.make(
                value1WeightedLeftX + value2WeightedLeftX,
                value1WeightedTopY + value2WeightedTopY,
                value1WeightedRightX + value2WeightedRightX,
                value1WeightedBottomY + value2WeightedBottomY
        );
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
