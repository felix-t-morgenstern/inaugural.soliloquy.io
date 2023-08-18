package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;

import java.util.UUID;

public class FakeProviderAtTime<T> implements ProviderAtTime<T> {
    public FakeProviderAtTime() {

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
    public T archetype() {
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
    public String getInterfaceName() {
        return null;
    }

    @Override
    public UUID uuid() {
        return null;
    }

    @Override
    public Long mostRecentTimestamp() {
        return null;
    }
}
