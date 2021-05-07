package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontDefinition;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontStyleDefinition;

public class FakeFontDefinition implements FontDefinition {
    public String Id;
    public String RelativeLocation;
    public float MaxLosslessFontSize;
    public FakeFontStyleDefinition Plain;
    public FakeFontStyleDefinition Italic;
    public FakeFontStyleDefinition Bold;
    public FakeFontStyleDefinition BoldItalic;
    public float LeadingAdjustment;

    public FakeFontDefinition(String id, String relativeLocation, float maxLosslessFontSize,
                              FakeFontStyleDefinition plain,
                              FakeFontStyleDefinition italic,
                              FakeFontStyleDefinition bold,
                              FakeFontStyleDefinition boldItalic,
                              float leadingAdjustment) {
        Id = id;
        RelativeLocation = relativeLocation;
        MaxLosslessFontSize = maxLosslessFontSize;
        Plain = plain;
        Italic = italic;
        Bold = bold;
        BoldItalic = boldItalic;
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
    public float leadingAdjustment() {
        return LeadingAdjustment;
    }

    @Override
    public FontStyleDefinition plain() {
        return Plain;
    }

    @Override
    public FontStyleDefinition italic() {
        return Italic;
    }

    @Override
    public FontStyleDefinition bold() {
        return Bold;
    }

    @Override
    public FontStyleDefinition boldItalic() {
        return BoldItalic;
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
