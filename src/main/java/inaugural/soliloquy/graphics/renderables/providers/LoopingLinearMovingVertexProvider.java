package inaugural.soliloquy.graphics.renderables.providers;

import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.renderables.providers.LoopingLinearMovingProvider;

import java.util.Map;
import java.util.UUID;

public class LoopingLinearMovingVertexProvider
        extends AbstractLoopingLinearMovingProvider<Vertex>
        implements LoopingLinearMovingProvider<Vertex> {
    public LoopingLinearMovingVertexProvider(UUID uuid,
                                             Map<Integer, Vertex> valuesAtTimes,
                                             int periodDuration, int periodModuloOffset,
                                             Long pausedTimestamp, Long mostRecentTimestamp) {
        super(uuid, valuesAtTimes, periodDuration, periodModuloOffset, pausedTimestamp,
                mostRecentTimestamp);
    }

    @Override
    protected Vertex interpolate(Vertex location1, float weight1, Vertex location2, float weight2,
                                 boolean isClockwise) {
        return Interpolate.vertices(location1, weight1, location2, weight2);
    }
}
