package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.renderables.providers.StaticProvider;

import java.util.ArrayList;

public class FakeStaticProviderAtTime<T> implements StaticProvider<T> {
    public T ProvidedValue;
    public ArrayList<Long> TimestampInputs = new ArrayList<>();

    public FakeStaticProviderAtTime(T providedValue) {
        ProvidedValue = providedValue;
    }

    // TODO: Ensure in unit tests that Renderers are providing timestamps
    @Override
    public T provide(long timestamp) throws IllegalArgumentException {
        TimestampInputs.add(timestamp);
        return ProvidedValue;
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
    public T getArchetype() {
        return null;
    }
}
