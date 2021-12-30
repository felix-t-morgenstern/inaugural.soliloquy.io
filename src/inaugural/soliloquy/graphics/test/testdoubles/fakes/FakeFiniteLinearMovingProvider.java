package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.providers.FiniteLinearMovingProvider;

import java.util.HashMap;
import java.util.Map;

public class FakeFiniteLinearMovingProvider<T> implements FiniteLinearMovingProvider<T> {
    public EntityUuid Uuid;
    public Map<Long, T> ValuesAtTimestamps;
    public Long PausedTimestamp;
    public Long MostRecentTimestamp;
    public T Archetype;

    public FakeFiniteLinearMovingProvider() {

    }

    public FakeFiniteLinearMovingProvider(EntityUuid uuid, Map<Long, T> valuesAtTimestamps,
                                          Long pausedTimestamp, Long mostRecentTimestamp,
                                          T archetype) {
        Uuid = uuid;
        ValuesAtTimestamps = valuesAtTimestamps;
        PausedTimestamp = pausedTimestamp;
        MostRecentTimestamp = mostRecentTimestamp;
        Archetype = archetype;
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
    public T getArchetype() {
        return Archetype;
    }

    @Override
    public EntityUuid uuid() {
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
