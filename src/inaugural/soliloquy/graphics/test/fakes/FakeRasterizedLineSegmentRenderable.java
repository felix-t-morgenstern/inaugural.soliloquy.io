package inaugural.soliloquy.graphics.test.fakes;

import soliloquy.specs.graphics.renderables.RasterizedLineSegmentRenderable;

public class FakeRasterizedLineSegmentRenderable implements RasterizedLineSegmentRenderable {
    public float Thickness;
    public short StipplePattern;
    public int StippleFactor;
    public float Red;
    public float Green;
    public float Blue;
    public float Alpha;
    public float XLoc;
    public float YLoc;
    public float Width;
    public float Height;
    public int Z;

    public FakeRasterizedLineSegmentRenderable(float thickness, short stipplePattern,
                                               int stippleFactor,
                                               float red, float green, float blue, float alpha,
                                               float xLoc,
                                               float yLoc, float width, float height, int z) {
        Thickness = thickness;
        StipplePattern = stipplePattern;
        StippleFactor = stippleFactor;
        Red = red;
        Green = green;
        Blue = blue;
        Alpha = alpha;
        XLoc = xLoc;
        YLoc = yLoc;
        Width = width;
        Height = height;
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
    public float xLoc() {
        return XLoc;
    }

    @Override
    public float yLoc() {
        return YLoc;
    }

    @Override
    public float width() {
        return Width;
    }

    @Override
    public float height() {
        return Height;
    }

    @Override
    public int z() {
        return Z;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
