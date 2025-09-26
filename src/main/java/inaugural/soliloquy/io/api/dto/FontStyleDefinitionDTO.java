package inaugural.soliloquy.io.api.dto;

public class FontStyleDefinitionDTO {
    public float additionalGlyphHorizontalTextureSpacing;
    public FontStyleDefinitionGlyphPropertyDTO[] glyphwiseAdditionalHorizontalTextureSpacing;
    public FontStyleDefinitionGlyphPropertyDTO[] glyphwiseAdditionalLeftBoundaryShift;
    public FontStyleDefinitionGlyphPropertyDTO[] glyphwiseWidthFactors;
    public float additionalGlyphVerticalTextureSpacing;

    public FontStyleDefinitionDTO(float additionalGlyphHorizontalTextureSpacing,
                                  FontStyleDefinitionGlyphPropertyDTO[]
                                          glyphwiseAdditionalHorizontalTextureSpacing,
                                  FontStyleDefinitionGlyphPropertyDTO[]
                                          glyphwiseAdditionalLeftBoundaryShift,
                                  FontStyleDefinitionGlyphPropertyDTO[]
                                          glyphwiseWidthFactors,
                                  float additionalGlyphVerticalTextureSpacing) {
        this.additionalGlyphHorizontalTextureSpacing = additionalGlyphHorizontalTextureSpacing;
        this.glyphwiseAdditionalHorizontalTextureSpacing =
                glyphwiseAdditionalHorizontalTextureSpacing;
        this.glyphwiseAdditionalLeftBoundaryShift = glyphwiseAdditionalLeftBoundaryShift;
        this.glyphwiseWidthFactors = glyphwiseWidthFactors;
        this.additionalGlyphVerticalTextureSpacing = additionalGlyphVerticalTextureSpacing;
    }
}
