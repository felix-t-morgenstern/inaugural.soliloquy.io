package inaugural.soliloquy.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.NearestFloorAndCeilingTree;
import inaugural.soliloquy.tools.generic.CanGetInterfaceName;
import soliloquy.specs.graphics.renderables.providers.LoopingLinearMovingProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// NB: This class has plenty of shared functionality with AbstractFiniteLinearMovingProvider, but
//     since Java does not support multiple inheritance, the functionality is duplicated.
public abstract class AbstractLoopingLinearMovingProvider<T> extends AbstractLoopingProvider<T>
        implements LoopingLinearMovingProvider<T> {
    protected final HashMap<Integer, T> VALUES_AT_TIMES;
    protected final NearestFloorAndCeilingTree NEAREST_FLOOR_AND_CEILING_TREE;
    private final CanGetInterfaceName CAN_GET_INTERFACE_NAME;

    protected AbstractLoopingLinearMovingProvider(UUID uuid, Map<Integer, T> valuesAtTimes,
                                                  int periodDuration, int periodModuloOffset,
                                                  Long pausedTimestamp, Long mostRecentTimestamp) {
        super(uuid, periodDuration, periodModuloOffset, pausedTimestamp, mostRecentTimestamp);

        VALUES_AT_TIMES = new HashMap<>();
        Check.ifNull(valuesAtTimes, "valuesAtTimes");
        if (valuesAtTimes.isEmpty()) {
            throw new IllegalArgumentException(
                    "AbstractFiniteLinearMovingProvider: valuesAtTimes is empty");
        }
        if (!valuesAtTimes.containsKey(0)) {
            throw new IllegalArgumentException(
                    "AbstractFiniteLinearMovingProvider: valuesAtTimes must specify value at 0ms");
        }
        valuesAtTimes.forEach((time, value) ->
                VALUES_AT_TIMES
                        .put((int) ((long) (Check.ifNull(time, "time within valuesAtTimes"))),
                                Check.ifNull(value, "value within valuesAtTimes")));

        NEAREST_FLOOR_AND_CEILING_TREE =
                NearestFloorAndCeilingTree.FromIntegers(VALUES_AT_TIMES.keySet());
        CAN_GET_INTERFACE_NAME = new CanGetInterfaceName();
    }

    @Override
    protected T provideValueAtMsWithinPeriod(int msWithinPeriod) {
        if (VALUES_AT_TIMES.containsKey(msWithinPeriod)) {
            return VALUES_AT_TIMES.get(msWithinPeriod);
        }

        int periodStartMs;
        int periodEndMs;
        T value1;
        T value2;
        float weight1;
        float weight2;

        int transition;
        if (msWithinPeriod > NEAREST_FLOOR_AND_CEILING_TREE.MaximumValue) {
            periodStartMs = (int) NEAREST_FLOOR_AND_CEILING_TREE.MaximumValue;
            periodEndMs = PERIOD_DURATION;

            transition = VALUES_AT_TIMES.size() - 1;

            value2 = VALUES_AT_TIMES.get(0);
        }
        else {
            periodStartMs = (int) NEAREST_FLOOR_AND_CEILING_TREE.getNearestFloor(msWithinPeriod);
            periodEndMs = (int) NEAREST_FLOOR_AND_CEILING_TREE.getNearestCeiling(msWithinPeriod);

            transition = NEAREST_FLOOR_AND_CEILING_TREE.ValueIndices.get((long) periodStartMs);

            value2 = VALUES_AT_TIMES.get(periodEndMs);
        }
        value1 = VALUES_AT_TIMES.get(periodStartMs);

        long interval = periodEndMs - periodStartMs;

        weight1 = (periodEndMs - msWithinPeriod) / (float) interval;
        weight2 = 1f - weight1;


        return interpolate(value1, weight1, value2, weight2, isClockwise(transition));
    }

    protected abstract T interpolate(T value1, float weight1, T value2, float weight2,
                                     boolean isClockwise);

    protected boolean isClockwise(int transition) {
        return true;
    }

    public Map<Integer, T> valuesWithinPeriod() {
        return new HashMap<>(VALUES_AT_TIMES);
    }

    @Override
    public int periodDuration() {
        return PERIOD_DURATION;
    }

    @Override
    public String getInterfaceName() {
        return LoopingLinearMovingProvider.class.getName() + "<" +
                CAN_GET_INTERFACE_NAME.getProperTypeName(getArchetype()) + ">";
    }
}
