package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.assets.Animation;
import soliloquy.specs.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.graphics.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.FiniteAnimationRenderable;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.util.List;

public class FakeFiniteAnimationRenderable implements FiniteAnimationRenderable {
    public Animation Animation;
    public List<ColorShift> ColorShifts;
    public FloatBox RenderingArea;
    public long StartTimestamp;
    public boolean Deleted;

    public FakeFiniteAnimationRenderable(Animation animation, List<ColorShift> colorShifts,
                                         FloatBox renderingArea, long startTimestamp) {
        Animation = animation;
        ColorShifts = colorShifts;
        RenderingArea = renderingArea;
        StartTimestamp = startTimestamp;
    }

    @Override
    public AnimationFrameSnippet currentSnippet(long timestamp) throws IllegalArgumentException {
        int frameMs = (int)(timestamp - StartTimestamp);
        return Animation.snippetAtFrame(frameMs);
    }

    @Override
    public void reportPause(long l) throws IllegalArgumentException {

    }

    @Override
    public void reportUnpause(long l) throws IllegalArgumentException {

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
        Deleted = true;
    }

    @Override
    public EntityUuid id() {
        return null;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }

    @Override
    public long startTimestamp() {
        return StartTimestamp;
    }

    @Override
    public long endTimestamp() {
        return StartTimestamp + Animation.msDuration();
    }
}
