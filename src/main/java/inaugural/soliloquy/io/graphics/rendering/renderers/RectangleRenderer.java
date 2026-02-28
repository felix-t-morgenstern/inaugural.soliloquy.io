package inaugural.soliloquy.io.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.io.graphics.renderables.RectangleRenderable;
import soliloquy.specs.io.graphics.rendering.Mesh;
import soliloquy.specs.io.graphics.rendering.Shader;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

// NB: Using BasicTriangleRenderer instead of TriangleSegmentRenderer would likely be a bit more
// performance-light, but wouldn't triangulate colors properly for rectangles intersecting with
// the rendering boundaries. Consider making the swap back to BasicTriangleRenderer if
// performance is a high concern, and maintaining color gradients for rectangles outside of
// rendering boundaries is not.
public class RectangleRenderer implements Renderer<RectangleRenderable> {
    private final TimestampValidator TIMESTAMP_VALIDATOR;
    private final TriangleSegmentRenderer TRIANGLE_SEGMENT_RENDERER;

    public RectangleRenderer(TimestampValidator timestampValidator,
                             TriangleSegmentRenderer triangleSegmentRenderer) {
        TIMESTAMP_VALIDATOR = Check.ifNull(timestampValidator, "timestampValidator");
        TRIANGLE_SEGMENT_RENDERER =
                Check.ifNull(triangleSegmentRenderer, "triangleSegmentRenderer");
    }

    @Override
    public void setMesh(Mesh mesh) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setShader(Shader shader) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void render(RectangleRenderable renderable, long timestamp)
            throws IllegalArgumentException {
        var renderingDimensions = renderable.getRenderingDimensionsProvider().provide(timestamp);

        var origTopLeftColor = renderable.getTopLeftColorProvider().provide(timestamp);
        var origTopRightColor = renderable.getTopRightColorProvider().provide(timestamp);
        var origBottomRightColor = renderable.getBottomRightColorProvider().provide(timestamp);
        var origBottomLeftColor = renderable.getBottomLeftColorProvider().provide(timestamp);

        var texId = renderable.getTextureIdProvider().provide(timestamp);

        Float texTileWidth = null;
        Float texTileHeight = null;
        if (texId != null) {
            texTileWidth = renderable.getTextureTileWidthProvider().provide(timestamp);
            texTileHeight = renderable.getTextureTileHeightProvider().provide(timestamp);
        }

        TIMESTAMP_VALIDATOR.validateTimestamp(this.getClass().getCanonicalName(), timestamp);

        TRIANGLE_SEGMENT_RENDERER.draw(
                renderingDimensions.topLeft(),
                origTopLeftColor,
                renderingDimensions.topRight(),
                origTopRightColor,
                renderingDimensions.bottomRight(),
                origBottomRightColor,
                texId,
                texTileWidth,
                texTileHeight
        );
        TRIANGLE_SEGMENT_RENDERER.draw(
                renderingDimensions.topLeft(),
                origTopLeftColor,
                renderingDimensions.bottomLeft(),
                origBottomLeftColor,
                renderingDimensions.bottomRight(),
                origBottomRightColor,
                texId,
                texTileWidth,
                texTileHeight
        );
    }
}
