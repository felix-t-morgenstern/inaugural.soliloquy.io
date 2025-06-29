package inaugural.soliloquy.io.graphics.renderables.providers.factories;

import inaugural.soliloquy.io.graphics.renderables.providers.ValuesAtTimestampType;
import inaugural.soliloquy.tools.Check;
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
            Function<Long, Function<Long, FiniteLinearMovingProvider>>>>> FACTORIES;

    /** @noinspection rawtypes, ConstantConditions */
    public FiniteLinearMovingProviderFactoryImpl(Map<String, Function<UUID, Function<Map,
            Function<Long, Function<Long, FiniteLinearMovingProvider>>>>> factories) {
        Check.ifMapIsNonEmptyWithRealKeysAndValues(factories, "factories");

        FACTORIES = mapOf(factories);
    }

    @Override
    public <T> FiniteLinearMovingProvider<T> make(UUID uuid, Map<Long, T> valuesAtTimestamps,
                                                  Long pausedTimestamp, Long mostRecentTimestamp)
            throws IllegalArgumentException {
        var type = ValuesAtTimestampType.get(valuesAtTimestamps);
        var factory = FACTORIES.get(type);
        //noinspection unchecked
        return (FiniteLinearMovingProvider<T>) factory.apply(uuid).apply(valuesAtTimestamps)
                .apply(pausedTimestamp).apply(mostRecentTimestamp);
    }
}
