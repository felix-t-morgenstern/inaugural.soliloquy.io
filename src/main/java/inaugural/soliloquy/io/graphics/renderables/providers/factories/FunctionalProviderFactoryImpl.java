package inaugural.soliloquy.io.graphics.renderables.providers.factories;

import inaugural.soliloquy.io.graphics.renderables.providers.FunctionalProviderImpl;
import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.entities.Function;
import soliloquy.specs.io.graphics.renderables.providers.FunctionalProvider;
import soliloquy.specs.io.graphics.renderables.providers.factories.FunctionalProviderFactory;

import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;

public class FunctionalProviderFactoryImpl implements FunctionalProviderFactory {
    @SuppressWarnings("rawtypes") private final java.util.function.Function<String, Function> GET_FUNCTION;
    @SuppressWarnings("rawtypes") private final java.util.function.Function<String, Action> GET_ACTION;
    private final TimestampValidator VALIDATOR;

    public FunctionalProviderFactoryImpl(
            @SuppressWarnings("rawtypes") java.util.function.Function<String, Function> getFunction,
            @SuppressWarnings("rawtypes") java.util.function.Function<String, Action> getAction,
            TimestampValidator validator) {
        GET_FUNCTION = Check.ifNull(getFunction, "getFunction");
        GET_ACTION = Check.ifNull(getAction, "getAction");
        VALIDATOR = Check.ifNull(validator, "validator");
    }

    @Override
    public <T> FunctionalProvider<T> make(
            UUID uuid,
            String provideFunctionId,
            String pauseActionId,
            String unpauseActionId,
            Long pauseTimestamp,
            Map<String, Object> data) throws IllegalArgumentException {
        Check.ifNull(uuid, "uuid");
        Check.ifNull(data, "data");

        @SuppressWarnings("unchecked") Function<FunctionalProvider.Inputs, T> provideFunction = GET_FUNCTION.apply(Check.ifNullOrEmpty(provideFunctionId, "provideFunctionId"));
        if (provideFunction == null) {
            throw new IllegalArgumentException("FunctionalProviderFactoryImpl.make: provideFunctionId (" + provideFunctionId + ") does not correspond to valid Function");
        }

        var pauseAction = getAction(pauseActionId, "pauseActionId");
        var unpauseAction = getAction(unpauseActionId, "unpauseActionId");

        return new FunctionalProviderImpl<>(
                uuid,
                provideFunction,
                pauseAction,
                unpauseAction,
                mapOf(data),
                pauseTimestamp,
                VALIDATOR
        );
    }

    private Action<FunctionalProvider.Inputs> getAction(String actionId, String actionIdName) {
        Action<FunctionalProvider.Inputs> action = null;
        if (actionId != null) {
            if (actionId.isEmpty()) {
                throw new IllegalArgumentException("FunctionalProviderFactoryImpl.make: " + actionIdName + " cannot be empty");
            }
            //noinspection unchecked
            action = GET_ACTION.apply(Check.ifNullOrEmpty(actionId, actionIdName));
            if (action == null) {
                throw new IllegalArgumentException("FunctionalProviderFactoryImpl.make: " + actionIdName + " (" + actionId + ") does not correspond to valid Function");
            }
        }
        return action;
    }
}
