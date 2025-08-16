package inaugural.soliloquy.io.graphics.renderables.providers.factories;

import inaugural.soliloquy.io.graphics.renderables.providers.ValuesAtTimestampType;
import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.io.graphics.renderables.providers.FiniteLinearMovingProvider;
import soliloquy.specs.io.graphics.renderables.providers.factories.FiniteLinearMovingProviderFactory;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;

public class FiniteLinearMovingProviderFactoryImpl
        implements FiniteLinearMovingProviderFactory {
    /** @noinspection rawtypes */
    private final Map<String, Function<UUID, Function<Map,
            Function<Long, Function<TimestampValidator, FiniteLinearMovingProvider>>>>> FACTORIES;
    private final TimestampValidator TIMESTAMP_VALIDATOR;

    /** @noinspection rawtypes, ConstantConditions */
    public FiniteLinearMovingProviderFactoryImpl(
            Map<String, Function<UUID, Function<Map, Function<Long, Function<TimestampValidator,
                    FiniteLinearMovingProvider>>>>> factories,
            TimestampValidator timestampValidator) {
        Check.ifMapIsNonEmptyWithRealKeysAndValues(factories, "factories");
        FACTORIES = mapOf(factories);

        TIMESTAMP_VALIDATOR = Check.ifNull(timestampValidator, "timestampValidator");
    }

    @Override
    public <T> FiniteLinearMovingProvider<T> make(UUID uuid, Map<Long, T> valuesAtTimestamps,
                                                  Long pausedTimestamp)
            throws IllegalArgumentException {
        var type = ValuesAtTimestampType.get(valuesAtTimestamps);
        var factory = FACTORIES.get(type);
        //noinspection unchecked
        return (FiniteLinearMovingProvider<T>) factory.apply(uuid).apply(valuesAtTimestamps)
                .apply(pausedTimestamp).apply(TIMESTAMP_VALIDATOR);
    }
}
