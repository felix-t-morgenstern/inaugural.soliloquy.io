package inaugural.soliloquy.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.NearestFloorAndCeilingTree;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;

import java.util.HashMap;
import java.util.Map;

public class AnimatedMouseCursorProvider extends AbstractLoopingProvider<Long>
        implements ProviderAtTime<Long> {
    private final HashMap<Long, Long> CURSORS_AT_MS;
    private final NearestFloorAndCeilingTree MS_POSITIONS;

    /** @noinspection ConstantConditions*/
    public AnimatedMouseCursorProvider(EntityUuid uuid, Map<Long, Long> cursorsAtMs,
                                       int periodDuration, int periodModuloOffset,
                                       Long pauseTimestamp, Long mostRecentTimestamp) {
        super(uuid, periodDuration, periodModuloOffset, pauseTimestamp, mostRecentTimestamp);
        Check.ifNull(cursorsAtMs, "cursorsAtMs");
        if (cursorsAtMs.isEmpty()) {
            throw new IllegalArgumentException(
                    "AnimatedMouseCursorProvider: cursorAtMs cannot be empty");
        }
        if (!cursorsAtMs.containsKey(0L)) {
            throw new IllegalArgumentException(
                    "AnimatedMouseCursorProvider: A value must be present at 0ms");
        }
        cursorsAtMs.forEach((ms, cursor) -> {
            Check.ifNull(ms, "ms within cursorsAtMs");
            Check.ifNull(cursor, "cursor within cursorsAtMs");
        });
        CURSORS_AT_MS = new HashMap<>(cursorsAtMs);
        MS_POSITIONS = NearestFloorAndCeilingTree.FromLongs(cursorsAtMs.keySet());
        Check.throwOnSecondLte(MS_POSITIONS.MaximumValue, periodDuration,
                "maximum ms within cursorsAtMs", "periodDuration");
    }

    @Override
    protected Long provideValueAtMsWithinPeriod(int msWithinPeriod) {
        return CURSORS_AT_MS.get(MS_POSITIONS.getNearestFloor(msWithinPeriod));
    }

    @Override
    public Long getArchetype() {
        return 0L;
    }

    @Override
    public String getInterfaceName() {
        return ProviderAtTime.class.getCanonicalName() + "<" + long.class.getCanonicalName() + ">";
    }
}
