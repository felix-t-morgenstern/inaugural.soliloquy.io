package inaugural.soliloquy.io.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import java.util.UUID;

public class StaticProvider<T> implements ProviderAtTime<T> {
    private final UUID UUID;
    private final T VALUE;

    public StaticProvider(UUID uuid, T value) {
        UUID = Check.ifNull(uuid, "uuid");
        VALUE = value;
    }

    @Override
    public T provide(long timestamp) throws IllegalArgumentException {
        return VALUE;
    }

    @Override
    public Object representation() {
        return VALUE;
    }

    @Override
    public void reportPause(long timestamp) throws IllegalArgumentException {
    }

    @Override
    public void reportUnpause(long timestamp) throws IllegalArgumentException {
    }

    @Override
    public Long pausedTimestamp() {
        throw new UnsupportedOperationException();
    }

    @Override
    public UUID uuid() {
        return UUID;
    }
}
