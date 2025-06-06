package inaugural.soliloquy.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.renderables.RectangleRenderable;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class RectangleRenderer extends AbstractPointDrawingRenderer<RectangleRenderable>
        implements Renderer<RectangleRenderable> {
    public RectangleRenderer(Long mostRecentTimestamp) {
        super(mostRecentTimestamp);
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

        var renderingDimensions =
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
        drawPoint(renderingDimensions.LEFT_X, renderingDimensions.TOP_Y);

        setDrawColor(topRightColor);
        if (hasTexture) {
            glTexCoord2f(tilesPerWidth, 0f);
        }
        drawPoint(renderingDimensions.RIGHT_X, renderingDimensions.TOP_Y);

        setDrawColor(bottomRightColor);
        if (hasTexture) {
            glTexCoord2f(tilesPerWidth, tilesPerHeight);
        }
        drawPoint(renderingDimensions.RIGHT_X, renderingDimensions.BOTTOM_Y);

        setDrawColor(bottomLeftColor);
        if (hasTexture) {
            glTexCoord2f(0f, tilesPerHeight);
        }
        drawPoint(renderingDimensions.LEFT_X, renderingDimensions.BOTTOM_Y);

        glEnd();
    }

    @Override
    protected String className() {
        return "RectangleRenderer";
    }
}
