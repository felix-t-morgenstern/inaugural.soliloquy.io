package inaugural.soliloquy.io.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.common.valueobjects.Pair;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.RectangleRenderable;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.function.Function;

import static inaugural.soliloquy.tools.collections.Collections.arrayOf;
import static inaugural.soliloquy.tools.valueobjects.FloatBox.intersection;
import static inaugural.soliloquy.tools.valueobjects.Vertex.distance;
import static org.lwjgl.opengl.GL11.*;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

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
                origBottomLeftColor
        );
        var intersectTopRightColor = getCornerColor(
                intersect,
                renderingDimensions,
                FloatBox::topRight,
                origTopLeftColor,
                origTopRightColor,
                origBottomRightColor,
                origBottomLeftColor
        );
        var intersectBottomRightColor = getCornerColor(
                intersect,
                renderingDimensions,
                FloatBox::bottomRight,
                origTopLeftColor,
                origTopRightColor,
                origBottomRightColor,
                origBottomLeftColor
        );
        var intersectBottomLeftColor = getCornerColor(
                intersect,
                renderingDimensions,
                FloatBox::bottomLeft,
                origTopLeftColor,
                origTopRightColor,
                origBottomRightColor,
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
                                 Color origBottomLeftColor) {
        if (getCorner.apply(intersect) != getCorner.apply(renderingDimensions)) {
            return triangulate(
                    getCorner.apply(intersect),
                    renderingDimensions,
                    origTopLeftColor,
                    origTopRightColor,
                    origBottomRightColor,
                    origBottomLeftColor);
        }
        else {
            return origTopLeftColor;
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
        var topLeftDist = distance(vertex, renderingDimens.topLeft());
        var topRightDist = distance(vertex, renderingDimens.topRight());
        var bottomLeftDist = distance(vertex, renderingDimens.bottomLeft());
        var bottomRightDist = distance(vertex, renderingDimens.bottomRight());

        var totalDist = topLeftDist + topRightDist + bottomLeftDist + bottomRightDist;

        var topLeftDistPercent = topLeftDist / totalDist;
        var topRightDistPercent = topRightDist / totalDist;
        var bottomLeftDistPercent = bottomLeftDist / totalDist;
        var bottomRightDistPercent = bottomRightDist / totalDist;

        var colorsAndWeights = arrayOf(
                pairOf(origTopLeftColor, topLeftDistPercent),
                pairOf(origTopRightColor, topRightDistPercent),
                pairOf(origBottomRightColor, bottomLeftDistPercent),
                pairOf(origBottomLeftColor, bottomRightDistPercent)
        );

        var r = getTriangulatedChannel(Color::getRed, colorsAndWeights);
        var g = getTriangulatedChannel(Color::getGreen, colorsAndWeights);
        var b = getTriangulatedChannel(Color::getBlue, colorsAndWeights);
        var a = getTriangulatedChannel(Color::getAlpha, colorsAndWeights);

        return new Color(r, g, b, a);
    }

    @SafeVarargs
    private static int getTriangulatedChannel(Function<Color, Integer> getChannel,
                                       Pair<Color, Float>... colorsAndPercents) {
        var weightedChannel = 0;

        for (var colorAndPercent : colorsAndPercents) {
            weightedChannel +=
                    (int) (getChannel.apply(colorAndPercent.FIRST) * colorAndPercent.SECOND);
        }

        return weightedChannel;
    }

    @Override
    protected String className() {
        return "RectangleRenderer";
    }
}
