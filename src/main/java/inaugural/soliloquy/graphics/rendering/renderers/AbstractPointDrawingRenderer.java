package inaugural.soliloquy.graphics.rendering.renderers;

import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.renderables.Renderable;

import java.awt.*;

import static inaugural.soliloquy.graphics.api.Constants.MAX_CHANNEL_VAL;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glVertex2f;

abstract class AbstractPointDrawingRenderer<TRenderable extends Renderable>
        extends AbstractRenderer<TRenderable> {
    protected AbstractPointDrawingRenderer(TRenderable archetype, Long mostRecentTimestamp) {
        super(archetype, mostRecentTimestamp);
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
        drawPoint(point.x, point.y);
    }

    protected void drawPoint(float x, float y) {
        glVertex2f((x * 2f) - 1f, 1f - (y * 2f));
    }

    protected void unbindMeshAndShader() {
        if (_mesh == null) {
            throw new IllegalStateException(className() + ".render: mesh cannot be null");
        }
        if (_shader == null) {
            throw new IllegalStateException(className() + ".render: shader cannot be null");
        }

        _mesh.unbind();
        _shader.unbind();
    }

    protected abstract String className();
}
