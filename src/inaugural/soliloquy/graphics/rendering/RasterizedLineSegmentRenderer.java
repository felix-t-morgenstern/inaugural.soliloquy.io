package inaugural.soliloquy.graphics.rendering;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.RasterizedLineSegmentRenderable;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.Mesh;
import soliloquy.specs.graphics.rendering.Renderer;
import soliloquy.specs.graphics.rendering.Shader;

import static org.lwjgl.opengl.GL11.*;

public class RasterizedLineSegmentRenderer
        extends AbstractRenderer<RasterizedLineSegmentRenderable>
        implements Renderer<RasterizedLineSegmentRenderable> {
    public RasterizedLineSegmentRenderer() {
        super(ARCHETYPE);
    }

    @Override
    public void setMesh(Mesh mesh) throws IllegalArgumentException {
        throw new UnsupportedOperationException("RasterizedLineSegmentRenderer.setMesh: " +
                "this Renderer does not require a Mesh");
    }

    @Override
    public void setShader(Shader shader) throws IllegalArgumentException {
        throw new UnsupportedOperationException("RasterizedLineSegmentRenderer.setShader: " +
                "this Renderer does not require a Shader");
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void render(RasterizedLineSegmentRenderable rasterizedLineSegmentRenderable,
                       long timestamp)
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

        validateTimestamp(timestamp);

        glLineWidth(rasterizedLineSegmentRenderable.thickness());

        glLineStipple(rasterizedLineSegmentRenderable.stippleFactor(),
                rasterizedLineSegmentRenderable.stipplePattern());

        glColor4f(rasterizedLineSegmentRenderable.red(),
                rasterizedLineSegmentRenderable.green(),
                rasterizedLineSegmentRenderable.blue(),
                rasterizedLineSegmentRenderable.alpha());

        glBegin(GL_LINES);

        glVertex2f(rasterizedLineSegmentRenderable.renderingArea().leftX(),
                rasterizedLineSegmentRenderable.renderingArea().topY());

        glVertex2f(rasterizedLineSegmentRenderable.renderingArea().rightX(),
                rasterizedLineSegmentRenderable.renderingArea().bottomY());

        glEnd();
    }

    @Override
    protected String getUnparameterizedInterfaceName() {
        return Renderer.class.getCanonicalName();
    }

    private static final RasterizedLineSegmentRenderable ARCHETYPE =
            new RasterizedLineSegmentRenderable() {
        @Override
        public String getInterfaceName() {
            return RasterizedLineSegmentRenderable.class.getCanonicalName();
        }

        @Override
        public EntityUuid id() {
            return null;
        }

        @Override
        public FloatBox renderingArea() {
            return null;
        }

        @Override
        public int z() {
            return 0;
        }

        @Override
        public void delete() {

        }

        @Override
        public float thickness() {
            return 0;
        }

        @Override
        public short stipplePattern() {
            return 0;
        }

        @Override
        public int stippleFactor() {
            return 0;
        }

        @Override
        public float red() {
            return 0;
        }

        @Override
        public float green() {
            return 0;
        }

        @Override
        public float blue() {
            return 0;
        }

        @Override
        public float alpha() {
            return 0;
        }
    };
}
