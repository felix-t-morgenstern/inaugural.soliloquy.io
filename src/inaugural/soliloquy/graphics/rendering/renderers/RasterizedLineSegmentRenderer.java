package inaugural.soliloquy.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.renderables.RasterizedLineSegmentRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.UUID;

import static inaugural.soliloquy.graphics.api.Constants.MAX_CHANNEL_VAL;
import static org.lwjgl.opengl.GL11.*;

public class RasterizedLineSegmentRenderer
        extends AbstractPointDrawingRenderer<RasterizedLineSegmentRenderable>
        implements Renderer<RasterizedLineSegmentRenderable> {

    public RasterizedLineSegmentRenderer(Long mostRecentTimestamp) {
        super(ARCHETYPE, mostRecentTimestamp);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void render(RasterizedLineSegmentRenderable rasterizedLineSegmentRenderable,
                       long timestamp)
            throws IllegalArgumentException {
        Check.ifNull(rasterizedLineSegmentRenderable, "rasterizedLineSegmentRenderable");

        Vertex vertex1 =
                Check.ifNull(rasterizedLineSegmentRenderable.getVertex1Provider(),
                        "rasterizedLineSegmentRenderable.getVertex1Provider()")
                        .provide(timestamp);
        Vertex vertex2 =
                Check.ifNull(rasterizedLineSegmentRenderable.getVertex2Provider(),
                        "rasterizedLineSegmentRenderable.getVertex2Provider()")
                        .provide(timestamp);
        float thickness = Check.ifNull(
                Check.ifNull(rasterizedLineSegmentRenderable.getThicknessProvider(),
                        "rasterizedLineSegmentRenderable.getThicknessProvider()")
                        .provide(timestamp),
                "value provided by " +
                        "rasterizedLineSegmentRenderable.getThicknessProvider()");
        Color color = Check.ifNull(rasterizedLineSegmentRenderable.getColorProvider(),
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

        if (_mesh == null) {
            throw new IllegalStateException(
                    "RasterizedLineSegmentRenderer.render: mesh cannot be null");
        }
        if (_shader == null) {
            throw new IllegalStateException(
                    "RasterizedLineSegmentRenderer.render: shader cannot be null");
        }
        _mesh.unbind();
        _shader.unbind();

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
    protected String getUnparameterizedInterfaceName() {
        return Renderer.class.getCanonicalName();
    }

    private static final RasterizedLineSegmentRenderable ARCHETYPE =
            new RasterizedLineSegmentRenderable() {

                @Override
                public UUID uuid() {
                    return null;
                }

                @Override
                public int getZ() {
                    return 0;
                }

                @Override
                public void setZ(int i) {

                }

                @Override
                public void delete() {

                }

                @Override
                public ProviderAtTime<Vertex> getVertex1Provider() {
                    return null;
                }

                @Override
                public void setVertex1Provider(
                        ProviderAtTime<Vertex> providerAtTime)
                        throws IllegalArgumentException {

                }

                @Override
                public ProviderAtTime<Vertex> getVertex2Provider() {
                    return null;
                }

                @Override
                public void setVertex2Provider(
                        ProviderAtTime<Vertex> providerAtTime)
                        throws IllegalArgumentException {

                }

                @Override
                public ProviderAtTime<Float> getThicknessProvider() {
                    return null;
                }

                @Override
                public void setThicknessProvider(ProviderAtTime<Float> providerAtTime)
                        throws IllegalArgumentException {

                }

                @Override
                public ProviderAtTime<Color> getColorProvider() {
                    return null;
                }

                @Override
                public void setColorProvider(ProviderAtTime<Color> providerAtTime)
                        throws IllegalArgumentException {

                }

                @Override
                public short getStipplePattern() {
                    return 0;
                }

                @Override
                public void setStipplePattern(short i) throws IllegalArgumentException {

                }

                @Override
                public short getStippleFactor() {
                    return 0;
                }

                @Override
                public void setStippleFactor(short i) throws IllegalArgumentException {

                }

                @Override
                public String getInterfaceName() {
                    return RasterizedLineSegmentRenderable.class.getCanonicalName();
                }
            };

    @Override
    protected String className() {
        return RasterizedLineSegmentRenderer.class.getSimpleName();
    }
}
