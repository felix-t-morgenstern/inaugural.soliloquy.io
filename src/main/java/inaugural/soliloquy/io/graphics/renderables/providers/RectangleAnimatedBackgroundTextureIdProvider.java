package inaugural.soliloquy.io.graphics.renderables.providers;

import inaugural.soliloquy.io.graphics.shared.FloorFrameProvider;
import soliloquy.specs.io.graphics.renderables.providers.LoopingLinearMovingProvider;

import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;

public class RectangleAnimatedBackgroundTextureIdProvider
        extends AbstractLoopingProvider<Integer>
        implements LoopingLinearMovingProvider<Integer> {
    private final FloorFrameProvider<Integer> FLOOR_FRAME_PROVIDER;

    public RectangleAnimatedBackgroundTextureIdProvider(UUID uuid, int periodDuration,
                                                        int periodModuloOffset,
                                                        Map<Integer, Integer> valuesWithinPeriod,
                                                        Long pauseTimestamp,
                                                        Long mostRecentTimestamp) {
        super(uuid, periodDuration, periodModuloOffset, pauseTimestamp, mostRecentTimestamp);
        FLOOR_FRAME_PROVIDER = new FloorFrameProvider<>(periodDuration, valuesWithinPeriod, null,
                null);
    }

    @Override
    protected Integer provideValueAtMsWithinPeriod(int msWithinPeriod) {
        return FLOOR_FRAME_PROVIDER.valueAtFrame(msWithinPeriod);
    }

    @Override
    public int periodDuration() {
        return FLOOR_FRAME_PROVIDER.MS_DURATION;
    }

    @Override
    public Map<Integer, Integer> valuesWithinPeriod() {
        return mapOf(FLOOR_FRAME_PROVIDER.FRAMES);
    }

    @Override
    public Object representation() {
        return FLOOR_FRAME_PROVIDER.representation();
    }
}
