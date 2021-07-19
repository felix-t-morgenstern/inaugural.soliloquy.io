package inaugural.soliloquy.graphics.renderables.providers;

import inaugural.soliloquy.graphics.shared.FloorFrameProvider;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.providers.LoopingMovingProvider;

import java.util.HashMap;
import java.util.Map;

public class RectangleAnimatedBackgroundTextureIdProvider
        extends LoopingProviderAbstract<Integer>
        implements LoopingMovingProvider<Integer> {
    private final FloorFrameProvider<Integer> FLOOR_FRAME_PROVIDER;

    public RectangleAnimatedBackgroundTextureIdProvider(EntityUuid uuid, int periodDuration,
                                                        int periodModuloOffset,
                                                        Map<Integer, Integer> valuesWithinPeriod) {
        super(uuid, periodDuration, periodModuloOffset);
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
    public boolean movementIsLinear() {
        return false;
    }

    @Override
    public Map<Integer, Integer> valuesWithinPeriod() {
        return new HashMap<>(FLOOR_FRAME_PROVIDER.FRAMES);
    }

    @Override
    public Integer getArchetype() {
        return 0;
    }

    // NB: Handling this manually rather than via HasOneGenericParam from Tools, since Java classes
    //     cannot extend multiple classes, and accessing LoopingPausableAtTime shared functionality
    //     via inheritance rather than composition seems more parsimonious and elegant at the
    //     moment
    @Override
    public String getInterfaceName() {
        return LoopingMovingProvider.class.getCanonicalName() + "<" +
                Integer.class.getCanonicalName() + ">";
    }
}
