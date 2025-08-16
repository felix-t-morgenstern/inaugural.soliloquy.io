package inaugural.soliloquy.io.graphics.renderables.providers.factories;

import inaugural.soliloquy.io.graphics.renderables.providers.ProgressiveStringProvider;
import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.renderables.providers.factories.ProgressiveStringProviderFactory;

import java.util.UUID;

public class ProgressiveStringProviderFactoryImpl implements ProgressiveStringProviderFactory {
    private final TimestampValidator TIMESTAMP_VALIDATOR;

    public ProgressiveStringProviderFactoryImpl(TimestampValidator timestampValidator) {
        TIMESTAMP_VALIDATOR = Check.ifNull(timestampValidator, "timestampValidator");
    }

    @Override
    public ProviderAtTime<String> make(UUID uuid, String string, long startTimestamp,
                                       long timeToComplete, Long pausedTimestamp) throws IllegalArgumentException {
        return new ProgressiveStringProvider(uuid, string, startTimestamp, timeToComplete,
                pausedTimestamp, TIMESTAMP_VALIDATOR);
    }
}
