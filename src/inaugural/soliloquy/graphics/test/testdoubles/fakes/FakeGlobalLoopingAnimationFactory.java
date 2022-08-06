package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.assets.Animation;
import soliloquy.specs.graphics.assets.GlobalLoopingAnimation;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.GlobalLoopingAnimationDefinition;
import soliloquy.specs.graphics.renderables.providers.factories.GlobalLoopingAnimationFactory;

import java.util.ArrayList;

public class FakeGlobalLoopingAnimationFactory implements GlobalLoopingAnimationFactory {
    public ArrayList<String> InputIds = new ArrayList<>();
    public ArrayList<Animation> InputAnimations = new ArrayList<>();
    public ArrayList<Integer> InputPeriodModuloOffsets = new ArrayList<>();
    public ArrayList<GlobalLoopingAnimation> Outputs = new ArrayList<>();

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

    @Override
    public String getInterfaceName() {
        return null;
    }
}
