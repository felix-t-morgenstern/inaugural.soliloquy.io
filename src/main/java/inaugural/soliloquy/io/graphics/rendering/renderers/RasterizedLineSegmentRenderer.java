package inaugural.soliloquy.io.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.io.graphics.renderables.RasterizedLineSegmentRenderable;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import static inaugural.soliloquy.io.api.Constants.MAX_CHANNEL_VAL;
import static org.lwjgl.opengl.GL11.*;

public class RasterizedLineSegmentRenderer
        extends AbstractPointDrawingRenderer<RasterizedLineSegmentRenderable>
        implements Renderer<RasterizedLineSegmentRenderable> {

    public RasterizedLineSegmentRenderer(TimestampValidator timestampValidator,
                                         RenderingBoundaries renderingBoundaries) {
        super(timestampValidator, renderingBoundaries);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void render(RasterizedLineSegmentRenderable renderable,
                       long timestamp)
            throws IllegalArgumentException {
        Check.ifNull(renderable, "renderable");

        var vertex1 =
                Check.ifNull(renderable.getVertex1Provider(),
                        "renderable.getVertex1Provider()")
                        .provide(timestamp);
        var vertex2 =
                Check.ifNull(renderable.getVertex2Provider(),
                        "renderable.getVertex2Provider()")
                        .provide(timestamp);
        float thickness = Check.ifNull(
                Check.ifNull(renderable.getThicknessProvider(),
                        "renderable.getThicknessProvider()")
                        .provide(timestamp),
                "value provided by " +
                        "renderable.getThicknessProvider()");
        var color = Check.ifNull(renderable.getColorProvider(),
                "renderable.getColorProvider()")
                .provide(timestamp);

        Check.throwOnLteZero(thickness,
                "renderable provided thickness");

        Check.throwOnLtValue(renderable.getStippleFactor(), (short) 1,
                "renderable.getStippleFactor()");
        Check.throwOnGtValue(renderable.getStippleFactor(), (short) 256,
                "renderable.getStippleFactor()");

        Check.ifNull(color, "renderable provided color");
        Check.ifNull(vertex1, "renderable provided vertex 1");
        Check.ifNull(vertex2, "renderable provided vertex 2");
        Check.ifNull(renderable.uuid(),
                "renderable.id()");

        TIMESTAMP_VALIDATOR.validateTimestamp(this.getClass().getCanonicalName(), timestamp);

        if (mesh == null) {
            throw new IllegalStateException(
                    "RasterizedLineSegmentRenderer.render: mesh cannot be null");
        }
        if (shader == null) {
            throw new IllegalStateException(
                    "RasterizedLineSegmentRenderer.render: shader cannot be null");
        }
        mesh.unbind();
        shader.unbind();

        glLineWidth(thickness);

        var stipplePattern = renderable.getStipplePattern();
        if (stipplePattern != null) {
            Check.throwOnEqualsValue(stipplePattern,
                    (short) 0x0000,
                    "renderable.getStipplePattern()");

            glEnable(GL_LINE_STIPPLE);
            glLineStipple(renderable.getStippleFactor(),
                    stipplePattern);
        }
        else {
            glDisable(GL_LINE_STIPPLE);
        }

        glColor4f(color.getRed() / MAX_CHANNEL_VAL,
                color.getGreen() / MAX_CHANNEL_VAL,
                color.getBlue() / MAX_CHANNEL_VAL,
                color.getAlpha() / MAX_CHANNEL_VAL);

        glBegin(GL_LINES);

        drawPoint(vertex1);
        drawPoint(vertex2);

        glEnd();
    }

    @Override
    protected String className() {
        return RasterizedLineSegmentRenderer.class.getSimpleName();
    }
}
