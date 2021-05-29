package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.common.valueobjects.Coordinate;
import soliloquy.specs.graphics.assets.FontStyleInfo;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.util.HashMap;
import java.util.Map;

public class FakeFontStyleInfo implements FontStyleInfo {
    public HashMap<Character,FloatBox> Glyphs = new HashMap<>();
    public HashMap<Character,Float> GlyphwiseAdditionalHorizontalTextureSpacing = new HashMap<>();
    public float TextureWidthToHeightRatio;

    @Override
    public FloatBox getUvCoordinatesForGlyph(char c) throws IllegalArgumentException {
        return Glyphs.get(c);
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
    public float additionalHorizontalTextureSpacing() {
        return 0;
    }

    @Override
    public Map<Character, Float> glyphwiseAdditionalHorizontalTextureSpacing() {
        return GlyphwiseAdditionalHorizontalTextureSpacing;
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
