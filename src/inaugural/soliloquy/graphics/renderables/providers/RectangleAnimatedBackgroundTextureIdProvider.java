package inaugural.soliloquy.graphics.renderables.providers;

import inaugural.soliloquy.graphics.shared.FloorFrameProvider;
import soliloquy.specs.graphics.renderables.providers.LoopingMovingProvider;

import java.util.HashMap;
import java.util.Map;

public class RectangleAnimatedBackgroundTextureIdProvider
        extends PausableProviderWithOffsetAbstract<Integer>
        implements LoopingMovingProvider<Integer> {
    private final FloorFrameProvider<Integer> FLOOR_FRAME_PROVIDER;

    public RectangleAnimatedBackgroundTextureIdProvider(int periodDuration, int periodModuloOffset,
                                                        Map<Integer, Integer> valuesWithinPeriod) {
        super(periodDuration, periodModuloOffset, 0);
        FLOOR_FRAME_PROVIDER = new FloorFrameProvider<>(periodDuration, valuesWithinPeriod, null, null);
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
    public boolean movementIsLinear() {
        return false;
    }

    @Override
    public Map<Integer, Integer> valuesWithinPeriod() {
        return new HashMap<>(FLOOR_FRAME_PROVIDER.FRAMES);
    }

    @Override
    protected String getUnparameterizedInterfaceName() {
        return LoopingMovingProvider.class.getCanonicalName();
    }
}
