package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.assets.Animation;
import soliloquy.specs.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.graphics.renderables.FiniteAnimationRenderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.awt.*;
import java.util.List;

public class FakeFiniteAnimationRenderable implements FiniteAnimationRenderable {
    public Animation Animation;
    public List<ColorShift> ColorShifts;
    public ProviderAtTime<FloatBox> RenderingDimensionsProvider;
    public long StartTimestamp;
    public boolean Deleted;
    public EntityUuid Uuid;

    public FakeFiniteAnimationRenderable(Animation animation, List<ColorShift> colorShifts,
                                         ProviderAtTime<FloatBox> renderingDimensionsProvider,
                                         long startTimestamp, EntityUuid uuid) {
        Animation = animation;
        ColorShifts = colorShifts;
        RenderingDimensionsProvider = renderingDimensionsProvider;
        StartTimestamp = startTimestamp;
        Uuid = uuid;
    }

    @Override
    public AnimationFrameSnippet provide(long timestamp) throws IllegalArgumentException {
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
    public Long pausedTimestamp() {
        return null;
    }

    @Override
    public boolean getCapturesMouseEvents() {
        return false;
    }

    @Override
    public void setCapturesMouseEvents(boolean capturesMouseEvents)
            throws IllegalArgumentException {

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
    public ProviderAtTime<Float> getBorderThicknessProvider() {
        return null;
    }

    @Override
    public void setBorderThicknessProvider(ProviderAtTime<Float> providerAtTime) throws IllegalArgumentException {

    }

    @Override
    public ProviderAtTime<Color> getBorderColorProvider() {
        return null;
    }

    @Override
    public void setBorderColorProvider(ProviderAtTime<Color> providerAtTime) throws IllegalArgumentException {

    }

    @Override
    public ProviderAtTime<FloatBox> getRenderingDimensionsProvider() {
        return RenderingDimensionsProvider;
    }

    @Override
    public void setRenderingDimensionsProvider(ProviderAtTime<FloatBox>
                                                           renderingDimensionsProvider)
            throws IllegalArgumentException {

    }

    @Override
    public int getZ() {
        return 0;
    }

    @Override
    public void setZ(int z) {

    }

    @Override
    public void delete() {
        Deleted = true;
    }

    @Override
    public EntityUuid uuid() {
        return Uuid;
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

    @Override
    public AnimationFrameSnippet getArchetype() {
        return null;
    }
}
