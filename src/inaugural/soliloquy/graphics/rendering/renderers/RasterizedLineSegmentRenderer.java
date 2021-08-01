package inaugural.soliloquy.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.RasterizedLineSegmentRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.Mesh;
import soliloquy.specs.graphics.rendering.Shader;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.awt.*;

import static inaugural.soliloquy.graphics.api.Constants.MAX_CHANNEL_VAL;
import static org.lwjgl.opengl.GL11.*;

public class RasterizedLineSegmentRenderer
        extends AbstractRenderer<RasterizedLineSegmentRenderable>
        implements Renderer<RasterizedLineSegmentRenderable> {
    public RasterizedLineSegmentRenderer(Long mostRecentTimestamp) {
        super(ARCHETYPE, mostRecentTimestamp);
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

        FloatBox renderingDimensions =
                Check.ifNull(rasterizedLineSegmentRenderable.getRenderingDimensionsProvider(),
                        "rasterizedLineSegmentRenderable.getRenderingDimensionsProvider()")
                        .provide(timestamp);
        float thickness = Check.ifNull(rasterizedLineSegmentRenderable.getThicknessProvider(),
                "rasterizedLineSegmentRenderable.getThicknessProvider()")
                .provide(timestamp);
        Color color = Check.ifNull(rasterizedLineSegmentRenderable.getColorProvider(),
                "rasterizedLineSegmentRenderable.getColorProvider()")
                .provide(timestamp);

        Check.throwOnLteZero(thickness,
                "rasterizedLineSegmentRenderable provided thickness");

        Check.throwOnEqualsValue(rasterizedLineSegmentRenderable.getStipplePattern(), (short)0x0000,
                "rasterizedLineSegmentRenderable.getStipplePattern()");

        Check.throwOnLtValue(rasterizedLineSegmentRenderable.getStippleFactor(), (short)1,
                "rasterizedLineSegmentRenderable.getStippleFactor()");
        Check.throwOnGtValue(rasterizedLineSegmentRenderable.getStippleFactor(), (short)256,
                "rasterizedLineSegmentRenderable.getStippleFactor()");

        Check.ifNull(color, "rasterizedLineSegmentRenderable provided color");
        Check.ifNull(renderingDimensions,
                "rasterizedLineSegmentRenderable provided rendering dimensions");
        Check.ifNull(rasterizedLineSegmentRenderable.uuid(), "rasterizedLineSegmentRenderable.id()");

        TIMESTAMP_VALIDATOR.validateTimestamp(this.getClass().getCanonicalName(), timestamp);

        glLineWidth(thickness);

        glLineStipple(rasterizedLineSegmentRenderable.getStippleFactor(),
                rasterizedLineSegmentRenderable.getStipplePattern());

        glColor4f(color.getRed() / MAX_CHANNEL_VAL,
                color.getGreen() / MAX_CHANNEL_VAL,
                color.getBlue() / MAX_CHANNEL_VAL,
                color.getAlpha() / MAX_CHANNEL_VAL);

        glBegin(GL_LINES);

        glVertex2f((renderingDimensions.leftX() * 2f) - 1f, -((renderingDimensions.topY() * 2f) - 1f));

        glVertex2f((renderingDimensions.rightX() * 2f) - 1f, -((renderingDimensions.bottomY() * 2f) - 1f));

        glEnd();
    }

    @Override
    protected String getUnparameterizedInterfaceName() {
        return Renderer.class.getCanonicalName();
    }

    private static final RasterizedLineSegmentRenderable ARCHETYPE =
            new RasterizedLineSegmentRenderable() {
                @Override
                public ProviderAtTime<FloatBox> getRenderingDimensionsProvider() {
                    return null;
                }

                @Override
                public void setRenderingDimensionsProvider(ProviderAtTime<FloatBox> providerAtTime) throws IllegalArgumentException {

                }

                @Override
                public ProviderAtTime<Float> getThicknessProvider() {
                    return null;
                }

                @Override
                public void setThicknessProvider(ProviderAtTime<Float> providerAtTime) throws IllegalArgumentException {

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
                public ProviderAtTime<Color> getColorProvider() {
                    return null;
                }

                @Override
                public void setColorProvider(ProviderAtTime<Color> providerAtTime) throws IllegalArgumentException {

                }

                @Override
                public EntityUuid uuid() {
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
                public String getInterfaceName() {
                    return RasterizedLineSegmentRenderable.class.getCanonicalName();
                }
    };
}
