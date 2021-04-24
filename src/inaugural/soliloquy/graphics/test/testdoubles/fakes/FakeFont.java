package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.common.valueobjects.Coordinate;
import soliloquy.specs.graphics.assets.Font;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.util.HashMap;
import java.util.Map;

public class FakeFont implements Font {
    public HashMap<Character,FloatBox> Glyphs = new HashMap<>();
    public HashMap<Character,FloatBox> GlyphsItalic = new HashMap<>();
    public HashMap<Character,FloatBox> GlyphsBold = new HashMap<>();
    public HashMap<Character,FloatBox> GlyphsBoldItalic = new HashMap<>();
    public float TextureWidthToHeightRatio;
    public float TextureWidthToHeightRatioItalic;
    public float TextureWidthToHeightRatioBold;
    public float TextureWidthToHeightRatioBoldItalic;

    @Override
    public FloatBox getUvCoordinatesForGlyph(char c) throws IllegalArgumentException {
        return Glyphs.get(c);
    }

    @Override
    public FloatBox getUvCoordinatesForGlyphItalic(char c) throws IllegalArgumentException {
        return GlyphsItalic.get(c);
    }

    @Override
    public FloatBox getUvCoordinatesForGlyphBold(char c) throws IllegalArgumentException {
        return GlyphsBold.get(c);
    }

    @Override
    public FloatBox getUvCoordinatesForGlyphBoldItalic(char c) throws IllegalArgumentException {
        return GlyphsBoldItalic.get(c);
    }

    @Override
    public Map<Character, Float> glyphwiseAdditionalHorizontalPadding() {
        return null;
    }

    @Override
    public Coordinate textureDimensions() {
        return null;
    }

    @Override
    public float textureWidthToHeightRatio() {
        return TextureWidthToHeightRatio;
    }

    @Override
    public int textureIdItalic() {
        return 0;
    }

    @Override
    public Coordinate textureDimensionsItalic() {
        return null;
    }

    @Override
    public float textureWidthToHeightRatioItalic() {
        return TextureWidthToHeightRatioItalic;
    }

    @Override
    public int textureIdBold() {
        return 0;
    }

    @Override
    public Coordinate textureDimensionsBold() {
        return null;
    }

    @Override
    public float textureWidthToHeightRatioBold() {
        return TextureWidthToHeightRatioBold;
    }

    @Override
    public int textureIdBoldItalic() {
        return 0;
    }

    @Override
    public Coordinate textureDimensionsBoldItalic() {
        return null;
    }

    @Override
    public float textureWidthToHeightRatioBoldItalic() {
        return TextureWidthToHeightRatioBoldItalic;
    }

    @Override
    public String id() throws IllegalStateException {
        return null;
    }

    @Override
    public int textureId() {
        return 0;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
