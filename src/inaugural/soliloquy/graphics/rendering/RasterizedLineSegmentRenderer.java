package inaugural.soliloquy.graphics.rendering;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.renderables.RasterizedLineSegmentRenderable;
import soliloquy.specs.graphics.rendering.RendererType;

import static org.lwjgl.opengl.GL11.*;

public class RasterizedLineSegmentRenderer implements RendererType<RasterizedLineSegmentRenderable> {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void render(RasterizedLineSegmentRenderable rasterizedLineSegmentRenderable)
            throws IllegalArgumentException {
        Check.ifNull(rasterizedLineSegmentRenderable, "rasterizedLineSegmentRenderable");

        Check.throwOnLteZero(rasterizedLineSegmentRenderable.thickness(),
                "rasterizedLineSegmentRenderable.thickness()");

        Check.throwOnEqualsValue(rasterizedLineSegmentRenderable.stipplePattern(), (short)0x0000,
                "rasterizedLineSegmentRenderable.stipplePattern()");

        Check.throwOnLtValue(rasterizedLineSegmentRenderable.stippleFactor(), (short)1,
                "rasterizedLineSegmentRenderable.stippleFactor()");
        Check.throwOnGtValue(rasterizedLineSegmentRenderable.stippleFactor(), (short)256,
                "rasterizedLineSegmentRenderable.stippleFactor()");

        Check.throwOnLtValue(rasterizedLineSegmentRenderable.red(), 0f,
                "rasterizedLineSegmentRenderable.red()");
        Check.throwOnGtValue(rasterizedLineSegmentRenderable.red(), 1f,
                "rasterizedLineSegmentRenderable.red()");
        Check.throwOnLtValue(rasterizedLineSegmentRenderable.green(), 0f,
                "rasterizedLineSegmentRenderable.green()");
        Check.throwOnGtValue(rasterizedLineSegmentRenderable.green(), 1f,
                "rasterizedLineSegmentRenderable.green()");
        Check.throwOnLtValue(rasterizedLineSegmentRenderable.blue(), 0f,
                "rasterizedLineSegmentRenderable.blue()");
        Check.throwOnGtValue(rasterizedLineSegmentRenderable.blue(), 1f,
                "rasterizedLineSegmentRenderable.blue()");
        Check.throwOnLtValue(rasterizedLineSegmentRenderable.alpha(), 0f,
                "rasterizedLineSegmentRenderable.alpha()");
        Check.throwOnGtValue(rasterizedLineSegmentRenderable.alpha(), 1f,
                "rasterizedLineSegmentRenderable.alpha()");

        Check.throwOnLtValue(rasterizedLineSegmentRenderable.xLoc(), -1f,
                "rasterizedLineSegmentRenderable.xLoc()");
        Check.throwOnGtValue(rasterizedLineSegmentRenderable.xLoc(), 1f,
                "rasterizedLineSegmentRenderable.xLoc()");

        Check.throwOnLtValue(rasterizedLineSegmentRenderable.yLoc(), -1f,
                "rasterizedLineSegmentRenderable.yLoc()");
        Check.throwOnGtValue(rasterizedLineSegmentRenderable.yLoc(), 1f,
                "rasterizedLineSegmentRenderable.yLoc()");

        if (rasterizedLineSegmentRenderable.xLoc() + rasterizedLineSegmentRenderable.width()
                > 1f) {
            throw new IllegalArgumentException("RasterizedLineSegmentRenderer.render: xLoc (" +
                    rasterizedLineSegmentRenderable.xLoc() + ") plus width (" +
                    rasterizedLineSegmentRenderable.width() + ") exceed maximum boundary of " +
                    "window (1.0)");
        }
        if (rasterizedLineSegmentRenderable.xLoc() + rasterizedLineSegmentRenderable.width()
                < -1f) {
            throw new IllegalArgumentException("RasterizedLineSegmentRenderer.render: xLoc (" +
                    rasterizedLineSegmentRenderable.xLoc() + ") plus width (" +
                    rasterizedLineSegmentRenderable.width() + ") exceed minimum boundary of " +
                    "window (-1.0)");
        }

        if (rasterizedLineSegmentRenderable.yLoc() + rasterizedLineSegmentRenderable.height()
                > 1f) {
            throw new IllegalArgumentException("RasterizedLineSegmentRenderer.render: yLoc (" +
                    rasterizedLineSegmentRenderable.yLoc() + ") plus height (" +
                    rasterizedLineSegmentRenderable.height() + ") exceed maximum boundary of " +
                    "window (1.0)");
        }

        if (rasterizedLineSegmentRenderable.yLoc() + rasterizedLineSegmentRenderable.height()
                < -1f) {
            throw new IllegalArgumentException("RasterizedLineSegmentRenderer.render: yLoc (" +
                    rasterizedLineSegmentRenderable.yLoc() + ") plus height (" +
                    rasterizedLineSegmentRenderable.height() + ") exceed minimum boundary of " +
                    "window (-1.0)");
        }

        glLineWidth(rasterizedLineSegmentRenderable.thickness());

        glLineStipple(rasterizedLineSegmentRenderable.stippleFactor(),
                rasterizedLineSegmentRenderable.stipplePattern());

        glColor4f(rasterizedLineSegmentRenderable.red(),
                rasterizedLineSegmentRenderable.green(),
                rasterizedLineSegmentRenderable.blue(),
                rasterizedLineSegmentRenderable.alpha());

        glBegin(GL_LINES);

        glVertex2f(rasterizedLineSegmentRenderable.xLoc(), rasterizedLineSegmentRenderable.yLoc());

        glVertex2f(rasterizedLineSegmentRenderable.xLoc() + rasterizedLineSegmentRenderable.width(),
                rasterizedLineSegmentRenderable.yLoc() + rasterizedLineSegmentRenderable.height());

        glEnd();
    }

    @Override
    public String getInterfaceName() {
        return RendererType.class.getCanonicalName() + "<" +
                RasterizedLineSegmentRenderable.class.getCanonicalName() + ">";
    }
}
