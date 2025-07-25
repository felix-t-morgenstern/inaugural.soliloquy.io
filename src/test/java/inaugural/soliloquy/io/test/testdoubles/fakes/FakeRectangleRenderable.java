package inaugural.soliloquy.io.test.testdoubles.fakes;

import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.RectangleRenderable;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.ui.Component;
import soliloquy.specs.ui.EventInputs;

import java.awt.*;
import java.util.Map;
import java.util.UUID;

public class FakeRectangleRenderable implements RectangleRenderable {
    public ProviderAtTime<Color> TopLeftColorProvider;
    public ProviderAtTime<Color> TopRightColorProvider;
    public ProviderAtTime<Color> BottomRightColorProvider;
    public ProviderAtTime<Color> BottomLeftColorProvider;
    public ProviderAtTime<Integer> TextureIdProvider;
    public ProviderAtTime<Float> TextureTileWidth;
    public ProviderAtTime<Float> TextureTileHeight;
    public ProviderAtTime<FloatBox> RenderingDimensionsProvider;
    public UUID Uuid;

    public FakeRectangleRenderable(ProviderAtTime<Color> topLeftColorProvider,
                                   ProviderAtTime<Color> topRightColorProvider,
                                   ProviderAtTime<Color> bottomRightColorProvider,
                                   ProviderAtTime<Color> bottomLeftColorProvider,
                                   ProviderAtTime<Integer> backgroundTextureIdProvider,
                                   ProviderAtTime<Float> textureTileWidthProvider,
                                   ProviderAtTime<Float> textureTileHeightProvider,
                                   ProviderAtTime<FloatBox> renderingDimensionsProvider,
                                   UUID uuid) {
        TopLeftColorProvider = topLeftColorProvider;
        TopRightColorProvider = topRightColorProvider;
        BottomRightColorProvider = bottomRightColorProvider;
        BottomLeftColorProvider = bottomLeftColorProvider;
        TextureIdProvider = backgroundTextureIdProvider;
        TextureTileWidth = textureTileWidthProvider;
        TextureTileHeight = textureTileHeightProvider;
        RenderingDimensionsProvider = renderingDimensionsProvider;
        Uuid = uuid;
    }

    @Override
    public ProviderAtTime<Color> getTopLeftColorProvider() {
        return TopLeftColorProvider;
    }

    @Override
    public void setTopLeftColorProvider(ProviderAtTime<Color> topLeftColorProvider)
            throws IllegalArgumentException {
        TopLeftColorProvider = topLeftColorProvider;
    }

    @Override
    public ProviderAtTime<Color> getTopRightColorProvider() {
        return TopRightColorProvider;
    }

    @Override
    public void setTopRightColorProvider(ProviderAtTime<Color> topRightColorProvider)
            throws IllegalArgumentException {
        TopRightColorProvider = topRightColorProvider;
    }

    @Override
    public ProviderAtTime<Color> getBottomRightColorProvider() {
        return BottomRightColorProvider;
    }

    @Override
    public void setBottomRightColorProvider(ProviderAtTime<Color> bottomRightColorProvider)
            throws IllegalArgumentException {
        BottomRightColorProvider = bottomRightColorProvider;
    }

    @Override
    public ProviderAtTime<Color> getBottomLeftColorProvider() {
        return BottomLeftColorProvider;
    }

    @Override
    public void setBottomLeftColorProvider(ProviderAtTime<Color> bottomLeftColorProvider)
            throws IllegalArgumentException {
        BottomLeftColorProvider = bottomLeftColorProvider;
    }

    @Override
    public ProviderAtTime<Integer> getTextureIdProvider() {
        return TextureIdProvider;
    }

    @Override
    public void setTextureIdProvider(ProviderAtTime<Integer> backgroundTextureIdProvider)
            throws IllegalArgumentException {
        TextureIdProvider = backgroundTextureIdProvider;
    }

    @Override
    public ProviderAtTime<Float> getTextureTileWidthProvider() {
        return TextureTileWidth;
    }

    @Override
    public void setTextureTileWidthProvider(ProviderAtTime<Float> provider)
            throws IllegalArgumentException {
        TextureTileWidth = provider;
    }

    @Override
    public ProviderAtTime<Float> getTextureTileHeightProvider() {
        return TextureTileHeight;
    }

    @Override
    public void setTextureTileHeightProvider(ProviderAtTime<Float> provider)
            throws IllegalArgumentException {
        TextureTileHeight = provider;
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
    public void setOnPress(int i, Action<EventInputs> action)
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
    public void setOnRelease(int i, Action<EventInputs> action)
            throws IllegalArgumentException {

    }

    @Override
    public java.util.Map<Integer, String> releaseActionIds() {
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
    public Component component() {
        return null;
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
    public boolean isDeleted() {
        return false;
    }

    @Override
    public UUID uuid() {
        return Uuid;
    }

    @Override
    public boolean capturesMouseEventAtPoint(Vertex point, long timestamp)
            throws UnsupportedOperationException, IllegalArgumentException {
        return false;
    }
}
