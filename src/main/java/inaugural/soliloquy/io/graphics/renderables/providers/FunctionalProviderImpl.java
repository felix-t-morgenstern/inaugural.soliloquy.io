package inaugural.soliloquy.io.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.entities.Function;
import soliloquy.specs.io.graphics.renderables.providers.FunctionalProvider;

import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;

public class FunctionalProviderImpl<T> implements FunctionalProvider<T> {
    private final UUID UUID;
    private final soliloquy.specs.common.entities.Function<Inputs, T> PROVIDE;
    private final Action<Inputs> PAUSE;
    private final Action<Inputs> UNPAUSE;
    private final Map<String, Object> DATA;
    private final TimestampValidator VALIDATOR;

    private Long pauseTimestamp;

    public FunctionalProviderImpl(UUID uuid,
                                  Function<Inputs, T> provide,
                                  Action<Inputs> pause,
                                  Action<Inputs> unpause,
                                  Map<String, Object> data,
                                  Long pauseTimestamp,
                                  TimestampValidator validator) {
        UUID = Check.ifNull(uuid, "uuid");
        PROVIDE = Check.ifNull(provide, "provide");
        PAUSE = pause;
        UNPAUSE = unpause;
        DATA = mapOf(Check.ifNull(data, "data"));
        this.pauseTimestamp = pauseTimestamp;
        VALIDATOR = Check.ifNull(validator, "validator");
    }

    @Override
    public T provide(long timestamp) throws IllegalArgumentException {
        VALIDATOR.validateTimestamp(timestamp);
        return PROVIDE.apply(new Inputs(timestamp, pauseTimestamp, DATA));
    }

    @Override
    public Object representation() {
        return new Representation(
                PROVIDE.id(),
                PAUSE.id(),
                UNPAUSE.id(),
                pauseTimestamp,
                mapOf(DATA)
        );
    }

    @Override
    public UUID uuid() {
        return UUID;
    }

    @Override
    public void reportPause(long timestamp)
            throws IllegalArgumentException, UnsupportedOperationException {
        if (pauseTimestamp != null) {
            throw new UnsupportedOperationException(
                    "FunctionalProviderImpl.reportPause: already paused");
        }
        VALIDATOR.validateTimestamp(timestamp);
        if (PAUSE != null) {
            PAUSE.accept(new Inputs(timestamp, null, DATA));
        }
        pauseTimestamp = timestamp;
    }

    @Override
    public void reportUnpause(long timestamp)
            throws IllegalArgumentException, UnsupportedOperationException {
        if (pauseTimestamp == null) {
            throw new UnsupportedOperationException(
                    "FunctionalProviderImpl.reportUnpause: already unpaused");
        }
        if (timestamp < pauseTimestamp) {
            throw new IllegalArgumentException(
                    "FunctionalProviderImpl.reportUnpause: timestamp (" + timestamp +
                            ") cannot be prior to pauseTimestamp (" + pauseTimestamp + ")");
        }
        VALIDATOR.validateTimestamp(timestamp);
        if (UNPAUSE != null) {
            UNPAUSE.accept(new Inputs(timestamp, pauseTimestamp, DATA));
        }
        pauseTimestamp = null;
    }

    @Override
    public Long pausedTimestamp() {
        return pauseTimestamp;
    }

    @Override
    public Map<String, Object> data() throws IllegalStateException {
        return DATA;
    }
}
