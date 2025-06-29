package inaugural.soliloquy.io.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.io.graphics.renderables.RasterizedLineSegmentRenderable;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import static inaugural.soliloquy.io.api.Constants.MAX_CHANNEL_VAL;
import static org.lwjgl.opengl.GL11.*;

public class RasterizedLineSegmentRenderer
        extends AbstractPointDrawingRenderer<RasterizedLineSegmentRenderable>
        implements Renderer<RasterizedLineSegmentRenderable> {

    public RasterizedLineSegmentRenderer(Long mostRecentTimestamp) {
        super(mostRecentTimestamp);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void render(RasterizedLineSegmentRenderable rasterizedLineSegmentRenderable,
                       long timestamp)
            throws IllegalArgumentException {
        Check.ifNull(rasterizedLineSegmentRenderable, "rasterizedLineSegmentRenderable");

        var vertex1 =
                Check.ifNull(rasterizedLineSegmentRenderable.getVertex1Provider(),
                        "rasterizedLineSegmentRenderable.getVertex1Provider()")
                        .provide(timestamp);
        var vertex2 =
                Check.ifNull(rasterizedLineSegmentRenderable.getVertex2Provider(),
                        "rasterizedLineSegmentRenderable.getVertex2Provider()")
                        .provide(timestamp);
        float thickness = Check.ifNull(
                Check.ifNull(rasterizedLineSegmentRenderable.getThicknessProvider(),
                        "rasterizedLineSegmentRenderable.getThicknessProvider()")
                        .provide(timestamp),
                "value provided by " +
                        "rasterizedLineSegmentRenderable.getThicknessProvider()");
        var color = Check.ifNull(rasterizedLineSegmentRenderable.getColorProvider(),
                "rasterizedLineSegmentRenderable.getColorProvider()")
                .provide(timestamp);

        Check.throwOnLteZero(thickness,
                "rasterizedLineSegmentRenderable provided thickness");

        Check.throwOnEqualsValue(rasterizedLineSegmentRenderable.getStipplePattern(),
                (short) 0x0000,
                "rasterizedLineSegmentRenderable.getStipplePattern()");

        Check.throwOnLtValue(rasterizedLineSegmentRenderable.getStippleFactor(), (short) 1,
                "rasterizedLineSegmentRenderable.getStippleFactor()");
        Check.throwOnGtValue(rasterizedLineSegmentRenderable.getStippleFactor(), (short) 256,
                "rasterizedLineSegmentRenderable.getStippleFactor()");

        Check.ifNull(color, "rasterizedLineSegmentRenderable provided color");
        Check.ifNull(vertex1, "rasterizedLineSegmentRenderable provided vertex 1");
        Check.ifNull(vertex2, "rasterizedLineSegmentRenderable provided vertex 2");
        Check.ifNull(rasterizedLineSegmentRenderable.uuid(),
                "rasterizedLineSegmentRenderable.id()");

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

        glLineStipple(rasterizedLineSegmentRenderable.getStippleFactor(),
                rasterizedLineSegmentRenderable.getStipplePattern());

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
