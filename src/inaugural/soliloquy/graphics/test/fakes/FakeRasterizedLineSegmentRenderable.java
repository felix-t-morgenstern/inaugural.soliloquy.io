package inaugural.soliloquy.graphics.test.fakes;

import soliloquy.specs.graphics.renderables.RasterizedLineSegmentRenderable;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.rendering.FloatBox;

public class FakeRasterizedLineSegmentRenderable implements RasterizedLineSegmentRenderable {
    public float Thickness;
    public short StipplePattern;
    public int StippleFactor;
    public float Red;
    public float Green;
    public float Blue;
    public float Alpha;
    public FloatBox RenderingArea;
    public int Z;

    public FakeRasterizedLineSegmentRenderable(float thickness, short stipplePattern,
                                               int stippleFactor,
                                               float red, float green, float blue, float alpha,
                                               FloatBox renderingArea, int z) {
        Thickness = thickness;
        StipplePattern = stipplePattern;
        StippleFactor = stippleFactor;
        Red = red;
        Green = green;
        Blue = blue;
        Alpha = alpha;
        RenderingArea = renderingArea;
        Z = z;
    }

    @Override
    public float thickness() {
        return Thickness;
    }

    @Override
    public short stipplePattern() {
        return StipplePattern;
    }

    @Override
    public int stippleFactor() {
        return StippleFactor;
    }

    @Override
    public float red() {
        return Red;
    }

    @Override
    public float green() {
        return Green;
    }

    @Override
    public float blue() {
        return Blue;
    }

    @Override
    public float alpha() {
        return Alpha;
    }

    @Override
    public FloatBox renderingArea() {
        return RenderingArea;
    }

    @Override
    public int z() {
        return Z;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }

    @Override
    public Renderable<RasterizedLineSegmentRenderable> makeClone() {
        return null;
    }
}
