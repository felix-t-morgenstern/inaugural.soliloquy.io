package inaugural.soliloquy.io.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.io.graphics.renderables.RectangleRenderable;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class RectangleRenderer extends AbstractPointDrawingRenderer<RectangleRenderable>
        implements Renderer<RectangleRenderable> {
    public RectangleRenderer(Long mostRecentTimestamp) {
        super(mostRecentTimestamp);
    }

    @Override
    public void render(RectangleRenderable renderable, long timestamp)
            throws IllegalArgumentException {
        Check.ifNull(renderable, "renderable");

        Check.ifNull(renderable.getTopLeftColorProvider(), "renderable.getTopLeftColorProvider()");
        Check.ifNull(renderable.getTopRightColorProvider(),
                "renderable.getTopRightColorProvider()");
        Check.ifNull(renderable.getBottomRightColorProvider(),
                "renderable.getBottomRightColorProvider()");
        Check.ifNull(renderable.getBottomLeftColorProvider(),
                "renderable.getBottomLeftColorProvider()");

        Check.ifNull(renderable.getTextureIdProvider(), "renderable.getTextureIdProvider()");

        Check.throwOnLtValue(renderable.getTextureTileWidth(), 0f,
                "renderable.getTextureTileWidth()");
        Check.throwOnLtValue(renderable.getTextureTileHeight(), 0f,
                "renderable.getTextureTileHeight()");

        Check.ifNull(renderable.getRenderingDimensionsProvider(),
                "renderable.getRenderingDimensionsProvider()");

        var renderingDimensions = renderable.getRenderingDimensionsProvider().provide(timestamp);

        Check.ifNull(renderingDimensions,
                "renderingDimensions provided by renderable.getRenderingDimensionsProvider()");

        Check.ifNull(renderable.uuid(), "renderable.uuid()");

        TIMESTAMP_VALIDATOR.validateTimestamp(this.getClass().getCanonicalName(), timestamp);

        unbindMeshAndShader();

        var tilesPerWidth = renderingDimensions.width() / renderable.getTextureTileWidth();
        var tilesPerHeight =
                renderingDimensions.height() / renderable.getTextureTileHeight();

        var topLeftColor = renderable.getTopLeftColorProvider().provide(timestamp);
        var topRightColor = renderable.getTopRightColorProvider().provide(timestamp);
        var bottomRightColor = renderable.getBottomRightColorProvider().provide(timestamp);
        var bottomLeftColor = renderable.getBottomLeftColorProvider().provide(timestamp);

        var backgroundTileTextureId = renderable.getTextureIdProvider().provide(timestamp);

        var hasTexture = false;

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
