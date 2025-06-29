package inaugural.soliloquy.io.test.testdoubles.fakes;

import soliloquy.specs.io.graphics.renderables.providers.LoopingLinearMovingProvider;

import java.util.Map;
import java.util.UUID;

public class FakeLoopingLinearMovingProvider<T> implements LoopingLinearMovingProvider<T> {
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
    public Object representation() {
        return null;
    }

    @Override
    public UUID uuid() {
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
}
