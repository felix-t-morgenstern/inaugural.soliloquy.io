package inaugural.soliloquy.io.graphics.renderables.providers.factories;

import inaugural.soliloquy.io.graphics.renderables.providers.StaticProviderImpl;
import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.io.graphics.renderables.providers.StaticProvider;
import soliloquy.specs.io.graphics.renderables.providers.factories.StaticProviderFactory;

import java.util.UUID;

public class StaticProviderFactoryImpl implements StaticProviderFactory {
    private final TimestampValidator TIMESTAMP_VALIDATOR;

    public StaticProviderFactoryImpl(TimestampValidator timestampValidator) {
        TIMESTAMP_VALIDATOR = Check.ifNull(timestampValidator, "timestampValidator");
    }

    @Override
    public <T> StaticProvider<T> make(UUID id, T value)
            throws IllegalArgumentException {
        return new StaticProviderImpl<>(id, value, TIMESTAMP_VALIDATOR);
    }
}
