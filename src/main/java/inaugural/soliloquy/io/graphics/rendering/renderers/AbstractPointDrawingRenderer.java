package inaugural.soliloquy.io.graphics.rendering.renderers;

import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.Renderable;

import java.awt.*;

import static inaugural.soliloquy.io.api.Constants.MAX_CHANNEL_VAL;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glVertex2f;

abstract class AbstractPointDrawingRenderer<TRenderable extends Renderable>
        extends AbstractRenderer<TRenderable> {
    protected AbstractPointDrawingRenderer(Long mostRecentTimestamp) {
        super(mostRecentTimestamp);
    }

    protected void setDrawColor(Color color) {
        if (color != null) {
            float[] rgba = color.getColorComponents(null);
            glColor4f(rgba[0], rgba[1], rgba[2], color.getAlpha() / MAX_CHANNEL_VAL);
        }
        else {
            glColor4f(1f, 1f, 1f, 1f);
        }
    }

    protected Color transparent(Color color) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), 0);
    }

    protected void drawPoint(Vertex point) {
        drawPoint(point.X, point.Y);
    }

    protected void drawPoint(float x, float y) {
        glVertex2f((x * 2f) - 1f, 1f - (y * 2f));
    }

    protected void unbindMeshAndShader() {
        if (mesh == null) {
            throw new IllegalStateException(className() + ".render: mesh cannot be null");
        }
        if (shader == null) {
            throw new IllegalStateException(className() + ".render: shader cannot be null");
        }

        mesh.unbind();
        shader.unbind();
    }

    protected abstract String className();
}
