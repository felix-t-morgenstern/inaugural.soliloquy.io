package inaugural.soliloquy.graphics.renderables.providers;

import soliloquy.specs.common.infrastructure.Pair;

import java.util.Map;
import java.util.UUID;

public class FiniteLinearMovingLocationProvider
        extends AbstractFiniteLinearMovingProvider<Pair<Float, Float>> {
    private static final Pair<Float, Float> ARCHETYPE = new Pair<>(0f, 0f);

    public FiniteLinearMovingLocationProvider(UUID uuid,
                                              Map<Long, Pair<Float, Float>> valuesAtTimes,
                                              Long pausedTimestamp, Long mostRecentTimestamp) {
        super(uuid, valuesAtTimes, pausedTimestamp, mostRecentTimestamp);
    }

    @Override
    protected Pair<Float, Float> interpolate(Pair<Float, Float> value1, float weight1,
                                             Pair<Float, Float> value2, float weight2,
                                             boolean isClockwise) {
        return new Pair<>(
                Interpolate.floats(value1.getItem1(), weight1, value2.getItem1(), weight2),
                Interpolate.floats(value1.getItem2(), weight1, value2.getItem2(), weight2)
        );
    }

    @Override
    public Pair<Float, Float> getArchetype() {
        return ARCHETYPE;
    }
}
