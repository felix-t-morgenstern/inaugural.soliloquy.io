package inaugural.soliloquy.io.graphics.renderables.providers.factories;

import inaugural.soliloquy.io.graphics.renderables.providers.ValuesAtTimestampType;
import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.io.graphics.renderables.providers.FiniteSinusoidMovingProvider;
import soliloquy.specs.io.graphics.renderables.providers.factories.FiniteSinusoidMovingProviderFactory;

import java.util.*;
import java.util.function.Function;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;

public class FiniteSinusoidMovingProviderFactoryImpl
        extends ValuesAtTimestampType
        implements FiniteSinusoidMovingProviderFactory {
    /** @noinspection rawtypes */
    private final Map<String, Function<UUID, Function<Map, Function<float[], Function<Long, Function<TimestampValidator, FiniteSinusoidMovingProvider>>>>>> FACTORIES;
    private final TimestampValidator TIMESTAMP_VALIDATOR;

    /** @noinspection rawtypes, ConstantConditions */
    public FiniteSinusoidMovingProviderFactoryImpl(
            Map<String, Function<UUID, Function<Map, Function<float[], Function<Long, Function<TimestampValidator, FiniteSinusoidMovingProvider>>>>>> factories,
            TimestampValidator timestampValidator) {
        Check.ifMapIsNonEmptyWithRealKeysAndValues(factories, "factories");
        FACTORIES = mapOf(factories);

        TIMESTAMP_VALIDATOR = Check.ifNull(timestampValidator, "timestampValidator");
    }

    @Override
    public <T> FiniteSinusoidMovingProvider<T> make(UUID uuid, Map<Long, T> valuesAtTimestamps,
                                                    float[] transitionSharpnesses,
                                                    Long pausedTimestamp)
            throws IllegalArgumentException {
        var type = get(valuesAtTimestamps);
        var factory = FACTORIES.get(type);
        //noinspection unchecked
        return (FiniteSinusoidMovingProvider<T>) factory.apply(uuid).apply(valuesAtTimestamps)
                .apply(transitionSharpnesses).apply(pausedTimestamp).apply(TIMESTAMP_VALIDATOR);
    }
}
