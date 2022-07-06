package inaugural.soliloquy.graphics.renderables.providers.factories;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.generic.CanGetInterfaceName;
import soliloquy.specs.graphics.renderables.providers.LoopingLinearMovingProvider;
import soliloquy.specs.graphics.renderables.providers.factories.LoopingLinearMovingProviderFactory;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class LoopingLinearMovingProviderFactoryImpl implements LoopingLinearMovingProviderFactory {
    /** @noinspection rawtypes*/
    private final Map<String, Function<UUID, Function<Integer, Function<Integer,
            Function<Map, Function<Long, Function<Long, Function<Object,
                    LoopingLinearMovingProvider>>>>>>>> FACTORIES;

    private static final CanGetInterfaceName CAN_GET_INTERFACE_NAME = new CanGetInterfaceName();

    public LoopingLinearMovingProviderFactoryImpl(
            @SuppressWarnings({"rawtypes", "ConstantConditions"}) Map<String, Function<UUID,
                    Function<Integer, Function<Integer, Function<Map, Function<Long, Function<Long,
                            Function<Object, LoopingLinearMovingProvider>>>>>>>> factories) {
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
                                                   Long mostRecentTimestamp, Long pausedTimestamp,
                                                   T archetype) throws IllegalArgumentException {
        String type = CAN_GET_INTERFACE_NAME.getProperTypeName(archetype);
        //noinspection rawtypes
        Function<UUID, Function<Integer, Function<Integer, Function<Map, Function<Long,
                Function<Long, Function<Object, LoopingLinearMovingProvider>>>>>>> factory =
                FACTORIES.get(type);
        //noinspection unchecked
        return (LoopingLinearMovingProvider<T>)factory
                .apply(uuid)
                .apply(periodDuration)
                .apply(periodModuloOffset)
                .apply(valuesWithinPeriod)
                .apply(mostRecentTimestamp)
                .apply(pausedTimestamp)
                .apply(archetype);
    }

    @Override
    public String getInterfaceName() {
        return LoopingLinearMovingProviderFactory.class.getCanonicalName();
    }
}
