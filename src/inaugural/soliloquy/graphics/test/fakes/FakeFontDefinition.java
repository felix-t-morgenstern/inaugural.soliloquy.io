package inaugural.soliloquy.graphics.test.fakes;

import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontDefinition;

public class FakeFontDefinition implements FontDefinition {
    public String Id;
    public String RelativeLocation;
    public float MaxLosslessFontSize;
    public float AdditionalGlyphPadding;
    public float LeadingAdjustment;

    public FakeFontDefinition(String id, String relativeLocation, float maxLosslessFontSize,
                              float additionalGlyphPadding, float leadingAdjustment) {
        Id = id;
        RelativeLocation = relativeLocation;
        MaxLosslessFontSize = maxLosslessFontSize;
        AdditionalGlyphPadding = additionalGlyphPadding;
        LeadingAdjustment = leadingAdjustment;
    }

    @Override
    public String relativeLocation() {
        return RelativeLocation;
    }

    @Override
    public float maxLosslessFontSize() {
        return MaxLosslessFontSize;
    }

    @Override
    public float additionalGlyphPadding() {
        return AdditionalGlyphPadding;
    }

    @Override
    public float leadingAdjustment() {
        return LeadingAdjustment;
    }

    @Override
    public String id() throws IllegalStateException {
        return Id;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
