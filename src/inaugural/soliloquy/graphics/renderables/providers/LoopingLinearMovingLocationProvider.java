package inaugural.soliloquy.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.factories.PairFactory;
import soliloquy.specs.common.infrastructure.Pair;
import soliloquy.specs.graphics.renderables.providers.LoopingLinearMovingProvider;

import java.util.Map;
import java.util.UUID;

public class LoopingLinearMovingLocationProvider
        extends AbstractLoopingLinearMovingProvider<Pair<Float, Float>>
        implements LoopingLinearMovingProvider<Pair<Float, Float>> {
    private final PairFactory PAIR_FACTORY;

    private static final Pair<Float, Float> ARCHETYPE = new LocationArchetype();

    public LoopingLinearMovingLocationProvider(UUID uuid,
                                               Map<Integer, Pair<Float, Float>> valuesAtTimes,
                                               int periodDuration, int periodModuloOffset,
                                               Long pausedTimestamp, Long mostRecentTimestamp,
                                               PairFactory pairFactory) {
        super(uuid, valuesAtTimes, periodDuration, periodModuloOffset, pausedTimestamp,
                mostRecentTimestamp);
        PAIR_FACTORY = Check.ifNull(pairFactory, "pairFactory");
    }

    @Override
    protected Pair<Float, Float> interpolate(Pair<Float, Float> location1, float weight1,
                                             Pair<Float, Float> location2, float weight2,
                                             boolean isClockwise) {
        return PAIR_FACTORY.make(
                Interpolate.floats(location1.getItem1(), weight1, location2.getItem1(), weight2),
                Interpolate.floats(location1.getItem2(), weight1, location2.getItem2(), weight2)
        );
    }

    @Override
    public Pair<Float, Float> getArchetype() {
        return ARCHETYPE;
    }

    private static class LocationArchetype implements Pair<Float, Float> {
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
    }
}
