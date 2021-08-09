package inaugural.soliloquy.graphics.shared;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.NearestFloorAndCeilingTree;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class FloorFrameProvider<T> {
    public final int MS_DURATION;
    public final boolean SUPPORTS_MOUSE_EVENT_CAPTURING;
    public final Map<Integer, T> FRAMES;

    private final NearestFloorAndCeilingTree FRAMES_MS_NEAREST_FLOOR_AND_CEILING_TREE;

    public FloorFrameProvider(int msDuration, Map<Integer, T> frames, Consumer<T> checkValue,
                              Function<T, Boolean> valueSupportsMouseEvents) {
        MS_DURATION = Check.ifNonNegative(msDuration, "msDuration");
        SUPPORTS_MOUSE_EVENT_CAPTURING =
                checkFramesValidityAndReturnCaptures(frames, checkValue, valueSupportsMouseEvents);
        FRAMES = frames;
        FRAMES_MS_NEAREST_FLOOR_AND_CEILING_TREE =
                NearestFloorAndCeilingTree.FromIntegers(FRAMES.keySet());
    }

    private boolean checkFramesValidityAndReturnCaptures(Map<Integer, T> frames,
                                                         Consumer<T> checkValue,
                                                         Function<T, Boolean>
                                                                 valueSupportsMouseEvents) {
        Check.ifNull(frames, "frames");
        if (frames.isEmpty()) {
            throw new IllegalArgumentException("AnimationImpl: frames cannot be empty");
        }
        boolean frameAt0Ms = false;
        boolean supportsMouseEventCapturing = true;
        for (Map.Entry<Integer, T> frameWithMs : frames.entrySet()) {
            Check.ifNonNegative(frameWithMs.getKey(), "frameWithMs.getKey()");
            Check.throwOnSecondLte(frameWithMs.getKey(), MS_DURATION, "frameWithMs.getKey()",
                    "MS_DURATION");
            if (frameWithMs.getKey() == 0) {
                frameAt0Ms = true;
            }
            if (checkValue != null) {
                checkValue.accept(frameWithMs.getValue());
            }

            if (valueSupportsMouseEvents != null && supportsMouseEventCapturing) {
                supportsMouseEventCapturing =
                        valueSupportsMouseEvents.apply(frameWithMs.getValue());
            }
        }
        if (!frameAt0Ms) {
            throw new IllegalArgumentException("AnimationImpl: No frame at 0 ms");
        }

        return supportsMouseEventCapturing;
    }

    public T valueAtFrame(int ms) throws IllegalArgumentException {
        Check.ifNonNegative(ms, "ms");
        Check.throwOnSecondGt(MS_DURATION, ms, "MS_DURATION", "ms");
        return FRAMES.get((int) FRAMES_MS_NEAREST_FLOOR_AND_CEILING_TREE.getNearestFloor(ms));
    }
}
