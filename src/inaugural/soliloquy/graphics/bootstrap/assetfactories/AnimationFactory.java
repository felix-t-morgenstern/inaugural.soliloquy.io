package inaugural.soliloquy.graphics.bootstrap.assetfactories;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.assets.Animation;
import soliloquy.specs.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.AnimationDefinition;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class AnimationFactory extends AssetFactoryAbstract<AnimationDefinition, Animation> {
    @Override
    public Animation make(AnimationDefinition animationDefinition) throws IllegalArgumentException {
        Check.ifNull(animationDefinition, "animationDefinition");
        return new AnimationImpl(animationDefinition.id(), animationDefinition.msDuration(),
                animationDefinition.frameSnippetDefinitions());
    }

    @Override
    public String getInterfaceName() {
        return AssetFactory.class.getCanonicalName() + "<" +
                AnimationDefinition.class.getCanonicalName() + "," +
                Animation.class.getCanonicalName() + ">";
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    public class AnimationImpl implements Animation {
        private final int MS_DURATION;
        private final Map<Integer, AnimationFrameSnippet> FRAMES;
        private final NearestFloorTree FRAMES_MS_NEAREST_FLOOR_TREE;
        private final String ID;
        private final boolean SUPPORTS_MOUSE_EVENT_CAPTURING;

        public AnimationImpl(String id, int msDuration,
                             Map<Integer, AnimationFrameSnippet> frames) {
            MS_DURATION = Check.ifNonNegative(msDuration, "msDuration");
            ID = Check.ifNullOrEmpty(id, "id");
            SUPPORTS_MOUSE_EVENT_CAPTURING = checkFramesValidityAndReturnCaptures(frames);
            FRAMES = frames;
            FRAMES_MS_NEAREST_FLOOR_TREE = new NearestFloorTree(FRAMES.keySet());
        }

        private boolean checkFramesValidityAndReturnCaptures(Map<Integer, AnimationFrameSnippet>
                                                                     frames) {
            Check.ifNull(frames, "frames");
            if (frames.isEmpty()) {
                throw new IllegalArgumentException("AnimationImpl: frames cannot be empty");
            }
            boolean frameAt0Ms = false;
            boolean supportsMouseEventCapturing = true;
            for (Map.Entry<Integer, AnimationFrameSnippet> frameWithMs : frames.entrySet()) {
                Check.ifNonNegative(frameWithMs.getKey(), "frameWithMs.getKey()");
                Check.throwOnSecondLte(frameWithMs.getKey(), MS_DURATION, "frameWithMs.getKey()",
                        "MS_DURATION");
                if (frameWithMs.getKey() == 0) {
                    frameAt0Ms = true;
                }
                throwOnInvalidSnippetDefinition(frameWithMs.getValue(), "frameWithMs.getValue()");

                if (supportsMouseEventCapturing) {
                    supportsMouseEventCapturing =
                            frameWithMs.getValue().image().supportsMouseEventCapturing();
                }
            }
            if (!frameAt0Ms) {
                throw new IllegalArgumentException("AnimationImpl: No frame at 0 ms");
            }

            // TODO: Ensure that this value is returned properly
            return supportsMouseEventCapturing;
        }

        @Override
        public int msDuration() {
            return MS_DURATION;
        }

        @Override
        public AnimationFrameSnippet snippetAtFrame(int ms) throws IllegalArgumentException {
            Check.ifNonNegative(ms, "ms");
            Check.throwOnSecondGt(MS_DURATION, ms, "MS_DURATION", "ms");
            return FRAMES.get(FRAMES_MS_NEAREST_FLOOR_TREE.getNearestFloor(ms));
        }

        @Override
        public boolean supportsMouseEventCapturing() {
            return SUPPORTS_MOUSE_EVENT_CAPTURING;
        }

        @Override
        public String id() throws IllegalStateException {
            return ID;
        }

        @Override
        public String getInterfaceName() {
            return Animation.class.getCanonicalName();
        }

        class NearestFloorTree {
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
}
