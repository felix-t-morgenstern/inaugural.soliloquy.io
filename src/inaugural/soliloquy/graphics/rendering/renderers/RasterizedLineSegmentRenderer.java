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

        FloatBox renderingArea =
                Check.ifNull(rasterizedLineSegmentRenderable.renderingAreaProvider(),
                        "rasterizedLineSegmentRenderable.renderingAreaProvider()")
                        .provide(timestamp);
        float thickness = Check.ifNull(rasterizedLineSegmentRenderable.thicknessProvider(),
                "rasterizedLineSegmentRenderable.thicknessProvider()")
                .provide(timestamp);
        Color color = Check.ifNull(rasterizedLineSegmentRenderable.colorProvider(),
                "rasterizedLineSegmentRenderable.colorProvider()")
                .provide(timestamp);

        Check.throwOnLteZero(thickness,
                "rasterizedLineSegmentRenderable provided thickness");

        Check.throwOnEqualsValue(rasterizedLineSegmentRenderable.stipplePattern(), (short)0x0000,
                "rasterizedLineSegmentRenderable.stipplePattern()");

        Check.throwOnLtValue(rasterizedLineSegmentRenderable.stippleFactor(), (short)1,
                "rasterizedLineSegmentRenderable.stippleFactor()");
        Check.throwOnGtValue(rasterizedLineSegmentRenderable.stippleFactor(), (short)256,
                "rasterizedLineSegmentRenderable.stippleFactor()");

        Check.ifNull(color, "rasterizedLineSegmentRenderable provided color");
        Check.ifNull(renderingArea,
                "rasterizedLineSegmentRenderable provided rendering area");
        Check.ifNull(rasterizedLineSegmentRenderable.uuid(), "rasterizedLineSegmentRenderable.id()");

        validateTimestamp(timestamp, "RasterizedLineSegmentRenderer");

        glLineWidth(thickness);

        glLineStipple(rasterizedLineSegmentRenderable.stippleFactor(),
                rasterizedLineSegmentRenderable.stipplePattern());

        glColor4f(color.getRed() / MAX_CHANNEL_VAL,
                color.getGreen() / MAX_CHANNEL_VAL,
                color.getBlue() / MAX_CHANNEL_VAL,
                color.getAlpha() / MAX_CHANNEL_VAL);

        glBegin(GL_LINES);

        glVertex2f((renderingArea.leftX() * 2f) - 1f, -((renderingArea.topY() * 2f) - 1f));

        glVertex2f((renderingArea.rightX() * 2f) - 1f, -((renderingArea.bottomY() * 2f) - 1f));

        glEnd();
    }

    @Override
    protected String getUnparameterizedInterfaceName() {
        return Renderer.class.getCanonicalName();
    }

    private static final RasterizedLineSegmentRenderable ARCHETYPE =
            new RasterizedLineSegmentRenderable() {
                @Override
                public EntityUuid uuid() {
                    return null;
                }

                @Override
                public ProviderAtTime<FloatBox> renderingAreaProvider() {
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
                public ProviderAtTime<Float> thicknessProvider() {
                    return null;
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
                public ProviderAtTime<Color> colorProvider() {
                    return null;
                }

                @Override
                public String getInterfaceName() {
                    return RasterizedLineSegmentRenderable.class.getCanonicalName();
                }
    };
}
