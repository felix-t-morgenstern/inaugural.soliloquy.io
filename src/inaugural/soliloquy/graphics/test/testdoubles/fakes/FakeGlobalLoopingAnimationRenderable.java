package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.common.entities.Action;
import soliloquy.specs.graphics.assets.GlobalLoopingAnimation;
import soliloquy.specs.graphics.renderables.GlobalLoopingAnimationRenderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.RenderableStack;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FakeGlobalLoopingAnimationRenderable implements GlobalLoopingAnimationRenderable {
    public GlobalLoopingAnimation GlobalLoopingAnimation;
    public List<ProviderAtTime<ColorShift>> ColorShiftProviders;
    public ProviderAtTime<FloatBox> RenderingDimensionsProvider;
    public UUID Uuid;

    public FakeGlobalLoopingAnimationRenderable(GlobalLoopingAnimation globalLoopingAnimation,
                                                List<ProviderAtTime<ColorShift>>
                                                        colorShiftProviders,
                                                ProviderAtTime<FloatBox>
                                                        renderingDimensionsProvider,
                                                UUID id) {
        GlobalLoopingAnimation = globalLoopingAnimation;
        ColorShiftProviders = colorShiftProviders;
        RenderingDimensionsProvider = renderingDimensionsProvider;
        Uuid = id;
    }

    @Override
    public GlobalLoopingAnimation getGlobalLoopingAnimation() {
        return GlobalLoopingAnimation;
    }

    @Override
    public void setGlobalLoopingAnimation(GlobalLoopingAnimation globalLoopingAnimation)
            throws IllegalArgumentException {
        GlobalLoopingAnimation = globalLoopingAnimation;
    }

    @Override
    public boolean getCapturesMouseEvents() {
        return false;
    }

    @Override
    public void setCapturesMouseEvents(boolean b) throws IllegalArgumentException {

    }

    @Override
    public void press(int i, long l)
            throws UnsupportedOperationException, IllegalArgumentException {

    }

    @Override
    public void setOnPress(int i, Action<Long> action) throws IllegalArgumentException {

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
    public void setOnRelease(int i, Action<Long> action) throws IllegalArgumentException {

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
    public void setZ(int i) {

    }

    @Override
    public RenderableStack containingStack() {
        return null;
    }

    @Override
    public boolean capturesMouseEventAtPoint(float x, float y, long timestamp)
            throws UnsupportedOperationException, IllegalArgumentException {
        return false;
    }

    @Override
    public void delete() {

    }

    @Override
    public UUID uuid() {
        return Uuid;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
