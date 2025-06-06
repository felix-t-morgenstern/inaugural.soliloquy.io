package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.common.valueobjects.Coordinate2d;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.graphics.assets.FontStyleInfo;

import java.util.Map;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;

public class FakeFontStyleInfo implements FontStyleInfo {
    public Map<Character, FloatBox> Glyphs = mapOf();
    public Map<Character, Float> GlyphwiseAdditionalHorizontalTextureSpacing = mapOf();
    public float TextureWidthToHeightRatio;

    @Override
    public FloatBox getUvCoordinatesForGlyph(char c) throws IllegalArgumentException {
        return Glyphs.get(c);
    }

    @Override
    public Coordinate2d textureDimensions() {
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
}
