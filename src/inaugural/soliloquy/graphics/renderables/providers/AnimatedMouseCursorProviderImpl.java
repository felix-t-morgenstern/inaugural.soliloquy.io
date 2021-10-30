package inaugural.soliloquy.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.NearestFloorAndCeilingTree;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.providers.AnimatedMouseCursorProvider;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;

import java.util.HashMap;
import java.util.Map;

public class AnimatedMouseCursorProviderImpl extends AbstractLoopingProvider<Long>
        implements AnimatedMouseCursorProvider {
    private final String ID;
    private final HashMap<Long, Long> CURSORS_AT_MS;
    private final NearestFloorAndCeilingTree MS_POSITIONS;

    private final static StubEntityUuid STUB_UUID = new StubEntityUuid();

    /** @noinspection ConstantConditions*/
    public AnimatedMouseCursorProviderImpl(String id, Map<Long, Long> cursorsAtMs,
                                           int periodDuration, int periodModuloOffset,
                                           Long pauseTimestamp, Long mostRecentTimestamp) {
        super(STUB_UUID, periodDuration, periodModuloOffset, pauseTimestamp, mostRecentTimestamp);
        ID = Check.ifNullOrEmpty(id, "id");
        Check.ifNull(cursorsAtMs, "cursorsAtMs");
        if (cursorsAtMs.isEmpty()) {
            throw new IllegalArgumentException(
                    "AnimatedMouseCursorProviderImpl: cursorAtMs cannot be empty");
        }
        if (!cursorsAtMs.containsKey(0L)) {
            throw new IllegalArgumentException(
                    "AnimatedMouseCursorProviderImpl: A value must be present at 0ms");
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

    @Override
    public String id() throws IllegalStateException {
        return ID;
    }

    @Override
    public EntityUuid uuid() throws IllegalArgumentException, UnsupportedOperationException {
        throw new UnsupportedOperationException(
                "AnimatedMouseCursorProviderImpl supports id instead of uuid");
    }

    private static class StubEntityUuid implements EntityUuid {
        @Override
        public long getMostSignificantBits() {
            throw new UnsupportedOperationException();
        }

        @Override
        public long getLeastSignificantBits() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getInterfaceName() {
            throw new UnsupportedOperationException();
        }
    }
}
