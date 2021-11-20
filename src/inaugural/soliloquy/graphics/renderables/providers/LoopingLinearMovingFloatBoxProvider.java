package inaugural.soliloquy.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.providers.LoopingMovingProvider;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.factories.FloatBoxFactory;

import java.util.Map;

public class LoopingLinearMovingFloatBoxProvider
        extends AbstractLoopingLinearMovingProvider<FloatBox>
        implements LoopingMovingProvider<FloatBox> {
    private final FloatBoxFactory FLOAT_BOX_FACTORY;

    private static final FloatBox ARCHETYPE = new FloatBoxArchetype();

    public LoopingLinearMovingFloatBoxProvider(EntityUuid uuid, Map<Long, FloatBox> valuesAtTimes,
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
        return FLOAT_BOX_FACTORY.make(
                (value1.leftX() * weight1) + (value2.leftX() * weight2),
                (value1.topY() * weight1) + (value2.topY() * weight2),
                (value1.rightX() * weight1) + (value2.rightX() * weight2),
                (value1.bottomY() * weight1) + (value2.bottomY() * weight2));
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
