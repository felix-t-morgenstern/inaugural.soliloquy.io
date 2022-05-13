package inaugural.soliloquy.graphics.renderables.providers;

import soliloquy.specs.common.factories.PairFactory;
import soliloquy.specs.common.infrastructure.Pair;
import soliloquy.specs.common.valueobjects.EntityUuid;

import java.util.Map;

public class FiniteLinearMovingLocationProvider
        extends AbstractFiniteLinearMovingProvider<Pair<Float, Float>> {
    private final PairFactory PAIR_FACTORY;

    public FiniteLinearMovingLocationProvider(EntityUuid uuid,
                                              Map<Long, Pair<Float, Float>> valuesAtTimes,
                                              Long pausedTimestamp, Long mostRecentTimestamp,
                                              PairFactory pairFactory) {
        super(uuid, valuesAtTimes, pausedTimestamp, mostRecentTimestamp);
        PAIR_FACTORY = pairFactory;
    }

    @Override
    protected Pair<Float, Float> interpolate(Pair<Float, Float> value1, float weight1,
                                             Pair<Float, Float> value2, float weight2,
                                             boolean isClockwise) {
        return PAIR_FACTORY.make(
                Interpolate.floats(value1.getItem1(), weight1, value2.getItem1(), weight2),
                Interpolate.floats(value1.getItem2(), weight1, value2.getItem2(), weight2)
        );
    }

    @Override
    public Pair<Float, Float> getArchetype() {
        return ARCHETYPE;
    }

    private static final Pair<Float, Float> ARCHETYPE = new Pair<Float, Float>() {
        @Override
        public Float getItem1() {
            return null;
        }

        @Override
        public Float getItem2() {
            return null;
        }

        @Override
        public void setItem1(Float aFloat) throws IllegalArgumentException {

        }

        @Override
        public void setItem2(Float aFloat) throws IllegalArgumentException {

        }

        @Override
        public Pair<Float, Float> makeClone() {
            return null;
        }

        @Override
        public Float getFirstArchetype() throws IllegalStateException {
            return 0f;
        }

        @Override
        public Float getSecondArchetype() throws IllegalStateException {
            return 0f;
        }

        @Override
        public String getInterfaceName() {
            return Pair.class.getCanonicalName() + "<" + Float.class.getCanonicalName() + "," +
                    Float.class.getCanonicalName() + ">";
        }
    };
}
