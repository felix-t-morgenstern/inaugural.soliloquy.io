package inaugural.soliloquy.graphics.renderables.providers.factories;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.generic.CanGetInterfaceName;
import soliloquy.specs.graphics.renderables.providers.FiniteSinusoidMovingProvider;
import soliloquy.specs.graphics.renderables.providers.factories.FiniteSinusoidMovingProviderFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class FiniteSinusoidMovingProviderFactoryImpl
        implements FiniteSinusoidMovingProviderFactory {
    /** @noinspection rawtypes */
    private final Map<String, Function<UUID, Function<Map, Function<List<Float>,
            Function<Long, Function<Long, FiniteSinusoidMovingProvider>>>>>> FACTORIES;

    private static final CanGetInterfaceName CAN_GET_INTERFACE_NAME = new CanGetInterfaceName();

    /** @noinspection rawtypes, ConstantConditions */
    public FiniteSinusoidMovingProviderFactoryImpl(
            Map<String, Function<UUID, Function<Map, Function<List<Float>, Function<Long,
                    Function<Long, FiniteSinusoidMovingProvider>>>>>> factories) {
        Check.ifMapIsNonEmptyWithRealKeysAndValues(factories, "factories");

        FACTORIES = new HashMap<>(factories);
    }

    @Override
    public <T> FiniteSinusoidMovingProvider<T> make(UUID uuid, Map<Long, T> valuesAtTimestamps,
                                                    List<Float> transitionSharpnesses,
                                                    Long pausedTimestamp, Long mostRecentTimestamp)
            throws IllegalArgumentException {
        String type = CAN_GET_INTERFACE_NAME
                .getProperTypeName(valuesAtTimestamps.values().iterator().next());
        //noinspection rawtypes
        Function<UUID, Function<Map, Function<List<Float>, Function<Long, Function<Long,
                FiniteSinusoidMovingProvider>>>>>
                factory = FACTORIES.get(type);
        //noinspection unchecked
        return (FiniteSinusoidMovingProvider<T>) factory.apply(uuid).apply(valuesAtTimestamps)
                .apply(transitionSharpnesses).apply(pausedTimestamp).apply(mostRecentTimestamp);
    }

    @Override
    public String getInterfaceName() {
        return FiniteSinusoidMovingProviderFactory.class.getCanonicalName();
    }
}
