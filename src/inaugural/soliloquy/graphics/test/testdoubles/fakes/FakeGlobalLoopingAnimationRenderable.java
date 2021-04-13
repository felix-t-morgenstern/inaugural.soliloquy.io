package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.GlobalLoopingAnimationRenderable;
import soliloquy.specs.graphics.renderables.RenderableAnimation;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.util.List;

public class FakeGlobalLoopingAnimationRenderable implements GlobalLoopingAnimationRenderable {
    public RenderableAnimation LoopingAnimation;
    public List<ColorShift> ColorShifts;
    public FloatBox RenderingArea;

    public FakeGlobalLoopingAnimationRenderable(RenderableAnimation loopingAnimation,
                                                List<ColorShift> colorShifts,
                                                FloatBox renderingArea) {
        LoopingAnimation = loopingAnimation;
        ColorShifts = colorShifts;
        RenderingArea = renderingArea;
    }

    @Override
    public RenderableAnimation loopingAnimation() {
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
    public FloatBox renderingArea() {
        return RenderingArea;
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
