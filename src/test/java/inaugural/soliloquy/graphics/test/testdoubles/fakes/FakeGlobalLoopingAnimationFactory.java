package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.assets.Animation;
import soliloquy.specs.graphics.assets.GlobalLoopingAnimation;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.GlobalLoopingAnimationDefinition;
import soliloquy.specs.graphics.renderables.providers.factories.GlobalLoopingAnimationFactory;

import java.util.List;

import static inaugural.soliloquy.tools.collections.Collections.listOf;


public class FakeGlobalLoopingAnimationFactory implements GlobalLoopingAnimationFactory {
    public List<String> InputIds = listOf();
    public List<Animation> InputAnimations = listOf();
    public List<Integer> InputPeriodModuloOffsets = listOf();
    public List<GlobalLoopingAnimation> Outputs = listOf();

    @Override
    public GlobalLoopingAnimation make(GlobalLoopingAnimationDefinition definition)
            throws IllegalArgumentException {
        InputIds.add(definition.id());
        InputAnimations.add(definition.animation());
        InputPeriodModuloOffsets.add(definition.periodModuloOffset());

        FakeGlobalLoopingAnimation output = new FakeGlobalLoopingAnimation(definition.id());
        Outputs.add(output);
        return output;
    }
}
