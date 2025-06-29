package inaugural.soliloquy.io.graphics.renderables.providers.factories;

import inaugural.soliloquy.io.graphics.renderables.providers.ValuesAtTimestampType;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.io.graphics.renderables.providers.LoopingLinearMovingProvider;
import soliloquy.specs.io.graphics.renderables.providers.factories.LoopingLinearMovingProviderFactory;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class LoopingLinearMovingProviderFactoryImpl implements LoopingLinearMovingProviderFactory {
    /** @noinspection rawtypes */
    private final Map<String, Function<UUID, Function<Integer, Function<Integer, Function<Map,
            Function<Long, Function<Long, LoopingLinearMovingProvider>>>>>>>
            FACTORIES;

    public LoopingLinearMovingProviderFactoryImpl(
            @SuppressWarnings({"rawtypes", "ConstantConditions"})
            Map<String, Function<UUID, Function<Integer, Function<Integer, Function<Map,
                    Function<Long, Function<Long, LoopingLinearMovingProvider>>>>>>> factories) {
        Check.ifNull(factories, "factories");
        factories.forEach((type, factory) -> {
            Check.ifNullOrEmpty(type, "type within factories");
            Check.ifNull(factory, "factory within factories");
        });
        FACTORIES = factories;
    }

    @Override
    public <T> LoopingLinearMovingProvider<T> make(UUID uuid, int periodDuration,
                                                   int periodModuloOffset,
                                                   Map<Integer, T> valuesWithinPeriod,
                                                   Long mostRecentTimestamp, Long pausedTimestamp)
            throws IllegalArgumentException {
        var type = ValuesAtTimestampType.get(valuesWithinPeriod);
        //noinspection rawtypes
        Function<UUID, Function<Integer, Function<Integer, Function<Map, Function<Long,
                Function<Long, LoopingLinearMovingProvider>>>>>> factory =
                FACTORIES.get(type);
        //noinspection unchecked
        return (LoopingLinearMovingProvider<T>) factory
                .apply(uuid)
                .apply(periodDuration)
                .apply(periodModuloOffset)
                .apply(valuesWithinPeriod)
                .apply(mostRecentTimestamp)
                .apply(pausedTimestamp);
    }
}
