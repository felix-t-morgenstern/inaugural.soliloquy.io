package inaugural.soliloquy.io.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.Renderable;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;

import java.awt.*;

import static inaugural.soliloquy.io.api.Constants.MAX_CHANNEL_VAL;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glVertex2f;

abstract class AbstractPointDrawingRenderer<TRenderable extends Renderable>
        extends AbstractRenderer<TRenderable> {
    protected final RenderingBoundaries RENDERING_BOUNDARIES;

    protected AbstractPointDrawingRenderer(TimestampValidator timestampValidator,
                                           RenderingBoundaries renderingBoundaries) {
        super(timestampValidator);
        RENDERING_BOUNDARIES = Check.ifNull(renderingBoundaries, "renderingBoundaries");
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

    protected void drawPoint(Vertex point) {
        glVertex2f(point.X, point.Y);
    }

    protected void drawPoint(float x, float y) {
        glVertex2f(x, y);
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
