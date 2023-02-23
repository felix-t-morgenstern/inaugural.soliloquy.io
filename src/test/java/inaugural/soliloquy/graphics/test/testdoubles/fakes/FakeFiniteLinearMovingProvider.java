package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.renderables.providers.FiniteLinearMovingProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FakeFiniteLinearMovingProvider<T> implements FiniteLinearMovingProvider<T> {
    public UUID Uuid;
    public Map<Long, T> ValuesAtTimestamps;
    public Long PausedTimestamp;
    public Long MostRecentTimestamp;
    public T Archetype;

    public FakeFiniteLinearMovingProvider() {

    }

    @Override
    public Map<Long, T> valuesAtTimestampsRepresentation() {
        return new HashMap<>(ValuesAtTimestamps);
    }

    @Override
    public T provide(long l) throws IllegalArgumentException {
        return null;
    }

    @Override
    public Object representation() {
        return null;
    }

    @Override
    public T getArchetype() {
        return Archetype;
    }

    @Override
    public UUID uuid() {
        return Uuid;
    }

    @Override
    public void reportPause(long l) throws IllegalArgumentException {

    }

    @Override
    public void reportUnpause(long l) throws IllegalArgumentException {

    }

    @Override
    public Long pausedTimestamp() {
        return PausedTimestamp;
    }

    @Override
    public Long mostRecentTimestamp() {
        return MostRecentTimestamp;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
