package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.infrastructure.Pair;
import soliloquy.specs.graphics.renderables.TriangleRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;

import java.awt.*;
import java.util.Map;
import java.util.UUID;

public class FakeTriangleRenderable implements TriangleRenderable {
    public ProviderAtTime<Pair<Float, Float>> Vertex1LocationProvider;
    public ProviderAtTime<Color> Vertex1ColorProvider;
    public ProviderAtTime<Pair<Float, Float>> Vertex2LocationProvider;
    public ProviderAtTime<Color> Vertex2ColorProvider;
    public ProviderAtTime<Pair<Float, Float>> Vertex3LocationProvider;
    public ProviderAtTime<Color> Vertex3ColorProvider;
    public ProviderAtTime<Integer> BackgroundTextureIdProvider;
    public float BackgroundTextureTileWidth;
    public float BackgroundTextureTileHeight;

    public FakeTriangleRenderable(ProviderAtTime<Pair<Float, Float>> vertex1LocationProvider,
                                  ProviderAtTime<Color> vertex1ColorProvider,
                                  ProviderAtTime<Pair<Float, Float>> vertex2LocationProvider,
                                  ProviderAtTime<Color> vertex2ColorProvider,
                                  ProviderAtTime<Pair<Float, Float>> vertex3LocationProvider,
                                  ProviderAtTime<Color> vertex3ColorProvider,
                                  ProviderAtTime<Integer> backgroundTextureIdProvider,
                                  float backgroundTextureTileWidth,
                                  float backgroundTextureTileHeight) {
        Vertex1LocationProvider = vertex1LocationProvider;
        Vertex1ColorProvider = vertex1ColorProvider;
        Vertex2LocationProvider = vertex2LocationProvider;
        Vertex2ColorProvider = vertex2ColorProvider;
        Vertex3LocationProvider = vertex3LocationProvider;
        Vertex3ColorProvider = vertex3ColorProvider;
        BackgroundTextureIdProvider = backgroundTextureIdProvider;
        BackgroundTextureTileWidth = backgroundTextureTileWidth;
        BackgroundTextureTileHeight = backgroundTextureTileHeight;
    }

    @Override
    public ProviderAtTime<Pair<Float, Float>> getVertex1LocationProvider() {
        return Vertex1LocationProvider;
    }

    @Override
    public void setVertex1LocationProvider(ProviderAtTime<Pair<Float, Float>> providerAtTime)
            throws IllegalArgumentException {

    }

    @Override
    public ProviderAtTime<Color> getVertex1ColorProvider() {
        return Vertex1ColorProvider;
    }

    @Override
    public void setVertex1ColorProvider(ProviderAtTime<Color> providerAtTime)
            throws IllegalArgumentException {

    }

    @Override
    public ProviderAtTime<Pair<Float, Float>> getVertex2LocationProvider() {
        return Vertex2LocationProvider;
    }

    @Override
    public void setVertex2LocationProvider(ProviderAtTime<Pair<Float, Float>> providerAtTime)
            throws IllegalArgumentException {

    }

    @Override
    public ProviderAtTime<Color> getVertex2ColorProvider() {
        return Vertex2ColorProvider;
    }

    @Override
    public void setVertex2ColorProvider(ProviderAtTime<Color> providerAtTime)
            throws IllegalArgumentException {

    }

    @Override
    public ProviderAtTime<Pair<Float, Float>> getVertex3LocationProvider() {
        return Vertex3LocationProvider;
    }

    @Override
    public void setVertex3LocationProvider(ProviderAtTime<Pair<Float, Float>> providerAtTime)
            throws IllegalArgumentException {

    }

    @Override
    public ProviderAtTime<Color> getVertex3ColorProvider() {
        return Vertex3ColorProvider;
    }

    @Override
    public void setVertex3ColorProvider(ProviderAtTime<Color> providerAtTime)
            throws IllegalArgumentException {

    }

    @Override
    public ProviderAtTime<Integer> getBackgroundTextureIdProvider() {
        return BackgroundTextureIdProvider;
    }

    @Override
    public void setBackgroundTextureIdProvider(ProviderAtTime<Integer> providerAtTime)
            throws IllegalArgumentException {

    }

    @Override
    public float getBackgroundTextureTileWidth() {
        return BackgroundTextureTileWidth;
    }

    @Override
    public void setBackgroundTextureTileWidth(float v) throws IllegalArgumentException {

    }

    @Override
    public float getBackgroundTextureTileHeight() {
        return BackgroundTextureTileHeight;
    }

    @Override
    public void setBackgroundTextureTileHeight(float v) throws IllegalArgumentException {

    }

    @Override
    public boolean getCapturesMouseEvents() {
        return false;
    }

    @Override
    public void setCapturesMouseEvents(boolean b) throws IllegalArgumentException {

    }

    @Override
    public boolean capturesMouseEventAtPoint(float v, float v1, long l)
            throws UnsupportedOperationException, IllegalArgumentException {
        return false;
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
    public UUID uuid() {
        return null;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
