package inaugural.soliloquy.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.graphics.renderables.RectangleRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.UUID;

import static inaugural.soliloquy.graphics.api.Constants.MAX_CHANNEL_VAL;
import static org.lwjgl.opengl.GL11.*;

public class RectangleRenderer extends AbstractRenderer<RectangleRenderable>
        implements Renderer<RectangleRenderable> {
    public RectangleRenderer(Long mostRecentTimestamp) {
        super(ARCHETYPE, mostRecentTimestamp);
    }

    @Override
    public void render(RectangleRenderable rectangleRenderable, long timestamp)
            throws IllegalArgumentException {
        Check.ifNull(rectangleRenderable, "rectangleRenderable");

        Check.ifNull(rectangleRenderable.getTopLeftColorProvider(),
                "rectangleRenderable.getTopLeftColorProvider()");
        Check.ifNull(rectangleRenderable.getTopRightColorProvider(),
                "rectangleRenderable.getTopRightColorProvider()");
        Check.ifNull(rectangleRenderable.getBottomRightColorProvider(),
                "rectangleRenderable.getBottomRightColorProvider()");
        Check.ifNull(rectangleRenderable.getBottomLeftColorProvider(),
                "rectangleRenderable.getBottomLeftColorProvider()");

        Check.ifNull(rectangleRenderable.getBackgroundTextureIdProvider(),
                "rectangleRenderable.getBackgroundTextureIdProvider()");

        Check.throwOnLtValue(rectangleRenderable.getBackgroundTextureTileWidth(), 0f,
                "rectangleRenderable.getBackgroundTextureTileWidth()");
        Check.throwOnLtValue(rectangleRenderable.getBackgroundTextureTileHeight(), 0f,
                "rectangleRenderable.getBackgroundTextureTileHeight()");

        Check.ifNull(rectangleRenderable.getRenderingDimensionsProvider(),
                "rectangleRenderable.getRenderingDimensionsProvider()");

        FloatBox renderingDimensions =
                rectangleRenderable.getRenderingDimensionsProvider().provide(timestamp);

        Check.ifNull(renderingDimensions, "renderingDimensions provided by " +
                "rectangleRenderable.getRenderingDimensionsProvider()");

        Check.ifNull(rectangleRenderable.uuid(), "rectangleRenderable.uuid()");

        TIMESTAMP_VALIDATOR.validateTimestamp(this.getClass().getCanonicalName(), timestamp);

        if (_mesh == null) {
            throw new IllegalStateException("RectangleRenderer.render: mesh cannot be null");
        }
        if (_shader == null) {
            throw new IllegalStateException("RectangleRenderer.render: shader cannot be null");
        }

        _mesh.unbind();
        _shader.unbind();

        float leftX = (renderingDimensions.leftX() * 2f) - 1f;
        float topY = -((renderingDimensions.topY() * 2f) - 1f);
        float rightX = (renderingDimensions.rightX() * 2f) - 1f;
        float bottomY = -((renderingDimensions.bottomY() * 2f) - 1f);

        float tilesPerWidth =
                renderingDimensions.width() / rectangleRenderable.getBackgroundTextureTileWidth();
        float tilesPerHeight =
                renderingDimensions.height() /
                        rectangleRenderable.getBackgroundTextureTileHeight();

        Color topLeftColor = rectangleRenderable.getTopLeftColorProvider().provide(timestamp);
        Color topRightColor = rectangleRenderable.getTopRightColorProvider().provide(timestamp);
        Color bottomRightColor =
                rectangleRenderable.getBottomRightColorProvider().provide(timestamp);
        Color bottomLeftColor =
                rectangleRenderable.getBottomLeftColorProvider().provide(timestamp);

        Integer backgroundTileTextureId =
                rectangleRenderable.getBackgroundTextureIdProvider().provide(timestamp);

        boolean hasTexture = false;

        if (backgroundTileTextureId != null) {
            glBindTexture(GL_TEXTURE_2D, backgroundTileTextureId);
            hasTexture = true;
        }

        glBegin(GL_QUADS);

        renderColor(topLeftColor);
        if (hasTexture) {
            glTexCoord2f(0f, 0f);
        }
        glVertex2f(leftX, topY);

        renderColor(topRightColor);
        if (hasTexture) {
            glTexCoord2f(tilesPerWidth, 0f);
        }
        glVertex2f(rightX, topY);

        renderColor(bottomRightColor);
        if (hasTexture) {
            glTexCoord2f(tilesPerWidth, tilesPerHeight);
        }
        glVertex2f(rightX, bottomY);

        renderColor(bottomLeftColor);
        if (hasTexture) {
            glTexCoord2f(0f, tilesPerHeight);
        }
        glVertex2f(leftX, bottomY);

        glEnd();
    }

    // TODO: Duplicate within AntialiasedLineSegmentRenderer
    private void renderColor(Color color) {
        if (color != null) {
            float[] rgba = color.getColorComponents(null);
            glColor4f(rgba[0], rgba[1], rgba[2], color.getAlpha() / MAX_CHANNEL_VAL);
        }
        else {
            glColor4f(1f, 1f, 1f, 1f);
        }
    }

    private static final RectangleRenderable ARCHETYPE = new RectangleRenderable() {
        @Override
        public ProviderAtTime<Color> getTopLeftColorProvider() {
            return null;
        }

        @Override
        public void setTopLeftColorProvider(ProviderAtTime<Color> providerAtTime)
                throws IllegalArgumentException {

        }

        @Override
        public ProviderAtTime<Color> getTopRightColorProvider() {
            return null;
        }

        @Override
        public void setTopRightColorProvider(ProviderAtTime<Color> providerAtTime)
                throws IllegalArgumentException {

        }

        @Override
        public ProviderAtTime<Color> getBottomRightColorProvider() {
            return null;
        }

        @Override
        public void setBottomRightColorProvider(ProviderAtTime<Color> providerAtTime)
                throws IllegalArgumentException {

        }

        @Override
        public ProviderAtTime<Color> getBottomLeftColorProvider() {
            return null;
        }

        @Override
        public void setBottomLeftColorProvider(ProviderAtTime<Color> providerAtTime)
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
        public java.util.Map<Integer, String> pressActionIds() {
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
        public java.util.Map<Integer, String> releaseActionIds() {
            return null;
        }

        @Override
        public void mouseOver(long l)
                throws UnsupportedOperationException, IllegalArgumentException {

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
            return null;
        }

        @Override
        public void setRenderingDimensionsProvider(ProviderAtTime<FloatBox> providerAtTime)
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
        public void delete() {

        }

        @Override
        public UUID uuid() {
            return null;
        }

        @Override
        public String getInterfaceName() {
            return RectangleRenderable.class.getCanonicalName();
        }
    };
}
