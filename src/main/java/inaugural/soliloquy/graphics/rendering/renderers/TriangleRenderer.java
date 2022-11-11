package inaugural.soliloquy.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.renderables.TriangleRenderable;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.awt.*;

import static inaugural.soliloquy.tools.generic.Archetypes.generateSimpleArchetype;
import static org.lwjgl.opengl.GL11.*;

public class TriangleRenderer extends AbstractPointDrawingRenderer<TriangleRenderable>
        implements Renderer<TriangleRenderable> {
    public TriangleRenderer(Long mostRecentTimestamp) {
        super(generateSimpleArchetype(TriangleRenderable.class), mostRecentTimestamp);
    }

    @Override
    public void render(TriangleRenderable renderable, long timestamp)
            throws IllegalArgumentException {
        TIMESTAMP_VALIDATOR.validateTimestamp(timestamp);

        Check.ifNull(renderable, "renderable");

        Check.ifNull(renderable.getVertex1Provider(),
                "renderable.getVertex1Provider");
        Vertex vertex1 = renderable.getVertex1Provider().provide(timestamp);
        Check.ifNull(vertex1, "provided vertex 1");

        Check.ifNull(renderable.getVertex1ColorProvider(), "renderable.getVertex1ColorProvider");
        Color color1 = renderable.getVertex1ColorProvider().provide(timestamp);

        Check.ifNull(renderable.getVertex2Provider(),
                "renderable.getVertex2Provider");
        Vertex vertex2 = renderable.getVertex2Provider().provide(timestamp);
        Check.ifNull(vertex2, "provided vertex 2");

        Check.ifNull(renderable.getVertex2ColorProvider(), "renderable.getVertex2ColorProvider");
        Color color2 = renderable.getVertex2ColorProvider().provide(timestamp);

        Check.ifNull(renderable.getVertex3Provider(),
                "renderable.getVertex3Provider");
        Vertex vertex3 = renderable.getVertex3Provider().provide(timestamp);
        Check.ifNull(vertex3, "provided vertex 3");

        Check.ifNull(renderable.getVertex3ColorProvider(), "renderable.getVertex3ColorProvider");
        Color color3 = renderable.getVertex3ColorProvider().provide(timestamp);

        Check.ifNull(renderable.getBackgroundTextureIdProvider(),
                "renderable.getBackgroundTextureIdProvider");
        Integer backgroundTextureId =
                renderable.getBackgroundTextureIdProvider().provide(timestamp);

        if (backgroundTextureId != null) {
            Check.throwOnLteZero(renderable.getBackgroundTextureTileWidth(),
                    "backgroundTextureTileWidth (with non-null backgroundTextureId)");
            Check.throwOnLteZero(renderable.getBackgroundTextureTileHeight(),
                    "backgroundTextureTileHeight (with non-null backgroundTextureId)");
        }
        else {
            Check.throwOnLtValue(renderable.getBackgroundTextureTileWidth(), 0f,
                    "backgroundTextureTileWidth (with null backgroundTextureId)");
            Check.throwOnLtValue(renderable.getBackgroundTextureTileHeight(), 0f,
                    "backgroundTextureTileHeight (with null backgroundTextureId)");
        }

        unbindMeshAndShader();

        float x1 = vertex1.X;
        float y1 = vertex1.Y;
        float x2 = vertex2.X;
        float y2 = vertex2.Y;
        float x3 = vertex3.X;
        float y3 = vertex3.Y;

        float minX = 0f;
        float maxX;
        float minY = 0f;
        float maxY;
        float height = 0f;
        float width = 0f;
        float tilesPerWidth = 0f;
        float tilesPerHeight = 0f;

        if (backgroundTextureId != null) {
            maxX = Math.max(x3, Math.max(x2, x1));
            minX = Math.min(x3, Math.min(x2, x1));
            maxY = Math.max(y3, Math.max(y2, y1));
            minY = Math.min(y3, Math.min(y2, y1));

            width = maxX - minX;
            height = maxY - minY;

            tilesPerWidth = width / renderable.getBackgroundTextureTileWidth();
            tilesPerHeight = height / renderable.getBackgroundTextureTileHeight();

            glBindTexture(GL_TEXTURE_2D, backgroundTextureId);
        }

        glBegin(GL_TRIANGLES);

        setDrawColor(color1);
        if (backgroundTextureId != null) {
            glTexCoord2f(((x1 - minX) / width) * tilesPerWidth,
                    ((y1 - minY) / height) * tilesPerHeight);
        }
        drawPoint(x1, y1);

        setDrawColor(color2);
        if (backgroundTextureId != null) {
            glTexCoord2f(((x2 - minX) / width) * tilesPerWidth,
                    ((y2 - minY) / height) * tilesPerHeight);
        }
        drawPoint(x2, y2);

        setDrawColor(color3);
        if (backgroundTextureId != null) {
            glTexCoord2f(((x3 - minX) / width) * tilesPerWidth,
                    ((y3 - minY) / height) * tilesPerHeight);
        }
        drawPoint(x3, y3);

        glEnd();
    }

    @Override
    protected String className() {
        return "TriangleRenderer";
    }
}
