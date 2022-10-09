package inaugural.soliloquy.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.AbstractFinitePausableAtTime;
import soliloquy.specs.common.valueobjects.Pair;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;

import java.util.UUID;

public class ProgressiveStringProvider
        extends AbstractFinitePausableAtTime
        implements ProviderAtTime<String> {
    private final UUID UUID;
    private final String STRING;
    private final long TIME_TO_COMPLETE;

    public ProgressiveStringProvider(UUID uuid, String string, long startTimestamp,
                                     long timeToComplete, Long pausedTimestamp,
                                     Long mostRecentTimestamp) {
        super(startTimestamp, pausedTimestamp, mostRecentTimestamp);
        UUID = Check.ifNull(uuid, "uuid");
        STRING = Check.ifNull(string, "string");
        TIME_TO_COMPLETE = Check.throwOnLteZero(timeToComplete, "timeToComplete");
    }

    @Override
    public String provide(long timestamp) throws IllegalArgumentException {
        TIMESTAMP_VALIDATOR.validateTimestamp(timestamp);

        if (_pausedTimestamp != null) {
            timestamp = _pausedTimestamp;
        }

        long timeWithinPeriod = timestamp - _anchorTime;

        if (timeWithinPeriod <= 0) {
            return "";
        }
        if (timeWithinPeriod >= TIME_TO_COMPLETE) {
            return STRING;
        }

        double percentOfPeriod = timeWithinPeriod / (double) TIME_TO_COMPLETE;
        int numberOfCharactersToProvide = (int) (STRING.length() * percentOfPeriod);
        return STRING.substring(0, numberOfCharactersToProvide);
    }

    @Override
    public Object representation() {
        // TODO: Revise this awful data structure.
        return new Pair<>(STRING, new Pair<>(TIME_TO_COMPLETE, _anchorTime));
    }

    @Override
    public String getArchetype() {
        return "";
    }

    @Override
    public UUID uuid() {
        return UUID;
    }

    @Override
    public Long mostRecentTimestamp() {
        return TIMESTAMP_VALIDATOR.mostRecentTimestamp();
    }

    @Override
    public String getInterfaceName() {
        return ProviderAtTime.class.getCanonicalName() + "<" + String.class.getCanonicalName() +
                ">";
    }
}
