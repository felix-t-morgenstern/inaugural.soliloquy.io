package inaugural.soliloquy.io.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.RectangleRenderable;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.function.Function;

import static inaugural.soliloquy.tools.valueobjects.FloatBox.intersection;
import static org.lwjgl.opengl.GL11.*;

public class RectangleRenderer extends AbstractPointDrawingRenderer<RectangleRenderable>
        implements Renderer<RectangleRenderable> {
    public RectangleRenderer(TimestampValidator timestampValidator,
                             RenderingBoundaries renderingBoundaries) {
        super(timestampValidator, renderingBoundaries);
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

        var renderingDimensions = renderable.getRenderingDimensionsProvider().provide(timestamp);
        Check.ifNull(renderingDimensions, "dimensions provided by renderable");

        var renderingBoundaries = RENDERING_BOUNDARIES.currentBoundaries();
        var intersect = intersection(renderingDimensions, renderingBoundaries);
        if (intersect == null) {
            return;
        }

        var origTopLeftColor = renderable.getTopLeftColorProvider().provide(timestamp);
        var origTopRightColor = renderable.getTopRightColorProvider().provide(timestamp);
        var origBottomRightColor = renderable.getBottomRightColorProvider().provide(timestamp);
        var origBottomLeftColor = renderable.getBottomLeftColorProvider().provide(timestamp);

        var intersectTopLeftColor = getCornerColor(
                intersect,
                renderingDimensions,
                FloatBox::topLeft,
                origTopLeftColor,
                origTopRightColor,
                origBottomRightColor,
                origBottomLeftColor,
                origTopLeftColor
        );
        var intersectTopRightColor = getCornerColor(
                intersect,
                renderingDimensions,
                FloatBox::topRight,
                origTopLeftColor,
                origTopRightColor,
                origBottomRightColor,
                origBottomLeftColor,
                origTopRightColor
        );
        var intersectBottomRightColor = getCornerColor(
                intersect,
                renderingDimensions,
                FloatBox::bottomRight,
                origTopLeftColor,
                origTopRightColor,
                origBottomRightColor,
                origBottomLeftColor,
                origBottomRightColor
        );
        var intersectBottomLeftColor = getCornerColor(
                intersect,
                renderingDimensions,
                FloatBox::bottomLeft,
                origTopLeftColor,
                origTopRightColor,
                origBottomRightColor,
                origBottomLeftColor,
                origBottomLeftColor
        );

        var texId =
                Check.ifNull(renderable.getTextureIdProvider(), "renderable.getTextureIdProvider()")
                        .provide(timestamp);

        unbindMeshAndShader();

        var hasTexture = false;

        var tilesPerWidth = 0f;
        var tilesPerHeight = 0f;
        if (texId != null) {
            glEnable(GL_TEXTURE_2D);
            glBindTexture(GL_TEXTURE_2D, texId);
            hasTexture = true;

            var texTileWidth = Check.ifNull(renderable.getTextureTileWidthProvider(),
                    "renderable.getTextureTileWidthProvider()").provide(timestamp);
            var texTileHeight = Check.ifNull(renderable.getTextureTileHeightProvider(),
                    "renderable.getTextureTileHeightProvider()").provide(timestamp);

            Check.throwOnLtValue(texTileWidth, 0f, "provided texTileWidth in renderable");
            Check.throwOnLtValue(texTileHeight, 0f, "provided texTileHeight in renderable");

            tilesPerWidth = renderingDimensions.width() / texTileWidth;
            tilesPerHeight = renderingDimensions.height() / texTileHeight;
        }

        Check.ifNull(renderingDimensions,
                "renderingDimensions provided by renderable.getRenderingDimensionsProvider()");

        Check.ifNull(renderable.uuid(), "renderable.uuid()");

        TIMESTAMP_VALIDATOR.validateTimestamp(this.getClass().getCanonicalName(), timestamp);

        glBegin(GL_QUADS);

        setDrawColor(intersectTopLeftColor);
        if (hasTexture) {
            glTexCoord2f(0f, 0f);
        }
        drawPoint(intersect.LEFT_X, intersect.TOP_Y);

        setDrawColor(intersectTopRightColor);
        if (hasTexture) {
            glTexCoord2f(tilesPerWidth, 0f);
        }
        drawPoint(intersect.RIGHT_X, intersect.TOP_Y);

        setDrawColor(intersectBottomRightColor);
        if (hasTexture) {
            glTexCoord2f(tilesPerWidth, tilesPerHeight);
        }
        drawPoint(intersect.RIGHT_X, intersect.BOTTOM_Y);

        setDrawColor(intersectBottomLeftColor);
        if (hasTexture) {
            glTexCoord2f(0f, tilesPerHeight);
        }
        drawPoint(intersect.LEFT_X, intersect.BOTTOM_Y);

        glEnd();

        glDisable(GL_TEXTURE_2D);
    }

    private Color getCornerColor(FloatBox intersect,
                                 FloatBox renderingDimensions,
                                 Function<FloatBox, Vertex> getCorner,
                                 Color origTopLeftColor,
                                 Color origTopRightColor,
                                 Color origBottomRightColor,
                                 Color origBottomLeftColor,
                                 Color origCornerColor) {
        if (!getCorner.apply(intersect).equals(getCorner.apply(renderingDimensions))) {
            return triangulate(
                    getCorner.apply(intersect),
                    renderingDimensions,
                    origTopLeftColor,
                    origTopRightColor,
                    origBottomRightColor,
                    origBottomLeftColor);
        }
        else {
            return origCornerColor;
        }
    }

    private Color triangulate(
            Vertex vertex,
            FloatBox renderingDimens,
            Color origTopLeftColor,
            Color origTopRightColor,
            Color origBottomRightColor,
            Color origBottomLeftColor) {
        if (origTopLeftColor == null || origTopRightColor == null || origBottomRightColor == null || origBottomLeftColor == null) {
            return null;
        }

        var horizPosition = (vertex.X - renderingDimens.LEFT_X) / renderingDimens.width();
        var vertPosition = (vertex.Y - renderingDimens.TOP_Y) / renderingDimens.height();

        var r = 0f;
        var g = 0f;
        var b = 0f;
        var a = 0f;

        var topLeftPercent = (1f - horizPosition) * (1f - vertPosition);
        r += topLeftPercent * (float)origTopLeftColor.getRed();
        g += topLeftPercent * (float)origTopLeftColor.getGreen();
        b += topLeftPercent * (float)origTopLeftColor.getBlue();
        a += topLeftPercent * (float)origTopLeftColor.getAlpha();

        var topRightPercent = horizPosition * (1f - vertPosition);
        r += topRightPercent * (float)origTopRightColor.getRed();
        g += topRightPercent * (float)origTopRightColor.getGreen();
        b += topRightPercent * (float)origTopRightColor.getBlue();
        a += topRightPercent * (float)origTopRightColor.getAlpha();

        var bottomRightPercent = horizPosition * vertPosition;
        r += bottomRightPercent * (float)origBottomRightColor.getRed();
        g += bottomRightPercent * (float)origBottomRightColor.getGreen();
        b += bottomRightPercent * (float)origBottomRightColor.getBlue();
        a += bottomRightPercent * (float)origBottomRightColor.getAlpha();

        var bottomLeftPercent = (1f - horizPosition) * vertPosition;
        r += bottomLeftPercent * (float)origBottomLeftColor.getRed();
        g += bottomLeftPercent * (float)origBottomLeftColor.getGreen();
        b += bottomLeftPercent * (float)origBottomLeftColor.getBlue();
        a += bottomLeftPercent * (float)origBottomLeftColor.getAlpha();

        return new Color(
                Math.min((int)r + 1, 255),
                Math.min((int)g + 1, 255),
                Math.min((int)b + 1, 255),
                Math.min((int)a + 1, 255)
        );
    }

    @Override
    protected String className() {
        return "RectangleRenderer";
    }
}
