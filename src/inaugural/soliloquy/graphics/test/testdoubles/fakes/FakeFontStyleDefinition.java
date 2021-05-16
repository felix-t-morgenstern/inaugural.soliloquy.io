package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontStyleDefinition;

import java.util.Map;

public class FakeFontStyleDefinition implements FontStyleDefinition {
    public float AdditionalGlyphHorizontalTextureSpacing;
    public Map<Character, Float> GlyphwiseAdditionalHorizontalTextureSpacing;
    public float AdditionalGlyphVerticalTextureSpacing;

    public FakeFontStyleDefinition(float additionalGlyphHorizontalTextureSpacing,
                                   Map<Character, Float>
                                           glyphwiseAdditionalHorizontalTextureSpacing,
                                   float additionalGlyphVerticalTextureSpacing) {
        AdditionalGlyphHorizontalTextureSpacing = additionalGlyphHorizontalTextureSpacing;
        GlyphwiseAdditionalHorizontalTextureSpacing = glyphwiseAdditionalHorizontalTextureSpacing;
        AdditionalGlyphVerticalTextureSpacing = additionalGlyphVerticalTextureSpacing;
    }

    @Override
    public float additionalGlyphHorizontalTextureSpacing() {
        return AdditionalGlyphHorizontalTextureSpacing;
    }

    @Override
    public Map<Character, Float> glyphwiseAdditionalHorizontalTextureSpacing() {
        return GlyphwiseAdditionalHorizontalTextureSpacing;
    }

    @Override
    public float additionalGlyphVerticalTextureSpacing() {
        return AdditionalGlyphVerticalTextureSpacing;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
