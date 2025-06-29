package inaugural.soliloquy.io.test.testdoubles.fakes;

import soliloquy.specs.io.graphics.assets.Animation;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.AnimationDefinition;

import java.util.List;
import java.util.Map;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;

public class FakeAnimationFactory implements AssetFactory<AnimationDefinition, Animation> {
    public final Map<String, AnimationDefinition> INPUTS = mapOf();
    public final List<Animation> OUTPUTS = listOf();

    @Override
    public Animation make(AnimationDefinition animationDefinition) throws IllegalArgumentException {
        INPUTS.put(animationDefinition.id(), animationDefinition);
        var createdAnimation = new FakeAnimation(animationDefinition.id());
        OUTPUTS.add(createdAnimation);
        return createdAnimation;
    }
}
