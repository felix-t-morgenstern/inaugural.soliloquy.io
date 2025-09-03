package inaugural.soliloquy.io.test.testdoubles.fakes;

import soliloquy.specs.io.graphics.renderables.providers.StaticProvider;

import java.util.List;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.listOf;

public class FakeStaticProvider<T> implements StaticProvider<T> {
    public T ProvidedValue;
    public UUID Uuid;
    public List<Long> TimestampInputs = listOf();

    public FakeStaticProvider(T providedValue) {
        ProvidedValue = providedValue;
    }

    // TODO: Ensure in unit tests that Renderers are providing timestamps
    @Override
    public T provide(long timestamp) throws IllegalArgumentException {
        TimestampInputs.add(timestamp);
        return ProvidedValue;
    }

    @Override
    public Object representation() {
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
    public UUID uuid() {
        return Uuid;
    }
}
