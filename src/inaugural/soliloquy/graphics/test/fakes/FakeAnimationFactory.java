package inaugural.soliloquy.graphics.test.fakes;

import soliloquy.specs.graphics.assets.Animation;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.AnimationDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeAnimationFactory implements AssetFactory<AnimationDefinition, Animation> {
    public final Map<String, AnimationDefinition> INPUTS = new HashMap<>();
    public final List<Animation> OUTPUTS = new ArrayList<>();

    @Override
    public Animation create(AnimationDefinition animationDefinition) throws IllegalArgumentException {
        INPUTS.put(animationDefinition.assetId(), animationDefinition);
        Animation createdAnimation = new FakeAnimation(animationDefinition.assetId());
        OUTPUTS.add(createdAnimation);
        return createdAnimation;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
