package inaugural.soliloquy.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.graphics.renderables.RectangleRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.RenderableStack;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.Map;
import java.util.UUID;

import static org.lwjgl.opengl.GL11.*;

public class RectangleRenderer extends AbstractPointDrawingRenderer<RectangleRenderable>
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

        unbindMeshAndShader();

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

        setDrawColor(topLeftColor);
        if (hasTexture) {
            glTexCoord2f(0f, 0f);
        }
        drawPoint(renderingDimensions.leftX(), renderingDimensions.topY());

        setDrawColor(topRightColor);
        if (hasTexture) {
            glTexCoord2f(tilesPerWidth, 0f);
        }
        drawPoint(renderingDimensions.rightX(), renderingDimensions.topY());

        setDrawColor(bottomRightColor);
        if (hasTexture) {
            glTexCoord2f(tilesPerWidth, tilesPerHeight);
        }
        drawPoint(renderingDimensions.rightX(), renderingDimensions.bottomY());

        setDrawColor(bottomLeftColor);
        if (hasTexture) {
            glTexCoord2f(0f, tilesPerHeight);
        }
        drawPoint(renderingDimensions.leftX(), renderingDimensions.bottomY());

        glEnd();
    }

    private static final RectangleRenderable ARCHETYPE = new RectangleRenderable() {
        @Override
        public UUID uuid() {
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
        public ProviderAtTime<FloatBox> getRenderingDimensionsProvider() {
            return null;
        }

        @Override
        public void setRenderingDimensionsProvider(ProviderAtTime<FloatBox> providerAtTime)
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
        public String getInterfaceName() {
            return RectangleRenderable.class.getCanonicalName();
        }
    };

    @Override
    protected String className() {
        return "RectangleRenderer";
    }
}
