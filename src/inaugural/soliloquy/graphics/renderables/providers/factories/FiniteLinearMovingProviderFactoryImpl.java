package inaugural.soliloquy.graphics.renderables.providers.factories;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.generic.CanGetInterfaceName;
import soliloquy.specs.graphics.renderables.providers.FiniteLinearMovingProvider;
import soliloquy.specs.graphics.renderables.providers.factories.FiniteLinearMovingProviderFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class FiniteLinearMovingProviderFactoryImpl implements FiniteLinearMovingProviderFactory {
    /** @noinspection rawtypes */
    private final Map<String, Function<UUID, Function<Map,
            Function<Long, Function<Long, FiniteLinearMovingProvider>>>>> FACTORIES;

    private static final CanGetInterfaceName CAN_GET_INTERFACE_NAME = new CanGetInterfaceName();

    /** @noinspection rawtypes, ConstantConditions */
    public FiniteLinearMovingProviderFactoryImpl(Map<String, Function<UUID, Function<Map,
            Function<Long, Function<Long, FiniteLinearMovingProvider>>>>> factories) {
        Check.ifMapIsNonEmptyWithRealKeysAndValues(factories, "factories");

        FACTORIES = new HashMap<>(factories);
    }

    @Override
    public <T> FiniteLinearMovingProvider<T> make(UUID id, Map<Long, T> valuesAtTimestamps,
                                                  Long pausedTimestamp, Long mostRecentTimestamp)
            throws IllegalArgumentException {
        String type = CAN_GET_INTERFACE_NAME
                .getProperTypeName(valuesAtTimestamps.values().iterator().next());
        //noinspection rawtypes
        Function<UUID, Function<Map, Function<Long, Function<Long, FiniteLinearMovingProvider>>>>
                factory = FACTORIES.get(type);
        //noinspection unchecked
        return (FiniteLinearMovingProvider<T>) factory.apply(id).apply(valuesAtTimestamps)
                .apply(pausedTimestamp).apply(mostRecentTimestamp);
    }

    @Override
    public String getInterfaceName() {
        return FiniteLinearMovingProviderFactory.class.getCanonicalName();
    }
}
