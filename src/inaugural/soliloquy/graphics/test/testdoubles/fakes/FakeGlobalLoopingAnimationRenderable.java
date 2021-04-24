package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.graphics.renderables.GlobalLoopingAnimationRenderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.util.List;

public class FakeGlobalLoopingAnimationRenderable implements GlobalLoopingAnimationRenderable {
    public ProviderAtTime<AnimationFrameSnippet> LoopingAnimation;
    public List<ColorShift> ColorShifts;
    public ProviderAtTime<FloatBox> RenderingAreaProvider;

    public FakeGlobalLoopingAnimationRenderable(ProviderAtTime<AnimationFrameSnippet>
                                                        loopingAnimation,
                                                List<ColorShift> colorShifts,
                                                ProviderAtTime<FloatBox> renderingAreaProvider) {
        LoopingAnimation = loopingAnimation;
        ColorShifts = colorShifts;
        RenderingAreaProvider = renderingAreaProvider;
    }

    @Override
    public ProviderAtTime<AnimationFrameSnippet> loopingAnimation() {
        return LoopingAnimation;
    }

    @Override
    public boolean capturesMouseEvents() {
        return false;
    }

    @Override
    public void click() throws UnsupportedOperationException {

    }

    @Override
    public void mouseOver() throws UnsupportedOperationException {

    }

    @Override
    public void mouseLeave() throws UnsupportedOperationException {

    }

    @Override
    public List<ColorShift> colorShifts() {
        return ColorShifts;
    }

    @Override
    public ProviderAtTime<FloatBox> renderingAreaProvider() {
        return RenderingAreaProvider;
    }

    @Override
    public int z() {
        return 0;
    }

    @Override
    public void delete() {

    }

    @Override
    public EntityUuid id() {
        return null;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
