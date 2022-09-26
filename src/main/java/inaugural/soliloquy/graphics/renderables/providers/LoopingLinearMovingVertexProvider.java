package inaugural.soliloquy.graphics.renderables.providers;

import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.renderables.providers.LoopingLinearMovingProvider;

import java.util.Map;
import java.util.UUID;

public class LoopingLinearMovingVertexProvider
        extends AbstractLoopingLinearMovingProvider<Vertex>
        implements LoopingLinearMovingProvider<Vertex> {
    private static final Vertex ARCHETYPE = Vertex.of(0f, 0f);

    public LoopingLinearMovingVertexProvider(UUID uuid,
                                             Map<Integer, Vertex> valuesAtTimes,
                                             int periodDuration, int periodModuloOffset,
                                             Long pausedTimestamp, Long mostRecentTimestamp) {
        super(uuid, valuesAtTimes, periodDuration, periodModuloOffset, pausedTimestamp,
                mostRecentTimestamp);
    }

    @Override
    protected Vertex interpolate(Vertex location1, float weight1,
                                             Vertex location2, float weight2,
                                             boolean isClockwise) {
        return Vertex.of(
                Interpolate.floats(location1.x, weight1, location2.x, weight2),
                Interpolate.floats(location1.y, weight1, location2.y, weight2)
        );
    }

    @Override
    public Vertex getArchetype() {
        return ARCHETYPE;
    }
}
