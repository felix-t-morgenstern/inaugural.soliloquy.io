package inaugural.soliloquy.io.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.io.graphics.renderables.TriangleRenderable;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import static org.lwjgl.opengl.GL11.*;

public class TriangleRenderer extends AbstractPointDrawingRenderer<TriangleRenderable>
        implements Renderer<TriangleRenderable> {
    public TriangleRenderer(TimestampValidator timestampValidator) {
        super(timestampValidator);
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
        else {
            Check.throwOnLtValue(textureTileWidth, 0f, "textureTileWidth (with null textureId)");
            Check.throwOnLtValue(textureTileHeight, 0f, "textureTileHeight (with null textureId)");
        }

        unbindMeshAndShader();

        var x1 = vertex1.X;
        var y1 = vertex1.Y;
        var x2 = vertex2.X;
        var y2 = vertex2.Y;
        var x3 = vertex3.X;
        var y3 = vertex3.Y;

        var minX = 0f;
        float maxX;
        var minY = 0f;
        float maxY;
        var height = 0f;
        var width = 0f;
        var tilesPerWidth = 0f;
        var tilesPerHeight = 0f;

        glEnable(GL_TEXTURE_2D);

        if (textureId != null) {
            maxX = Math.max(x3, Math.max(x2, x1));
            minX = Math.min(x3, Math.min(x2, x1));
            maxY = Math.max(y3, Math.max(y2, y1));
            minY = Math.min(y3, Math.min(y2, y1));

            width = maxX - minX;
            height = maxY - minY;

            tilesPerWidth = width / textureTileWidth;
            tilesPerHeight = height / textureTileHeight;

            glBindTexture(GL_TEXTURE_2D, textureId);
        }

        glBegin(GL_TRIANGLES);

        setDrawColor(color1);
        if (textureId != null) {
            glTexCoord2f(((x1 - minX) / width) * tilesPerWidth,
                    ((y1 - minY) / height) * tilesPerHeight);
        }
        drawPoint(x1, y1);

        setDrawColor(color2);
        if (textureId != null) {
            glTexCoord2f(((x2 - minX) / width) * tilesPerWidth,
                    ((y2 - minY) / height) * tilesPerHeight);
        }
        drawPoint(x2, y2);

        setDrawColor(color3);
        if (textureId != null) {
            glTexCoord2f(((x3 - minX) / width) * tilesPerWidth,
                    ((y3 - minY) / height) * tilesPerHeight);
        }
        drawPoint(x3, y3);

        glEnd();

        glDisable(GL_TEXTURE_2D);
    }

    @Override
    protected String className() {
        return "TriangleRenderer";
    }
}
