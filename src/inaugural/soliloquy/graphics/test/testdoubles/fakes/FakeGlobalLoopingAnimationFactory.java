package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.assets.Animation;
import soliloquy.specs.graphics.renderables.providers.GlobalLoopingAnimation;
import soliloquy.specs.graphics.renderables.providers.factories.GlobalLoopingAnimationFactory;

import java.util.ArrayList;

public class FakeGlobalLoopingAnimationFactory implements GlobalLoopingAnimationFactory {
    public ArrayList<String> InputIds = new ArrayList<>();
    public ArrayList<Animation> InputAnimations = new ArrayList<>();
    public ArrayList<Integer> InputPeriodModuloOffsets = new ArrayList<>();
    public ArrayList<GlobalLoopingAnimation> Outputs = new ArrayList<>();

    @Override
    public GlobalLoopingAnimation make(String id, Animation animation, int periodModuloOffset)
            throws IllegalArgumentException {
        InputIds.add(id);
        InputAnimations.add(animation);
        InputPeriodModuloOffsets.add(periodModuloOffset);

        FakeGlobalLoopingAnimation output = new FakeGlobalLoopingAnimation(id);
        Outputs.add(output);
        return output;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
