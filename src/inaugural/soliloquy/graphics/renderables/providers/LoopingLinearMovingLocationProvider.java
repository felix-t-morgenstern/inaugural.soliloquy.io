package inaugural.soliloquy.graphics.renderables.providers;

import soliloquy.specs.common.infrastructure.Pair;
import soliloquy.specs.graphics.renderables.providers.LoopingLinearMovingProvider;

import java.util.Map;
import java.util.UUID;

public class LoopingLinearMovingLocationProvider
        extends AbstractLoopingLinearMovingProvider<Pair<Float, Float>>
        implements LoopingLinearMovingProvider<Pair<Float, Float>> {
    private static final Pair<Float, Float> ARCHETYPE = new Pair<>(0f, 0f);

    public LoopingLinearMovingLocationProvider(UUID uuid,
                                               Map<Integer, Pair<Float, Float>> valuesAtTimes,
                                               int periodDuration, int periodModuloOffset,
                                               Long pausedTimestamp, Long mostRecentTimestamp) {
        super(uuid, valuesAtTimes, periodDuration, periodModuloOffset, pausedTimestamp,
                mostRecentTimestamp);
    }

    @Override
    protected Pair<Float, Float> interpolate(Pair<Float, Float> location1, float weight1,
                                             Pair<Float, Float> location2, float weight2,
                                             boolean isClockwise) {
        return new Pair<>(
                Interpolate.floats(location1.getItem1(), weight1, location2.getItem1(), weight2),
                Interpolate.floats(location1.getItem2(), weight1, location2.getItem2(), weight2)
        );
    }

    @Override
    public Pair<Float, Float> getArchetype() {
        return ARCHETYPE;
    }
}
