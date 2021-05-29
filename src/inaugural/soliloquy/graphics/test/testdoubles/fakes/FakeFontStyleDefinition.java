package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontStyleDefinition;

import java.util.Map;

public class FakeFontStyleDefinition implements FontStyleDefinition {
    public float AdditionalGlyphHorizontalTextureSpacing;
    public Map<Character, Float> GlyphwiseAdditionalHorizontalTextureSpacing;
    public Map<Character, Float> GlyphwiseAdditionalLeftBoundaryShift;
    public float AdditionalGlyphVerticalTextureSpacing;

    public FakeFontStyleDefinition(float additionalGlyphHorizontalTextureSpacing,
                                   Map<Character, Float>
                                           glyphwiseAdditionalHorizontalTextureSpacing,
                                   Map<Character, Float> glyphwiseAdditionalLeftBoundaryShift,
                                   float additionalGlyphVerticalTextureSpacing) {
        AdditionalGlyphHorizontalTextureSpacing = additionalGlyphHorizontalTextureSpacing;
        GlyphwiseAdditionalHorizontalTextureSpacing = glyphwiseAdditionalHorizontalTextureSpacing;
        GlyphwiseAdditionalLeftBoundaryShift = glyphwiseAdditionalLeftBoundaryShift;
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
    public Map<Character, Float> glyphwiseAdditionalLeftBoundaryShift() {
        return GlyphwiseAdditionalLeftBoundaryShift;
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
