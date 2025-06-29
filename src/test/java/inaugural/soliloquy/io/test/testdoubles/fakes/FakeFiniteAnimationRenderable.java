package inaugural.soliloquy.io.test.testdoubles.fakes;

import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.assets.Animation;
import soliloquy.specs.io.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.io.graphics.renderables.FiniteAnimationRenderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderableStack;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FakeFiniteAnimationRenderable implements FiniteAnimationRenderable {
    public Animation Animation;
    public List<ProviderAtTime<ColorShift>> ColorShiftProviders;
    public ProviderAtTime<FloatBox> RenderingDimensionsProvider;
    public long StartTimestamp;
    public boolean Deleted;
    public UUID Uuid;

    public FakeFiniteAnimationRenderable(Animation animation,
                                         List<ProviderAtTime<ColorShift>> colorShiftProviders,
                                         ProviderAtTime<FloatBox> renderingDimensionsProvider,
                                         long startTimestamp, UUID uuid) {
        Animation = animation;
        ColorShiftProviders = colorShiftProviders;
        RenderingDimensionsProvider = renderingDimensionsProvider;
        StartTimestamp = startTimestamp;
        Uuid = uuid;
    }

    @Override
    public AnimationFrameSnippet provide(long timestamp) throws IllegalArgumentException {
        int frameMs = (int) (timestamp - StartTimestamp);
        return Animation.snippetAtFrame(frameMs);
    }

    @Override
    public Object representation() {
        return null;
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
    public void press(int i, long l)
            throws UnsupportedOperationException, IllegalArgumentException {

    }

    @Override
    public void setOnPress(int i, Action<MouseEventInputs> action)
            throws IllegalArgumentException {

    }

    @Override
    public Map<Integer, String> pressActionIds() {
        return null;
    }

    @Override
    public void release(int i, long l)
            throws UnsupportedOperationException, IllegalArgumentException {

    }

    @Override
    public void setOnRelease(int i, Action<MouseEventInputs> action)
            throws IllegalArgumentException {

    }

    @Override
    public Map<Integer, String> releaseActionIds() {
        return null;
    }

    @Override
    public void mouseOver(long l) throws UnsupportedOperationException, IllegalArgumentException {

    }

    @Override
    public String mouseOverActionId() {
        return null;
    }

    @Override
    public void mouseLeave(long l) throws UnsupportedOperationException, IllegalArgumentException {

    }

    @Override
    public String mouseLeaveActionId() {
        return null;
    }

    @Override
    public void setOnMouseOver(Action action) {

    }

    @Override
    public void setOnMouseLeave(Action action) {

    }

    @Override
    public List<ProviderAtTime<ColorShift>> colorShiftProviders() {
        return ColorShiftProviders;
    }

    @Override
    public ProviderAtTime<Float> getBorderThicknessProvider() {
        return null;
    }

    @Override
    public void setBorderThicknessProvider(ProviderAtTime<Float> providerAtTime)
            throws IllegalArgumentException {

    }

    @Override
    public ProviderAtTime<Color> getBorderColorProvider() {
        return null;
    }

    @Override
    public void setBorderColorProvider(ProviderAtTime<Color> providerAtTime)
            throws IllegalArgumentException {

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
    public RenderableStack containingStack() {
        return null;
    }

    @Override
    public boolean capturesMouseEventAtPoint(Vertex point, long timestamp)
            throws UnsupportedOperationException, IllegalArgumentException {
        return false;
    }

    @Override
    public void delete() {
        Deleted = true;
    }

    @Override
    public UUID uuid() {
        return Uuid;
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
    public String animationId() {
        return Animation.id();
    }

    @Override
    public Long mostRecentTimestamp() {
        return null;
    }
}
