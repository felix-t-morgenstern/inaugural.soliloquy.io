package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.renderables.TriangleRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.RenderableStack;

import java.awt.*;
import java.util.Map;
import java.util.UUID;

public class FakeTriangleRenderable implements TriangleRenderable {
    public ProviderAtTime<Vertex> Vertex1Provider;
    public ProviderAtTime<Color> Vertex1ColorProvider;
    public ProviderAtTime<Vertex> Vertex2Provider;
    public ProviderAtTime<Color> Vertex2ColorProvider;
    public ProviderAtTime<Vertex> Vertex3Provider;
    public ProviderAtTime<Color> Vertex3ColorProvider;
    public ProviderAtTime<Integer> BackgroundTextureIdProvider;
    public float BackgroundTextureTileWidth;
    public float BackgroundTextureTileHeight;

    public FakeTriangleRenderable(ProviderAtTime<Vertex> vertex1Provider,
                                  ProviderAtTime<Color> vertex1ColorProvider,
                                  ProviderAtTime<Vertex> vertex2Provider,
                                  ProviderAtTime<Color> vertex2ColorProvider,
                                  ProviderAtTime<Vertex> vertex3Provider,
                                  ProviderAtTime<Color> vertex3ColorProvider,
                                  ProviderAtTime<Integer> backgroundTextureIdProvider,
                                  float backgroundTextureTileWidth,
                                  float backgroundTextureTileHeight) {
        Vertex1Provider = vertex1Provider;
        Vertex1ColorProvider = vertex1ColorProvider;
        Vertex2Provider = vertex2Provider;
        Vertex2ColorProvider = vertex2ColorProvider;
        Vertex3Provider = vertex3Provider;
        Vertex3ColorProvider = vertex3ColorProvider;
        BackgroundTextureIdProvider = backgroundTextureIdProvider;
        BackgroundTextureTileWidth = backgroundTextureTileWidth;
        BackgroundTextureTileHeight = backgroundTextureTileHeight;
    }

    @Override
    public ProviderAtTime<Vertex> getVertex1Provider() {
        return Vertex1Provider;
    }

    @Override
    public void setVertex1Provider(ProviderAtTime<Vertex> providerAtTime)
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
    public ProviderAtTime<Vertex> getVertex2Provider() {
        return Vertex2Provider;
    }

    @Override
    public void setVertex2Provider(ProviderAtTime<Vertex> providerAtTime)
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
    public ProviderAtTime<Vertex> getVertex3Provider() {
        return Vertex3Provider;
    }

    @Override
    public void setVertex3Provider(ProviderAtTime<Vertex> providerAtTime)
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
    public boolean capturesMouseEventAtPoint(Vertex point, long l)
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
    public RenderableStack containingStack() {
        return null;
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
