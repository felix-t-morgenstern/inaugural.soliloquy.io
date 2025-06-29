package inaugural.soliloquy.io.test.testdoubles.fakes;

import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.ImageAssetRenderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderableStack;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.listOf;

public class FakeImageAssetRenderable implements ImageAssetRenderable {
    public boolean CapturesMouseEvents;
    public boolean CapturesMouseEventsAtPoint = true;
    public List<Vertex> CapturesMouseEventsAtPointInputLocations = listOf();
    public List<Long> CapturesMouseEventsAtPointInputTimestamps = listOf();
    public FloatBox RenderingDimensions;
    public ProviderAtTime<FloatBox> RenderingDimensionsProvider = new FakeProviderAtTime<>();
    public int Z;

    @Override
    public boolean getCapturesMouseEvents() {
        return CapturesMouseEvents;
    }

    @Override
    public void setCapturesMouseEvents(boolean b) throws IllegalArgumentException {

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
    public void setOnMouseOver(Action<MouseEventInputs> action) {

    }

    @Override
    public String mouseOverActionId() {
        return null;
    }

    @Override
    public void mouseLeave(long l) throws UnsupportedOperationException, IllegalArgumentException {

    }

    @Override
    public void setOnMouseLeave(Action<MouseEventInputs> action) {

    }

    @Override
    public String mouseLeaveActionId() {
        return null;
    }

    @Override
    public List<ProviderAtTime<ColorShift>> colorShiftProviders() {
        return null;
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
        RenderingDimensionsProvider = renderingDimensionsProvider;
    }

    @Override
    public int getZ() {
        return Z;
    }

    @Override
    public void setZ(int z) {
        Z = z;
    }

    @Override
    public RenderableStack containingStack() {
        return null;
    }

    @Override
    public boolean capturesMouseEventAtPoint(Vertex point, long timestamp)
            throws UnsupportedOperationException, IllegalArgumentException {
        CapturesMouseEventsAtPointInputLocations.add(point);
        CapturesMouseEventsAtPointInputTimestamps.add(timestamp);
        return CapturesMouseEventsAtPoint;
    }

    @Override
    public void delete() {

    }

    @Override
    public UUID uuid() {
        return null;
    }
}
