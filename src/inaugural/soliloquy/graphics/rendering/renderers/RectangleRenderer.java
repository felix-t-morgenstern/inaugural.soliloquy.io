package inaugural.soliloquy.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.infrastructure.Map;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.RectangleRenderable;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.Mesh;
import soliloquy.specs.graphics.rendering.Shader;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.List;

import static inaugural.soliloquy.graphics.api.Constants.MAX_CHANNEL_VAL;
import static org.lwjgl.opengl.GL11.*;

public class RectangleRenderer extends AbstractRenderer<RectangleRenderable>
        implements Renderer<RectangleRenderable> {

    public RectangleRenderer() {
        super(ARCHETYPE);
    }

    @Override
    public void setMesh(Mesh mesh) throws IllegalArgumentException {
        throw new UnsupportedOperationException(
                "RectangleRenderer.setMesh: this renderer does not require a Mesh");
    }

    @Override
    public void setShader(Shader shader) throws IllegalArgumentException {
        throw new UnsupportedOperationException(
                "RectangleRenderer.setShader: this renderer does not require a Shader");
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

        if (rectangleRenderable.getBorderThicknessProvider() != null &&
                rectangleRenderable.getBorderColorProvider() == null) {
            throw new IllegalArgumentException(
                    "RectangleRenderer.render: rectangleRenderer with non-null " +
                            "borderThicknessProvider must have non-null borderColorProvider");
        }

        Check.ifNull(rectangleRenderable.getRenderingDimensionsProvider(),
                "rectangleRenderable.getRenderingDimensionsProvider()");

        FloatBox renderingDimensions =
                rectangleRenderable.getRenderingDimensionsProvider().provide(timestamp);

        Check.ifNull(renderingDimensions, "renderingDimensions provided by " +
                "rectangleRenderable.getRenderingDimensionsProvider()");

        Check.ifNull(rectangleRenderable.uuid(), "rectangleRenderable.uuid()");

        validateTimestamp(timestamp, "RectangleRenderer");

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

    private void renderColor(Color color) {
        if (color != null) {
            float[] topLeftRgba = getColorComponents(color);
            glColor4f(topLeftRgba[0], topLeftRgba[1], topLeftRgba[2], topLeftRgba[3]);
        }
        else {
            glColor4f(1f, 1f, 1f, 1f);
        }
    }

    // TODO: Consider moving this to Tools (it is also used in SpriteRenderer)
    private float[] getColorComponents(Color color) {
        if (color == null) {
            return new float[] { 0f, 0f, 0f, 0f };
        }
        return new float[] {
                color.getRed() / MAX_CHANNEL_VAL,
                color.getGreen() / MAX_CHANNEL_VAL,
                color.getBlue() / MAX_CHANNEL_VAL,
                color.getAlpha() / MAX_CHANNEL_VAL
        };
    }

    private static final RectangleRenderable ARCHETYPE = new RectangleRenderable() {
        @Override
        public ProviderAtTime<Color> getTopLeftColorProvider() {
            return null;
        }

        @Override
        public void setTopLeftColorProvider(ProviderAtTime<Color> providerAtTime) throws IllegalArgumentException {

        }

        @Override
        public ProviderAtTime<Color> getTopRightColorProvider() {
            return null;
        }

        @Override
        public void setTopRightColorProvider(ProviderAtTime<Color> providerAtTime) throws IllegalArgumentException {

        }

        @Override
        public ProviderAtTime<Color> getBottomRightColorProvider() {
            return null;
        }

        @Override
        public void setBottomRightColorProvider(ProviderAtTime<Color> providerAtTime) throws IllegalArgumentException {

        }

        @Override
        public ProviderAtTime<Color> getBottomLeftColorProvider() {
            return null;
        }

        @Override
        public void setBottomLeftColorProvider(ProviderAtTime<Color> providerAtTime) throws IllegalArgumentException {

        }

        @Override
        public ProviderAtTime<Integer> getBackgroundTextureIdProvider() {
            return null;
        }

        @Override
        public void setBackgroundTextureIdProvider(ProviderAtTime<Integer> providerAtTime) throws IllegalArgumentException {

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
        public void click() throws UnsupportedOperationException {

        }

        @Override
        public void setOnClick(Action action) {

        }

        @Override
        public void mouseOver() throws UnsupportedOperationException {

        }

        @Override
        public void setOnMouseOver(Action action) {

        }

        @Override
        public void mouseLeave() throws UnsupportedOperationException {

        }

        @Override
        public void setOnMouseLeave(Action action) {

        }

        @Override
        public List<ColorShift> colorShifts() {
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
            return null;
        }

        @Override
        public void setRenderingDimensionsProvider(ProviderAtTime<FloatBox> providerAtTime) throws IllegalArgumentException {

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
        public EntityUuid uuid() {
            return null;
        }

        @Override
        public void clearContainedRenderables() {

        }

        @Override
        public void add(Renderable renderable) throws IllegalArgumentException {

        }

        @Override
        public Map<Integer, soliloquy.specs.common.infrastructure.List<Renderable>> snapshot() {
            return null;
        }

        @Override
        public String getInterfaceName() {
            return RectangleRenderable.class.getCanonicalName();
        }
    };
}
