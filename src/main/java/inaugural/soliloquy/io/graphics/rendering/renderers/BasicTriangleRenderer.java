package inaugural.soliloquy.io.graphics.rendering.renderers;

// NB: This is different from the TriangleRenderer, as well as the TriangleSegmentRenderer. The
// TriangleRenderer renders a triangle shape as an object on the screen, by using the
// TriangleSegmentRenderer to determine the composite triangles it will have to render, which
// then uses the BasicTriangleRenderer to do the final drawing of triangles on-screen.
//
// In other words:
//
// [ Renderer ] (e.g., TriangleRenderer, AntialiasedLineSegmentRenderer)
//      ||
//      \/
// [ TriangleSegmentRenderer ]
//      ||
//      \/
// [ BasicTriangleRenderer ]
//
// An example: The TriangleRenderer takes a triangle in screen space and renders it within the
// RenderingBoundaries. When clipped by rectangular RenderingBoundaries, the "triangle" might
// need to be rendered as a hexagon. The TriangleRenderer figures out how the triangle looks when
// clipped by the RenderingBoundaries (including triangulating color and texture coordinates at
// various vertices), at which point it feeds that information (c.f. the Point record in this
// class) to this class to handle the actual drawing via OpenGL.

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.rendering.Mesh;
import soliloquy.specs.io.graphics.rendering.Shader;

import java.awt.*;

import static inaugural.soliloquy.io.api.Constants.MAX_CHANNEL_VAL;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;

public class BasicTriangleRenderer {
    private Mesh mesh;
    private Shader shader;

    public void draw(Point point1, Point point2, Point point3, Integer textureId) {
        boolean hasTexture;

        unbindMeshAndShader();

        if (textureId != null) {
            glEnable(GL_TEXTURE_2D);
            glBindTexture(GL_TEXTURE_2D, textureId);
            hasTexture = true;
        }
        else {
            hasTexture = false;
        }

        glBegin(GL_TRIANGLES);

        drawPoint(point1, hasTexture);
        drawPoint(point2, hasTexture);
        drawPoint(point3, hasTexture);

        glEnd();

        glDisable(GL_TEXTURE_2D);
    }

    private void unbindMeshAndShader() {
        if (mesh == null) {
            throw new IllegalStateException("BasicTriangleRenderer.draw: mesh cannot be null");
        }
        if (shader == null) {
            throw new IllegalStateException("BasicTriangleRenderer.draw: shader cannot be null");
        }

        mesh.unbind();
        shader.unbind();
    }

    private void drawPoint(Point point, boolean hasTexture) {
        setDrawColor(point.color);
        if (hasTexture) {
            glTexCoord2f(point.texCoordinate.X, point.texCoordinate.Y);
        }glVertex2f(point.loc().X, point.loc().Y);
    }

    private void setDrawColor(Color color) {
        if (color != null) {
            float[] rgba = color.getColorComponents(null);
            glColor4f(rgba[0], rgba[1], rgba[2], color.getAlpha() / MAX_CHANNEL_VAL);
        }
        else {
            glColor4f(0f, 0f, 0f, 0f);
        }
    }

    public void setMesh(Mesh mesh) throws IllegalArgumentException {
        this.mesh = Check.ifNull(mesh, "mesh");
    }

    public void setShader(Shader shader) throws IllegalArgumentException {
        this.shader = Check.ifNull(shader, "shader");
    }

    public record Point(Vertex loc, Color color, Vertex texCoordinate) {
        public static Point point(Vertex loc, Color color, Vertex texCoordinate) {
            return new Point(loc, color, texCoordinate);
        }
        public static Point point(Vertex loc, Color color) {
            return new Point(loc, color, null);
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Point(Vertex loc1, Color color1, Vertex texCoordinate1)) {
                return loc1 == loc && color1 == color && texCoordinate1 == texCoordinate;
            } else {
                return false;
            }
        }
    }
}
