package inaugural.soliloquy.io.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.io.graphics.renderables.RectangleRenderable;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import static org.lwjgl.opengl.GL11.*;

public class RectangleRenderer extends AbstractPointDrawingRenderer<RectangleRenderable>
        implements Renderer<RectangleRenderable> {
    public RectangleRenderer(TimestampValidator timestampValidator) {
        super(timestampValidator);
    }

    @Override
    public void render(RectangleRenderable renderable, long timestamp)
            throws IllegalArgumentException {
        Check.ifNull(renderable, "renderable");

        Check.ifNull(renderable.getTopLeftColorProvider(),
                "renderable.getTopLeftColorProvider()");
        Check.ifNull(renderable.getTopRightColorProvider(),
                "renderable.getTopRightColorProvider()");
        Check.ifNull(renderable.getBottomRightColorProvider(),
                "renderable.getBottomRightColorProvider()");
        Check.ifNull(renderable.getBottomLeftColorProvider(),
                "renderable.getBottomLeftColorProvider()");

        Check.ifNull(renderable.getRenderingDimensionsProvider(),
                "renderable.getRenderingDimensionsProvider()");

        var renderingDimensions =
                renderable.getRenderingDimensionsProvider().provide(timestamp);

        Check.ifNull(renderable.getTextureIdProvider(), "renderable.getTextureIdProvider()");

        var backgroundTileTextureId = renderable.getTextureIdProvider().provide(timestamp);

        var hasTexture = false;

        float tilesPerWidth = 0f;
        float tilesPerHeight = 0f;
        if (backgroundTileTextureId != null) {
            glBindTexture(GL_TEXTURE_2D, backgroundTileTextureId);
            hasTexture = true;

            var textureTileWidth = Check.ifNull(renderable.getTextureTileWidthProvider(),
                    "renderable.getTextureTileWidthProvider()").provide(timestamp);
            var textureTileHeight = Check.ifNull(renderable.getTextureTileHeightProvider(),
                    "renderable.getTextureTileHeightProvider()").provide(timestamp);
            Check.throwOnLtValue(textureTileWidth, 0f, "provided textureTileWidth in renderable");
            Check.throwOnLtValue(textureTileHeight, 0f, "provided textureTileHeight in renderable");

            tilesPerWidth = renderingDimensions.width() / textureTileWidth;
            tilesPerHeight = renderingDimensions.height() / textureTileHeight;
        }

        Check.ifNull(renderingDimensions,
                "renderingDimensions provided by renderable.getRenderingDimensionsProvider()");

        Check.ifNull(renderable.uuid(), "renderable.uuid()");

        TIMESTAMP_VALIDATOR.validateTimestamp(this.getClass().getCanonicalName(), timestamp);

        unbindMeshAndShader();

        var topLeftColor = renderable.getTopLeftColorProvider().provide(timestamp);
        var topRightColor = renderable.getTopRightColorProvider().provide(timestamp);
        var bottomRightColor = renderable.getBottomRightColorProvider().provide(timestamp);
        var bottomLeftColor = renderable.getBottomLeftColorProvider().provide(timestamp);

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
