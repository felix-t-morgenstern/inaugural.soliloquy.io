package inaugural.soliloquy.graphics.renderables.providers;

import inaugural.soliloquy.graphics.shared.FloorFrameProvider;
import soliloquy.specs.graphics.renderables.providers.LoopingLinearMovingProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RectangleAnimatedBackgroundTextureIdProvider
        extends AbstractLoopingProvider<Integer>
        implements LoopingLinearMovingProvider<Integer> {
    private final FloorFrameProvider<Integer> FLOOR_FRAME_PROVIDER;

    public RectangleAnimatedBackgroundTextureIdProvider(UUID uuid, int periodDuration,
                                                        int periodModuloOffset,
                                                        Map<Integer, Integer> valuesWithinPeriod,
                                                        Long pauseTimestamp,
                                                        Long mostRecentTimestamp) {
        super(uuid, periodDuration, periodModuloOffset, pauseTimestamp, mostRecentTimestamp, 0);
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
        return new HashMap<>(FLOOR_FRAME_PROVIDER.FRAMES);
    }

    // NB: Handling this manually rather than via HasOneGenericParam from Tools, since Java classes
    //     cannot extend multiple classes, and accessing LoopingPausableAtTime shared functionality
    //     via inheritance rather than composition seems more parsimonious and elegant at the
    //     moment
    @Override
    public String getInterfaceName() {
        return LoopingLinearMovingProvider.class.getCanonicalName() + "<" +
                Integer.class.getCanonicalName() + ">";
    }

    @Override
    public Object representation() {
        return FLOOR_FRAME_PROVIDER.representation();
    }
}
