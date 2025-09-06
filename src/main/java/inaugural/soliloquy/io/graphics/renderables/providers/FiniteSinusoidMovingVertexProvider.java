package inaugural.soliloquy.io.graphics.renderables.providers;

import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.providers.FiniteSinusoidMovingProvider;

import java.util.Map;
import java.util.UUID;

import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

public class FiniteSinusoidMovingVertexProvider
        extends AbstractFiniteSinusoidMovingProvider<Vertex>
        implements FiniteSinusoidMovingProvider<Vertex> {
    public FiniteSinusoidMovingVertexProvider(UUID uuid,
                                              Map<Long, Vertex> valuesAtTimes,
                                              float[] transitionSharpnesses,
                                              Long pausedTimestamp,
                                              TimestampValidator timestampValidator) {
        super(uuid, valuesAtTimes, transitionSharpnesses, pausedTimestamp, timestampValidator);
    }

    @Override
    protected Vertex interpolateFromSineWeights(Vertex value1, Vertex value2,
                                                float value2PercentToAdd) {
        return vertexOf(
                value1.X + (value2.X * value2PercentToAdd),
                value1.Y + (value2.Y * value2PercentToAdd)
        );
    }

    @Override
    protected String className() {
        return this.getClass().getSimpleName();
    }
}
