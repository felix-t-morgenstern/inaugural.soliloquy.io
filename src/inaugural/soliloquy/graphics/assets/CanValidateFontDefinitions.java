package inaugural.soliloquy.graphics.assets;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontDefinition;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontStyleDefinition;

public abstract class CanValidateFontDefinitions {
    protected static void validateFontDefinition(FontDefinition fontDefinition) {
        Check.ifNull(fontDefinition, "fontDefinition");
        Check.ifNullOrEmpty(fontDefinition.id(), "fontDefinition.id()");
        Check.ifNullOrEmpty(fontDefinition.relativeLocation(),
                "fontDefinition.relativeLocation()");
        Check.throwOnLteZero(fontDefinition.maxLosslessFontSize(),
                "fontDefinition.maxLosslessFontSize()");
        Check.throwOnLtValue(fontDefinition.leadingAdjustment(), 0f,
                "fontDefinition.leadingAdjustment()");
        Check.throwOnGteValue(fontDefinition.leadingAdjustment(), 1f,
                "fontDefinition.leadingAdjustment()");
        Check.ifNull(fontDefinition.plain(), "fontDefinition.plain()");
        validateFontStyleDefinition(fontDefinition.plain(),
                fontDefinition.leadingAdjustment());
        Check.ifNull(fontDefinition.italic(), "fontDefinition.italic()");
        validateFontStyleDefinition(fontDefinition.italic(),
                fontDefinition.leadingAdjustment());
        Check.ifNull(fontDefinition.bold(), "fontDefinition.bold()");
        validateFontStyleDefinition(fontDefinition.bold(),
                fontDefinition.leadingAdjustment());
        Check.ifNull(fontDefinition.boldItalic(), "fontDefinition.boldItalic()");
        validateFontStyleDefinition(fontDefinition.boldItalic(),
                fontDefinition.leadingAdjustment());
    }

    private static void validateFontStyleDefinition(FontStyleDefinition fontStyleDefinition,
                                                    float leadingAdjustment) {
        Check.throwOnLtValue(fontStyleDefinition.additionalGlyphHorizontalPadding(), 0f,
                "fontStyleDefinition.additionalGlyphHorizontalPadding()");
        Check.throwOnLtValue(fontStyleDefinition.additionalGlyphVerticalPadding(), 0f,
                "fontStyleDefinition.additionalGlyphVerticalPadding()");
        Check.throwOnGteValue(
                leadingAdjustment + fontStyleDefinition.additionalGlyphVerticalPadding(), 1f,
                "sum of leadingAdjustment and " +
                        "fontStyleDefinition.additionalGlyphVerticalPadding()");
        // TODO: Check whether glyphwiseAdditionalHorizontalPadding is null
    }
}
