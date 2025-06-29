package inaugural.soliloquy.io.graphics.bootstrap.assetfactories;

import inaugural.soliloquy.io.graphics.shared.FloorFrameProvider;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.io.graphics.assets.Animation;
import soliloquy.specs.io.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.AnimationDefinition;

import java.util.Map;

public class AnimationFactory extends AbstractAssetFactory<AnimationDefinition, Animation> {
    @Override
    public Animation make(AnimationDefinition animationDefinition) throws IllegalArgumentException {
        Check.ifNull(animationDefinition, "animationDefinition");
        return new AnimationImpl(animationDefinition.id(), animationDefinition.msDuration(),
                animationDefinition.frameSnippetDefinitions());
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    public class AnimationImpl implements Animation {
        private final String ID;
        private final FloorFrameProvider<AnimationFrameSnippet> FLOOR_FRAME_PROVIDER;

        public AnimationImpl(String id, int msDuration,
                             Map<Integer, AnimationFrameSnippet> frames) {
            ID = Check.ifNullOrEmpty(id, "id");

            FLOOR_FRAME_PROVIDER = new FloorFrameProvider<>(msDuration, frames,
                    snippet -> throwOnInvalidSnippetDefinition(snippet, "frameWithMs.getValue()"),
                    snippet -> snippet.image().supportsMouseEventCapturing());
        }

        @Override
        public int msDuration() {
            return FLOOR_FRAME_PROVIDER.MS_DURATION;
        }

        @Override
        public AnimationFrameSnippet snippetAtFrame(int ms) throws IllegalArgumentException {
            return FLOOR_FRAME_PROVIDER.valueAtFrame(ms);
        }

        @Override
        public boolean supportsMouseEventCapturing() {
            return FLOOR_FRAME_PROVIDER.SUPPORTS_MOUSE_EVENT_CAPTURING;
        }

        @Override
        public String id() throws IllegalStateException {
            return ID;
        }
    }
}
