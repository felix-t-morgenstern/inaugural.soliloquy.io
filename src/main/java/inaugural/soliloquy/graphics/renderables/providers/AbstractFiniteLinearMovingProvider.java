package inaugural.soliloquy.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.NearestFloorAndCeilingTree;
import inaugural.soliloquy.tools.generic.CanGetInterfaceName;
import inaugural.soliloquy.tools.timing.AbstractFinitePausableAtTime;
import soliloquy.specs.graphics.renderables.providers.FiniteLinearMovingProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// NB: This class has plenty of shared functionality with AbstractLoopingLinearMovingProvider, but
//     since Java does not support multiple inheritance, the functionality is duplicated.
public abstract class AbstractFiniteLinearMovingProvider<T> extends AbstractFinitePausableAtTime
        implements FiniteLinearMovingProvider<T> {
    private final UUID UUID;
    private final T ARCHETYPE;
    private final HashMap<Long, T> VALUES_AT_TIMES;
    protected final NearestFloorAndCeilingTree NEAREST_FLOOR_AND_CEILING_TREE;
    protected final CanGetInterfaceName CAN_GET_INTERFACE_NAME;

    protected AbstractFiniteLinearMovingProvider(UUID uuid, Map<Long, T> valuesAtTimes,
                                                 Long pausedTimestamp, Long mostRecentTimestamp,
                                                 T archetype) {
        super(pausedTimestamp, mostRecentTimestamp);
        UUID = Check.ifNull(uuid, "uuid");
        ARCHETYPE = Check.ifNull(archetype, "archetype");

        VALUES_AT_TIMES = new HashMap<>();
        Check.ifNull(valuesAtTimes, "valuesAtTimes");
        if (valuesAtTimes.isEmpty()) {
            throw new IllegalArgumentException(
                    "AbstractFiniteLinearMovingProvider: valuesAtTimes is empty");
        }
        valuesAtTimes.forEach((time, value) -> VALUES_AT_TIMES.put(
                Check.ifNull(time, "time within valuesAtTimes"),
                Check.ifNull(value, "value within valuesAtTimes")
        ));

        NEAREST_FLOOR_AND_CEILING_TREE =
                NearestFloorAndCeilingTree.FromLongs(VALUES_AT_TIMES.keySet());
        CAN_GET_INTERFACE_NAME = new CanGetInterfaceName();
    }

    @Override
    public Map<Long, T> valuesAtTimestampsRepresentation() {
        return new HashMap<>(VALUES_AT_TIMES);
    }

    @Override
    public T provide(long timestamp) throws IllegalArgumentException {
        TIMESTAMP_VALIDATOR.validateTimestamp(timestamp);
        if (pausedTimestamp != null) {
            timestamp = pausedTimestamp;
        }
        if (VALUES_AT_TIMES.containsKey(timestamp)) {
            return VALUES_AT_TIMES.get(timestamp);
        }
        if (timestamp < NEAREST_FLOOR_AND_CEILING_TREE.MinimumValue) {
            return VALUES_AT_TIMES.get(NEAREST_FLOOR_AND_CEILING_TREE.MinimumValue);
        }
        if (timestamp > NEAREST_FLOOR_AND_CEILING_TREE.MaximumValue) {
            return VALUES_AT_TIMES.get(NEAREST_FLOOR_AND_CEILING_TREE.MaximumValue);
        }
        long time1 = NEAREST_FLOOR_AND_CEILING_TREE.getNearestFloor(timestamp);
        int transitionNumber = NEAREST_FLOOR_AND_CEILING_TREE.ValueIndices.get(time1);
        long time2 = NEAREST_FLOOR_AND_CEILING_TREE
                .OrderedValues[NEAREST_FLOOR_AND_CEILING_TREE.ValueIndices.get(time1) + 1];
        long distanceBetweenTimes = time2 - time1;
        float weight2 = (timestamp - time1) / (float) distanceBetweenTimes;
        float weight1 = 1f - weight2;
        T value1 = VALUES_AT_TIMES.get(time1);
        T value2 = VALUES_AT_TIMES.get(time2);

        return interpolate(value1, weight1, value2, weight2, transitionNumber);
    }

    protected abstract T interpolate(T value1, float weight1, T value2, float weight2,
                                     int transitionNumber);

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
        return FiniteLinearMovingProvider.class.getName() + "<" +
                CAN_GET_INTERFACE_NAME.getProperTypeName(getArchetype()) + ">";
    }

    @Override
    protected void updateInternalValuesOnUnpause(long timestamp) {
        long pauseDuration = timestamp - pausedTimestamp;
        for (int i = 0; i < NEAREST_FLOOR_AND_CEILING_TREE.OrderedValues.length; i++) {
            T value = VALUES_AT_TIMES.get(NEAREST_FLOOR_AND_CEILING_TREE.OrderedValues[i]);
            VALUES_AT_TIMES.remove(NEAREST_FLOOR_AND_CEILING_TREE.OrderedValues[i]);
            VALUES_AT_TIMES.put(NEAREST_FLOOR_AND_CEILING_TREE.OrderedValues[i] + pauseDuration,
                    value);
        }
        NEAREST_FLOOR_AND_CEILING_TREE.incrementAllValues(pauseDuration);
    }

    @Override
    public Object representation() {
        return new HashMap<>(VALUES_AT_TIMES);
    }

    @Override
    public T getArchetype() {
        return ARCHETYPE;
    }
}
