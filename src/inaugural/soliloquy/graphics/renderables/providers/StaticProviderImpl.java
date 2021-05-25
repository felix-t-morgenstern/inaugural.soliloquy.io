package inaugural.soliloquy.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.generic.HasOneGenericParam;
import soliloquy.specs.graphics.renderables.providers.StaticProvider;

public class StaticProviderImpl<T> extends HasOneGenericParam<T> implements StaticProvider<T> {
    private final T VALUE;

    private Long _mostRecentTimestamp;

    public StaticProviderImpl(T value) {
        this(value, value);
    }

    public StaticProviderImpl(T value, T archetype) {
        super(archetype);
        VALUE = value;
    }

    @Override
    protected String getUnparameterizedInterfaceName() {
        return StaticProvider.class.getCanonicalName();
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
