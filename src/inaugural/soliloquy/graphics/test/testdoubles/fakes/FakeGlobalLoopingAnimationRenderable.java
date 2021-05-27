package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.common.entities.Action;
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
    public EntityUuid Id;

    public FakeGlobalLoopingAnimationRenderable(ProviderAtTime<AnimationFrameSnippet>
                                                        loopingAnimation,
                                                List<ColorShift> colorShifts,
                                                ProviderAtTime<FloatBox> renderingAreaProvider,
                                                EntityUuid id) {
        LoopingAnimation = loopingAnimation;
        ColorShifts = colorShifts;
        RenderingAreaProvider = renderingAreaProvider;
        Id = id;
    }

    @Override
    public ProviderAtTime<AnimationFrameSnippet> loopingAnimation() {
        return LoopingAnimation;
    }

    @Override
    public boolean getCapturesMouseEvents() {
        return false;
    }

    @Override
    public void setCapturesMouseEvents(boolean b) throws IllegalArgumentException {

    }

    @Override
    public void click() throws UnsupportedOperationException {

    }

    @Override
    public void setOnClick(Action action) {

    }

    @Override
    public void mouseOver() throws UnsupportedOperationException {

    }

    @Override
    public void setOnMouseOver(Action action) {

    }

    @Override
    public void mouseLeave() throws UnsupportedOperationException {

    }

    @Override
    public void setOnMouseLeave(Action action) {

    }

    @Override
    public List<ColorShift> colorShifts() {
        return ColorShifts;
    }

    @Override
    public ProviderAtTime<FloatBox> getRenderingAreaProvider() {
        return RenderingAreaProvider;
    }

    @Override
    public void setRenderingAreaProvider(ProviderAtTime<FloatBox> providerAtTime)
            throws IllegalArgumentException {

    }

    @Override
    public int getZ() {
        return 0;
    }

    @Override
    public void setZ(int i) {

    }

    @Override
    public void delete() {

    }

    @Override
    public EntityUuid uuid() {
        return Id;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
