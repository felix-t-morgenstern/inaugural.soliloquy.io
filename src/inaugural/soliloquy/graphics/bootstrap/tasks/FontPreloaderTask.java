package inaugural.soliloquy.graphics.bootstrap.tasks;

import inaugural.soliloquy.graphics.api.dto.FontDefinitionDTO;
import inaugural.soliloquy.graphics.api.dto.FontStyleDefinitionDTO;
import inaugural.soliloquy.graphics.api.dto.FontStyleDefinitionGlyphPropertyDTO;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.assets.Font;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontDefinition;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontStyleDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.function.Consumer;

public class FontPreloaderTask implements Runnable {
    private final AssetFactory<FontDefinition, Font> FACTORY;
    private final Collection<FontDefinition> FONT_DEFINITIONS;
    private final Consumer<Font> PROCESS_RESULT;

    /** @noinspection ConstantConditions*/
    public FontPreloaderTask(Collection<FontDefinitionDTO> fontDefinitionDTOs,
                             AssetFactory<FontDefinition, Font> factory,
                             Consumer<Font> processResult) {
        FACTORY = Check.ifNull(factory, "factory");
        Check.ifNull(fontDefinitionDTOs, "fontDefinitionDTOs");
        for (FontDefinitionDTO fontDefinitionDTO : fontDefinitionDTOs) {
            validateFontDefinitionDTO(fontDefinitionDTO);
        }
        FONT_DEFINITIONS = new ArrayList<>();
        fontDefinitionDTOs.forEach(fontDefinitionDTO ->
                FONT_DEFINITIONS.add(makeFontDefinition(fontDefinitionDTO)));
        PROCESS_RESULT = Check.ifNull(processResult, "processResult");
    }

    // NB: Extremely similar to FontImpl::validateFontDefinition; logic is duplicated, since classes are different
    private void validateFontDefinitionDTO(FontDefinitionDTO fontDefinitionDTO) {
        Check.ifNull(fontDefinitionDTO, "fontDefinitionDTO");
        Check.ifNullOrEmpty(fontDefinitionDTO.id, "fontDefinitionDTO.id()");
        Check.ifNullOrEmpty(fontDefinitionDTO.relativeLocation,
                "fontDefinitionDTO.relativeLocation");
        Check.throwOnLteZero(fontDefinitionDTO.maxLosslessFontSize,
                "fontDefinitionDTO.maxLosslessFontSize");
        Check.throwOnLtValue(fontDefinitionDTO.leadingAdjustment, 0f,
                "fontDefinitionDTO.leadingAdjustment");
        Check.throwOnGteValue(fontDefinitionDTO.leadingAdjustment, 1f,
                "fontDefinitionDTO.leadingAdjustment");
        Check.ifNull(fontDefinitionDTO.plain, "fontDefinitionDTO.plain");
        validateFontStyleDefinitionDTO(fontDefinitionDTO.plain,
                fontDefinitionDTO.leadingAdjustment);
        Check.ifNull(fontDefinitionDTO.italic, "fontDefinitionDTO.italic");
        validateFontStyleDefinitionDTO(fontDefinitionDTO.italic,
                fontDefinitionDTO.leadingAdjustment);
        Check.ifNull(fontDefinitionDTO.bold, "fontDefinitionDTO.bold");
        validateFontStyleDefinitionDTO(fontDefinitionDTO.bold,
                fontDefinitionDTO.leadingAdjustment);
        Check.ifNull(fontDefinitionDTO.boldItalic, "fontDefinitionDTO.boldItalic");
        validateFontStyleDefinitionDTO(fontDefinitionDTO.boldItalic,
                fontDefinitionDTO.leadingAdjustment);
    }

    private void validateFontStyleDefinitionDTO(FontStyleDefinitionDTO fontStyleDefinitionDTO,
                                                float leadingAdjustment) {
        Check.throwOnLtValue(fontStyleDefinitionDTO.additionalGlyphHorizontalTextureSpacing, 0f,
                "fontStyleDefinitionDTO.additionalGlyphHorizontalTextureSpacing");
        Check.throwOnLtValue(fontStyleDefinitionDTO.additionalGlyphVerticalTextureSpacing, 0f,
                "fontStyleDefinitionDTO.additionalGlyphVerticalTextureSpacing");
        Check.ifNull(fontStyleDefinitionDTO.glyphwiseAdditionalHorizontalTextureSpacing,
                "fontStyleDefinitionDTO.glyphwiseAdditionalHorizontalTextureSpacing");
        Check.ifNull(fontStyleDefinitionDTO.glyphwiseAdditionalLeftBoundaryShift,
                "fontStyleDefinitionDTO.glyphwiseAdditionalLeftBoundaryShift");
        Check.throwOnGteValue(
                leadingAdjustment + fontStyleDefinitionDTO.additionalGlyphVerticalTextureSpacing,
                1f,
                "sum of leadingAdjustment and " +
                        "fontStyleDefinitionDTO.additionalGlyphVerticalTextureSpacing");
    }

    @Override
    public void run() {
        FONT_DEFINITIONS.forEach(fontDefinition ->
                PROCESS_RESULT.accept(FACTORY.make(fontDefinition)));
    }

    private FontDefinition makeFontDefinition(FontDefinitionDTO dto) {
        return new FontDefinition(
                dto.id,
                dto.relativeLocation,
                dto.maxLosslessFontSize,
                dto.leadingAdjustment,
                makeFontStyleDefinition(dto.plain),
                makeFontStyleDefinition(dto.italic),
                makeFontStyleDefinition(dto.bold),
                makeFontStyleDefinition(dto.boldItalic)
        );
    }

    private FontStyleDefinition makeFontStyleDefinition(FontStyleDefinitionDTO dto) {
        return new FontStyleDefinition(
                dto.additionalGlyphHorizontalTextureSpacing,
                makeFontStyleDefinitionGlyphProperties(
                        dto.glyphwiseAdditionalHorizontalTextureSpacing),
                makeFontStyleDefinitionGlyphProperties(
                        dto.glyphwiseAdditionalLeftBoundaryShift),
                dto.additionalGlyphVerticalTextureSpacing
        );
    }

    private HashMap<Character, Float> makeFontStyleDefinitionGlyphProperties(
            FontStyleDefinitionGlyphPropertyDTO[] dtos) {
        HashMap<Character, Float> glyphProperties = new HashMap<>();

        for (FontStyleDefinitionGlyphPropertyDTO dto : dtos) {
            glyphProperties.put(dto.glyph, dto.amount);
        }

        return glyphProperties;
    }
}
