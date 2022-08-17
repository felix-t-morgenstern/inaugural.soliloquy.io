package inaugural.soliloquy.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.infrastructure.Pair;
import soliloquy.specs.graphics.renderables.TriangleRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.Map;
import java.util.UUID;

import static org.lwjgl.opengl.GL11.*;

public class TriangleRenderer extends AbstractPointDrawingRenderer<TriangleRenderable>
        implements Renderer<TriangleRenderable> {
    private static final TriangleRenderable ARCHETYPE = new TriangleRenderableArchetype();

    public TriangleRenderer(Long mostRecentTimestamp) {
        super(ARCHETYPE, mostRecentTimestamp);
    }

    @Override
    public void render(TriangleRenderable renderable, long timestamp)
            throws IllegalArgumentException {
        TIMESTAMP_VALIDATOR.validateTimestamp(timestamp);

        Check.ifNull(renderable, "renderable");

        Check.ifNull(renderable.getVertex1LocationProvider(),
                "renderable.getVertex1LocationProvider");
        Pair<Float, Float> vertex1 = renderable.getVertex1LocationProvider().provide(timestamp);
        Check.ifNull(vertex1, "provided vertex 1");
        Check.ifNull(vertex1.getItem1(), "x value of vertex 1");
        Check.ifNull(vertex1.getItem2(), "y value of vertex 1");

        Check.ifNull(renderable.getVertex1ColorProvider(), "renderable.getVertex1ColorProvider");
        Color color1 = renderable.getVertex1ColorProvider().provide(timestamp);

        Check.ifNull(renderable.getVertex2LocationProvider(),
                "renderable.getVertex2LocationProvider");
        Pair<Float, Float> vertex2 = renderable.getVertex2LocationProvider().provide(timestamp);
        Check.ifNull(vertex2, "provided vertex 2");
        Check.ifNull(vertex2.getItem1(), "x value of vertex 2");
        Check.ifNull(vertex2.getItem2(), "y value of vertex 2");

        Check.ifNull(renderable.getVertex2ColorProvider(), "renderable.getVertex2ColorProvider");
        Color color2 = renderable.getVertex2ColorProvider().provide(timestamp);

        Check.ifNull(renderable.getVertex3LocationProvider(),
                "renderable.getVertex3LocationProvider");
        Pair<Float, Float> vertex3 = renderable.getVertex3LocationProvider().provide(timestamp);
        Check.ifNull(vertex3, "provided vertex 3");
        Check.ifNull(vertex3.getItem1(), "x value of vertex 3");
        Check.ifNull(vertex3.getItem2(), "y value of vertex 3");

        Check.ifNull(renderable.getVertex3ColorProvider(), "renderable.getVertex3ColorProvider");
        Color color3 = renderable.getVertex3ColorProvider().provide(timestamp);

        Check.ifNull(renderable.getBackgroundTextureIdProvider(),
                "renderable.getBackgroundTextureIdProvider");
        Integer backgroundTextureId =
                renderable.getBackgroundTextureIdProvider().provide(timestamp);

        if (backgroundTextureId != null) {
            Check.throwOnLteZero(renderable.getBackgroundTextureTileWidth(),
                    "backgroundTextureTileWidth (with non-null backgroundTextureId)");
            Check.throwOnLteZero(renderable.getBackgroundTextureTileHeight(),
                    "backgroundTextureTileHeight (with non-null backgroundTextureId)");
        }
        else {
            Check.throwOnLtValue(renderable.getBackgroundTextureTileWidth(), 0f,
                    "backgroundTextureTileWidth (with null backgroundTextureId)");
            Check.throwOnLtValue(renderable.getBackgroundTextureTileHeight(), 0f,
                    "backgroundTextureTileHeight (with null backgroundTextureId)");
        }

        unbindMeshAndShader();

        float x1 = vertex1.getItem1();
        float y1 = vertex1.getItem2();
        float x2 = vertex2.getItem1();
        float y2 = vertex2.getItem2();
        float x3 = vertex3.getItem1();
        float y3 = vertex3.getItem2();

        float minX = 0f;
        float maxX = 0f;
        float minY = 0f;
        float maxY = 0f;
        float height = 0f;
        float width = 0f;
        float tilesPerWidth = 0f;
        float tilesPerHeight = 0f;

        if (backgroundTextureId != null) {
            maxX = Math.max(x3, Math.max(x2, x1));
            minX = Math.min(x3, Math.min(x2, x1));
            maxY = Math.max(y3, Math.max(y2, y1));
            minY = Math.min(y3, Math.min(y2, y1));

            width = maxX - minX;
            height = maxY - minY;

            tilesPerWidth = width / renderable.getBackgroundTextureTileWidth();
            tilesPerHeight = height / renderable.getBackgroundTextureTileHeight();

            glBindTexture(GL_TEXTURE_2D, backgroundTextureId);
        }

        glBegin(GL_TRIANGLES);

        setDrawColor(color1);
        if (backgroundTextureId != null) {
            glTexCoord2f(((x1 - minX) / width) * tilesPerWidth, ((y1 - minY) / height) * tilesPerHeight);
        }
        drawPoint(x1, y1);

        setDrawColor(color2);
        if (backgroundTextureId != null) {
            glTexCoord2f(((x2 - minX) / width) * tilesPerWidth, ((y2 - minY) / height) * tilesPerHeight);
        }
        drawPoint(x2, y2);

        setDrawColor(color3);
        if (backgroundTextureId != null) {
            glTexCoord2f(((x3 - minX) / width) * tilesPerWidth, ((y3 - minY) / height) * tilesPerHeight);
        }
        drawPoint(x3, y3);

        glEnd();
    }

    @Override
    protected String className() {
        return "TriangleRenderer";
    }

    private static class TriangleRenderableArchetype implements TriangleRenderable {
        @Override
        public ProviderAtTime<Pair<Float, Float>> getVertex1LocationProvider() {
            return null;
        }

        @Override
        public void setVertex1LocationProvider(ProviderAtTime<Pair<Float, Float>> providerAtTime)
                throws IllegalArgumentException {

        }

        @Override
        public ProviderAtTime<Color> getVertex1ColorProvider() {
            return null;
        }

        @Override
        public void setVertex1ColorProvider(ProviderAtTime<Color> providerAtTime)
                throws IllegalArgumentException {

        }

        @Override
        public ProviderAtTime<Pair<Float, Float>> getVertex2LocationProvider() {
            return null;
        }

        @Override
        public void setVertex2LocationProvider(ProviderAtTime<Pair<Float, Float>> providerAtTime)
                throws IllegalArgumentException {

        }

        @Override
        public ProviderAtTime<Color> getVertex2ColorProvider() {
            return null;
        }

        @Override
        public void setVertex2ColorProvider(ProviderAtTime<Color> providerAtTime)
                throws IllegalArgumentException {

        }

        @Override
        public ProviderAtTime<Pair<Float, Float>> getVertex3LocationProvider() {
            return null;
        }

        @Override
        public void setVertex3LocationProvider(ProviderAtTime<Pair<Float, Float>> providerAtTime)
                throws IllegalArgumentException {

        }

        @Override
        public ProviderAtTime<Color> getVertex3ColorProvider() {
            return null;
        }

        @Override
        public void setVertex3ColorProvider(ProviderAtTime<Color> providerAtTime)
                throws IllegalArgumentException {

        }

        @Override
        public ProviderAtTime<Integer> getBackgroundTextureIdProvider() {
            return null;
        }

        @Override
        public void setBackgroundTextureIdProvider(ProviderAtTime<Integer> providerAtTime)
                throws IllegalArgumentException {

        }

        @Override
        public float getBackgroundTextureTileWidth() {
            return 0;
        }

        @Override
        public void setBackgroundTextureTileWidth(float v) throws IllegalArgumentException {

        }

        @Override
        public float getBackgroundTextureTileHeight() {
            return 0;
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
        public void mouseOver(long l)
                throws UnsupportedOperationException, IllegalArgumentException {

        }

        @Override
        public void setOnMouseOver(Action<Long> action) {

        }

        @Override
        public String mouseOverActionId() {
            return null;
        }

        @Override
        public void mouseLeave(long l)
                throws UnsupportedOperationException, IllegalArgumentException {

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
            return TriangleRenderable.class.getCanonicalName();
        }
    }
}
