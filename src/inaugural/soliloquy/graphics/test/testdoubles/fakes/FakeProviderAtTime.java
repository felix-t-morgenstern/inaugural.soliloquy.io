package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;

public class FakeProviderAtTime<T> implements ProviderAtTime<T> {
    public FakeProviderAtTime() {

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
    public EntityUuid uuid() {
        return null;
    }
}
