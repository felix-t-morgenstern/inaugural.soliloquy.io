package inaugural.soliloquy.graphics.renderables.providers;

import soliloquy.specs.common.valueobjects.Vertex;

import java.util.Map;
import java.util.UUID;

public class FiniteLinearMovingVertexProvider
        extends AbstractFiniteLinearMovingProvider<Vertex> {
    private static final Vertex ARCHETYPE = Vertex.of(0f, 0f);

    public FiniteLinearMovingVertexProvider(UUID uuid,
                                            Map<Long, Vertex> valuesAtTimes,
                                            Long pausedTimestamp, Long mostRecentTimestamp) {
        super(uuid, valuesAtTimes, pausedTimestamp, mostRecentTimestamp);
    }

    @Override
    protected Vertex interpolate(Vertex value1, float weight1, Vertex value2, float weight2,
                                 int transitionNumber) {
        return Vertex.of(
                Interpolate.floats(value1.x, weight1, value2.x, weight2),
                Interpolate.floats(value1.y, weight1, value2.y, weight2)
        );
    }

    @Override
    public Vertex getArchetype() {
        return ARCHETYPE;
    }
}
