package inaugural.soliloquy.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.generic.HasOneGenericParam;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;

public class StaticProvider<T> extends HasOneGenericParam<T> implements ProviderAtTime<T> {
    private final T VALUE;

    private Long _mostRecentTimestamp;

    public StaticProvider(T value) {
        this(value, value);
    }

    public StaticProvider(T value, T archetype) {
        super(archetype);
        VALUE = value;
    }

    @Override
    protected String getUnparameterizedInterfaceName() {
        return ProviderAtTime.class.getCanonicalName();
    }

    @Override
    public T provide(long timestamp) throws IllegalArgumentException {
        validateTimestamp(timestamp);
        return VALUE;
    }

    @Override
    public void reportPause(long timestamp) throws IllegalArgumentException {
        validateTimestamp(timestamp);
    }

    @Override
    public void reportUnpause(long timestamp) throws IllegalArgumentException {
        validateTimestamp(timestamp);
    }

    private void validateTimestamp(long timestamp) {
        if (_mostRecentTimestamp != null) {
            Check.throwOnLtValue(timestamp, _mostRecentTimestamp, "timestamp");
        }
        _mostRecentTimestamp = timestamp;
    }
}
