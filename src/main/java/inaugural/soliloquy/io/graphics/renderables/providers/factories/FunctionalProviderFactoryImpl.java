package inaugural.soliloquy.io.graphics.renderables.providers.factories;

import inaugural.soliloquy.io.graphics.renderables.providers.FunctionalProviderImpl;
import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.common.entities.Consumer;
import soliloquy.specs.common.entities.Function;
import soliloquy.specs.io.graphics.renderables.providers.FunctionalProvider;
import soliloquy.specs.io.graphics.renderables.providers.factories.FunctionalProviderFactory;

import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;

public class FunctionalProviderFactoryImpl implements FunctionalProviderFactory {
    @SuppressWarnings("rawtypes") private final java.util.function.Function<String, Function> GET_FUNCTION;
    @SuppressWarnings("rawtypes") private final java.util.function.Function<String, Consumer> GET_CONSUMER;
    private final TimestampValidator VALIDATOR;

    public FunctionalProviderFactoryImpl(
            @SuppressWarnings("rawtypes") java.util.function.Function<String, Function> getFunction,
            @SuppressWarnings("rawtypes") java.util.function.Function<String, Consumer> getConsumer,
            TimestampValidator validator) {
        GET_FUNCTION = Check.ifNull(getFunction, "getFunction");
        GET_CONSUMER = Check.ifNull(getConsumer, "getConsumer");
        VALIDATOR = Check.ifNull(validator, "validator");
    }

    @Override
    public <T> FunctionalProvider<T> make(
            UUID uuid,
            String provideFunctionId,
            String pauseConsumerId,
            String unpauseConsumerId,
            Long pauseTimestamp,
            Map<String, Object> data) throws IllegalArgumentException {
        Check.ifNull(uuid, "uuid");
        Check.ifNull(data, "data");

        @SuppressWarnings("unchecked") Function<FunctionalProvider.Inputs, T> provideFunction = GET_FUNCTION.apply(Check.ifNullOrEmpty(provideFunctionId, "provideFunctionId"));
        if (provideFunction == null) {
            throw new IllegalArgumentException("FunctionalProviderFactoryImpl.make: provideFunctionId (" + provideFunctionId + ") does not correspond to valid Function");
        }

        var pauseConsumer = getConsumer(pauseConsumerId, "pauseConsumerId");
        var unpauseConsumer = getConsumer(unpauseConsumerId, "unpauseConsumerId");

        return new FunctionalProviderImpl<>(
                uuid,
                provideFunction,
                pauseConsumer,
                unpauseConsumer,
                data,
                pauseTimestamp,
                VALIDATOR
        );
    }

    private Consumer<FunctionalProvider.Inputs> getConsumer(String actionId, String actionIdName) {
        Consumer<FunctionalProvider.Inputs> action = null;
        if (actionId != null) {
            if (actionId.isEmpty()) {
                throw new IllegalArgumentException("FunctionalProviderFactoryImpl.make: " + actionIdName + " cannot be empty");
            }
            //noinspection unchecked
            action = GET_CONSUMER.apply(Check.ifNullOrEmpty(actionId, actionIdName));
            if (action == null) {
                throw new IllegalArgumentException("FunctionalProviderFactoryImpl.make: " + actionIdName + " (" + actionId + ") does not correspond to valid Function");
            }
        }
        return action;
    }
}
