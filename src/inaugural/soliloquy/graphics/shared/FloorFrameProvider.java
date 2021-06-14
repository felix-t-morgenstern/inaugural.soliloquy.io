package inaugural.soliloquy.graphics.shared;

import inaugural.soliloquy.tools.Check;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class FloorFrameProvider<T> {
    public final int MS_DURATION;
    public final boolean SUPPORTS_MOUSE_EVENT_CAPTURING;
    public final Map<Integer, T> FRAMES;

    private final NearestFloorTree FRAMES_MS_NEAREST_FLOOR_TREE;

    public FloorFrameProvider(int msDuration, Map<Integer, T> frames, Consumer<T> checkValue,
                              Function<T, Boolean> valueSupportsMouseEvents) {
        MS_DURATION = Check.ifNonNegative(msDuration, "msDuration");
        SUPPORTS_MOUSE_EVENT_CAPTURING =
                checkFramesValidityAndReturnCaptures(frames, checkValue, valueSupportsMouseEvents);
        FRAMES = frames;
        FRAMES_MS_NEAREST_FLOOR_TREE = new NearestFloorTree(FRAMES.keySet());
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

        // TODO: Ensure that this value is returned properly
        return supportsMouseEventCapturing;
    }

    public T valueAtFrame(int ms) throws IllegalArgumentException {
        Check.ifNonNegative(ms, "ms");
        Check.throwOnSecondGt(MS_DURATION, ms, "MS_DURATION", "ms");
        return FRAMES.get(FRAMES_MS_NEAREST_FLOOR_TREE.getNearestFloor(ms));
    }

    static class NearestFloorTree {
        int nodeValue;
        NearestFloorTree _leftNode;
        NearestFloorTree _rightNode;

        NearestFloorTree(int floor) {
            nodeValue = floor;
        }

        NearestFloorTree(int[] floors) {
            populateFromSortedArray(floors);
        }

        NearestFloorTree(Collection<Integer> unsorted) {
            int[] toSort = new int[unsorted.size()];
            int index = 0;
            for(Integer i : unsorted) {
                toSort[index++] = i;
            }
            Arrays.sort(toSort);
            populateFromSortedArray(toSort);
        }

        private void populateFromSortedArray(int[] floors) {
            if (floors.length == 1) {
                nodeValue = floors[0];
            }
            if (floors.length == 2) {
                nodeValue = floors[0];
                _rightNode = new NearestFloorTree(floors[1]);
            }
            if (floors.length == 3) {
                _leftNode = new NearestFloorTree(floors[0]);
                nodeValue = floors[1];
                _rightNode = new NearestFloorTree(floors[2]);
            }
            if (floors.length > 3) {
                int centerIndex;
                if (floors.length % 2 == 1) {
                    centerIndex = (floors.length / 2) + 1;
                }
                else {
                    float middleOfRange = (floors[0] + floors[floors.length - 1]) / 2f;
                    int leftOfCenterIndex = floors.length / 2;
                    int rightOfCenterIndex = leftOfCenterIndex + 1;
                    if (Math.abs(middleOfRange - floors[leftOfCenterIndex]) <
                            Math.abs(middleOfRange - floors[rightOfCenterIndex])) {
                        centerIndex = leftOfCenterIndex;
                    }
                    else {
                        centerIndex = rightOfCenterIndex;
                    }
                }
                nodeValue = floors[centerIndex];
                _leftNode = new NearestFloorTree(Arrays.copyOfRange(floors, 0, centerIndex));
                _rightNode = new NearestFloorTree(Arrays.copyOfRange(floors, centerIndex + 1,
                        floors.length));
            }
        }

        int getNearestFloor(int value) {
            return getNearestFloor(value, null);
        }

        private int getNearestFloor(int value, Integer nearestFloor) {
            if (value == nodeValue) {
                return nodeValue;
            }
            if (value > nodeValue) {
                nearestFloor = nodeValue;
                if (_rightNode == null) {
                    return nearestFloor;
                }
                return _rightNode.getNearestFloor(value, nearestFloor);
            }

            //if (value < nodeValue) {
            if (_leftNode == null) {
                return nearestFloor;
            }
            return _leftNode.getNearestFloor(value, nearestFloor);
        }
    }
}
