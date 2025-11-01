package inaugural.soliloquy.io.graphics.renderables.providers.factories;

import inaugural.soliloquy.io.graphics.renderables.providers.ValuesAtTimestampType;
import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.io.graphics.renderables.providers.LoopingLinearMovingProvider;
import soliloquy.specs.io.graphics.renderables.providers.factories.LoopingLinearMovingProviderFactory;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import static inaugural.soliloquy.tools.collections.Collections.mapKeys;

public class LoopingLinearMovingProviderFactoryImpl implements LoopingLinearMovingProviderFactory {
    /** @noinspection rawtypes */
    private final Map<String, Function<UUID, Function<Integer, Function<Integer, Function<Map,
            Function<Long, Function<TimestampValidator, LoopingLinearMovingProvider>>>>>>>
            FACTORIES;
    private final TimestampValidator TIMESTAMP_VALIDATOR;

    public LoopingLinearMovingProviderFactoryImpl(
            @SuppressWarnings({"rawtypes", "ConstantConditions"})
            Map<Class, Function<UUID, Function<Integer, Function<Integer, Function<Map, Function<Long, Function<TimestampValidator, LoopingLinearMovingProvider>>>>>>> factories,
            TimestampValidator timestampValidator) {
        Check.ifNull(factories, "factories");
        factories.forEach((type, factory) -> {
            Check.ifNull(type, "type within factories");
            Check.ifNull(factory, "factory within factories");
        });
        FACTORIES = mapKeys(factories, Class::getCanonicalName);
        TIMESTAMP_VALIDATOR = Check.ifNull(timestampValidator, "timestampValidator");
    }

    @Override
    public <T> LoopingLinearMovingProvider<T> make(UUID uuid, int periodDuration,
                                                   int periodModuloOffset,
                                                   Map<Integer, T> valuesWithinPeriod,
                                                   Long pausedTimestamp)
            throws IllegalArgumentException {
        var type = ValuesAtTimestampType.getTypeName(valuesWithinPeriod);
        var factory = FACTORIES.get(type);
        //noinspection unchecked
        return (LoopingLinearMovingProvider<T>) factory
                .apply(uuid)
                .apply(periodDuration)
                .apply(periodModuloOffset)
                .apply(valuesWithinPeriod)
                .apply(pausedTimestamp)
                .apply(TIMESTAMP_VALIDATOR);
    }
}
