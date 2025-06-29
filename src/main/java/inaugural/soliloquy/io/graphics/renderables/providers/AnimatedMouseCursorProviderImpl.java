package inaugural.soliloquy.io.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.NearestFloorAndCeilingTree;
import soliloquy.specs.io.graphics.renderables.providers.AnimatedMouseCursorProvider;

import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;

public class AnimatedMouseCursorProviderImpl extends AbstractLoopingProvider<Long>
        implements AnimatedMouseCursorProvider {
    private final String ID;
    private final Map<Integer, Long> CURSORS_AT_MS;
    private final NearestFloorAndCeilingTree MS_POSITIONS;

    private final static UUID PLACEHOLDER_UUID = new UUID(0, 0);

    public AnimatedMouseCursorProviderImpl(String id, Map<Integer, Long> cursorsAtMs,
                                           int periodDuration, int periodModuloOffset,
                                           Long pauseTimestamp, Long mostRecentTimestamp) {
        super(PLACEHOLDER_UUID, periodDuration, periodModuloOffset, pauseTimestamp,
                mostRecentTimestamp);
        ID = Check.ifNullOrEmpty(id, "id");
        Check.ifNull(cursorsAtMs, "cursorsAtMs");
        if (cursorsAtMs.isEmpty()) {
            throw new IllegalArgumentException(
                    "AnimatedMouseCursorProviderImpl: cursorAtMs cannot be empty");
        }
        if (!cursorsAtMs.containsKey(0)) {
            throw new IllegalArgumentException(
                    "AnimatedMouseCursorProviderImpl: A value must be present at 0ms");
        }
        cursorsAtMs.forEach((ms, cursor) -> {
            Check.ifNull(ms, "ms within cursorsAtMs");
            Check.ifNull(cursor, "cursor within cursorsAtMs");
        });
        CURSORS_AT_MS = mapOf(cursorsAtMs);
        MS_POSITIONS = NearestFloorAndCeilingTree.FromIntegers(cursorsAtMs.keySet());
        Check.throwOnSecondLte(MS_POSITIONS.MaximumValue, periodDuration,
                "maximum ms within cursorsAtMs", "periodDuration");
    }

    @Override
    protected Long provideValueAtMsWithinPeriod(int msWithinPeriod) {
        return CURSORS_AT_MS.get((int) MS_POSITIONS.getNearestFloor(msWithinPeriod));
    }

    @Override
    public String id() throws IllegalStateException {
        return ID;
    }

    @Override
    public UUID uuid() throws IllegalArgumentException, UnsupportedOperationException {
        throw new UnsupportedOperationException(
                "AnimatedMouseCursorProviderImpl supports id instead of uuid");
    }

    @Override
    public Object representation() {
        return mapOf(CURSORS_AT_MS);
    }
}
