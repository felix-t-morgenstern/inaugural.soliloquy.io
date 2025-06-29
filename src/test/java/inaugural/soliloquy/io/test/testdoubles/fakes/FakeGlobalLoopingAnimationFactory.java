package inaugural.soliloquy.io.test.testdoubles.fakes;

import soliloquy.specs.io.graphics.assets.Animation;
import soliloquy.specs.io.graphics.assets.GlobalLoopingAnimation;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.GlobalLoopingAnimationDefinition;
import soliloquy.specs.io.graphics.renderables.providers.factories.GlobalLoopingAnimationFactory;

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
