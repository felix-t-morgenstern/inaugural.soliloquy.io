package inaugural.soliloquy.io.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.io.graphics.renderables.TriangleRenderable;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

public class TriangleRenderer extends AbstractPointDrawingRenderer<TriangleRenderable>
        implements Renderer<TriangleRenderable> {
    private final TriangleSegmentRenderer TRIANGLE_SEGMENT_RENDERER;

    public TriangleRenderer(TimestampValidator timestampValidator,
                            RenderingBoundaries renderingBoundaries,
                            TriangleSegmentRenderer triangleSegmentRenderer) {
        super(timestampValidator, renderingBoundaries);
        TRIANGLE_SEGMENT_RENDERER =
                Check.ifNull(triangleSegmentRenderer, "triangleSegmentRenderer");
    }

    @Override
    public void render(TriangleRenderable renderable, long timestamp)
            throws IllegalArgumentException {
        TIMESTAMP_VALIDATOR.validateTimestamp(this.getClass().getCanonicalName(), timestamp);

        Check.ifNull(renderable, "renderable");

        Check.ifNull(renderable.getVertex1Provider(),
                "renderable.getVertex1Provider");
        var vertex1 = renderable.getVertex1Provider().provide(timestamp);
        Check.ifNull(vertex1, "provided vertex 1");

        Check.ifNull(renderable.getVertex1ColorProvider(), "renderable.getVertex1ColorProvider");
        var color1 = renderable.getVertex1ColorProvider().provide(timestamp);

        Check.ifNull(renderable.getVertex2Provider(),
                "renderable.getVertex2Provider");
        var vertex2 = renderable.getVertex2Provider().provide(timestamp);
        Check.ifNull(vertex2, "provided vertex 2");

        Check.ifNull(renderable.getVertex2ColorProvider(), "renderable.getVertex2ColorProvider");
        var color2 = renderable.getVertex2ColorProvider().provide(timestamp);

        Check.ifNull(renderable.getVertex3Provider(), "renderable.getVertex3Provider");
        var vertex3 = renderable.getVertex3Provider().provide(timestamp);
        Check.ifNull(vertex3, "provided vertex 3");

        Check.ifNull(renderable.getVertex3ColorProvider(), "renderable.getVertex3ColorProvider");
        var color3 = renderable.getVertex3ColorProvider().provide(timestamp);

        Check.ifNull(renderable.getTextureIdProvider(), "renderable.getTextureIdProvider");
        var textureId = renderable.getTextureIdProvider().provide(timestamp);

        var textureTileWidth = Check.ifNull(renderable.getTextureTileWidthProvider(),
                "renderable.getTextureTileWidthProvider()").provide(timestamp);
        var textureTileHeight = Check.ifNull(renderable.getTextureTileHeightProvider(),
                "renderable.getTextureTileHeightProvider()").provide(timestamp);

        if (textureId != null) {
            Check.throwOnLteZero(textureTileWidth, "textureTileWidth (with non-null textureId)");
            Check.throwOnLteZero(textureTileHeight, "textureTileHeight (with non-null textureId)");
        }

        TRIANGLE_SEGMENT_RENDERER.draw(
                vertex1,
                color1,
                vertex2,
                color2,
                vertex3,
                color3,
                textureId,
                textureTileWidth,
                textureTileHeight
        );
    }

    @Override
    protected String className() {
        return "TriangleRenderer";
    }
}
