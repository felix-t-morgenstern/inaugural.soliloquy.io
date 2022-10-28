package inaugural.soliloquy.graphics.renderables.providers;

import soliloquy.specs.common.valueobjects.Vertex;

import java.util.Map;
import java.util.UUID;

public class FiniteLinearMovingVertexProvider
        extends AbstractFiniteLinearMovingProvider<Vertex> {
    public FiniteLinearMovingVertexProvider(UUID uuid,
                                            Map<Long, Vertex> valuesAtTimes,
                                            Long pausedTimestamp, Long mostRecentTimestamp) {
        super(uuid, valuesAtTimes, pausedTimestamp, mostRecentTimestamp, Vertex.of(0f, 0f));
    }

    @Override
    protected Vertex interpolate(Vertex location1, float weight1, Vertex location2, float weight2,
                                 int transitionNumber) {
        return Interpolate.vertices(location1, weight1, location2, weight2);
    }
}
