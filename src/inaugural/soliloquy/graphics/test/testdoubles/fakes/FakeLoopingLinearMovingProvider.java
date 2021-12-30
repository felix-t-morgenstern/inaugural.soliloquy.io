package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.providers.LoopingMovingProvider;

import java.util.Map;

public class FakeLoopingLinearMovingProvider<T> implements LoopingMovingProvider<T> {
    @Override
    public Map<Integer, T> valuesWithinPeriod() {
        return null;
    }

    @Override
    public int periodDuration() {
        return 0;
    }

    @Override
    public int periodModuloOffset() {
        return 0;
    }

    @Override
    public void reset(long l) throws IllegalArgumentException {

    }

    @Override
    public T provide(long l) throws IllegalArgumentException {
        return null;
    }

    @Override
    public T getArchetype() {
        return null;
    }

    @Override
    public EntityUuid uuid() {
        return null;
    }

    @Override
    public void reportPause(long l) throws IllegalArgumentException {

    }

    @Override
    public void reportUnpause(long l) throws IllegalArgumentException {

    }

    @Override
    public Long pausedTimestamp() {
        return null;
    }

    @Override
    public Long mostRecentTimestamp() {
        return null;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
