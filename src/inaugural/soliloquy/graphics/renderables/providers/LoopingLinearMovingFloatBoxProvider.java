package inaugural.soliloquy.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.renderables.providers.LoopingLinearMovingProvider;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.factories.FloatBoxFactory;

import java.util.Map;
import java.util.UUID;

public class LoopingLinearMovingFloatBoxProvider
        extends AbstractLoopingLinearMovingProvider<FloatBox>
        implements LoopingLinearMovingProvider<FloatBox> {
    private final FloatBoxFactory FLOAT_BOX_FACTORY;

    private static final FloatBox ARCHETYPE = new FloatBoxArchetype();

    @SuppressWarnings("ConstantConditions")
    public LoopingLinearMovingFloatBoxProvider(UUID uuid,
                                               Map<Integer, FloatBox> valuesAtTimes,
                                               int periodDuration, int periodModuloOffset,
                                               Long pausedTimestamp, Long mostRecentTimestamp,
                                               FloatBoxFactory floatBoxFactory) {
        super(uuid, valuesAtTimes, periodDuration, periodModuloOffset, pausedTimestamp,
                mostRecentTimestamp);
        FLOAT_BOX_FACTORY = Check.ifNull(floatBoxFactory, "floatBoxFactory");
    }

    @Override
    protected FloatBox interpolate(FloatBox value1, float weight1, FloatBox value2, float weight2,
                                   boolean isClockwise) {
        return Interpolate.floatBoxes(value1, weight1, value2, weight2, FLOAT_BOX_FACTORY);
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
