package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontStyleDefinition;

import java.util.Map;

public class FakeFontStyleDefinition implements FontStyleDefinition {
    public float AdditionalGlyphHorizontalPadding;
    public Map<Character, Float> GlyphwiseAdditionalHorizontalPadding;
    public float AdditionalGlyphVerticalPadding;

    public FakeFontStyleDefinition(float additionalGlyphHorizontalPadding,
                                   Map<Character, Float> glyphwiseAdditionalHorizontalPadding,
                                   float additionalGlyphVerticalPadding) {
        AdditionalGlyphHorizontalPadding = additionalGlyphHorizontalPadding;
        GlyphwiseAdditionalHorizontalPadding = glyphwiseAdditionalHorizontalPadding;
        AdditionalGlyphVerticalPadding = additionalGlyphVerticalPadding;
    }

    @Override
    public float additionalGlyphHorizontalPadding() {
        return AdditionalGlyphHorizontalPadding;
    }

    @Override
    public Map<Character, Float> glyphwiseAdditionalHorizontalPadding() {
        return GlyphwiseAdditionalHorizontalPadding;
    }

    @Override
    public float additionalGlyphVerticalPadding() {
        return AdditionalGlyphVerticalPadding;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
