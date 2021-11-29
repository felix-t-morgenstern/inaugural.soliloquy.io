package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import inaugural.soliloquy.common.test.fakes.FakePair;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.infrastructure.Pair;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.RenderableWithArea;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FakeRenderableWithArea implements RenderableWithArea {
    public boolean CapturesMouseEvents;
    public boolean CapturesMouseEventsAtPoint = true;
    public ArrayList<Pair<Float, Float>> CapturesMouseEventsAtPointInputLocations =
            new ArrayList<>();
    public ArrayList<Long> CapturesMouseEventsAtPointInputTimestamps = new ArrayList<>();
    public FakeFloatBox RenderingDimensions;
    public ProviderAtTime<FloatBox> RenderingDimensionsProvider =
            new FakeStaticProvider<>(RenderingDimensions);
    public int Z;

    @Override
    public boolean getCapturesMouseEvents() {
        return CapturesMouseEvents;
    }

    @Override
    public void setCapturesMouseEvents(boolean b) throws IllegalArgumentException {

    }

    @Override
    public void press(int i, long l) throws UnsupportedOperationException, IllegalArgumentException {

    }

    @Override
    public void setOnPress(int i, Action<Long> action) throws IllegalArgumentException {

    }

    @Override
    public Map<Integer, String> pressActionIds() {
        return null;
    }

    @Override
    public void release(int i, long l) throws UnsupportedOperationException, IllegalArgumentException {

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
    public void setOnMouseOver(Action<Long> action) {

    }

    @Override
    public String mouseOverActionId() {
        return null;
    }

    @Override
    public void mouseLeave(long l) throws UnsupportedOperationException, IllegalArgumentException {

    }

    @Override
    public void setOnMouseLeave(Action<Long> action) {

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
    public boolean capturesMouseEventAtPoint(float x, float y, long timestamp)
            throws UnsupportedOperationException, IllegalArgumentException {
        CapturesMouseEventsAtPointInputLocations.add(new FakePair<>(x, y));
        CapturesMouseEventsAtPointInputTimestamps.add(timestamp);
        return CapturesMouseEventsAtPoint;
    }

    @Override
    public void delete() {

    }

    @Override
    public EntityUuid uuid() {
        return null;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
