package inaugural.soliloquy.io.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.io.graphics.renderables.TriangleRenderable;
import soliloquy.specs.io.graphics.rendering.Mesh;
import soliloquy.specs.io.graphics.rendering.Shader;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

public class TriangleRenderer implements Renderer<TriangleRenderable> {
    private final TimestampValidator TIMESTAMP_VALIDATOR;
    private final TriangleSegmentRenderer TRIANGLE_SEGMENT_RENDERER;

    public TriangleRenderer(TimestampValidator timestampValidator,
                            TriangleSegmentRenderer triangleSegmentRenderer) {
        TIMESTAMP_VALIDATOR = Check.ifNull(timestampValidator, "timestampValidator");
        TRIANGLE_SEGMENT_RENDERER =
                Check.ifNull(triangleSegmentRenderer, "triangleSegmentRenderer");
    }

    @Override
    public void render(TriangleRenderable renderable, long timestamp)
            throws IllegalArgumentException {
        TIMESTAMP_VALIDATOR.validateTimestamp(this.getClass().getCanonicalName(), timestamp);

        var vertex1 = renderable.getVertex1Provider().provide(timestamp);
        var color1 = renderable.getVertex1ColorProvider().provide(timestamp);
        var vertex2 = renderable.getVertex2Provider().provide(timestamp);
        var color2 = renderable.getVertex2ColorProvider().provide(timestamp);
        var vertex3 = renderable.getVertex3Provider().provide(timestamp);
        var color3 = renderable.getVertex3ColorProvider().provide(timestamp);

        var texId = renderable.getTextureIdProvider().provide(timestamp);

        Float texTileWidth = null;
        Float texXOffset = null;
        Float texTileHeight = null;
        Float texYOffset = null;
        if (texId != null) {
            texTileWidth = renderable.getTextureTilesPerWidthProvider().provide(timestamp);
            texXOffset = renderable.getTextureXOffsetProvider().provide(timestamp);
            texTileHeight = renderable.getTextureTilesPerHeightProvider().provide(timestamp);
            texYOffset = renderable.getTextureYOffsetProvider().provide(timestamp);
        }

        TRIANGLE_SEGMENT_RENDERER.draw(
                vertex1,
                color1,
                vertex2,
                color2,
                vertex3,
                color3,
                texId,
                texTileWidth,
                texXOffset,
                texTileHeight,
                texYOffset
        );
    }

    @Override
    public void setMesh(Mesh mesh) throws IllegalArgumentException {
        throw new UnsupportedOperationException("");
    }

    @Override
    public void setShader(Shader shader) throws IllegalArgumentException {
        throw new UnsupportedOperationException("");
    }
}
