package inaugural.soliloquy.graphics.bootstrap.assetfactories;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.assets.Animation;
import soliloquy.specs.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.AnimationDefinition;

import java.util.Map;

public class AnimationFactory extends AssetFactoryAbstract<AnimationDefinition, Animation> {
    @Override
    public Animation make(AnimationDefinition animationDefinition) throws IllegalArgumentException {
        Check.ifNull(animationDefinition, "animationDefinition");
        return new AnimationImpl(animationDefinition.assetId(), animationDefinition.msDuration(),
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
        private final String ID;

        public AnimationImpl(String id, int msDuration,
                             Map<Integer, AnimationFrameSnippet> frames) {
            MS_DURATION = Check.ifNonNegative(msDuration, "msDuration");
            ID = Check.ifNullOrEmpty(id, "id");
            FRAMES = checkFramesValidity(frames);
        }

        private Map<Integer, AnimationFrameSnippet> checkFramesValidity(
                Map<Integer, AnimationFrameSnippet> frames) {
            Check.ifNull(frames, "frames");
            if (frames.isEmpty()) {
                throw new IllegalArgumentException("AnimationImpl: frames cannot be empty");
            }
            boolean frameAt0Ms = false;
            for (Map.Entry<Integer, AnimationFrameSnippet> frameWithMs : frames.entrySet()) {
                Check.ifNonNegative(frameWithMs.getKey(), "frameWithMs.getKey()");
                Check.throwOnSecondLte(frameWithMs.getKey(), MS_DURATION, "frameWithMs.getKey()",
                        "MS_DURATION");
                if (frameWithMs.getKey() == 0) {
                    frameAt0Ms = true;
                }
                throwOnInvalidSnippetDefinition(frameWithMs.getValue(), "frameWithMs.getValue()");
            }
            if (!frameAt0Ms) {
                throw new IllegalArgumentException("AnimationImpl: No frame at 0 ms");
            }
            return frames;
        }

        @Override
        public int msDuration() {
            return MS_DURATION;
        }

        // TODO: Consider optimization here
        @Override
        public AnimationFrameSnippet snippetAtFrame(int ms) throws IllegalArgumentException {
            Check.ifNonNegative(ms, "ms");
            Check.throwOnSecondGt(MS_DURATION, ms, "MS_DURATION", "ms");
            AnimationFrameSnippet frameToReturn = null;
            for (Map.Entry<Integer, AnimationFrameSnippet> frameWithMs : FRAMES.entrySet()) {
                if (ms >= frameWithMs.getKey()) {
                    frameToReturn = frameWithMs.getValue();
                }
                else return frameToReturn;
            }
            return frameToReturn;
        }

        @Override
        public String id() throws IllegalStateException {
            return ID;
        }

        @Override
        public String getInterfaceName() {
            return Animation.class.getCanonicalName();
        }
    }
}
